package com.dbt.platform.ladder.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 阶梯规则
 * @author hanshimeng
 *
 */

@SuppressWarnings("serial")
public class LadderRuleCog extends BasicProperties {

	/** 规则主键 **/
	private String infoKey;
	/** 规则编号 **/
	private String ruleNo;
	/** 阶梯规则名称 **/
	private String ruleName;
	/** 规则开始时间 **/
	private String startDate;
	/** 规则结束时间 **/
	private String endDate;
	/** 阶梯规则：1每天，2累计 **/
	private String ruleFlag;
	/** 阶梯开始时间 **/
	private String ladderStartTime;
	/** 阶梯结束时间 **/
	private String ladderEndTime;
	/** 活动keys，多个用逗号分隔 **/
	private String vcodeActivityKeys;
    private String currDate;
    private String isBegin;
    /** 阶梯红包与翻倍卡是否互斥：0否、1是*/
    private String allowanceaExclusiveFlag;
    /** 备注*/
    private String remarks;
    public LadderRuleCog() {
        super();
    }

    public LadderRuleCog(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.ruleName = paramAry.length > 0 ? paramAry[0] : "";
    }
    
	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
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

	public String getRuleFlag() {
		return ruleFlag;
	}

	public void setRuleFlag(String ruleFlag) {
		this.ruleFlag = ruleFlag;
	}

	public String getLadderStartTime() {
		return ladderStartTime;
	}

	public void setLadderStartTime(String ladderStartTime) {
		this.ladderStartTime = ladderStartTime;
	}

	public String getLadderEndTime() {
		return ladderEndTime;
	}

	public void setLadderEndTime(String ladderEndTime) {
		this.ladderEndTime = ladderEndTime;
	}

	public String getCurrDate() {
		return currDate;
	}
	
	public void setCurrDate(String currDate) {
		this.currDate = currDate;
	}

	public String getRuleNo() {
		return ruleNo;
	}

	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}

	public String getVcodeActivityKeys() {
		return vcodeActivityKeys;
	}

	public void setVcodeActivityKeys(String vcodeActivityKeys) {
		this.vcodeActivityKeys = vcodeActivityKeys;
	}

	public String getIsBegin() {
		return isBegin;
	}

	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
	}

    public String getAllowanceaExclusiveFlag() {
        return allowanceaExclusiveFlag;
    }

    public void setAllowanceaExclusiveFlag(String allowanceaExclusiveFlag) {
        this.allowanceaExclusiveFlag = allowanceaExclusiveFlag;
    }

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
