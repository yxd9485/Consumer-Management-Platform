package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;

public class VpointsCouponReceiveStatistics extends BasicProperties {

    private static final long serialVersionUID = 1L;
    /** */
    private String infoKey;
    /** 用户主键*/
    private String userKey;
    /** 优惠券主键*/
    private String couponKey;
    /** 当天领取数量*/
    private int dayNum;
    /** 当周领取数量*/
    private int weekNum;
    /** 当月领取数量*/
    private int monthNum;
    /** 累计领取数量*/
    private int totalNum;
    /** 最后领取时间*/
    private String lastReceiveTime;
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getCouponKey() {
        return couponKey;
    }
    public void setCouponKey(String couponKey) {
        this.couponKey = couponKey;
    }
    public int getDayNum() {
        return dayNum;
    }
    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }
    public int getWeekNum() {
        return weekNum;
    }
    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }
    public int getMonthNum() {
        return monthNum;
    }
    public void setMonthNum(int monthNum) {
        this.monthNum = monthNum;
    }
    public int getTotalNum() {
        return totalNum;
    }
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
    public String getLastReceiveTime() {
        return lastReceiveTime;
    }
    public void setLastReceiveTime(String lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
