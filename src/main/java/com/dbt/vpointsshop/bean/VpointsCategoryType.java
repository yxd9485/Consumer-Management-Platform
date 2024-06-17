package com.dbt.vpointsshop.bean;
/**
 * 商品品类
 * @author zhaohongtao
 *2017年12月11日
 */
public class VpointsCategoryType {
	private String categoryType;
	private String categoryName;
	private String categoryParent;
	private String exchangeType;
	private String categoryIco;
	private String categoryImg;
	private String categoryOrder;
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryParent() {
		return categoryParent;
	}
	public void setCategoryParent(String categoryParent) {
		this.categoryParent = categoryParent;
	}
	public String getCategoryIco() {
		return categoryIco;
	}
	public void setCategoryIco(String categoryIco) {
		this.categoryIco = categoryIco;
	}
	public String getCategoryImg() {
		return categoryImg;
	}
	public void setCategoryImg(String categoryImg) {
		this.categoryImg = categoryImg;
	}
	public String getCategoryOrder() {
		return categoryOrder;
	}
	public void setCategoryOrder(String categoryOrder) {
		this.categoryOrder = categoryOrder;
	}
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
}
