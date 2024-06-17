package com.dbt.platform.doubleprize.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  一码双奖活动配置表Bean
 */
@SuppressWarnings("serial")
public class VpsVcodeDoublePrizeCog extends BasicProperties {
    
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
    /** 活动热区主键*/
    private String hotAreaKey;
    /** 是否要关注公众号:0否、1是*/
    private String subscribeStatus;
    /** 活动概述标题*/
    private String activitySummaryTitle;
    /** 活动概述*/
    private String activitySummary;
    /** 活动描述*/
    private String activityDesc;
    /** 促销SKU集合*/
    private String promotionSkuKey;
    /** 活动期间每人中奖次数上限*/
    private String everyoneLimit;
    /** 活动期间每天中奖次数上限*/
    private String everyDayLimit;
    /** 奖品限制*/
    private String prizeLimit;
    /** 兑奖截止日期类型：0指定日期、1有效天数*/
    private String prizeExpireType;
    /** 奖品兑奖截止日期*/
    private String prizeExpireDate;
    /** 大奖自中出后兑换有效天数*/
    private int prizeValidDay;
    /** 筛选区域集合*/
    private String filterAreaCode;
    /** 筛选起始日期*/
    private String filterStartDate;
    /** 筛选结束日期*/
    private String filterEndDate;
    /** 筛选可疑用户是否能参与:0不能、1能参与*/
    private String filterDoubtStatus;
    /** 筛选SKU扫码总次数最小限制*/
    private int filterSkuTotal;
    /** 筛选SKU主键集合*/
    private String filterSkuKey;
    /** 筛选能参与活动的总人数*/
    private int filterPersonNum;
    /** 扫过活动SKU的总人数*/
    private int joinPersonNum;
    /** 是否已有金额配置：0：否；1：是*/
    private String moneyConfigFlag;
    /** 周期类型：0自然天、1自然周日、2自然月、3活动周期*/
    private String periodType;
    /** 筛选类型：0历史扫码用户、1当前扫码用户*/
    private String filterType;
    /** 中奖时扫码返利金额区间最小值*/
    private String minMoney;
    /** 中奖时扫码返利金额区间最大值*/
    private String maxMoney;
    /** 抽奖触发机制：0阶梯、1金额*/
    private String lotteryType;
    /** 触发抽奖金额集合*/
    private String lotteryMoney;
    
    //-------------------扩展字段---------------------
    private String IsBegin;
    private String[] promotionSkuKeyAry;
    private String filterAreaName;
    private String[] filterAreaNameAry;
    private String[] filterSkuKeyAry;
    private String lotteryNum;
    private String cancelLotteryNum;
    private String keyword;
    /** 选项卡标识：0待上线，1已上线，2已下线，3全部 **/
    private String tabsFlag;
    
