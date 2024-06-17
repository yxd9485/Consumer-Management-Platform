/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:27:23 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.sweep.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.PackRecordRouterUtil;
import com.dbt.platform.sweep.bean.VpsVcodeRationRecord;
import com.dbt.platform.sweep.dao.IVcodeRationRecordDao;

/**
 * 提现记录 service
 */
@Service
public class VcodeRationRecordService extends BaseService<VpsVcodeRationRecord>{
	@Autowired
	private IVcodeRationRecordDao vcodeRationRecordDao;

	/**
	 * 提现列表List
	 * @param queryBean</br>
	 * @param pageInfo</br>  
	 * @return List<VpsVcodeRationRecord> </br>
	 */
	public List<VpsVcodeRationRecord> queryList(VpsVcodeRationRecord queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rationRecord", queryBean);
		map.put("pageInfo", pageInfo);
		if("guangxi".equals(DbContextHolder.getDBType()) || "zhejiang".equals(DbContextHolder.getDBType())){
			map.put("tableIndex", "");
		}else{
			map.put("tableIndex", "_" + PackRecordRouterUtil.getTabSuffixByDate(4, queryBean.getStartDate()));
		}
		
		List<VpsVcodeRationRecord> activityList = vcodeRationRecordDao.queryList(map);
		return activityList;
	}

	/**
	 * 提现列表Count
	 * @param queryBean</br>
	 * @return int </br>
	 */
	public int queryCount(VpsVcodeRationRecord queryBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rationRecord", queryBean);
		if("guangxi".equals(DbContextHolder.getDBType()) || "zhejiang".equals(DbContextHolder.getDBType())){
			map.put("tableIndex", "");
		}else{
			map.put("tableIndex", "_" + PackRecordRouterUtil.getTabSuffixByDate(4, queryBean.getStartDate()));
		}
		return vcodeRationRecordDao.queryCount(map);
	}
	
}
