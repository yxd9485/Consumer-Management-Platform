package com.dbt.smallticket.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.smallticket.bean.VpsTicketRecord;
import com.dbt.smallticket.bean.VpsTicketRecordSkuDetail;
import com.dbt.smallticket.dao.VpsTicketRecordSkuDetailDao;

@Service
public class VpsTicketRecordSkuDetailService extends BaseService<VpsTicketRecordSkuDetail>{

	@Autowired
	private VpsTicketRecordSkuDetailDao ticketRecordSkuDetailDao;
	
	public void insertTicketSkuDetail(VpsTicketRecord vpsTicketRecord) {
		for (VpsTicketRecordSkuDetail datail : vpsTicketRecord.getDetailLst()) {
			datail.fillFields(vpsTicketRecord);
		}
		ticketRecordSkuDetailDao.insertTicketSkuDetail(vpsTicketRecord.getDetailLst());
	}

	public void deleteTicketSkuDetailByRecordKey(String infoKey) {
		ticketRecordSkuDetailDao.deleteTicketSkuDetailByRecordKey(infoKey);
	}

	
}

