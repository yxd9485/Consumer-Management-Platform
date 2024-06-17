package com.dbt.platform.activity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityMorescanCog;

import java.util.List;
import java.util.Map;

/**
 * 一码多扫规则DAO
 * @author hanshimeng
 *
 */
public interface IVcodeActivityMorescanCogDao extends IBaseDao<VcodeActivityMorescanCog>{

	/**
	 * 获取列表List
	 * @param map
	 * @return
	 */
	List<VcodeActivityMorescanCog> queryForList(Map<String, Object> map);

	/**
	 * 获取列表Count
	 * @param map
	 * @return
	 */
	int queryForCount(Map<String, Object> map);
	
	/**
	 * 删除记录
	 * @param map
	 */
	void deleteById(Map<String, Object> map);
	
	/**
	 * 获取该活动下一码多扫有效规则
	 * @param map keys:vcodeActivityKey
	 * @return
	 */
	public VcodeActivityMorescanCog findValidByActivityKey(Map<String, Object> map);

	/**
	 * 根据活动Key获取已配置的
	 *
	 */
	List<VcodeActivityMorescanCog> getByActivityKey(String activityKey);
}
