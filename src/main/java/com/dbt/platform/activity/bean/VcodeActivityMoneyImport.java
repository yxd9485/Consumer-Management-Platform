/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 *
 * Original Author: Administrator
 *
 * ChangeLog:
 * 2016-11-25 by Administrator create
 * </pre>
 */
package com.dbt.platform.activity.bean;

/**
 * The Class VcodeActivityMoneyImport.
 *
 * @author RoyFu
 * @createTime 2016年1月25日 下午4:16:08
 * @description 
 */

public class VcodeActivityMoneyImport {
    //addby zhangbin 20200903
    private String InfoKey;
    private String scanType;
    private String randomType;
    private String prizeType;
    private String isWaitActivation;
    private Double minMoney;
    private Double maxMoney;
    private Double unitMoney;
    private Integer minVpoints;
    private Integer maxVpoints;
    private Double unitVpoints;
    private Double prizePercent;
    private Double prizePercentWarn;
    private Long amounts;
    private Long rangeVal;
    private String scanNum;
    private String content;
    
    private String vpoints;
    private String money;
    
    /** 卡编号：A\B\C\D\E\F\G\H\I\J **/
    private String cardNo;
    private double prizePayMoney;
    private double prizeDiscount;
    private String allowanceType;
    private double allowanceMoney;

    private String isScanqrcodeWaitActivation;
    private String waitActivationPrizeKey;
    private double waitActivationMinMoney;
    private double waitActivationMaxMoney;



    public String getInfoKey() {
        return InfoKey;
    }

    public void setInfoKey(String infoKey) {
        InfoKey = infoKey;
    }

    public double getPrizePayMoney() {
        return prizePayMoney;
    }
    public void setPrizePayMoney(double prizePayMoney) {
        this.prizePayMoney = prizePayMoney;
    }
    public String getAllowanceType() {
        return allowanceType;
    }
    public void setAllowanceType(String allowanceType) {
        this.allowanceType = allowanceType;
    }
    public double getAllowanceMoney() {
        return allowanceMoney;
    }
    public void setAllowanceMoney(double allowanceMoney) {
        this.allowanceMoney = allowanceMoney;
    }
    public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
    
    public String getScanType() {
        return scanType;
    }
    public void setScanType(String scanType) {
        this.scanType = scanType;
    }
    public String getRandomType() {
        return randomType;
    }
    public void setRandomType(String randomType) {
        this.randomType = randomType;
    }
    public String getPrizeType() {
        return prizeType;
    }
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getIsWaitActivation() {
        return isWaitActivation;
    }

    public void setIsWaitActivation(String isWaitActivation) {
        this.isWaitActivation = isWaitActivation;
    }

    public Double getMinMoney() {
        return minMoney;
    }
    public void setMinMoney(Double minMoney) {
        this.minMoney = minMoney;
    }
    public Double getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(Double maxMoney) {
        this.maxMoney = maxMoney;
    }
    public Integer getMinVpoints() {
        return minVpoints;
    }
    public void setMinVpoints(Integer minVpoints) {
        this.minVpoints = minVpoints;
    }
    public Integer getMaxVpoints() {
        return maxVpoints;
    }
    public void setMaxVpoints(Integer maxVpoints) {
        this.maxVpoints = maxVpoints;
    }
    public Double getPrizePercent() {
        return prizePercent;
    }
    public void setPrizePercent(Double prizePercent) {
        this.prizePercent = prizePercent;
    }
    public Double getPrizePercentWarn() {
        return prizePercentWarn;
    }
    public void setPrizePercentWarn(Double prizePercentWarn) {
        this.prizePercentWarn = prizePercentWarn;
    }
    public Long getAmounts() {
        return amounts;
    }
    public void setAmounts(Long amounts) {
        this.amounts = amounts;
    }
    public Long getRangeVal() {
        return rangeVal;
    }
    public void setRangeVal(Long rangeVal) {
        this.rangeVal = rangeVal;
    }
    public String getScanNum() {
        return scanNum;
    }
    public void setScanNum(String scanNum) {
        this.scanNum = scanNum;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getVpoints() {
        return vpoints;
    }
    public void setVpoints(String vpoints) {
        this.vpoints = vpoints;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public Double getUnitMoney() {
        return unitMoney;
    }
    public void setUnitMoney(Double unitMoney) {
        this.unitMoney = unitMoney;
    }
    public Double getUnitVpoints() {
        return unitVpoints;
    }
    public void setUnitVpoints(Double unitVpoints) {
        this.unitVpoints = unitVpoints;
    }
	public double getPrizeDiscount() {
		return prizeDiscount;
	}
	public void setPrizeDiscount(double prizeDiscount) {
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
