package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu
 * @createTime 2016年1月19日 下午5:43:09
 * @description V码活动积分分布配置
 */

@SuppressWarnings("serial")
public class VcodeActivityVpointsCog extends BasicProperties {
	
	/** 主鍵 */
	private String vpointsCogKey;
	/** 活動主鍵 */
	private String vcodeActivityKey;
	/** 活动规则主键 */
	private String rebateRuleKey;
	/** 扫码类型：0首次扫码、1普通扫码*/
	private String scanType;
	/** 随机类型：0随机、1固定*/
	private String randomType;
	/** 奖品类型 */
	private String prizeType;
    /** 是否为待激活红包 */
    private String isWaitActivation;
	/** 区间最小金额：元*/
	private Double minMoney;
	/** 区间最大金额：元*/
	private Double maxMoney;
	/** 区间最小积分*/
	private Integer minVpoints;
	/** 区间最大积分*/
	private Integer maxVpoints;
	/** 奖项占比*/
	private Double prizePercent;
	/** 奖项占比预警值*/
	private Double prizePercentWarn;
	/** 配置个数 */
	private Long cogAmounts;
	/** 剩余个数 */
	private Long restAmounts;
	/** 显示内容 */
	private String codeContentUrl;
    /** 概率值 **/
    private Long rangeVal;
    private String scanNum;
    /** 集卡编号：A\B\C\D\E\F\G\H\I\J **/
    private String cardNo;
    /** 集卡类型名称*/
    private String cardName;
    /** 大奖需支付金额*/
    private double prizePayMoney;
    /** 尊享卡金额*/
    private double allowanceMoney;
    /** 尊享卡折扣*/
    private double prizeDiscount;
    /** 津贴卡类型*/
    private String allowanceType;
    
    
    //-----------------扩展字段------------------------
    /** 概率占比*/
    private String rangePercent;
    private String vcodeActivityName;
    private String areaCode;
    private String ruleType;
    private String beginDate;
    private String endDate;
    private String beginTime;
    private String endTime;

    private String isScanqrcodeWaitActivation;
    private String waitActivationPrizeKey;
    private double waitActivationMinMoney;
    private double waitActivationMaxMoney;
	
	public VcodeActivityVpointsCog() {
		super();
	}

	public VcodeActivityVpointsCog(String vpointsCogKey, String vcodeActivityKey, String rebateRuleKey,
	        String prizeType, String isWaitActivation, String scanType, String randomType, Integer minVpoints, Integer maxVpoints, Double minMoney,
	        Double maxMoney, Double prizePercent, Double prizePercentWarn, String cardNo, Long cogAmounts, Long restAmounts, Long rangeVal, String scanNum, String codeContentUrl,
	        String cardName, Double prizePayMoney, Double prizeDiscount, String allowanceType, Double allowanceMoney,String isScanqrcodeWaitActivation,String waitActivationPrizeKey,double waitActivationMinMoney,double waitActivationMaxMoney) {
		this.vpointsCogKey = vpointsCogKey;
		this.vcodeActivityKey = vcodeActivityKey;
		this.rebateRuleKey = rebateRuleKey;
		this.prizeType = prizeType;
		this.isWaitActivation = isWaitActivation;
		this.scanType = scanType;
		this.randomType = randomType;
		this.minVpoints = minVpoints;
		this.maxVpoints = maxVpoints;
		this.minMoney = minMoney;
		this.maxMoney = maxMoney;
		this.prizePercent = prizePercent;
		this.prizePercentWarn = prizePercentWarn;
		this.cardNo = cardNo;
		this.cogAmounts = cogAmounts;
		this.restAmounts = restAmounts;
		this.rangeVal = rangeVal;
		this.scanNum = scanNum;
		this.codeContentUrl = codeContentUrl;
		this.cardName = cardName;
		this.prizePayMoney = prizePayMoney;
		this.prizeDiscount = prizeDiscount;
		this.allowanceType = allowanceType;
		this.allowanceMoney = allowanceMoney;
		this.isScanqrcodeWaitActivation = isScanqrcodeWaitActivation;
		this.waitActivationPrizeKey = waitActivationPrizeKey;
		this.waitActivationMinMoney = waitActivationMinMoney;
		this.waitActivationMaxMoney = waitActivationMaxMoney;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getVpointsCogKey() {
        return vpointsCogKey;
    }

    public void setVpointsCogKey(String vpointsCogKey) {
        this.vpointsCogKey = vpointsCogKey;
    }

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }

    public String getRebateRuleKey() {
        return rebateRuleKey;
    }

    public void setRebateRuleKey(String rebateRuleKey) {
        this.rebateRuleKey = rebateRuleKey;
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

    public Long getCogAmounts() {
        return cogAmounts;
    }

    public void setCogAmounts(Long cogAmounts) {
        this.cogAmounts = cogAmounts;
    }

    public Long getRestAmounts() {
        return restAmounts;
    }

    public void setRestAmounts(Long restAmounts) {
        this.restAmounts = restAmounts;
    }

    public String getCodeContentUrl() {
        return codeContentUrl;
    }

    public void setCodeContentUrl(String codeContentUrl) {
        this.codeContentUrl = codeContentUrl;
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

    public String getRangePercent() {
        return rangePercent;
    }

    public void setRangePercent(String rangePercent) {
        this.rangePercent = rangePercent;
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

	public double getPrizeDiscount() {
		return prizeDiscount;
	}

	public void setPrizeDiscount(double prizeDiscount) {
		this.prizeDiscount = prizeDiscount;
	}

    public String getVcodeActivityName() {
        return vcodeActivityName;
    }

    public void setVcodeActivityName(String vcodeActivityName) {
        this.vcodeActivityName = vcodeActivityName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
