package com.dbt.platform.permantissa.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 逢尾数中奖规则Bean
 */
public class VpsVcodePerMantissaCog extends BasicProperties {
    
    private static final long serialVersionUID = -5946111409163002699L;
    /** 主键*/
    private String infoKey;
    /** 企业主键*/
    private String companyKey;
    /** 逢尾数活动编号*/
    private String perMantissaNo;
    /** 逢尾数活动名称*/
    private String perMantissaName;
    /** 活动开始日期*/
    private String startDate;
    /** 活动结束日期*/
    private String endDate;
    /** 循环日期集合*/
    private String cycleDay;
    /** 循环尾数集合 */
    private String cycleMantissa;
    /** 兑奖截止日期类型：0指定日期、1有效天数*/
    private String prizeExpireType;
    /** 奖品兑奖截止日期*/
    private String prizeExpireDate;
    /** 大奖自中出后兑换有效天数*/
    private int prizeValidDay;
    /** 促销SKU主键集合*/
    private String skuKey;
    /** 促销SKU名称集合*/
    private String skuName;
    /** 限制时间类型：0规则时间，1每天 **/
    private String restrictTimeType;
     /** 消费限制积分 **/
    private int restrictVpoints;
    /** 消费限制金额 **/
    private double restrictMoney;
    /** 消费限制瓶数 **/
    private int restrictBottle;
    /** 状态：0未启用、1已启用 **/
    private String status;
    
    // 扩展字段
    /** 上线状态：0 待上线，1 已上线，2 已过期 **/
    private String isBegin;
    /** 选项卡标识 **/
    private String tabsFlag;
    /** 规则消耗积分 **/
    private int ruleTotalVpoints;
    /** 规则消耗金额 **/
    private double ruleTotalMoney;
    /** 规则消耗瓶数 **/
    private int ruleTotalBottle;
    
    public VpsVcodePerMantissaCog() {}
    public VpsVcodePerMantissaCog(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.perMantissaNo = paramAry.length > 0 ? paramAry[0] : "";
        this.perMantissaName = paramAry.length > 1 ? paramAry[1] : "";
        this.status = paramAry.length > 2 ? paramAry[2] : "";
        this.isBegin = paramAry.length > 3 ? paramAry[3] : "";
    }
    
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getCompanyKey() {
        return companyKey;
    }
    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }
    public String getPerMantissaNo() {
        return perMantissaNo;
    }
    public void setPerMantissaNo(String perMantissaNo) {
        this.perMantissaNo = perMantissaNo;
    }
    public String getPerMantissaName() {
        return perMantissaName;
    }
    public void setPerMantissaName(String perMantissaName) {
        this.perMantissaName = perMantissaName;
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
    public String getCycleDay() {
        return cycleDay;
    }
    public void setCycleDay(String cycleDay) {
        this.cycleDay = cycleDay;
    }
    public String getCycleMantissa() {
        return cycleMantissa;
    }
    public void setCycleMantissa(String cycleMantissa) {
        this.cycleMantissa = cycleMantissa;
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
    public String getSkuKey() {
        return skuKey;
    }
    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }
    public String getSkuName() {
        return skuName;
    }
    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
    public String getRestrictTimeType() {
        return restrictTimeType;
    }
    public void setRestrictTimeType(String restrictTimeType) {
        this.restrictTimeType = restrictTimeType;
    }
    public int getRestrictVpoints() {
        return restrictVpoints;
    }
    public void setRestrictVpoints(int restrictVpoints) {
        this.restrictVpoints = restrictVpoints;
    }
    public double getRestrictMoney() {
        return restrictMoney;
    }
    public void setRestrictMoney(double restrictMoney) {
        this.restrictMoney = restrictMoney;
    }
    public int getRestrictBottle() {
        return restrictBottle;
    }
    public void setRestrictBottle(int restrictBottle) {
        this.restrictBottle = restrictBottle;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getIsBegin() {
        return isBegin;
    }
    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }
    public String getTabsFlag() {
        return tabsFlag;
    }
    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }
    public int getRuleTotalVpoints() {
        return ruleTotalVpoints;
    }
    public void setRuleTotalVpoints(int ruleTotalVpoints) {
        this.ruleTotalVpoints = ruleTotalVpoints;
    }
    public double getRuleTotalMoney() {
        return ruleTotalMoney;
    }
    public void setRuleTotalMoney(double ruleTotalMoney) {
        this.ruleTotalMoney = ruleTotalMoney;
    }
    public int getRuleTotalBottle() {
        return ruleTotalBottle;
    }
    public void setRuleTotalBottle(int ruleTotalBottle) {
        this.ruleTotalBottle = ruleTotalBottle;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
