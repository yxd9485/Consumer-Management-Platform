package com.dbt.platform.ticket.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.ticket.bean.VpsVcodeTicketInfo;
import com.dbt.platform.ticket.dao.IVpsVcodeTicketInfoDao;

/**
 * 优惠券类型Service
 */
@Service
public class VpsVcodeTicketInfoService extends BaseService<VpsVcodeTicketInfo> {

	@Autowired
	private IVpsVcodeTicketInfoDao ticketInfoDao;

	/**
	 * 保存优惠券面额
	 * @param ticketInfo
	 */
	public void addTicketInfo(VpsVcodeTicketInfo ticketInfo) {
		ticketInfoDao.create(ticketInfo);
	}
	
	/**
	 * 更新优惠券面额
	 * @param paramMap
	 */
	public void updateTicketInfo(Map<String, Object> paramMap) {
		ticketInfoDao.updateTicketInfo(paramMap);
	}

	/**
	 * 查询当前活动下该优惠券类型对象
	 * @param paramMap
	 * @return
	 */
	public VpsVcodeTicketInfo findTicketInfoForMap(Map<String, Object> map) {
		return ticketInfoDao.findTicketInfoForMap(map);
	}

	public VpsVcodeTicketInfo findByTicketNo(String ticketNo) {
		return ticketInfoDao.findByTicketNo(ticketNo);
	}
}
