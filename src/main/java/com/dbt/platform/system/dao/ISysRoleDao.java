package com.dbt.platform.system.dao;

import java.util.List;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.system.bean.SysRole;

/**
 * @author RoyFu 
 * @createTime 2016年4月20日 上午10:41:10
 * @description 
 */

public interface ISysRoleDao extends IBaseDao<SysRole> {

	public List<SysRole> loadCurrentRoleByUser(String userKey);

}
