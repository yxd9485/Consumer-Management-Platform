package com.dbt.platform.jobhandler;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.fission.service.IVpsVcodeActivateShareRecordService;
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
 * 	1 0 0 * * ?
 * 分享裂变--清除裂变活动 领取时间到期未分享的活动
 * @author shuDa
 * @date 2022/5/17
 **/
@Component
@JobHandler(value="ClearActivateRedEnvelopeShareRecordJobHandle")
public class ClearActivateRedEnvelopeShareRecordJobHandle extends IJobHandler {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private IVpsVcodeActivateShareRecordService iVpsVcodeActivateShareRecordService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //清除裂变活动 领取时间到期未分享的活动
        // 获取job执行的省区
        DbContextHolder.clearDBType();
        Set<String> nameList = null;
        String projectServerNames = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.PROJECT_JOB,
                DatadicKey.ProjectJob.CLEAR_ACTIVATE_RED_ENVELOPE_RECORD_JOB);
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
            iVpsVcodeActivateShareRecordService.removeActivateRedEnvelopeShareRecordJobHandle();
            DbContextHolder.clearDBType();
            Thread.sleep(500);
        }
        return new ReturnT<>(ReturnT.SUCCESS_CODE,"成功");
    }
}
