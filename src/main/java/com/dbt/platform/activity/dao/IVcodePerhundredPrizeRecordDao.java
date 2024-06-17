package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodePerhundredPrizeRecord;

/**
 * 逢百中奖纪录DAO
 * @author hanshimeng
 *
 */
public interface IVcodePerhundredPrizeRecordDao extends IBaseDao<VcodePerhundredPrizeRecord>{

	/**
	 * 获取列表List
	 * @param map
	 * @return
	 */
	List<VcodePerhundredPrizeRecord> queryForList(Map<String, Object> map);

	/**
	 * 获取列表Count
	 * @param map
	 * @return
	 */
	int queryForCount(Map<String, Object> map);
}
