package com.dbt.platform.wechatmovement.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PackRecordRouterUtil;
import com.dbt.framework.util.RandomUtils;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.platform.appuser.bean.VpsConsumerAccountInfo;
import com.dbt.platform.appuser.service.VpsConsumerAccountInfoService;
import com.dbt.platform.sweep.bean.VpsCommonPacksRecord;
import com.dbt.platform.sweep.dao.IVpsCommonPacksRecordDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatActivityBasicInfo;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPeriods;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPkApply;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPkRelation;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementUserDetail;
import com.dbt.platform.wechatmovement.dao.IVpsWechatActivityBasicInfoDao;
import com.dbt.platform.wechatmovement.dao.IVpsWechatMovementPeriodsDao;
import com.dbt.platform.wechatmovement.dao.IVpsWechatMovementPkApplyDao;
import com.dbt.platform.wechatmovement.dao.IVpsWechatMovementPkRelationDao;
import com.dbt.platform.wechatmovement.dao.IVpsWechatMovementUserDetailDao;

/**
 * 用户参与微信运动详情Service
 * @author hanshimeng
 *
 */
@Service
public class VpsWechatMovementUserDetailService extends BaseService<VpsWechatMovementUserDetail>{
	@Autowired
	private IVpsWechatMovementPeriodsDao  wechatMovementPeriodsDao;
	@Autowired
	private IVpsWechatActivityBasicInfoDao wechatActivityBasicInfoDao;
	@Autowired
	private IVpsWechatMovementUserDetailDao wechatMovementUserDetailDao;
	@Autowired
	private VpsWechatMovementSigninRecordService wechatMovementSigninRecordService;
	@Autowired
	private VpsConsumerAccountInfoService consumerAccountInfoService;
	@Autowired
	private IVpsWechatMovementPkApplyDao wechatMovementPkApplyDao;
	@Autowired
	private IVpsWechatMovementPkRelationDao wechatMovementPkRelationDao;
	@Autowired
	private IVpsCommonPacksRecordDao commonPacksRecordDao;

