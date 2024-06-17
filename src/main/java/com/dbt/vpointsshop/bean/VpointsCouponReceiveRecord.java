package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;

public class VpointsCouponReceiveRecord extends BasicProperties {

    private static final long serialVersionUID = 1L;
    /** */
    private String infoKey;
    /** 用户主键*/
    private String userKey;
    /** 优惠券主键*/
    private String couponKey;
    /** 状态：0未使用、1已使用*/
    private String receiveStatus;
    /** 领取时间*/
    private String receiveTime;
    /** 使用时间*/
    private String useTime;
    /** 过期时间*/
    private String expireTime;
    /** 领券时定位*/
    private String province;
    private String city;
    private String county;
    /** 使用的兑换记录主键*/
    private String exchangeId;
    /** 实际优惠金额*/
    private double discountMoney;
    /** 实际优惠积分*/
    private int discountVpoints;
    
    private String address;
    private String longitude;
    private String latitude;
    private String nickName;
    private String phoneNumber;
    private String paOpenid;
    private String couponName;
    /** 会员小程序openid */
    private String memberOpenid;

    public String getMemberOpenid() {
        return memberOpenid;
    }

    public void setMemberOpenid(String memberOpenid) {
        this.memberOpenid = memberOpenid;
    }

    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getCouponKey() {
        return couponKey;
    }
    public void setCouponKey(String couponKey) {
        this.couponKey = couponKey;
    }
    public String getReceiveStatus() {
        return receiveStatus;
    }
    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }
    public String getReceiveTime() {
        return receiveTime;
    }
    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }
    public String getUseTime() {
        return useTime;
    }
    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
    public String getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
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
    public String getExchangeId() {
        return exchangeId;
    }
    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }
    public double getDiscountMoney() {
        return discountMoney;
    }
    public void setDiscountMoney(double discountMoney) {
        this.discountMoney = discountMoney;
    }
    public int getDiscountVpoints() {
        return discountVpoints;
    }
    public void setDiscountVpoints(int discountVpoints) {
        this.discountVpoints = discountVpoints;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    public String getPaOpenid() {
        return paOpenid;
    }
    public void setPaOpenid(String paOpenid) {
        this.paOpenid = paOpenid;
    }
    public String getCouponName() {
        return couponName;
    }
    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
