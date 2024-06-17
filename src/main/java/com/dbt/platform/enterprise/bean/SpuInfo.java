package com.dbt.platform.enterprise.bean;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

@SuppressWarnings("serial")
public class SpuInfo extends BasicProperties {

	private String spuInfoKey;
	private String spuCode;
	private String spuName;
	private String companyKey;
	private String skuKeyGroup;
	private List<SkuInfo> skuList;
	
	// 扩展sku属性
	private String skuName;
	private String commodityCode;
	
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getCommodityCode() {
		return commodityCode;
	}
	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}
	public String getSpuInfoKey() {
		return spuInfoKey;
	}
	public void setSpuInfoKey(String spuInfoKey) {
		this.spuInfoKey = spuInfoKey;
	}
	public String getSpuCode() {
		return spuCode;
	}
	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
	public String getSpuName() {
		return spuName;
	}
	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public List<SkuInfo> getSkuList() {
		return skuList;
	}
	public void setSkuList(List<SkuInfo> skuList) {
		this.skuList = skuList;
	}
	public String getSkuKeyGroup() {
		return skuKeyGroup;
	}
	public void setSkuKeyGroup(String skuKeyGroup) {
		this.skuKeyGroup = skuKeyGroup;
	}
	public SpuInfo(){}
	public SpuInfo(String queryParam){
	    String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
	    this.spuName = paramAry.length > 0 ? paramAry[0] : "";
	    this.spuCode = paramAry.length > 1 ? paramAry[1] : "";
	    this.commodityCode = paramAry.length > 2 ? paramAry[2] : "";
	    this.skuName = paramAry.length > 3 ? paramAry[3] : "";
	}
}
