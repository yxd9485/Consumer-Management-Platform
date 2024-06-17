package com.dbt.platform.doubtuser.dao;

import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.doubtuser.bean.VCodeScanRecordInfo;

/**
 * 扫码记录统计dao
 * @author:hanshimeng<br>
 * @date:2016-4-21 上午11:27:11<br>
 * @version:1.0.0<br>
 * 
 */
public interface IVCodeScanRecordDao extends IBaseDao<VCodeScanRecordInfo>{
	
	/**
	 * 重置扫码次数-旧表
	 * @param scanRecordInfo
	 */
	public void resetVcodeScanCounts(Map<String,Object> map);

	/**
	 * 新表重置
	 * */
	public void resetVcodeScanCountsNew(Map<String,Object> map);

}
