package com.dbt.platform.firstscanrule.bean;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class FirstScanRule implements Serializable{

	/** 首扫类型：用户：USER，sku：SKU，活动：ACTIVITY，分组：GROUP **/
	private String firstScanType;
	/** SKU分组信息 **/
	private List<SkuGroupRule> skuGroupRuleList;
	/** 规则串 （需要解析到skuGroupRuleList中）**/
	private String ruleValue;
	public FirstScanRule(){}
	public FirstScanRule(String firstScanType, List<SkuGroupRule> skuGroupRuleList){
		this.firstScanType = firstScanType;
		this.skuGroupRuleList = skuGroupRuleList;
	}
	
	public String getFirstScanType() {
		return firstScanType;
	}
	public void setFirstScanType(String firstScanType) {
		this.firstScanType = firstScanType;
	}
	public List<SkuGroupRule> getSkuGroupRuleList() {
		return skuGroupRuleList;
	}
	public void setSkuGroupRuleList(List<SkuGroupRule> skuGroupRuleList) {
		this.skuGroupRuleList = skuGroupRuleList;
	}
	public String getRuleValue() {
		return ruleValue;
	}
	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}
}
