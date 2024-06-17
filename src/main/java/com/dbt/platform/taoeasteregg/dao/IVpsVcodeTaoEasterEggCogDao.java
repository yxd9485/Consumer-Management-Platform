package com.dbt.platform.taoeasteregg.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.taoeasteregg.bean.VpsVcodeTaoEasterEggCog;


public interface IVpsVcodeTaoEasterEggCogDao extends IBaseDao<VpsVcodeTaoEasterEggCog> {
	public List<VpsVcodeTaoEasterEggCog> queryForLst(Map<String, Object> map);
	public int queryForCount(Map<String, Object> map);
	public VpsVcodeTaoEasterEggCog findByActivityKey(@Param("activityKey") String activityKey);
	
	/**
	 * 配置活动冲突校验
	 * @param map keys:infoKey、activityKeyAry、startDate、endDate
	 * @return
	 */
	public List<VpsVcodeTaoEasterEggCog> queryForConflictCheck(Map<String, Object> map);
}
