package com.dbt.platform.drinkcapacity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 酒量1V1记录表
 */
public class VpsVcodeDrinkCapacityPkRecord extends BasicProperties {

    private static final long serialVersionUID = 1L;
    
    private String infoKey;
    /** 发起者用户主键*/
    private String userKeyA;
    /** 接受者用户主键*/
    private String userKeyB;
    /** 比赛开始时间*/
    private String beginTime;
    /** 比赛结束时间*/
    private String endTime;
    /** 扫码瓶数*/
    private int scanNumA;
    /** 扫码瓶数*/
    private int scanNumB;
    /** 点赞数*/
    private int thumbUpNumA;
    /** 未转化成扫码次数的新增点赞数*/
    private int newThumbUpNumA;
    /** 点赞数*/
    private int thumbUpNumB;
    /** 未转化成扫码次数的新增点赞数*/
    private int newThumbUpNumB;
    /** 查看次数*/
    private int viewNum;
    /** 查看人数*/
    private int viewPersonNum;
    /** 前10名查看用户的微信头像*/
    private String viewHeadImgUrl;
    /** 比赛状态：0进行中、1分出胜负、2平局、3流局*/
    private String pkStatus;
    /** 获胜金额*/
    private String winMoney;
    private double winMoneyA;
    private double winMoneyB;
    /** 自己标志：A、B*/
    private String selfFlag;
    private String nickName;
    private String nickNameA;
    private String nickNameB;
    private String headImgUrl;
    private String headImgUrlA;
    private String headImgUrlB;
    /** 是否显示点赞转换提醒:1显示、0:不显示*/
    private String thumbUpTipsFlag;
    /** 点赞转换成扫码瓶数的模*/
    private int thumbUpDivisor;
    private String appletPkOpenidA;
    private String appletPkOpenidB;
    /** 流局原因*/
    private String unpkReason;
    
    public String getUnpkReason() {
        return unpkReason;
    }
    public void setUnpkReason(String unpkReason) {
        this.unpkReason = unpkReason;
    }
    public String getAppletPkOpenidA() {
        return appletPkOpenidA;
    }
    public void setAppletPkOpenidA(String appletPkOpenidA) {
        this.appletPkOpenidA = appletPkOpenidA;
    }
    public String getAppletPkOpenidB() {
        return appletPkOpenidB;
    }
    public void setAppletPkOpenidB(String appletPkOpenidB) {
        this.appletPkOpenidB = appletPkOpenidB;
    }
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getUserKeyA() {
        return userKeyA;
    }
    public void setUserKeyA(String userKeyA) {
        this.userKeyA = userKeyA;
    }
    public String getUserKeyB() {
        return userKeyB;
    }
    public void setUserKeyB(String userKeyB) {
        this.userKeyB = userKeyB;
    }
    public String getBeginTime() {
        return beginTime;
    }
    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getScanNumA() {
        return scanNumA;
    }
    public void setScanNumA(int scanNumA) {
        this.scanNumA = scanNumA;
    }
    public int getScanNumB() {
        return scanNumB;
    }
    public void setScanNumB(int scanNumB) {
        this.scanNumB = scanNumB;
    }
    public int getThumbUpNumA() {
        return thumbUpNumA;
    }
    public void setThumbUpNumA(int thumbUpNumA) {
        this.thumbUpNumA = thumbUpNumA;
    }
    public int getThumbUpNumB() {
        return thumbUpNumB;
    }
    public void setThumbUpNumB(int thumbUpNumB) {
        this.thumbUpNumB = thumbUpNumB;
    }
    public int getViewNum() {
        return viewNum;
    }
    public void setViewNum(int viewNum) {
        this.viewNum = viewNum;
    }
    public int getViewPersonNum() {
        return viewPersonNum;
    }
    public void setViewPersonNum(int viewPersonNum) {
        this.viewPersonNum = viewPersonNum;
    }
    public String getViewHeadImgUrl() {
        return viewHeadImgUrl;
    }
    public void setViewHeadImgUrl(String viewHeadImgUrl) {
        this.viewHeadImgUrl = viewHeadImgUrl;
    }
    public String getPkStatus() {
        return pkStatus;
    }
    public void setPkStatus(String pkStatus) {
        this.pkStatus = pkStatus;
    }
    public String getWinMoney() {
        return winMoney;
    }
    public void setWinMoney(String winMoney) {
        this.winMoney = winMoney;
    }
    public double getWinMoneyA() {
        return winMoneyA;
    }
    public void setWinMoneyA(double winMoneyA) {
        this.winMoneyA = winMoneyA;
    }
    public double getWinMoneyB() {
        return winMoneyB;
    }
    public void setWinMoneyB(double winMoneyB) {
        this.winMoneyB = winMoneyB;
    }
    public String getNickNameA() {
        return nickNameA;
    }
    public void setNickNameA(String nickNameA) {
        this.nickNameA = nickNameA;
    }
    public String getNickNameB() {
        return nickNameB;
    }
    public void setNickNameB(String nickNameB) {
        this.nickNameB = nickNameB;
    }
    public String getHeadImgUrlA() {
        return headImgUrlA;
    }
    public void setHeadImgUrlA(String headImgUrlA) {
        this.headImgUrlA = headImgUrlA;
    }
    public String getHeadImgUrlB() {
        return headImgUrlB;
    }
    public void setHeadImgUrlB(String headImgUrlB) {
        this.headImgUrlB = headImgUrlB;
    }
    public String getHeadImgUrl() {
        return headImgUrl;
    }
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
    public String getSelfFlag() {
        return selfFlag;
    }
    public void setSelfFlag(String selfFlag) {
        this.selfFlag = selfFlag;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getNewThumbUpNumA() {
        return newThumbUpNumA;
    }
    public void setNewThumbUpNumA(int newThumbUpNumA) {
        this.newThumbUpNumA = newThumbUpNumA;
    }
    public int getNewThumbUpNumB() {
        return newThumbUpNumB;
    }
    public void setNewThumbUpNumB(int newThumbUpNumB) {
        this.newThumbUpNumB = newThumbUpNumB;
    }
    public String getThumbUpTipsFlag() {
        return thumbUpTipsFlag;
    }
    public void setThumbUpTipsFlag(String thumbUpTipsFlag) {
        this.thumbUpTipsFlag = thumbUpTipsFlag;
    }
    public int getThumbUpDivisor() {
        return thumbUpDivisor;
    }
    public void setThumbUpDivisor(int thumbUpDivisor) {
        this.thumbUpDivisor = thumbUpDivisor;
    }
    
}
