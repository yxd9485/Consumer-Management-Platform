package com.dbt.platform.autocode.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
/**
 * 码源订单接口Dao
 * @author hanshimeng
 *
 */
public interface IVpsQrcodeOrderDao extends IBaseDao<VpsQrcodeOrder>{

	/**
	 * 码源订单列表
	 * @param map keys:queryBean、pageInfo
	 * @return
	 */
	List<VpsQrcodeOrder> queryForLst(Map<String, Object> map);

	/**
	 * 码源订单列表count
	 * @param map keys:queryBean
	 * @return
	 */
	int queryForCount(Map<String, Object> map);
	
	/** 
	 * 依据码源订单编号获取记录
	 * 
	 * @param orderNo
	 * @return
	 */
	public VpsQrcodeOrder findByOrderNo(String orderNo);
	
	/**
	 * 修改订单状态
	 * @param map：orderKey,orderStatus
	 */
	void updateOrderStatus(Map<String, Object> map);

	/**
	 * 更新码源回传状态 
	 * @param map：orderKey、importFlag、optUserKey
	 */
	void updateOrderImportFlag(Map<String, Object> map);

	/**
	 * 查询码源订单List
	 * @param map
	 * @return
	 */
	List<VpsQrcodeOrder> queryByList(Map<String, Object> map);
	
	/**
	 * 获取大编号
	 * 
	 * @param map keys:startIndex
	 * @return
	 */
	public int findMaxOrderNo(Map<String, Object> map);
	
	/**
	 * 获取未生成或生成失败的2分钟前自动下单的码源订单
	 */
	public List<VpsQrcodeOrder> queryFailedOrderForAuto();
	
	/**
	 * 自动码源订单相关批次绑定活动并激活
	 */
	public void updateAutoQrcodeQrderBatchBindActivity();

    List<VpsQrcodeOrder> queryExportDataForLst(Map<String, Object> map);
}
