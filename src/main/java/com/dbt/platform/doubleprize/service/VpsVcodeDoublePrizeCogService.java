package com.dbt.platform.doubleprize.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.cache.bean.CacheKey.cacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PackRecordRouterUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.zone.bean.Address;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;
import com.dbt.platform.activity.dao.IVcodeActivityVpointsCogDao;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.doubleprize.bean.VpsVcodeDoublePrizeCog;
import com.dbt.platform.doubleprize.dao.IVpsVcodeDoublePrizeCogDao;

/**
 * 一码双奖活动配置表Service
 */
@Service
public class VpsVcodeDoublePrizeCogService extends BaseService<VpsVcodeDoublePrizeCog> {

	@Autowired
	private IVpsVcodeDoublePrizeCogDao doublePrizeCogDao;
	@Autowired
    private IVcodeActivityVpointsCogDao vpointsCogDao;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;

	/**
	 * 获取活动
	 * 
	 * @param activityKey
	 * @return
	 */
	public VpsVcodeDoublePrizeCog findActivityByKey(String activityKey) {
		return doublePrizeCogDao.findById(activityKey);
	}

	/**
	 * 活动列表
	 * 
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 */
	public List<VpsVcodeDoublePrizeCog> queryVcodeActivityList(
	        VpsVcodeDoublePrizeCog queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		List<VpsVcodeDoublePrizeCog> activityList = doublePrizeCogDao.loadActivityList(queryMap);
		if(null != activityList && !activityList.isEmpty()){
            Object cacheObj = null;
            VpsVcodeDoublePrizeCog configItem = null;
            String currDate = DateUtil.getDate();
            String cacheStr = CacheKey.cacheKey.vcode.KEY_VCODE_DOUBLE_PRIZE_COG + Constant.DBTSPLIT;
            for (VpsVcodeDoublePrizeCog item : activityList) {
                // 筛选类型为所有用户时，获取redis中参与人数的数据
                if ("1".equals(item.getFilterType())) {
                    item.setFilterPersonNum(Integer.valueOf(RedisApiUtil.getInstance().get(CacheKey.cacheKey.vcode
                                .KEY_VCODE_DOUBLE_PRIZE_COG + ":" + item.getActivityKey() + ":filterPersonNum", "0")));
                }
                item.setJoinPersonNum(Integer.valueOf(RedisApiUtil.getInstance().get(CacheKey.cacheKey.vcode
                                .KEY_VCODE_DOUBLE_PRIZE_COG + ":" + item.getActivityKey() + ":joinPersonNum", "0")));
                item.setLotteryNum(RedisApiUtil.getInstance().get(CacheKey.cacheKey.vcode
                        .KEY_VCODE_DOUBLE_PRIZE_COG + ":" + item.getActivityKey() + ":lotteryNum", "0"));
                item.setCancelLotteryNum(RedisApiUtil.getInstance().get(CacheKey.cacheKey.vcode
                        .KEY_VCODE_DOUBLE_PRIZE_COG + ":" + item.getActivityKey() + ":cancelLotteryNum", "0"));
                cacheObj = CacheUtilNew.getCacheValue(cacheStr + item.getActivityKey() + "_cog");
                if(null == cacheObj) continue;
                configItem = JSON.parseObject(cacheObj.toString(), VpsVcodeDoublePrizeCog.class);
                if(null == configItem) continue;
                if(!item.getStartDate().equals(configItem.getStartDate())
                        || !item.getEndDate().equals(configItem.getEndDate())){
                    
                    if (currDate.compareTo(configItem.getEndDate()) > 0) {
                        item.setIsBegin("缓存异常(已结束)");
                        
                    } else if (currDate.compareTo(configItem.getStartDate()) < 0){
                        item.setIsBegin("缓存异常(未开始)");
                        
                    } else {
                        item.setIsBegin("缓存异常(进行中)");
                    }
                }
            }
        }
		return activityList;
	}

