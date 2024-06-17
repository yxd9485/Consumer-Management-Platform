package com.dbt.platform.autocode.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.autocode.bean.VpsVcodeBatchSerialNoQueryLog;
import com.dbt.platform.autocode.dao.IVpsVcodeBatchSerialNoQueryLogDao;

/**
 * 批次序号查询记录Service
 */
@Service	
public class VpsVcodeBatchSerialNoQueryLogService extends BaseService<VpsVcodeBatchSerialNoQueryLog>{
	
	@Autowired
	private IVpsVcodeBatchSerialNoQueryLogDao batchSerialNoLogDao;
	
	public void addSeriallNOLog(VpsVcodeBatchSerialNoQueryLog batchSerialNoQueryLog) {
	   batchSerialNoLogDao.create(batchSerialNoQueryLog);
	}

	/**
	 * 列表
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public List<VpsVcodeBatchSerialNoQueryLog> queryForLst(VpsVcodeBatchSerialNoQueryLog queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return batchSerialNoLogDao.queryForLst(map);
	}
	
	/**
	 * 列表count
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public int queryForCount(VpsVcodeBatchSerialNoQueryLog queryBean) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("queryBean", queryBean);
	    return batchSerialNoLogDao.queryForCount(map);
	}

	/**
	 * 依据二维码序号查询记录
	 */
	public VpsVcodeBatchSerialNoQueryLog findByBatchSerialNo(String batchSerialNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("batchSerialNo", batchSerialNo);
		return batchSerialNoLogDao.findByBatchSerialNo(map);
	}
}
