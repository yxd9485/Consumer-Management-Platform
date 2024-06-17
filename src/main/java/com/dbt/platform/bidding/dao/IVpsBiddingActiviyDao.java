package com.dbt.platform.bidding.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.bidding.bean.VpsBiddingActiviy;

/**
  * 竞价活动配置表Dao
 */

public interface IVpsBiddingActiviyDao extends IBaseDao<VpsBiddingActiviy> {

	/**
	 * 活动列表
	 * @param queryMap
	 * @return
	 */
	public List<VpsBiddingActiviy> loadActivityList(Map<String, Object> queryMap);

	/**
	 * 活动列表 -- 条数
	 * @param queryMap
	 * @return
	 */
	public int countActivityList(Map<String, Object> queryMap);
	
	/**
	 * 通过活动KEY获取活动信息
	 *
	 * @return
	 */
	public VpsBiddingActiviy findById(String id);

	/**
	 * 查询有效竞价活动
	 * @param map
	 * @return
	 */
	public List<VpsBiddingActiviy> queryValidBiddingList(Map<String, Object> map);

}
