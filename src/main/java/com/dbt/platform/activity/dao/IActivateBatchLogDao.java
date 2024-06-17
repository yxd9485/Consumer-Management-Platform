package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VpsActivateBatchLog;

/**
 * 批次激活日志dao
 * @author hanshimeng
 *
 */
public interface IActivateBatchLogDao extends IBaseDao<VpsActivateBatchLog>{

	/**
	 * 查询激活日志List
	 * @param map:batchKey
	 * @return
	 */
	public List<VpsActivateBatchLog> queryList(Map<String, Object> map);
}
