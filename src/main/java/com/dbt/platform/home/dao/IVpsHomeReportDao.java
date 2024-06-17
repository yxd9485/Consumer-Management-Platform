package com.dbt.platform.home.dao;

import com.dbt.platform.home.bean.ReportMainInfo;
import com.dbt.platform.home.bean.VpointsType;

import java.util.Map;

public interface IVpsHomeReportDao {

    /**
     * 根据查询条件查询报表总表
     * @param map
     * @return
     */
    public ReportMainInfo getReportByDayTypeMain(Map<String,Object> map);
    
    /**
     * 累计消耗
     * @param queryDate
     * @return
     */
    public String getTotalVpoints(String queryDate);

    /**
     * 根据日期查询周报表
     * @param
     * @return
     */
    public String getAvgVpoints(String startDate,String endDate);//平均消耗积分

    public VpointsType getVpointsType(String reportDate);//积分消耗类别


}
