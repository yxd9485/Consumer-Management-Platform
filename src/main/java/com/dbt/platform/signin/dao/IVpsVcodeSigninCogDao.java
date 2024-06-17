package com.dbt.platform.signin.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.signin.bean.VpsVcodeSigninCog;

/**
 * 签到活动配置表Dao
 */

public interface IVpsVcodeSigninCogDao extends IBaseDao<VpsVcodeSigninCog> {

	/**
	 * 活动列表
	 * @param queryMap
	 * @return
	 */
	public List<VpsVcodeSigninCog> loadActivityList(Map<String, Object> queryMap);

	/**
	 * 活动列表 -- 条数
	 * @param queryMap
	 * @return
	 */
	public int countActivityList(Map<String, Object> queryMap);

}
