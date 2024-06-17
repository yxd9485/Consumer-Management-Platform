package com.dbt.platform.enterprise.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.bean.StatInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author RoyFu 
 * @createTime 2016年4月21日 下午12:37:52
 * @description 
 */

public interface IStatInfoDao extends IBaseDao<StatInfo> {

	/**
	 * 根据企业获取sku列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<StatInfo> queryForLst(Map<String, Object> map);

	/**
	 * 根据企业获取sku数量
     * @param map keys:queryBean
	 */
	public Integer queryForCount(Map<String, Object> map);


    ServerInfo getServerList();
}
