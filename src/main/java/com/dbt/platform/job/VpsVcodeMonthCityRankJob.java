package com.dbt.platform.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.cityrank.service.VpsVcodeMonthCityRankHistoryService;

/**
 *  月度城市酒王排名Job
 */
@Service("vpsVcodeMonthCityRankJob")
public class VpsVcodeMonthCityRankJob {
    private Logger log = Logger.getLogger(VpsVcodeMonthCityRankJob.class);
    
    @Autowired
    private VpsVcodeMonthCityRankHistoryService rankHistoryService;
    @Autowired
    private SysAreaService sysAreaService;
    
    @SuppressWarnings("unchecked")
	public void executeRankHistory() throws Exception{
    	// 获取job执行的省区
   		DbContextHolder.clearDBType();
       	Set<String> nameList = null;
       	String projectServerNames = DatadicUtil.getDataDicValue(
   				DatadicKey.dataDicCategory.PROJECT_JOB,
   				DatadicKey.ProjectJob.EXECUTE_RANK_HISTORY);
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
	        String projectArea = DatadicUtil.getDataDicValue(
            		DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, 
					DatadicKey.filterCompanyInfo.PROJECT_AREA);
	        if (StringUtils.isNotBlank(projectArea)) {
	            
	            // 获取上个月份
	            String rankMonth = DateUtil.getDateTime(DateUtil.addMonths(-1), "yyyyMM");
	
	            // 获取揭榜人数限制
	            int perRankNum = Integer.valueOf(StringUtils.defaultString(DatadicUtil.getDataDicValue(DatadicUtil
	                            .dataDicCategory.MONTH_CITY_RANK, DatadicUtil.dataDic.monthCityRank.MCR_PER_RANK_NUM), "30"));
	            
	            // 持久化各省区下市级排名数据
	            String[] projectAreaAry = projectArea.split(",");
	            for (String provinceCode : projectAreaAry) {
	                // 获取项目所属省区下的所有市
	                List<SysAreaM> cityLst = sysAreaService.findByParentId(provinceCode + "0000");
	                for (SysAreaM areaItem : cityLst) {
	                    if ("全部".equals(areaItem.getAreaName())) continue;
	                    try {
	                        rankHistoryService.executeMonthCityRankHistory(rankMonth, 
	                                areaItem.getAreaName().substring(0, areaItem.getAreaName().length() - 1), perRankNum);
	                    } catch (Exception e) {
	                        log.error("rankMonth:" + rankMonth + " cityName:" + areaItem.getAreaName() + " perRankNum:" + perRankNum + " --- 排名信息持久化异常");
	                    }
	                }
	            }
	        }
	        DbContextHolder.clearDBType();
	        Thread.sleep(500);
    	}
    }

}
