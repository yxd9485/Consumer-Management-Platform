package com.dbt.platform.bottlecap.bean;


import org.apache.commons.lang.StringUtils;

public class VpsVcodeBottlecapActivityCogInfo {

    private String activityKey;
    /** 集盖活动名称 **/
    private String activityName;
    private String startDate;
    private String endDate;
    /** 集盖配置sku **/
    private String activitySku;
    /** banner图 **/
    private String bannerPic;
    /** 活动规则描述 **/
    private String roleDescribe;
    private String remarks;
    private String regionCog;
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    /** 活动类型 0累计 1消耗 **/
    private String activityType;
    private String isBegin;
    private String notEditStartDate;

    /*拓展字段*/
    private String[] prizeInfoKey;
    private String[] ladder;
    private String[] consumeBoottlecap;
    private String[] boottlecapPrizeType;
    private String[] minMoney;
    private String[] maxMoney;
    private String[] minVpoints;
    private String[] maxVpoints;
    private String[] bigPrizeType;
    private String[] launchAmount;
    private String[] limitExchange;
    private String[] receiveEndTime;

    public VpsVcodeBottlecapActivityCogInfo() {
    }

    public VpsVcodeBottlecapActivityCogInfo(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.activityName = paramAry.length > 0 ? paramAry[0] : "";
        this.activityType = paramAry.length > 1 ? paramAry[1] : "";
        this.isBegin = paramAry.length > 2 ? paramAry[2] : "";
        this.startDate = paramAry.length > 3 ? paramAry[3] : "";
        this.endDate = paramAry.length > 4 ? paramAry[4] : "";
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
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

    public String getActivitySku() {
        return activitySku;
    }

    public void setActivitySku(String activitySku) {
        this.activitySku = activitySku;
    }

    public String getBannerPic() {
        return bannerPic;
    }

    public void setBannerPic(String bannerPic) {
        this.bannerPic = bannerPic;
    }

    public String getRoleDescribe() {
        return roleDescribe;
    }

    public void setRoleDescribe(String roleDescribe) {
        this.roleDescribe = roleDescribe;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRegionCog() {
        return regionCog;
    }

    public void setRegionCog(String regionCog) {
        this.regionCog = regionCog;
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

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }


    public String getNotEditStartDate() {
        return notEditStartDate;
    }

    public void setNotEditStartDate(String notEditStartDate) {
        this.notEditStartDate = notEditStartDate;
    }

    public String[] getPrizeInfoKey() {
        return prizeInfoKey;
    }

    public void setPrizeInfoKey(String[] prizeInfoKey) {
        this.prizeInfoKey = prizeInfoKey;
    }

    public String[] getLadder() {
        return ladder;
    }

    public void setLadder(String[] ladder) {
        this.ladder = ladder;
    }

    public String[] getConsumeBoottlecap() {
        return consumeBoottlecap;
    }

    public void setConsumeBoottlecap(String[] consumeBoottlecap) {
        this.consumeBoottlecap = consumeBoottlecap;
    }

    public String[] getBoottlecapPrizeType() {
        return boottlecapPrizeType;
    }

    public void setBoottlecapPrizeType(String[] boottlecapPrizeType) {
        this.boottlecapPrizeType = boottlecapPrizeType;
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

    public String[] getBigPrizeType() {
        return bigPrizeType;
    }

    public void setBigPrizeType(String[] bigPrizeType) {
        this.bigPrizeType = bigPrizeType;
    }

    public String[] getLaunchAmount() {
        return launchAmount;
    }

    public void setLaunchAmount(String[] launchAmount) {
        this.launchAmount = launchAmount;
    }

    public String[] getLimitExchange() {
        return limitExchange;
    }

    public void setLimitExchange(String[] limitExchange) {
        this.limitExchange = limitExchange;
    }

    public String[] getReceiveEndTime() {
        return receiveEndTime;
    }

    public void setReceiveEndTime(String[] receiveEndTime) {
        this.receiveEndTime = receiveEndTime;
    }
}
