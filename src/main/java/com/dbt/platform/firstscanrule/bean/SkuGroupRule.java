package com.dbt.platform.firstscanrule.bean;

public class SkuGroupRule{

	private String skuGroupName;
	private String skuKeys;
	public SkuGroupRule(){}
	public SkuGroupRule(String skuGroupName, String skuKeys){
		this.skuGroupName = skuGroupName;
		this.skuKeys = skuKeys;
	}
	public String getSkuGroupName() {
		return skuGroupName;
	}
	public void setSkuGroupName(String skuGroupName) {
		this.skuGroupName = skuGroupName;
	}
	public String getSkuKeys() {
		return skuKeys;
	}
	public void setSkuKeys(String skuKeys) {
		this.skuKeys = skuKeys;
	}
	
}
