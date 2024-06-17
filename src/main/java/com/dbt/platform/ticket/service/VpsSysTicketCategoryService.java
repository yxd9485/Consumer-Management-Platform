package com.dbt.platform.ticket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.ticket.bean.VpsSysTicketCategory;
import com.dbt.platform.ticket.dao.IVpsSysTicketCategoryDao;

/**
 * 优惠券基础类型Service
 */
@Service
public class VpsSysTicketCategoryService extends BaseService<VpsSysTicketCategory> {

	@Autowired
	private IVpsSysTicketCategoryDao sysTicketCategoryDao;
	
	/**
	 * 查询类型List
	 * @return
	 */
	public List<VpsSysTicketCategory> loadTicketCategory(){
		return sysTicketCategoryDao.loadTicketCategory();
	}

	public VpsSysTicketCategory findById(String id) {
		return sysTicketCategoryDao.findById(id);
	}
}
