package com.dbt.platform.activity.dao;
  
import java.util.List;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeQrcodePackInfo;
/**
* 文件名:IVcodeQrcodePackInfoDao.java<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
* 描述: V码二维码码包信息dao<br>
* 修改人: cpgu<br>
* 修改时间：2016-04-22 10:22:00<br>
* 修改内容：新增<br>
*/
public interface IVcodeQrcodePackInfoDao extends IBaseDao<VcodeQrcodePackInfo>
{ 
	/**
	 * 根据编码key查询码包信息
	 * @param batchKey 编码key
	 * @return VcodeQrcodePackInfo V码二维码码包信息
	 */
	public VcodeQrcodePackInfo findPackInfoByBatchkey(String batchKey);
	/**
	 * 根据包编码批量修改二维码包码信息
	 * @param list List<VcodeQrcodePackInfo>
	 */
	public void batchUpdatePackInfoByPackKey(VcodeQrcodePackInfo vcodeQrcodePackInfo);
	/**
	 * 批量插入二维码包吗信息
	 * @param list  List<VcodeQrcodePackInfo>
	 */
	public void batchAddPackInfo(List<VcodeQrcodePackInfo> list);
	/**
	 * 根据批次Key获取包
	 * @param batchKey
	 * @return
	 */
	public List<VcodeQrcodePackInfo> findPackListByBatchKey(String batchKey);
	
	/**
	 * 物理删除批次相关的包
	 * 
	 * @param batckKey
	 */
	public void removeByBatchKey(String batckKey);
}