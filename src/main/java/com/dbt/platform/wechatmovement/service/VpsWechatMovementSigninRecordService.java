package com.dbt.platform.wechatmovement.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.appuser.service.VpsConsumerAccountInfoService;
import com.dbt.platform.wechatmovement.bean.VpsWechatActivityBasicInfo;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementSigninRecord;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementUserDetail;
import com.dbt.platform.wechatmovement.dao.IVpsWechatMovementSigninRecordDao;

@Service
public class VpsWechatMovementSigninRecordService extends BaseService<VpsWechatMovementSigninRecord>{

	@Autowired
	private IVpsWechatMovementSigninRecordDao wechatMovementSigninRecordDao;
	@Autowired
	private VpsConsumerAccountInfoService consumerAccountInfoService;
	
	/**
	 * 获取已完成周赛信息
	 * @param map:signWeek
	 * @return
	 */
	public List<VpsWechatMovementSigninRecord> queryFinishSigninRecord(Map<String, Object> map) {
		return wechatMovementSigninRecordDao.queryFinishSigninRecord(map);
	}
	
	/**
	 * 活动达标后更新周赛信息（凌晨之后执行）
	 * @param detailList 该期已达标用户数据list
	 * @throws Exception 
	 */
	public void executeUserSigninRecord(List<VpsWechatMovementUserDetail> detailList, 
			String periodsKey, VpsWechatActivityBasicInfo activateBasicInfo) throws Exception {
		String signDates = null;
		String dayOfWeek = null;
		String signWeek = null;
		
		if(StringUtils.isNotBlank(periodsKey)){
			// 几号
			signDates = periodsKey.substring(6);
			// 周几
			dayOfWeek = DateUtil.getDayOfWeek(DateUtil.parse(periodsKey, DateUtil.DEFAULT_DATE_FORMAT_SHT)) + "";
			// 第几周
			signWeek = DateUtil.getWeekOfYearFor53(DateUtil.parse(periodsKey, DateUtil.DEFAULT_DATE_FORMAT_SHT));
		}else{
			// 昨天几号
			signDates = DateUtil.getYesterDay() + "";
			// 昨天周几
			dayOfWeek = DateUtil.getDayOfWeek(DateUtil.addDays(-1)) + "";
			// 昨天是第几周，格式：201810
			signWeek = DateUtil.getWeekOfYearFor53(DateUtil.addDays(-1));
		}
				
		double earnMoney = Double.parseDouble(StringUtils.defaultIfBlank(activateBasicInfo.getWeeksReachMoney(), "0.00"));
		String earnTime = DateUtil.parseDate(periodsKey, DateUtil.DATE_TYPE_FORMAT_SHT) + " 23:59:59";
		List<VpsWechatMovementSigninRecord> updateUserList = new ArrayList<VpsWechatMovementSigninRecord>();
		List<String> immediatelyfinishUserKeyList = new ArrayList<String>();
		for (VpsWechatMovementUserDetail detail : detailList) {
			// 组装新增用户周赛信息	
			if(StringUtils.isNotBlank(detail.getWeekSignInfoKey())){
				// 即将完成周赛用户
				if(detail.getSignDays() == 6){
					immediatelyfinishUserKeyList.add(detail.getUserKey());
					updateUserList.add(new VpsWechatMovementSigninRecord(
							detail.getUserKey(), signWeek, signDates, dayOfWeek, 1, earnMoney, earnTime));
				}else{
					updateUserList.add(new VpsWechatMovementSigninRecord(
							detail.getUserKey(), signWeek, signDates, dayOfWeek, 1, 0.00, null));
				}
			}
		}
		
		// 批量更新周赛信息
		if(!CollectionUtils.isEmpty(updateUserList)){
			wechatMovementSigninRecordDao.batchUpdate(updateUserList);
		}
		
		// 更新账户信息
		if(!CollectionUtils.isEmpty(immediatelyfinishUserKeyList)){
			consumerAccountInfoService.batchUpdateUserVpoints(immediatelyfinishUserKeyList, 0, earnMoney, earnTime);
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(DateUtil.getWeekOfYearFor53(DateUtil.parse("20190119", DateUtil.DEFAULT_DATE_FORMAT_SHT)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
