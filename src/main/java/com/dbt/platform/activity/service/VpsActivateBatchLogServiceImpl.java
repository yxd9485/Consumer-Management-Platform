package com.dbt.platform.activity.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.platform.activity.bean.VpsActivateBatchLog;
import com.dbt.platform.activity.dao.IActivateBatchLogDao;

@Service
public class VpsActivateBatchLogServiceImpl {
    
    private Logger log = Logger.getLogger(this.getClass());
    
    @Autowired
    private IActivateBatchLogDao activateBatchLogDao;

    /**
     * 查询激活日志List
     * @param batchKey
     * @return
     */
	public List<VpsActivateBatchLog> queryList(String batchKey) {
		Map<String, Object> map = new HashMap<>();
		map.put("batchKey", batchKey);
		return activateBatchLogDao.queryList(map);
	}

	/**
	 * 添加日志
	 * @param activateBatchLog
	 */
	public void create(VpsActivateBatchLog activateBatchLog) {
		activateBatchLogDao.create(activateBatchLog);
	}

}	
