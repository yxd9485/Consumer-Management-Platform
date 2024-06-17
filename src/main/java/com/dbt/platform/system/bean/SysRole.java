package com.dbt.platform.system.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu
 * @createTime 2016年4月20日 上午10:14:06
 * @description
 */

@SuppressWarnings("serial")
public class SysRole extends BasicProperties {

	private String roleKey;
	/** 角色名称 */
	private String roleName;
	/** 角色类型 */
	private String roleType;
	/** 状态 */
	private String roleStatus;

	public String getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(String roleKey) {
		this.roleKey = roleKey;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

}