	/**
	 * 处理比赛结果
	 * @param periodsKey 期数，格式20180808
	 * @throws Exception 
	 */
	public void executeCompetitionResult(String periodsKey) throws Exception {
		// 获取活动基础设置
		VpsWechatActivityBasicInfo activateBasicInfo = 
				wechatActivityBasicInfoDao.findWechatActivityBasicInfo();
		if(null == activateBasicInfo){
			throw new Exception("处理比赛结果异常：活动基础数据配置为空");
		}
		
		// 已达标数量
		int finishCount = 0;
		// 达标后每人分发红包
		double earnMoney = 0.00;
		// 第几周
		String signWeek = null;
		// 表后缀
		String tableIndex = null;
		
		boolean isManual = false;
		
		if(StringUtils.isBlank(periodsKey)){
			// 上期期数
			periodsKey = new DateTime().plusDays(-1).toString("yyyyMMdd");
			// 上期是第几周，格式：201810
			signWeek = DateUtil.getWeekOfYearFor53(DateUtil.addDays(-1));
		}else{
			// 第几周
			signWeek = DateUtil.getWeekOfYearFor53(DateUtil.parse(periodsKey, DateUtil.DEFAULT_DATE_FORMAT_SHT));
			isManual = true;
		}
		
		// 根据期数查询概况信息
		VpsWechatMovementPeriods periods = wechatMovementPeriodsDao.findWechatMovemenPeriods(periodsKey);
		if(null == periods){
			log.warn("处理比赛结果异常：获取期数概况为空"); 
			return;
		}else if(periods.getFinishCount() > 0){
			log.warn("处理比赛结果异常：该期的比赛结果已经处理过了，无需重复处理"); 
			return;
		}
		
		// 表后缀
		tableIndex = PackRecordRouterUtil.getTabSuffixByDate(4, DateUtil.parseDate(periodsKey, DateUtil.DATE_TYPE_FORMAT_SHT));
		
		// 获取已达标List
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("signWeek", signWeek);
		map.put("periodsKey", periodsKey);
		map.put("stepLimit", activateBasicInfo.getStepLimit());
		map.put("tableIndex", tableIndex);
		List<VpsWechatMovementUserDetail> detailList = 
				wechatMovementUserDetailDao.queryFinishWechatMovemenList(map);
		if(CollectionUtils.isEmpty(detailList)){
			periods.setFinishCount(0);
			periods.setFinishAvgMoney(100);
			wechatMovementPeriodsDao.updateFinishInfo(periods);
			log.warn("处理比赛结果异常：根据期数"+periodsKey+"获取已达标记录为空！");
			return;
		}else{
			if(detailList.get(0).getEarnMoney() > 0){
				log.warn("处理比赛结果异常：该期的比赛结果已经处理过了，无需重复处理");
				return;
			}
		}
		
		finishCount = detailList.size();
		
		// 中奖金额 = 总泡币 / 达标人数 * 泡币单价
		// ROUND_DOWN 直接删除多余的小数位
		String price = periods.getCurrencyPrice();
		earnMoney = new BigDecimal(periods.getTotalVpoints())
				.multiply(new BigDecimal(price))
				.divide(new BigDecimal(finishCount), 2, BigDecimal.ROUND_DOWN).doubleValue();
				
		// 修改已达标人数
		periods.setFinishCount(finishCount);
		periods.setFinishAvgMoney(earnMoney);
		wechatMovementPeriodsDao.updateFinishInfo(periods);
		
		// 因为job执行时间为次日凌晨，所以时间设置为前一天
		String earnTime = null;
		if(isManual){
			earnTime = DateUtil.parseDate(periodsKey, DateUtil.DATE_TYPE_FORMAT_SHT) + " 23:59:59";
		}else{
			earnTime = DateUtil.getDateTime(DateUtil.addDays(-1),DateUtil.DEFAULT_DATE_FORMAT) + " 23:59:59";
		}
		
		// 更新已达标记录
		map.put("earnMoney", earnMoney);
		map.put("earnTime", earnTime);
		wechatMovementUserDetailDao.updateFinishDetailByMap(map);
		
		// 修改账户信息 
		List<String> userList = ReflectUtil.getFieldsValueByName("userKey", detailList.subList(0, detailList.size()));
		consumerAccountInfoService.batchUpdateUserVpoints(userList, 0, earnMoney, earnTime);
		
		// 更新周赛信息
		wechatMovementSigninRecordService.executeUserSigninRecord(detailList, periodsKey, activateBasicInfo);
		
		// 锦鲤红包（规则：当期达标用户中随机抽取一个未获取过的用户）
		map.put("stepLimit", 0);
		detailList = wechatMovementUserDetailDao.queryFinishWechatMovemenList(map);
		userList = ReflectUtil.getFieldsValueByName("userKey", detailList.subList(0, detailList.size()));
		dealForKioRedpacket(userList, earnTime);
	}
	
