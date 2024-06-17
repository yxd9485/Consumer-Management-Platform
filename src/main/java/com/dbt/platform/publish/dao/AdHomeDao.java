package com.dbt.platform.publish.dao;

import java.util.List;
import java.util.Map;

import com.dbt.platform.publish.bean.VpsAdHome;

public interface AdHomeDao {

	List<VpsAdHome> queryForLst(Map<String, Object> queryMap);

	int queryForCount(Map<String, Object> queryMap);

	void addAdHome(VpsAdHome vpsAdHome);

	VpsAdHome findById(String adHomeKey);

	void deleteById(Map<String, Object> map);

	void updateVpsAdHome(VpsAdHome vpsAdHome);

    List<VpsAdHome> queryAllAdHome();
}
