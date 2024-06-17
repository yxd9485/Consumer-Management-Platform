package com.dbt.platform.commonrule.dao;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.commonrule.bean.CommonRule;

/**
 * 通用规则DAO
 * @author hanshimeng
 *
 * @param <T>
 */
public interface ICommonRuleDao extends IBaseDao<CommonRule>{

	public CommonRule findByRuleType(@Param("ruleType") String ruleType);

}
