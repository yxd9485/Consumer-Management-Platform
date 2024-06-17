package com.dbt.platform.doubtuser.bean;

/**
 * @author hanshimeng 
 * @createTime 2016年4月21日 下午5:44:15
 * @description
 */

public class VcodeBaseCog {

	private String deleteFlag;
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;

	public void setData(String currentTime, String currentUser) {
		this.createTime = currentTime;
		this.createUser = currentUser;
		this.updateTime = currentTime;
		this.updateUser = currentUser;
	}
	
	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

}
