package com.dbt.platform.activity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文件名:IVcodeQrcodeBatchInfoDao.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: V码二维码批次信息dao<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-22 10:30:15<br>
 * 修改内容：新增<br>
 */
public interface IVcodeQrcodeBatchInfoDao extends IBaseDao<VcodeQrcodeBatchInfo> {

    /**
     * 生成码源批次列表
     * 
     * @map map keys:queryBean、pageInfo
     */
    public List<VcodeQrcodeBatchInfo> queryForLst(Map<String, Object> map);
    
    /**
     * 生成码源批次列表记录总条数 
     * 
     * @map map keys:queryBean
     */
    public int queryForCount(Map<String, Object> map);
    
    /**
     * 回传码源批次列表
     * 
     * @map map keys:queryBean、pageInfo
     */
    public List<VcodeQrcodeBatchInfo> queryForImportLst(Map<String, Object> map);
    
    /**
     * 回传码源批次列表记录总条数 
     * 
     * @map map keys:queryBean
     */
    public int queryForImportCount(Map<String, Object> map);
    
    /**
     * 已使用回传码源批次列表
     * 
     * @map map keys:queryBean、pageInfo
     */
    public List<VcodeQrcodeBatchInfo> queryForImportUsedLst(Map<String, Object> map);
    
	/**
	 * 已使用回传码源批次列表记录总条数 
	 * 
	 * @map map keys:queryBean
	 */
	public int queryForImportUsedCount(Map<String, Object> map);
	
	/**
	 * 逻辑删除
	 * @param map keys:infoKey、optUserKey
	 */
	public void deleteById(Map<String, Object> map);
	
    /**
     * 物理删除，注意只能删除未生成码源的批次
     * 
     * @param batchKey 批次主键
     */
    public void removeById(String batchKey);


	/**
	 * 根据活动key获取批次列表
	 * @param vcodeActivityKey
	 * @return
	 */
	public List<VcodeQrcodeBatchInfo> findVcodeQrcodeBatchInfoByActivityId(
			String vcodeActivityKey);

	/**
	 * 判断批码说明是否存在
	 * @param batchDesc</br> 
	 * @return Integer </br>
	 */
	public int isExistActivityBatchDesc(String batchDesc);

	/**
	 * 批量添加批次信息
	 * @param </br> 
	 * @return void </br>
	 */
	public void batchAddBatchInfo(List<VcodeQrcodeBatchInfo> list);

	/**
	 * 根据批码编号查询批次信息
	 * @param batchDesc </br> 
	 * @return VcodeQrcodeBatchInfo </br>
	 */
	public VcodeQrcodeBatchInfo findVcodeQrcodeBatchInfoByBatchDesc(String batchDesc);
	
	/**
	 * 依据批次主键列表查询批次信息
	 * 
	 * @param batchKeyList
	 * @return
	 */
	public List<VcodeQrcodeBatchInfo> queryBatchInfoByKey(List<String> batchKeyList);
	
	/**
	 * 批量更新批次时间
	 * @param batchInfoList
	 */
	public void batchUpdateBatchTimeInfo(List<VcodeQrcodeBatchInfo> batchInfoList);
	
	/**
	 * 调整批次更新批次活动key及开始结束日期
	 * 
	 * @param map key:batchKeyList、batchInfo、optUserKey
	 */
	public void updateBatchForAdjust(Map<String, Object> map);
	
	/**
	 * 修改批次所属合同年份
	 * 
	 * @param map key:batchKeyLst、contractYear、contractChangeDate、nextContractYear
	 */
	public void updateContractYear(Map<String, Object> map);

	/**
	 * 移除活动下的批次到码源回传入库
	 * 
	 * @param map key:batchKeyList、optUserKey
	 */
    public void updateBatchBackForImport(Map<String, Object> map);

    /**
     * 查询激活批次list
     * @param map keys:queryBean、pageInfo
     * @return
     */
	public List<VcodeQrcodeBatchInfo> queryActivateBatchList(Map<String, Object> map);

	/**
	 * 查询激活批次Count
	 * @param map keys:queryBean
	 * @return
	 */
	public int queryActivateBatchCount(Map<String, Object> map);
	
	/**
     * 根据码源订单主键查询批次
     * @param map
     * @return
     */
	public List<VcodeQrcodeBatchInfo> queryVcodeQrcodeBatchInfoByOrderKey(String orderKey);
	/**
	 * 根据码源订单主键查询批次 和码源订单前三位
	 * @param map
	 * @return
	 */
	public List<VcodeQrcodeBatchInfo> queryVcodeQrcodeBatchInfoAndPrefixByOrderKey(String orderKey);

	/**
	 * 更新批次信息
	 * @param map：orderKey,batchKeyList
	 */
	public void updateBatchForMap(Map<String, Object> map);

	/**
	 * 清空批次订单主键
	 * @param orderKey
	 */
	public void clearBatchOrderKey(String orderKey);

	/**
	 * 码源生成所需批次
	 * @param orderKey
	 * @return
	 */
	public List<VcodeQrcodeBatchInfo> queryQrcodeBatchByOrderKey(String orderKey);

	/**
	 * 根据订单号修改批次
	 * @param map
	 */
	public void updateBatchForOrderKey(Map<String, Object> map);

	/**
	 * 统计活动下批次快到期数量
	 * @param
	 */
	public List<VcodeQrcodeBatchInfo> queryBatchExpireRemind();

	public void removeByOrderKey(String orderKey);

	public String findExistBatchPrefix(@Param("batchDesc") String batchDesc);
	
	/**
	 * 蒙牛高印更新批次状态
	 * 
	 * @param batchInfo
	 */
	public void updateForMN(VcodeQrcodeBatchInfo batchInfo);

	VcodeQrcodeBatchInfo queryVcodeQrcodeBatchByBatchKey(String batchKey);
}