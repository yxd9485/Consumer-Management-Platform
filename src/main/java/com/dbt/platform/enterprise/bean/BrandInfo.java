package com.dbt.platform.enterprise.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu
 * @createTime 2016年4月21日 下午12:35:32
 * @description
 */

@SuppressWarnings("serial")
public class BrandInfo extends BasicProperties {

	private String brandKey;
	private String companyKey;
	private String brandName;
	private String brandDesc;
	private String brandLogo;

	public String getBrandKey() {
		return brandKey;
	}

	public void setBrandKey(String brandKey) {
		this.brandKey = brandKey;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandDesc() {
		return brandDesc;
	}

	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}

	public String getBrandLogo() {
		return brandLogo;
	}

	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}

}
