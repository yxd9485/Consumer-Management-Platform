package com.dbt.risk.bean;
/**
 * 风控处理类
 * @author ty2zh
 *
 */
public class MethodGlueBean {
	private String glueId;
	private String methodInfoKey;
	private String glueSource;
	private String glueRmk;
	private String glueStatus;
	private String updateTime;
	
	public String getGlueId() {
		return glueId;
	}
	public void setGlueId(String glueId) {
		this.glueId = glueId;
	}
	public String getMethodInfoKey() {
		return methodInfoKey;
	}
	public void setMethodInfoKey(String methodInfoKey) {
		this.methodInfoKey = methodInfoKey;
	}
	public String getGlueSource() {
		return glueSource;
	}
	public void setGlueSource(String glueSource) {
		this.glueSource = glueSource;
	}
	public String getGlueRmk() {
		return glueRmk;
	}
	public void setGlueRmk(String glueRmk) {
		this.glueRmk = glueRmk;
	}
	public String getGlueStatus() {
		return glueStatus;
	}
	public void setGlueStatus(String glueStatus) {
		this.glueStatus = glueStatus;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
