package com.dbt.platform.autocode.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.autocode.bean.VpsVcodeBatchSerialNoQueryLog;
/**
 * 批次序号查询记录Dao
 *
 */
public interface IVpsVcodeBatchSerialNoQueryLogDao extends IBaseDao<VpsVcodeBatchSerialNoQueryLog>{

	/**
	 * 批次序号查询记录列表
	 * @param map keys:queryBean、pageInfo
	 * @return
	 */
	List<VpsVcodeBatchSerialNoQueryLog> queryForLst(Map<String, Object> map);

	/**
	 * 批次序号查询记录列表count
	 * @param map keys:queryBean
	 * @return
	 */
	int queryForCount(Map<String, Object> map);
	
	/**
	 * 依据二维码序号查询记录
	 * @param map keys: batchSerialNo
	 * @return
	 */
	public VpsVcodeBatchSerialNoQueryLog findByBatchSerialNo(Map<String, Object> map);
}
