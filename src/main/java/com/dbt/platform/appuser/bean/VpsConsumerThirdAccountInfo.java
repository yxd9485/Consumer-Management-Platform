package com.dbt.platform.appuser.bean;

import com.dbt.framework.base.bean.BaseBean;

/**
 * 文件名: VpsConsumerThirdAccountInfo.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: 消费者三方账户信息bean<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-21 10:34:50<br>
 * 修改内容：新增<br>
 */
public class VpsConsumerThirdAccountInfo extends BaseBean {

	/** **/
	private static final long serialVersionUID = 1L;
	/** 主鍵 */
	private String thirdaccountKey;
	/** userKey */
	private String userKey;
	/** openid */
	private String openid;
	/** unionid */
	private String unionid;
	/** 关注状态：1：关注；2：取消关注 */
	private String subscribeStatus;
	/** 关注时间 */
	private String subscribeTime;

	/** 昵称 **/
	private String nickname;
	/** 性别0：女；1：男；2：未知 **/
	private String gender;
	/** 微信头像url **/
	private String headimgurl;

	/** 省 **/
	private String province;
	/** 市 **/
	private String city;
	/** 县 **/
	private String county;
	/** 通用小程序openid **/
    private String appletOpenid;
    private String paOpenid;
    /** 会员小程序openid */
    private String memberOpenid;

    public String getMemberOpenid() {
        return memberOpenid;
    }

    public void setMemberOpenid(String memberOpenid) {
        this.memberOpenid = memberOpenid;
    }

    public String getAppletOpenid() {
		return appletOpenid;
	}

	public void setAppletOpenid(String appletOpenid) {
		this.appletOpenid = appletOpenid;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public VpsConsumerThirdAccountInfo() {
		super();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

	public void setThirdaccountKey(String thirdaccountKey) {
		this.thirdaccountKey = thirdaccountKey;
	}

	public String getThirdaccountKey() {
		return thirdaccountKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setSubscribeStatus(String subscribeStatus) {
		this.subscribeStatus = subscribeStatus;
	}

	public String getSubscribeStatus() {
		return subscribeStatus;
	}

	public void setSubscribeTime(String subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getSubscribeTime() {
		return subscribeTime;
	}

    public String getPaOpenid() {
        return paOpenid;
    }

    public void setPaOpenid(String paOpenid) {
        this.paOpenid = paOpenid;
    }
}
