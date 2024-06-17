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
package com.dbt.framework.util;

import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.platform.wctaccesstoken.bean.WechatTemplateMsg;

/**
 * <pre>
 * The Class MsgUtil.
 * </pre>
 *
 * @author hanshimeng
 */
public class MsgAppletUtil implements Runnable{
	static Logger log = Logger.getLogger(MsgAppletUtil.class);
	private String projectServerName;
	
    private String openid;
    private String formid;
    private String appid;
    private String appsec;
    private String templateId;
    private String page;
    
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String keyword4;
    private String keyword5;
    
    public MsgAppletUtil(){}
    
    public MsgAppletUtil(String openid, String formid, String appid, String appsec, String page, 
    		String keyword1, String keyword2, String keyword3, String keyword4, String keyword5, 
    		WechatTemplateMsg.msgType msgType, String projectServerName) {
        this.openid = openid;
        this.formid = formid;
        this.appid = appid;
        this.appsec = appsec;
        this.page = page;
        this.keyword1 = keyword1;
        this.keyword2 = keyword2;
        this.keyword3 = keyword3;
        this.keyword4 = keyword4;
        this.keyword5 = keyword5;
        this.projectServerName = projectServerName;
        initTemplate(msgType);
    }
	
	/** 消息类型*/
    public enum msgType {
        /** 兑换成功 */ EXCHANGE, 
        /** 发货成功*/ EXPRESS_SEND, 
        /** 服务通知*/ SERVICE_NOTIFICATIONS,
        /** 单品推送*/ GOODS_NOTIFICATIONS,
        /** 特殊推送*/ SPECIAL_NOTIFICATIONS
    }
    
    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     * @param openId
     * @param templateMsgId
     */
    public void initTemplate(WechatTemplateMsg.msgType msgType) {
        switch (msgType) {
        case SERVICE_NOTIFICATIONS:
            this.templateId = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_SERVICENOTIFICATIONS);
            break;
        case GOODS_NOTIFICATIONS:
        	this.templateId = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_GOODSNOTIFICATIONS);
            break;
        case SPECIAL_NOTIFICATIONS:
        	this.templateId = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_SPECIALNOTIFICATIONS);
            break;
        default:
            break;
        }
    }
    
    @Override
    public void run() {
        try {
        	DbContextHolder.setDBType(projectServerName);
            String sendText = msgToJson(openid, templateId, page, formid, keyword1, keyword2, keyword3, keyword4, keyword5);
            if(StringUtils.isNotBlank(sendText)){
            	boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
                log.error("商城消息发送" + (sendFlag ? "成功" : "失败") +"，openid:" + openid);
            }
        } catch (Exception e) {
            log.error("商城消息发送失败，openid:" + openid); 
        }

    }
    
	
	/**
	 * 小程序包装模板消息基础方法.
	 * @param openid
	 * @param tempid
	 * @param page
	 * @param formid
	 * @param keyword1
	 * @param keyword2
	 * @param keyword3
	 * @param keyword4
	 * @param keyword5
	 * @return
	 */
	public static String msgToJson(String openid, String tempid, String page, String formid,
			String keyword1, String keyword2, String keyword3, String keyword4, String keyword5){
		MsgAppletBean msg=new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
		msg.setTouser(openid);
		msg.setTemplate_id(tempid);
		msg.setForm_id(formid);
		if(!StringUtils.isEmpty(page)){
			msg.setPage(page);
		}
		
		MsgAppletData data=new MsgAppletData();
		data.setKeyword1(new MsgValue(keyword1, null));
		data.setKeyword2(new MsgValue(keyword2, null));
		data.setKeyword3(new MsgValue(keyword3, null));
		data.setKeyword4(new MsgValue(keyword4, null));
		data.setKeyword5(new MsgValue(keyword5, null));
		msg.setData(data);
		msg.setColor("#173177");
		// 标题字体放大
//		msg.setEmphasis_keyword("keyword1.DATA");
		return JSON.toJSONString(msg);
	}
	
	
	/**
	 * 小程序包装服务状态提醒模板.
	 * @param openid	用户openid
	 * @param templateType	模板类型：1活动信息提醒，2比赛结果通知，3任务完成通知
	 * @param keyword1	小标题
	 * @param keyword2	大标题
	 * @param keyword3	日期
	 * @param keyword4	内容1
	 * @param keyword5 	内容2
	 * @return
	 * @throws Exception 
	 */
	public static String  getTemplateNotifyJson(String openid, String templateType, 
			String keyword1, String keyword2, String keyword3, String keyword4, String keyword5) throws Exception{
		//同一个模板在不同公众号对应不同的模板id
		String tempid=null; //PropertiesUtil.getPropertyValue("msg_applet_template_id" + templateType);
		String formid = getFormid(openid);
		String page = null;
		if(Constant.APPLET_TEMPLATE_TYPE.type_1.equals(templateType) 
				|| Constant.APPLET_TEMPLATE_TYPE.type_2.equals(templateType)
				|| Constant.APPLET_TEMPLATE_TYPE.type_4.equals(templateType)){
			page = null; //PropertiesUtil.getPropertyValue("wechat_movement_home_page_url");
		}
		if(StringUtils.isNotBlank(formid)){
			return msgToJson(openid, tempid, page, formid, keyword1, keyword2, keyword3, keyword4, keyword5);
		}else{
			log.error("小程序推送消息失败：获取用户formid为空！");
			return null;
		}
	}
	
	/**
	 * 用户用户formid
	 * @param openid 小程序openid
	 * @return
	 * @throws Exception
	 */
	public static String getFormid(String openid) throws Exception {
		String formid = null;
		String formidKey = CacheKey.cacheKey.USER_INFO.KEY_USER_FORMID_INFO + Constant.DBTSPLIT + openid;
		String formidStr = RedisApiUtil.getInstance().rpop(formidKey);
		if(StringUtils.isNotBlank(formidStr)){
			if(formidStr.indexOf("@") > -1){
				String dateTime = formidStr.split("@")[1];
				if(DateUtil.diffSecond(DateUtil.parse(dateTime, DateUtil.DEFAULT_DATETIME_FORMAT_SHT), DateUtil.getNow()) > 0){
					formid = formidStr.split("@")[0];
				}else{
					getFormid(openid);
				}
			}else{
				getFormid(openid);
			}
		}
		return formid;
	}
}
