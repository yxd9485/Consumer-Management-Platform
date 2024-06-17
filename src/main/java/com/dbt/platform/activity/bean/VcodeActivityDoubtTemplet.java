package com.dbt.platform.activity.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 风控模板主表
 */
public class VcodeActivityDoubtTemplet extends BasicProperties {
    private static final long serialVersionUID = 4986132291402166622L;
    
    private String infoKey;
    private String companyKey;
    private String templetName;
    /** 规则一：同一分钟扫码次数大于等于X次 */
    private Integer sameMinuteRestrict;
    /** 规则二：同一天扫码次数大于等于X次 */
    private Integer sameDayRestrict;
    /** 规则三：历史累计扫码次数大于等于X次 */
    private Integer historyTimesRestrict;
    /** 规则四：同一月累计扫码次数大于等于X次 */
    private Integer sameMonthRestrict;
    /** 可疑规则时间限制**/
    private String doubtfulTimeLimitType;
    private String limitFactory;
    /** 可疑返利类型：0 金额， 1 积分 **/
    private String doubtRebateType;
    /** 疑似黑名单规则最小区间 */
    private String doubtRuleRangeMin;
    /** 疑似黑名单规则最大区间 */
    private String doubtRuleRangeMax;
    private String templetDesc;
    private String status;
    
    // 扩展字段
    private String startDate;
    private String endDate;
    
    public VcodeActivityDoubtTemplet() {
        
    }
    
    public VcodeActivityDoubtTemplet(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.templetName = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
        this.status = paramAry.length > 3 ? paramAry[3] : "";
    }

    public Integer getSameMonthRestrict () {
        return sameMonthRestrict;
    }
    
    public void setSameMonthRestrict (Integer sameMonthRestrict) {
        this.sameMonthRestrict = sameMonthRestrict;
    }
    
    public String getDoubtfulTimeLimitType () {
        return doubtfulTimeLimitType;
    }
    
    public void setDoubtfulTimeLimitType (String doubtfulTimeLimitType) {
        this.doubtfulTimeLimitType = doubtfulTimeLimitType;
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

    public String getTempletName() {
        return templetName;
    }

    public void setTempletName(String templetName) {
        this.templetName = templetName;
    }

    public Integer getSameMinuteRestrict() {
        return sameMinuteRestrict;
    }

    public void setSameMinuteRestrict(Integer sameMinuteRestrict) {
        this.sameMinuteRestrict = sameMinuteRestrict;
    }

    public Integer getSameDayRestrict() {
        return sameDayRestrict;
    }

    public void setSameDayRestrict(Integer sameDayRestrict) {
        this.sameDayRestrict = sameDayRestrict;
    }

    public Integer getHistoryTimesRestrict() {
        return historyTimesRestrict;
    }

    public void setHistoryTimesRestrict(Integer historyTimesRestrict) {
        this.historyTimesRestrict = historyTimesRestrict;
    }

    public String getLimitFactory() {
        return limitFactory;
    }

    public void setLimitFactory(String limitFactory) {
        this.limitFactory = limitFactory;
    }

    public String getDoubtRebateType() {
        return doubtRebateType;
    }

    public void setDoubtRebateType(String doubtRebateType) {
        this.doubtRebateType = doubtRebateType;
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

    public String getTempletDesc() {
        return templetDesc;
    }

    public void setTempletDesc(String templetDesc) {
        this.templetDesc = templetDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