	/**
	 * 活动列表条数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int countVcodeActivityList(VpsVcodeDoublePrizeCog queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return doublePrizeCogDao.countActivityList(queryMap);
	}

	/**
	 * 创建活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws Exception 
	 */
	public void writeActivityCog(VpsVcodeDoublePrizeCog activityCog, 
	                                String currentUserKey, Model model) throws Exception {
	    
	    // 按扫码次数抽奖时，校验活动开始及结束日期的合法性
	    if ("0".equals(activityCog.getLotteryType())) {
	        checkPeriodType(activityCog.getPeriodType(), activityCog.getStartDate(), activityCog.getEndDate());
	    }
	    
	    // 初始化活动主键
	    activityCog.setActivityKey(UUID.randomUUID().toString());
	    // 生成编号
	    activityCog.setActivityNo(getBussionNo("doubleprize", "activity_no", Constant.OrderNoType.type_SJ));
	    
	    // 初始化促销及筛选SKU
	    if (activityCog.getPromotionSkuKeyAry() != null) {
	        Set<String> skuSet = new HashSet<>(Arrays.asList(activityCog.getPromotionSkuKeyAry()));
	        activityCog.setPromotionSkuKey(StringUtils.join(skuSet, ","));
	    }
	    if (activityCog.getFilterSkuKeyAry() != null) {
	        Set<String> skuSet = new HashSet<>(Arrays.asList(activityCog.getFilterSkuKeyAry()));
	        activityCog.setFilterSkuKey(StringUtils.join(skuSet, ","));
	    }
	    activityCog.fillFields(currentUserKey);
        
        // 筛选符合活动的历史扫码用户
        if ("0".equals(activityCog.getFilterType())) {
            activityCog.setFilterPersonNum(filterUser(activityCog));
        }
	    
	    // 持久化当前活动
	    doublePrizeCogDao.create(activityCog);
	    
	    logService.saveLog("doubleprize", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(activityCog), "创建一码双奖活动");
	}

	/**
	 * 更新活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws IOException 
	 */
	public void updateActivityCog(VpsVcodeDoublePrizeCog activityCog, String currentUserKey, Model model) throws Exception {
        
        // 按扫码次数抽奖时，校验活动开始及结束日期的合法性
        if ("0".equals(activityCog.getLotteryType())) {
            checkPeriodType(activityCog.getPeriodType(), activityCog.getStartDate(), activityCog.getEndDate());
        }

        // 初始化促销及筛选SKU
        if (activityCog.getPromotionSkuKeyAry() != null) {
            Set<String> skuSet = new HashSet<>(Arrays.asList(activityCog.getPromotionSkuKeyAry()));
            activityCog.setPromotionSkuKey(StringUtils.join(skuSet, ","));
        }
        if (activityCog.getFilterSkuKeyAry() != null) {
            Set<String> skuSet = new HashSet<>(Arrays.asList(activityCog.getFilterSkuKeyAry()));
            activityCog.setFilterSkuKey(StringUtils.join(skuSet, ","));
        }
        activityCog.fillFields(currentUserKey);
	    
	    // 如果筛选条件发生变化，则需要重新筛选人数
        VpsVcodeDoublePrizeCog oldActivityCog = doublePrizeCogDao.findById(activityCog.getActivityKey());
        if (StringUtils.isNotBlank(activityCog.getFilterStartDate()) && "0".equals(activityCog.getFilterType())) {
            if (!oldActivityCog.getFilterStartDate().equals(activityCog.getFilterStartDate())
                    || !oldActivityCog.getFilterEndDate().equals(activityCog.getFilterEndDate())
                    || oldActivityCog.getFilterSkuTotal() != activityCog.getFilterSkuTotal()
                    || !oldActivityCog.getFilterDoubtStatus().equals(activityCog.getFilterDoubtStatus())
                    || !CollectionUtils.isEqualCollection(Arrays.asList(oldActivityCog
                            .getFilterAreaCode().split(",")), Arrays.asList(activityCog.getFilterAreaCode().split(",")))
                    || !CollectionUtils.isEqualCollection(Arrays.asList(oldActivityCog
                            .getFilterSkuKey().split(",")), Arrays.asList(activityCog.getFilterSkuKey().split(",")))) {
                activityCog.setFilterPersonNum(filterUser(activityCog));
            }
        }
	    doublePrizeCogDao.update(activityCog);
	    
        // 删除活动缓存
        CacheUtilNew.removeGroupByKey(CacheKey.cacheKey.vcode
                .KEY_VCODE_DOUBLE_PRIZE_COG + Constant.DBTSPLIT + activityCog.getActivityKey());
        
        logService.saveLog("doubleprize", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(activityCog), "修改一码双奖活动");
	}
	