	/**
	 * 处理锦鲤红包
	 * @param userList
	 * @param earnTime 
	 */
	private void dealForKioRedpacket(List<String> userList, String earnTime) {
		// 判断时间是否开启
		String limitTime = DatadicUtil.getDataDicValue(
				DatadicUtil.dataDicCategory.DATA_CONSTANT_CONFIG, 
				DatadicUtil.dataDic.dataConstantConfig.KIO_PACKED_LIMIT_TIME);
		if(StringUtils.isBlank(limitTime) || limitTime.split(",").length != 2) return;
		
		String nowTime = DateUtil.getDateTime();
		String startTime = limitTime.split(",")[0];
		String endTime = limitTime.split(",")[1];
		if(startTime.compareTo(nowTime) > 0 || endTime.compareTo(nowTime) < 0) return;
		
		String userKey = null;
		boolean isFlag = false;
		double kioMoney = 0;
		// 用户锦鲤红包KEY
		String kioRedpackedKey = CacheKey.cacheKey.USER_INFO.KEY_USER_KOI_REDPACKED_INFO;
		while(true){
			long index = RandomUtils.random(0, userList.size()-1);
			userKey = userList.get((int)index);
			if(StringUtils.isBlank(RedisApiUtil.getInstance().getHSet(kioRedpackedKey, userKey))){
				RedisApiUtil.getInstance().setHSet(kioRedpackedKey, userKey, "1");
				isFlag = true;
				break;
			}else{
				userList.remove(userKey);
			}
			if(userList.size() == 0) break;
		}
		if(isFlag){
			kioMoney = Double.parseDouble(StringUtils.defaultIfBlank(
					DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory.DATA_CONSTANT_CONFIG,
	        		DatadicUtil.dataDic.dataConstantConfig.KIO_PACKED_MONEY), "0.00")
					);
			// 红包上限超过100元视为异常配置（可调节）
			kioMoney = kioMoney > 100.00 ? 0 : kioMoney;
			
			// 插入锦鲤红包记录
            VpsCommonPacksRecord record = new VpsCommonPacksRecord();
            record.setInfoKey(UUID.randomUUID().toString());
            record.setUserKey(userKey);
            record.setEarnMoney(kioMoney);
            record.setEarnTime(earnTime);
            record.setPrizeType(Constant.PrizeType.status_0);
            record.setEarnChannel(Constant.commonPacketChannel.CHANNEL_4);
            commonPacksRecordDao.create(record);
            
            // 更新账户金额
            consumerAccountInfoService.executeAddUserAccountPoints(userKey, 0, kioMoney);
		}
	}

	/**
	 * 根据期数查询参与运动数据List
	 * @param periodsKey
	 * @param tableIndex
	 * @return
	 */
	public List<VpsWechatMovementUserDetail> queryWechatMovemen(Map<String, Object> map) {
		return wechatMovementUserDetailDao.queryWechatMovemen(map);
	}
	
	/**
	 * 根据期数查询参与用户List
	 * @param periodsKey
	 * @param tableIndex
	 * @param isFinish
	 * @return
	 */
	public List<VpsWechatMovementUserDetail> queryWechatMovemenUserInfo(Map<String, Object> map) {
		return wechatMovementUserDetailDao.queryWechatMovemenUserInfo(map);
	}
	
	
	/**
	 * PK赛匹配
	 * @param periodsKey
	 * @throws Exception
	 */
	public void executePkRelation(String periodsKey) throws Exception {

//		String pkSwitch = DatadicUtil.getDataDicValue(
//				DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
//				DatadicKey.filterSwitchSetting.WECHAT_MOVEMENT_PK_SWITCH);
//		if(!"1".equals(pkSwitch)){
//			log.warn("PK赛活动已结束"); 
//			return;
//		} 
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		
		// 第几周
		String signWeek = null;
		// 表后缀
		String tableIndex = null;
		
		if(StringUtils.isBlank(periodsKey)){
			periodsKey = DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT);
		}
		// 第几周
		signWeek = DateUtil.getWeekOfYearFor53(DateUtil.parse(periodsKey, DateUtil.DEFAULT_DATE_FORMAT_SHT));
		
		// 表后缀
		tableIndex = PackRecordRouterUtil.getTabSuffixByDate(4, DateUtil.parseDate(periodsKey, DateUtil.DATE_TYPE_FORMAT_SHT));
		
		paramsMap.put("periodsKey", periodsKey);
		paramsMap.put("tableIndex", tableIndex);
		if(wechatMovementPkRelationDao.findCountByPeriodsKey(paramsMap) > 0){
			log.warn("匹配PK赛异常：该期的PK赛已经匹配完成，无需重复处理"); 
			return;
		}
		
		// 获取PK赛记录List
		List<VpsWechatMovementPkApply> applyList = 
				wechatMovementPkApplyDao.queryWechatMovemenPkApply(paramsMap);
		if(CollectionUtils.isEmpty(applyList)){
			log.warn("匹配PK赛结果异常：该期PK赛记录为空！");
			return;
		}
		
		// 匹配时间
		String createTime = DateUtil.parseDate(periodsKey, DateUtil.DATE_TYPE_FORMAT_SHT) + " 00:00:00";
		
		List<String> applyList1 = new ArrayList<String>();
		List<String> applyList2 = new ArrayList<String>();
		List<String> applyList3 = new ArrayList<String>();
		List<VpsWechatMovementPkRelation> relationList = new ArrayList<VpsWechatMovementPkRelation>();
		for (VpsWechatMovementPkApply item : applyList) {
			if(item.getPayVpoints() == 50){
				handlerRelation(item.getUserKey(), applyList1, relationList, createTime);
			}else if(item.getPayVpoints() == 100){
				handlerRelation(item.getUserKey(), applyList2, relationList, createTime);
			}else if(item.getPayVpoints() == 200){
				handlerRelation(item.getUserKey(), applyList3, relationList, createTime);
			}
		}
		
		if(applyList1.size() > 0){
			relationList.add(new VpsWechatMovementPkRelation(applyList1.get(0), null, createTime));
		}
		if(applyList2.size() > 0){
			relationList.add(new VpsWechatMovementPkRelation(applyList2.get(0), null, createTime));
		}
		if(applyList3.size() > 0){
			relationList.add(new VpsWechatMovementPkRelation(applyList3.get(0), null, createTime));
		}
	
		if(!CollectionUtils.isEmpty(relationList)){
			paramsMap.put("relationList", relationList);
			paramsMap.put("createTime", DateUtil.getDateTime());
			paramsMap.put("weekDay", Integer.parseInt(signWeek));
			wechatMovementPkRelationDao.createBatch(paramsMap);
		}
	}
	
	private void handlerRelation(String userKey, List<String> applyList, 
			List<VpsWechatMovementPkRelation> relationList, String createTime) {
		applyList.add(userKey);
		if(applyList.size() == 2){
			relationList.add(new VpsWechatMovementPkRelation(applyList.get(0), applyList.get(1), createTime));
			applyList.clear();
		}
		
	}
	
	/**
	 * PK赛结果job（每天00:00）
	 * @param string
	 * @throws Exception 
	 */
	public void executePkResult(String periodsKey) throws Exception {

//		String pkSwitch = DatadicUtil.getDataDicValue(
//				DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
//				DatadicKey.filterSwitchSetting.WECHAT_MOVEMENT_PK_SWITCH);
//		if(!"1".equals(pkSwitch)){
//			log.warn("PK赛活动已结束"); 
//			return;
//		} 
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		
		// 表后缀
		String tableIndex = null;
		
		boolean isManual = false;
		
		if(StringUtils.isBlank(periodsKey)){
			// 上期期数
			periodsKey = new DateTime().plusDays(-1).toString("yyyyMMdd");
		}else{
			isManual = true;
		}
		
		// 表后缀
		tableIndex = PackRecordRouterUtil.getTabSuffixByDate(4, DateUtil.parseDate(periodsKey, DateUtil.DATE_TYPE_FORMAT_SHT));
		
		paramsMap.put("periodsKey", periodsKey);
		paramsMap.put("tableIndex", tableIndex);
		VpsWechatMovementPkApply applyInfo = wechatMovementPkApplyDao.findOneByPeriodsKey(paramsMap);
		if(null == applyInfo || !"0".equals(applyInfo.getPkStatus())){
			log.warn("PK赛结果：无报名记录或已经处理过"); 
			return;
		}
		
		// 获取PK赛关系List
		List<VpsWechatMovementPkRelation> relationList = 
				wechatMovementPkRelationDao.queryRelation(paramsMap);
		if(CollectionUtils.isEmpty(relationList)){
			log.warn("PK赛结果：无匹配记录");
			return;
		}
		
		// 因为job执行时间为次日凌晨，所以时间设置为前一天
		String earnTime = null;
		if(isManual){
			earnTime = DateUtil.parseDate(periodsKey, DateUtil.DATE_TYPE_FORMAT_SHT) + " 23:59:59";
		}else{
			earnTime = DateUtil.getDateTime(DateUtil.addDays(-1),DateUtil.DEFAULT_DATE_FORMAT) + " 23:59:59";
		}
		
		List<VpsWechatMovementPkApply> applyList = new ArrayList<VpsWechatMovementPkApply>();
		List<VpsConsumerAccountInfo> accountList = new ArrayList<VpsConsumerAccountInfo>();
		for (VpsWechatMovementPkRelation item : relationList) {
			
			if(Constant.PK_STATUS.status_1.equals(item.getPkStatus())){
				applyList.add(new VpsWechatMovementPkApply(
						item.getUserKey(), Constant.PK_STATUS.status_1, item.getPayVpoints() * 2));
				
				// 排除最后一个匹配空的
				if(StringUtils.isNotBlank(item.getPkUserKey())){
					applyList.add(new VpsWechatMovementPkApply(
							item.getPkUserKey(), Constant.PK_STATUS.status_2, 0));
				}
				
				// 添加积分
				accountList.add(new VpsConsumerAccountInfo(item.getUserKey(), item.getPayVpoints() * 2));
			}else if(Constant.PK_STATUS.status_2.equals(item.getPkStatus())){
				applyList.add(new VpsWechatMovementPkApply(item.getUserKey(), Constant.PK_STATUS.status_2, 0));
				applyList.add(new VpsWechatMovementPkApply(
						item.getPkUserKey(), Constant.PK_STATUS.status_1, item.getPayVpoints() * 2));
				// 添加积分
				accountList.add(new VpsConsumerAccountInfo(item.getPkUserKey(), item.getPayVpoints() * 2));
			}else if(Constant.PK_STATUS.status_3.equals(item.getPkStatus())){
				applyList.add(new VpsWechatMovementPkApply(
						item.getUserKey(), Constant.PK_STATUS.status_3, item.getPayVpoints()));
				// 添加积分
				accountList.add(new VpsConsumerAccountInfo(item.getUserKey(), item.getPayVpoints()));
				
				// 排除最后一个匹配空的
				if(StringUtils.isNotBlank(item.getPkUserKey())){
					applyList.add(new VpsWechatMovementPkApply(
							item.getPkUserKey(), Constant.PK_STATUS.status_3, item.getPayVpoints()));
					// 添加积分
					accountList.add(new VpsConsumerAccountInfo(item.getPkUserKey(), item.getPayVpoints()));
				}
			}
			
		}
		
		// 更新处理PK结果
		if(!CollectionUtils.isEmpty(applyList)){
			paramsMap.put("earnTime", earnTime);
			paramsMap.put("applyList", applyList);
			wechatMovementPkApplyDao.updateBatch(paramsMap);
		}
		
		// 更新用户积分
		if(!CollectionUtils.isEmpty(accountList)){
			consumerAccountInfoService.updateBatchVpoints(earnTime, accountList);
		}
	}
	

	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) throws Exception {
		List<String> userList = new ArrayList<String>();
		userList.add("a");
//		long index = 0;
//		userList.remove(index);
//		System.out.println(userList);
		
		while(true) {
			long index = RandomUtils.random(0, userList.size()-1);
			String userKey = userList.get((int)index);
			System.out.println("userKey=" + userKey);
			userList.remove(userKey);
			if(userList.size() == 0) break;
		}
		System.out.println("OK");
		
	}
}
