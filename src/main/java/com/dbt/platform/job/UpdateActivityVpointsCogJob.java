package com.dbt.platform.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;

/**
 * 更新待减积分剩余数量
 * @author:Jiquanwei<br>
 * @date:2016-1-22 下午05:22:57<br>
 * @version:1.0.0<br>
 * 
 */
@Service("updateActivityVpointsCogJob")
public class UpdateActivityVpointsCogJob {
	
	@Autowired
	private VcodeActivityVpointsCogService activityVpointsCogService;
		
	@SuppressWarnings("unchecked")
	public void updateBathWaitActivityVpointsCog() throws Exception {
	    // 获取job执行的省区
	    DbContextHolder.clearDBType();
	    Set<String> nameList = null;
	    String projectServerNames = DatadicUtil.getDataDicValue(
	            DatadicKey.dataDicCategory.PROJECT_JOB,
	            DatadicKey.ProjectJob.UPDATE_BATH_WAIT_ACTIVITY_VPOINTS_COG);
	    if(StringUtils.isBlank(projectServerNames)) return;
	    
	    System.out.println("扣减数量projectServerNames-" + projectServerNames);
	    if(!"ALL".equals(projectServerNames)){
	        nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
	    }else{
	        nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
	                .getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
	    }
	    
	    System.out.println("扣减数量nameList.size()-" + nameList.size());
	    
	    // 当前时间对应的缓存Key
	    int minuteKey = Integer.valueOf(DateUtil.getDateTime("mm")) / 5 * 5;
	    String currCacheKey = RedisApiUtil.CacheKey.ActivityVpointsCog
	            .ACTIVITY_FORVCODE_GET_POINTS_MAP + DateUtil.getDateTime("yyyyMMddHH") + minuteKey;
	    
	    // 循环执行任务
	    for (String projectServerName : nameList) {
	        System.out.println("扣减数量projectServerName-" + projectServerName);
	        DbContextHolder.setDBType(projectServerName);
	        
	        // 获取待处理的缓存key集合
	        Set<String> cacheKeySet = RedisApiUtil.getInstance().getSet(RedisApiUtil
	                .CacheKey.ActivityVpointsCog.ACTIVITY_FORVCODE_GET_POINTS_QUEUE);
	        System.out.println("扣减数量1");
	        if (cacheKeySet == null || cacheKeySet.size() == 0) continue;
	        System.out.println("扣减数量2");
	        for (String item : cacheKeySet) {
	            if (currCacheKey.equals(item)) continue;
	            
	            activityVpointsCogService.updateBathWaitActivityVpointsCog(item);
	            RedisApiUtil.getInstance().removeSetValue(RedisApiUtil.CacheKey
	                    .ActivityVpointsCog.ACTIVITY_FORVCODE_GET_POINTS_QUEUE, item);
	            RedisApiUtil.getInstance().del(true, item);
	        }
	        DbContextHolder.clearDBType();
	        Thread.sleep(100);
	    }
	}
	
	@SuppressWarnings("unchecked")
	public void updateBathWaitActivityVpointsCogWarn() throws Exception {
		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.UPDATE_BATH_WAIT_ACTIVITY_VPOINTS_COG);
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
            activityVpointsCogService.initPrizePercentWarn();
	        DbContextHolder.clearDBType();
			Thread.sleep(100);
		}
	}
}