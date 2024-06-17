package com.dbt.smallticket.dao;




import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.smallticket.bean.VpsTicketTerminalInsideDetail;

public interface VpsTicketTerminalInsideDetailDao extends IBaseDao<VpsTicketTerminalInsideDetail>{


	List<String> queryInsideDetailByInalnsideCode(Map<String, Object> queryMap);
	
	//模糊查询
	List<VpsTicketTerminalInsideDetail> searchLst(VpsTicketTerminalInsideDetail detail);

	void addBatch(List<VpsTicketTerminalInsideDetail> insertDetailLst);

	void updateBatch(List<VpsTicketTerminalInsideDetail> udapteDetailLst);
	
}
