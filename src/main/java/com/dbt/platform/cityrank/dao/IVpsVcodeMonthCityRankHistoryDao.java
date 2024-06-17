package com.dbt.platform.cityrank.dao;

import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;

/**
 * 城市酒王月度操行记录表Dao
 */
public interface IVpsVcodeMonthCityRankHistoryDao extends IBaseDao<com.dbt.platform.cityrank.bean.VpsVcodeMonthCityRankHistory> {

    /**
     * 批量插排名数据
     * 
     * @param map rankMonthLst
     */
    public void batchWrite(Map<String, Object> map);
}
