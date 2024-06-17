package com.dbt.framework.wechat.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.appuser.bean.VpsConsumerThirdAccountInfo;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.dao.IVpsConsumerThirdAccountInfoDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatActivityBasicInfo;
import com.dbt.platform.wechatmovement.service.VpsWechatActivityBasicInfoService;
/**
 * 小程序推送消息Service
 * @author hanshimeng
 *
 */
@Service
public class SendAppletMsgService {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IVpsConsumerThirdAccountInfoDao thirdAccountInfoDao;
	@Autowired
	private VpsWechatActivityBasicInfoService wechatActivityBasicInfoService;
	
	/**
	 * 小程序推送模板消息
	 * @param userInfo 用户信息
	 * @param earnVpoints 金额（分）
	 * @param templateType 小程序模板类型：1活动信息提醒，2比赛结果通知，3任务完成通知, 4周赛提醒通知
	 * @param msgType 小程序消息类型：1报名成功，2活动开赛，3周赛开赛，4同步步数，5每日活动达标，6周赛达标，7邀请好友，8关注公众号
	 */
	public void sendAppletMsg(VpsConsumerUserInfo userInfo, double earnMoney, String templateType, String msgType, String projectServerName){
		if(Constant.APPLET_MSG_TYPE.type_1.equals(msgType)
				|| Constant.APPLET_MSG_TYPE.type_2.equals(msgType)) return;
		
		// openid
		String openid = userInfo.getOpenid();
		if(StringUtils.isBlank(openid)){
			return;
		}else{
			VpsConsumerThirdAccountInfo thirdInfo = thirdAccountInfoDao.queryThirdAccountInfoByOpenid(openid);
			if(null == thirdInfo) return;
			openid = thirdInfo.getPaOpenid();
			if(StringUtils.isBlank(openid)) return;
		}
		
		
		// 微信昵称
		String nickName = userInfo.getNickName();
		nickName = StringUtils.isBlank(nickName) || "null".equals(nickName) ? "--" : nickName;
		
		// 参赛时间（当天）
		String date = DateUtil.parseDate(DateUtil.getDate(), DateUtil.DATE_TYPE_8);
		
		// 积分转为金额
		String money = earnMoney+"";
		
		// 获取活动基础设置
		VpsWechatActivityBasicInfo activateBasicInfo = null;
		if(Constant.APPLET_MSG_TYPE.type_2.equals(msgType) 
				|| Constant.APPLET_MSG_TYPE.type_3.equals(msgType) 
				|| Constant.APPLET_MSG_TYPE.type_4.equals(msgType)){
			activateBasicInfo = wechatActivityBasicInfoService.findWechatActivityBasicInfo();
			if(null == activateBasicInfo){
				log.error("推送模板消息异常：基础活动未配置"); 
				return;
			}
			String nowTime = DateUtil.getDateTime();
			if(nowTime.compareTo(activateBasicInfo.getStartTime()) < 0){
				log.error("推送模板消息异常：活动未开始"); 
				return;
			}
			if(nowTime.compareTo(activateBasicInfo.getEndTime()) > 0){
				log.error("推送模板消息异常：活动已结束"); 
				return;
			}
		}
		
		// 活动进行中
		if(Constant.APPLET_MSG_TYPE.type_2.equals(msgType)){
			// 当天期数
			String periods = new DateTime().toString("MM-dd");
			
			
			if(activateBasicInfo.getStepLimit() > 0){
				taskExecutor.execute(new AsyncMsgAppletService(openid, templateType, 
						"活动正在进行中", nickName, 
						"嗨，亲爱的参赛者！您报名的" + periods + 
						"期活动正在进中，赶紧完成当日 " + activateBasicInfo.getStepLimit() +
						"步，瓜分红包吧！", 
						date + " 00:00:00", date + " 23:59:59", projectServerName));
			}else{
				log.error("推送模板消息：活动基础配置信息异常");
			}
		}
		
		// 周赛开赛提醒
		if(Constant.APPLET_MSG_TYPE.type_3.equals(msgType)){
			// 获取活动基础设置
			if(activateBasicInfo.getStepLimit() > 0){
				taskExecutor.execute(new AsyncMsgAppletService(openid, templateType, 
						"参与周赛得红包", nickName, 
						"从明天开始一定要记得完成每天" + activateBasicInfo.getStepLimit() +
						"步呦，加油！！！", 
						new DateTime().plusDays(1).toString("yyyy-MM-dd") + " 00:00:00", 
						new DateTime().plusDays(7).toString("yyyy-MM-dd") + " 23:59:59", projectServerName));
			}else{
				log.error("推送模板消息：活动基础配置信息异常");
			}
		}
		
		// 同步步数
		if(Constant.APPLET_MSG_TYPE.type_4.equals(msgType)){
			if(activateBasicInfo.getStepLimit() > 0){
//				taskExecutor.execute(new AsyncMsgAppletService(openid, templateType, 
//						"查看您的步数呦", nickName, 
//						"活动当日达成步数为" + activateBasicInfo.getStepLimit() +
//						"步，达标成功后，便可瓜分当日现金奖池，赶紧点击查看自己的步数吧！", 
//						date + " 00:00:00", date + " 23:59:59"));
				
				taskExecutor.execute(new AsyncMsgAppletService(openid, templateType, 
						"活动即将结束，查看今天步数是否达标~", date + " 23:59:59", projectServerName));
			}else{
				log.error("推送模板消息：活动基础配置信息异常");
			}
		}
		
		// 每日活动达标提醒
		if(Constant.APPLET_MSG_TYPE.type_5.equals(msgType)){
			// 开赛日期
			date = new DateTime().plusDays(-1).toString("yyyy-MM-dd");
			
			// 昨天期数
			String periods = date.replaceAll("-", "").substring(4);
			taskExecutor.execute(new AsyncMsgAppletService(openid, templateType, 
					"每日活动已达标", nickName, 
					"您参与的" + periods +
					"期活动已达标并获得了" + money + 
					"元奖励红包！", 
					date + " 00:00:00", date + " 23:59:59", projectServerName));
		}
				
		// 周赛活动达标提醒
		if(Constant.APPLET_MSG_TYPE.type_6.equals(msgType)){
			taskExecutor.execute(new AsyncMsgAppletService(openid, templateType, 
					"周赛活动已达标", nickName, 
					"您参与的周赛活动已达标并获得了" + money + 
					"元奖励红包！", 
					new DateTime().plusDays(-7).toString("yyyy-MM-dd") + " 00:00:00", 
					new DateTime().plusDays(-1).toString("yyyy-MM-dd") + " 23:59:59", projectServerName));
		}		
	}
	
	public static void main(String[] args) {
		System.out.println("123");
		System.out.println(new DateTime().plusDays(-1).toString("yyyy-MM-dd"));
	}
}
