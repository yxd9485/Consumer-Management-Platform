package com.dbt.smallticket.bean;



import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.MathUtil;
/**
 * 每日Excel
 */
public class VpsTicketDayExcel extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String bigAreaName = "";
	private String warAreaName;
	// Ka人数
	private int kaNum = 0;
	// 酒行人数
	private int wineNum= 0;
	// 餐饮人数
	private int restaurantNum= 0;
	// 微信关注人数
	private int subscribeNum = 0;
	// 电商人数
	private int retailersNum= 0;
	//每日销售额（元）
	private Double daySalesMoney= 0.0;
	//每日销售数量
	private int daySalesNum= 0;
	//累计销售额（元）
	private Double totalSalesMoney= 0.0;
	//累计礼品兑换数量
	private int totalSalesNum= 0;
	
	public VpsTicketDayExcel(){
		
	}
	
	
	/**
	 * 增加大区合计 行
	 * @param total
	 */
	public VpsTicketDayExcel(VpsTicketDayExcel total){
		this.bigAreaName = "";
		this.warAreaName = "小计";
		this.kaNum = total.getKaNum();
		this.wineNum = total.getWineNum();
		this.restaurantNum = total.getRestaurantNum();
		this.retailersNum = total.getRetailersNum();
		this.daySalesNum =total.getDaySalesNum();
		this.totalSalesNum = total.getTotalSalesNum();
		this.daySalesMoney = total.getDaySalesMoney();
		this.totalSalesMoney = total.getTotalSalesMoney();
		this.subscribeNum = total.getSubscribeNum();
	}
	
	public void clearTotal() {
		this.kaNum = 0;
		this.wineNum = 0;
		this.restaurantNum = 0;
		this.retailersNum = 0;
		this.daySalesMoney = 0.0;
		this.daySalesNum =0;
		this.totalSalesMoney =0.0;
		this.totalSalesNum = 0;
		this.subscribeNum = 0;
	}
	
	public void totalNum(VpsTicketDayExcel entity) {
		this.kaNum += entity.getKaNum();
		this.wineNum += entity.getWineNum();
		this.restaurantNum += entity.getRestaurantNum();
		this.retailersNum += entity.getRetailersNum();
		this.daySalesNum +=entity.getDaySalesNum();
		this.totalSalesNum += entity.getTotalSalesNum();
		this.subscribeNum += entity.getSubscribeNum();
		this.daySalesMoney = MathUtil.add(this.daySalesMoney, entity.getDaySalesMoney());
		this.totalSalesMoney = MathUtil.add(this.totalSalesMoney, entity.getTotalSalesMoney());
		
	}
	
	public String getBigAreaName() {
		return bigAreaName;
	}
	public void setBigAreaName(String bigAreaName) {
		this.bigAreaName = bigAreaName;
	}
	public String getWarAreaName() {
		return warAreaName;
	}
	public void setWarAreaName(String warAreaName) {
		this.warAreaName = warAreaName;
	}
	public int getKaNum() {
		return kaNum;
	}
	public void setKaNum(int kaNum) {
		this.kaNum = kaNum;
	}
	public int getWineNum() {
		return wineNum;
	}
	public void setWineNum(int wineNum) {
		this.wineNum = wineNum;
	}
	public int getRestaurantNum() {
		return restaurantNum;
	}
	public void setRestaurantNum(int restaurantNum) {
		this.restaurantNum = restaurantNum;
	}
	public int getRetailersNum() {
		return retailersNum;
	}
	public void setRetailersNum(int retailersNum) {
		this.retailersNum = retailersNum;
	}
	public Double getDaySalesMoney() {
		return daySalesMoney;
	}
	public void setDaySalesMoney(Double daySalesMoney) {
		this.daySalesMoney = daySalesMoney;
	}
	public int getDaySalesNum() {
		return daySalesNum;
	}
	public void setDaySalesNum(int daySalesNum) {
		this.daySalesNum = daySalesNum;
	}
	public Double getTotalSalesMoney() {
		return totalSalesMoney;
	}
	public void setTotalSalesMoney(Double totalSalesMoney) {
		this.totalSalesMoney = totalSalesMoney;
	}
	public int getTotalSalesNum() {
		return totalSalesNum;
	}
	public void setTotalSalesNum(int totalSalesNum) {
		this.totalSalesNum = totalSalesNum;
	}


	public int getSubscribeNum() {
		return subscribeNum;
	}


	public void setSubscribeNum(int subscribeNum) {
		this.subscribeNum = subscribeNum;
	}
	

	
	
}
