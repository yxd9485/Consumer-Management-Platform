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
import com.dbt.platform.bidding.service.BiddingActivitySubscribeInfoService;

/**
 * 竞价活动消息通知Job
 * @author hanshimeng
 *
 */
@Service("sendBiddingMessageJob")
public class SendBiddingMessageJob {
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private BiddingActivitySubscribeInfoService subscribeInfoService;
	
	/**
	 * 活动开始提醒（每天09:00后达到开场人数的活动）
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public void activityStart() throws InterruptedException{
		log.warn("活动开始提醒job处理开始");

        // 获取job执行的省区
        DbContextHolder.clearDBType();
        Set<String> nameList = null;
        String projectServerNames = DatadicUtil.getDataDicValue(DatadicKey
                    .dataDicCategory.PROJECT_JOB,DatadicKey.ProjectJob.BIDDING_ACTIVITY_START);
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
            subscribeInfoService.sendActivityRemind("1");
            DbContextHolder.clearDBType();
            Thread.sleep(100);
        }
    
		
        log.warn("活动开始提醒job处理结束");
	}
	
	/**
	 * 活动即将结束提醒（每晚8点半）
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	public void activityEnd() throws InterruptedException{
		log.warn("活动即将结束提醒job处理开始");

        // 获取job执行的省区
        DbContextHolder.clearDBType();
        Set<String> nameList = null;
        String projectServerNames = DatadicUtil.getDataDicValue(DatadicKey
                    .dataDicCategory.PROJECT_JOB,DatadicKey.ProjectJob.BIDDING_ACTIVITY_END);
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
            subscribeInfoService.sendActivityRemind("2");
            DbContextHolder.clearDBType();
            Thread.sleep(1000);
        }
    
		
        log.warn("活动即将结束提醒job处理结束");
	}
	
}
