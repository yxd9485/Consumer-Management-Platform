package com.dbt.platform.bottlecap.dao;

import com.dbt.platform.bottlecap.bean.VpsVcodeBottlecapActivityCogInfo;

import java.util.List;
import java.util.Map;

public interface BottleCapDao {
    List<VpsVcodeBottlecapActivityCogInfo> queryForList(Map<String, Object> map);

    int queryForListCount(Map<String, Object> map);

    int queryOverlapActivity(Map<String, Object> map);

    void create(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo);

    VpsVcodeBottlecapActivityCogInfo queryActivityCogByKey(String activityKey);

    void update(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo);
}
