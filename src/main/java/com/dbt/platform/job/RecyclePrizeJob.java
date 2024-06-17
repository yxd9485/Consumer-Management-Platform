package com.dbt.platform.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.prize.service.MajorInfoService;

@Service("recyclePrizeJob")
public class RecyclePrizeJob {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private MajorInfoService majorInfoService;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
    
    @SuppressWarnings("unchecked")
	public void recyclePrize() throws Exception {
        log.warn("大奖回收Job开始执行...");
    	// 获取job执行的省区
   		DbContextHolder.clearDBType();
       	Set<String> nameList = null;
       	String projectServerNames = DatadicUtil.getDataDicValue(
   				DatadicKey.dataDicCategory.PROJECT_JOB,
   				DatadicKey.ProjectJob.RECYCLE_PRIZE);
       	if(StringUtils.isBlank(projectServerNames)) return;
    	
    	if(!"ALL".equals(projectServerNames)){
       		nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
       	}else{
       		nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
					.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
       	}
       	
       	// 循环执行任务
       	for (String projectServerName : nameList) {	
    		this.recyclePrize(DateUtil.getDateTime(DateUtil.addDays(-1), "yyyy-MM-dd"), projectServerName);
    		Thread.sleep(500);
    	}
    }

    /**
     * 手动触发
     * 
     * @param expireTime 过期日期yyyy-MM-dd
     */
    @RequestMapping("/recyclePrize")
    public void recyclePrize(String expireTime, String projectServerName) throws Exception {
        log.warn("大奖回收Job开始执行...");
        DbContextHolder.setDBType(projectServerName);
		// 获取需要回收的大奖类型
        List<String> recoveryPrizes = vcodeActivityBigPrizeService.getRecoveryPrizes();
		
        List<MajorInfo> majorInfoLst = majorInfoService.queryForExpired(expireTime,recoveryPrizes);
        if (majorInfoLst == null || majorInfoLst.isEmpty()) {
            log.warn("expireTime:" + expireTime + " --- 无要回收的大奖");
        } else {
            int successNum = 0;
            for (int i = 0; i < majorInfoLst.size(); i++) {
                try {
                    majorInfoService.executeRecyclePrize(majorInfoLst.get(i));
                    successNum++;
                } catch (Exception e) {
                    if (e instanceof BusinessException) {
                        i--;
                        continue;
                    }
                    log.error(e.getMessage(), e);
                }
            }
            
            log.warn("expireTime:" + expireTime + " --- 要回收大奖个数：" + majorInfoLst.size() + " 回收成功个数：" + successNum);
        }
        DbContextHolder.clearDBType();
        log.warn("大奖回收Job执行结束...");
	}
}
