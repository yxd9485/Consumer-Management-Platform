package com.dbt.platform.batchreward.dao;


import com.dbt.platform.batchreward.bean.VpsBatchRewardRecordDetails;

import java.util.List;

public interface BatchRewardDetailsDao {

    void batchCreate(List<VpsBatchRewardRecordDetails> batchRewardRecordDetailsList);
}
