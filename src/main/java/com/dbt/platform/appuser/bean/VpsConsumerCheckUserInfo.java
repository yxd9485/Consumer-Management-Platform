package com.dbt.platform.appuser.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

@SuppressWarnings("serial")
public class VpsConsumerCheckUserInfo extends BasicProperties{
	private String infoKey;
    private String openid;
    private String userName;
    private String phoneNum;
    private String province;
    private String city;
    private String county;
    private String address;
    /** 用户状态：0 停用，1 启用 **/
    private String userStatus;
    private String terminalName;
    private String terminalAddress;
    private String userAddress;
    public VpsConsumerCheckUserInfo(){}
	public VpsConsumerCheckUserInfo(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.openid = paramAry.length > 0 ? paramAry[0] : "";
        this.userName = paramAry.length > 1 ? paramAry[1] : "";
        this.phoneNum = paramAry.length > 2 ? paramAry[2] : "";
        this.terminalName = paramAry.length > 3 ? paramAry[3] : "";
        this.userStatus = paramAry.length > 4 ? paramAry[4] : "";
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getTerminalAddress() {
		return terminalAddress;
	}
	public void setTerminalAddress(String terminalAddress) {
		this.terminalAddress = terminalAddress;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	
}
