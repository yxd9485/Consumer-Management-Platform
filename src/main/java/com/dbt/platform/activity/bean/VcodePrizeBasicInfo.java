package com.dbt.platform.activity.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BaseBean;

/**
 * @Description: 奖品信息表
 * @Author bin.zhang
 **/
public class VcodePrizeBasicInfo extends BaseBean {

    private String infoKey;
    private String prizeNo;
    private String prizeName;
    private String prizeShortName;
    private String prizeValue;
    private String prizeEndTime;
    private String cashPrizeEndTime;
    private String cashPrizeEndDay;
    private String isRecycle;
    private String prizeType;
    private String prizeWinPic;
    private String prizeEarnPic;
    private String prizeListPic;
    private String verificationType;
    private String isCheckCaptcha;
    private String isIdcard;
    private String isPhone;
    private String isAddress;
    private String isName;
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    //兑奖截止时间
    private String cashPrize;
    private String isMsg;
    private String winPrizeTime;
    private String prizeContent;
    private String lxPrizeType;
    /**-----------拓展字段----------------------- **/
    /**门店主键 **/
    private String terminalKey;
    /**门店状态 **/
    private String status;
    /**兑奖状态 **/
    private String cashStatus;
    /**
     * 兑奖咨询电话
     */
    private String cashPrizeTel;
    /**
     * 兑奖咨询时间
     */
    private String cashAdvisoryTime;
    /**  每天中出限制次数*/
    private int dayLimitNum;
    /** 当前奖项每人中奖限制次数类型：0每周、1每月*/
    private String everyoneLimitType;
    /**  当前奖项每人中奖限制次数*/
    private int everyoneLimitNum;
    
    /** 实物是否兑换红包：0否，1是 **/
    private String isExchangeMoney;
    /** 红包最低金额 **/
    private double exchangeMinMoney;
    /** 红包最高金额 **/
    private double exchangeMaxMoney;
    
    public VcodePrizeBasicInfo(){}
    public VcodePrizeBasicInfo(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.prizeNo = paramAry.length > 0 ? paramAry[0] : "";
        this.prizeType = paramAry.length > 1 ? paramAry[1] : "";
        this.isExchangeMoney = paramAry.length > 2 ? paramAry[2] : "";
    }
    
    public void setPrizeBasicQueryParam(String queryParam, String terminalKey){
    	 String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
         this.prizeNo = paramAry.length > 0 ? paramAry[0] : "";
         this.prizeName = paramAry.length > 1 ? paramAry[1] : "";
         this.prizeType = paramAry.length > 2 ? paramAry[2] : "";
         this.status = paramAry.length > 3 ? paramAry[3] : "";
         this.terminalKey = terminalKey;
    }

    public String getPrizeContent() {
        return prizeContent;
    }

    public void setPrizeContent(String prizeContent) {
        this.prizeContent = prizeContent;
    }

    public String getIsMsg() {
        return isMsg;
    }

    public void setIsMsg(String isMsg) {
        this.isMsg = isMsg;
    }

    public String getWinPrizeTime() {
        return winPrizeTime;
    }

    public void setWinPrizeTime(String winPrizeTime) {
        this.winPrizeTime = winPrizeTime;
    }

    public String getCashPrize() {
        return cashPrize;
    }

    public void setCashPrize(String cashPrize) {
        this.cashPrize = cashPrize;
    }

    public String getPrizeShortName() {
        return prizeShortName;
    }

    public void setPrizeShortName(String prizeShortName) {
        this.prizeShortName = prizeShortName;
    }

    public String getPrizeValue() {
        return prizeValue;
    }

    public void setPrizeValue(String prizeValue) {
        this.prizeValue = prizeValue;
    }

    public String getPrizeEndTime() {
        return prizeEndTime;
    }

    public void setPrizeEndTime(String prizeEndTime) {
        this.prizeEndTime = prizeEndTime;
    }

    public String getCashPrizeEndTime() {
        return cashPrizeEndTime;
    }

    public void setCashPrizeEndTime(String cashPrizeEndTime) {
        this.cashPrizeEndTime = cashPrizeEndTime;
    }

