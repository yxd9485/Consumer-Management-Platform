package com.dbt.smallticket.service;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.smallticket.bean.VpsTicketRecord;
import com.dbt.smallticket.bean.VpsTicketTerminalInsideDetail;
import com.dbt.smallticket.dao.VpsTicketTerminalInsideDetailDao;

@Service
public class VpsTicketTerminalInsideDetailService extends BaseService<VpsTicketTerminalInsideDetail>{

	@Autowired
	private VpsTicketTerminalInsideDetailDao insideDetailDao;
	
	
	/**
	 * 模糊搜查
	 * @param searchType 0门店 1店内码
	 * @param searchValue
	 * @param searchKey 详情表的字段
	 * @return
	 */
	public List<VpsTicketTerminalInsideDetail> searchLst(VpsTicketTerminalInsideDetail detail) {
		return insideDetailDao.searchLst(detail);
	}
	/**
	 * 根据体系查询店内码
	 * @param vpsTicketRecord
	 * @return
	 */
	public List<String> queryInsideDetailByInalnsideCode(VpsTicketRecord vpsTicketRecord) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("insideCodeType", vpsTicketRecord.getInsideCodeType());
		queryMap.put("terminalKey", vpsTicketRecord.getTerminalKey());
		queryMap.put("detailLst", vpsTicketRecord.getDetailLst());
		return insideDetailDao.queryInsideDetailByInalnsideCode(queryMap);
	}

	public void addBatch(List<VpsTicketTerminalInsideDetail> insertDetailLst) {
		insideDetailDao.addBatch(insertDetailLst);		
	}

	public void updateBatch(List<VpsTicketTerminalInsideDetail> udapteDetailLst) {
		insideDetailDao.updateBatch(udapteDetailLst);			
	}


	
	
}

