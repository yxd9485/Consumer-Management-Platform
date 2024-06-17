package com.dbt.platform.ticket.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *优惠券码库表Bean
 */
public class VpsVcodeTicketLib extends BasicProperties {

    private static final long serialVersionUID = -2230445368717519481L;

    /** 主键*/
    private String infoKey;
    /** 优惠券活动KEY */
    private String activityKey;
    /** 优惠券类型KEY*/
    private String ticketKey;
    /** 优惠券券码*/
    private String ticketCode;
    /** 扫码用户*/
    private String userKey;
    /** 扫码时间*/
    private String earnTime;
    /** 使用状态：0:未使用、1:已使用*/
    private String useStatus;
    
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getTicketCode() {
        return ticketCode;
    }
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getEarnTime() {
        return earnTime;
    }
    public void setEarnTime(String earnTime) {
        this.earnTime = earnTime;
    }
    public String getUseStatus() {
        return useStatus;
    }
    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }
	public String getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	public String getTicketKey() {
		return ticketKey;
	}
	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}
}
