package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.vpointsshop.bean.CouponBean;
import com.dbt.vpointsshop.bean.VpointsCouponBatch;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.bean.VpointsCouponReceiveRecord;
import com.dbt.vpointsshop.bean.VpointsCouponReceiveStatistics;

public interface VpointsCouponDao {
	/**
	 * 获取批次列表
	 * @param map
	 * @return
	 */
	public List<VpointsCouponBatch> getBatchList(Map<String,Object> map);
	/**
	 * 获取批次数量
	 * @param map
	 * @return
	 */
	public int getBatchCount(Map<String,Object> map);
	/**
	 * 查询使用该批次的商品
	 * @param batchKey
	 * @return
	 */
	public int getGoodsByBatch(String batchKey);
	/**
	 * 获取批次所在表
	 * @param batchKey
	 * @return
	 */
	public String getTalbeByBatch(String batchKey);
	/**
	 * 根据批次获取电子券
	 * @param map
	 * @return
	 */
	public int getCouponByBatch(Map<String,Object> map);
	/**
	 * 删除批次
	 * @param batchKey
	 */
	public void delBatchByKey(String batchKey);
	/**
	 * 删除批次下电子券
	 * @param map
	 */
	public void delCouponByBatch(Map<String,Object> map);
	/**
	 * 保存批次
	 * @param batch
	 */
	public void saveBatch(VpointsCouponBatch batch);
	/**
	 * 保存电子券
	 * @param map
	 */
	public void saveCoupon(Map<String, Object> map);
	/**
	 * 更新批次
	 * @param batch
	 */
	public void updateBatch(VpointsCouponBatch batch);
	
	
	 /**
     * 更新优惠券配置表
     * @param map keys:inofKey
     */
    public void updateReceiveNum(String couponKey);
    
    /**
     * 更新优惠券情况记录表
     * @param map keys:inofKey
     */
    public void updateReceiveStatisticsNum(String infoKey);
    
    /**
     * 获取用户指定优惠券的领取情况
     * @param map keys:userKey、couponKey
     * @return
     */
    public VpointsCouponReceiveStatistics queryByCouponStatistics(Map<String, Object> map);
    
	
	
	public void addReceiveStatistics(VpointsCouponReceiveStatistics statistics);
}

