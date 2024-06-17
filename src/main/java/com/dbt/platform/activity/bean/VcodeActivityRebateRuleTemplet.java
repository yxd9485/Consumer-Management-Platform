package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 活动规则模板主表
 */
public class VcodeActivityRebateRuleTemplet extends BasicProperties {
    private static final long serialVersionUID = -2998826500620691582L;
    
    private String infoKey;
    private String companyKey;
    private String templetName;
    private String unitMoney;
    private String unitVpoints;
    private String firstScanPercent;
    private String commonScanPercent;
    /**津贴卡返利类型：0现金，1积分*/
	private String allowanceaRebateType;
	/** 翻倍卡金额取值区间最小值 **/
	private String allowanceaMinMoney;
	/** 翻倍卡金额取值区间最大值 **/
	private String allowanceaMaxMoney;
	/** 翻倍红包最小积分区间 **/
	private String allowanceaMinVpoints;
	/** 翻倍红包最大积分区间 **/
    private String allowanceaMaxVpoints;
    private String status;


    // 扩展字段
    private String startDate;
    private String endDate;
    private String scanType[];
    private String randomType[];
    private String isWaitActivation[];
    private String minMoney[];
    private String maxMoney[];
    private String fixationMoney[];
    private String minVpoints[];
    private String maxVpoints[];
    private String fixationVpoints[];
    private String prizePercent[];
    private String prizePercentWarn[];
    private String bigPrizeType[];
    private String prizePayMoney[];
    private String allowanceType[];
    private String allowanceMoney[];
    private String prizeDiscount[];
    private String scanNum[];
    private String cardNo[];
    private String cogAmounts[];
    private String waitActivationPrizeKey[];
    private String minWaitActivationMoney[];
    private String maxWaitActivationMoney[];
    private List<VcodeActivityRebateRuleTempletDetail> firstDetailLst;
    private List<VcodeActivityRebateRuleTempletDetail> commonDetailLst;
    
    public VcodeActivityRebateRuleTemplet() {
        
    }
    
