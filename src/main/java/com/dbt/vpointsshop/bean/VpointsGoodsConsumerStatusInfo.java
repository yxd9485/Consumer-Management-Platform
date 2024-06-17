package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 商品到货通知Bean
 */
public class VpointsGoodsConsumerStatusInfo extends BasicProperties {

    private static final long serialVersionUID = 1L;
    
    /** 主键*/
    private String infoKey;
    /** 商品主键*/
    private String goodsId;
    /** 用户主键*/
    private String userKey;
    /** 是否收藏本产品：0否、1是*/
    private String collectFlag;
    /** 是否接收到货通知：0不接收、1接收*/
    private String arrivalNoticeFlag;
    /** 秒杀预约提醒标志：0-未预约、1-已预约、2-已发送*/
    private String secKillRemindFlag;
    private String appletOpenid;
    private String paOpenid;
    /** 会员小程序openid */
    private String memberOpenid;
    //-------------扩展字段-------------------
    private String goodsShortName;
    private String goodsStartTime;
    private int realPay;
    private int goodsNum;

    public String getMemberOpenid() {
        return memberOpenid;
    }

    public void setMemberOpenid(String memberOpenid) {
        this.memberOpenid = memberOpenid;
    }

    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getCollectFlag() {
        return collectFlag;
    }
    public void setCollectFlag(String collectFlag) {
        this.collectFlag = collectFlag;
    }
    public String getArrivalNoticeFlag() {
        return arrivalNoticeFlag;
    }
    public void setArrivalNoticeFlag(String arrivalNoticeFlag) {
        this.arrivalNoticeFlag = arrivalNoticeFlag;
    }
    public String getAppletOpenid() {
        return appletOpenid;
    }
    public void setAppletOpenid(String appletOpenid) {
        this.appletOpenid = appletOpenid;
    }
    public String getPaOpenid() {
        return paOpenid;
    }
    public void setPaOpenid(String paOpenid) {
        this.paOpenid = paOpenid;
    }
    public String getGoodsShortName() {
        return goodsShortName;
    }
    public void setGoodsShortName(String goodsShortName) {
        this.goodsShortName = goodsShortName;
    }
    public String getGoodsStartTime() {
        return goodsStartTime;
    }
    public void setGoodsStartTime(String goodsStartTime) {
        this.goodsStartTime = goodsStartTime;
    }
    public int getRealPay() {
        return realPay;
    }
    public void setRealPay(int realPay) {
        this.realPay = realPay;
    }
    public int getGoodsNum() {
        return goodsNum;
    }
    public void setGoodsNum(int goodsNum) {
        this.goodsNum = goodsNum;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getSecKillRemindFlag() {
        return secKillRemindFlag;
    }
    public void setSecKillRemindFlag(String secKillRemindFlag) {
        this.secKillRemindFlag = secKillRemindFlag;
    }
}