	/**
	 * 筛选符合当前活动的用户
	 */
	private int filterUser(VpsVcodeDoublePrizeCog activityCog) {
        
        // 清除用户的活动标签
        Map<String, Object> map = new HashMap<>();
        map.put("activityKey", activityCog.getActivityKey());
        doublePrizeCogDao.filterClearUser(map);

        // 筛选符合活动的用户，打个活动标签
        // 处理筛选区域
        List<Address> areaNameLst = new ArrayList<>();
        if (StringUtils.isNotBlank(activityCog.getFilterAreaName())) {
            List<String> filterAreaNameLst = Arrays.asList(activityCog.getFilterAreaName().split(";"));
            Collections.sort(filterAreaNameLst);
            String areaName = "areaName";
            String[] areaNameAry = null;
            for (String item : filterAreaNameLst) {
                if (item.startsWith(areaName)) continue;
                if (item.indexOf("--") == -1) {
                    areaName = item;
                } else {
                    areaName = item.substring(0, item.indexOf("--"));
                }
                areaNameAry = item.split("_");
                areaNameLst.add(new Address(areaNameAry[0].replace("--", ""), 
                        areaNameAry[1].replace("--", ""), areaNameAry[2].replace("--", "")));
            }
        }
        
        // 根据筛选开始结束日期，确认数据分布的子表
        List<String> tableSuffixLst = PackRecordRouterUtil.getTabSuffixByDate(24, 
                        activityCog.getFilterStartDate(), activityCog.getFilterEndDate());
        
        // 循环生成中间表数据
        map.put("areaNameLst", areaNameLst);
        map.put("startDate", activityCog.getFilterStartDate() + " 00:00:00");
        map.put("endDate", activityCog.getFilterEndDate() + " 23:59:59");
        map.put("filterSkuKeyAry", activityCog.getFilterSkuKey().split(","));
        map.put("filterSkuTotal", activityCog.getFilterSkuTotal());
        for (String tableSuffix : tableSuffixLst) {
            map.put("tableSuffix", tableSuffix);
            doublePrizeCogDao.filterPacks(map);
        }
        
        // 给筛选用户打上活动标签
        map.put("doubtStatusFlag", activityCog.getFilterDoubtStatus());
        doublePrizeCogDao.filterUser(map);
        
        // 删除当前活动的中间表数据
        doublePrizeCogDao.filterDelete(map);
        
        // 返回符合条件的人数
        return doublePrizeCogDao.filterUserNum(map);
	}
	
    /**
     * 修改活动的活动配置添加状态
     * 
     * @param vcodeActivityCog
     */
    public void changeStatus(String activityKey) {
        VpsVcodeDoublePrizeCog doublePrizeCog = findActivityByKey(activityKey);
        doublePrizeCog.setMoneyConfigFlag("1");
        doublePrizeCogDao.update(doublePrizeCog);
    }
    
    /**
     * 清除用户的活动标签
     * 
     * @param activityKey   活动主键
     */
    public void executeClearUser(String activityKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("activityKey", activityKey);
        doublePrizeCogDao.filterClearUser(map);
    }
    
    /**
     * 获取所有已结束且未清除标签的活动
     */
    public List<VpsVcodeDoublePrizeCog> queryAllJobActivity() {
        return doublePrizeCogDao.queryAllJobActivity();
    }
    
    /**
     * 获取所有有效的活动
     */
    public List<VpsVcodeDoublePrizeCog> queryAllValidActivity() {
        return doublePrizeCogDao.queryAllValidActivity();
    }
    
	/**
	 * 转换成扫码活动对应
	 * 
	 * @param doublePrizeCog
	 * @return
	 */
	public VcodeActivityCog transformForActivityCog(VpsVcodeDoublePrizeCog doublePrizeCog) {
	    VcodeActivityCog activityCog = new VcodeActivityCog();
	    activityCog.setVcodeActivityKey(doublePrizeCog.getActivityKey());
	    activityCog.setVcodeActivityName(doublePrizeCog.getActivityName());
	    activityCog.setStartDate(doublePrizeCog.getStartDate());
	    activityCog.setEndDate(doublePrizeCog.getEndDate());
	    activityCog.setActivityType(Constant.activityType.activity_type6);
	    return activityCog;
	}
	
	/**
	 * 获取目前扶持的大奖类型（临时方法，后续实现奖品类型维护功能）
	 * @return
	 */
	public Map<String, String> getPrizeType() throws Exception {
        Map<String, String> prizeMap = vcodeActivityBigPrizeService.getPrizeTypeMap();
	    return prizeMap;
	}

    /**
	 *  根据活动ID获取活动信息
	 */
    public VpsVcodeDoublePrizeCog findById(String id){
		return doublePrizeCogDao.findById(id);
	}
    
