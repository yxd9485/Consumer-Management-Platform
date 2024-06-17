package com.dbt.platform.integral.dao;

import com.dbt.platform.integral.bean.VpsVipDailyTaskCog;

import java.util.List;
import java.util.Map;

public interface VipDailyDao {
    List<VpsVipDailyTaskCog> queryForLst(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);

    void createVipDailyTask(VpsVipDailyTaskCog vpsVipDailyTaskCog);

    int queryCountForTaskType(String taskType);

    VpsVipDailyTaskCog findById(String infoKey);

    void updateVipDailyTask(VpsVipDailyTaskCog vpsVipDailyTaskCog);
}
