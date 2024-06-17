package com.dbt.framework.base.dao;

import java.util.Map;

import com.dbt.framework.base.bean.CommonBean;

public interface ICommonDao extends IBaseDao<CommonBean>{

	/**
	 * 生成业务编号
	 * @param map:tableName
	 * @return
	 */
	int getCount(Map<String, Object> map);

	/**
	 * 检验业务名称是否重复
	 * @param map:tableName,columnName,bussionName
	 * @return
	 */
	int queryCountForName(Map<String, Object> map);
	
}
