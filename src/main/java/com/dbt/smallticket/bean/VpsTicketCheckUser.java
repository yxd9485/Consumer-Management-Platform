package com.dbt.smallticket.bean;



import com.dbt.framework.base.bean.BasicProperties;
/**
 * 省区审核人员
 */
public class VpsTicketCheckUser extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String checkUserKey;
	private String openid;
	private String province;
	private String userName;
	private String phoneNum;
	private String userStatus;
	public String getCheckUserKey() {
		return checkUserKey;
	}
	public void setCheckUserKey(String checkUserKey) {
		this.checkUserKey = checkUserKey;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
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
	
	
}
