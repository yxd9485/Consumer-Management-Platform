package com.dbt.platform.doubtuser.bean;

import com.dbt.framework.base.bean.BaseBean;

/**
 * @author hanshimeng 
 * @createTime 2016年4月21日 下午5:44:15
 * @description 扫码记录统计
 */
public class VCodeScanRecordInfo extends BaseBean{

	private static final long serialVersionUID = -5504706385335297857L;
	
	private String infokey;
	private String userkey;
	/** 活动key**/
	private String vcodeactivitykey;
	/** 上一次扫码时间**/
	private String lastscantime;
	/** 同一分钟扫码次数**/
	private String lastdurationcounts;
	/** 一天内扫码次数**/
	private String currentscancounts;
   /** 一月内扫码次数**/
	private String monthscancounts;
	/** 历史累计扫码次数**/
	private String totalscancounts;

	public String getMonthscancounts () {
		return monthscancounts;
	}
	
	public void setMonthscancounts (String monthscancounts) {
		this.monthscancounts = monthscancounts;
	}

	public String getInfokey() {
		return infokey;
	}
	public void setInfokey(String infokey) {
		this.infokey = infokey;
	}
	public String getUserkey() {
		return userkey;
	}
	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}
	public String getVcodeactivitykey() {
		return vcodeactivitykey;
	}
	public void setVcodeactivitykey(String vcodeactivitykey) {
		this.vcodeactivitykey = vcodeactivitykey;
	}
	public String getLastscantime() {
		return lastscantime;
	}
	public void setLastscantime(String lastscantime) {
		this.lastscantime = lastscantime;
	}
	public String getLastdurationcounts() {
		return lastdurationcounts;
	}
	public void setLastdurationcounts(String lastdurationcounts) {
		this.lastdurationcounts = lastdurationcounts;
	}
	public String getCurrentscancounts() {
		return currentscancounts;
	}
	public void setCurrentscancounts(String currentscancounts) {
		this.currentscancounts = currentscancounts;
	}
	public String getTotalscancounts() {
		return totalscancounts;
	}
	public void setTotalscancounts(String totalscancounts) {
		this.totalscancounts = totalscancounts;
	}
	
}