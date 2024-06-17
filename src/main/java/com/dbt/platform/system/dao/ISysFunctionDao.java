package com.dbt.platform.system.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.system.bean.SysFunction;

/**
 * @author RoyFu 
 * @createTime 2016年4月20日 上午10:41:10
 * @description 
 */

public interface ISysFunctionDao extends IBaseDao<SysFunction> {

	public List<SysFunction> loadFunctionListByUserKey(Map<String, Object> map);

	public List<String> loadCurrentFunctionByUser(String userKey);

}
