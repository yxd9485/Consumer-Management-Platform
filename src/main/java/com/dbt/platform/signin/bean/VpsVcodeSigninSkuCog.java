package com.dbt.platform.signin.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  签到活动SKU限制配置表Bean
 */
@SuppressWarnings("serial")
public class VpsVcodeSigninSkuCog extends BasicProperties {
    
    /** 活动主键*/
    private String infoKey;
    /** 活动主键*/
    private String activityKey;
    /** SKU主键*/
    private String skuKey;
    /** 签到类型:0天、1次*/
    private String signType;
    /** 签到天/次数运算符:0小于、1小于等于、2等于、3大于等于、4、大于*/
    private String signOperator;
    /** 签到达成天/次数*/
    private Integer signNum;
    /** 连续签到标志：null/0：否；1：是*/
    private String continueFlag;
    
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getActivityKey() {
        return activityKey;
    }
    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }
    public String getSkuKey() {
        return skuKey;
    }
    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }
    public String getSignType() {
        return signType;
    }
    public void setSignType(String signType) {
        this.signType = signType;
    }
    public String getSignOperator() {
        return signOperator;
    }
    public void setSignOperator(String signOperator) {
        this.signOperator = signOperator;
    }
    public Integer getSignNum() {
        return signNum;
    }
    public void setSignNum(Integer signNum) {
        this.signNum = signNum;
    }
    public String getContinueFlag() {
        return continueFlag;
    }
    public void setContinueFlag(String continueFlag) {
        this.continueFlag = continueFlag;
    }
}
