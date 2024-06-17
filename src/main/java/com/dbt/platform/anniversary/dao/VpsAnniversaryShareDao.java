package com.dbt.platform.anniversary.dao;

import com.dbt.platform.anniversary.bean.VpsAnniversaryShare;

import java.util.List;
import java.util.Map;

/**
 * @author: LiangRunBin
 * @create-date: 2023/12/19 18:16
 */
public interface VpsAnniversaryShareDao {

    List<VpsAnniversaryShare> queryAnniversaryShareList();

    List<VpsAnniversaryShare> queryForLst(Map<String, Object> queryMap);

    int queryForCount(Map<String, Object> queryMap);

    int insert(VpsAnniversaryShare vpsAnniversaryShare);

    int update(VpsAnniversaryShare vpsAnniversaryShare);

    int deleteByKey(String shareKey);

    VpsAnniversaryShare queryByKey(String shareKey);

}
