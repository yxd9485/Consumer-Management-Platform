package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  商城优惠券数据汇总Bean
 */
@SuppressWarnings("serial")
public class VpointsCouponCollect extends BasicProperties {
	/** 优惠券发行量 **/
	private int couponNum;
	/** 优惠券领取量 **/
	private int couponReceiveNum;
	/** 优惠券使用量 **/
	private int useNum;
	/** 独立用户量 **/
	private int userNum;
	/** 用券总成交额 **/
	private double totalPay;
	/** 用券总成交积分 **/
	private int totalVpoints;
	/** 优惠总金额 **/
	private double totalDiscountsPay;
	/** 优惠总积分 **/
	private int totalDiscountsVpoints;
	/** 购买件数 **/
	private int totalNum;
	/** 用券比单价（优惠订单平均金额） **/
	private double orderPayPrice;
	/** 用券比单积分（优惠订单平均积分） **/
	private int orderVpointsPrice;
	/** 费效比（优惠金额占比） **/
	private int discountsPayRatio;
	/** 积分效比（优惠积分占比） **/
	private int discountsVpointsRatio;
	public int getCouponNum() {
		return couponNum;
	}
	public void setCouponNum(int couponNum) {
		this.couponNum = couponNum;
	}
	public int getCouponReceiveNum() {
		return couponReceiveNum;
	}
	public void setCouponReceiveNum(int couponReceiveNum) {
		this.couponReceiveNum = couponReceiveNum;
	}
	public int getUseNum() {
		return useNum;
	}
	public void setUseNum(int useNum) {
		this.useNum = useNum;
	}
	public int getUserNum() {
		return userNum;
	}
	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}
	public double getTotalPay() {
		return totalPay;
	}
	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}
	public int getTotalVpoints() {
		return totalVpoints;
	}
	public void setTotalVpoints(int totalVpoints) {
		this.totalVpoints = totalVpoints;
	}
	public double getTotalDiscountsPay() {
		return totalDiscountsPay;
	}
	public void setTotalDiscountsPay(double totalDiscountsPay) {
		this.totalDiscountsPay = totalDiscountsPay;
	}
	public int getTotalDiscountsVpoints() {
		return totalDiscountsVpoints;
	}
	public void setTotalDiscountsVpoints(int totalDiscountsVpoints) {
		this.totalDiscountsVpoints = totalDiscountsVpoints;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public double getOrderPayPrice() {
		return orderPayPrice;
	}
	public void setOrderPayPrice(double orderPayPrice) {
		this.orderPayPrice = orderPayPrice;
	}
	public int getOrderVpointsPrice() {
		return orderVpointsPrice;
	}
	public void setOrderVpointsPrice(int orderVpointsPrice) {
		this.orderVpointsPrice = orderVpointsPrice;
	}
	public int getDiscountsPayRatio() {
		return discountsPayRatio;
	}
	public void setDiscountsPayRatio(int discountsPayRatio) {
		this.discountsPayRatio = discountsPayRatio;
	}
	public int getDiscountsVpointsRatio() {
		return discountsVpointsRatio;
	}
	public void setDiscountsVpointsRatio(int discountsVpointsRatio) {
		this.discountsVpointsRatio = discountsVpointsRatio;
	}
}
