package com.dbt.smallticket.bean;



import com.dbt.framework.base.bean.BasicProperties;

/**
 * 小票每晚参与定时抽奖用户中奖概率表
 */
public class VpsTicketLotteryRecord extends BasicProperties {

    private static final long serialVersionUID = -5388308624996597028L;

    /** 主键*/
    private String infoKey;
    /** 用户主键*/
    private String userKey;
    /** 参与抽奖日期*/
    private String lotteryDate;
    /** 洒行渠道获取次数*/
    private int channelNum0;
    /** 餐饮渠道获取次数*/
    private int channelNum1;
    /** KA渠道获取次数*/
    private int channelNum2;
    /** 电商渠道获取次数*/
    private int channelNum3;
    /** 分享获取次数*/
    private int shareNum;
    /** 订阅号关注获取次数*/
    private int subscriptionNum;
    private String province;
	private String city;
	private String county;
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
    public String getLotteryDate() {
        return lotteryDate;
    }
    public void setLotteryDate(String lotteryDate) {
        this.lotteryDate = lotteryDate;
    }
    public int getChannelNum0() {
        return channelNum0;
    }
    public void setChannelNum0(int channelNum0) {
        this.channelNum0 = channelNum0;
    }
    public int getChannelNum1() {
        return channelNum1;
    }
    public void setChannelNum1(int channelNum1) {
        this.channelNum1 = channelNum1;
    }
    public int getChannelNum2() {
        return channelNum2;
    }
    public void setChannelNum2(int channelNum2) {
        this.channelNum2 = channelNum2;
    }
    public int getChannelNum3() {
        return channelNum3;
    }
    public void setChannelNum3(int channelNum3) {
        this.channelNum3 = channelNum3;
    }
    public int getShareNum() {
        return shareNum;
    }
    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }
    public int getSubscriptionNum() {
        return subscriptionNum;
    }
    public void setSubscriptionNum(int subscriptionNum) {
        this.subscriptionNum = subscriptionNum;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
    
}
