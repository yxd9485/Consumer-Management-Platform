package com.dbt.platform.autocode.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 批次序号查询记录
 */
public class VpsVcodeBatchSerialNoQueryLog extends BasicProperties {

    private static final long serialVersionUID = 1L;
    
    private String infoKey;
    private String batchSerialNo;
    private String queryUserPhoneNum;
    private String orderKey;
    private String batchKey;
    private String queryNotes;

    // 扩展字段
    private String orderNo;
    private String orderName;
    private String clientOrderNo;
    private String qrcodeManufacture;
    private String qrcodeFactoryName;
    private String qrcodeProductLineName;
    private String qrcodeMachineId;
    private String qrcodeWorkGroup;
    private String skuKey;
    private String skuName;
    private String batchDesc;
    private String startDate;
    private String endDate;
    
    public VpsVcodeBatchSerialNoQueryLog() {
        super();
    }
    
    public VpsVcodeBatchSerialNoQueryLog(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.startDate = paramAry.length > 0 ? paramAry[0] : "";
        this.endDate = paramAry.length > 1 ? paramAry[1] : "";
    }
    
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getBatchSerialNo() {
        return batchSerialNo;
    }
    public void setBatchSerialNo(String batchSerialNo) {
        this.batchSerialNo = batchSerialNo;
    }
    public String getQueryUserPhoneNum() {
        return queryUserPhoneNum;
    }
    public void setQueryUserPhoneNum(String queryUserPhoneNum) {
        this.queryUserPhoneNum = queryUserPhoneNum;
    }
    public String getOrderKey() {
        return orderKey;
    }
    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }
    public String getBatchKey() {
        return batchKey;
    }
    public void setBatchKey(String batchKey) {
        this.batchKey = batchKey;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getOrderName() {
        return orderName;
    }
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
    public String getClientOrderNo() {
        return clientOrderNo;
    }
    public void setClientOrderNo(String clientOrderNo) {
        this.clientOrderNo = clientOrderNo;
    }
    public String getQrcodeManufacture() {
        return qrcodeManufacture;
    }
    public void setQrcodeManufacture(String qrcodeManufacture) {
        this.qrcodeManufacture = qrcodeManufacture;
    }
    public String getQrcodeFactoryName() {
        return qrcodeFactoryName;
    }
    public void setQrcodeFactoryName(String qrcodeFactoryName) {
        this.qrcodeFactoryName = qrcodeFactoryName;
    }
    public String getQrcodeProductLineName() {
        return qrcodeProductLineName;
    }
    public void setQrcodeProductLineName(String qrcodeProductLineName) {
        this.qrcodeProductLineName = qrcodeProductLineName;
    }
    public String getQrcodeMachineId() {
        return qrcodeMachineId;
    }
    public void setQrcodeMachineId(String qrcodeMachineId) {
        this.qrcodeMachineId = qrcodeMachineId;
    }
    public String getQrcodeWorkGroup() {
        return qrcodeWorkGroup;
    }
    public void setQrcodeWorkGroup(String qrcodeWorkGroup) {
        this.qrcodeWorkGroup = qrcodeWorkGroup;
    }
    public String getSkuKey() {
        return skuKey;
    }
    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }
    public String getSkuName() {
        return skuName;
    }
    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
    public String getBatchDesc() {
        return batchDesc;
    }
    public void setBatchDesc(String batchDesc) {
        this.batchDesc = batchDesc;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getQueryNotes() {
        return queryNotes;
    }

    public void setQueryNotes(String queryNotes) {
        this.queryNotes = queryNotes;
    }
}
