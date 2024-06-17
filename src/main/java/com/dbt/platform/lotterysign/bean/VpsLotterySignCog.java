package com.dbt.platform.lotterysign.bean;


import org.apache.commons.lang.StringUtils;


public class VpsLotterySignCog {

    private String infoKey;
    private String activityName;
    private String startDate;
    private String endDate;
    private String cycleType;
    private Integer scanCountCog;
    private String skuCog;
    private String activityRule;
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String status;
    private String noEditStartDate;

    public VpsLotterySignCog(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.activityName = paramAry.length > 0 ? paramAry[0] : "";
        this.status = paramAry.length > 1 ? paramAry[1] : "";
        this.startDate = paramAry.length > 2 ? paramAry[2] : "";
        this.endDate = paramAry.length > 3 ? paramAry[3] : "";
        this.cycleType = paramAry.length > 4 ? paramAry[4] : "";
    }

    public String getNoEditStartDate() {
        return noEditStartDate;
    }

    public void setNoEditStartDate(String noEditStartDate) {
        this.noEditStartDate = noEditStartDate;
    }

    public VpsLotterySignCog(){
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
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

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public Integer getScanCountCog() {
        return scanCountCog;
    }

    public void setScanCountCog(Integer scanCountCog) {
        this.scanCountCog = scanCountCog;
    }

    public String getSkuCog() {
        return skuCog;
    }

    public void setSkuCog(String skuCog) {
        this.skuCog = skuCog;
    }

    public String getActivityRule() {
        return activityRule;
    }

    public void setActivityRule(String activityRule) {
        this.activityRule = activityRule;
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
}
