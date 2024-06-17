package com.dbt.platform.job;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PackRecordRouterUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.wechat.service.SendAppletMsgService;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.clientcenter.bean.VpsClientUserInfo;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementSigninRecord;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementUserDetail;
import com.dbt.platform.wechatmovement.service.VpsWechatMovementSigninRecordService;
import com.dbt.platform.wechatmovement.service.VpsWechatMovementUserDetailService;

/**
 * 微信运动消息通知Job
 * @author hanshimeng
 *
 */
@Service("sendWechatmovementMessageJob")
public class SendWechatmovementMessageJob {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private SendAppletMsgService sendAppletMsgService;
	@Autowired
	private VpsWechatMovementUserDetailService wechatMovementUserDetailService;
	@Autowired
	private VpsWechatMovementSigninRecordService wechatMovementSigninRecordService;

	/**
	 * 活动进行中提醒（每天08:50）
	 */
	public void activityUnderway(){
//		Map<String, Object> map = new HashMap<>();
//		map.put("periodsKey", periodsKey);
//		map.put("tableIndex", PackRecordRouterUtil.getTabSuffixByDate(4, DateUtil.getDate()));
//		List<VpsConsumerUserInfo> userInfoList = userInfoService.queryWechatMovemenUser(map);
		String[] userGroup = null;
		int idx = 0;
		try{
			log.warn("活动进行中提醒job处理开始");
			
			// 获取今天期数
			String periodsKey = new DateTime().toString("yyyyMMdd");
			// 参与今天活动的用户缓存KEY
			String domain = CacheKey.cacheKey.USER_INFO
					.KEY_USER_JOIN_WECHAT_MOVEMENT + Constant.DBTSPLIT + periodsKey;
			Map<String, String> userMap = RedisApiUtil.getInstance().getHAll(domain);
			for (Entry<String, String> item : userMap.entrySet()) {
				idx++;
				if(idx % 500 == 0){
					Thread.sleep(1000);
				}
				userGroup = item.getValue().split("!@#");
				if(userGroup.length == 2){
					sendAppletMsgService.sendAppletMsg(new VpsConsumerUserInfo(userGroup[0], userGroup[1]), 
						0, Constant.APPLET_TEMPLATE_TYPE.type_1, Constant.APPLET_MSG_TYPE.type_2, null);
				}
			}
			log.warn("活动进行中提醒job处理结束");
		}catch(Exception e){
			log.error("发送模板消息job：活动进行中方法异常", e);
		}
	}
	
	/**
	 * 周赛开赛提醒（周日20:30）
	 */
	public void weekActivityStart(){
		int idx = 0;
		String[] userGroup = null;
		try{
			log.warn("周赛开赛提醒job处理开始");
			
			// 参与微信运动的用户
			String isJoinDomain = CacheKey.cacheKey.USER_INFO.KEY_USER_JOIN_WECHAT_MOVEMENT_FLAG;
			Map<String, String> userMap = RedisApiUtil.getInstance().getHAll(isJoinDomain);
			for (Entry<String, String> item : userMap.entrySet()) {
				idx++;
				if(idx % 500 == 0){
					Thread.sleep(2000);
				}
				userGroup = item.getValue().split("!@#");
				if(userGroup.length == 2){
					sendAppletMsgService.sendAppletMsg(
							new VpsConsumerUserInfo(userGroup[0], userGroup[1]), 0,
							Constant.APPLET_TEMPLATE_TYPE.type_4, Constant.APPLET_MSG_TYPE.type_3, null);
				}
			}
			log.warn("周赛开赛提醒job处理开始");
		}catch(Exception e){
			log.error("发送模板消息job：周赛开赛方法异常", e);
		}
	}
	