    public String getCashPrizeEndDay() {
        return cashPrizeEndDay;
    }

    public void setCashPrizeEndDay(String cashPrizeEndDay) {
        this.cashPrizeEndDay = cashPrizeEndDay;
    }

    public String getIsRecycle() {
        return isRecycle;
    }

    public void setIsRecycle(String isRecycle) {
        this.isRecycle = isRecycle;
    }

    public String getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(String isPhone) {
        this.isPhone = isPhone;
    }

    public String getIsAddress() {
        return isAddress;
    }

    public void setIsAddress(String isAddress) {
        this.isAddress = isAddress;
    }

    public String getIsName() {
        return isName;
    }

    public void setIsName(String isName) {
        this.isName = isName;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getPrizeNo() {
        return prizeNo;
    }

    public void setPrizeNo(String prizeNo) {
        this.prizeNo = prizeNo;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeWinPic() {
        return prizeWinPic;
    }

    public void setPrizeWinPic(String prizeWinPic) {
        this.prizeWinPic = prizeWinPic;
    }

    public String getPrizeEarnPic() {
        return prizeEarnPic;
    }

    public void setPrizeEarnPic(String prizeEarnPic) {
        this.prizeEarnPic = prizeEarnPic;
    }

    public String getPrizeListPic() {
        return prizeListPic;
    }

    public void setPrizeListPic(String prizeListPic) {
        this.prizeListPic = prizeListPic;
    }

    public String getVerificationType() {
        return verificationType;
    }

    public void setVerificationType(String verificationType) {
        this.verificationType = verificationType;
    }

    public String getIsCheckCaptcha() {
        return isCheckCaptcha;
    }

    public void setIsCheckCaptcha(String isCheckCaptcha) {
        this.isCheckCaptcha = isCheckCaptcha;
    }

    public String getIsIdcard() {
        return isIdcard;
    }

    public void setIsIdcard(String isIdcard) {
        this.isIdcard = isIdcard;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
	public String getTerminalKey() {
		return terminalKey;
	}
	public void setTerminalKey(String terminalKey) {
		this.terminalKey = terminalKey;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCashStatus() {
		return cashStatus;
	}
	public void setCashStatus(String cashStatus) {
		this.cashStatus = cashStatus;
	}

    public String getCashPrizeTel() {
        return cashPrizeTel;
    }

    public void setCashPrizeTel(String cashPrizeTel) {
        this.cashPrizeTel = cashPrizeTel;
    }

    public String getCashAdvisoryTime() {
        return cashAdvisoryTime;
    }

    public void setCashAdvisoryTime(String cashAdvisoryTime) {
        this.cashAdvisoryTime = cashAdvisoryTime;
    }
    public int getDayLimitNum() {
        return dayLimitNum;
    }
    public void setDayLimitNum(int dayLimitNum) {
        this.dayLimitNum = dayLimitNum;
    }
    public String getEveryoneLimitType() {
        return everyoneLimitType;
    }
    public void setEveryoneLimitType(String everyoneLimitType) {
        this.everyoneLimitType = everyoneLimitType;
    }
    public int getEveryoneLimitNum() {
        return everyoneLimitNum;
    }
    public void setEveryoneLimitNum(int everyoneLimitNum) {
        this.everyoneLimitNum = everyoneLimitNum;
    }
    public String getIsExchangeMoney() {
		return isExchangeMoney;
	}
	public void setIsExchangeMoney(String isExchangeMoney) {
		this.isExchangeMoney = isExchangeMoney;
	}
    public double getExchangeMinMoney() {
        return exchangeMinMoney;
    }
    public void setExchangeMinMoney(double exchangeMinMoney) {
        this.exchangeMinMoney = exchangeMinMoney;
    }
    public double getExchangeMaxMoney() {
        return exchangeMaxMoney;
    }
    public void setExchangeMaxMoney(double exchangeMaxMoney) {
        this.exchangeMaxMoney = exchangeMaxMoney;
    }
    public String getLxPrizeType() {
        return lxPrizeType;
    }
    public void setLxPrizeType(String lxPrizeType) {
        this.lxPrizeType = lxPrizeType;
    }
}
