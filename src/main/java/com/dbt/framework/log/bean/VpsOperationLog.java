package com.dbt.framework.log.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 操作日志Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsOperationLog extends BasicProperties{

	/** 主键 **/
	private String infoKey;
	/** 菜单标识**/
	private String menuFlag;
	/** 操作类型：0登录；1增加；2修改；3删除 **/
	private String operationType;
	/** 操作内容（新增和删除只记录主键KEY，修改需存储当前内容的json串） **/
	private String operationContent;
	/** 操作描述 **/
	private String operationDescription;
	/** 登录名称 **/
	private String loginName;
	/** 登录手机号 **/
	private String phoneNumber;
	

	/** 开始时间 **/
	private String startDate;
	/** 结束时间 **/
	private String endDate;
	
	public VpsOperationLog(){}
	public VpsOperationLog(String menuFlag, String operationType, 
			String operationContent, String operationDescription, String loginName, String phoneNumber) {
		this.menuFlag = menuFlag;
		this.operationType = operationType;
		this.operationContent = operationContent;
		this.operationDescription = operationDescription;
		this.loginName = loginName;
		this.phoneNumber = phoneNumber;
	}
	
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getMenuFlag() {
		return menuFlag;
	}
	public void setMenuFlag(String menuFlag) {
		this.menuFlag = menuFlag;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperationContent() {
		return operationContent;
	}
	public void setOperationContent(String operationContent) {
		this.operationContent = operationContent;
	}
	public String getOperationDescription() {
		return operationDescription;
	}
	public void setOperationDescription(String operationDescription) {
		this.operationDescription = operationDescription;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
