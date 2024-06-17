package com.dbt.platform.ticket.dao;

import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.ticket.bean.VpsVcodeTicketInfo;

/**
 *  优惠券活动Dao
 */
public interface IVpsVcodeTicketInfoDao extends IBaseDao<VpsVcodeTicketInfo> {

	/**
	 * paramMap
	 * @param map
	 * @return
	 */
	VpsVcodeTicketInfo findTicketInfoForMap(Map<String, Object> map);
	
	/**
	 * 更新优惠券面额
	 * @param paramMap
	 */
	void updateTicketInfo(Map<String, Object> paramMap);

    VpsVcodeTicketInfo findByTicketNo(String ticketNo);
}
