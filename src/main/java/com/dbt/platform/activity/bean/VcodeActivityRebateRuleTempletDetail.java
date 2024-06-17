package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 活动规则模板奖项配置项明细
 */
public class VcodeActivityRebateRuleTempletDetail extends BasicProperties {
    private static final long serialVersionUID = 6103587664437997322L;
    
    private String infoKey;
    private String templetKey;
    /** 扫码类型：0首次扫码、1普通扫码 */
    private String scanType;
    /** 随机类型：0随机、1固定 */
    private String randomType;
    /** 奖项类型：0现金红包、1:积分红包、2:积分现金红包 */
    private String prizeType;
    private String minMoney;
    private String maxMoney;
    private String minVpoints;
    private String maxVpoints;
    private String prizePercent;
    /** 奖项占比预警值*/
    private String prizePercentWarn;
    private String prizePayMoney;
    private String allowanceType;
    private String allowanceMoney;
    private String prizeDiscount;
    private String scanNum;
    private String cardNo;
    private String cogAmounts;


    private String isScanqrcodeWaitActivation;
    private String waitActivationPrizeKey;
    private double waitActivationMinMoney;
    private double waitActivationMaxMoney;
    
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getTempletKey() {
        return templetKey;
    }
    public void setTempletKey(String templetKey) {
        this.templetKey = templetKey;
    }
    public String getScanType() {
        return scanType;
    }
    public void setScanType(String scanType) {
        this.scanType = scanType;
    }
    public String getPrizeType() {
        return prizeType;
    }
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
    public String getRandomType() {
        return randomType;
    }
    public void setRandomType(String randomType) {
        this.randomType = randomType;
    }
    public String getMinMoney() {
        return minMoney;
    }
    public void setMinMoney(String minMoney) {
        this.minMoney = minMoney;
    }
    public String getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(String maxMoney) {
        this.maxMoney = maxMoney;
    }
    public String getMinVpoints() {
        return minVpoints;
    }
    public void setMinVpoints(String minVpoints) {
        this.minVpoints = minVpoints;
    }
    public String getMaxVpoints() {
        return maxVpoints;
    }
    public void setMaxVpoints(String maxVpoints) {
        this.maxVpoints = maxVpoints;
    }
    public String getPrizePercent() {
        return prizePercent;
    }
    public void setPrizePercent(String prizePercent) {
        this.prizePercent = prizePercent;
    }
    public String getPrizePercentWarn() {
        return prizePercentWarn;
    }
    public void setPrizePercentWarn(String prizePercentWarn) {
        this.prizePercentWarn = prizePercentWarn;
    }
    public String getPrizePayMoney() {
        return prizePayMoney;
    }
    public void setPrizePayMoney(String prizePayMoney) {
        this.prizePayMoney = prizePayMoney;
    }
    public String getAllowanceType() {
        return allowanceType;
    }
    public void setAllowanceType(String allowanceType) {
        this.allowanceType = allowanceType;
    }
    public String getAllowanceMoney() {
        return allowanceMoney;
    }
    public void setAllowanceMoney(String allowanceMoney) {
        this.allowanceMoney = allowanceMoney;
    }
    public String getScanNum() {
        return scanNum;
    }
    public void setScanNum(String scanNum) {
        this.scanNum = scanNum;
    }
    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getCogAmounts() {
        return cogAmounts;
    }
    public void setCogAmounts(String cogAmounts) {
        this.cogAmounts = cogAmounts;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
	public String getPrizeDiscount() {
		return prizeDiscount;
	}
	public void setPrizeDiscount(String prizeDiscount) {
		this.prizeDiscount = prizeDiscount;
	}

    public String getIsScanqrcodeWaitActivation() {
        return isScanqrcodeWaitActivation;
    }

    public void setIsScanqrcodeWaitActivation(String isScanqrcodeWaitActivation) {
        this.isScanqrcodeWaitActivation = isScanqrcodeWaitActivation;
    }

    public String getWaitActivationPrizeKey() {
        return waitActivationPrizeKey;
    }

    public void setWaitActivationPrizeKey(String waitActivationPrizeKey) {
        this.waitActivationPrizeKey = waitActivationPrizeKey;
    }

    public double getWaitActivationMinMoney() {
        return waitActivationMinMoney;
    }

    public void setWaitActivationMinMoney(double waitActivationMinMoney) {
        this.waitActivationMinMoney = waitActivationMinMoney;
    }

    public double getWaitActivationMaxMoney() {
        return waitActivationMaxMoney;
    }

    public void setWaitActivationMaxMoney(double waitActivationMaxMoney) {
        this.waitActivationMaxMoney = waitActivationMaxMoney;
    }
}
