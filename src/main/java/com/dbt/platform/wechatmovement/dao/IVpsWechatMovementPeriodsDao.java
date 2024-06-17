package com.dbt.platform.wechatmovement.dao;

import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPeriods;

/**
 * 期数统计DAO
 * @author hanshimeng
 *
 */
public interface IVpsWechatMovementPeriodsDao extends IBaseDao<VpsWechatMovementPeriods>{

	/**
	 * 获取某期微信运动数据统计
	 * @param periodsKey
	 * @return
	 */
	VpsWechatMovementPeriods findWechatMovemenPeriods(String periodsKey);

	/**
	 * 更新达标信息
	 * @param periods
	 */
	void updateFinishInfo(VpsWechatMovementPeriods periods);

	/**
	 * 更新期表的规则数据
	 * @param map:periodsKey,currencyPrice,activityMagnification
	 */
	void updatePeriodsRule(Map<String, Object> map);

}
