package com.dbt.platform.commonrule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.commonrule.bean.CommonRule;
import com.dbt.platform.commonrule.dao.ICommonRuleDao;

/**
 * 通用规则Service
 * @author hanshimeng
 *
 * @param <T>
 */
@Service
public class CommonRuleService extends BaseService<CommonRule>{

	@Autowired
	private ICommonRuleDao commonRuleDao;
	
	/**
	 * 新增
	 * @param commonRule
	 */
	public void create(CommonRule commonRule) {
		commonRuleDao.create(commonRule);	
	}
	
	/**
	 * 新增或编辑
	 * @param commonRule
	 * @throws Exception 
	 */
	public void createOrUpdate(CommonRule commonRule) throws Exception {
		CommonRule dbCommonRule = commonRuleDao.findByRuleType(commonRule.getRuleType());
		if(null == dbCommonRule){
			commonRuleDao.create(commonRule);	
		}else{
			commonRule.setInfoKey(dbCommonRule.getInfoKey());
			commonRuleDao.update(commonRule);
			
			// 删除缓存
			CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
	                .KEY_COMMON_RULE_TYPE + Constant.DBTSPLIT + commonRule.getRuleType());
		}
	}
	
	/**
	 * 查询（根据规则类型：1.首扫规则）
	 * @param ruleType
	 * @param T
	 * @return
	 */
	public <T> T findByRuleType(String ruleType, Class<T> T){
		CommonRule commonRule = commonRuleDao.findByRuleType(ruleType);
		if(null != commonRule){
			return JSONObject.parseObject(commonRule.getRuleValue(), T);
		}
		return  null;
	}
}
