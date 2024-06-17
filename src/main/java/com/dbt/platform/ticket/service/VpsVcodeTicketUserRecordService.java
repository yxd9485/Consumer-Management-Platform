package com.dbt.platform.ticket.service;

import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.platform.ticket.bean.VpsVcodeTicketExchangeCog;
import com.dbt.platform.ticket.bean.VpsVcodeTicketUserRecord;
import com.dbt.platform.ticket.dao.IVpsVcodeTicketUserRecordDao;
import com.dbt.platform.turntable.bean.VpsTurntablePacksRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VpsVcodeTicketUserRecordService {
    @Autowired
    private IVpsVcodeTicketUserRecordDao vpsVcodeTicketUserRecordDao;

    public List<VpsVcodeTicketUserRecord> queryList(VpsVcodeTicketUserRecord queryBean, PageOrderInfo pageInfo, String infoKey) {
        Map<String,Object> map = new HashMap<>();
        map.put("queryBean",queryBean);
        map.put("pageInfo",pageInfo);
        map.put("infoKey",infoKey);
        List<String> tableSuffix = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            tableSuffix.add(i + 1 + "");
        }
        map.put("tableSuffix", tableSuffix);
        return vpsVcodeTicketUserRecordDao.queryList(map);
    }

    public int queryForCount(VpsVcodeTicketUserRecord queryBean, String infoKey) {
        Map<String,Object> map = new HashMap<>();
        map.put("queryBean",queryBean);
        map.put("infoKey",infoKey);
        List<String> tableSuffix = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            tableSuffix.add(i + 1 + "");
        }
        map.put("tableSuffix", tableSuffix);
        return vpsVcodeTicketUserRecordDao.queryForCount(map);
    }


    public void exportTicketExchangeRecordList(String infoKey, VpsVcodeTicketUserRecord queryBean, HttpServletResponse response) throws IOException {
        response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();

        // 获取导出信息
        List<VpsVcodeTicketUserRecord> vpsVcodeTicketUserRecords = queryList(queryBean, null, infoKey);


        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        bookName = "兑换记录导出";
        headers = new String[]{"优惠券名称", "消耗积分", "兑换人", "兑换人手机号", "兑换时间"};
        valueTags = new String[]{"ticketName", "consumeVpoints", "userName", "phoneNumber", "earnTime"};

        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
        ExcelUtil<VpsVcodeTicketUserRecord> excel = new ExcelUtil<>();
        excel.writeExcel(bookName, headers, valueTags, vpsVcodeTicketUserRecords, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
    }
}
