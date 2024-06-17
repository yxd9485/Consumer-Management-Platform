package com.dbt.platform.system.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu 
 * @createTime 2016年4月20日 上午10:13:54
 * @description 
 */

@SuppressWarnings("serial")
public class SysUserBasis extends BasicProperties {

	private String userKey;
	/** 项目标识 **/
	private String projectServerName;
	/** 品牌标识*/
	private String brandCode;
	/** 用户昵称 */
	private String nickName;
	/** 用户账号 */
	private String userName;
	/** 密码 */
	private String userPassword;
	/** 状态 */
	private String userStatus;
	/** 用户类型 */
	private String userType;
	/** 所属组织 */
	private String companyKey;
    /** 验证手机号 */
    private String phoneNum;
    /** 手机验证码 */
    private String veriCode;
    /** 角色KEY **/
    private String roleKey;
    /** 验证码秘钥KEY **/
    private String secretKey;
    /** ui素材用户类型 QP 青啤**/
    private String picBrandType;

    private String unionid;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getPicBrandType() {
		return picBrandType;
	}

	public void setPicBrandType(String picBrandType) {
		this.picBrandType = picBrandType;
	}

	public String getProjectServerName() {
		return projectServerName;
	}

	public void setProjectServerName(String projectServerName) {
		this.projectServerName = projectServerName;
	}

	public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getVeriCode() {
        return veriCode;
    }

    public void setVeriCode(String veriCode) {
        this.veriCode = veriCode;
    }

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
