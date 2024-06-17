package com.dbt.framework.wechat.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.wechat.bean.SendTemplateMsg;
import com.dbt.framework.wechat.service.SendTemplateMsgService;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 微信发送消息
 * @author hanshimeng
 *
 */

@Controller
@RequestMapping("/sendWechatMsg")
public class SendWechatMsgAction extends BaseAction {

	@Autowired
	private SendTemplateMsgService sendTemplateMsgService;
	@Autowired
	private VpsConsumerUserInfoService consumerUserInfoService;
	
	/**
	 * 消息列表
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showSendTemplateMsgList")
	public String showSendTemplateMsgList(HttpSession session, 
				String queryParam, String pageParam, Model model){
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			SendTemplateMsg queryBean = new SendTemplateMsg(queryParam);
			queryBean.setCompanyKey(companyKey);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			
			List<SendTemplateMsg> resultList = sendTemplateMsgService.queryForLst(queryBean, pageInfo);
			int countResult = sendTemplateMsgService.queryForCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
			model.addAttribute("orderCol", pageInfo.getOrderCol());
	        model.addAttribute("orderType", pageInfo.getOrderType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showSendTemplateMsgList";
	}
	
	
	/**
	 * 查询有效openid数量
	 * @param session
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/queryValidCount")
	public String queryValidCount(HttpSession session, String messageType, String minVpoint, String maxVpoint, Model model) {
		// 预估推送条数
		int estimateSendCount = 0;
		// 已推送条数
		int alreadySendCount = 0;
		try {
			if(Constant.sendMessageType.messageType_1.equals(messageType)){
				estimateSendCount = consumerUserInfoService.queryValidOpenidCount(minVpoint, maxVpoint);
			}else{
				estimateSendCount = consumerUserInfoService.queryValidPaOpenidCount(minVpoint, maxVpoint);
			}
			
			// 查询已推送条数
			SendTemplateMsg queryBean = new SendTemplateMsg();
			queryBean.setStartDate(DateUtil.getDate());
			queryBean.setEndDate(DateUtil.getDate());
			queryBean.setMessageType(messageType);
			alreadySendCount = sendTemplateMsgService.queryForSendMessageCount(queryBean);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return JSONObject.toJSONString(estimateSendCount + "-" + alreadySendCount);
	}
	
	/**
	 * 添加页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showSendTemplateMsgAdd")
	public String showSendTemplateMsgAdd(HttpSession session, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			model.addAttribute("companyKey", companyKey);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showSendTemplateMsgAdd";
	}
	
	/**
	 * 新增模板消息记录
	 * @param session
	 * @param sendTemplateMsg
	 * @param model
	 * @return
	 */
	@RequestMapping("/doSendTemplateMsgAdd")
	public String doSendTemplateMsgAdd(HttpSession session, SendTemplateMsg sendTemplateMsg, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			sendTemplateMsg.setCompanyKey(companyKey);
			sendTemplateMsg.fillFields(currentUser.getUserKey());
			sendTemplateMsgService.create(sendTemplateMsg);
			model.addAttribute("errMsg", "新增模板消息成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "新增模板消息失败");
			ex.printStackTrace();
		}
		return showSendTemplateMsgList(session, null, null, model);
	}
	
	
	/**
	 * 修改页面
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showSendTemplateMsgEdit")
	public String showSendTemplateMsgEdit(HttpSession session, String infoKey, Model model) {
		try {
			SendTemplateMsg sendTemplateMsg = sendTemplateMsgService.findByid(infoKey);
			model.addAttribute("sendTemplateMsg", sendTemplateMsg);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showSendTemplateMsgEdit";
	}
	
	/**
	 * 删除记录
	 * @param session
	 * @param infoKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteById")
	public String deleteById(HttpSession session, String infoKey, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			sendTemplateMsgService.deleteById(infoKey, currentUser.getUserKey());
			model.addAttribute("errMsg","删除成功");
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("errMsg","删除失败");
		}
		return showSendTemplateMsgList(session, null, null, model);
	}
	
	/**
	 * 修改模板消息记录
	 * @param session
	 * @param sendTemplateMsg
	 * @param model
	 * @return
	 */
	@RequestMapping("/doSendTemplateMsgEdit")
	public String doSendTemplateMsgEdit(HttpSession session, SendTemplateMsg sendTemplateMsg, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			sendTemplateMsg.setCompanyKey(companyKey);
			sendTemplateMsg.fillFields(currentUser.getUserKey());
			sendTemplateMsgService.update(sendTemplateMsg, true);
			model.addAttribute("errMsg", "修改模板消息成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "修改模板消息失败");
			ex.printStackTrace();
		}
		return showSendTemplateMsgList(session, null, null, model);
	}
	