    /**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("doubleprize", "activity_key", infoKey, "activity_name", bussionName);
	}
	
	/** 
	 * 清除过期的已中出奖项
	 * @throws Exception 
	 */
	public void executeClearLottery(String activityKey) throws Exception {
	    String lotteryFlagRedisKey = cacheKey.vcode.KEY_VCODE_DOUBLE_PRIZE_COG + ":LotteryFlag";
	    Map<String, String> lotteryCacheMap = RedisApiUtil.getInstance().getHAll(lotteryFlagRedisKey);
	    if (lotteryCacheMap != null) {
	        String[] itemValAry = null;
	        String baseRedisKey = cacheKey.vcode.KEY_VCODE_DOUBLE_PRIZE_COG + ":" + activityKey;
	        for (Entry<String, String> item : lotteryCacheMap.entrySet()) {
                if (StringUtils.isNotBlank(item.getValue())) {
                    // 用户获取抽奖机会奖项缓存:V码,奖项类型,一码双奖活动主键,规则主键,奖项配置项主键,时间戳,获取金额
                    itemValAry = item.getValue().split(",");
                    if (itemValAry.length < 6) {
                        RedisApiUtil.getInstance().delHSet(lotteryFlagRedisKey, item.getKey());
                        log.error("prizeCache:" + item.getValue() + " --- 一码双奖：奖项缓存格式异常");
                        continue;
                    };
                    
                    // 如果超过5分钟，则删除大奖缓存 
                    if ((System.currentTimeMillis() - Long.valueOf(itemValAry[5])) / 60000  > 5) {
                        
                        // 删除缓存
                        RedisApiUtil.getInstance().delHSet(lotteryFlagRedisKey, item.getKey());
                        
                        // 限制反扣、大奖返还
                        Map<String, String> prizeMap = vcodeActivityBigPrizeService.getPrizeTypeMap();
                        if (prizeMap != null && prizeMap.containsKey(itemValAry[1])) {
                            RedisApiUtil.getInstance().setHincrBySet(baseRedisKey + ":everyone", item.getKey(), -1); // 每人限制
                            RedisApiUtil.getInstance().incrBy(baseRedisKey + ":everyDay" + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT), -1); // 每天限制
                        }

                        // 反扣当前奖项配置项的剩余数量redis缓存
                        if (StringUtils.isNotBlank(itemValAry[3]) && StringUtils.isNotBlank(itemValAry[4])) {
                            List<VcodeActivityVpointsCog> cogLst = new ArrayList<VcodeActivityVpointsCog>();
                            VcodeActivityVpointsCog vpointsCog = new VcodeActivityVpointsCog();
                            vpointsCog.setVpointsCogKey(itemValAry[4]);
                            vpointsCog.setRangeVal(-1L);
                            cogLst.add(vpointsCog);
                            vpointsCogDao.updateBathWaitActivityVpointsCog(cogLst);
                            String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                                    .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + itemValAry[3];
                            RedisApiUtil.getInstance().setHincrBySet(redisKey, itemValAry[4], 1);
                        }
                        
                        log.error("prizeCache:" + item.getValue() + " --- 一码双奖：奖项回收");
                    }
                }
            }
	    }
	}
	
	/**
	 * 校验周期类型对应的开始及结束日期的合法性
	 * 
	 * @param periodType
	 * @param startDate
	 * @param endDate
	 * @throws Exception 
	 */
	public void checkPeriodType(String periodType, String startDate, String endDate) throws Exception {

        if (Constant.periodType.PERIOD_TYPE0.equals(periodType)) {
            if (1 != DateUtil.getDayOfWeek(DateUtil.getDateFromDay(startDate, DateUtil.DEFAULT_DATE_FORMAT))) {
                throw new BusinessException("周期类型为自然周时活动的开始日期必需为周一");
            }
            if (7 != DateUtil.getDayOfWeek(DateUtil.getDateFromDay(endDate, DateUtil.DEFAULT_DATE_FORMAT))) {
                throw new BusinessException("周期类型为自然周时活动的结束日期必需为周日");
            }
        } else if (Constant.periodType.PERIOD_TYPE1.equals(periodType)) {
            String endOfMonth = DateUtil.getDateTime(DateUtil.getMonthLastDay(DateUtil
                    .getDateFromDay(endDate, DateUtil.DEFAULT_DATE_FORMAT)), DateUtil.DEFAULT_DATE_FORMAT);
            if (!"01".equals(startDate.substring(8))
                        || !endOfMonth.equals(endDate)) {
                throw new BusinessException("周期类型为自然月时周期必需是完整的月");
            }
        } else if (Constant.periodType.PERIOD_TYPE3.equals(periodType)) {
            if (DateUtil.diffDays(endDate, startDate) > 31) {
                throw new BusinessException("周期类型为活动周期时最大支持31天");
            }
        }
	}
}
