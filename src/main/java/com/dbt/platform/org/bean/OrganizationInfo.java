package com.dbt.platform.org.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 	青啤组织机构Bean
 * @author hanshimeng
 *
 */
public class OrganizationInfo extends BasicProperties {

    private static final long serialVersionUID = 1L;
    
    private String infoKey;
    private String province;
    private String city;
    private String county;
    private String bigRegionName;
    private String secondaryName;
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getBigRegionName() {
		return bigRegionName;
	}
	public void setBigRegionName(String bigRegionName) {
		this.bigRegionName = bigRegionName;
	}
	public String getSecondaryName() {
		return secondaryName;
	}
	public void setSecondaryName(String secondaryName) {
		this.secondaryName = secondaryName;
	}
}
