package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleTemplet;

/**
 * 活动规则模板主表DAO
 */
public interface IVcodeActivityRebateRuleTempletDao extends IBaseDao<VcodeActivityRebateRuleTemplet> {

	/**
	 * 获取列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<VcodeActivityRebateRuleTemplet> queryForLst(Map<String, Object> map);

	/**
	 * 获取列表记录总个数
     * @param map keys:queryBean
	 */
	public Integer queryForCount(Map<String, Object> map);
	
	/**
	 * 逻辑删除
	 * @param map keys:infoKey、optUserKey
	 */
	public void deleteById(Map<String, Object> map);
	
	/**
	 * 获取所有有效规则模板
	 */
	public List<VcodeActivityRebateRuleTemplet> queryAllValid();

}
