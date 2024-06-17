package com.dbt.platform.doubleprize.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.doubleprize.bean.VpsVcodeDoublePrizeCog;

/**
 * 一码双奖活动配置表Dao
 */

public interface IVpsVcodeDoublePrizeCogDao extends IBaseDao<VpsVcodeDoublePrizeCog> {

	/**
	 * 活动列表
	 * @param queryMap
	 * @return
	 */
	public List<VpsVcodeDoublePrizeCog> loadActivityList(Map<String, Object> queryMap);

	/**
	 * 活动列表 -- 条数
	 * @param queryMap
	 * @return
	 */
	public int countActivityList(Map<String, Object> queryMap);
	
	/**
	 * filter:第一步，生成中间表数据
	 * 
	 * @param map keys:tableSuffix、activityKey、startDate、endDate、filterSkuKeyAry、areaNameLst
	 */
	public void filterPacks(Map<String, Object> map);
	
	/**
	 * filter:第二步，给符合条件的用户打上一码双奖活动标签
	 * 
	 * @param map keys:activityKey、filterSKuTotal、doubtStatusFlag
	 */
	public void filterUser(Map<String, Object> map);
	
	/**
	 * filter:第三步，返回一码双奖活动能参与的人数
	 * 
	 * @param map keys:activityKey
	 * @return
	 */
	public int filterUserNum(Map<String, Object> map);
	
	/**
	 * filter:第四步，删除临时数据 
	 * 
	 * @param map keys:activityKey
	 * @return
	 */
	public int filterDelete(Map<String, Object> map);
	
	/**
	 * filter:清除用户的活动标签
	 * 
	 * @param map keys:activityKey
	 * @return
	 */
	public int filterClearUser(Map<String, Object> map);
	
	/**
	 * 获取所有已结束且未清除标签的活动
	 * 
	 * @return
	 */
	public List<VpsVcodeDoublePrizeCog> queryAllJobActivity();
	
	/**
	 * 获取所有有效的活动
	 * 
	 * @return
	 */
	public List<VpsVcodeDoublePrizeCog> queryAllValidActivity();

	/**
	 * 通过活动KEY获取活动信息
	 *
	 * @return
	 */
	public VpsVcodeDoublePrizeCog findById(String id);

}