	/**
	 * 同步步数提醒（每天20:00）
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public void syncStep() throws InterruptedException{
		log.warn("同步步数提醒job处理开始");

		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.WECHAT_MOVEMENT_MESSAGE_SYNC_STEP);
    	if(StringUtils.isBlank(projectServerNames)) return;
    	
    	if(!"ALL".equals(projectServerNames)){
    		nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
    	}else{
    		nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
					.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
    	}
    	
    	// 循环执行任务
    	for (String projectServerName : nameList) {
			DbContextHolder.setDBType(projectServerName);

			int idx = 0;
			String[] userGroup = null;
			// 获取今天期数
			String periodsKey = new DateTime().toString("yyyyMMdd");
			// 参与今天活动的用户缓存KEY
			String domain = CacheKey.cacheKey.USER_INFO
					.KEY_USER_JOIN_WECHAT_MOVEMENT + Constant.DBTSPLIT + periodsKey;
			Map<String, String> userMap = RedisApiUtil.getInstance().getHAll(domain);
			
			// 参与今天PK赛缓存KEY
			String domainPk = CacheKey.cacheKey.USER_INFO
					.KEY_USER_JOIN_WECHAT_MOVEMENT_PK + Constant.DBTSPLIT + periodsKey;
			if(null != userMap){
				userMap.putAll(RedisApiUtil.getInstance().getHAll(domainPk));
			}else{
				userMap = RedisApiUtil.getInstance().getHAll(domainPk);
			}
			
			for (Entry<String, String> item : userMap.entrySet()) {
				idx++;
				if(idx % 500 == 0){
					Thread.sleep(2000);
				}
				userGroup = item.getValue().split("!@#");
				if(userGroup.length == 2){
					sendAppletMsgService.sendAppletMsg(
							new VpsConsumerUserInfo(userGroup[0], userGroup[1]), 0, 
							Constant.APPLET_TEMPLATE_TYPE.type_1, Constant.APPLET_MSG_TYPE.type_4, projectServerName);
				}
			}
			
	        DbContextHolder.clearDBType();
			Thread.sleep(100);
		}
		
        log.warn("同步步数提醒job处理结束");
	}
	
	/**
	 * 活动已达标提醒（每天09:00）
	 */
	public void activityFinish(){
		int idx = 0;
		try{
			log.warn("活动已达标提醒job处理开始");
			
			// 昨天日期
			String yesterDay = new DateTime().plusDays(-1).toString("yyyy-MM-dd");
			// 昨天期数
			String periodsKey = yesterDay.replaceAll("-", ""); 
			Map<String, Object> map = new HashMap<>();
			map.put("periodsKey", periodsKey);
			map.put("isFinish", "1");
			map.put("tableIndex", PackRecordRouterUtil.getTabSuffixByDate(4, yesterDay));
			List<VpsWechatMovementUserDetail> detailList = 
					wechatMovementUserDetailService.queryWechatMovemenUserInfo(map);
			if(null != detailList && !CollectionUtils.isEmpty(detailList)){
				for (VpsWechatMovementUserDetail item : detailList) {
					idx++;
					if(idx % 500 == 0){
						Thread.sleep(2000);
					}
					sendAppletMsgService.sendAppletMsg(new VpsConsumerUserInfo(
							item.getOpenid(), item.getNickName()), item.getEarnMoney(), 
							Constant.APPLET_TEMPLATE_TYPE.type_2, Constant.APPLET_MSG_TYPE.type_5, null);
				}
			}
			log.warn("活动已达标提醒job处理结束");
		}catch(Exception e){
			log.error("发送模板消息job：活动已达标方法异常", e);
		}
	}
	
	/**
	 * 周赛已达标提醒（周一09:00）
	 */
	public void weekActivityFinish(){
		int idx = 0;
		try{
			log.warn("周赛已达标提醒job处理开始");
			
			// 当前年的第几周，格式：201810
			String signWeek = DateUtil.getWeekOfYearFor53(DateUtil.addDays(-1));
			Map<String, Object> map = new HashMap<>();
			map.put("signWeek", signWeek);
			List<VpsWechatMovementSigninRecord> detailList = 
					wechatMovementSigninRecordService.queryFinishSigninRecord(map);
			if(null != detailList && !CollectionUtils.isEmpty(detailList)){
				for (VpsWechatMovementSigninRecord item : detailList) {
					idx++;
					if(idx % 500 == 0){
						Thread.sleep(2000);
					}
					sendAppletMsgService.sendAppletMsg(new VpsConsumerUserInfo(
							item.getOpenid(), item.getNickName()), item.getEarnMoney(), 
							Constant.APPLET_TEMPLATE_TYPE.type_2, Constant.APPLET_MSG_TYPE.type_6, null);
				}
			}
			log.warn("周赛已达标提醒job处理结束");
		}catch(Exception e){
			log.error("发送模板消息job：周赛已达标方法异常", e);
		}
	}
}
