package com.dbt.framework.securityauth;

import java.util.List;

import com.dbt.platform.system.bean.SysFunction;
import com.dbt.platform.system.bean.SysRole;
import com.dbt.platform.system.bean.SysUserBasis;

/**
* 文件名：SecurityContext.java<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
* 描述: 安全Context（包含当前登录用户、角色、组织、除菜单以外的权限信息)<br>
* 修改人: 谷长鹏<br>
* 修改时间：2014-06-20<br>
* 修改内容：新增<br>
*/
public class SecurityContext {
	
	private SysUserBasis sysUserBasis = null;
	
	private List<SysRole> sysRoleList = null;
	
	private List<String> permissionList = null;
	
	private List<SysFunction> functionList = null;

	public SysUserBasis getSysUserBasis() {
		return sysUserBasis;
	}

	public void setSysUserBasis(SysUserBasis sysUserBasis) {
		this.sysUserBasis = sysUserBasis;
	}

	public List<SysRole> getSysRoleList() {
		return sysRoleList;
	}

	public void setSysRoleList(List<SysRole> sysRoleList) {
		this.sysRoleList = sysRoleList;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}

	public List<SysFunction> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<SysFunction> functionList) {
		this.functionList = functionList;
	}
	
	
	
}
