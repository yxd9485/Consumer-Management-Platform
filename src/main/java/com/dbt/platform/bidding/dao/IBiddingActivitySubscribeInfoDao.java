package com.dbt.platform.bidding.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.bidding.bean.BiddingActivitySubscribeInfo;

/**
 * 竞价活动订阅Dao
 * @author Administrator
 *
 */
public interface IBiddingActivitySubscribeInfoDao extends IBaseDao<BiddingActivitySubscribeInfo>{

	/**
	 * 查询有效活动
	 * @param map
	 * @return
	 */
	List<BiddingActivitySubscribeInfo> queryValidBiddingList(Map<String, Object> map);

	/**
	 * 查询消息list
	 * @param map
	 * @return
	 */
	List<BiddingActivitySubscribeInfo> querySubscribeInfoList(Map<String, Object> map);

	/**
	 * 修改订阅消息状态
	 * @param map
	 */
	void updateBiddingRemindFlagForSend(Map<String, Object> map);

}
