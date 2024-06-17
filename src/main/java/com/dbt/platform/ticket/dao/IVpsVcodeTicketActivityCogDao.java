package com.dbt.platform.ticket.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.ticket.bean.VpsVcodeTicketActivityCog;

/**
 *  优惠券活动Dao
 */
public interface IVpsVcodeTicketActivityCogDao extends IBaseDao<VpsVcodeTicketActivityCog> {

	/**
	 * 优惠券活动列表
	 * @param queryMap
	 * @return
	 */
	public List<VpsVcodeTicketActivityCog> loadTicketActivityList(Map<String, Object> queryMap);

	/**
	 * 优惠券活动列表 -- 条数
	 * @param queryMap
	 * @return
	 */
	public int countTicketActivityList(Map<String, Object> queryMap);

	public void changeStatus(VpsVcodeTicketActivityCog activity);
	
	/**
	 * 获取DateTime所在的活动 
	 * 
	 * @param dateTime yyyyMMdd
	 * @return
	 */
	public VpsVcodeTicketActivityCog findByDateTime(String dateTime);

}
