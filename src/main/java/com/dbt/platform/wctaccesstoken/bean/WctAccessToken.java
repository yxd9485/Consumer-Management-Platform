package com.dbt.platform.wctaccesstoken.bean;

import com.dbt.framework.base.bean.BaseBean;

/**
 * 文件名: WctAccessToken.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 微信公众号唯一票据信息<br>
 * 修改人: guchangpeng<br>
 * 修改时间：2014-07-03 17:57:34<br>
 * 修改内容：新增<br>
 */
public class WctAccessToken extends BaseBean {

	/** **/
	private static final long serialVersionUID = 3740006270308132124L;
	/** 主键 */
	private String accesskey;
	/** TOKEN */
	private String accesstoken;
	/** 过期时间 */
	private String expiretime;

	/**
	 * 微信公众平台返回：token
	 */
	private String access_token;
	/**
	 * 微信公众平台返回 :过期时间
	 */
	private String expires_in;
	/** 微信appid **/
	private String appid;
	/** 微信sec **/
	private String appsec;
	
	private transient int version;

	
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getAppsec() {
		return appsec;
	}

	public void setAppsec(String appsec) {
		this.appsec = appsec;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expiresIn) {
		expires_in = expiresIn;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesstoken(String accesstoken) {
		this.accesstoken = accesstoken;
	}

	public String getAccesstoken() {
		return accesstoken;
	}

	public void setExpiretime(String expiretime) {
		this.expiretime = expiretime;
	}

	public String getExpiretime() {
		return expiretime;
	}
}
