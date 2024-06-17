package com.dbt.framework.datadic.bean;

import org.springframework.util.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
* 文件名: Datadic
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述: 数据字典类型bean
* 修改人: HaoQi
* 修改时间：2014-08-19 13:40:45
* 修改内容：新增
*/
public class SysDicCategoryInfo extends BasicProperties {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4661664182626449356L;
	
	private String categoryKey;

	private String categoryName;
	
	private String categoryCode;
	
	private String dicType;
	
	private String invoker;
	
	private String categoryExplain;
	
	public SysDicCategoryInfo(String params) {
		if(!StringUtils.isEmpty(params)){
			String[] values = params.split(",");
			this.categoryName = values.length > 0 ? values[0] : "";
			this.dicType = values.length > 1 ? values[1]: "";
			this.invoker = values.length > 2 ? values[2] : "";
		}
	}

	public SysDicCategoryInfo() {
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	public String getInvoker() {
		return invoker;
	}

	public void setInvoker(String invoker) {
		this.invoker = invoker;
	}

	public String getCategoryExplain() {
		return categoryExplain;
	}

	public void setCategoryExplain(String categoryExplain) {
		this.categoryExplain = categoryExplain;
	}
	
}
