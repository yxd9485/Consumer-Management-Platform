package com.dbt.framework.datadic.bean;

import java.io.Serializable;

public class ServerInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String itemValue;
	private String serverName;
	private String projectServerName;
	private String serverProvince;
	private String appId;
	private String redisServer;
	private String serverJdbc;
	private String serverU;
	private String serverP;
	private int serverC;
	// 数据源状态：0正常
	private String serverStatus;
	private String brandCode;
	
	public int getServerC() {
		return serverC;
	}
	public void setServerC(int serverC) {
		this.serverC = serverC;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getServerProvince() {
		return serverProvince;
	}
	public void setServerProvince(String serverProvince) {
		this.serverProvince = serverProvince;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getRedisServer() {
		return redisServer;
	}
	public void setRedisServer(String redisServer) {
		this.redisServer = redisServer;
	}
	public String getServerJdbc() {
		return serverJdbc;
	}
	public void setServerJdbc(String serverJdbc) {
		this.serverJdbc = serverJdbc;
	}
	public String getServerU() {
		return serverU;
	}
	public void setServerU(String serverU) {
		this.serverU = serverU;
	}
	public String getServerP() {
		return serverP;
	}
	public void setServerP(String serverP) {
		this.serverP = serverP;
	}
	public String getProjectServerName() {
		return projectServerName;
	}
	public void setProjectServerName(String projectServerName) {
		this.projectServerName = projectServerName;
	}
	public String getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(String serverStatus) {
		this.serverStatus = serverStatus;
	}
    public String getBrandCode() {
        return brandCode;
    }
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
}
