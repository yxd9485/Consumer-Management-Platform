package com.dbt.platform.redenveloperain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.bean.VcodeActivityMoneyImport;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleTemplet;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.fission.bean.VpsVcodeActivatePrizeRecordEntity;
import com.dbt.platform.redenveloperain.bean.RedEnvelopeRainActivityEntity;
import com.dbt.platform.redenveloperain.bean.RedEnvelopeRainActivityRecord;
import com.dbt.platform.redenveloperain.dao.IVpsRedEnvelopeRainActivityMapper;
import com.dbt.platform.redenveloperain.dao.IVpsRedEnvelopeRainActivityRecordMapper;
import com.dbt.platform.redenveloperain.dto.RedEnvelopeRainActivityQuery;
import com.dbt.platform.redenveloperain.service.RedEnvelopeRainActivityService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.vjifen.module.wechat.utils.DateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author shuDa
 * @date 2022/1/12
 **/
@Service
public class RedEnvelopeRainActivityServiceImpl  extends ServiceImpl<IVpsRedEnvelopeRainActivityMapper, RedEnvelopeRainActivityEntity> implements RedEnvelopeRainActivityService {


    @Autowired
    private VcodeActivityRebateRuleCogService rebateRuleCogService;
    @Autowired
    private IVpsRedEnvelopeRainActivityRecordMapper redEnvelopeRainActivityRecordMapper;
    @Autowired
    private SysAreaService sysAreaService;
    @Override
    public IPage<RedEnvelopeRainActivityQuery> queryPage(IPage objectPage,RedEnvelopeRainActivityQuery param) throws InvocationTargetException, IllegalAccessException {

        IPage<RedEnvelopeRainActivityQuery> page = this.baseMapper.selectPageVO(objectPage,param);
        page.getRecords().forEach(query->{
            String areaCode = query.getAreaCode();
            String replace = StringUtils.replace(areaCode, "000000", "010000");
            List<SysAreaM> sysAreaMS = sysAreaService.queryByAreaCode(Arrays.asList(StringUtils.split(replace, ",")));
            query.setAreaCode(sysAreaMS.stream().map(SysAreaM::getAreaName).collect(Collectors.joining(",")));
        });

        return page;
    }

    @Override
    public Boolean create(RedEnvelopeRainActivityQuery query, SysUserBasis user) throws Exception {
        String timeRain = "";

//        List<RedEnvelopeRainActivityQuery> list  = this.baseMapper.validDateTime(query);
//        if (list.size() > 0) {
//            throw new BusinessException("活动时间冲突");
//        }
        for (int i = 0; i < query.getBeginTime().length; i++) {
            timeRain += query.getBeginTime()[i] + "@" + query.getEndTime()[i]+",";
        }
        RedEnvelopeRainActivityEntity entity = new RedEnvelopeRainActivityEntity();
        entity.setActivityName(query.getActivityName());
        entity.setAreaCodes(query.getAreaCode());
        entity.setTimeRange(timeRain);
        entity.setHotAreaKey(query.getHotAreaKey());
        entity.setVcodeActivityKey(query.getVcodeActivityKey());
        entity.setRestrictBottle(query.getRestrictBottle());
        entity.setRestrictMoney(query.getRestrictMoney());
        entity.setRestrictTimeType(query.getRestrictTimeType());
        entity.setRestrictUserCount(query.getRestrictUserCount());
        entity.setStatusFlag(query.getStatusFlag());
        entity.setStartDate(DateUtil.getDateFromDay(DateUtil.getStartDateTime(query.getStartDate()), DateUtil.DEFAULT_DATE_FORMAT));
        entity.setEndDate(DateUtil.getDateFromDay(DateUtil.getEndDateTime(query.getEndDate()), DateUtil.DEFAULT_DATE_FORMAT));
        entity.setCreateUser(user.getUserName());
        entity.setUpdateUser(user.getUserName());
        entity.setRuleContentUrl(query.getRuleContentUrl());
        int insert = this.baseMapper.insert(entity);
         if(insert > 0){
             createRebateRule(query, entity, user);
        }
        return insert > 0;
    }

    @Override
    public RedEnvelopeRainActivityQuery findById(String activityKey) {
        RedEnvelopeRainActivityEntity entity = this.baseMapper.selectById(activityKey);
        return new RedEnvelopeRainActivityQuery(entity);
    }

