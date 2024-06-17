package com.dbt.platform.jobhandler;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.vpointsshop.service.GiftCardService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 礼品卡赠送提醒
 * 0 0 8-20 * * ?
 * @author shuDa
 * @date 2022/6/30
 **/
@Component
@JobHandler(value = "GiftCardGiveRemidJobHandler")
public class GiftCardGiveRemidJobHandler  extends IJobHandler {
    protected Log log = LogFactory.getLog(getClass());
    @Autowired
    private GiftCardService giftCardService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        // 获取job执行的省区
        DbContextHolder.clearDBType();
        Set<String> nameList = null;
        String projectServerNames = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.PROJECT_JOB,
                DatadicKey.ProjectJob.GIFT_CARD_GIVE_JOB_HANDLER);
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
            giftCardService.GiftCardGiveRemidJob(projectServerName);
            DbContextHolder.clearDBType();
            Thread.sleep(500);
        }
        return new ReturnT<>("成功");
    }

}
