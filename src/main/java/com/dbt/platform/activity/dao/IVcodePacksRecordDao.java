package com.dbt.platform.activity.dao;
  
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodePacksRecord;
/**
* 文件名:IVcodePacksRecordDao.java<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
* 描述: V码活动扫码红包领取记录<br>
* 修改人: cpgu<br>
* 修改时间：2016-04-26 16:01:39<br>
* 修改内容：新增<br>
*/
public interface IVcodePacksRecordDao extends IBaseDao<VcodePacksRecord>
{ 
	/**
	 * 根据批码下扫码总记录数
	 * @param batchKey 批码key
	 * @return VcodePacksRecord 扫码红包领取记录
	 */
	public VcodePacksRecord queryPacksRecordCountByBatchKey(String batchKey);

	/**
	 * 查询爆点红包List
	 * @param map</br> 
	 * @return List<VpsVcodePacksRecord> </br>
	 */
	public List<VcodePacksRecord> queryEruptRedpacketList(Map<String, Object> map);

	/**
	 * 查询爆点红包count
	 * @param map </br> 
	 * @return int </br>
	 */
	public int queryEruptRedpacketCount(Map<String, Object> map);
}