    @Override
    public Boolean updateById(RedEnvelopeRainActivityQuery query, SysUserBasis user) throws Exception {
        String timeRain = "";
//        List<RedEnvelopeRainActivityQuery> list  = this.baseMapper.validDateTime(query);
//        if (list.size() > 0) {
//            throw new BusinessException("活动时间冲突");
//        }
        for (int i = 0; i < query.getBeginTime().length; i++) {
            timeRain += query.getBeginTime()[i] + "@" + query.getEndTime()[i]+",";
        }
        RedEnvelopeRainActivityEntity entity = new RedEnvelopeRainActivityEntity();
        entity.setInfoKey(query.getInfoKey());
        entity.setActivityName(query.getActivityName());
        entity.setAreaCodes(query.getAreaCode());
        entity.setTimeRange(timeRain);
        entity.setHotAreaKey(query.getHotAreaKey());
        entity.setVcodeActivityKey(query.getVcodeActivityKey());
        entity.setRestrictBottle(query.getRestrictBottle());
        entity.setRestrictMoney(query.getRestrictMoney());
        entity.setRestrictTimeType(query.getRestrictTimeType());
        entity.setRestrictUserCount(query.getRestrictUserCount());
        entity.setStatusFlag(query.getStatusFlag());
        entity.setStartDate(DateUtil.getDateFromDay(DateUtil.getStartDateTime(query.getStartDate()), DateUtil.DEFAULT_DATE_FORMAT));
        entity.setEndDate(DateUtil.getDateFromDay(DateUtil.getEndDateTime(query.getEndDate()), DateUtil.DEFAULT_DATE_FORMAT));
        entity.setCreateUser(user.getUserName());
        entity.setUpdateUser(user.getUserName());
        entity.setRuleContentUrl(query.getRuleContentUrl());
        int insert = this.baseMapper.updateById(entity);
        if(insert > 0){
            createRebateRule(query, entity, user);
        }
        return insert>0;
    }

    private void createRebateRule(RedEnvelopeRainActivityQuery query,  RedEnvelopeRainActivityEntity entity, SysUserBasis user) throws Exception {
        int length = query.getMaxMoney().length;
        String[] areaCodes = query.getAreaCode().split(",");
        String[] cogAmounts = new String[length];
        String[] scanType = new String[length];
        String[] fixationMoney= new String[length];
        String[] minVpoints= new String[length];
        String[] maxVpoints= new String[length];
        String[] fixationVpoints= new String[length];
        double unityMoneyD = 0;
        for (int i = 0; i < query.getMinMoney().length; i++) {
            cogAmounts[i] = String.valueOf(Integer.MAX_VALUE);
            scanType[i] = "1";
            minVpoints[i] = "0";
            maxVpoints[i] = "0";
            fixationVpoints[i] = "0";
            unityMoneyD += Double.valueOf(query.getUnitMoney()[i]);
        }
        //单瓶计算
        unityMoneyD = new BigDecimal(unityMoneyD).divide(BigDecimal.valueOf(query.getUnitMoney().length), 2, RoundingMode.HALF_UP).doubleValue();
        String filterDateAry = query.getStartDate() + "@" + query.getEndDate();
        String filterAreaCode = entity.getAreaCodes();
        VcodeActivityRebateRuleCog rebateRuleCog = new VcodeActivityRebateRuleCog();
        rebateRuleCog.setRebateRuleKey(query.getRebateRuleCogList() !=null?query.getRebateRuleCogList().get(0).getRebateRuleKey():null);
        rebateRuleCog.setRebateRuleName(entity.getActivityName() + "规则");
        rebateRuleCog.setVcodeActivityKey(entity.getInfoKey());
        rebateRuleCog.setMoneyDanping(unityMoneyD);
        rebateRuleCog.setRuleType(Constant.rebateRuleType.RULE_TYPE_2);
        rebateRuleCog.setPrizeCogType(Constant.PrizeCogType.type_1);
        rebateRuleCog.setHotAreaKey(entity.getHotAreaKey());
///       rebateRuleCog.setRestrictMoney(entity.getRestrictMoney());
        rebateRuleCog.setRestrictMoney(0);
        rebateRuleCog.setRestrictBottle(entity.getRestrictBottle());
        rebateRuleCog.setRestrictCount(entity.getRestrictUserCount());
        rebateRuleCog.setRestrictTimeType(String.valueOf(entity.getRestrictTimeType()));
        rebateRuleCog.setBeginDate(query.getStartDate());
        rebateRuleCog.setEndDate(query.getEndDate());
        rebateRuleCog.setFirstScanPercent("0");
        rebateRuleCog.setFirstScanDanpingLimit("0");
        rebateRuleCog.setRuleNewUserLadder("0");
        rebateRuleCog.setRedPacketRain("0");
        rebateRuleCog.setRuleTotalPrize(String.valueOf(Integer.MAX_VALUE));
        rebateRuleCog.setActivityVersion("红包雨");
        rebateRuleCog.setActivityType(Constant.activityType.activity_type12);
        //红包雨规则新增默认给全部，接口区域限制要额外判断，不能靠getMoney
        //前端页面 多区域会生成很多规则 防止这种情况出现
        rebateRuleCog.setAreaCode("000000");
        rebateRuleCog.setRuleTotalVpoints("0");
        rebateRuleCog.setRuleTotalMoney(String.valueOf(entity.getRestrictMoney()));
        rebateRuleCog.setRuleUnitMoney(String.valueOf(unityMoneyD));
        VcodeActivityRebateRuleTemplet rebateRuleTemplet = new VcodeActivityRebateRuleTemplet();
        rebateRuleTemplet.setMaxMoney(query.getMaxMoney());
        rebateRuleTemplet.setMinMoney(query.getMinMoney());
        rebateRuleTemplet.setPrizePercent(query.getPrizePercent());
        rebateRuleTemplet.setRandomType(query.getRandomType());
        rebateRuleTemplet.setCogAmounts(cogAmounts);
        rebateRuleTemplet.setScanType(scanType);
        rebateRuleTemplet.setBigPrizeType(new String[length]);
        rebateRuleTemplet.setScanNum(new String[length]);
        rebateRuleTemplet.setCardNo(new String[length]);
        rebateRuleTemplet.setFixationMoney(query.getFixationMoney());
        rebateRuleTemplet.setMinVpoints(minVpoints);
        rebateRuleTemplet.setMaxVpoints(maxVpoints);
        rebateRuleTemplet.setFixationVpoints(fixationVpoints);
        rebateRuleTemplet.setPrizePercentWarn(new String[length]);
        rebateRuleTemplet.setPrizePayMoney(new String[length]);
        rebateRuleTemplet.setAllowanceMoney(new String[length]);
        rebateRuleTemplet.setAllowanceType(new String[length]);
        rebateRuleTemplet.setPrizeDiscount(new String[length]);


        // 初始化奖项配置项
        List<VcodeActivityMoneyImport> excelList = rebateRuleCogService
                .initPrizeItem(rebateRuleCog, null, rebateRuleTemplet, Constant.activityType.activity_type12);

        // 入库前先拿出老的奖项主键集合
        Set<String> oldKeys = null;
        String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + rebateRuleCog.getRebateRuleKey();
        if (excelList != null && !excelList.isEmpty()) {
            oldKeys = RedisApiUtil.getInstance().hkeys(redisKey);
        }

        // 写入库
        rebateRuleCogService.writeBatchVpointsCog(excelList, rebateRuleCog,
                "000000", "", "",filterDateAry, "00:00:00@23:59:59", user.getUserKey(), "0", Constant.activityType.activity_type12);

        // 删除规则对应的配置项
        CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                .KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST + Constant.DBTSPLIT
                + rebateRuleCog.getVcodeActivityKey() + Constant.DBTSPLIT + rebateRuleCog.getRebateRuleKey());


