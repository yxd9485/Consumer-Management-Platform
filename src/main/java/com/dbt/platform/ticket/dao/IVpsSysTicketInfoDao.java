package com.dbt.platform.ticket.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.ticket.bean.VpsSysTicketInfo;

/**
 *  优惠券基础面额Dao
 */
public interface IVpsSysTicketInfoDao extends IBaseDao<VpsSysTicketInfo> {

	VpsSysTicketInfo findByTicketNo(String ticketNo);

	List<VpsSysTicketInfo> localList();


    List<VpsSysTicketInfo> findTicketDenomination(Map<String, Object> queryMap);

	int countTicketActivityList(Map<String, Object> queryBean);

	void writeTicketDenomination(VpsSysTicketInfo vpsSysTicketInfo);

	void delTicketDenomination(Map<String, Object> infoKey);

	VpsSysTicketInfo findTicketDenominationByKey(String infoKey);

	void updateTicketDenominationEdit(VpsSysTicketInfo vpsSysTicketInfo);

	List<VpsSysTicketInfo> getTicketList(VpsSysTicketInfo vpsSysTicketInfo);

	List<VpsSysTicketInfo> findByCategoryKey(String categoryKey);

    List<VpsSysTicketInfo> queryAllTicket();
}
