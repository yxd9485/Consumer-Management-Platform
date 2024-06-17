package com.dbt.platform.appuser.bean;

import com.dbt.framework.base.bean.BaseBean;

import org.apache.commons.lang.StringUtils;

/**
 * 文件名: VpsConsumerAccountInfo.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.
 * <br>
 * 描述: 消费者账户信息bean<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-21 11:10:31<br>
 * 修改内容：新增<br>
 */
public class VpsConsumerAccountInfo extends BaseBean {

	/** **/
	private static final long serialVersionUID = 1L;
	/** */
	private String accountKey;
	/** */
	private String userKey;
	/** 总积分 */
	private Long accountVpoints;
	/** 剩余积分 */
	private Long surplusVpoints;
	/** 总金额 */
	private Double accountMoney;
	/** 剩余金额 */
	private Double surplusMoney;
	/** 冻结金额 */
	private double freezeMoney;
	/** */
	private String createTime;
	/** */
	private String updateTime;
	/** 昵称 */
	private String nikeName;
	/** 手机号 */
	private String phoneNum;
	/** 兑换积分 */
	private String exchangePoints;
	/** 扫码次数 */
	private String totalScan;
	/** 兑换次数 */
	private String totalExchangeNum;
	/***********************************/

	/***/
	private String earnVpoints;
	/** 码 */
	private String qrcodeContent;
	/** 省 */
	private String province;
	/** 市 */
	private String city;
	/** 县 */
	private String county;
	/** 首次领取时间 */
	private String earnTime;
	/** sku名称 */
	private String skuName;
	/** */
	private String index;

	private String address;

	private String longitude;
	private String latitude;
	/** 是否参加活动 */
	private String inActivities;
	private String openid;
	
	public VpsConsumerAccountInfo(String userKey, long surplusVpoints) {
		this.userKey = userKey;
		this.surplusVpoints = surplusVpoints;
	}

	public String getInActivities() {
		return inActivities;
	}

	public void setInActivities(String inActivities) {
		this.inActivities = inActivities;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getEarnVpoints() {
		return earnVpoints;
	}

	public void setEarnVpoints(String earnVpoints) {
		this.earnVpoints = earnVpoints;
	}

	public String getQrcodeContent() {
		return qrcodeContent;
	}

	public void setQrcodeContent(String qrcodeContent) {
		this.qrcodeContent = qrcodeContent;
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

	public String getEarnTime() {
		return earnTime;
	}

	public void setEarnTime(String earnTime) {
		this.earnTime = earnTime;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getTotalScan() {
		return totalScan;
	}

	public void setTotalScan(String totalScan) {
		this.totalScan = totalScan;
	}

	public String getTotalExchangeNum() {
		return totalExchangeNum;
	}

	public void setTotalExchangeNum(String totalExchangeNum) {
		this.totalExchangeNum = totalExchangeNum;
	}

	public String getExchangePoints() {
		return exchangePoints;
	}

	public void setExchangePoints(String exchangePoints) {
		this.exchangePoints = exchangePoints;
	}

	public String getNikeName() {
		return nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public VpsConsumerAccountInfo() {
		super();
	}

	public VpsConsumerAccountInfo(String pageParam) {
		super();
		if (!StringUtils.isEmpty(pageParam)) {
			String[] values = pageParam.split(",");

			this.userKey = values.length > 0 && StringUtils.isNotEmpty(values[0]) ? values[0].trim() : "";
			this.nikeName = values.length > 1 && StringUtils.isNotEmpty(values[1]) ? values[1].trim() : "";
			this.phoneNum = values.length > 2 && StringUtils.isNotEmpty(values[2]) ? values[2].trim() : "";
			this.createTime = values.length > 3 && StringUtils.isNotEmpty(values[3]) ? values[3] : "";
			this.updateTime = values.length > 4 && StringUtils.isNotEmpty(values[4]) ? values[4] : "";
			this.inActivities = values.length > 5 && StringUtils.isNotEmpty(values[5]) ? values[5] : "";
		}

	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public Long getAccountVpoints() {
		return accountVpoints;
	}

	public void setAccountVpoints(Long accountVpoints) {
		this.accountVpoints = accountVpoints;
	}

	public Long getSurplusVpoints() {
		return surplusVpoints;
	}

	public void setSurplusVpoints(Long surplusVpoints) {
		this.surplusVpoints = surplusVpoints;
	}

	public Double getAccountMoney() {
		return accountMoney;
	}

	public void setAccountMoney(Double accountMoney) {
		this.accountMoney = accountMoney;
	}

	public Double getSurplusMoney() {
		return surplusMoney;
	}

	public void setSurplusMoney(Double surplusMoney) {
		this.surplusMoney = surplusMoney;
	}

    public double getFreezeMoney() {
        return freezeMoney;
    }

    public void setFreezeMoney(double freezeMoney) {
        this.freezeMoney = freezeMoney;
    }

    @Override
	public String getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Override
	public String getUpdateTime() {
		return updateTime;
	}

	@Override
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
