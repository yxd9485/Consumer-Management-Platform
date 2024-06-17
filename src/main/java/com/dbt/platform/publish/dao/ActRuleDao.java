package com.dbt.platform.publish.dao;

import java.util.List;
import java.util.Map;

import com.dbt.platform.publish.bean.VpsActRule;

public interface ActRuleDao {

	List<VpsActRule> queryForLst(Map<String, Object> queryMap);

	int queryForCount(Map<String, Object> queryMap);

	void updateVpsActRule(VpsActRule vpsActRule);

	VpsActRule findById(String infoKey);

    void addActRule(VpsActRule vpsActRule);

    List<String> findVcodeActivityKeys();
}
