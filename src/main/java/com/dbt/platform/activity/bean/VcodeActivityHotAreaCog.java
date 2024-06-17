/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:12:03 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 热区配置表
 */
@SuppressWarnings("serial")
public class VcodeActivityHotAreaCog extends BasicProperties{

	/**  主键. */
	private String hotAreaKey;
	
	/**  热区名称. */
	private String hotAreaName;
	
	/**  区域编码 */
	private String areaCode;
	
	/**  热区关键点集合 */
	private String coordinate;
	
	//-------------------扩展字段-------------------
	
	/**  区域名称 */
	private String areaName;
	
    public String getHotAreaKey() {
        return hotAreaKey;
    }

    
    public void setHotAreaKey(String hotAreaKey) {
        this.hotAreaKey = hotAreaKey;
    }

    
    public String getHotAreaName() {
        return hotAreaName;
    }

    
    public void setHotAreaName(String hotAreaName) {
        this.hotAreaName = hotAreaName;
    }

    
    public String getAreaCode() {
        return areaCode;
    }

    
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    
    public String getCoordinate() {
        return coordinate;
    }

    
    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    
    public String getAreaName() {
        return areaName;
    }

    
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
	
}
