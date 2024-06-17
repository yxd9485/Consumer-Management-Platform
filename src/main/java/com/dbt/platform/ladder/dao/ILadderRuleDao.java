package com.dbt.platform.ladder.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.ladder.bean.LadderRuleCog;

/**
 * @author RoyFu 
 * @createTime 2016年1月19日 下午5:45:30
 * @description 
 */

public interface ILadderRuleDao extends IBaseDao<LadderRuleCog> {
	
	public List<LadderRuleCog> queryForLst(Map<String, Object> map);
	public int queryForCount(Map<String, Object> map);
	public LadderRuleCog findByActivityKey(@Param("activityKey") String activityKey);
	
	
}
