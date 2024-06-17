package com.dbt.platform.system.bean;

import java.util.ArrayList;
import java.util.List;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu 
 * @createTime 2016年4月20日 上午10:14:25
 * @description 
 */

@SuppressWarnings("serial")
public class SysFunction extends BasicProperties {

	private String functionKey;
	/** 功能名称 */
	private String functionName;
	/** 功能描述 */
	private String functionDesc;
	/** 父功能主键 */
	private String parentKey;
	/** 功能URL */
	private String startUrl;
	/** 功能类型 */
	private String functionType;
	/** 功能层级 */
	private String fuctionLevel;
	private String fuctionCode;
	/** 功能图标 */
	private String menuIcon;
	/** 功能状态 */
	private String functionStatus;
    /** 不加IP和端口的地址 */
	private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private List<SysFunction> subFunctionList = new ArrayList<SysFunction>();

	public String getFunctionKey() {
		return functionKey;
	}

	public void setFunctionKey(String functionKey) {
		this.functionKey = functionKey;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getFunctionDesc() {
		return functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	public String getFuctionLevel() {
		return fuctionLevel;
	}

	public void setFuctionLevel(String fuctionLevel) {
		this.fuctionLevel = fuctionLevel;
	}

	public String getFuctionCode() {
        return fuctionCode;
    }

    public void setFuctionCode(String fuctionCode) {
        this.fuctionCode = fuctionCode;
    }

    public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public String getFunctionStatus() {
		return functionStatus;
	}

	public void setFunctionStatus(String functionStatus) {
		this.functionStatus = functionStatus;
	}

	public List<SysFunction> getSubFunctionList() {
		return subFunctionList;
	}

	public void setSubFunctionList(List<SysFunction> subFunctionList) {
		this.subFunctionList = subFunctionList;
	}
	
}
