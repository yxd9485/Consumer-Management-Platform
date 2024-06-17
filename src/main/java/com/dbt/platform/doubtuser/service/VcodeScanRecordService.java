package com.dbt.platform.doubtuser.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.platform.doubtuser.dao.IVCodeScanRecordDao;

/**
 * @author hanshimeng
 * @createTime 2016年4月21日 下午5:49:52
 * @description 扫码记录service
 */
@Service("vcodeScanRecordService")
public class VcodeScanRecordService {
	
	@Autowired
	private IVCodeScanRecordDao iVcodeScanRecordDao;
	
	/**
	 * 重置用户扫码次数
	 * @param userkey
	 * @param vcodeactivitykey
	 */
	public void resetVcodeScanCounts(String userkey,String vcodeactivitykey){
		Map<String,Object> map = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(userkey)){
			map.put("userkey", userkey);
		}
		if(StringUtils.isNotBlank(vcodeactivitykey)){
			map.put("vcodeactivitykey", vcodeactivitykey);
		}
		iVcodeScanRecordDao.resetVcodeScanCounts(map);
		iVcodeScanRecordDao.resetVcodeScanCountsNew(map);
	}
}
