package com.dbt.platform.wechatmovement.bean;


import com.dbt.framework.base.bean.BaseBean;
/**
 * 期数统计Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsWechatMovementPeriods extends BaseBean{

	/** 期数主键 **/
	private String periodsKey;
	/** 总泡币 **/
	private int totalVpoints;
	/** 参与人数 **/
	private int participantsCount;
	/** 达标人数 **/
	private int finishCount;
	/** 邀请人数 **/
	private int inviteeCount;
	/** 当期泡币价格 **/
	private String currencyPrice;
	/** 当期虚拟活动数据倍数 **/
	private String activityMagnification;
	/** 当期达标平均金额 **/
	private double finishAvgMoney;
	
	public int getInviteeCount() {
		return inviteeCount;
	}
	public void setInviteeCount(int inviteeCount) {
		this.inviteeCount = inviteeCount;
	}
	public String getCurrencyPrice() {
		return currencyPrice;
	}
	public void setCurrencyPrice(String currencyPrice) {
		this.currencyPrice = currencyPrice;
	}
	public double getFinishAvgMoney() {
		return finishAvgMoney;
	}
	public void setFinishAvgMoney(double finishAvgMoney) {
		this.finishAvgMoney = finishAvgMoney;
	}
	public String getActivityMagnification() {
		return activityMagnification;
	}
	public void setActivityMagnification(String activityMagnification) {
		this.activityMagnification = activityMagnification;
	}
	public String getPeriodsKey() {
		return periodsKey;
	}
	public void setPeriodsKey(String periodsKey) {
		this.periodsKey = periodsKey;
	}
	public int getTotalVpoints() {
		return totalVpoints;
	}
	public void setTotalVpoints(int totalVpoints) {
		this.totalVpoints = totalVpoints;
	}
	public int getParticipantsCount() {
		return participantsCount;
	}
	public void setParticipantsCount(int participantsCount) {
		this.participantsCount = participantsCount;
	}
	public int getFinishCount() {
		return finishCount;
	}
	public void setFinishCount(int finishCount) {
		this.finishCount = finishCount;
	}
	
}
