package com.dbt.platform.publish.dao;

import java.util.List;
import java.util.Map;

import com.dbt.platform.publish.bean.VpsAdHome;
import com.dbt.platform.publish.bean.VpsAdUp;

public interface AdUpDao {

	List<VpsAdUp> queryForLst(Map<String, Object> queryMap);

	int queryForCount(Map<String, Object> queryMap);

	void updateVpsAdUp(VpsAdUp vpsAdPub);

	VpsAdUp findById(String infoKey);

	void deleteById(Map<String, Object> map);

	void addAdUp(VpsAdUp vpsAdPub);

	void updateVpsAdHome(VpsAdHome vpsAdHome);

	List<VpsAdUp> quAllAdUp();

}
