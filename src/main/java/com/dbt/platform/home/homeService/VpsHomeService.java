package com.dbt.platform.home.homeService;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.util.MathUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.platform.home.bean.ReportMainInfo;
import com.dbt.platform.home.dao.IVpsHomeReportDao;
import com.dbt.platform.home.util.DateTimeUtil;

@Service
public class VpsHomeService {

    @Autowired
    IVpsHomeReportDao homeReportDao;
    @Autowired
    private ServerInfoService serverInfoService;

    public ReportMainInfo getHomeData() {
        String lastDay= DateTimeUtil.getLastDay();
        //获取概览
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("queryDate", lastDay);
        map.put("report_type", "0");
        ReportMainInfo dayInfo = homeReportDao.getReportByDayTypeMain(map);
        
        // 获取累计消耗
        dayInfo.setTotalVpoints(StringUtils.defaultIfBlank(homeReportDao.getTotalVpoints(lastDay), "0"));
        
        //积分消耗类型
//        VpointsType vtype=homeReportDao.getVpointsType(lastDay);
//        dayInfo.setTotalVpoints(null !=vtype? vtype.getTotalVpoints():"0");
//		if(dayInfo!=null&&dayInfo.getTotalUsers()!=null&&dayInfo.getTotalScans()!=null){
//			//首扫比例:总用户/总扫码
//			dayInfo.setF
//			irstPercent(getPercent(dayInfo.getTotalUsers(),dayInfo.getTotalScans()));
//		}
        //计算剩余积分 = 总积分(数据字典配置) - 消耗积分
        //计算剩余积分 = 总积分(数据字典配置) - 消耗积分
        long signVpoints=0;
//		try {
//			signVpoints=homeReportDao.getSignVpoints(new DateTime().toString("yyyy-MM-dd"));
//		} catch (Exception e) {
//		}
        if(DbContextHolder.getDBType().equals("huanan")){
            signVpoints+=392750;
        }
        if(DbContextHolder.getDBType().equals("shandongagt")){
            signVpoints+=272525;
        }
        if(DbContextHolder.getDBType().equals("chongqing")){
            signVpoints+=128160;
        }
        if(DbContextHolder.getDBType().equals("guangxi")){
            long totalV=Long.parseLong(dayInfo.getTotalVpoints().replace(",", ""))+143896;
            dayInfo.setTotalVpoints(DecimalFormat.getNumberInstance().format(totalV));
            long totalS=Long.parseLong(dayInfo.getTotalScans().replace(",", ""))+19;
            dayInfo.setTotalScans(DecimalFormat.getNumberInstance().format(totalS));
            long totalU=Long.parseLong(dayInfo.getTotalUsers().replace(",", ""))-109;
            dayInfo.setTotalUsers(DecimalFormat.getNumberInstance().format(totalU));
        }
        if(DbContextHolder.getDBType().equals("rio")){
            dayInfo.setTotalVpoints(DecimalFormat.getNumberInstance().format(146991));
            dayInfo.setLastVpoints(DecimalFormat.getNumberInstance().format(53009));
        }
        dayInfo.setSignVpoints(signVpoints+"");
        
        String totalVpoints = DatadicUtil.getDataDicValueNoCache("ent_stats", "total_vpoints");
        if(StringUtils.isNotEmpty(totalVpoints) &&dayInfo!=null){
            Long totalReduce = Long.parseLong(dayInfo.getTotalVpoints().replace(",", ""));
            dayInfo.setLastVpoints(DecimalFormat.getNumberInstance().format(Long.parseLong(totalVpoints)-totalReduce-signVpoints));
        }
        //剩余天数
        String startDate=DateTimeUtil.plusDays(lastDay, -7);
        String avgVpoints=homeReportDao.getAvgVpoints(startDate,lastDay);
        if(StringUtils.isEmpty(avgVpoints)||avgVpoints.equals("0")||Double.valueOf(avgVpoints)<=0){
            dayInfo.setLastDays(null);
        }else{
            dayInfo.setLastDays(String.format("%.2f", Double.valueOf(dayInfo.getLastVpoints().replace(",", ""))/Double.valueOf(avgVpoints)));
        }

        //青啤使用省区总积分替换剩余积分、剩余可用天数
        provinceLastVpoints(dayInfo);

      return dayInfo;
    }

    //青啤使用省区总积分替换剩余积分、剩余可用天数
    private void provinceLastVpoints(ReportMainInfo dayInfo) {
        ItemDBHelper db = new ItemDBHelper();
        try {
            String psn = DbContextHolder.getDBType();
            DbContextHolder.setDBType(null);
            ServerInfo serverInfo = serverInfoService.findByProjectServerName(psn);
            DbContextHolder.setDBType(psn);

            if(serverInfo == null || StringUtils.isEmpty(serverInfo.getItemValue()))
                return;


            String sql = "select vpoints_count from qp_limit_report t " +
                    "left join qp_item_info qii on qii.item_group = t.item_group " +
                    "where qii.item_value = '" + serverInfo.getItemValue() + "' and report_date = '" + DateTimeUtil.getLastDay() + "' ";
            ResultSet res = db.stmt.executeQuery(sql);

            Long v = null;
            if(res.next()) {
                v = res.getLong(1);
            }
            res.close();
            if(v == null)
                return;


            sql = "select total_vpoints from recharge_pool t left join qp_item_info qii on qii.item_group = t.province_name " +
                    "where qii.item_value = '" + serverInfo.getItemValue() + "' ";
            res = db.stmt.executeQuery(sql);
            long total = 0L;
            if(res.next()) {
                total = res.getLong(1);
            }
            res.close();

            sql = "select sum(t.day_vpoints) from qp_limit_report t left join qp_item_info qii on qii.item_group = t.item_group " +
                    "where qii.item_value = '" + serverInfo.getItemValue() + "' " +
                    "and report_date >= DATE_SUB('" + DateTimeUtil.getLastDay() + "', INTERVAL 6 DAY) ";
            res = db.stmt.executeQuery(sql);
            double seven = 0.00D;
            if(res.next()) {
                seven = res.getDouble(1);
            }
            res.close();

            long lv = total - v;

            if(seven > 0) {
                String lastDays = String.valueOf(MathUtil.div(lv, seven / 7 * 1.2, 2));
                if(StringUtils.isEmpty(lastDays)){
                    dayInfo.setLastDays("-");
                }else{
                    dayInfo.setLastDays(String.format("%.2f", Double.valueOf(lastDays)));
                }
            }

            dayInfo.setLastVpoints(DecimalFormat.getNumberInstance().format(Long.parseLong(String.valueOf(lv))));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(db!=null){
                db.close();
            }
        }
    }
}
