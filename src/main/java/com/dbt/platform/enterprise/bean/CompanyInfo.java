package com.dbt.platform.enterprise.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu
 * @createTime 2016年4月21日 下午12:33:21
 * @description
 */

@SuppressWarnings("serial")
public class CompanyInfo extends BasicProperties {

	/** 企业主键 **/
	private String companyKey;
	/** 企业名称 **/
	private String companyName;
	/** 企业短名称 **/
	private String shortName;
	/** 企业编码 **/
	private String companyDesc;
	
	// 扩展字段
	/** 企业负责人 **/
	private String companyLinkUser;
	/** 企业联系手机号 **/
	private String companyPhone;
	/** 企业邮箱 **/
	private String companyEmail;
	/** 企业合同日期 **/
	private String companyContractDdate;
	/** 企业登录手机号 **/
	private String companyLoginPhone;
	
	public String getCompanyLoginPhone() {
		return companyLoginPhone;
	}

	public void setCompanyLoginPhone(String companyLoginPhone) {
		this.companyLoginPhone = companyLoginPhone;
	}

	public String getCompanyLinkUser() {
		return companyLinkUser;
	}

	public void setCompanyLinkUser(String companyLinkUser) {
		this.companyLinkUser = companyLinkUser;
	}

	public String getCompanyPhone() {
		return companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyContractDdate() {
		return companyContractDdate;
	}

	public void setCompanyContractDdate(String companyContractDdate) {
		this.companyContractDdate = companyContractDdate;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
