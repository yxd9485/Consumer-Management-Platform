package com.dbt.framework.datadic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.dao.ServerInfoDao;


/**
 * 数据源Service
 * @author hanshimeng
 * @description
 */
@Service
public class ServerInfoService extends BaseService<ServerInfo>{

	@Autowired
	private ServerInfoDao serverInfoDao;
	public List<ServerInfo> getAllServer(){
		return serverInfoDao.getAllServer();
	}
	public ServerInfo findByProjectServerName(String projectServerName) {
		return serverInfoDao.findByProjectServerName(projectServerName);
	}
}
