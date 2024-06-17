package com.dbt.platform.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.doubleprize.bean.VpsVcodeDoublePrizeCog;
import com.dbt.platform.doubleprize.service.VpsVcodeDoublePrizeCogService;

/**
 * 一码双奖活动结束后清空参与用户的活动标签
 * @author hongen
 *
 */
@Service("vpsVcodeDoublePrizeCogJob")
public class VpsVcodeDoublePrizeCogJob {
    Logger log = Logger.getLogger(this.getClass());
    
    @Autowired
    private VpsVcodeDoublePrizeCogService doublePrizeCogService;
    
    /**
     * 清除已结束且未清除过的活动的标签
     */
    @SuppressWarnings("unchecked")
	public void clearDoublePrizeForUser() throws Exception{
		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.CLEAR_DOUBLE_PRIZE_FOR_USER);
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
	        List<VpsVcodeDoublePrizeCog> doublePrizeCogLst = doublePrizeCogService.queryAllJobActivity();
	        if (CollectionUtils.isNotEmpty(doublePrizeCogLst)) {
	            for (VpsVcodeDoublePrizeCog item : doublePrizeCogLst) {
	                
	                // 清除用户的活动标志
	                doublePrizeCogService.executeClearUser(item.getActivityKey());
	                
	                // 删除活动相关的用户扫码次数的Redis缓存
	                RedisApiUtil.getInstance().del(true, CacheKey.cacheKey.vcode
	                        .KEY_VCODE_DOUBLE_PRIZE_COG + ":" + item.getActivityKey() + ":scanNum");
	                
	                // 删除活动下每个限中次数缓存
	                RedisApiUtil.getInstance().del(true, CacheKey.cacheKey.vcode
	                        .KEY_VCODE_DOUBLE_PRIZE_COG + ":" + item.getActivityKey() + ":everyone");
	            }
	            CacheUtilNew.removeAll();
	        }
	        DbContextHolder.clearDBType();
			Thread.sleep(500);
    	}
    }
    
    /**
     * 清除过期的已中出奖项
     */
    @SuppressWarnings("unchecked")
	public void clearDoublePrizeLottery() throws Exception{
		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.CLEAR_DOUBLE_PRIZE_LOTTERY);
    	if(StringUtils.isBlank(projectServerNames)) return;
    	
    	if(!"ALL".equals(projectServerNames)){
    		nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
    	}else{
    		nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
					.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
    	}
    	
    	// 循环执行任务
    	for (String projectServerName : nameList) {	
    		log.error("处理省区="+ projectServerName);
    		DbContextHolder.setDBType(projectServerName);
	        List<VpsVcodeDoublePrizeCog> doublePrizeCogLst = doublePrizeCogService.queryAllValidActivity();
	        if (CollectionUtils.isNotEmpty(doublePrizeCogLst)) {
	            for (VpsVcodeDoublePrizeCog item : doublePrizeCogLst) {
	                
	                // 清除过期的已中出奖项
	                doublePrizeCogService.executeClearLottery(item.getActivityKey());
	            }
	        }
	        DbContextHolder.clearDBType();
	        Thread.sleep(500);
    	}
    }
}
