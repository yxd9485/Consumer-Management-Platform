package com.dbt.platform.activityui.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activityui.bean.VpsAuthInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author bin.zhang
 **/
public interface IVpsAuthDao extends IBaseDao<VpsAuthInfo> {

    void addAuthInfo(Map<String, Object> authList);

    List<VpsAuthInfo> getAuthInfo(String templateKey);

    void updateAuth(List<VpsAuthInfo> authList);
}
