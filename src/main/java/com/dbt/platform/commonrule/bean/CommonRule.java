package com.dbt.platform.commonrule.bean;

import com.dbt.framework.base.bean.BasicProperties;

@SuppressWarnings("serial")
public class CommonRule extends BasicProperties{

	/** 主键 **/
	private String infoKey;
	/** 规则类型:1首扫 **/
	private String ruleType;
	/** 规则内容 **/
	private String ruleValue;
	
	public CommonRule(){}
	public CommonRule(String ruleType, String ruleValue, String userKey){
		this.ruleType = ruleType;
		this.ruleValue = ruleValue;
		fillFields(userKey);
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public String getRuleValue() {
		return ruleValue;
	}
	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}
	
	
}
