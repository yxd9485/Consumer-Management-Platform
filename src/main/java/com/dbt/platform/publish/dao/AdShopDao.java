package com.dbt.platform.publish.dao;

import java.util.List;
import java.util.Map;

import com.dbt.platform.publish.bean.VpsAdShop;
import com.dbt.platform.publish.bean.VpsAdUp;

public interface AdShopDao {

	List<VpsAdShop> queryForLst(Map<String, Object> queryMap);

	int queryForCount(Map<String, Object> queryMap);

	void addAdShop(VpsAdShop vpsAdShop);

	VpsAdShop findById(String adHomeKey);

	void deleteById(Map<String, Object> map);

	void updateVpsAdShop(VpsAdShop vpsAdShop);

	List<VpsAdShop> queryAllAdShop();


}
