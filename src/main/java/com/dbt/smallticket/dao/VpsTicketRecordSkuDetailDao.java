package com.dbt.smallticket.dao;




import java.util.List;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.smallticket.bean.VpsTicketRecordSkuDetail;

public interface VpsTicketRecordSkuDetailDao extends IBaseDao<VpsTicketRecordSkuDetail>{

	List<VpsTicketRecordSkuDetail> queryRecordByTicketKey(String infoKey);

	void insertTicketSkuDetail(List<VpsTicketRecordSkuDetail> insertDetailLst);

	void deleteTicketSkuDetailByRecordKey(String infoKey);
	
}
