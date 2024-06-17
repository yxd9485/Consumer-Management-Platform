package com.dbt.platform.codefactory.bean;

import org.springframework.util.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

public class VpsWinery extends BasicProperties {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String wineryName;
	private String wineryShort;
	private String projectServerName;
	private String projectServer;
	private String deleteFlag;
	
	public VpsWinery(){
		
	}
	
	public VpsWinery(String queryParam) {
		if (!StringUtils.isEmpty(queryParam)) {
			String[] values = queryParam.split(",");
			this.wineryName = values.length > 0 ? values[0] : null;
			this.projectServer = values.length > 1 ? values[1] : null;
		}
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWineryName() {
		return wineryName;
	}
	public void setWineryName(String wineryName) {
		this.wineryName = wineryName;
	}

	public String getWineryShort() {
		return wineryShort;
	}

	public void setWineryShort(String wineryShort) {
		this.wineryShort = wineryShort;
	}

	public String getProjectServerName() {
		return projectServerName;
	}
	public void setProjectServerName(String projectServerName) {
		this.projectServerName = projectServerName;
	}
	public String getProjectServer() {
		return projectServer;
	}
	public void setProjectServer(String projectServer) {
		this.projectServer = projectServer;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	
}
    
