package com.dbt.platform.wctaccesstoken.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;

public class WechatTemplateMsg implements Runnable {
    protected Log log = LogFactory.getLog(this.getClass());
    private String projectServerName;
    
    /** 必传：接收者openid*/
    private String touser;
    /** 必传：模板ID*/
    private String template_id;
    /** 必传：模板数据*/
    private Map<String, Map<String, String>> data;
    /** 模板跳转链接（海外帐号没有跳转能力）*/
    private String url;
    /** 跳小程序所需数据，不需跳小程序可不用传该数据*/
    private Map<String, String> miniprogram;
    
    
    
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
    public WechatTemplateMsg(String openId, WechatTemplateMsg.msgType msgType) {
        this.touser = openId;
        switch (msgType) {
        case EXCHANGE:
//            this.template_id = PropertiesUtil.getPropertyValue("wechat_tmpMsg_exchange");
            this.template_id = "fZ8z21Bl-acX17CRXU_x5cGPumhOTybCuYm6wl3LQ24";
            break;
        case EXPRESS_SEND:
//            this.template_id = PropertiesUtil.getPropertyValue("wechat_tmpMsg_expressSend");
            this.template_id = "qjThHqQUzPmzxifJenfoix7KYx4kA18gUSuXJeDmZpE";
            break;
        case SERVICE_NOTIFICATIONS:
//            this.template_id = PropertiesUtil.getPropertyValue("wechat_tmpmsg_serviceNotifications");
            this.template_id = "YE4392prYkqACH2huPihxXCKfVnDKQBnKGPEvNEZoQA";
            break;
        case GOODS_NOTIFICATIONS:
//            this.template_id = PropertiesUtil.getPropertyValue("wechat_tmpmsg_goodsNotifications");
            this.template_id = "mZ2rxppAFqiDg2tF4ITFyoxRp5bUw9dHPC8FPg1qJ58";
            break;
        case SPECIAL_NOTIFICATIONS:
//            this.template_id = PropertiesUtil.getPropertyValue("wechat_tmpmsg_specialNotifications");
            this.template_id = "UOiiKiItQxAqmbxDXSoll_8eRAKK8m6c0vlpFGvxyko";
            break;
            
        default:
            break;
        }
    }

    /** 发送模板消息 */
    @Override
    public void run() {
    	DbContextHolder.setDBType(projectServerName);
        WechatUtil.sendTemplateMsg(JSON.toJSONString(this));
    }
    
    /**
     * 初始化小程序跳转相差参数
     * 
     * @param appId     所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     * @param pagePath  所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），暂不支持小游戏
     */
    public void initApp(String appId, String pagePath) {
        miniprogram = new HashMap<>();
        miniprogram.put("appid", appId);
        miniprogram.put("pagepath", pagePath);
    }
    
    /**
     * 初始化积分商场兑换成功模板消息
     * 
     * @param title 标题
     * @param goodsName 商品名称
     * @param exchangeVpoints 兑换积分
     * @param exchangeTime  兑换时间
     * @param url           跳转url
     */
    @SuppressWarnings("serial")
    public void initExchangeMsg(final String title, final String goodsName,
                        final String exchangeVpoints, final String exchangeTime, final String url, String projectServerName) {
        this.url = url;
        data = new HashMap<>();
        data.put("first", new HashMap<String, String>(){{put("value", title);}});
        data.put("productType", new HashMap<String, String>(){{put("value", "商品名细");}});
        data.put("name", new HashMap<String, String>(){{put("value", goodsName);}});
        data.put("points", new HashMap<String, String>(){{put("value", exchangeVpoints);}});
        data.put("date", new HashMap<String, String>(){{put("value", exchangeTime);}});
        this.projectServerName = projectServerName;
    }
    
    /**
     * 初始化积分商场发货成功模板消息
     * 
     * @param title 标题
     * @param exchangeId    兑换记录主键
     * @param goodsName     商品名称
     * @param expressCompany物流公司
     * @param expressNumber 物流单号
     * @param address       收件人信息
     * @param url           跳转url
     */
    @SuppressWarnings("serial")
    public void initExpressSendMsg(final String title, final String exchangeId, 
            final String goodsName, final String expressCompany, final String expressNumber, final String address, final String url, String projectServerName) {
        this.url = url;
        data = new HashMap<>();
        data.put("first", new HashMap<String, String>(){{put("value", title);}});
        data.put("keyword1", new HashMap<String, String>(){{put("value", exchangeId);}});
        data.put("keyword2", new HashMap<String, String>(){{put("value", goodsName);}});
        data.put("keyword3", new HashMap<String, String>(){{put("value", expressCompany);}});
        data.put("keyword4", new HashMap<String, String>(){{put("value", expressNumber);}});
        data.put("keyword5", new HashMap<String, String>(){{put("value", address);}});
        this.projectServerName = projectServerName;
    }
    
    /**
     * 初始化商品兑换提醒模板消息
     * 
     * @param title
     * @param keyword1     
     * @param keyword2 		
     * @param keyword3 
     * @param remark	
     */
    @SuppressWarnings("serial")
    public void initExchangeRemindSendMsg(final String first, final String keyword1, 
    		final String keyword2, final String keyword3, final String keyword4, final String remark, final String url, String projectServerName) {
    	this.url = url;
        data = new HashMap<>();
        data.put("first", new HashMap<String, String>(){{put("value", first);}});
        data.put("keyword1", new HashMap<String, String>(){{put("value", keyword1);}});
        data.put("keyword2", new HashMap<String, String>(){{put("value", keyword2);}});
        data.put("keyword3", new HashMap<String, String>(){{put("value", keyword3);}});
        data.put("keyword4", new HashMap<String, String>(){{put("value", keyword4);}});
        data.put("remark", new HashMap<String, String>(){{put("value", remark);put("color", "#2C90FF");}});
        this.projectServerName = projectServerName;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(Map<String, String> miniprogram) {
        this.miniprogram = miniprogram;
    }
    
}
