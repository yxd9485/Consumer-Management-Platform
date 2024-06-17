package com.dbt.platform.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.system.bean.SysRole;
import com.dbt.platform.system.dao.ISysRoleDao;

/**
 * @author RoyFu 
 * @createTime 2016年4月21日 上午9:34:49
 * @description 
 */
@Service
public class SysRoleService extends BaseService<SysRole> {

	@Autowired
	private ISysRoleDao iSysRoleDao;
	
	public List<SysRole> loadCurrentRoleByUser(String userKey){
		return iSysRoleDao.loadCurrentRoleByUser(userKey);
	}
}
