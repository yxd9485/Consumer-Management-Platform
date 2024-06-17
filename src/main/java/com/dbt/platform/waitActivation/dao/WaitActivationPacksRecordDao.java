package com.dbt.platform.waitActivation.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPacksRecord;

import java.util.List;
import java.util.Map;

public interface WaitActivationPacksRecordDao extends IBaseDao<VpsWaitActivationPacksRecord> {

    /**
     * 查询累计中出限制金额
     * @param queryMap
     * @return
     */
    int queryAllTables(Map<String, Object> queryMap);

}
