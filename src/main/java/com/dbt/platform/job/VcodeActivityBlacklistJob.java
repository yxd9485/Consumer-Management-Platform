package com.dbt.platform.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.doubtuser.service.VcodeActivityBlacklistJobService;

@Service("vcodeActivityBlacklistJob")
public class VcodeActivityBlacklistJob {
	/** The logger. */
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private VcodeActivityBlacklistJobService vcodeActivityBlacklistJobService;
	
	/**
	 * 河北：可疑用户一个月后自动进入黑名单
	 * @param </br> 
	 * @return void </br>
	 */
	@SuppressWarnings("unchecked")
	public void dubiousUserConvertBlackUser() throws Exception{
		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.DUBIOUS_USER_CONVERT_BLACK_USER);
    	if(StringUtils.isBlank(projectServerNames)) return;
    	
    	if(!"ALL".equals(projectServerNames)){
    		nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
    	}else{
    		nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
					.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
    	}
    	
    	// 循环执行任务
    	for (String projectServerName : nameList) {
			DbContextHolder.setDBType(projectServerName);
			try {
				vcodeActivityBlacklistJobService.executeDubiousUserConvertBlackUser();
			} catch (Exception e) {
				logger.error("可疑用户一个月后自动进入黑名单JOB异常!", e);
			}
			DbContextHolder.clearDBType();
			Thread.sleep(500);
		}
	}
}
