package com.dbt.platform.codefactory.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.codefactory.bean.VpsWinery;

public interface IVpsVcodeWineryDao extends IBaseDao<VpsWinery> {

	List<VpsWinery> queryForLst(Map<String, Object> map);

	int queryForCount(Map<String, Object> map);

	int checkShort(VpsWinery info);

	List<VpsWinery> findWineryByFactoryLst(@Param("wineryName") String wineryName);


}
