package com.dbt.platform.activity.dao;
  
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityLibRelation;
/**
* 文件名:IVcodeActivityLibRelationDao.java<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
* 描述: V码活动活动与码库关联dao<br>
* 修改人: cpgu<br>
* 修改时间：2016-04-22 10:37:12<br>
* 修改内容：新增<br>
*/
public interface IVcodeActivityLibRelationDao extends IBaseDao<VcodeActivityLibRelation>
{ 
	/**
	 * 检验随机标识码是否唯一
	 * @return int 活动唯一标识码
	 */
	public int checkUniqueCode(String vcodeUniqueCode);
	
	/**
	 * 查询所有活动批次编码
	 * @return List<String> 活动批次编码list
	 */
	public List<String> queryAllCodeList();
	
	/**
	 * 查询所有码库名称
	 * @return List<String> 有码库名称list
	 */
	public List<String> queryAllLibNameList();
	
	/**
	 * 批量入库V码活动活动与码库关联关系
	 * @param list V码活动活动与码库list
	 */
	public void batchAddActivityLibRelation(List<VcodeActivityLibRelation> list);
	
	/**
	 * 根据批次查询
	 * @param batchKey </br> 
	 * @return VcodeActivityLibRelation </br>
	 */
	public VcodeActivityLibRelation findLibRelationBybatchKey(String batchKey);

	/**
	 * 根据码库标识查询
	 * @param vcodeUniqueCode 码库标识</br> 
	 * @return VcodeActivityLibRelation </br>
	 */
	public VcodeActivityLibRelation findLibRelationByUniqueCode(String vcodeUniqueCode);

	/**
	 * 查询一码多扫相关的码库表
	 * @param map
	 * @return
	 */
	public List<VcodeActivityLibRelation> queryListForMoreSweep(Map<String, Object> map);

    /**
     * 物理删除批次相关的记录
     * 
     * @param batckKey
     */
    public void removeByBatchKey(String batckKey);
}