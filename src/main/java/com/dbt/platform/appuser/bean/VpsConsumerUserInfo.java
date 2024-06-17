package com.dbt.platform.appuser.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BaseBean;

/**
 * 文件名: VpsConsumerUserInfo.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: 消费者用户信息bean<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-21 11:06:57<br>
 * 修改内容：新增<br>
 */
public class VpsConsumerUserInfo extends BaseBean {

	/** **/
	private static final long serialVersionUID = 1L;
	/** */
	private String openid;
	/** */
	private String userKey;
	/** */
	private String nickName;
	/** 真实姓名*/
	private String mnRealName;
	/** 身份证号*/
	private String idCard;
	/** 手机号. */
	private String phoneNumber;
	/** 微信头像url **/
	private String headImgUrl;
	/** */
	private String gender;
	/** */
	private String province;
	/** */
	private String city;
	/** */
	private String county;
	/** 用户类型：1：正式用户；2：游客 */
	private String userType;
	/** 用户状态 0正常，1可疑，2黑名单 */
	private String userStatus;
	/** 周签到时间 **/
	private String weekSignDate;
	/** 真正第一次關注的時間 */
	private String registerTime;
	/** 是否幸运用户， 0 否；1 是 **/
	private String isLuckyUser;
	/** */
	private String deleteFlag;
	/** */
	private String createTime;
	/** */
	private String updateTime;

	//用户校色
	private String userRole;
	
	public VpsConsumerUserInfo() {
	    super();
	}
	
	public VpsConsumerUserInfo(String openid, String nickName){
    	this.openid = openid;
    	this.nickName = nickName;
    }
	
	public VpsConsumerUserInfo(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.userKey = paramAry.length > 0 ? paramAry[0] : "";
        this.nickName = paramAry.length > 1 ? paramAry[1] : "";
        this.phoneNumber = paramAry.length > 2 ? paramAry[2] : "";
        this.isLuckyUser = paramAry.length > 3 ? paramAry[3] : "";
	}

	public String getWeekSignDate() {
		return weekSignDate;
	}

	public void setWeekSignDate(String weekSignDate) {
		this.weekSignDate = weekSignDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getIsLuckyUser() {
		return isLuckyUser;
	}

	public void setIsLuckyUser(String isLuckyUser) {
		this.isLuckyUser = isLuckyUser;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMnRealName() {
		return mnRealName;
	}

	public void setMnRealName(String mnRealName) {
		this.mnRealName = mnRealName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
}
