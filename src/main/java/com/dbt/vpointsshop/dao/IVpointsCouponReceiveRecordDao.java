package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpointsCouponReceiveRecord;

public interface IVpointsCouponReceiveRecordDao extends IBaseDao<VpointsCouponReceiveRecord> {

	List<VpointsCouponReceiveRecord> queryReceiveRecordList(Map<String, Object> map);

	int countReceiveRecordList(Map<String, Object> map);

	/**
	 * 查询要提醒的记录
	 * 
	 * @param map
	 * @return
	 */
	public List<VpointsCouponReceiveRecord> queryExpireRemind(Map<String, Object> map);
	
	/**
	 * 更新提醒标志
	 * 
	 * @param map
	 */
	public void updateExpireRemindFlag(Map<String, Object> map);

	void addReceive(VpointsCouponReceiveRecord receiveRecord);
}
