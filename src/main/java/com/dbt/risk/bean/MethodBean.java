package com.dbt.risk.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

public class MethodBean extends BasicProperties {
	private static final long serialVersionUID = 1L;
	private String infoKey;
	private String methodName;//接口地址
	private String methodDesc;//接口描述
	private String methodStatus;//接口状态 0执行中 1已暂停
	private String logStatus="0";//是否留存请求日志 0留存 1不留存
	private int limitNum=0;//是否限流 大于0限流
	private int userLimitNum=0;//是否用户并发控制秒数 大于0控制
	private String isBusinessFlag="0";//是否业务并发标识：默认1否，0是
	private String userCheckStatus="0";//是否校验用户信息 0校验 1不校验
	private String userBlackCheck="1";//黑名单用户是否可访问 0可以 1不可以
	private String userSuspiciousCheck="0";//可疑用户是否可访问 0可以 1不可以
	private String requestRmk;//请求摘要
	private String requestRmkMsg;//请求摘要描述
	private String responseRmk;//响应摘要
	private String responseRmkMsg;//响应摘要描述
	private String handleType;//接口处理类型 0 无 1 bean类型 2glue类型
	private String handleServiceName;//接口风控处理类
	private String handleGlueId;//接口风控处理类id
	public MethodBean() {}
	public MethodBean(String queryParam) {
		if (!StringUtils.isEmpty(queryParam)) {
			queryParam = trickParam(queryParam);
			String[] params = queryParam.split(",");
			this.methodName = params.length > 0 ? !StringUtils.isEmpty(params[0]) ? params[0]: null : null;
			this.methodDesc = params.length > 1 ? !StringUtils.isEmpty(params[1]) ? params[1]: null : null;
		}
	}
	
	public String getMethodStatus() {
		return methodStatus;
	}
	public void setMethodStatus(String methodStatus) {
		this.methodStatus = methodStatus;
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodDesc() {
		return methodDesc;
	}
	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
	public String getLogStatus() {
		return logStatus;
	}
	public void setLogStatus(String logStatus) {
		this.logStatus = logStatus;
	}
	public int getLimitNum() {
		return limitNum;
	}
	public void setLimitNum(int limitNum) {
		this.limitNum = limitNum;
	}
	public int getUserLimitNum() {
		return userLimitNum;
	}
	public void setUserLimitNum(int userLimitNum) {
		this.userLimitNum = userLimitNum;
	}
	public String getUserCheckStatus() {
		return userCheckStatus;
	}
	public void setUserCheckStatus(String userCheckStatus) {
		this.userCheckStatus = userCheckStatus;
	}
	public String getUserBlackCheck() {
		return userBlackCheck;
	}
	public void setUserBlackCheck(String userBlackCheck) {
		this.userBlackCheck = userBlackCheck;
	}
	public String getUserSuspiciousCheck() {
		return userSuspiciousCheck;
	}
	public void setUserSuspiciousCheck(String userSuspiciousCheck) {
		this.userSuspiciousCheck = userSuspiciousCheck;
	}
	public String getRequestRmk() {
		return requestRmk;
	}
	public void setRequestRmk(String requestRmk) {
		this.requestRmk = requestRmk;
	}
	public String getResponseRmk() {
		return responseRmk;
	}
	public void setResponseRmk(String responseRmk) {
		this.responseRmk = responseRmk;
	}
	public String getHandleType() {
		return handleType;
	}
	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}
	public String getHandleServiceName() {
		return handleServiceName;
	}
	public void setHandleServiceName(String handleServiceName) {
		this.handleServiceName = handleServiceName;
	}
	public String getHandleGlueId() {
		return handleGlueId;
	}
	public void setHandleGlueId(String handleGlueId) {
		this.handleGlueId = handleGlueId;
	}
	public String getIsBusinessFlag() {
		return isBusinessFlag;
	}
	public void setIsBusinessFlag(String isBusinessFlag) {
		this.isBusinessFlag = isBusinessFlag;
	}
	public String getRequestRmkMsg() {
		return requestRmkMsg;
	}
	public void setRequestRmkMsg(String requestRmkMsg) {
		this.requestRmkMsg = requestRmkMsg;
	}
	public String getResponseRmkMsg() {
		return responseRmkMsg;
	}
	public void setResponseRmkMsg(String responseRmkMsg) {
		this.responseRmkMsg = responseRmkMsg;
	}
}
