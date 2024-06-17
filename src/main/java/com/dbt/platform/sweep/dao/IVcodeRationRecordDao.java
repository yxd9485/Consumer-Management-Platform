package com.dbt.platform.sweep.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.sweep.bean.VpsVcodeRationRecord;

/**
 * 提现记录Dao接口
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2017年12月1日 </br>
 */

public interface IVcodeRationRecordDao extends IBaseDao<VpsVcodeRationRecord> {

	/**
	 * 提现列表List
	 * @param queryBean</br>
	 * @param pageInfo</br>  
	 * @return List<VpsVcodeRationRecord> </br>
	 */
	List<VpsVcodeRationRecord> queryList(Map<String, Object> queryMap);

	/**
	 * 提现列表Count
	 * @param map</br>
	 * @return int </br>
	 */
	int queryCount(Map<String, Object> map);
	
}
