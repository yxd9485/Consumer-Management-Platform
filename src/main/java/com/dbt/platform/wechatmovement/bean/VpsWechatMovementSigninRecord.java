package com.dbt.platform.wechatmovement.bean;


import com.dbt.framework.base.bean.BaseBean;
/**
 * 周赛记录
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsWechatMovementSigninRecord extends BaseBean{

	/** 主键 **/
	private String infoKey;
	/** 用户主键 **/
	private String userKey;
	/** 第几周，格式：201805 **/
	private String signWeek;
	/** 达标天数集合 **/
	private String signDates;
	/** 达标周几集合 **/
	private String signWeekDays;
	/** 累计达标天数 **/
	private int signDays;
	/** 领取金额 **/
	private double earnMoney;
	/** 领取时间 **/
	private String earnTime;
	
	/**
	 * 扩充字段
	 * @return
	 */
	private String openid;
	private String nickName;
	
	public VpsWechatMovementSigninRecord(){}
	public VpsWechatMovementSigninRecord(String userKey, String signWeek, 
			String signDates, String signWeekDays, int signDays, double earnMoney, String earnTime){
		this.userKey = userKey;
		this.signWeek = signWeek;
		this.signDays = signDays;
		this.signDates = signDates;
		this.signWeekDays = signWeekDays;
		this.earnMoney = earnMoney;
		this.earnTime = earnTime;
	}
	
	public double getEarnMoney() {
		return earnMoney;
	}
	public void setEarnMoney(double earnMoney) {
		this.earnMoney = earnMoney;
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
	public String getSignWeek() {
		return signWeek;
	}
	public void setSignWeek(String signWeek) {
		this.signWeek = signWeek;
	}
	public String getSignDates() {
		return signDates;
	}
	public void setSignDates(String signDates) {
		this.signDates = signDates;
	}
	public String getSignWeekDays() {
		return signWeekDays;
	}
	public void setSignWeekDays(String signWeekDays) {
		this.signWeekDays = signWeekDays;
	}
	public int getSignDays() {
		return signDays;
	}
	public void setSignDays(int signDays) {
		this.signDays = signDays;
	}
	public String getEarnTime() {
		return earnTime;
	}
	public void setEarnTime(String earnTime) {
		this.earnTime = earnTime;
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
}
