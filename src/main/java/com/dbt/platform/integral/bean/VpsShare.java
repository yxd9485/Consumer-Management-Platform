package com.dbt.platform.integral.bean;

import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;

import java.util.List;

public class VpsShare {
    /** 主键 */
    private String infoKey;
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String endDate;
    /** 图片Url */
    private String picUrl;
    /** 每天限制次数 */
    private int dayCount;
    private List<VcodeActivityVpointsCog> commonList;
    private String specialType[];
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

    public List<VcodeActivityVpointsCog> getCommonList() {
        return commonList;
    }

    public void setCommonList(List<VcodeActivityVpointsCog> commonList) {
        this.commonList = commonList;
    }

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

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public String[] getRandomType() {
        return randomType;
    }

    public void setRandomType(String[] randomType) {
        this.randomType = randomType;
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
}
