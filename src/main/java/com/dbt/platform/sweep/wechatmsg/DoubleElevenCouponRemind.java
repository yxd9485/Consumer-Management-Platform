package com.dbt.platform.sweep.wechatmsg;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PropertiesUtil;
import org.apache.commons.lang.math.RandomUtils;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;

public class DoubleElevenCouponRemind implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String openid;
    private String formid;
    
    public DoubleElevenCouponRemind(String openid, String formid) {
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
                log.error("双十一优惠券过期提醒消息发送" + (sendFlag ? "成功" : "失败") +"，openid:" + openid);
            }
        } catch (Exception e) {
            log.error("双十一优惠券过期提醒消息发送失败，openid:" + openid); 
        }

    }

    private String initMsg(String appletId, String openid, String formid) throws Exception {
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(openid);
        msg.setTemplate_id(PropertiesUtil.getPropertyValue("applet_tmpmsg_doubleelevenRemind"));
        msg.setForm_id(formid);
        msg.setPage("pages/index/frommsg");
        
        String[] msgGroup = {"亲，你参与的“青啤双11活动”获得的权益快要到期了，赶紧点击查看呦~~","哈喽，今天是全球狂欢的购物节，记得使用你在“青啤双11活动”中获得的权益，不要忘记啊"};

        MsgAppletData data=new MsgAppletData();
        if("2019-11-10".equals(DateUtil.getDate())){
        	data.setKeyword1(new MsgValue(msgGroup[0], null));
        }else if("2019-11-11".equals(DateUtil.getDate())){
        	data.setKeyword1(new MsgValue(msgGroup[1], null));
        }else if("2019-11-07".equals(DateUtil.getDate()) 
        		|| "2019-11-08".equals(DateUtil.getDate())){
        	data.setKeyword1(new MsgValue(msgGroup[RandomUtils.nextInt(2)], null));
        }
        data.setKeyword2(new MsgValue("2019-11-11 23:59:59", null));
        msg.setData(data);
        return JSON.toJSONString(msg);
    }
}
