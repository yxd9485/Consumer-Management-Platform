package com.dbt.framework.log.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.framework.log.bean.VpsOperationLog;

/**
 * 操作日志Dao
 * @author hanshimeng
 *
 */
public interface VpsOperationLogDao extends IBaseDao<VpsOperationLog>{

	/**
	 * 新增操作日志
	 * @param log
	 */
	void saveLog(VpsOperationLog log);

	/**
	 * 查询日志列表
	 * @param map
	 * @return
	 */
	List<VpsOperationLog> queryVpsOperationLogList(Map<String, Object> map);

	/**
	 * 查询日志count
	 * @param map
	 * @return
	 */
	int queryVpsOperationLogCount(Map<String, Object> map);

}
