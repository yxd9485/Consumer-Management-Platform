package com.dbt.platform.ticket.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.ticket.bean.VpsSysTicketCategory;

/**
 *  优惠券基础类型Dao
 */
public interface IVpsSysTicketCategoryDao extends IBaseDao<VpsSysTicketCategory> {

	/**
	 * 查询类型List
	 * @return
	 */
	List<VpsSysTicketCategory> loadTicketCategory();


	List<VpsSysTicketCategory> findTicketDenomination(Map<String, Object> queryMap);

    int countTicketActivityList(Map<String, Object> queryMap);

	VpsSysTicketCategory queryTicketCategoryByName(String categoryName);

	void doTicketCategoryDel(String categoryKey);
}
