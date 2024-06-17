package com.dbt.platform.turntable.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.turntable.bean.VpsTurntablePacksRecord;


import java.util.List;
import java.util.Map;


public interface TurntablePacksRecordDao extends IBaseDao<VpsTurntablePacksRecord> {

    List<VpsTurntablePacksRecord> queryTurntablePacksRecordInfoByActivityKey(Map<String, Object> map);

    int queryTurntablePacksRecordInfoForCount(Map<String, Object> map);
}
