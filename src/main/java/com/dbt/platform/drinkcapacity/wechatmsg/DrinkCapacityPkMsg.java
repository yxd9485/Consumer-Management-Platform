package com.dbt.platform.drinkcapacity.wechatmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.MsgAppletUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.platform.drinkcapacity.bean.VpsVcodeDrinkCapacityPkRecord;

public class DrinkCapacityPkMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String currUserKey;
    private VpsVcodeDrinkCapacityPkRecord pkRecord;
    
    public DrinkCapacityPkMsg(String currUserKey, VpsVcodeDrinkCapacityPkRecord pkRecord) {
        this.currUserKey = currUserKey;
        this.pkRecord = pkRecord;
    }

    @Override
    public void run() {
        try {
        	DbContextHolder.setDBType("shandongagt");
            String appid = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.APPLET_DRINKCAPACITYPK_APPID);
            String appsec = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.APPLET_DRINKCAPACITYPK_SECRET);
            String sendText = this.initMsg(appid, currUserKey, pkRecord);
            boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
            log.error("酒量1V1PK开始20小时后提醒模板消息发送" + (sendFlag ? "成功" : "失败") +"，userKey:" 
                        + currUserKey + " infoKey:" + pkRecord.getInfoKey() + " pkBeginTime:" + pkRecord.getBeginTime());
        } catch (Exception e) {
            log.error("酒量1V1PK开始20小时后提醒模板消息发送失败，userKey:" + currUserKey 
                    + " infoKey:" + pkRecord.getInfoKey() + " pkBeginTime:" + pkRecord.getBeginTime());
        }

    }

    private String initMsg(String appletId, String currUserKey, VpsVcodeDrinkCapacityPkRecord pkRecord) throws Exception {
        String openid = currUserKey.equals(pkRecord.getUserKeyA()) ? pkRecord.getAppletPkOpenidA() : pkRecord.getAppletPkOpenidB();
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(openid);
        msg.setTemplate_id(DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                DatadicKey.filterWxPayTemplateInfo.APPLET_TMPMSG_DRINKCAPACITYPK));
        msg.setForm_id(MsgAppletUtil.getFormid(openid));
        msg.setPage("pages/pk/index");

        MsgAppletData data=new MsgAppletData();
        // 昵称
        data.setKeyword1(new MsgValue(currUserKey.equals(pkRecord.getUserKeyA()) ? pkRecord.getNickNameA() : pkRecord.getNickNameB(), null));
        data.setKeyword2(new MsgValue("你与好友的比赛已经进行了20个小时，赶紧点击，查看你们的比赛情况吧", null));
        // 比赛开始时间
        data.setKeyword3(new MsgValue(pkRecord.getBeginTime().substring(0, 19), null));
        // 比赛结束时间
        data.setKeyword4(new MsgValue(pkRecord.getEndTime().substring(0, 19), null));
        msg.setData(data);
        msg.setColor("#173177");
        // 标题字体放大
//      msg.setEmphasis_keyword("keyword1.DATA");
        return JSON.toJSONString(msg);
    }
}
