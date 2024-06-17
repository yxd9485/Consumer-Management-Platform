package com.dbt.platform.autocode.dto;

import java.util.Date;

/**
 * 用于二维码补录业务
 * @see com.dbt.platform.autocode.action.VpsQrcodeMakeUpAction
 * @author shuDa
 * @date 2021/12/3
 **/
public class VpsQrcodeLibNameSaveDTO {

    private String vcodeKey;
    private String vcodeActivityKey;
    private String companyKey;
    private String packKey;
    private String batchKey;
    /** 二维码内容**/
    private String qrcodeContent;
    /** 二维码类型 0.普通二维码；1.指定金额二维码*/
    private String qrcodeType;
    /** 码库表名**/
    private String libName;
    /**创建时间*/
    private Date creDate;
    private Integer fixedAmount;
    private Integer moerScanKey;
    private String useStatus;
    private Integer ticketCode;
    /** 批次序号*/
    private String batchAutocodeNo;


    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public Integer getMoerScanKey() {
        return moerScanKey;
    }

    public void setMoerScanKey(Integer moerScanKey) {
        this.moerScanKey = moerScanKey;
    }

    public Integer getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(Integer ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getVcodeKey() {
        return vcodeKey;
    }

    public void setVcodeKey(String vcodeKey) {
        this.vcodeKey = vcodeKey;
    }

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getPackKey() {
        return packKey;
    }

    public void setPackKey(String packKey) {
        this.packKey = packKey;
    }

    public String getBatchKey() {
        return batchKey;
    }

    public void setBatchKey(String batchKey) {
        this.batchKey = batchKey;
    }

    public String getQrcodeContent() {
        return qrcodeContent;
    }

    public void setQrcodeContent(String qrcodeContent) {
        this.qrcodeContent = qrcodeContent;
    }

    public String getQrcodeType() {
        return qrcodeType;
    }

    public void setQrcodeType(String qrcodeType) {
        this.qrcodeType = qrcodeType;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public Date getCreDate() {
        return creDate;
    }

    public void setCreDate(Date creDate) {
        this.creDate = creDate;
    }

    public Integer getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(Integer fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getBatchAutocodeNo() {
        return batchAutocodeNo;
    }

    public void setBatchAutocodeNo(String batchAutocodeNo) {
        this.batchAutocodeNo = batchAutocodeNo;
    }
}
