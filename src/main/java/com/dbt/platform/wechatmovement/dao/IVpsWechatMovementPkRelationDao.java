package com.dbt.platform.wechatmovement.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPkRelation;
/**
 * 用户参与微信运动PK关系Dao
 * @author hanshimeng
 *
 */
public interface IVpsWechatMovementPkRelationDao extends IBaseDao<VpsWechatMovementPkRelation>{

	int findCountByPeriodsKey(Map<String, Object> paramsMap);

	void createBatch(Map<String, Object> paramsMap);

	List<VpsWechatMovementPkRelation> queryRelation(Map<String, Object> paramsMap);

}
