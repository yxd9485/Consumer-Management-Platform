package com.dbt.vpointsshop.dao;

import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsPrizeBean;
import com.dbt.vpointsshop.bean.VpointsRechargeLog;
import com.dbt.vpointsshop.bean.VpointsSeckillOrderStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VpointsExchangeDao extends IBaseDao<VpointsExchangeLog>{
	public List<VpointsExchangeLog> getExchangeList(Map<String,Object> map);
	public int getExchangeCount(Map<String,Object> map);
	public List<VpointsPrizeBean> getPrizeList(Map<String,Object> map);
	public int getPrizeCount(Map<String,Object> map);
	public List<VpointsRechargeLog> getRechargeList(Map<String,Object> map);
	public int getRechargeCount(Map<String,Object> map);
	public String getNickNameByKey(String userKey);
	public String getOpenidByKey(String userKey);
	
	/**
	 * 订单管理列表 
	 * 
	 * @param map keys:queryBean、pageInfo
	 * @return
	 */
	public List<VpointsExchangeLog> queryForExpressLst(Map<String, Object> map);
	
	/**
	 * 订单管理列表记录条数
	 * 
	 * @param map keys:queryBean
	 * @return
	 */
	public int queryForExpressCount(Map<String, Object> map);
	
	/** 
	 * 更新退货申请审核状态
	 * 
	 * @param map keys:exchangeId、goodsReturnAudit、goodsReturnAuditUser、exchangeStatus
	 */
	public void updateGoodsReturnAudit(Map<String, Object> map);
	
	/** 
	 * 更新订单的物流信息
	 * 
	 * @param map keys:exchangeId、expressCompany、expressNumber
	 */
	public void updateExpressInfo(Map<String, Object> map);
	
	/**
	 * 更新订单签收状态，发货后15天
	 */
	public void updateExpressSignJob(Map<String, Object> map);
	
	/**
	 * 获取截止日期前所有未核销订单
	 * 
	 * @param map keys:verificationEndDate
	 * @return
	 */
	public List<VpointsExchangeLog> queryVerificationByDate(Map<String, Object> map);
	
	/**
	 * 更新核销主键
	 * 
	 * @param map keys:exchangeId、verificationId
	 * @return
	 */
	public void updateVerificationId(Map<String, Object> map);
	
	/**
	 * 清除核销主键
	 * 
	 * @param map keys:verificationId
	 * @return
	 */
	public void updateVerificationIdForClear(Map<String, Object> map);
	
	/**
	 * 更新订单的收货信息
	 * @param map
	 */
	public void updateOrderAddress(Map<String, Object> map);
	
	/**
	 * 更新订单状态
	 * @param map:exchangeId,exchangeStatus
	 */
	public void updateOrderStatus(Map<String, Object> map);
	
	/**
	 * 查询订单汇总数据
	 * @param seckillActivityKey
	 * @return
	 */
	public VpointsSeckillOrderStatistics findOrderStatistics(Map<String, Object> map);

    List<VpointsExchangeLog> queryAllNotReceiveGiftCardOrder(@Param("days")Integer days);

    VpointsExchangeLog queryExchangeLogByGiftCardExchangeId(@Param("giftCardExchangeId") String giftCardExchangeId);

    void updateSfJsdExchangeOrder(JSONObject result);

	List<VpointsExchangeLog> findByShopOrderId(String shopOrderId);
}
