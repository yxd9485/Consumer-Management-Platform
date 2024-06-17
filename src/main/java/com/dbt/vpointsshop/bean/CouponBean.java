package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;
/**
 * 电子券bean
 * @author zhaohongtao
 *2017年12月11日
 */
public class CouponBean extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String couponKey;
	private String batchKey;
	private String couponVcode;
	private String userKey;
	private String couponStatus;
	public String getCouponKey() {
		return couponKey;
	}
	public void setCouponKey(String couponKey) {
		this.couponKey = couponKey;
	}
	public String getBatchKey() {
		return batchKey;
	}
	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}
	public String getCouponVcode() {
		return couponVcode;
	}
	public void setCouponVcode(String couponVcode) {
		this.couponVcode = couponVcode;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(String couponStatus) {
		this.couponStatus = couponStatus;
	}
}
