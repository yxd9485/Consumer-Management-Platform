package com.dbt.platform.activate.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  一万一批次激活人员授权配置表Bean
 */
public class VpsVcodeBatchActivateCog extends BasicProperties {

    private static final long serialVersionUID = 7685905395551240335L;
    
    private String infoKey;
    private String openid;
    private String userName;
    private String phoneNum;
    private String factoryName;
    private String serverName;
    /** 用户权限：0 无权限，1 激活，2 查询检测， 3 激活+查询检测 **/
    private String userPrivilege;
    /** 用户状态：0 停用，1 待审核，2 已审核，3 驳回 **/
    private String userStatus;
    
    public VpsVcodeBatchActivateCog() {
    }
    
    public VpsVcodeBatchActivateCog(String infoKey, String openid, String userName, 
    		String phoneNum, String factoryName, String userPrivilege, String userStatus) {
    	this.infoKey = infoKey;
    	this.openid = openid;
    	this.userName = userName;
    	this.phoneNum = phoneNum;
    	this.factoryName = factoryName;
    	this.userPrivilege = userPrivilege;
    	this.userStatus = userStatus;
    }
    
    public VpsVcodeBatchActivateCog(String queryParam) {
        if (StringUtils.isNotBlank(queryParam)) {
            String[] paramAry = queryParam.split(",");
            this.userName = paramAry.length > 0 ? paramAry[0] : null;
            this.phoneNum = paramAry.length > 1 ? paramAry[1] : null;
        }
    }
    
    public String getUserPrivilege() {
		return userPrivilege;
	}

	public void setUserPrivilege(String userPrivilege) {
		this.userPrivilege = userPrivilege;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getFactoryName() {
        return factoryName;
    }
    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
