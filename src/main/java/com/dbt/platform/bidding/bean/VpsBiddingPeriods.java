package com.dbt.platform.bidding.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  竞价活动期数Bean
 */
@SuppressWarnings("serial")
public class VpsBiddingPeriods extends BasicProperties {
    
    /** 主键*/
    private String periodsKey;
    /** 活动主键*/
    private String activityKey;
    /** 期数*/
    private String periodsNumber;
    /** 预约人数 */
    private int subscribeNumber;
    /** 参与人数 */
    private int participateNumber;
    /** 用户主键*/
    private String userKey;
    /** 订单主键*/
    private String exchangeId;
    /** 状态：默认0进行中，1已结束*/
    private String status;
    
    public VpsBiddingPeriods() {
        super();
    }
	public String getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	public String getPeriodsNumber() {
		return periodsNumber;
	}
	public void setPeriodsNumber(String periodsNumber) {
		this.periodsNumber = periodsNumber;
	}
	public int getSubscribeNumber() {
		return subscribeNumber;
	}
	public void setSubscribeNumber(int subscribeNumber) {
		this.subscribeNumber = subscribeNumber;
	}
	public int getParticipateNumber() {
		return participateNumber;
	}
	public void setParticipateNumber(int participateNumber) {
		this.participateNumber = participateNumber;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getPeriodsKey() {
		return periodsKey;
	}
	public void setPeriodsKey(String periodsKey) {
		this.periodsKey = periodsKey;
	}
}
