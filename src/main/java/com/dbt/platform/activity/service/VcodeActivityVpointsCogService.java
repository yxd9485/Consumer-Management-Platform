package com.dbt.platform.activity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;
import com.dbt.platform.activity.dao.IVcodeActivityVpointsCogDao;
import com.dbt.platform.ticket.bean.VpsSysTicketInfo;
import com.dbt.platform.ticket.service.VpsSysTicketInfoService;
import com.dbt.platform.wctaccesstoken.bean.WechatRemindTemplateMsg;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.service.VpointsCouponCogService;

/**
 * 积分配置Service
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2016年11月7日 </br>
 */
@Service
public class VcodeActivityVpointsCogService{

    private final static Log log = LogFactory.getLog(VcodeActivityVpointsCogService.class);
	
	@Autowired
	private IVcodeActivityVpointsCogDao vpointsCogDao;
	@Autowired
	private VpsSysTicketInfoService sysTicketInfoService;
	@Autowired
	private VcodeActivityRebateRuleCogService rebateRuleCogService;
	@Autowired
	private VcodeActivityService activityService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
    @Autowired
    private VpointsCouponCogService couponCogService;
    
    /**
     * 批量插入积分配置
     * @param map
     */
    public void batchWrite(Map<String, Object> map) {
        vpointsCogDao.batchWrite(map);
    }
	
	/**
	 * 批量更新活动配置剩余数量
	 * 
	 * @param cogkeyList
	 */
    public void updateBathWaitActivityVpointsCog(String cacheKeyStr) {
        Map<String, String> cogKeyMap = RedisApiUtil.getInstance().getHAll(cacheKeyStr);
        if (null != cogKeyMap && !cogKeyMap.isEmpty()) {
            List<VcodeActivityVpointsCog> cogLst = new ArrayList<VcodeActivityVpointsCog>();
            Set<String> keySet = cogKeyMap.keySet();
            int i = 0;
            VcodeActivityVpointsCog cogItem = null;
            for (String key : keySet) {
                cogItem = new VcodeActivityVpointsCog();
                cogItem.setVpointsCogKey(key);
                cogItem.setRangeVal(Long.valueOf(cogKeyMap.get(key)));
                cogLst.add(cogItem);
                i++;
                if (i == 300) {
                    vpointsCogDao.updateBathWaitActivityVpointsCog(cogLst);
                    i = 0;
                    cogLst = new ArrayList<VcodeActivityVpointsCog>();
                }
            }
            if (cogLst.size() > 0) {
                vpointsCogDao.updateBathWaitActivityVpointsCog(cogLst);
            }
            log.info(DbContextHolder.getDBType() + "：批量更新活动配置剩余数量成功;共更新条数 " + cogKeyMap.size() + " 条!");
        } else {
            log.info(DbContextHolder.getDBType() + "：无批量更新活动配置记录!");
        }
	}
    
