package com.dbt.framework.wechat.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.MsgAppletUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.wechat.bean.SendTemplateMsg;
import com.dbt.framework.wechat.dao.ISendTemplateMsgDao;
import com.dbt.platform.appuser.bean.VpsConsumerAccountInfo;
import com.dbt.platform.appuser.bean.VpsConsumerThirdAccountInfo;
import com.dbt.platform.appuser.dao.IVpsConsumerThirdAccountInfoDao;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.sweep.wechatmsg.DoubleElevenCouponRemind;
import com.dbt.platform.sweep.wechatmsg.HlsTicketTempletMsg;
import com.dbt.platform.wctaccesstoken.bean.WechatTemplateMsg;
import com.dbt.platform.wctaccesstoken.bean.WechatTemplateMsg.msgType;

@Service
public class SendTemplateMsgService extends BaseService<SendTemplateMsg>{

	/** The logger. */
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private VpsConsumerUserInfoService userInfoService;
	@Autowired
	private IVpsConsumerThirdAccountInfoDao thirdAccountInfoDao;
	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private ISendTemplateMsgDao msgDao;
	
	/**
	 * 查询模板列表
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public List<SendTemplateMsg> queryForLst(SendTemplateMsg queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return msgDao.queryForLst(map);
	}

	/**
	 * 查询模板count
	 * @param queryBean
	 * @return
	 */
	public int queryForCount(SendTemplateMsg queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return msgDao.queryForCount(map);
	}
	
	/**
	 * 查询已推送消息数量
	 * @param queryBean
	 * @return
	 */
	public int queryForSendMessageCount(SendTemplateMsg queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return msgDao.queryForSendMessageCount(map);
	}
	
	/**
	 * 获取未执行的消息list
	 * @return
	 */
	public List<SendTemplateMsg> queryNonExecutionMsgList() {
		return msgDao.queryNonExecutionMsgList();
	}
	
	/**
	 * 查询对象
	 * @param infoKey
	 * @return
	 */
	public SendTemplateMsg findByid(String infoKey) {
		return msgDao.findById(infoKey);
	}
	
