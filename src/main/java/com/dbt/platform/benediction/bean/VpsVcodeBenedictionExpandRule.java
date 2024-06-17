package com.dbt.platform.benediction.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 膨胀红包规则配置Bean
 */
public class VpsVcodeBenedictionExpandRule extends BasicProperties {
    
    private static final long serialVersionUID = 9184357441613245857L;
    
    /** 主键*/
    private String infoKey;
    /** 规则编号*/
    private String expandRuleNo;
    /** 规则名称*/
    private String expandRuleName;
    /** 活动主键*/
    private String vcodeActivityKey;
    /**规则开始日期 */
    private String beginDate;
    /** 规则结束日期*/
    private String endDate;
    /** 单瓶膨胀成本*/
    private String expandDanping;
    /** 膨胀红包个数*/
    private String expandNum;
    /** 膨胀红包最小额度*/
    private String expandMinMoney;
    /** 膨胀红包最大额度*/
    private String expandMaxMoney;
    /** 膨胀截止日期类型：0指定日期、1有效天数*/
    private String expireType;
    /** 截止日期*/
    private String expireDate;
    /** 有效天数*/
    private String expireDay;
    
    private String tabsFlag;
    private String isBegin;
    private String vcodeActivityName;
    

    public VpsVcodeBenedictionExpandRule() {}
    public VpsVcodeBenedictionExpandRule(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.expandRuleNo = paramAry.length > 0 ? paramAry[0] : "";
        this.expandRuleName = paramAry.length > 1 ? paramAry[1] : "";
        this.isBegin = paramAry.length > 2 ? paramAry[2] : "";
    }
    
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getExpandRuleNo() {
        return expandRuleNo;
    }
    public void setExpandRuleNo(String expandRuleNo) {
        this.expandRuleNo = expandRuleNo;
    }
    public String getExpandRuleName() {
        return expandRuleName;
    }
    public void setExpandRuleName(String expandRuleName) {
        this.expandRuleName = expandRuleName;
    }
    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }
    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }
    public String getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getExpandDanping() {
        return expandDanping;
    }
    public void setExpandDanping(String expandDanping) {
        this.expandDanping = expandDanping;
    }
    public String getExpandNum() {
        return expandNum;
    }
    public void setExpandNum(String expandNum) {
        this.expandNum = expandNum;
    }
    public String getExpandMinMoney() {
        return expandMinMoney;
    }
    public void setExpandMinMoney(String expandMinMoney) {
        this.expandMinMoney = expandMinMoney;
    }
    public String getExpandMaxMoney() {
        return expandMaxMoney;
    }
    public void setExpandMaxMoney(String expandMaxMoney) {
        this.expandMaxMoney = expandMaxMoney;
    }
    public String getExpireType() {
        return expireType;
    }
    public void setExpireType(String expireType) {
        this.expireType = expireType;
    }
    public String getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
    public String getExpireDay() {
        return expireDay;
    }
    public void setExpireDay(String expireDay) {
        this.expireDay = expireDay;
    }
    public String getTabsFlag() {
        return tabsFlag;
    }
    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }
    public String getIsBegin() {
        return isBegin;
    }
    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }
    public String getVcodeActivityName() {
        return vcodeActivityName;
    }
    public void setVcodeActivityName(String vcodeActivityName) {
        this.vcodeActivityName = vcodeActivityName;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
