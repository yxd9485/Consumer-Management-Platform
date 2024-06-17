package com.dbt.platform.wechatmovement.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPkApply;
/**
 * 用户参与微信运动PK报名Dao
 * @author hanshimeng
 *
 */
public interface IVpsWechatMovementPkApplyDao extends IBaseDao<VpsWechatMovementPkApply>{

	List<VpsWechatMovementPkApply> queryWechatMovemenPkApply(Map<String, Object> paramsMap);

	VpsWechatMovementPkApply findOneByPeriodsKey(Map<String, Object> paramsMap);

	void updateBatch(Map<String, Object> paramsMap);

}
