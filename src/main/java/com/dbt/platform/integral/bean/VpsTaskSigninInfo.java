package com.dbt.platform.integral.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 签到任务Bean
 */
public class VpsTaskSigninInfo extends BasicProperties {

    private static final long serialVersionUID = 1009656056443543868L;
    
    /** 主键 */
    private String infoKey;
    /** 基础规则主键 */
    private String basicRuleKey;
    /** 特殊规则主键 */
    private String specialRuleKey;
    /** 时间类型：1 按天，2按周 */
    private String timeType;
    /** 时间值：多个使用英文逗号分开 */
    private String timeValue;
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String endDate;
    
    public VpsTaskSigninInfo() {}

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getBasicRuleKey() {
		return basicRuleKey;
	}

	public void setBasicRuleKey(String basicRuleKey) {
		this.basicRuleKey = basicRuleKey;
	}

	public String getSpecialRuleKey() {
		return specialRuleKey;
	}

	public void setSpecialRuleKey(String specialRuleKey) {
		this.specialRuleKey = specialRuleKey;
	}

	public String getTimeType() {
		return timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	public String getTimeValue() {
		return timeValue;
	}

	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
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
}
