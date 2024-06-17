package com.dbt.platform.wechatmovement.bean;


import com.dbt.framework.base.bean.BaseBean;

/**
 * 用户参与微信运动PK赛Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsWechatMovementPkApply extends BaseBean{
	/** 主键 **/
	private String infoKey;
	/** 用户主键 **/
	private String userKey;
	/** 期数，格式：20180605 **/
	private String periodsKey;
	/** 周几 **/
	private int weekDay;
	/** 支付积分 **/
	private int payVpoints;
	/** 当天步数 **/
	private int stepNumber;
	/** PK状态： 1胜利，2失败，3平局 **/
	private String pkStatus;
	/** 领取积分 **/
	private int earnVpoints;
	/** 领取时间 **/
	private String earnTime;
	
	// 扩展字段
	/** 表后缀 **/
	private String tableIndex;
        
	public VpsWechatMovementPkApply(){}
	public VpsWechatMovementPkApply(String userKey, String periodsKey, int weekDay, int payVpoints) {
		this.userKey = userKey;
		this.weekDay = weekDay;
		this.periodsKey = periodsKey;
		this.payVpoints = payVpoints;
	}

	public VpsWechatMovementPkApply(String userKey, String pkStatus, int earnVpoints) {
		this.userKey = userKey;
		this.pkStatus = pkStatus;
		this.earnVpoints = earnVpoints;
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

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public int getPayVpoints() {
		return payVpoints;
	}

	public void setPayVpoints(int payVpoints) {
		this.payVpoints = payVpoints;
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	public String getPkStatus() {
		return pkStatus;
	}
	
	public void setPkStatus(String pkStatus) {
		this.pkStatus = pkStatus;
	}
	public int getEarnVpoints() {
		return earnVpoints;
	}

	public void setEarnVpoints(int earnVpoints) {
		this.earnVpoints = earnVpoints;
	}

	public String getEarnTime() {
		return earnTime;
	}

	public void setEarnTime(String earnTime) {
		this.earnTime = earnTime;
	}

	public String getTableIndex() {
		return tableIndex;
	}

	public void setTableIndex(String tableIndex) {
		this.tableIndex = tableIndex;
	}
}
