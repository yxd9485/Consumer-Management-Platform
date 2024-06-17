package com.dbt.framework.zone.bean;

import com.dbt.framework.base.bean.BaseBean;

/**
 * 文件名: SysAreaM.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 修改人: FuZhongYue<br>
 * 修改时间：2014-03-17 15:40:45<br>
 * 修改内容：新增<br>
 */
public class SysAreaM extends BaseBean {

	private static final long serialVersionUID = 8142375282363533824L;
	/* 区域编号 */
	private String areaCode;
	/* 区域名称 */
	private String areaName;
	/* 邮政编码 */
	private String postCode;
	/* 长途号编码 */
	private String telCode;
	/* 父编码 */
	private String parentCode;
	/* 区域等级 */
	private String level;
	// 显示区域全称
	private String zoneFullName;
	
	public SysAreaM() {
	    super();
	}
	
	public SysAreaM (String areaCode, String areaName) {
	    this.areaCode = areaCode;
	    this.areaName = areaName;
	}
    
    public SysAreaM(String areaCode, String areaName, String parentCode) {
        this.areaCode = areaCode;
        this.areaName = areaName;
        this.parentCode = parentCode;
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

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getTelCode() {
		return telCode;
	}

	public void setTelCode(String telCode) {
		this.telCode = telCode;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getZoneFullName() {
		return zoneFullName;
	}

	public void setZoneFullName(String zoneFullName) {
		this.zoneFullName = zoneFullName;
	}

}