	/**
	 * 手动退推送使用：userKey == null ? 全部用户 : 指定用户 
	 * 本地：推送默认消息：http://localhost:8080/DBTFJQHPlatform/sendWechatMsg/sendMsg.do?userKey=3a7ad189-d171-40c7-858a-c707941c5369
	        线上：推送默认消息：http://59.110.54.200:9898/DBTFJQHPlatform/sendWechatMsg/sendMsg.do?userKey=4fd5bd67-2aef-4066-8d49-43b9eb75bd2c
	 * @param userKey
	 * @return
	 */
	@RequestMapping("/sendMsg")
	public @ResponseBody String sendTemplateMsg(String userKey){
		String msg = sendTemplateMsgService.sendTemplateMsg(userKey);
	    return JSON.toJSONString(msg);
	}
	
	
	/**
	 * 测试Job推送
	 * @return
	 */
	@RequestMapping("/testSendMsgJob")
	public @ResponseBody String testSendTemplateMsgJob(){
		StringBuilder sb = new StringBuilder();
		try{
			// 获取未推送消息List
			List<SendTemplateMsg> msgList = sendTemplateMsgService.queryNonExecutionMsgList();
			if(!msgList.isEmpty()){
				for (SendTemplateMsg sendTemplateMsg : msgList) {
					String msg = sendTemplateMsgService.sendTemplateMsg(sendTemplateMsg);
					sb.append("模板消息主键：" + sendTemplateMsg.getInfoKey() + ",执行结果为：" +msg);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return JSON.toJSONString(sb.toString());
	}
	
	/**
	 * 手动退推送双十一优惠券过期提醒消息 
	 * 本地：推送默认消息：http://localhost:8080/DBTHBQPplatform/sendWechatMsg/sendDoubleElevenMsg.do
	        线上：推送默认消息：http://ip:9898/平台名称/sendWechatMsg/sendDoubleElevenMsg.do
	 * @return
	 */
	@RequestMapping("/sendDoubleElevenMsg")
	public @ResponseBody String sendDoubleElevenTemplateMsg(){
	    String msg = null;
	    try{
	        msg = sendTemplateMsgService.sendDoubleElevenTemplateMsg();
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	    return JSON.toJSONString(msg);
	}
	
	/**
	 * 好物好生活优惠券小程序模板消息 
	 * 本地：推送默认消息：http://localhost:8080/DBTHBQPplatform/sendWechatMsg/sendHlsTicketTemplateMsg.do?appletOpenid=&startDate=&endDate=
	        线上：推送默认消息：http://ip:9898/平台名称/sendWechatMsg/sendHlsTicketTemplateMsg.do?appletOpenid=&startDate=&endDate=
	 * @return
	 */
	@RequestMapping("/sendHlsTicketTemplateMsg")
	public @ResponseBody String sendHlsTicketTemplateMsg(String appletOpenid, String startDate, String endDate){
		String msg = null;
		try{
			msg = sendTemplateMsgService.sendHlsTicketTemplateMsg(appletOpenid, startDate, endDate);
		}catch(Exception e){
			e.printStackTrace();
		}
	    return JSON.toJSONString(msg);
	}
	
}
