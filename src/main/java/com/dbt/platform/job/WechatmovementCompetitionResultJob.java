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
import com.dbt.platform.wechatmovement.service.VpsWechatMovementUserDetailService;

/**
 * 比赛结果Job
 * @author hanshimeng
 *
 */
@Service("wechatmovementCompetitionResultJob")
public class WechatmovementCompetitionResultJob {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private VpsWechatMovementUserDetailService wechatMovementUserDetailService;
	

	/**
	 * 比赛结果job（每天00:00）
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void executeCompetitionResult() throws Exception{
		log.warn("比赛结果job处理开始");

		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.WECHAT_MOVEMENT_COMPETITION_RESULT);
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
			wechatMovementUserDetailService.executeCompetitionResult("");
	        DbContextHolder.clearDBType();
			Thread.sleep(100);
		}
    	
    	String lua="redis.call('del',unpack(redis.call('keys','*keyWechatmovementUserDetail*')))";
        RedisApiUtil.getInstance().evalEx(lua ,0, "");
		
		log.warn("比赛结果job处理结束");
	}
	
	/**
	 * PK赛匹配job（每天00:05）
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void executePkRelation() throws Exception{
		log.warn("PK赛匹配job处理开始");
		
		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.WECHAT_MOVEMENT_PK_RELATION);
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
			wechatMovementUserDetailService.executePkRelation("");
	        DbContextHolder.clearDBType();
			Thread.sleep(100);
		}
		
		log.warn("PK赛匹配job处理结束");
	}
	
	/**
	 * PK赛结果job（每天00:00）
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void executePkResult() throws Exception{
		log.warn("PK赛结果job处理开始");
		
		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.WECHAT_MOVEMENT_PK_RESULT);
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
			wechatMovementUserDetailService.executePkResult("");
	        DbContextHolder.clearDBType();
			Thread.sleep(100);
		}
		
		log.warn("PK赛结果job处理结束");
	}
	
	public static void main(String[] args) {
		String str = "ojlLy0HDVlE4dO05V0ls0Rmy1Dto?谷歌";
		System.out.println(str.split("?").length);
		System.out.println(str.split("?")[0]);
	}
}
