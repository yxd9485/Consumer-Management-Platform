package com.dbt.platform.wechatmovement.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementUserDetail;
/**
 * 用户参与微信运动详情Dao
 * @author hanshimeng
 *
 */
public interface IVpsWechatMovementUserDetailDao extends IBaseDao<VpsWechatMovementUserDetail>{

	/**
	 * 更新用户当天步数
	 * @param map
	 */
	public void updateUserStep(Map<String, Object> map);

	/**
	 * 根据期数查询参与运动数据List
	 * @param periodsKey
	 * @return
	 */
	public List<VpsWechatMovementUserDetail> queryWechatMovemen(Map<String, Object> map);

	/**
	 * 查询用户7天达标情况
	 * @param map
	 */
	public List<VpsWechatMovementUserDetail> queryUser7DayEntryDetail(Map<String, Object> map);

	/**
	 * 根据期数查询参与用户List
	 * @param periodsKey
	 * @param tableIndex
	 * @param isFinish
	 * @return
	 */
	public List<VpsWechatMovementUserDetail> queryWechatMovemenUserInfo(Map<String, Object> map);

	/**
	 * 更新已达标记录
	 * @param map
	 */
	public void updateFinishDetailByMap(Map<String, Object> map);

	/**
	 * 查询已达标的数据List
	 * @param map
	 * @return
	 */
	public List<VpsWechatMovementUserDetail> queryFinishWechatMovemenList(Map<String, Object> map);

}