    public VpsVcodeDoublePrizeCog() {
        super();
    }
    public VpsVcodeDoublePrizeCog(String queryParam) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyword = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
    }

    public String getLotteryType() {
        return lotteryType;
    }
    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType;
    }
    public String getLotteryMoney() {
        return lotteryMoney;
    }
    public void setLotteryMoney(String lotteryMoney) {
        this.lotteryMoney = lotteryMoney;
    }
    public String getPeriodType() {
        return periodType;
    }
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }
    public String getFilterType() {
        return filterType;
    }
    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
    public String getMinMoney() {
        return minMoney;
    }
    public void setMinMoney(String minMoney) {
        this.minMoney = minMoney;
    }
    public String getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(String maxMoney) {
        this.maxMoney = maxMoney;
    }
    public String getTabsFlag() {
		return tabsFlag;
	}

	public void setTabsFlag(String tabsFlag) {
		this.tabsFlag = tabsFlag;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getLotteryNum() {
        return lotteryNum;
    }

    public void setLotteryNum(String lotteryNum) {
        this.lotteryNum = lotteryNum;
    }
    
    public String getCancelLotteryNum() {
        return cancelLotteryNum;
    }
    public void setCancelLotteryNum(String cancelLotteryNum) {
        this.cancelLotteryNum = cancelLotteryNum;
    }
    public String[] getFilterAreaNameAry() {
        return filterAreaNameAry;
    }

    public void setFilterAreaNameAry(String[] filterAreaNameAry) {
        this.filterAreaNameAry = filterAreaNameAry;
    }

    public String getActivityKey() {
        return activityKey;
    }
    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
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
    public String getHotAreaKey() {
        return hotAreaKey;
    }
    public void setHotAreaKey(String hotAreaKey) {
        this.hotAreaKey = hotAreaKey;
    }
    public String getSubscribeStatus() {
        return subscribeStatus;
    }
    public void setSubscribeStatus(String subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }
    public String getActivitySummaryTitle() {
        return activitySummaryTitle;
    }

    public void setActivitySummaryTitle(String activitySummaryTitle) {
        this.activitySummaryTitle = activitySummaryTitle;
    }

    public String getActivitySummary() {
        return activitySummary;
    }

    public void setActivitySummary(String activitySummary) {
        this.activitySummary = activitySummary;
    }

    public String getActivityDesc() {
        return activityDesc;
    }
    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }
    public String getPromotionSkuKey() {
        return promotionSkuKey;
    }
    public void setPromotionSkuKey(String promotionSkuKey) {
        this.promotionSkuKey = promotionSkuKey;
    }
    public String getEveryoneLimit() {
        return everyoneLimit;
    }
    public void setEveryoneLimit(String perPersonLimit) {
        this.everyoneLimit = perPersonLimit;
    }
    public String getEveryDayLimit() {
        return everyDayLimit;
    }
    public void setEveryDayLimit(String everyDayLimit) {
        this.everyDayLimit = everyDayLimit;
    }
    public String getFilterAreaCode() {
        return filterAreaCode;
    }
    public void setFilterAreaCode(String filterAreaCode) {
        this.filterAreaCode = filterAreaCode;
    }
    public String getFilterStartDate() {
        return filterStartDate;
    }
    public void setFilterStartDate(String filterStartDate) {
        this.filterStartDate = filterStartDate;
    }
    public String getFilterEndDate() {
        return filterEndDate;
    }
    public void setFilterEndDate(String filterEndDate) {
        this.filterEndDate = filterEndDate;
    }
    public String getFilterDoubtStatus() {
        return filterDoubtStatus;
    }
    public void setFilterDoubtStatus(String filterDoubtStatus) {
        this.filterDoubtStatus = filterDoubtStatus;
    }
    public int getFilterSkuTotal() {
        return filterSkuTotal;
    }
    public void setFilterSkuTotal(int filterSkuTotal) {
        this.filterSkuTotal = filterSkuTotal;
    }
    public String getFilterSkuKey() {
        return filterSkuKey;
    }
    public void setFilterSkuKey(String filterSkuKey) {
        this.filterSkuKey = filterSkuKey;
    }
    public int getFilterPersonNum() {
        return filterPersonNum;
    }
    public void setFilterPersonNum(int filterPersonNum) {
        this.filterPersonNum = filterPersonNum;
    }
    public int getJoinPersonNum() {
        return joinPersonNum;
    }
    public void setJoinPersonNum(int joinPersonNum) {
        this.joinPersonNum = joinPersonNum;
    }
    public String getIsBegin() {
        return IsBegin;
    }
    public void setIsBegin(String isBegin) {
        IsBegin = isBegin;
    }
    public String getMoneyConfigFlag() {
        return moneyConfigFlag;
    }
    public void setMoneyConfigFlag(String moneyConfigFlag) {
        this.moneyConfigFlag = moneyConfigFlag;
    }

    public String[] getPromotionSkuKeyAry() {
        return promotionSkuKeyAry;
    }

    public void setPromotionSkuKeyAry(String[] promotionSkuKeyAry) {
        this.promotionSkuKeyAry = promotionSkuKeyAry;
    }

    public String getFilterAreaName() {
        return filterAreaName;
    }

    public void setFilterAreaName(String filterAreaName) {
        this.filterAreaName = filterAreaName;
    }

    public String[] getFilterSkuKeyAry() {
        return filterSkuKeyAry;
    }

    public void setFilterSkuKeyAry(String[] filterSkuKeyAry) {
        this.filterSkuKeyAry = filterSkuKeyAry;
    }

    public String getPrizeLimit() {
        return prizeLimit;
    }

    public void setPrizeLimit(String prizeLimit) {
        this.prizeLimit = prizeLimit;
    }

    public String getPrizeExpireType() {
        return prizeExpireType;
    }
    public void setPrizeExpireType(String prizeExpireType) {
        this.prizeExpireType = prizeExpireType;
    }
    public int getPrizeValidDay() {
        return prizeValidDay;
    }
    public void setPrizeValidDay(int prizeValidDay) {
        this.prizeValidDay = prizeValidDay;
    }
    public String getPrizeExpireDate() {
        return prizeExpireDate;
    }

    public void setPrizeExpireDate(String prizeExpireDate) {
        this.prizeExpireDate = prizeExpireDate;
    }
}
