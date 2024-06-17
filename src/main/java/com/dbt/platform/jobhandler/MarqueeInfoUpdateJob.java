package com.dbt.platform.jobhandler;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.marquee.service.MarqueeInfoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 	0 0 5 * * ?
 * 跑马灯删除过期数据
 */
@Component
@JobHandler(value = "MarqueeInfoUpdateJob")
public class MarqueeInfoUpdateJob  extends IJobHandler {
    Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private MarqueeInfoService marqueeInfoService;

    @Override
    public ReturnT<String> execute(String serverName) throws Exception {
        log.warn("跑马灯删除过期数据开始");
        // 获取job执行的省区
        DbContextHolder.clearDBType();
        Set<String> nameList = null;
        String projectServerNames = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.PROJECT_JOB,
                DatadicKey.ProjectJob.MARQUEE_INFO_UPDATE_JOB);
        if(StringUtils.isBlank(projectServerNames)) {
            return new ReturnT<>("error");
        }
        if(!"ALL".equals(projectServerNames)){
            nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
        }else{
            nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
                    .getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
        }
        // 循环执行任务
        for (String projectServerName : nameList) {
            DbContextHolder.setDBType(projectServerName);
            marqueeInfoService.deleteMarqueeInfoByMarqueeCogInfo();
            DbContextHolder.clearDBType();
            Thread.sleep(500);
        }
        return new ReturnT<>("成功");
    }
}
