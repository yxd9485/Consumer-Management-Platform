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
import com.dbt.platform.expireremind.service.VpsExpireRemindService;

@Service("redPacketMsgExpireRemindInfoJob")
public class RedPacketMsgExpireRemindInfoJob {
	Logger log = Logger.getLogger(this.getClass());
	
    @Autowired
    private VpsExpireRemindService remindService;
    
    @SuppressWarnings("unchecked")
	public void  totalRedPacketMsgExpireRemind() throws Exception{

    	// 获取job执行的省区
   		DbContextHolder.clearDBType();
       	Set<String> nameList = null;
       	String projectServerNames = DatadicUtil.getDataDicValue(
   				DatadicKey.dataDicCategory.PROJECT_JOB,
   				DatadicKey.ProjectJob.TOTAL_RED_PACKET_MSG_EXPIRE_REMIND);
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
			log.error("处理省区="+ projectServerName);
			remindService.updateTotalRedPacketMsgExpireRemind();
			DbContextHolder.clearDBType();
			Thread.sleep(500);
       	}
    }
    
    @Deprecated
    public void releaseOfSuspects(){
        
        try {
            remindService.updateTotalRedPacketMsgExpireRemind();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    
    
}

  
}