    public VcodeActivityRebateRuleTemplet(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.templetName = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
        this.status = paramAry.length > 3 ? paramAry[3] : "";
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getTempletName() {
        return templetName;
    }

    public void setTempletName(String templetName) {
        this.templetName = templetName;
    }

    public String getUnitMoney() {
        return unitMoney;
    }

    public void setUnitMoney(String unitMoney) {
        this.unitMoney = unitMoney;
    }

    public String getUnitVpoints() {
        return unitVpoints;
    }

    public void setUnitVpoints(String unitVpoints) {
        this.unitVpoints = unitVpoints;
    }

    public String getFirstScanPercent() {
        return firstScanPercent;
    }

    public void setFirstScanPercent(String firstScanPercent) {
        this.firstScanPercent = firstScanPercent;
    }

    public String getCommonScanPercent() {
        return commonScanPercent;
    }

    public void setCommonScanPercent(String commonScanPercent) {
        this.commonScanPercent = commonScanPercent;
    }

    public String getAllowanceaMinMoney() {
        return allowanceaMinMoney;
    }

    public void setAllowanceaMinMoney(String allowanceaMinMoney) {
        this.allowanceaMinMoney = allowanceaMinMoney;
    }

    public String getAllowanceaMaxMoney() {
        return allowanceaMaxMoney;
    }

    public void setAllowanceaMaxMoney(String allowanceaMaxMoney) {
        this.allowanceaMaxMoney = allowanceaMaxMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String[] getScanType() {
        return scanType;
    }

    public void setScanType(String[] scanType) {
        this.scanType = scanType;
    }

    public String[] getRandomType() {
        return randomType;
    }

    public void setRandomType(String[] randomType) {
        this.randomType = randomType;
    }

    public String[] getIsWaitActivation() {
        return isWaitActivation;
    }

    public void setIsWaitActivation(String[] isWaitActivation) {
        this.isWaitActivation = isWaitActivation;
    }

    public String[] getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(String[] minMoney) {
        this.minMoney = minMoney;
    }

    public String[] getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(String[] maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String[] getFixationMoney() {
        return fixationMoney;
    }

    public void setFixationMoney(String[] fixationMoney) {
        this.fixationMoney = fixationMoney;
    }

    public String[] getMinVpoints() {
        return minVpoints;
    }

    public void setMinVpoints(String[] minVpoints) {
        this.minVpoints = minVpoints;
    }

    public String[] getMaxVpoints() {
        return maxVpoints;
    }

    public void setMaxVpoints(String[] maxVpoints) {
        this.maxVpoints = maxVpoints;
    }

    public String[] getFixationVpoints() {
        return fixationVpoints;
    }

    public void setFixationVpoints(String[] fixationVpoints) {
        this.fixationVpoints = fixationVpoints;
    }

    public String[] getPrizePercent() {
        return prizePercent;
    }

    public void setPrizePercent(String[] prizePercent) {
        this.prizePercent = prizePercent;
    }

    public String[] getPrizePercentWarn() {
        return prizePercentWarn;
    }

    public void setPrizePercentWarn(String[] prizePercentWarn) {
        this.prizePercentWarn = prizePercentWarn;
    }

    public List<VcodeActivityRebateRuleTempletDetail> getFirstDetailLst() {
        return firstDetailLst;
    }

    public void setFirstDetailLst(List<VcodeActivityRebateRuleTempletDetail> firstDetailLst) {
        this.firstDetailLst = firstDetailLst;
    }

    public List<VcodeActivityRebateRuleTempletDetail> getCommonDetailLst() {
        return commonDetailLst;
    }

    public void setCommonDetailLst(List<VcodeActivityRebateRuleTempletDetail> commonDetailLst) {
        this.commonDetailLst = commonDetailLst;
    }

    public String[] getBigPrizeType() {
        return bigPrizeType;
    }

    public void setBigPrizeType(String[] bigPrizeType) {
        this.bigPrizeType = bigPrizeType;
    }

    public String[] getPrizePayMoney() {
        return prizePayMoney;
    }

    public void setPrizePayMoney(String[] prizePayMoney) {
        this.prizePayMoney = prizePayMoney;
    }

    public String[] getAllowanceType() {
        return allowanceType;
    }

    public void setAllowanceType(String[] allowanceType) {
        this.allowanceType = allowanceType;
    }

    public String[] getAllowanceMoney() {
        return allowanceMoney;
    }

    public void setAllowanceMoney(String[] allowanceMoney) {
        this.allowanceMoney = allowanceMoney;
    }
    public String[] getScanNum() {
        return scanNum;
    }

    public void setScanNum(String[] scanNum) {
        this.scanNum = scanNum;
    }

    public String[] getCardNo() {
        return cardNo;
    }

    public void setCardNo(String[] cardNo) {
        this.cardNo = cardNo;
    }

    public String[] getCogAmounts() {
        return cogAmounts;
    }

    public void setCogAmounts(String[] cogAmounts) {
        this.cogAmounts = cogAmounts;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

	public String getAllowanceaRebateType() {
		return allowanceaRebateType;
	}

	public void setAllowanceaRebateType(String allowanceaRebateType) {
		this.allowanceaRebateType = allowanceaRebateType;
	}

	public String getAllowanceaMinVpoints() {
		return allowanceaMinVpoints;
	}

	public void setAllowanceaMinVpoints(String allowanceaMinVpoints) {
		this.allowanceaMinVpoints = allowanceaMinVpoints;
	}

	public String getAllowanceaMaxVpoints() {
		return allowanceaMaxVpoints;
	}

	public void setAllowanceaMaxVpoints(String allowanceaMaxVpoints) {
		this.allowanceaMaxVpoints = allowanceaMaxVpoints;
	}

	public String[] getPrizeDiscount() {
		return prizeDiscount;
	}

	public void setPrizeDiscount(String[] prizeDiscount) {
		this.prizeDiscount = prizeDiscount;
	}

	public void setRebateRuleTemplet(VcodeActivityRebateRuleTemplet rebateRuleTemplet){
		this.scanType=StringUtils.join(rebateRuleTemplet.getScanType(), ",").split(",");
		this.fixationMoney =StringUtils.join(rebateRuleTemplet.getFixationMoney(), ",").split(",");
		this.randomType = StringUtils.join(rebateRuleTemplet.getRandomType(), ",").split(",");
		this.minMoney = StringUtils.join(rebateRuleTemplet.getMinMoney(), ",").split(",");
	}

    public String[] getWaitActivationPrizeKey() {
        return waitActivationPrizeKey;
    }

    public void setWaitActivationPrizeKey(String[] waitActivationPrizeKey) {
        this.waitActivationPrizeKey = waitActivationPrizeKey;
    }

    public String[] getMinWaitActivationMoney() {
        return minWaitActivationMoney;
    }

    public void setMinWaitActivationMoney(String[] minWaitActivationMoney) {
        this.minWaitActivationMoney = minWaitActivationMoney;
    }

    public String[] getMaxWaitActivationMoney() {
        return maxWaitActivationMoney;
    }

    public void setMaxWaitActivationMoney(String[] maxWaitActivationMoney) {
        this.maxWaitActivationMoney = maxWaitActivationMoney;
    }
}
