package com.dbt.platform.wechatmovement.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementSigninRecord;

public interface IVpsWechatMovementSigninRecordDao extends IBaseDao<VpsWechatMovementSigninRecord>{

	/**
	 * 获取已完成周赛信息
	 * @param map:signWeek
	 * @param map
	 * @return
	 */
	public List<VpsWechatMovementSigninRecord> queryFinishSigninRecord(Map<String, Object> map);

	/**
	 * 查询个人周赛信息
	 * @param map：userKey,signWeek
	 * @return
	 */
	public VpsWechatMovementSigninRecord findSigninRecord(Map<String, Object> map);

	/**
	 * 批量写入
	 * @param list
	 */
	public void batchWrite(List<VpsWechatMovementSigninRecord> list);
	
	/**
	 * 批量更新
	 * @param updateUserList
	 */
	public void batchUpdate(List<VpsWechatMovementSigninRecord> updateUserList);
}
