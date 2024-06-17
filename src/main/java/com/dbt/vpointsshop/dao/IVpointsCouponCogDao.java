package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.CouponBean;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.bean.VpointsCouponCogGoodsLimit;
import com.dbt.vpointsshop.bean.VpointsCouponCollect;

import feign.Param;

/**
 * 商城优惠券配置表Dao
 */

public interface IVpointsCouponCogDao extends IBaseDao<VpointsCouponCog> {

	public List<VpointsCouponCog> loadActivityList(Map<String, Object> queryMap);
	public int countActivityList(Map<String, Object> queryMap);
	
	/**
	 * 依据ID删除
	 * @param map keys:couponKey、updateUser、updateTime
	 * @return
	 */
	public int deleteById(Map<String, Object> map);
	
	/**
	 * 数据汇总
	 * @param couponKey
	 * @return
	 */
	public VpointsCouponCollect findCouponCollect(@Param("couponKey") String couponKey);
	public List<VpointsCouponCog> queryValidCouponList(Map<String, Object> map);
	public VpointsCouponCog findByCouponCog(String couponKey);
	public void updateReceiveNum(String couponKey);
	/**
	 * 查询全部优惠券
	 * @param batch
	 */
	public List<VpointsCouponCog> queryAllCouponCog();
	
	/**
	 * 依据优惠券主键删除相关的商品限制
	 * @param couponKey
	 */
	public void removeGoodsLimitByCouponKey(String couponKey);
	
	/**
	 * 批量插入优惠券相关的商品限制
	 * @param map couponKey、goodsIdAry、createUser、createTime
	 */
	public void batchWritGoodsLimit(Map<String, Object> map);
	
	/**
	 * 依据优惠券主键获取相关的商品限制
	 * @param couponKey
	 */
	public List<VpointsCouponCogGoodsLimit> queryGoodsLimitByCouponKey(String couponKey);
	
	
}
