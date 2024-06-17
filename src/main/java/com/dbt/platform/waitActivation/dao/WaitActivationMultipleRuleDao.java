package com.dbt.platform.waitActivation.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationMultipleRule;

import javax.annotation.Resource;

@Resource
public interface WaitActivationMultipleRuleDao extends IBaseDao<VpsWaitActivationMultipleRule> {

    /**
     * 创建天降红包倍数中出规则
     * @param multipleRule
     */
    void createMultipleRule(VpsWaitActivationMultipleRule multipleRule);


    /**
     * 修改倍数中出规则
     * @param multipleRule
     */
    void updateMultipleRule(VpsWaitActivationMultipleRule multipleRule);

    /**
     * 根据infokey查询倍数中出规则
     * @param infoKey
     * @return
     */
    VpsWaitActivationMultipleRule findByInfoKey(String infoKey);
}
