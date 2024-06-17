package com.dbt.platform.bidding.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  竞价活动配置表Bean
 */
@SuppressWarnings("serial")
public class VpsBiddingActiviy extends BasicProperties {
    
    /** 活动主键*/
    private String activityKey;
    /** 活动编号*/
    private String activityNo;
    /** 企业主键*/
    private String companyKey;
    /** 活动名称*/
    private String activityName;
    /** 起始日期*/
    private String startDate;
    /** 结束日期*/
    private String endDate;
    /** 活动类型*/
    private String activityType;
    /** 商品ID*/
    private String goodsId;
    /** 开场人数（日擂台使用）*/
    private int openingNumber;
    /** 虚拟人数（日擂台使用）*/
    private int virtualNumber;
    /** 兑换所需积分（月擂台使用）*/
    private int exchangeVpoints;
    /** 兑奖截止日期类型：0指定日期、1有效天数*/
    private String prizeExpireType;
    /** 奖品兑奖截止日期*/
    private String prizeExpireDate;
    /** 大奖自中出后兑换有效天数*/
    private int prizeValidDay;
    /** 是否酒水专用：1是*/
    private String isDedicated;
    
    //-------------------扩展字段---------------------
    private String isBegin;
    private String keyword;
    /** 选项卡标识：0待上线，1已上线，2已下线，3全部 **/
    private String tabsFlag;
    private String goodsName;
    /** 该活动下多少期 **/
    private int periodsCount;
    
    public VpsBiddingActiviy() {
        super();
    }
    public VpsBiddingActiviy(String queryParam) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyword = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
        this.goodsId = paramAry.length > 3 ? paramAry[3] : "";
        this.activityType = paramAry.length > 4 ? paramAry[4] : "";
    }
	public String getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	public String getActivityNo() {
		return activityNo;
	}
	public void setActivityNo(String activityNo) {
		this.activityNo = activityNo;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
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
	public String getActivityType() {
		return activityType;
	}
	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getOpeningNumber() {
		return openingNumber;
	}
	public void setOpeningNumber(int openingNumber) {
		this.openingNumber = openingNumber;
	}
	public int getVirtualNumber() {
		return virtualNumber;
	}
	public void setVirtualNumber(int virtualNumber) {
		this.virtualNumber = virtualNumber;
	}
	public int getExchangeVpoints() {
		return exchangeVpoints;
	}
	public void setExchangeVpoints(int exchangeVpoints) {
		this.exchangeVpoints = exchangeVpoints;
	}
	public String getPrizeExpireType() {
		return prizeExpireType;
	}
	public void setPrizeExpireType(String prizeExpireType) {
		this.prizeExpireType = prizeExpireType;
	}
	public String getPrizeExpireDate() {
		return prizeExpireDate;
	}
	public void setPrizeExpireDate(String prizeExpireDate) {
		this.prizeExpireDate = prizeExpireDate;
	}
	public int getPrizeValidDay() {
		return prizeValidDay;
	}
	public void setPrizeValidDay(int prizeValidDay) {
		this.prizeValidDay = prizeValidDay;
	}
	public String getIsDedicated() {
		return isDedicated;
	}
	public void setIsDedicated(String isDedicated) {
		this.isDedicated = isDedicated;
	}
	
	public String getIsBegin() {
		return isBegin;
	}
	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getTabsFlag() {
		return tabsFlag;
	}
	public void setTabsFlag(String tabsFlag) {
		this.tabsFlag = tabsFlag;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getPeriodsCount() {
		return periodsCount;
	}
	public void setPeriodsCount(int periodsCount) {
		this.periodsCount = periodsCount;
	}
}
