package com.dbt.platform.waitActivation.bean;

import com.dbt.platform.enterprise.bean.SkuInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 天降红包活动配置bean
 */
public class WaitActivationRule implements Serializable {
    private String activityKey;//活动key
    private String activityName;//活动名称
    private String startDate;//活动开始时间
    private String endDate;//活动结束时间
    private String startTimeSpan;//开始时段
    private String endTimeSpan;//结束时段
    private String activityScopeType;//活动范围类型
    private String areaCode;//行政区编码集合
    private String hotAreaKey;//热区key
    private List<String> multipleRuleInfoKeyList;//倍数中出规则key
    private String multipleVpointsRatio;//倍数红包待激活比例
    private Double proposedInvestmentAmount;//拟投放金额
    private String postedAmount;//已经投放的金额
    private String redEnvelopeLimit;//待激活红包上限
    private String validityType;//待激活红包有效期类型
    private String effectiveDays;//中出后有效天数
    private String fixedDeadline;//固定的截止日期
    private String boomVpointsRatio;//爆点红包待激活比例
    private List<String> skuKeys;//激活SKU集合
    private String[] skuKeyArray;//
    private String ruleImageUrl;//规则图片地址
    private String productImageUrl;//活动产品图片地址
    private String freItems;//新用户频次中出

    //扩展字段
    private String isBegin;//是否开始 0 待上线；1 已上线；2 已下线
    private String createTime;//创建时间 CREATE_TIME
    private String[] infoKey;//倍数中出规则--infokey
    private String[] drawNum;//倍数中出规则--抽奖次数
    private String[] money;//倍数中出规则--必中金额
    private String[] putInNum;//倍数中出规则--每天投放个数

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
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

    public String getStartTimeSpan() {
        return startTimeSpan;
    }

    public void setStartTimeSpan(String startTimeSpan) {
        this.startTimeSpan = startTimeSpan;
    }

    public String getEndTimeSpan() {
        return endTimeSpan;
    }

    public void setEndTimeSpan(String endTimeSpan) {
        this.endTimeSpan = endTimeSpan;
    }

    public String getActivityScopeType() {
        return activityScopeType;
    }

    public void setActivityScopeType(String activityScopeType) {
        this.activityScopeType = activityScopeType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getHotAreaKey() {
        return hotAreaKey;
    }

    public void setHotAreaKey(String hotAreaKey) {
        this.hotAreaKey = hotAreaKey;
    }

    public List<String> getMultipleRuleInfoKeyList() {
        return multipleRuleInfoKeyList;
    }

    public void setMultipleRuleInfoKeyList(List<String> multipleRuleInfoKeyList) {
        this.multipleRuleInfoKeyList = multipleRuleInfoKeyList;
    }

    public String getMultipleVpointsRatio() {
        return multipleVpointsRatio;
    }

    public void setMultipleVpointsRatio(String multipleVpointsRatio) {
        this.multipleVpointsRatio = multipleVpointsRatio;
    }

    public Double getProposedInvestmentAmount() {
        return proposedInvestmentAmount;
    }

    public void setProposedInvestmentAmount(Double proposedInvestmentAmount) {
        this.proposedInvestmentAmount = proposedInvestmentAmount;
    }

    public String getPostedAmount() {
        return postedAmount;
    }

    public void setPostedAmount(String postedAmount) {
        this.postedAmount = postedAmount;
    }

    public String getRedEnvelopeLimit() {
        return redEnvelopeLimit;
    }

    public void setRedEnvelopeLimit(String redEnvelopeLimit) {
        this.redEnvelopeLimit = redEnvelopeLimit;
    }

    public String getValidityType() {
        return validityType;
    }

    public void setValidityType(String validityType) {
        this.validityType = validityType;
    }

    public String getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(String effectiveDays) {
        this.effectiveDays = effectiveDays;
    }

    public String getFixedDeadline() {
        return fixedDeadline;
    }

    public void setFixedDeadline(String fixedDeadline) {
        this.fixedDeadline = fixedDeadline;
    }

    public String getBoomVpointsRatio() {
        return boomVpointsRatio;
    }

    public void setBoomVpointsRatio(String boomVpointsRatio) {
        this.boomVpointsRatio = boomVpointsRatio;
    }

    public List<String> getSkuKeys() {
        return skuKeys;
    }

    public void setSkuKeys(List<String> skuKeys) {
        this.skuKeys = skuKeys;
    }

    public String[] getSkuKeyArray() {
        return skuKeyArray;
    }

    public void setSkuKeyArray(String[] skuKeyArray) {
        this.skuKeyArray = skuKeyArray;
    }

    public String getRuleImageUrl() {
        return ruleImageUrl;
    }

    public void setRuleImageUrl(String ruleImageUrl) {
        this.ruleImageUrl = ruleImageUrl;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getFreItems() {
        return freItems;
    }

    public void setFreItems(String freItems) {
        this.freItems = freItems;
    }

    public String getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String[] getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String[] infoKey) {
        this.infoKey = infoKey;
    }

    public String[] getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(String[] drawNum) {
        this.drawNum = drawNum;
    }

    public String[] getMoney() {
        return money;
    }

    public void setMoney(String[] money) {
        this.money = money;
    }

    public String[] getPutInNum() {
        return putInNum;
    }

    public void setPutInNum(String[] putInNum) {
        this.putInNum = putInNum;
    }
}
