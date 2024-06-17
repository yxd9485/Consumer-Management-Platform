package com.dbt.platform.piclib.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.piclib.bean.PicLibrary;

import java.util.List;
import java.util.Map;

/**
 * @Author bin.zhang
 **/
public interface IPicLibDao extends IBaseDao<PicLibrary> {
    List<PicLibrary> findPicLib(Map<String, Object> map);

    int findPicLibCount(Map<String, Object> map);

    void addPicLib(PicLibrary picLibrary);

    PicLibrary findPicLibByKey(String key);

    void updatePicLib(PicLibrary picLibrary);

    void deleteByIds(Map<String, Object> map);

    void doBatchPicLibUpdate(Map<String, Object> map);

    List<PicLibrary> queryAll(Map<String, Object> map);

    void setPicUnDefault(PicLibrary picLibrary);
}
    
