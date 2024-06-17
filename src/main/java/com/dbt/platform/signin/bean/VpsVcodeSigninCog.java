package com.dbt.platform.signin.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  签到活动配置表Bean
 */
@SuppressWarnings("serial")
public class VpsVcodeSigninCog extends BasicProperties {
    
    /** 活动主键*/
    private String activityKey;
    /** 企业主键*/
    private String companyKey;
    /** 活动名称*/
    private String activityName;
    /** 起始日期*/
    private String startDate;
    /** 结束日期*/
    private String endDate;
    /** 周期类型:0周、1月*/
    private String periodType;
    /** 上周期扫码次数限制*/
    private Integer limitNum;
    /** SKU限制关系：0并且、1或者、3混合*/
    private String skuRelationType;
    /** 周期红包上限，0:不限制 */
    private Integer prizeUpperLimit;
    /** 可疑用户是否参与签到活动：0或null不参与、1参与*/
    private String doubtRebateFlag;
    /** 是否已有金额配置：0：否；1：是*/
    private String moneyConfigFlag;
    /** 活动描述*/
    private String activityDesc;
    
    //-------------------扩展字段---------------------
    private String IsBegin;
    
    private String[] infoKey;
    private String[] skuKey;
    private String[] signType;
    private Integer[] signNum;
    private String[] continueFlag;
    private String keyword;
    /** 选项卡标识：0待上线，1已上线，2已下线，3全部 **/
    private String tabsFlag;
    private String activityNo;
    
    public VpsVcodeSigninCog() {}
    public VpsVcodeSigninCog(String queryParam) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyword = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
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
	public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String[] getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String[] infoKey) {
        this.infoKey = infoKey;
    }

    public String[] getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String[] skuKey) {
        this.skuKey = skuKey;
    }

    public String[] getSignType() {
        return signType;
    }

    public void setSignType(String[] signType) {
        this.signType = signType;
    }

    public Integer[] getSignNum() {
        return signNum;
    }

    public void setSignNum(Integer[] signNum) {
        this.signNum = signNum;
    }

    public String[] getContinueFlag() {
        return continueFlag;
    }

    public void setContinueFlag(String[] continueFlag) {
        this.continueFlag = continueFlag;
    }

    public String getIsBegin() {
        return IsBegin;
    }
    public void setIsBegin(String isBegin) {
        IsBegin = isBegin;
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
    public String getPeriodType() {
        return periodType;
    }
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }
    public Integer getLimitNum() {
        return limitNum;
    }
    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }
    public String getSkuRelationType() {
        return skuRelationType;
    }
    public void setSkuRelationType(String skuRelationType) {
        this.skuRelationType = skuRelationType;
    }
    public Integer getPrizeUpperLimit() {
        return prizeUpperLimit;
    }

    public void setPrizeUpperLimit(Integer prizeUpperLimit) {
        this.prizeUpperLimit = prizeUpperLimit;
    }

    public String getDoubtRebateFlag() {
        return doubtRebateFlag;
    }
    public void setDoubtRebateFlag(String doubtRebateFlag) {
        this.doubtRebateFlag = doubtRebateFlag;
    }
    public String getMoneyConfigFlag() {
        return moneyConfigFlag;
    }
    public void setMoneyConfigFlag(String moneyConfigFlag) {
        this.moneyConfigFlag = moneyConfigFlag;
    }
    public String getActivityDesc() {
        return activityDesc;
    }
    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }
}
