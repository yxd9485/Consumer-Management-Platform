package com.dbt.seckill.bean;

import java.io.Serializable;

import com.dbt.framework.util.DateUtil;
public class SeckillActivityBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String seckillId;
	private String seckillType;
	private String seckillName;
	private String seckillVcode;
	private String seckillUrl;
	private String seckillStatus;
	private String skuKey;
	private String seckillDateType;
	private String startDate;
	private String endDate;
	private String seckillTimeSet;
	private String province;
	private String city;
	private String county;
	private String followScanType;
	private String seckillRuleKey;
	private int seckillCountLimit;
	private double seckillMoneyLimit;
	private String userLimitType;
	private int seckillUserlimit;
	private String filterAreaCode;
	private String filterAreaName;
	
	private String createUser;
	private String createTime;
	private String deleteFlag;
	private String updateTime;
	private String hotAreaKey;

	public String getHotAreaKey() {
		return hotAreaKey;
	}

	public void setHotAreaKey(String hotAreaKey) {
		this.hotAreaKey = hotAreaKey;
	}

	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getSeckillId() {
		return seckillId;
	}
	public void setSeckillId(String seckillId) {
		this.seckillId = seckillId;
	}
	public String getSeckillType() {
		return seckillType;
	}
	public void setSeckillType(String seckillType) {
		this.seckillType = seckillType;
	}
	public String getSeckillName() {
		return seckillName;
	}
	public void setSeckillName(String seckillName) {
		this.seckillName = seckillName;
	}
	public String getSeckillVcode() {
		return seckillVcode;
	}
	public void setSeckillVcode(String seckillVcode) {
		this.seckillVcode = seckillVcode;
	}
	public String getSeckillUrl() {
		return seckillUrl;
	}
	public void setSeckillUrl(String seckillUrl) {
		this.seckillUrl = seckillUrl;
	}
	public String getSeckillStatus() {
		return seckillStatus;
	}
	public void setSeckillStatus(String seckillStatus) {
		this.seckillStatus = seckillStatus;
	}
	public String getSkuKey() {
		return skuKey;
	}
	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}
	public String getSeckillDateType() {
		return seckillDateType;
	}
	public void setSeckillDateType(String seckillDateType) {
		this.seckillDateType = seckillDateType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSeckillTimeSet() {
		return seckillTimeSet;
	}
	public void setSeckillTimeSet(String seckillTimeSet) {
		this.seckillTimeSet = seckillTimeSet;
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
	public String getFollowScanType() {
		return followScanType;
	}
	public void setFollowScanType(String followScanType) {
		this.followScanType = followScanType;
	}
	public String getSeckillRuleKey() {
		return seckillRuleKey;
	}
	public void setSeckillRuleKey(String seckillRuleKey) {
		this.seckillRuleKey = seckillRuleKey;
	}
	public int getSeckillCountLimit() {
		return seckillCountLimit;
	}
	public void setSeckillCountLimit(int seckillCountLimit) {
		this.seckillCountLimit = seckillCountLimit;
	}
	public double getSeckillMoneyLimit() {
		return seckillMoneyLimit;
	}
	public void setSeckillMoneyLimit(double seckillMoneyLimit) {
		this.seckillMoneyLimit = seckillMoneyLimit;
	}
	public String getUserLimitType() {
		return userLimitType;
	}
	public void setUserLimitType(String userLimitType) {
		this.userLimitType = userLimitType;
	}
	public int getSeckillUserlimit() {
		return seckillUserlimit;
	}
	public void setSeckillUserlimit(int seckillUserlimit) {
		this.seckillUserlimit = seckillUserlimit;
	}
	public String getFilterAreaCode() {
        return filterAreaCode;
    }
    public void setFilterAreaCode(String filterAreaCode) {
        this.filterAreaCode = filterAreaCode;
    }
    public String getFilterAreaName() {
        return filterAreaName;
    }
    public void setFilterAreaName(String filterAreaName) {
        this.filterAreaName = filterAreaName;
    }
    public void fillFields(String userKey){
	    this.createTime = DateUtil.getDateTime();
	    this.updateTime = DateUtil.getDateTime();
	    this.createUser = userKey;
	}
}
