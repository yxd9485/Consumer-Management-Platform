package com.dbt.platform.batchreward.dao;

import com.dbt.platform.batchreward.bean.VpsBatchRewardRecord;

import java.util.List;
import java.util.Map;

public interface BatchRewardDao {

    List<VpsBatchRewardRecord> queryForLst(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);

    void createBatchReward(VpsBatchRewardRecord batchRewardRecord);

    int queryCount(String dateQuery);
}
