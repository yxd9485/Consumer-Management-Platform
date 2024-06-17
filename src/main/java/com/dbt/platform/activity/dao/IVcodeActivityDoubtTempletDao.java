package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityDoubtTemplet;

/**
 * 风控模板主表DAO
 */
public interface IVcodeActivityDoubtTempletDao extends IBaseDao<VcodeActivityDoubtTemplet> {

	/**
	 * 获取列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<VcodeActivityDoubtTemplet> queryForLst(Map<String, Object> map);

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
	 * 获取所有
     * @param map keys:status
	 * @return
	 */
	public List<VcodeActivityDoubtTemplet> queryForAll(Map<String, Object> map);

}
