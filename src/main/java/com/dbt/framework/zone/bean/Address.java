package com.dbt.framework.zone.bean;

import java.io.Serializable;

/**
 * 地理位置信息
 * @author:Jiquanwei<br>
 * @date:2016-06-06 上午11:50:29<br>
 * @version:1.0.0<br>
 * 
 */
public class Address implements Serializable{

	private static final long serialVersionUID = 181583465456443136L;
	
	private String province;
	
	private String city;
	private String county;
	private String provinceCode;
	private String cityCode;
	private String countyCode;
    private String latitude;
    private String longitude;
    private String geoHash;
    private String latitudeBaidu;
    private String longitudeBaidu;
    
    public Address() {
        super();
    }
    
    public Address(String province, String city, String county) {
        this.province = province;
        this.city = city;
        this.county = county;
    }

	public String getLatitudeBaidu() {
        return latitudeBaidu;
    }

    public void setLatitudeBaidu(String latitudeBaidu) {
        this.latitudeBaidu = latitudeBaidu;
    }

    public String getLongitudeBaidu() {
        return longitudeBaidu;
    }

    public void setLongitudeBaidu(String longitudeBaidu) {
        this.longitudeBaidu = longitudeBaidu;
    }

    public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }

}