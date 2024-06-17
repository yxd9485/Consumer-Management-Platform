package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityPerhundredCog;

/**
 * 逢百规则DAO
 * @author hanshimeng
 *
 */
public interface IVcodeActivityPerhundredCogDao extends IBaseDao<VcodeActivityPerhundredCog>{

	/**
	 * 获取列表List
	 * @param map
	 * @return
	 */
	List<VcodeActivityPerhundredCog> queryForList(Map<String, Object> map);

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
     * 获取该活动下逢百有效规则
     * @param map keys:vcodeActivityKey
     * @return
     */
    public VcodeActivityPerhundredCog findValidByActivityKey(Map<String, Object> map);
}
