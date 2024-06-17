package com.dbt.platform.promotion.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  捆绑促销活动配置表Bean
 */
@SuppressWarnings("serial")
public class VpsVcodeBindPromotionCog extends BasicProperties {
    
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
    /** 周期类型:0周、1月、2天*/
    private String periodType;
    /** SKU筛选关系：0并且、1或者、3混合*/
    private String skuRelationType;
    /** 周期红包上限，0:不限制 */
    private Integer prizeUpperLimit;
    /** SKU筛选周期：0本期、1上期*/
    private String skuRelationPeriod;
    /** 是否已有金额配置：0：否；1：是*/
    private String moneyConfigFlag;
    /** 促销SKU主键 */
    private String promotionSkuKey;
    /** 促销SKU达到中奖的限制次数 */
    private Integer promotionSkuNum;
    /** 奖项获取类型：0替换扫码红包、1额外红包 */
    private String prizeGainType;
    /** 可疑规则类型：1：系数；2：区间 */
    private String doubtRuleType;
    /** 可疑规则系数值 */
    private String doubtRuleCoe;
    /** 可疑规则最小区间 */
    private String doubtRuleRangeMin;
    /** 可疑规则最大区间 */
    private String doubtRuleRangeMax;
    /** 活动描述*/
    private String activityDesc;
    
    //-------------------扩展字段---------------------
    private String IsBegin;
    private String promotionSkuName;
    
    private String[] infoKey;
    private String[] skuKey;
    private String[] signType;
    private String[] signOperator;
    private Integer[] signNum;
    private String[] continueFlag;
    private String keyword;
    /** 选项卡标识：0待上线，1已上线，2已下线，3全部 **/
    private String tabsFlag;
    private String activityNo;
    private String skuName;
    
    public VpsVcodeBindPromotionCog() {}
    public VpsVcodeBindPromotionCog(String queryParam) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyword = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getSkuRelationPeriod() {
        return skuRelationPeriod;
    }

    public void setSkuRelationPeriod(String skuRelationPeriod) {
        this.skuRelationPeriod = skuRelationPeriod;
    }

    public String getDoubtRuleType() {
        return doubtRuleType;
    }

    public void setDoubtRuleType(String doubtRuleType) {
        this.doubtRuleType = doubtRuleType;
    }

    public String getDoubtRuleCoe() {
        return doubtRuleCoe;
    }

    public void setDoubtRuleCoe(String doubtRuleCoe) {
        this.doubtRuleCoe = doubtRuleCoe;
    }

    public String getDoubtRuleRangeMin() {
        return doubtRuleRangeMin;
    }

    public void setDoubtRuleRangeMin(String doubtRuleRangeMin) {
        this.doubtRuleRangeMin = doubtRuleRangeMin;
    }

    public String getDoubtRuleRangeMax() {
        return doubtRuleRangeMax;
    }

    public void setDoubtRuleRangeMax(String doubtRuleRangeMax) {
        this.doubtRuleRangeMax = doubtRuleRangeMax;
    }

    public String getPromotionSkuKey() {
        return promotionSkuKey;
    }

    public void setPromotionSkuKey(String promotionSkuKey) {
        this.promotionSkuKey = promotionSkuKey;
    }

    public Integer getPromotionSkuNum() {
        return promotionSkuNum;
    }

    public void setPromotionSkuNum(Integer promotionSkuNum) {
        this.promotionSkuNum = promotionSkuNum;
    }

    public String getPrizeGainType() {
        return prizeGainType;
    }

    public void setPrizeGainType(String prizeGainType) {
        this.prizeGainType = prizeGainType;
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

    public String[] getSignOperator() {
        return signOperator;
    }

    public void setSignOperator(String[] signOperator) {
        this.signOperator = signOperator;
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
    public String getPromotionSkuName() {
        return promotionSkuName;
    }

    public void setPromotionSkuName(String promotionSkuName) {
        this.promotionSkuName = promotionSkuName;
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
    public String getSkuName() {
        return skuName;
    }
    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
    
}
