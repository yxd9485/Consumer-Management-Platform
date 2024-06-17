package com.dbt.platform.bidding.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.bidding.bean.VpsBiddingPeriods;

/**
  * 竞价活动期数Dao
 */

public interface IVpsBiddingPeriodsDao extends IBaseDao<VpsBiddingPeriods> {

	/**
	 * 期数列表
	 * @param queryMap
	 * @return
	 */
	public List<VpsBiddingPeriods> loadPeriodsList(Map<String, Object> queryMap);

	/**
	 * 期数列表 -- 条数
	 * @param queryMap
	 * @return
	 */
	public int countPeriodsList(Map<String, Object> queryMap);
	
	/**
	 * 通过期数KEY获取信息
	 *
	 * @return
	 */
	public VpsBiddingPeriods findById(String id);

}