        // 如果操作成功删除旧的奖项主键集合
        if (oldKeys != null && !oldKeys.isEmpty()) {
            RedisApiUtil.getInstance().delHSet(redisKey, oldKeys.toArray(new String[]{}));
        }
}


    @Override
    public void updateBackRedEnvelopeMoneyJobHandler() {
            this.updateBackRedEnvelopeMoney();
    }

    @Override
    public boolean checkDateTime(RedEnvelopeRainActivityQuery query) {
        List<RedEnvelopeRainActivityQuery> list  = this.baseMapper.validDateTime(query);
        return list.size() > 0;
    }

    public void updateBackRedEnvelopeMoney() {
        RedEnvelopeRainActivityRecord record = new RedEnvelopeRainActivityRecord();
        record.setBackFlag("0");
        int integer = redEnvelopeRainActivityRecordMapper.selectCount(new QueryWrapper<>(record));
        if(integer > 0 ){
            QueryWrapper<RedEnvelopeRainActivityRecord> recordQueryWrapper = new QueryWrapper<>(record);
            recordQueryWrapper.le("create_time", DateUtils.addMinutes(new Date(), -1));
            List<RedEnvelopeRainActivityRecord> recordList = redEnvelopeRainActivityRecordMapper.selectList(recordQueryWrapper);
            Set<String> redEnvelopeActivityKey = new HashSet<>();
            for (RedEnvelopeRainActivityRecord redEnvelopeRainActivityRecord : recordList) {
                redEnvelopeActivityKey.add(redEnvelopeRainActivityRecord.getActivityKey());
                RedEnvelopeRainActivityRecord updateRecord = new RedEnvelopeRainActivityRecord();
                updateRecord.setInfoKey(redEnvelopeRainActivityRecord.getInfoKey());
                updateRecord.setBackFlag("2");
                redEnvelopeRainActivityRecordMapper.updateById(updateRecord);
            }
            for (String activityKey : redEnvelopeActivityKey) {
                String rebateRuleMoneyStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_RED_ENVELOPE_RESTRICT_MONEY + activityKey;
                RedisApiUtil.getInstance().delHSet(rebateRuleMoneyStr, "Total");
                RedisApiUtil.getInstance().delHSet(rebateRuleMoneyStr, DateUtil.getDate());
            }

        }


    }

    public static void main(String[] args) {
        System.out.println( 2 & 3);
        System.out.println( 2 | 3);
        System.out.println( 2 |~-4);
    }



}
