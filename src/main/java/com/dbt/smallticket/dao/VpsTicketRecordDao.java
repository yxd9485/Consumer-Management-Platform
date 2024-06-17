package com.dbt.smallticket.dao;




import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.smallticket.bean.VpsTicketDayExcelTemplet;
import com.dbt.smallticket.bean.VpsTicketRecord;
import com.dbt.smallticket.bean.VpsTicketTerminalCog;
import com.dbt.smallticket.bean.VpsTicketTerminalInsideType;

public interface VpsTicketRecordDao extends IBaseDao<VpsTicketRecord>{

	List<VpsTicketRecord> queryForLst(Map<String, Object> queryMap);

	int queryForCount(Map<String, Object> queryMap);


	List<VpsTicketTerminalInsideType> findTerminalInside(@Param(value="province")String province);
	/**
	 * 更新小票查询店内体系码适用省份
	 * @param map keys:insideCodeType、province
	 */
	void updateTerminalInsideProvince(Map<String, Object> map);

	List<SkuInfo> findSkuLst();

	String checkTicket(VpsTicketRecord detail);

	void createTerminal(VpsTicketTerminalCog ticketTerminalCog);

	List<String> findCheckUserByOpenid(@Param("warAreaName") String warAreaName,@Param("ticketChannel") String ticketChannel);

	// 查询已失效小票
	List<VpsTicketRecord> findExpireTicketRecord();
	//日报
	List<VpsTicketDayExcelTemplet> findTicketDayExcel();
	//查询出KA及餐饮日报明细
	List<VpsTicketDayExcelTemplet> findTicketDayExcelDetail();
	
}