    /**
     * 奖项配置项占比预警
     * @param cacheKeyStr
     */
    public void initPrizePercentWarn() throws Exception {
            
        // AI助手appid 
        String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
                    .FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.AI_APPID);
        String[] openidAry = StringUtils.defaultIfBlank(DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.AI_REMIND_OPENID, DatadicKey.aiRemindOpenid.AI_REMIND_PRIZE_PERCENT), "").split(",");
        if (StringUtils.isBlank(appid) || openidAry.length == 0) return;

        // 获取所有要预警的奖项配置项
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nowTime", DateUtil.getDate());
        map.put("weekSeveral", DateUtil.getDayOfWeek());
        List<VcodeActivityVpointsCog> vpointsCogWarnLst = vpointsCogDao.queryWarnVpointsCogByRebateRuleKey(map);
        if (CollectionUtils.isEmpty(vpointsCogWarnLst)) {
            log.error(DbContextHolder.getDBType() + "暂无奖项占比预警配置");
            return;
        }
        
        // 获取规则奖项部个数
        Set<String> rebateRuleKeySet = new HashSet<>();
        for (VcodeActivityVpointsCog item : vpointsCogWarnLst) {
            rebateRuleKeySet.add(item.getRebateRuleKey());
        }
        map.put("rebateRuleKeyAry", rebateRuleKeySet);
        List<VcodeActivityVpointsCog> vpointsCogCountLst = vpointsCogDao.countWarnVpointsCogByRebateRuleKey(map);
        
        // 项目名称
        String projectName = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_NAME);
        
        // 奖项类型
        Map<String, String> prizeTypeMap = queryAllPrizeType(true, true, true, true, true, true, null);
        
        // 当天已预警过的奖项配置项主键集合
        Date dayLastTime = DateUtil.getDayOfYear(0, true);
        String redisCacheKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                .ACTIVITY_FORVCODE_GET_POINTS_WARN + ":vpointsKey" + DateUtil.getDateTime(dayLastTime, "yyyyMMdd");
        RedisApiUtil.getInstance().expireAt(redisCacheKey, dayLastTime.getTime()/1000);
        
        StringBuffer buffer = null;
        double realPercent, earnPercent;
        for (VcodeActivityVpointsCog itemWarn : vpointsCogWarnLst) {
            if (RedisApiUtil.getInstance().containsInSet(redisCacheKey, itemWarn.getVpointsCogKey())) continue;
            for (VcodeActivityVpointsCog itemCount : vpointsCogCountLst) {
                if (itemWarn.getRebateRuleKey().equals(itemCount.getRebateRuleKey()) && itemWarn.getScanType().equals(itemCount.getScanType())) {
                    if (itemCount.getCogAmounts() - itemCount.getRestAmounts() < 1000) break;
                    realPercent = itemWarn.getRangeVal() * 100D / itemCount.getRangeVal();
                    earnPercent = (itemWarn.getCogAmounts() - itemWarn.getRestAmounts()) * 100D / (itemCount.getCogAmounts() - itemCount.getRestAmounts());
                    if (itemWarn.getPrizePercentWarn() <= Math.abs(realPercent - earnPercent)) {
                        buffer = new StringBuffer();
                        buffer.append(activityService.findActivityNameByKey(itemWarn.getVcodeActivityKey()));
                        buffer.append("\r\n").append("规则:").append(rebateRuleCogService.getRebateRuleCogName(new VcodeActivityRebateRuleCog(itemCount.getAreaCode(), itemCount.getRuleType(), itemCount.getBeginDate(), itemCount.getEndDate(), itemCount.getBeginTime(), itemCount.getEndTime())));
                        buffer.append("\r\n").append("0".equals(itemWarn.getScanType()) ? "首扫 " : "普扫 ").append(prizeTypeMap.get(itemWarn.getPrizeType()));
                        if (Constant.PrizeType.status_0.equals(itemWarn.getPrizeType())) {
                            buffer.append(Constant.PrizeRandomType.type_0.equals(itemWarn.getRandomType()) ? itemWarn.getMinMoney() + "~" + itemWarn.getMaxMoney() : itemWarn.getMinMoney());
                        } else if (Constant.PrizeType.status_1.equals(itemWarn.getPrizeType())) {
                            buffer.append(Constant.PrizeRandomType.type_0.equals(itemWarn.getRandomType()) ? itemWarn.getMinVpoints() + "~" + itemWarn.getMaxVpoints() : itemWarn.getMinVpoints());
                        } else if (Constant.PrizeType.status_2.equals(itemWarn.getPrizeType())) {
                            buffer.append(" 现金").append(Constant.PrizeRandomType.type_0.equals(itemWarn.getRandomType()) ? itemWarn.getMinMoney() + "~" + itemWarn.getMaxMoney() : itemWarn.getMinMoney());
                            buffer.append(" 积分").append(Constant.PrizeRandomType.type_0.equals(itemWarn.getRandomType()) ? itemWarn.getMinVpoints() + "~" + itemWarn.getMaxVpoints() : itemWarn.getMinVpoints());
                        }
                        buffer.append("\r\n").append("配置占比").append(String.format("%.2f", itemWarn.getPrizePercent())).append("%");
                        buffer.append(" 实际占比").append(String.format("%.2f", realPercent)).append("%");
                        buffer.append(" 中出占比").append(String.format("%.2f", earnPercent)).append("%");
                        buffer.append(" 预警值").append(String.format("%.2f", itemWarn.getPrizePercentWarn())).append("%") ;
                        RedisApiUtil.getInstance().addSet(redisCacheKey, itemWarn.getVpointsCogKey());
                        log.warn("projectName:" + projectName + " 奖项占比预警:" + buffer.toString().replace("\r\n", ""));
                        for (String openid : openidAry) {
                            taskExecutor.execute(new WechatRemindTemplateMsg(appid, openid).initRemindMsg("奖项占比预警", projectName, buffer.toString()));
                        }
                    }
                    break;
                }
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println(String.format("%.4f", (69519  / ((69519 + 990000 + 10000) * 1D)) * 100D));
    }
    
    public void updateBathWaitActivityVpointsCog(List<VcodeActivityVpointsCog> cogLst) {
        vpointsCogDao.updateBathWaitActivityVpointsCog(cogLst);
    }

    /**
     * 根据活动规则主键停用中奖配置项
     * 
     * @param map   Key:rebateRuleKey、updateTime、updateUser
     */
    public void removeByrebateRuleKey(Map<String, Object> map) {
        vpointsCogDao.removeByrebateRuleKey(map);
    }

    /**
     * 根据活动规则主键查询相应配置项
     * 
     * @param rebateRuleKey    活动规则主键</br> 
     * @return void </br>
     */
    public List<VcodeActivityVpointsCog> queryVpointsCogByrebateRuleKey(String rebateRuleKey) {
        return vpointsCogDao.queryVpointsCogByrebateRuleKey(rebateRuleKey);
    }
    
    /**
     * 校验大奖类型
     * 
     * @param prizeType 随机奖品类型
     * @return true：是大奖
     */
    public boolean checkPrizeType(String prizeType) throws Exception {
        if (StringUtils.isBlank(prizeType)) return false;
       
        // 默认不是大奖
        boolean resultFlag = false;
        Map<String, String> prizeMap = vcodeActivityBigPrizeService.getPrizeTypeMap();
        if(prizeMap.containsKey(prizeType)){
            resultFlag = true;
        }
        
        return resultFlag;
    }
    
    /**
     * 将定投奖品类型转换配置项中的奖品类型
     * 
     * @param grandprizetype 定投奖品类型
     * 
     * @return  返回对应的配置项奖品类型, 若未匹配成功则返回grandPrizeType自身
     */
    public String transformPrizeType(String grandPrizeType) {
        if (StringUtils.isBlank(grandPrizeType)) return grandPrizeType;
        
        String prizeType = null;
        
        // 一等奖-游轮 
        if (Constant.GrandPrizeType.GRANDPRIZETYPE_0.equals(grandPrizeType)) {
            prizeType = Constant.PrizeType.status_5;
            
        // 二等奖 
        } else if (Constant.GrandPrizeType.GRANDPRIZETYPE_1.equals(grandPrizeType)) {
            prizeType = Constant.PrizeType.status_6;
        
        // 三等奖
        } else if (Constant.GrandPrizeType.GRANDPRIZETYPE_2.equals(grandPrizeType)) {
            prizeType = Constant.PrizeType.status_7;
            
        // 若未匹配成功则返回grandPrizeType自身（字母）
        } else {
            prizeType = grandPrizeType.toUpperCase();
        }
        return prizeType;
    }

    /**
     * 拷贝配置项
     * @param newRebateRuleKey
     * @param copyRebateRuleKey
     */
	public void copyActivityVpointsItem(String vcodeActivityKey, String newRebateRuleKey, String copyRebateRuleKey) {
		Map<String, Object> map = new HashMap<>();
		map.put("vcodeActivityKey", vcodeActivityKey);
		map.put("newRebateRuleKey", newRebateRuleKey);
		map.put("copyRebateRuleKey", copyRebateRuleKey);
		vpointsCogDao.copyActivityVpointsItem(map);
	}

    
    /**
     * 获取所有奖项类型
     * @param basicPrizeFlag 普通奖项 
     * @param bigPrizeFlag 	   大奖奖项 
     * @param ticketPrizeFlag 优惠券奖项 
     * @param lxPrizeFlag 乐享券奖项 
     */
    public Map<String, String> queryAllPrizeType(boolean basicPrizeFlag, boolean bigPrizeFlag, boolean ticketPrizeFlag, boolean lxPrizeFlag, boolean shopingCouponFlag, boolean allshopingCouponFlag, List<String> couponNoLst) throws Exception {
        
        // 配置文件的奖项
        LinkedHashMap<String, String> prizeMap = new LinkedHashMap<>();
        List<SysDataDic> prizes = new ArrayList<SysDataDic>();
        
        // 普通奖
        if(basicPrizeFlag){
        	prizes = DatadicUtil.getListByCategoryCode(DatadicKey.dataDicCategory.FILTER_PRIZENAME);
            for(SysDataDic s : prizes){
                if (StringUtils.isNotBlank(s.getDataAlias())){
                    prizeMap.put(s.getDataValue(),s.getDataAlias());
                }
            }
        }
        
        // 实物奖
        if(bigPrizeFlag){
            LinkedHashMap<String, String>  bigPrizeMap = (LinkedHashMap<String, String>) vcodeActivityBigPrizeService.getPrizeTypeMap();
            prizeMap.putAll(bigPrizeMap);
        }
        // 乐享奖
        if(lxPrizeFlag){
        	List<SysDataDic> lxPrizeList = 
        			DatadicUtil.getListByCategoryCode(DatadicKey.dataDicCategory.FILTER_LX);
        	if(CollectionUtils.isNotEmpty(lxPrizeList)){
        		for (SysDataDic item : lxPrizeList) {
					if(item.getDataId().length() == 14 && item.getDataId().startsWith("prize_typeLX")){
						prizeMap.put(item.getDataValue(),item.getDataAlias());
					}
				}
        	}
        }
        
        // 优惠券的奖项
        if(ticketPrizeFlag){
        	List<VpsSysTicketInfo> sysTicketInfoList = sysTicketInfoService.localList();
            if(!CollectionUtils.isEmpty(sysTicketInfoList)){
                String categoryType = "";
                StringBuilder builder = new StringBuilder();
                for (VpsSysTicketInfo item : sysTicketInfoList) {
                    
                    // 优惠券大分类
                    if (StringUtils.isNotBlank(item.getCategoryType()) && !categoryType.equals(item.getCategoryType())) {
                        categoryType = item.getCategoryType();
                        prizeMap.put(item.getCategoryType(), item.getCategoryName());
                    }
                    
                    // 优惠券明细
                    builder.append(item.getCategoryType());
                    builder.append(item.getCategoryName());
                    builder.append("-");
                    builder.append(item.getTicketName());
                    builder.append("（");
                    if (Constant.TICKET_TYPE.ticketType_0.equals(item.getTicketType())) {
                        builder.append("链接");
                    } else if (Constant.TICKET_TYPE.ticketType_1.equals(item.getTicketType())) {
                        builder.append("券码");
                    } else if (Constant.TICKET_TYPE.ticketType_2.equals(item.getTicketType())) {
                        builder.append("图片");
                    } else if (Constant.TICKET_TYPE.ticketType_3.equals(item.getTicketType())) {
                        builder.append("动态券码");
                    } else if (Constant.TICKET_TYPE.ticketType_4.equals(item.getTicketType())) {
                        builder.append("活动编码");
                    }
                    builder.append("）");
                    prizeMap.put(item.getTicketNo(), builder.toString());
                    builder.setLength(0);
                }
            }
        }
        
        // 商城优惠券
        if(shopingCouponFlag) {
        	List<VpointsCouponCog> couponCogList = couponCogService.queryValidCouponList(allshopingCouponFlag, couponNoLst);
        	if(CollectionUtils.isNotEmpty(couponCogList)) {
        		for (VpointsCouponCog item : couponCogList) {
        			prizeMap.put(item.getCouponNo(),"商城优惠券：" + item.getCouponName());
				}
        	}
        }
        
        return prizeMap;
    }
    
    /**
     * 获取尊享卡类型
     * @return
     */
    public Map<String, String> queryBigPrizeForZunXiang() throws Exception {
        Map<String, String> prizeMap = vcodeActivityBigPrizeService.getPrizeTypeMap();
        Map<String, String> prizeMap1 = new HashMap<>();
        for (String k : prizeMap.keySet()) {
            //移除大奖类型 Z
            if (k.startsWith("Z")){
                prizeMap1.put(k, prizeMap.get(k));
            }
        }
        return prizeMap1;
    }

    /**
     * 获取尊享卡类型(map.value拼接prizeNo参数)
     * @return
     */
    public Map<String, String> queryBigPrizeForZunXiangCard() throws Exception {
        Map<String, String> prizeMap = vcodeActivityBigPrizeService.getPrizeTypeMap();
        Map<String, String> prizeMap1 = new HashMap<>();
        for (String k : prizeMap.keySet()) {
            //移除大奖类型 Z
            if (k.startsWith("Z")){
                prizeMap1.put(k, k+"-"+prizeMap.get(k));
            }
        }
        return prizeMap1;
    }
    
    /**
     * 获取集卡类型
     * @return
     */
    public Map<String, String> queryCardType() {
        TreeMap<String, String> map = new TreeMap<String, String>();
        List<SysDataDic> cardList = DatadicUtil
                .getListByCategoryCode(DatadicKey.dataDicCategory.FILTER_COLLECT_CARDS);
        for(SysDataDic s : cardList){
            if (StringUtils.isNotBlank(s.getDataAlias())){
                map.put(s.getDataValue(),s.getDataAlias());
            }
        }
        return map;
    }
    
    /**
     * 获取津贴卡类型
     * @return
     */
    public Map<String, String> queryAllowanceType() {
        TreeMap<String, String> map = new TreeMap<String, String>();
        List<SysDataDic> allowanceList = DatadicUtil
                .getListByCategoryCode(DatadicKey.dataDicCategory.FILTER_PRIVILEGE);
        for(SysDataDic s : allowanceList){
            if (StringUtils.isNotBlank(s.getDataAlias())){
                map.put(s.getDataValue(),s.getDataAlias());
            }
        }
        return map;
    }

    public void deleteByrebateRuleKey(String commmonKey, String specialKey) {
        Map<String,String> param = new HashMap<>();
        param.put("commmonKey", commmonKey);
        param.put("specialKey", specialKey);
        vpointsCogDao.deleteByrebateRuleKey(param);
    }
    
    /**
     * 获取规则下商城优惠券NO
     * @param rebateRuleKey
     * @return
     */
    public List<String> queryCouponNoByRebateRuleKey(String rebateRuleKey) {
        return vpointsCogDao.queryCouponNoByRebateRuleKey(rebateRuleKey);
    }
}

