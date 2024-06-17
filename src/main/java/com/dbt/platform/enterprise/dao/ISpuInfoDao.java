package com.dbt.platform.enterprise.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.bean.SpuInfo;

public interface ISpuInfoDao extends IBaseDao<SpuInfo> {

	/**
	 * 根据企业获取spu列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<SpuInfo> queryForLst(Map<String, Object> map);

	/**
	 * 根据企业获取spu数量
     * @param map keys:queryBean
	 */
	public Integer queryForCount(Map<String, Object> map);

    /**
     * 根据企业获取spu列表
     * @param companyKey 企业KEY</br> 
     * @return Integer </br>
     */
    public List<SpuInfo> loadSpuListByCompany(@Param(value = "companyKey") String companyKey);

    /**
     * 根据企业获取spu数量
     * @param companyKey 企业KEY</br> 
     * @return Integer </br>
     */
    public Integer loadSpuListByCompanyCount(@Param(value = "companyKey") String companyKey);

	/**
	 * 获取spu主键的最大编号
	 * @param </br> 
	 * @return int </br>
	 */
	public int findMaxSpuKeyNum();

}
