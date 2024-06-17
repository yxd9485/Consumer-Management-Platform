package com.dbt.platform.integral.bean;

import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;

import java.io.Serializable;
import java.util.List;

public class VpsSignIn implements Serializable {
    /** 主键 */
    private String infoKey;
    /** 基础规则主键 */
    private String basicRuleKey;
    /** 特殊规则主键 */
    private String specialRuleKey;
    /** 时间类型：1 按天，2按周 */
    private String timeType;
    /** 时间值：多个使用英文逗号分开  */
    private String timeValue;
    //按天日期
    private String cycleDay;
    //按周结果
    private String week;
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String endDate;


    public String getCycleDay() {
        return cycleDay;
    }

    public void setCycleDay(String cycleDay) {
        this.cycleDay = cycleDay;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }


    private List<VcodeActivityVpointsCog> signInInfo;
    private List<VcodeActivityVpointsCog> specialDayInfo;

    private String scanType[];
    private String randomType[];
    private String fixationMoney[];
    private String minVpoints[];
    private String maxVpoints[];
    private String fixationVpoints[];
    private String prizePercent[];
    private String prizeDiscount[];
    private String scanNum[];
    private String cogAmounts[];
    /** 特殊日期配置字段 1为特殊 */
    private String specialType[];
    public VpsSignIn() {}

    public String[] getSpecialType() {
        return specialType;
    }

    public void setSpecialType(String[] specialType) {
        this.specialType = specialType;
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

    public List<VcodeActivityVpointsCog> getSignInInfo() {
        return signInInfo;
    }

    public void setSignInInfo(List<VcodeActivityVpointsCog> signInInfo) {
        this.signInInfo = signInInfo;
    }

    public List<VcodeActivityVpointsCog> getSpecialDayInfo() {
        return specialDayInfo;
    }

    public void setSpecialDayInfo(List<VcodeActivityVpointsCog> specialDayInfo) {
        this.specialDayInfo = specialDayInfo;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getBasicRuleKey() {
        return basicRuleKey;
    }

    public void setBasicRuleKey(String basicRuleKey) {
        this.basicRuleKey = basicRuleKey;
    }

    public String getSpecialRuleKey() {
        return specialRuleKey;
    }

    public void setSpecialRuleKey(String specialRuleKey) {
        this.specialRuleKey = specialRuleKey;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public void setTimeValue(String timeValue) {
        this.timeValue = timeValue;
    }

    public String[] getFixationMoney() {
        return fixationMoney;
    }

    public void setFixationMoney(String[] fixationMoney) {
        this.fixationMoney = fixationMoney;
    }


    public String[] getPrizeDiscount() {
        return prizeDiscount;
    }

    public void setPrizeDiscount(String[] prizeDiscount) {
        this.prizeDiscount = prizeDiscount;
    }

    public String[] getScanNum() {
        return scanNum;
    }

    public void setScanNum(String[] scanNum) {
        this.scanNum = scanNum;
    }


    public String[] getCogAmounts() {
        return cogAmounts;
    }

    public void setCogAmounts(String[] cogAmounts) {
        this.cogAmounts = cogAmounts;
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
}
