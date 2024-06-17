package com.dbt.platform.turntable.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.turntable.bean.TurntableActivityCogInfo;
import com.dbt.platform.turntable.bean.VpsTurntablePrizeCog;

import java.util.List;
import java.util.Map;

public interface TurntableDao extends IBaseDao<TurntableActivityCogInfo> {
    List<TurntableActivityCogInfo> queryForList(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);

    void createTurntableActivity(TurntableActivityCogInfo turntableActivityCogInfo);

    TurntableActivityCogInfo queryTurntableActivityByKey(String activityKey);

    int queryOverlapActivity(Map<String, Object> map);
}
