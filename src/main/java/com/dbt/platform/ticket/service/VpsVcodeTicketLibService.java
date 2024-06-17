package com.dbt.platform.ticket.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.ticket.bean.VpsVcodeTicketLib;
import com.dbt.platform.ticket.dao.IVpsVcodeTicketLibDao;

@Service
public class VpsVcodeTicketLibService extends BaseService<VpsVcodeTicketLib>{

	@Autowired
	private IVpsVcodeTicketLibDao ticketLibDao;
	
	/**
     * 创建优惠券库表
     * 
     * @param map libName
     * @return
     */
    public void createTicketTable(String libName){
    	ticketLibDao.createTicketTable(libName);
    }

	/**
     * 批量插入券码
     * @param map
     */
   //  @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void batchWrite(Map<String, Object> map){
    	ticketLibDao.batchWrite(map);
    }
    
    /**
	 * 券码入库
	 * @param paramMap
	 */
	public int addTicketCodeToData(Map<String, Object> paramMap) {
		return ticketLibDao.addTicketCodeToData(paramMap);
	}

    /**
     * 根据时间删除券码
     * @param paramMap
     */
	public void deleteByTime(Map<String, Object> paramMap) {
		ticketLibDao.deleteByTime(paramMap);
	}
}
