package com.dbt.platform.drinkcapacity.wechatmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.MsgAppletUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.platform.drinkcapacity.bean.VpsVcodeDrinkCapacityPkRecord;

public class DrinkCapacityPkResultMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String currUserKey;
    private VpsVcodeDrinkCapacityPkRecord pkRecord;
    private double selfFreezeMoney;
    private double adverseFreezeMoney;
    
    public DrinkCapacityPkResultMsg(String currUserKey, VpsVcodeDrinkCapacityPkRecord pkRecord, double selfFreezeMoney, double adverseFreezeMoney) {
        this.currUserKey = currUserKey;
        this.pkRecord = pkRecord;
        this.selfFreezeMoney = selfFreezeMoney;
        this.adverseFreezeMoney = adverseFreezeMoney;
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
            String sendText = this.initMsg(appid, currUserKey, pkRecord, selfFreezeMoney, adverseFreezeMoney);
            boolean sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
            log.error("酒量1V1PK结果模板消息发送" + (sendFlag ? "成功" : "失败") +"，userKey:" + currUserKey 
                                + " infoKey:" + pkRecord.getInfoKey() + " pkBeginTime:" + pkRecord.getBeginTime());
        } catch (Exception e) {
            log.error("酒量1V1PK结果模板消息发送失败，userKey:" + currUserKey 
                    + " infoKey:" + pkRecord.getInfoKey() + " pkBeginTime:" + pkRecord.getBeginTime());
        }

    }

    private String initMsg(String appletId, String currUserKey, VpsVcodeDrinkCapacityPkRecord
                                pkRecord, double selfFreezeMoney, double adverseFreezeMoney) throws Exception {
        String openid = currUserKey.equals(pkRecord.getUserKeyA()) ? pkRecord.getAppletPkOpenidA() : pkRecord.getAppletPkOpenidB();
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(openid);
        msg.setTemplate_id(DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                DatadicKey.filterWxPayTemplateInfo.APPLET_TMPMSG_DRINKCAPACITYPKRESULT));
        msg.setForm_id(MsgAppletUtil.getFormid(openid));
        msg.setPage("pages/pk/index");

        MsgAppletData data=new MsgAppletData();
        if (Constant.PkStatus.status_2.equals(pkRecord.getPkStatus())) {
            data.setKeyword1(new MsgValue("好友酒量PK——平", "#FF0000"));
            data.setKeyword3(new MsgValue("在好友酒量PK中，您与好友平局，再次向好友发起挑战", null));
            
        } else if (Constant.PkStatus.status_3.equals(pkRecord.getPkStatus())) {
            data.setKeyword1(new MsgValue("好友酒量PK——流局", "#FF0000"));
            if (selfFreezeMoney == 0 && adverseFreezeMoney == 0) {
                data.setKeyword3(new MsgValue("在好友酒量PK中，由于比赛期间双方的扫码账户余额为0元，本次PK流局", null));
            } else if (selfFreezeMoney == 0) {
                data.setKeyword3(new MsgValue("在好友酒量PK中，由于比赛期间您的扫码账户余额为0元，本次PK流局", null));
            } else if (adverseFreezeMoney == 0) {
                data.setKeyword3(new MsgValue("在好友酒量PK中，由于比赛期间对方扫码账户余额为0元，本次PK流局", null));
            }
            
        } else if (Constant.PkStatus.status_1.equals(pkRecord.getPkStatus())) {
            if ((currUserKey.equals(pkRecord.getUserKeyA()) && pkRecord.getScanNumA() > pkRecord.getScanNumB())
                    || (currUserKey.equals(pkRecord.getUserKeyB()) && pkRecord.getScanNumB() > pkRecord.getScanNumA())) {
                data.setKeyword1(new MsgValue("好友酒量PK——胜", "#FF0000"));
                data.setKeyword3(new MsgValue("在好友酒量PK中，您获得胜利，并赢取对方" + pkRecord.getWinMoney() + "元", null));
            } else {
                data.setKeyword1(new MsgValue("好友酒量PK——负", "#FF0000"));
                data.setKeyword3(new MsgValue("在好友酒量PK中，您惜败对手，对方赢取您" + pkRecord.getWinMoney() + "元", null));
            }
        }
        
        // 昵称
        data.setKeyword2(new MsgValue(currUserKey.equals(pkRecord.getUserKeyA()) ? pkRecord.getNickNameA() : pkRecord.getNickNameB(), null));
        // 比赛开始时间
        data.setKeyword4(new MsgValue(pkRecord.getBeginTime(), null));
        // 比赛结束时间
        data.setKeyword5(new MsgValue(pkRecord.getEndTime(), null));
        msg.setData(data);
        msg.setColor("#173177");
        // 标题字体放大
//      msg.setEmphasis_keyword("keyword1.DATA");
        return JSON.toJSONString(msg);
    }
}
