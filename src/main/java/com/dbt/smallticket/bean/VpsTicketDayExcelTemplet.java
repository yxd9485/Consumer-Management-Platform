package com.dbt.smallticket.bean;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.MathUtil;

/**
 * 每日Excel:渠道类型：0酒行渠道、1餐饮渠道、2KA渠道、3电商渠道
 */
public class VpsTicketDayExcelTemplet extends BasicProperties implements Comparable<VpsTicketDayExcelTemplet> {
    private static final long serialVersionUID = 1L;
    private String bigAreaName = "";
    private String warAreaName;
    private int daySaleNum;
    private double daySaleMoney;
    private int totalPersonNum;
    private int totalSaleNum;
    private double totalSaleMoney;
    private int dayPersonNum0;
    private double daySaleMoney0;
    private int dayPersonNum1;
    private int daySaleNum1;
    private double daySaleMoney1;
    private int dayPersonNum2;
    private int daySaleNum2;
    private double daySaleMoney2;
    private int dayPersonNum3;
    private int daySaleMoney3;
    private String commodityCode;
    private String skuName;
    private int rowNum;

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

    public int getDaySaleNum() {
        return daySaleNum;
    }

    public void setDaySaleNum(int daySaleNum) {
        this.daySaleNum = daySaleNum;
    }

    public double getDaySaleMoney() {
        return daySaleMoney;
    }

    public void setDaySaleMoney(double daySaleMoney) {
        this.daySaleMoney = daySaleMoney;
    }

    public int getTotalPersonNum() {
        return totalPersonNum;
    }

    public void setTotalPersonNum(int totalPersonNum) {
        this.totalPersonNum = totalPersonNum;
    }

    public int getTotalSaleNum() {
        return totalSaleNum;
    }

    public void setTotalSaleNum(int totalSaleNum) {
        this.totalSaleNum = totalSaleNum;
    }

    public double getTotalSaleMoney() {
        return totalSaleMoney;
    }

    public void setTotalSaleMoney(double totalSaleMoney) {
        this.totalSaleMoney = totalSaleMoney;
    }

    public int getDayPersonNum0() {
        return dayPersonNum0;
    }

    public void setDayPersonNum0(int dayPersonNum0) {
        this.dayPersonNum0 = dayPersonNum0;
    }

    public double getDaySaleMoney0() {
        return daySaleMoney0;
    }

    public void setDaySaleMoney0(double daySaleMoney0) {
        this.daySaleMoney0 = daySaleMoney0;
    }

    public int getDayPersonNum1() {
        return dayPersonNum1;
    }

    public void setDayPersonNum1(int dayPersonNum1) {
        this.dayPersonNum1 = dayPersonNum1;
    }

    public int getDaySaleNum1() {
        return daySaleNum1;
    }

    public void setDaySaleNum1(int daySaleNum1) {
        this.daySaleNum1 = daySaleNum1;
    }

    public double getDaySaleMoney1() {
        return daySaleMoney1;
    }

    public void setDaySaleMoney1(double daySaleMoney1) {
        this.daySaleMoney1 = daySaleMoney1;
    }

    public int getDayPersonNum2() {
        return dayPersonNum2;
    }

    public void setDayPersonNum2(int dayPersonNum2) {
        this.dayPersonNum2 = dayPersonNum2;
    }

    public int getDaySaleNum2() {
        return daySaleNum2;
    }

    public void setDaySaleNum2(int daySaleNum2) {
        this.daySaleNum2 = daySaleNum2;
    }

    public double getDaySaleMoney2() {
        return daySaleMoney2;
    }

    public void setDaySaleMoney2(double daySaleMoney2) {
        this.daySaleMoney2 = daySaleMoney2;
    }

    public int getDayPersonNum3() {
        return dayPersonNum3;
    }

    public void setDayPersonNum3(int dayPersonNum3) {
        this.dayPersonNum3 = dayPersonNum3;
    }

    public int getDaySaleMoney3() {
        return daySaleMoney3;
    }

    public void setDaySaleMoney3(int daySaleMoney3) {
        this.daySaleMoney3 = daySaleMoney3;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public int compareTo(VpsTicketDayExcelTemplet o) {
        return (int)MathUtil.round((o.getTotalSaleMoney() - this.getTotalSaleMoney()) * 100, 0);
    }

}
