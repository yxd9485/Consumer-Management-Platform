package com.dbt.platform.bidding.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 竞价活动预约Bean
 */
@SuppressWarnings("serial")
public class BiddingActivitySubscribeInfo extends BasicProperties {
    
    /** 主键*/
    private String infoKey;
    /** 活动主键*/
    private String activityKey;
    /** 用户主键*/
    private String userKey;
    /** 是否酒水专场：0否、1是*/
    private String isDedicated;
    /** 活动开始提醒标志：1已预约，2已发送*/
    private String activityStartFlag;
    /** 活动即将结束提醒标志：1已预约，2已发送*/
    private String activityEndFlag;
    /** 中奖结果提醒标志：1已预约，2已发送*/
    private String winningResultFlag;
    
    // 扩展字段
    /** 省区小程序OPENID **/
    private String paOpenid;
    /** 通用小程序OPENID **/
    private String appletOpenid;
    /** 活动名称 **/
    private String activityName;
    /** 擂台类型：1日擂台，2月擂台 **/
    private String activityType;
    /** 开场人数 **/
    private int openingNumber;
    /** 商品名称 **/
    private String goodsName;
    
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getAppletOpenid() {
        return appletOpenid;
    }
	public String getIsDedicated() {
		return isDedicated;
	}
	public void setIsDedicated(String isDedicated) {
		this.isDedicated = isDedicated;
	}
	public String getActivityStartFlag() {
		return activityStartFlag;
	}
	public void setActivityStartFlag(String activityStartFlag) {
		this.activityStartFlag = activityStartFlag;
	}
	public String getActivityEndFlag() {
		return activityEndFlag;
	}
	public void setActivityEndFlag(String activityEndFlag) {
		this.activityEndFlag = activityEndFlag;
	}
	public String getWinningResultFlag() {
		return winningResultFlag;
	}
	public void setWinningResultFlag(String winningResultFlag) {
		this.winningResultFlag = winningResultFlag;
	}
	public void setAppletOpenid(String appletOpenid) {
		this.appletOpenid = appletOpenid;
	}
	public String getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	public String getPaOpenid() {
		return paOpenid;
	}
	public void setPaOpenid(String paOpenid) {
		this.paOpenid = paOpenid;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getOpeningNumber() {
		return openingNumber;
	}
	public void setOpeningNumber(int openingNumber) {
		this.openingNumber = openingNumber;
	}
}
