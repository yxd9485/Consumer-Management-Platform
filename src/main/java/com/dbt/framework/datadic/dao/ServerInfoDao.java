package com.dbt.framework.datadic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.framework.datadic.bean.ServerInfo;

/**
 * 数据源DAO
 * @author hanshimeng 
 * @description 
 */

public interface ServerInfoDao extends IBaseDao<ServerInfo> {

	/**
	 * 获取所以省区信息
	 * @return
	 */
	List<ServerInfo> getAllServer();

	/**
	 * 根据省区标识查询
	 * @param projectServerName
	 * @return
	 */
	ServerInfo findByProjectServerName(@Param("projectServerName") String projectServerName);

}
