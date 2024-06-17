package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 积分商场实物奖兑换核销表
 */
public class VpointsVerificationInfo extends BasicProperties {
    private static final long serialVersionUID = 4986132291402166622L;
    
    /** 核销编号*/
    private String verificationId;
    /** 商品种类个数*/
    private Integer goodsTypeNum;
    /** 商品个数*/
    private Integer goodsNum;
    /** 兑换总积分*/
    private Long totalVpoints;
    /** 商品总金额：单位元*/
    private Double totalMoney;
    /** 核销周期开始日期*/
    private String startDate;
    /** 核销周期结束日期*/
    private String endDate;
    /** 核销状态：0待核销、1已核销、2已终止核销*/
    private String status;
    private String tabsFlag;
    
    public VpointsVerificationInfo() {
        
    }
    
    public VpointsVerificationInfo(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.verificationId = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
        this.status = paramAry.length > 3 ? paramAry[3] : "";
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public Integer getGoodsTypeNum() {
        return goodsTypeNum;
    }

    public void setGoodsTypeNum(Integer goodsTypeNum) {
        this.goodsTypeNum = goodsTypeNum;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Long getTotalVpoints() {
        return totalVpoints;
    }

    public void setTotalVpoints(Long totalVpoints) {
        this.totalVpoints = totalVpoints;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTabsFlag() {
        return tabsFlag;
    }

    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
