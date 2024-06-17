package com.dbt.platform.oyshoVote.dao;

import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.oyshoVote.bean.VpsVoteOyshoInfo;

public interface IVpsVoteOyshoInfoDao extends IBaseDao<VpsVoteOyshoInfo> {

	void deleteByUserKey(String userKey);

	int findAutoId(Map<String, Object> params);

}
