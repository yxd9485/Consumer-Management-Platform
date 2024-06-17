package com.dbt.platform.enterprise.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.enterprise.bean.SkuInfo;

/**
 * @author RoyFu 
 * @createTime 2016年4月21日 下午12:37:52
 * @description 
 */

public interface ISkuInfoDao extends IBaseDao<SkuInfo> {

	/**
	 * 根据企业获取sku列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<SkuInfo> queryForLst(Map<String, Object> map);

	/**
	 * 根据企业获取sku数量
     * @param map keys:queryBean
	 */
	public Integer queryForCount(Map<String, Object> map);

    /**
     * 根据企业获取sku列表
     * @param companyKey 企业KEY</br> 
     * @return Integer </br>
     */
    public List<SkuInfo> loadSkuListByCompany(@Param(value = "companyKey") String companyKey);

    /**
     * 根据企业获取sku数量
     * @param companyKey 企业KEY</br> 
     * @return Integer </br>
     */
    public Integer loadSkuListByCompanyCount(@Param(value = "companyKey") String companyKey);

	/**
	 * 获取sku主键的最大编号
	 * @param </br> 
	 * @return int </br>
	 */
	public int findMaxSkuKeyNum();

	/**
	 * 根据spukey查询sku数量
	 * @param spuInfoKey
	 * @return
	 */
	public int queryForCountBySpuKey(@Param(value = "spuInfoKey") String spuInfoKey);
	
	/**
	 * 根据spukey查询skuList
	 * @param spuInfoKey
	 * @return
	 */
	public List<SkuInfo> queryForListBySpuKey(String spuInfoKey);

	/**
	 * 查询SKU
	 * @param isExistSpuKey	是否存在spuKey
	 * @return
	 */
	public List<SkuInfo> queryListByMap(Map<String, Object> map);

	/**
	 * 更新SKU信息
	 * @param map
	 */
	public void updateSpuInfo(Map<String, Object> map);

	public void clearSpuInfoKey(@Param(value = "spuInfoKey") String spuInfoKey);
	/**
     * 获取与高印同步的SKU信息
     * @return
     */
    public List<SkuInfo> queryForCPIS();

}
