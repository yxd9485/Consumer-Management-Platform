package com.dbt.platform.clientcenter.bean;

import org.springframework.util.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2015年5月5日 下午5:24:31
 * @description 用户信息
 */

@SuppressWarnings("serial")
public class VpsClientUserInfo extends BasicProperties {

	/** 用户主键 */
	private String userKey;
	/** 昵称 */
	private String nickName;
	/** 账号 */
	private String userAccount;
	/** 密码 */
	private String password;
	/** 性别 */
	private String gender;
	/** 生日 */
	private String birthday;
	/** 年龄区段 */
	private String ageRange;
	/** 省 */
	private String province;
	/** 市 */
	private String city;
	/** 区/县 */
	private String county;
	/** 详细地址 */
	private String address;
	/** 用户来源 */
	private String userSource;
	/** 用户类型 */
	private String userType;
	/** loginToken */
	private String loginToken;
	/** 头像图片 */
	private String filePath;
	/** 邮箱 */
	private String email;
	/** 注册时间 */
	private String registerTime;
	/** 邀请码 */
	private Integer inviteCode;
	/** 是否联合会员 */
	private String ifMember;
	/** 完整昵称 */
	private String nickNameFull;
	/** 真实姓名 */
	private String realName;
	/** 身份证号 */
	private String idCard;
	
	public VpsClientUserInfo(){}
	
	public VpsClientUserInfo(String params) {
		if(!StringUtils.isEmpty(params)){
			String[] values = params.split(",");
			this.nickName = values.length > 0 ? values[0] : null;
			this.userAccount = values.length > 1 ? values[1] : null;
			this.province = values.length > 2 ? values[2] : null;
			this.city = values.length > 3 ? values[3] : null;
			this.county = values.length > 4 ? values[4] : null;
			this.gender = values.length > 5 ? values[5] : null;
			this.userSource = values.length > 6 ? values[6] : null;
			this.nickNameFull = values.length > 7 ? values[7] : null;
			this.registerTime = values.length > 8 ? values[8] : null;
		}
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

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
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

	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginToken() {
		return loginToken;
	}

	public void setLoginToken(String loginToken) {
		this.loginToken = loginToken;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public Integer getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(Integer inviteCode) {
		this.inviteCode = inviteCode;
	}
	
	public String getIfMember() {
		return ifMember;
	}

	public void setIfMember(String ifMember) {
		this.ifMember = ifMember;
	}

	public String getNickNameFull() {
		return nickNameFull;
	}

	public void setNickNameFull(String nickNameFull) {
		this.nickNameFull = nickNameFull;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
}
