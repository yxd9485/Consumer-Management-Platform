package com.dbt.platform.activity.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class VcodeActivityHotAreaForAreaCode {
    private String areaCode;
    private String areaName;
    
    /** 扩展字段 **/
    private String hotAreaName;
    private String provinceCode;
	private String cityCode;
	private String countyCode;
    private List<VcodeActivityHotAreaCog> hotAreaCogList;
    
    public VcodeActivityHotAreaForAreaCode() {
        super();
        this.hotAreaCogList = new ArrayList<>();
    }
    
    public VcodeActivityHotAreaForAreaCode(String areaCode, 
            String areaName, VcodeActivityHotAreaCog hotAreaCog) {
        this.areaCode = areaCode;
        this.areaName = areaName;
        if (hotAreaCogList == null) {
            hotAreaCogList = new ArrayList<>();
        }
        hotAreaCogList.add(hotAreaCog);
    }
    
    public VcodeActivityHotAreaForAreaCode(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.hotAreaName = paramAry.length > 0 ? paramAry[0] : "";
        this.provinceCode = paramAry.length > 1 ? paramAry[1] : "";
        this.cityCode = paramAry.length > 2 ? paramAry[2] : "";
        this.countyCode = paramAry.length > 3 ? paramAry[3] : "";
	}
    
    public String getHotAreaName() {
		return hotAreaName;
	}

	public void setHotAreaName(String hotAreaName) {
		this.hotAreaName = hotAreaName;
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

	public String getAreaCode() {
        return areaCode;
    }
    
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    
    public String getAreaName() {
        return areaName;
    }
    
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    
    public List<VcodeActivityHotAreaCog> getHotAreaCogList() {
        return hotAreaCogList;
    }

    public void setHotAreaCogList(List<VcodeActivityHotAreaCog> hotAreaCogList) {
        this.hotAreaCogList = hotAreaCogList;
    }
    
}
