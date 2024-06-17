package com.dbt.framework.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.dbt.smallticket.bean.VpsTicketDayExcelTemplet;

public class TestUtil {

    public static void main(String[] args) {
        TestUtil.easyExcel();
    }
    
    public static void easyExcel() {
        Map<String, Object> map = new HashMap<>();
        map.put("reportYear", "2021");
//        EasyExcel.write("C:\\Users\\1\\Desktop\\2.xlsx").withTemplate("C:\\Users\\1\\Desktop\\1.xlsx").sheet(0).doFill(map);
        
//        ExcelWriter excelWriter = EasyExcel.write("C:\\Users\\1\\Desktop\\2.xlsx").withTemplate("C:\\Users\\1\\Desktop\\1.xlsx").build();
//        excelWriter.writeContext().writeWorkbookHolder().getWorkbook().setForceFormulaRecalculation(true);
//        excelWriter.fill(map, EasyExcel.writerSheet(0).build()).finish();
        
        List<VpsTicketDayExcelTemplet> lst = new ArrayList<>();
        VpsTicketDayExcelTemplet item = new VpsTicketDayExcelTemplet();
        item.setCommodityCode("69code");
        item.setSkuName("skuName");
        item.setDaySaleNum(1);
        item.setDaySaleMoney(2.00);
        item.setTotalSaleNum(3);
        item.setTotalSaleMoney(4.00);
        lst.add(item);
        

        String tempReport = "d:/data/upload/cogFolder/zhongLCNY/长城双节消费者活动数据报表-V积分.xlsx";
        String reportFile = "d:/data/upload/cogFolder/zhongLCNY/report/长城双节消费者活动数据报表-V积分" + DateUtil.getDate("yyyyMMdd")+ ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(reportFile).withTemplate(tempReport).build();
        excelWriter.writeContext().writeWorkbookHolder().getWorkbook().setForceFormulaRecalculation(true);
        excelWriter.fill(map, EasyExcel.writerSheet(0).build());
        excelWriter.fill(map, EasyExcel.writerSheet(1).build());
        excelWriter.fill(lst, EasyExcel.writerSheet(1).build()).finish();
        
        
        
    }
}
