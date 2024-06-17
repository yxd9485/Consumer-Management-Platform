package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

public class VpointsRechargeLog extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String exchangeId;
	private String companyKey;
	private String ztRechargeId;
	private String phoneNum;
	private String userKey;
	private String rechargeMoney;
	private String exchangeVpoints;
	private String exchangeTime;
	private String exchangeStatus;
	
	private String exchangeStartTime;
	private String exchangeEndTime;
	private String companyName;
	private String nickName;
	
	public VpointsRechargeLog(){}
	public VpointsRechargeLog(String queryParam){
		if (!StringUtils.isEmpty(queryParam)) {
			queryParam = trickParam(queryParam);
			String[] params = queryParam.split(",");
			this.exchangeId = params.length > 0 ? !StringUtils.isEmpty(params[0]) ? params[0]: null : null;
			this.ztRechargeId = params.length > 1 ? !StringUtils.isEmpty(params[1]) ? params[1]: null : null;
			this.phoneNum = params.length > 2 ? !StringUtils.isEmpty(params[2]) ? params[2]: null : null;
			this.userKey = params.length > 3 ? !StringUtils.isEmpty(params[3]) ? params[3]: null : null;
			this.exchangeStartTime = params.length > 4 ? !StringUtils.isEmpty(params[4]) ? params[4]: null : null;
			this.exchangeEndTime = params.length > 5 ? !StringUtils.isEmpty(params[5]) ? params[5]+" 23:59:59": null : null;
			this.companyKey = params.length > 6 ? !StringUtils.isEmpty(params[6]) ? params[6]: null : null;
		}
	}
	
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getZtRechargeId() {
		return ztRechargeId;
	}
	public void setZtRechargeId(String ztRechargeId) {
		this.ztRechargeId = ztRechargeId;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getRechargeMoney() {
		return rechargeMoney;
	}
	public void setRechargeMoney(String rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}
	
	public String getExchangeVpoints() {
		return exchangeVpoints;
	}
	public void setExchangeVpoints(String exchangeVpoints) {
		this.exchangeVpoints = exchangeVpoints;
	}
	public String getExchangeTime() {
		return exchangeTime;
	}
	public void setExchangeTime(String exchangeTime) {
		this.exchangeTime = exchangeTime;
	}
	public String getExchangeStatus() {
		return exchangeStatus;
	}
	public void setExchangeStatus(String exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}
	public String getExchangeStartTime() {
		return exchangeStartTime;
	}
	public void setExchangeStartTime(String exchangeStartTime) {
		this.exchangeStartTime = exchangeStartTime;
	}
	public String getExchangeEndTime() {
		return exchangeEndTime;
	}
	public void setExchangeEndTime(String exchangeEndTime) {
		this.exchangeEndTime = exchangeEndTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
}
