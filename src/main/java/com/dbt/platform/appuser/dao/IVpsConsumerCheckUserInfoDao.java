package com.dbt.platform.appuser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.appuser.bean.VpsConsumerCheckUserInfo;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;

/**
 * 大奖核销人员DAO
 * @author hanshimeng
 *
 */
public interface IVpsConsumerCheckUserInfoDao extends IBaseDao<VpsConsumerCheckUserInfo>{

	/**
	 * 查询核销人员List
	 * @param map
	 * @return
	 */
	List<VpsConsumerCheckUserInfo> findCheckUserInfoList(Map<String, Object> map);

	/**
	 * 查询核销人员Count
	 * @param map
	 * @return
	 */
	int findCheckUserInfoCount(Map<String, Object> map);

	/**
	 * 更新核销人员状态
	 * @param map
	 */
	void updateCheckUserStatus(Map<String, Object> map);
	
	/**
	 * 删除核销人员
	 * @param infoKey
	 */
	void deleteCheckUser(@Param("infoKey") String infoKey);

}
