package com.dbt.platform.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.smallticket.bean.VpsTicketDayExcelTemplet;
import com.dbt.smallticket.dao.VpsTicketRecordDao;
import com.dbt.smallticket.service.VpsTicketRecordService;

/**
 * 长城荟送审过期提醒JOB
 */
@Service("sendTicketRecordJob")
public class SendTicketRecordJob {
    
    @Autowired
    private VpsTicketRecordDao ticketRecordDao;
    @Autowired
    private VpsTicketRecordService vpsTicketRecordService;
    
    /**
     * 推送过期未处理审核消息
     */
    public void sendTicketRecordExpireCheckUser() throws Exception {
        DbContextHolder.setDBType("zhongLCNY");
        vpsTicketRecordService.sendTicketRecordExpireCheckUser();

    }

    /**
     * 推送Excel邮件
     * @throws InterruptedException 
     */
	public void sendTicketRecordEmailExcelJob() throws Exception {
	    
	    // 获取长城玖号服务员激励每日报表数据
        DbContextHolder.setDBType("zhongLJH");
        List<VpsTicketDayExcelTemplet> jiuHaoResultLst = ticketRecordDao.findTicketDayExcel();
	    
        DbContextHolder.setDBType("zhongLCNY");
        vpsTicketRecordService.sendTicketRecordEmailExcel(jiuHaoResultLst);
	}
	
	 /**
     * 推送大奖邮件
     * @throws InterruptedException 
     */
	public void sendTicketPrizeEmailExcelJob() throws Exception {
        DbContextHolder.setDBType("zhongLCNY");
        vpsTicketRecordService.sendTicketPrizeEmailExcel();
	}

}