	/**
	 * 新增模板消息
	 * @param sendTemplateMsg
	 */
	public void create(SendTemplateMsg sendTemplateMsg) {
		// 设置模板消息编号
		String templateNo = getBussionNo("sendTemplateMsg", "template_no", Constant.OrderNoType.type_XX);
		sendTemplateMsg.setTemplateNo(templateNo);
		
		// 设置跳转URL
		String url = null;
		if(Constant.TEMPLATE_URL_TYPE.type_1.equals(sendTemplateMsg.getUrlType())){
			if(Constant.sendMessageType.messageType_1.equals(sendTemplateMsg.getMessageType())){
				url = DatadicUtil.getDataDicValue(
						DatadicUtil.dataDicCategory.VPOINTS_ESTORE_COG, 
						DatadicUtil.dataDic.vpointsEstoreCog.vpointsImageTextUrl);
			}else if(Constant.sendMessageType.messageType_2.equals(sendTemplateMsg.getMessageType())){
				url = PropertiesUtil.getPropertyValue("wechat_tmpmsg_paApplet_url");
			}
		}
		sendTemplateMsg.setUrl(url);
		
		msgDao.create(sendTemplateMsg);
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_1, 
		        JSON.toJSONString(sendTemplateMsg), "新增模板消息:" + sendTemplateMsg.getInfoKey());
	}
	
	/**
	 * 修改模板消息
	 * @param sendTemplateMsg
	 */
	public void update(SendTemplateMsg sendTemplateMsg, boolean isAddLog) {
		msgDao.update(sendTemplateMsg);
		if(isAddLog) {
			logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, 
			        JSON.toJSONString(sendTemplateMsg), "修改模板消息:" + sendTemplateMsg.getInfoKey());
		}
	}
	
	/**
	 * 删除成功
	 * @param infoKey
	 */
	public void deleteById(String infoKey, String userKey) {
		SendTemplateMsg sendTemplateMsg = findByid(infoKey);
		if(null != sendTemplateMsg){
			sendTemplateMsg.setDeleteFlag(Constant.DbDelFlag.del);
			sendTemplateMsg.fillFields(userKey);
		}
		update(sendTemplateMsg, true);
		logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_3, 
		        JSON.toJSONString(sendTemplateMsg), "删除模板消息:" + sendTemplateMsg.getInfoKey());
	}
	
	/**
	 * 发送公众号模板消息
	 */
	public String sendTemplateMsg(SendTemplateMsg sendTemplateMsg) throws Exception{
		// 基础验证
		String msg = verify(sendTemplateMsg);
		if(StringUtils.isNotBlank(msg)){
			return msg;
		}

		// openid集合
		Set<String> openidList = new HashSet<String>();
		
		// 获取指定用户openid
		String userKey = sendTemplateMsg.getUserKey();
		if(StringUtils.isNotBlank(userKey)){
			List<String> userKeyGro = Arrays.asList(userKey.split(","));
			openidList.addAll(userInfoService.queryOpenidByUserkey(userKeyGro));
		}
		
		// 获取符合条件openid
		String minVpoint = sendTemplateMsg.getMinVpoint();
		String maxVpoint = sendTemplateMsg.getMaxVpoint();
		String minMoney = sendTemplateMsg.getMinMoney();
		String maxMoney = sendTemplateMsg.getMaxMoney();
		if(StringUtils.isNotBlank(minVpoint) || StringUtils.isNotBlank(maxVpoint) 
				|| StringUtils.isNotBlank(minMoney) || StringUtils.isNotBlank(maxMoney)){
			openidList.addAll(userInfoService.queryValidOpenid(minVpoint, maxVpoint, minMoney, maxMoney));
		}
		
		// 更新消息推送状态
		sendTemplateMsg.setStatus("1");
					
		if(CollectionUtils.isEmpty(openidList)){
			// 更新消息
			update(sendTemplateMsg, false);
			return "无有效用户";
		}
		
		// 发送消息
		msgType type = null;
		String first = sendTemplateMsg.getFirst();
		String keyword1 = sendTemplateMsg.getKeyword1();
		String keyword2 = sendTemplateMsg.getKeyword2();
		String keyword3 = sendTemplateMsg.getKeyword3();
		String keyword4 = sendTemplateMsg.getKeyword4();
		String url =  sendTemplateMsg.getUrl();
		String remark = sendTemplateMsg.getRemark();
		WechatTemplateMsg templetMsg = null;
		if(Constant.TEMPLATE_TYPE.type_1.equals(sendTemplateMsg.getTemplateType())){
			type = WechatTemplateMsg.msgType.SERVICE_NOTIFICATIONS;
		}else if(Constant.TEMPLATE_TYPE.type_2.equals(sendTemplateMsg.getTemplateType())){
			type = WechatTemplateMsg.msgType.GOODS_NOTIFICATIONS;
			keyword2 = keyword2 + " 积分";
			keyword3 = "截止" + new DateTime(keyword3).toString("yyyy年MM月dd日");
			// 积分
		}if(Constant.TEMPLATE_TYPE.type_3.equals(sendTemplateMsg.getTemplateType())){
			type = WechatTemplateMsg.msgType.SPECIAL_NOTIFICATIONS;
		}
		for (String openid : openidList) {
        	templetMsg = new WechatTemplateMsg(openid, type);
        	templetMsg.initExchangeRemindSendMsg(first, keyword1, keyword2, keyword3, keyword4, remark, url, DbContextHolder.getDBType());
        	taskExecutor.execute(templetMsg);
		}
		
		// 更新消息
		sendTemplateMsg.setSendCount(openidList.size());
		update(sendTemplateMsg, false);
		return "SUCCESS";
	}
	
	/**
	 * 发送小程序模板消息
	 */
	public String sendAppletTemplateMsg(SendTemplateMsg sendTemplateMsg) throws Exception{
		logger.error("----进入小程序推送消息Service");
		// 基础验证
		String msg = verifyApplet(sendTemplateMsg);
		if(StringUtils.isNotBlank(msg)){
			return msg;
		}

		// paOpenid集合
		Set<String> openidList = new HashSet<String>();
		
		// 获取指定用户paOpenid
		String userKey = sendTemplateMsg.getUserKey();
		if(StringUtils.isNotBlank(userKey)){
			List<String> userKeyGro = Arrays.asList(userKey.split(","));
			openidList.addAll(userInfoService.queryPaOpenidByUserkey(userKeyGro));
		}
		
		// 获取符合条件paOpenid
		String minVpoint = sendTemplateMsg.getMinVpoint();
		String maxVpoint = sendTemplateMsg.getMaxVpoint();
		if(StringUtils.isNotBlank(minVpoint) || StringUtils.isNotBlank(maxVpoint)){
			openidList.addAll(userInfoService.queryValidPaOpenid(minVpoint, maxVpoint));
		}
		
		// 更新消息推送状态
		sendTemplateMsg.setStatus("1");
					
		if(CollectionUtils.isEmpty(openidList)){
			// 更新消息
			update(sendTemplateMsg, false);
			return "无有效用户";
		}
		msgType type = null;
		String formid = null;
		String keyword1 = sendTemplateMsg.getKeyword1();
		String keyword2 = sendTemplateMsg.getKeyword2();
		String keyword3 = sendTemplateMsg.getKeyword3();
		String keyword4 = sendTemplateMsg.getKeyword4();
		String keyword5 = sendTemplateMsg.getKeyword5();
		String page = sendTemplateMsg.getUrl();
		String appid = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
		String appsec = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
		if(Constant.TEMPLATE_TYPE.type_1.equals(sendTemplateMsg.getTemplateType())){
			type = WechatTemplateMsg.msgType.SERVICE_NOTIFICATIONS;
		}else if(Constant.TEMPLATE_TYPE.type_2.equals(sendTemplateMsg.getTemplateType())){
			type = WechatTemplateMsg.msgType.GOODS_NOTIFICATIONS;
			// 积分
		}if(Constant.TEMPLATE_TYPE.type_3.equals(sendTemplateMsg.getTemplateType())){
			type = WechatTemplateMsg.msgType.SPECIAL_NOTIFICATIONS;
		}
        
		int idx = 0;
		for (String openid : openidList) {
			formid = MsgAppletUtil.getFormid(openid);
	    	if(StringUtils.isNotBlank(formid)){
	    		idx ++;
	    		MsgAppletUtil msgAppletUtil = new MsgAppletUtil(
	    				openid, formid, appid, appsec, page, keyword1, keyword2, keyword3, keyword4, keyword5, type, DbContextHolder.getDBType());
	    		taskExecutor.execute(msgAppletUtil);
	    	}
		}
		
		log.error("推送成功：推送用户共" + openidList.size() + "人,含有formid的共" + idx + "人");
		
		// 更新消息
		sendTemplateMsg.setSendCount(idx);
		update(sendTemplateMsg, false);
		return "SUCCESS";
	}
	
	/**
	 * 检验数据
	 * @param sendTemplateMsg
	 */
	private String verify(SendTemplateMsg sendTemplateMsg) {
		if(StringUtils.isBlank(sendTemplateMsg.getFirst())
				|| StringUtils.isBlank(sendTemplateMsg.getKeyword1())
				|| StringUtils.isBlank(sendTemplateMsg.getKeyword2())){
			return "参数不完整";
		}
		
		if(Constant.TEMPLATE_TYPE.type_1.equals(sendTemplateMsg.getTemplateType())){
			if(StringUtils.isBlank(sendTemplateMsg.getKeyword3())
					|| StringUtils.isBlank(sendTemplateMsg.getKeyword4())){
				return "参数不完整：缺少关键字";
			}
		}else if(Constant.TEMPLATE_TYPE.type_2.equals(sendTemplateMsg.getTemplateType())){
			if(StringUtils.isBlank(sendTemplateMsg.getKeyword3())){
				return "参数不完整：缺少关键字";
			}
		}
		
		if(Constant.TEMPLATE_URL_TYPE.type_1.equals(sendTemplateMsg.getUrlType())
				&& StringUtils.isBlank(sendTemplateMsg.getUrl())){
			return "参数不完整:缺少跳转URL";
		}
		return null;
	}
	
	/**
	 * 检验数据
	 * @param sendTemplateMsg
	 */
	private String verifyApplet(SendTemplateMsg sendTemplateMsg) {
		if(StringUtils.isBlank(sendTemplateMsg.getKeyword1())){
			return "参数不完整";
		}
		
		if(Constant.TEMPLATE_TYPE.type_1.equals(sendTemplateMsg.getTemplateType())
				|| Constant.TEMPLATE_TYPE.type_3.equals(sendTemplateMsg.getTemplateType())){
			if(StringUtils.isBlank(sendTemplateMsg.getKeyword2())){
				return "参数不完整：缺少关键字";
			}
		}else if(Constant.TEMPLATE_TYPE.type_2.equals(sendTemplateMsg.getTemplateType())){
			if(StringUtils.isBlank(sendTemplateMsg.getKeyword2())
					|| StringUtils.isBlank(sendTemplateMsg.getKeyword3())){
				return "参数不完整：缺少关键字";
			}
		}
		
		if(Constant.TEMPLATE_URL_TYPE.type_1.equals(sendTemplateMsg.getUrlType())
				&& StringUtils.isBlank(sendTemplateMsg.getUrl())){
			return "参数不完整:缺少跳转URL";
		}
		return null;
	}

	/**
	 * 发送模板消息
	 */
	public String sendTemplateMsg(String userKey) {
		List<VpsConsumerAccountInfo> accountList = new ArrayList<>();
		if(StringUtils.isNotBlank(userKey)){
			 VpsConsumerThirdAccountInfo userAccount = userInfoService.queryOpenidByUserkey(userKey);
			 if(null == userAccount){
				 return "指定用户不存在";
			 }
			 VpsConsumerAccountInfo account = new VpsConsumerAccountInfo();
			 account.setSurplusVpoints(111L);
			 account.setOpenid(userAccount.getOpenid());
			 accountList.add(account);
		}else{
			// 获取剩余积分大于0的用户
			accountList = userInfoService.queryUserByVpoints();
		}
		if(CollectionUtils.isEmpty(accountList)){
			return "发送用户不能为空";
		}
		

		// 发送消息
		String url = DatadicUtil.getDataDicValue(
				DatadicUtil.dataDicCategory.VPOINTS_ESTORE_COG, 
				DatadicUtil.dataDic.vpointsEstoreCog.vpointsImageTextUrl);
		String title = "尊敬的用户，农历2019年元宵佳节快乐，感谢您对青花汾酒的支持！";
		String keyword1 = "剩余积分";
		String keyword2 = "青花汾酒";
		String keyword3 = "2019年06月30日";
		String remark = "点击详情,进入积分商城进行兑换,抓紧呦~~";
		WechatTemplateMsg templetMsg = null;
		for (VpsConsumerAccountInfo account : accountList) {
        	templetMsg = new WechatTemplateMsg(account.getOpenid(), WechatTemplateMsg.msgType.GOODS_NOTIFICATIONS);
        	keyword1 = account.getSurplusVpoints()+"[累计剩余积分]";
        	templetMsg.initExchangeRemindSendMsg(title, keyword1, keyword2, keyword3, null, remark, url, DbContextHolder.getDBType());
        	taskExecutor.execute(templetMsg);
		}
		return "SUCCESS";
	}
	
	public String sendDoubleElevenTemplateMsg() throws Exception {
	    String formid = null;
	    List<String> accountList = thirdAccountInfoDao.findAllAppletOpenid();
	    if(CollectionUtils.isEmpty(accountList)){
	        return "推送失败：推送用户为空";
	    }
	    
	    int idx = 0;
	    for (String openid : accountList) {
	        formid = MsgAppletUtil.getFormid(openid);
	        if(StringUtils.isNotBlank(formid)){
	            idx++;
	            taskExecutor.execute(new DoubleElevenCouponRemind(openid, formid));
	        }
	    }
	    log.error("推送成功：双十一用户共" + accountList.size() + "人,含有formid的共" + idx + "人");
	    return "推送成功：双十一用户共" + accountList.size() + "人,含有formid的共" + idx + "人";
	}

	/**
	 * 好物好生活优惠券小程序模板消息
	 * @return
	 * @throws Exception
	 */
	public String sendHlsTicketTemplateMsg(String appletOpenid, String startDate, String endDate) throws Exception {
		String formid = null;
		List<String> accountList = new ArrayList<>();
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
		    Map<String, Object> map = new HashMap<>();
            map.put("startDate", startDate + " 00:00:00");
            map.put("endDate", endDate + " 23:59:59");
            accountList = thirdAccountInfoDao.queryAppletOpenidByCreateTime(map);
		}
		if (StringUtils.isNotBlank(appletOpenid)) {
		    if (accountList == null) {
		        accountList = Arrays.asList(appletOpenid.split(","));
		    } else {
		        accountList.addAll(Arrays.asList(appletOpenid.split(",")));
		    }
        }
		if(CollectionUtils.isEmpty(accountList)){
			return "推送失败：推送用户为空";
		}
		
		int idx = 0;
		for (String openid : accountList) {
			formid = MsgAppletUtil.getFormid(openid);
	    	if(StringUtils.isNotBlank(formid)){
	    		idx++;
	    		taskExecutor.execute(new HlsTicketTempletMsg(openid, formid));
	    	}
		}
		log.error("推送成功：好物好生活用户共" + accountList.size() + "人,含有formid的共" + idx + "人");
		return "推送成功：好物好生活用户共" + accountList.size() + "人,含有formid的共" + idx + "人";
	}
	
}
