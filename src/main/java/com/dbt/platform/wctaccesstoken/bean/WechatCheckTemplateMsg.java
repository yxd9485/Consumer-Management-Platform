package com.dbt.platform.wctaccesstoken.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.WechatUtil;

/** 数据同步模板消息*/
public class WechatCheckTemplateMsg implements Runnable {
    protected Log log = LogFactory.getLog(this.getClass());
    
    private String appid;
    /** 必传：接收者openid*/
    private String touser;
    /** 必传：模板ID*/
    private String template_id;
    /** 必传：模板数据*/
    private Map<String, Map<String, String>> data;
    /** 模板跳转链接（海外帐号没有跳转能力）*/
    private String url;
    /** 跳小程序所需数据，不需跳小程序可不用传该数据
     * 	appid	小程序appid
     * 	pagepath小程序跳转路径，可以带参数
     * */
    private Map<String, String> miniprogram;
    private String paAppletAppid;
    
    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     * @param openId
     * @param templateMsgId
     */
    public WechatCheckTemplateMsg(String appid, String openId, String url, String template_id) {
        this.appid = appid;
        this.touser = openId;
        this.url = url;
        this.template_id = template_id;
    }

    /** 发送模板消息 */
    @Override
    public void run() {
        WechatUtil.sendTemplateMsg(appid, JSON.toJSONString(this));
    }
    
    /**
     * 初始化小程序跳转相差参数
     * 
     * @param appId     所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系，暂不支持小游戏）
     * @param pagePath  所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar），暂不支持小游戏
     */
    public WechatCheckTemplateMsg initApp(String paAppletAppid, String pagePath) {
        miniprogram = new HashMap<>();
        miniprogram.put("appid", paAppletAppid);
        miniprogram.put("pagepath", pagePath);
        return this;
    }
    
    /**
     * 	同步模板
     * 
     */
    @SuppressWarnings("serial")
    public WechatCheckTemplateMsg initSyncMsg(final String checkStatus) {
    	String checkMsg = Constant.COMMON_CHECK_STATUS.CHECK_STATUS_1.equals(checkStatus) ? "审核通过" : "报名被驳回";
    	String remark = Constant.COMMON_CHECK_STATUS.CHECK_STATUS_1.equals(checkStatus) ? "进入小程序查看详情" : "进入小程序重新报名";
    	
        data = new HashMap<>();
        data.put("first", new HashMap<String, String>(){{put("value", "长城好酒推荐官报名");put("color", "#FF0000");}});
        data.put("keyword1", new HashMap<String, String>(){{put("value", checkMsg);}});
        data.put("keyword2", new HashMap<String, String>(){{put("value", DateUtil.getDateTime());}});
        data.put("remark", new HashMap<String, String>(){{put("value", remark);}});
        return this;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
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

	public String getPaAppletAppid() {
		return paAppletAppid;
	}

	public void setPaAppletAppid(String paAppletAppid) {
		this.paAppletAppid = paAppletAppid;
	}
    
}
