package com.dbt.vpointsshop.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsGroupBuyingActivityCog;
import com.dbt.vpointsshop.bean.VpointsSeckillActivityCog;
import com.dbt.vpointsshop.dao.IVpointsGroupBuyingActivityCogDao;
import com.dbt.vpointsshop.dao.IVpointsSeckillActivityCogDao;

/**
 *  商城秒杀活动Service
 * @author Administrator
 *
 */
@Service
public class VpointsSeckillActivityService extends BaseService<VpointsSeckillActivityCog> {

	@Autowired
	private IVpointsSeckillActivityCogDao seckillActivityCogDao;
	@Autowired
	private IVpointsGroupBuyingActivityCogDao groupBuyingActivityCogDao;
	@Autowired
	private VpointsGoodsService goodsService;
	
	/**
	 * 获取某个V码活动
	 * 
	 * @param vcodeActivityKey
	 * @return
	 */
	public VpointsSeckillActivityCog loadActivityByKey(String infoKey) {
		VpointsSeckillActivityCog activityCog = seckillActivityCogDao.findById(infoKey);
	    return activityCog;
	}

	/**
	 * 活动列表
	 * 
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 */
	public List<VpointsSeckillActivityCog> findVcodeActivityList(
			VpointsSeckillActivityCog queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		List<VpointsSeckillActivityCog> activityList = seckillActivityCogDao.queryForLst(map);
        
        if(null != activityList && !activityList.isEmpty()){
        	String date = DateUtil.getDate().replace("-", "");
        	// 当天秒杀结束时间：20201019184200
    		String todayEndTime = null;
    		String seckillNumber = null;
            for (VpointsSeckillActivityCog item : activityList) {
        		todayEndTime = date + item.getEndTime().replaceAll(":", "");
        		
        		// 每期秒杀库存剩余数量 = 每期库存 - 已下单库存
        		seckillNumber = StringUtils.defaultString(RedisApiUtil.getInstance().get(
        				RedisApiUtil.CacheKey.vpointsSeckill.SECKILL_PERIODS_NUM 
        				+ Constant.DBTSPLIT + item.getInfoKey() + Constant.DBTSPLIT + todayEndTime), "0");
        		
        		// 秒杀占用总库存
        		String totalCount = StringUtils.defaultString(RedisApiUtil.getInstance().get(
        				RedisApiUtil.CacheKey.vpointsSeckill.SECKILL_TOTAL_NUM + Constant.DBTSPLIT + item.getInfoKey()), "0");
        		
        		// 剩余总库存
        		int subCount = item.getSeckillTotalNum() - Integer.parseInt(totalCount);
        		// 每期剩余库存
        		int periodsCount  = item.getSeckillPeriodsNum() - Integer.parseInt(seckillNumber);
        		
        		item.setSeckillPeriodsRemainsNum(subCount < periodsCount ? subCount : periodsCount);
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
	public int countVcodeActivityList(VpointsSeckillActivityCog queryBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryBean", queryBean);
		return seckillActivityCogDao.queryForCount(map);
	}

	/**
	 * 创建活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 */
	public void writeActivityCog(VpointsSeckillActivityCog activityCog, SysUserBasis optUser) throws Exception {
	    
	    // 校验名称是否重复
	    if ("1".equals(checkBussionName("seckillActivityCog", "info_key", null, "activity_name", activityCog.getActivityName()))) {
	        throw new BusinessException("活动名称已存在");
	    }
	    
	    // 检验当前商品是否存在时间范围冲突
	    String errorInfo = checkActivityForSeckill(activityCog);
	    if(StringUtils.isNotBlank(errorInfo)) {
	    	throw new BusinessException(errorInfo);
	    }
	    
	    // 完善活动信息
		activityCog.setInfoKey(UUID.randomUUID().toString());
        activityCog.setActivityNo(getBussionNo("seckillActivityCog", "activity_no", Constant.OrderNoType.type_MS));
        activityCog.setSeckillPay(new BigDecimal(activityCog.getSeckillPay())
        		.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString());
		activityCog.fillFields(optUser.getUserKey());

		// 保存活动
		seckillActivityCogDao.create(activityCog);
		logService.saveLog("seckillActivityCog", Constant.OPERATION_LOG_TYPE.TYPE_1, 
		        JSON.toJSONString(activityCog), "创建活动:" + activityCog.getActivityName());
		
		// 预扣商品库存
	    goodsService.updateGoodsRemainsForWithholding(activityCog.getGoodsId(), activityCog.getSeckillTotalNum());
	}

	/**
	 * 更新活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws IOException 
	 */
	public void updateActivityCog(VpointsSeckillActivityCog activityCog, SysUserBasis optUser) throws Exception {
        
		// 校验名称是否重复
	    if ("1".equals(checkBussionName("seckillActivityCog", "info_key", activityCog.getInfoKey(), "activity_name", activityCog.getActivityName()))) {
	        throw new BusinessException("活动名称已存在");
	    }
        
	    // 检验当前商品是否存在时间范围冲突
	    String errorInfo = checkActivityForSeckill(activityCog);
	    if(StringUtils.isNotBlank(errorInfo)) {
	    	throw new BusinessException(errorInfo);
	    }
	    
        // 保存活动
	    activityCog.setSeckillPay(new BigDecimal(activityCog.getSeckillPay())
	    		.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString());
        activityCog.fillUpdateFields(optUser.getUserKey());
        seckillActivityCogDao.update(activityCog);
       
        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
               .KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + activityCog.getInfoKey());
       
       logService.saveLog("seckillActivityCog", Constant.OPERATION_LOG_TYPE.TYPE_2,
               JSON.toJSONString(activityCog), "修改活动:" + activityCog.getActivityName());
    }

	/**
	 * 判断商品及时间范围是否合法
	 * @param activityKey</br>
	 * @param goodsId</br>
	 * @param ruleType</br> 时间类型：1时间段，2周几
	 * @param startDate</br>
	 * @param endDate</br> 
	 * @return String flag 返回异常信息</br>
	 * @throws Exception 
	 */
	public String checkActivityForSeckill(VpointsSeckillActivityCog activityCogInsert) throws Exception {
		String flag = "";
		
		// 判断秒杀
		List<VpointsSeckillActivityCog> activityCogList = seckillActivityCogDao.queryByGoodsId(activityCogInsert.getGoodsId());
		if(CollectionUtils.isNotEmpty(activityCogList)) {
			for (VpointsSeckillActivityCog item : activityCogList) {
				if(item.getInfoKey().equals(activityCogInsert.getInfoKey())) continue;
				
				if("1".equals(checkActivityTime(true, activityCogInsert, item.getRuleType(), 
						item.getBeginDate(), item.getEndDate(), item.getBeginTime(), item.getEndTime(), item.getIsBegin()))) {
					flag = "与秒杀活动["+item.getActivityName()+"]存在时间冲突";
					break;
				}
			}
		}
		
		if(StringUtils.isBlank(flag)) {
			// 判断拼团
			List<VpointsGroupBuyingActivityCog> groupBuyingActivity = groupBuyingActivityCogDao.queryByGoodsId(activityCogInsert.getGoodsId());
			if(CollectionUtils.isNotEmpty(groupBuyingActivity)) {
				for (VpointsGroupBuyingActivityCog item : groupBuyingActivity) {
					if("1".equals(checkActivityTime(false, activityCogInsert, item.getRuleType(), 
							item.getBeginDate(), item.getEndDate(), null, null, item.getIsBegin()))) {
						flag = "与拼团活动["+item.getActivityName()+"]存在时间冲突";
						break;
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 判断商品及时间范围是否合法
	 * @param checkTimeFlag</br> 是否检验时分秒
	 * @param activityCogInsert</br> 新增活动
	 * @param dbRuleType</br> 
	 * @param dbBeginDate</br> 
	 * @param dbEndDate</br> 
	 * @param dbBeginTime</br> 
	 * @param dbEndTime</br> 
	 * @param dbIsBegin</br> 
	 * @return String flag 返回异常信息</br>
	 * @throws Exception 
	 */
	public String checkActivityTime(boolean checkTimeFlag, VpointsSeckillActivityCog activityCogInsert, String dbRuleType, 
			String dbBeginDate, String dbEndDate, String dbBeginTime, String dbEndTime, String dbIsBegin) throws Exception {
		String flag = "0";
		String ruleType = activityCogInsert.getRuleType(); 
		String beginDate = activityCogInsert.getBeginDate();
		String endDate = activityCogInsert.getEndDate();
		String beginTime = activityCogInsert.getBeginTime();
		String endTime = activityCogInsert.getEndTime();
		
		// 已过期
		if("2".equals(dbIsBegin)) {
			return flag;
		}
		
		// 新增活动时间为时间段
		if("1".equals(ruleType)) {
			
			// 数据库数据为时间段
			if("1".equals(dbRuleType)) {
				// as>be || ae<bs
				if(beginDate.compareTo(dbEndDate) > 0 || endDate.compareTo(dbBeginDate) < 0) {
					flag = "0";
				}else if(checkTimeFlag && (beginTime.compareTo(dbEndTime) > 0 || endTime.compareTo(dbBeginTime) < 0)){
					flag = "0";
				}else {
					flag = "1";
				}
				
			// 数据库数据为周几
			}else if("2".equals(dbRuleType)){
				Date newBeginDate = DateUtil.parse(beginDate, DateUtil.DEFAULT_DATE_FORMAT);
				Date newEndDate = DateUtil.parse(endDate, DateUtil.DEFAULT_DATE_FORMAT);
				long day = DateUtil.diffDays(newEndDate, newBeginDate);
				for (int i = 0; i <= day; i++) {
					int week = DateUtil.getDayOfWeek(DateUtil.addDays(newBeginDate, i));
					if(week >= Integer.parseInt(dbBeginDate) && week <= Integer.parseInt(dbEndDate)) {
						if(checkTimeFlag) {
							if(beginTime.compareTo(dbEndTime) > 0 || endTime.compareTo(dbBeginTime) < 0) {
								continue;
							}else {
								flag = "1";
								break;
							}
						}else {
							flag = "1";
							break;
						}
					}
				}
			}
			
		// 新增活动时间为周几
		}else {
			// 数据库数据为时间段
			if("1".equals(dbRuleType)) {
				Date newBeginDate = DateUtil.parse(dbBeginDate, DateUtil.DEFAULT_DATE_FORMAT);
				Date newEndDate = DateUtil.parse(dbEndDate, DateUtil.DEFAULT_DATE_FORMAT);
				long day = DateUtil.diffDays(newEndDate, newBeginDate);
				for (int i = 0; i <= day; i++) {
					int week = DateUtil.getDayOfWeek(DateUtil.addDays(newBeginDate, i));
					if(week >= Integer.parseInt(beginDate) && week <= Integer.parseInt(endDate)) {
						if(checkTimeFlag) {
							if(beginTime.compareTo(dbEndTime) > 0 || endTime.compareTo(dbBeginTime) < 0) {
								continue;
							}else {
								flag = "1";
								break;
							}
						}else {
							flag = "1";
							break;
						}
					}
				}
				
			// 数据库数据为周几
			}else if("2".equals(dbRuleType)){
				// as>be || ae<bs
				if(Integer.parseInt(beginDate) > Integer.parseInt(dbEndDate) || Integer.parseInt(endDate) < Integer.parseInt(dbBeginDate)) {
					flag = "0";
				}else {
					if(checkTimeFlag && (beginTime.compareTo(dbEndTime) > 0 || endTime.compareTo(dbBeginTime) < 0)) {
						flag = "0";
					}else {
						flag = "1";
					}
				}
			}
		}
		
		return flag;
	}
	

	public VpointsSeckillActivityCog findById(String vcodeActivityKey) {
		return seckillActivityCogDao.findById(vcodeActivityKey);
	}

	/**
	 * 停止活动
	 * @param infoKey
	 * @throws Exception 
	 */
	public void updateActivityStop(String infoKey) throws Exception {
		seckillActivityCogDao.updateActivityStop(infoKey);
		// 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
               .KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + infoKey);
	}

	/**
	 * 删除活动
	 * @param infoKey
	 * @throws Exception 
	 */
	public void updateActivityDel(String infoKey) throws Exception {
		VpointsSeckillActivityCog info = seckillActivityCogDao.findById(infoKey);
		if(null != info) {
			seckillActivityCogDao.updateActivityDel(infoKey);
			CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
		               .KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + infoKey);
		}
	}

	public static void main(String[] args) {
		// new BigDecimal(activityCog.getSeckillPay()).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString()
		System.out.println(new BigDecimal(125.00).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_UP).toString());
	}
}
