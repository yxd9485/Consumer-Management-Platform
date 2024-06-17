/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 *
 * Original Author: sunshunbo
 *
 * ChangeLog:
 * 2017-8-11 by sunshunbo create
 * </pre>
 */
package com.dbt.framework.wechat.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;

/**
 * 小程序异步消息推送.
 * @author hanshimeng
 *
 */
public class AsyncMsgAppletService implements Runnable{
	
	Logger log = Logger.getLogger(this.getClass());
	
	/** The openid. */
	String openid="";
	
	/** The templateType 
	 * 模板类型：1活动信息提醒，2比赛结果通知，3任务完成通知 
	 **/
	String templateType;
	
	/** The keyword1. */
	String keyword1="";
	
	/** The keyword2. */
	String keyword2="";
	
	/** The keyword3. */
	String keyword3="";
	
	/** The keyword4. */
	String keyword4="";
	
	/** The keyword5. */
	String keyword5="";
	
	String date4;
	
	String thing5;
	
	String projectServerName;
	

	public AsyncMsgAppletService(String openid, String templateType,
			String keyword1, String keyword2, String keyword3, String keyword4, String keyword5, String projectServerName) {
		super();
		this.openid = openid;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.keyword4 = keyword4;
		this.keyword5 = keyword5;
		this.templateType = templateType;
		this.projectServerName = projectServerName;
	}
	
	public AsyncMsgAppletService(String openid, String templateType, String thing5, String date4, String projectServerName) {
		super();
		this.openid = openid;
		this.thing5 = thing5;
		this.date4 = date4;
		this.templateType = templateType;
		this.projectServerName = projectServerName;
	}



	@Override
	public void run() {
		try {
//			String msg=MsgAppletUtil.getTemplateNotifyJson(
//					openid, templateType, keyword1, keyword2, keyword3, keyword4, keyword5);
			DbContextHolder.setDBType(projectServerName);
			
			// 临时代码begin
			String tempid=DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, "msg_applet_template_id" + templateType);
			MsgAppletBean msgBean=new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
			msgBean.setTouser(openid);
			msgBean.setTemplate_id(tempid);
			String page = null;
			if(Constant.APPLET_TEMPLATE_TYPE.type_1.equals(templateType) 
					|| Constant.APPLET_TEMPLATE_TYPE.type_2.equals(templateType)
					|| Constant.APPLET_TEMPLATE_TYPE.type_4.equals(templateType)){
				page = DatadicUtil.getDataDicValue(
						DatadicKey.dataDicCategory.FILTER_HTTP_URL, DatadicKey.filterHttpUrl.WECHAT_MOVEMENT_HOME_PAGE_URL);
			}
			msgBean.setPage(page);
			
			MsgAppletData data=new MsgAppletData();
			data.setThing5(new MsgValue(thing5, null));
			data.setDate4(new MsgValue(date4, null));
			msgBean.setData(data);
			String msg = JSON.toJSONString(msgBean);
			// 临时代码end
			
			
			
			log.error("发送消息组装字符串：" + msg);
			if(StringUtils.isNotBlank(msg)){
				String appid = DatadicUtil.getDataDicValue(
						DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
						DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
				String appsec = DatadicUtil.getDataDicValue(
						DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
						DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
				WechatUtil.sendAppletTemplateMsg(msg, appid, appsec);
			}else{
				log.error("小程序推送消息失败:组装内容为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
