package com.dbt.platform.publish.dao;

import com.dbt.platform.publish.bean.VpsAdRegion;
import com.dbt.platform.publish.bean.VpsAdShop;

import java.util.List;
import java.util.Map;

public interface ActRegionDao {
    List<VpsAdRegion> queryForLst(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);

    void addAdRegion(VpsAdRegion vpsAdRegion);

    List<VpsAdRegion> queryAll();

    VpsAdRegion findById(String infoKey);

    void updateVpsAdRegion(VpsAdRegion vpsAdRegion);

    void adRegionDelete(String infoKey);
}
