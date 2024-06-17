package com.dbt.platform.wechatmovement.bean;


import com.dbt.framework.base.bean.BaseBean;

/**
 * 用户参与微信运动详情Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsWechatMovementUserDetail extends BaseBean{
	/** 主键 **/
	private String infoKey;
	/** 用户主键 **/
	private String userKey;
	/** 期数，格式：20180605 **/
	private String periodsKey;
	/** 支付泡币 **/
	private int payVpoints;
	/** 当天步数 **/
	private int stepNumber;
	/** 是否邀请新人： 0否，1是 **/
	private String isInvitee;
	/** 是否达标： 0否，1是 **/
	private String isFinish;
	/** 领取金额 **/
	private double earnMoney;
	/** 领取时间 **/
	private String earnTime;
	/** 省 **/
	private String province;
	/** 市 **/
	private String city;
	/** 县 **/
	private String county;
	
	// 扩展字段
	/** 微信openid **/
	private String openid;
	/** 微信昵称 **/
	private String nickName;
	/** 表后缀 **/
	private String tableIndex;
	/** 总泡币 **/
	private int totalVpoints;
	/** 参与人数 **/
	private int participantsCount;
	/** 达标人数 **/
	private int finishCount;
	/** 累计达标天数 **/
	private int signDays;
	/** 达标的周赛主键KEY **/
	private String weekSignInfoKey;
	/** 当期泡币价格 **/
	private String currencyPrice;
	/** 当期虚拟活动数据倍数 **/
	private String activityMagnification;
	/** 当期达标平均金额 **/
	private String finishAvgMoney;
	
	public String getCurrencyPrice() {
		return currencyPrice;
	}
	public void setCurrencyPrice(String currencyPrice) {
		this.currencyPrice = currencyPrice;
	}
	public String getActivityMagnification() {
		return activityMagnification;
	}
	public void setActivityMagnification(String activityMagnification) {
		this.activityMagnification = activityMagnification;
	}
	public String getFinishAvgMoney() {
		return finishAvgMoney;
	}
	public void setFinishAvgMoney(String finishAvgMoney) {
		this.finishAvgMoney = finishAvgMoney;
	}
	
	public double getEarnMoney() {
		return earnMoney;
	}
	public void setEarnMoney(double earnMoney) {
		this.earnMoney = earnMoney;
	}
	public String getWeekSignInfoKey() {
		return weekSignInfoKey;
	}
	public void setWeekSignInfoKey(String weekSignInfoKey) {
		this.weekSignInfoKey = weekSignInfoKey;
	}
	public int getSignDays() {
		return signDays;
	}
	public void setSignDays(int signDays) {
		this.signDays = signDays;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getPayVpoints() {
		return payVpoints;
	}
	public void setPayVpoints(int payVpoints) {
		this.payVpoints = payVpoints;
	}
	public String getIsInvitee() {
		return isInvitee;
	}
	public void setIsInvitee(String isInvitee) {
		this.isInvitee = isInvitee;
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
	public String getTableIndex() {
		return tableIndex;
	}
	public void setTableIndex(String tableIndex) {
		this.tableIndex = tableIndex;
	}
	public VpsWechatMovementUserDetail(){}
	public VpsWechatMovementUserDetail(String userKey, String periodsKey) {
		this.userKey = userKey;
		this.periodsKey = periodsKey;
	}
	
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
	public String getPeriodsKey() {
		return periodsKey;
	}
	public void setPeriodsKey(String periodsKey) {
		this.periodsKey = periodsKey;
	}
	public int getStepNumber() {
		return stepNumber;
	}
	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	public String getEarnTime() {
		return earnTime;
	}
	public void setEarnTime(String earnTime) {
		this.earnTime = earnTime;
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
