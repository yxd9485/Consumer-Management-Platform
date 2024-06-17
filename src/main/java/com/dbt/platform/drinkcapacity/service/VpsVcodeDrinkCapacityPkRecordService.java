package com.dbt.platform.drinkcapacity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.PackRecordRouterUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.appuser.bean.VpsConsumerAccountInfo;
import com.dbt.platform.appuser.service.VpsConsumerAccountInfoService;
import com.dbt.platform.drinkcapacity.bean.VpsVcodeDrinkCapacityPkRecord;
/**
 * 酒量1V1记录表Service
 */
import com.dbt.platform.drinkcapacity.dao.IVpsVcodeDrinkCapacityPkRecordDao;
import com.dbt.platform.drinkcapacity.wechatmsg.DrinkCapacityPkMsg;
import com.dbt.platform.drinkcapacity.wechatmsg.DrinkCapacityPkResultMsg;
@Service
public class VpsVcodeDrinkCapacityPkRecordService extends BaseService<VpsVcodeDrinkCapacityPkRecord> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private IVpsVcodeDrinkCapacityPkRecordDao pkRecordDao;
    @Autowired
    private VpsConsumerAccountInfoService accountInfoService;
    @Autowired
    private VpsVcodeDrinkCapacityPkStatisticsService pkStatisticsService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    
    /**
     * 
     * @param infoKey
     * @param beginTime
     * @return
     */
    public VpsVcodeDrinkCapacityPkRecord findByIdWithAppletOpenid(String infoKey, String beginTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("infoKey", infoKey);
        map.put("splitTableSuffix", PackRecordRouterUtil.getTabSuffixByDate(12, beginTime));
        return pkRecordDao.findByIdWithAppletOpenid(map);
    }
    
    /**
     * 获取指定区间比赛记录
     * 
     * @param pkStatus         记录状态
     * @param beginMinTime     比赛开始时间的开始区间    
     * @param beginMaxTime     比赛开始时间的结束区间
     * @return
     */
    public List<VpsVcodeDrinkCapacityPkRecord> queryByPkTime(String pkStatus, String beginMinTime, String beginMaxTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableIndexs", PackRecordRouterUtil.getTabSuffixByDate(12, beginMaxTime, beginMaxTime));
        map.put("pkStatus", pkStatus);
        map.put("beginMinTime", beginMinTime);
        map.put("beginMaxTime", beginMaxTime);
        return pkRecordDao.queryByPkTime(map);
    }
    
    /**
     * 更新比赛结果
     */
    public void updatePkStatus(VpsVcodeDrinkCapacityPkRecord pkRecord) {
        pkRecord.setSplitTableSuffix(PackRecordRouterUtil.getTabSuffixByDate(12, pkRecord.getBeginTime()));
        pkRecordDao.updatePkStatus(pkRecord);
        
        // 删除缓存
        try {
            CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey
                    .drinkCapacityPk.DRINKCAPACITY_PK_RECORD + "_" + pkRecord.getUserKeyA());
            CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey
                    .drinkCapacityPk.DRINKCAPACITY_PK_RECORD + "_" + pkRecord.getUserKeyB());
        } catch (Exception e) {
            log.error("更新比赛结果时刷新比赛记录缓存失败", e);
        }
    }
    
    /**
     * 判定比赛结果
     * @param pkCacheValue 
     */
    public void executePkStatus(String pkInfoKey, String pkBeginTime) {
        VpsVcodeDrinkCapacityPkRecord pkRecord = findByIdWithAppletOpenid(pkInfoKey, pkBeginTime);
        if (pkRecord == null || !Constant.PkStatus.status_0.equals(pkRecord.getPkStatus())) {
            log.warn("比赛infoKey:" + pkRecord.getInfoKey() + "状态异常:" + pkRecord.getPkStatus());
            return;
        }
        
        // 获取比赛双方的冻结金额
        VpsConsumerAccountInfo accountInfoA = accountInfoService.findByUserKey(pkRecord.getUserKeyA(), true);
        VpsConsumerAccountInfo accountInfoB = accountInfoService.findByUserKey(pkRecord.getUserKeyB(), true);
        pkRecord.setWinMoney("0.00");
        if (accountInfoA.getFreezeMoney() == 0 || accountInfoB.getFreezeMoney() == 0) {
            pkRecord.setPkStatus(Constant.PkStatus.status_3);
            if (accountInfoA.getFreezeMoney() == 0 && accountInfoB.getFreezeMoney() == 0) {
                pkRecord.setUnpkReason("比赛期间双方的扫码账户余额为0元");
            } else if (accountInfoA.getFreezeMoney() == 0) {
                pkRecord.setUnpkReason("比赛期间A的扫码账户余额为0元");
            } else if (accountInfoB.getFreezeMoney() == 0) {
                pkRecord.setUnpkReason("比赛期间B的扫码账户余额为0元");
            }
        } else if (pkRecord.getScanNumA() == pkRecord.getScanNumB()) {
            pkRecord.setPkStatus(Constant.PkStatus.status_2);
        } else {
            pkRecord.setPkStatus(Constant.PkStatus.status_1);
            pkRecord.setWinMoney(String.format("%.2f", (pkRecord.getScanNumA() > pkRecord
                    .getScanNumB() ? accountInfoB.getFreezeMoney() : accountInfoA.getFreezeMoney()) * 0.3, 2));
        }
        
        // 补充 上比赛查看人数
        pkRecord.setViewPersonNum((int)RedisApiUtil.getInstance().getSetNum(
                RedisApiUtil.CacheKey.drinkCapacityPk.DRINK_CAPACITY_PK_VIEW+ pkRecord.getInfoKey()));
        
        // 更新比赛记录
        this.updatePkStatus(pkRecord);
        
        // 更新比赛情况记录表
        pkStatisticsService.updatePkStatistics(pkRecord);
        
        // 更新双方冻结金额
        if (Constant.PkStatus.status_1.equals(pkRecord.getPkStatus())) {
            double winMoney = Double.valueOf(pkRecord.getWinMoney());
            if (pkRecord.getScanNumA() > pkRecord.getScanNumB()) {
                accountInfoService.updateFreezeMoney(accountInfoA.getAccountKey(), winMoney);
                accountInfoService.updateFreezeMoney(accountInfoB.getAccountKey(), -winMoney);
            } else {
                accountInfoService.updateFreezeMoney(accountInfoA.getAccountKey(), -winMoney);
                accountInfoService.updateFreezeMoney(accountInfoB.getAccountKey(), +winMoney);
            }
        } else {
            accountInfoService.updateFreezeMoney(accountInfoA.getAccountKey(), 0.00D);
            accountInfoService.updateFreezeMoney(accountInfoB.getAccountKey(), 0.00D);
        }
        
        // 推送PK结果模板消息
        taskExecutor.execute(new DrinkCapacityPkResultMsg(pkRecord.getUserKeyA(), pkRecord, accountInfoA.getFreezeMoney(), accountInfoB.getFreezeMoney()));
        taskExecutor.execute(new DrinkCapacityPkResultMsg(pkRecord.getUserKeyB(), pkRecord, accountInfoB.getFreezeMoney(), accountInfoA.getFreezeMoney()));
        
        // 删除查看及点赞用户集合缓存
        RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.drinkCapacityPk.DRINK_CAPACITY_PK_VIEW + pkRecord.getInfoKey());
        RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.drinkCapacityPk.DRINK_CAPACITY_PK_THUMBUP + pkRecord.getInfoKey());

        // 比赛判定结果日志
        StringBuffer buffer = new StringBuffer();
        buffer.append("比赛infoKey：").append(pkRecord.getInfoKey());
        buffer.append(" userKeyA：").append(pkRecord.getUserKeyA());
        buffer.append(" userKeyB：").append(pkRecord.getUserKeyB());
        buffer.append(" 比赛结果：");
        if (Constant.PkStatus.status_2.equals(pkRecord.getPkStatus())) {
            buffer.append("平");
        } else if (Constant.PkStatus.status_3.equals(pkRecord.getPkStatus())) {
            buffer.append("流局");
        } else if (Constant.PkStatus.status_1.equals(pkRecord.getPkStatus())) {
            buffer.append(pkRecord.getScanNumA() > pkRecord.getScanNumB() ? "A" : "B").append("胜");
        }
        buffer.append(" winMoney：" + pkRecord.getWinMoney());
        log.warn(buffer.toString());
    }
    
    /**
     * 比赛开始20小时后的提醒
     * 
     * @param infoKey
     * @param pkBeginTime
     */
    public void send20HourTips(String pkInfoKey, String pkBeginTime) {
        VpsVcodeDrinkCapacityPkRecord pkRecord = findByIdWithAppletOpenid(pkInfoKey, pkBeginTime);
        if (pkRecord == null) return;
        if (!Constant.PkStatus.status_0.equals(pkRecord.getPkStatus())) {
            log.warn("20小时提醒——比赛infoKey:" + pkRecord.getInfoKey() + "状态异常:" + pkRecord.getPkStatus());
            return;
        }
        
        // 推送PK形如20小时提醒模板消息
        taskExecutor.execute(new DrinkCapacityPkMsg(pkRecord.getUserKeyA(), pkRecord));
        taskExecutor.execute(new DrinkCapacityPkMsg(pkRecord.getUserKeyB(), pkRecord));
        
    }
}
