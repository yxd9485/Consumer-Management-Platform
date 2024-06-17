package com.dbt.platform.wechatmovement.bean;


import com.dbt.framework.base.bean.BaseBean;

/**
 * 用户参与微信运动PK赛关联Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsWechatMovementPkRelation extends BaseBean{
	/** 主键 **/
	private String infoKey;
	/** 期数，格式：20180605 **/
	private String periodsKey;
	/** 周几 **/
	private int weekDay;
	/** 用户主键 **/
	private String userKey;
	/** PK用户主键 **/
	private String pkUserKey;
	
	// 扩充字段
	/** PK状态： 1胜利，2失败，3平局 **/
	private String pkStatus;
	/** 支付积分 **/
	private int payVpoints;
        
	public VpsWechatMovementPkRelation(){}
	public VpsWechatMovementPkRelation(String userKey, String pkUserKey, String createTime) {
		this.userKey = userKey;
		this.pkUserKey = pkUserKey;
		this.setCreateTime(createTime);
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
	public String getPkUserKey() {
		return pkUserKey;
	}
	public void setPkUserKey(String pkUserKey) {
		this.pkUserKey = pkUserKey;
	}
	public String getPkStatus() {
		return pkStatus;
	}
	public void setPkStatus(String pkStatus) {
		this.pkStatus = pkStatus;
	}
	public int getPayVpoints() {
		return payVpoints;
	}
	public void setPayVpoints(int payVpoints) {
		this.payVpoints = payVpoints;
	}
	
}
