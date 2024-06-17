package com.dbt.platform.oyshoVote.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.oyshoVote.bean.VpsVoteOyshoRecord;

public interface IVpsVoteOyshoRecordDao extends IBaseDao<VpsVoteOyshoRecord> {

	void deleteByUserKey(String userKey);

}
