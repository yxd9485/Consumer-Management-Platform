package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  商城秒杀活动Bean
 * @author Administrator
 *
 */

@SuppressWarnings("serial")
public class VpointsSeckillOrderStatistics extends BasicProperties {

	/** 订单成交数 **/
	private int orderCount;
	/** 现有库存 **/
	private int goodsRemains;
	/** 秒杀总金额（分） **/
	private int totalMoney;
	/** 秒杀总积分 **/
	private int totalVpoints;
	/** 秒杀优惠总金额（分） **/
	private int discountsMoney;
	/** 秒杀优惠总积分 **/
	private int discountsVpoints;
	
    public VpointsSeckillOrderStatistics() {
        super();
    }

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getGoodsRemains() {
		return goodsRemains;
	}

	public void setGoodsRemains(int goodsRemains) {
		this.goodsRemains = goodsRemains;
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	public int getTotalVpoints() {
		return totalVpoints;
	}

	public void setTotalVpoints(int totalVpoints) {
		this.totalVpoints = totalVpoints;
	}

	public int getDiscountsMoney() {
		return discountsMoney;
	}

	public void setDiscountsMoney(int discountsMoney) {
		this.discountsMoney = discountsMoney;
	}

	public int getDiscountsVpoints() {
		return discountsVpoints;
	}

	public void setDiscountsVpoints(int discountsVpoints) {
		this.discountsVpoints = discountsVpoints;
	}
}
