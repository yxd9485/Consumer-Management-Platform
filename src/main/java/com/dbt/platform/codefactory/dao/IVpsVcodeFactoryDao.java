package com.dbt.platform.codefactory.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;

/**
 * @Description
 * @Author 
 **/
public interface IVpsVcodeFactoryDao extends IBaseDao<VpsVcodeFactory> {

	List<VpsVcodeFactory> queryForLst(Map<String, Object> map);

	int queryForCount(Map<String, Object> map);

	int checkShort(VpsVcodeFactory factoryShort);

	VpsVcodeFactory findByFactoryName(String qrcodeManufacture);


}
