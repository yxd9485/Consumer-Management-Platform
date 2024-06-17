package com.dbt.platform.wctaccesstoken.bean;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.WechatUtil;

/** 预警类模板消息*/
public class WechatRemindTemplateMsg implements Runnable {
    protected Log log = LogFactory.getLog(this.getClass());
    
    private String appid;
    /** 必传：接收者openid*/
    private String touser;
    /** 必传：模板ID*/
//  private String template_id = "7084yNzo4PgMj-XXvhIbxe1eHO_5jjsDdcLkqchXSUM";
  private String template_id = "-3uGtn5pxCz-YIFY5AxZPaViwa2yD2ZMUKNvVG4q2Jc";
    /** 必传：模板数据*/
    private Map<String, Map<String, String>> data;
    /** 模板跳转链接（海外帐号没有跳转能力）*/
    private String url;
    /** 跳小程序所需数据，不需跳小程序可不用传该数据*/
    private Map<String, String> miniprogram;
    
    /** 预警消息类型*/
    public enum msgType {
        /** 奖项占比 */ PRIZE_PERCENT, 
        /** 活动结束*/ ACTIVITY_END
    }
    
    /**
     * 模板跳转链接（海外帐号没有跳转能力）
     * @param openId
     * @param templateMsgId
     */
    public WechatRemindTemplateMsg(String appid, String openId) {
        this.appid = appid;
        this.touser = openId;
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
    public void initApp(String appId, String pagePath) {
        miniprogram = new HashMap<>();
        miniprogram.put("appid", appId);
        miniprogram.put("pagepath", pagePath);
    }
    
    /**
     * 通用预警模板
     * 
     * @param title 省区名称
     * @param goodsName 预警内容
     */
    @SuppressWarnings("serial")
    public WechatRemindTemplateMsg initRemindMsg(final String remindType, final String projectName, final String remindContent) {
        data = new HashMap<>();
//      data.put("first", new HashMap<String, String>(){{put("value", projectName);}});
      data.put("keyword1", new HashMap<String, String>(){{put("value", StringUtils.defaultIfBlank(projectName, "消费者促销管理平台"));}});
      data.put("keyword2", new HashMap<String, String>(){{put("value", DateUtil.getDateTime());}});
      data.put("keyword3", new HashMap<String, String>(){{put("value", "重要");}});
      data.put("keyword4", new HashMap<String, String>(){{put("value", remindType);}});
      data.put("keyword5", new HashMap<String, String>(){{put("value", remindContent);}});
//      data.put("remark", new HashMap<String, String>(){{put("value", remindContent);put("color", "#FF0000");}});
      this.setUrl("https://w.vjifen.com/v/reportTemplate/redirect.html?dataRmk=" + URLEncoder.encode(remindContent));
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
    
}
