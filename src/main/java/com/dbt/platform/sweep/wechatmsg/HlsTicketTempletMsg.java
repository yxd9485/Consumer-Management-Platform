package com.dbt.platform.sweep.wechatmsg;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;

/**
 * 好物好生活优惠券小程序模板消息
 */
public class HlsTicketTempletMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String openid;
    private String formid;
    
    public HlsTicketTempletMsg(String openid, String formid) {
        this.openid = openid;
        this.formid = formid;
    }

    @Override
    public void run() {
        try {
            String appid = PropertiesUtil.getPropertyValue("applet_doubleeleven_appid");
            String appsec = PropertiesUtil.getPropertyValue("applet_doubleeleven_secret");
            String sendText = this.initMsg(appid, openid, formid);
            if(StringUtils.isNotBlank(sendText)){
            	boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
                log.error("好物好生活优惠券小程序模板消息发送" + (sendFlag ? "成功" : "失败") +"，openid:" + openid);
            }
        } catch (Exception e) {
            log.error("好物好生活优惠券小程序模板消息发送失败，openid:" + openid); 
        }

    }

    private String initMsg(String appletId, String openid, String formid) throws Exception {
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(openid);
        msg.setTemplate_id("cB46zqz-4XGwh4jDN7GjbtNnjauUoCj3sgYjAptfb8A");
        msg.setForm_id(formid);
        msg.setPage("pages/push/hw");

        MsgAppletData data=new MsgAppletData();
        data.setKeyword1(new MsgValue("恭喜您中奖了!", null));
        data.setKeyword2(new MsgValue("【青岛啤酒】20元全场通用券", null));
        data.setKeyword3(new MsgValue("万分之一的中奖率，你就是锦鲤本锂\n20元不算啥，但毕竟是送的\n会过日子的人买个酒都有回头钱\n每人限领一张，7天内有效\n\n立即点击领取吧！", null));
        msg.setData(data);
        return JSON.toJSONString(msg);
    }
}
