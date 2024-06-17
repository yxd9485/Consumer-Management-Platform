package com.dbt.platform.expireremind.bean;

import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class VpsMsgExpireRemindInfo {
    private String infoKey;

    private String vcodeActivityKey;

    private String activityType;

    private String msgType;

    private String msgContent;

    private String readFlag;

    private String topFlag;

    private String deleteFlag;

    private Date createTime;

    private String updateTime;

    /***     活动属性         **/
    private String  activityNo;

    private String  activityName;

    private String  activityStartDate;

    private String  activityEndDate;

    private String  activityStartDates;
    private String  activityEndDates;
    private String createTimes;
    private String updateTimes;
    public VpsMsgExpireRemindInfo() {}
    public VpsMsgExpireRemindInfo(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.activityName = paramAry.length > 0 ? paramAry[0].trim() : "";
        this.activityNo = paramAry.length > 1 ? paramAry[1].trim() : "";
        this.msgType = paramAry.length > 2 ? paramAry[2] : "";
        this.activityType= paramAry.length > 3 ? paramAry[3] : "";
        this.activityStartDate = paramAry.length > 4 ? paramAry[4] : "";
        this.activityEndDate = paramAry.length > 5 ? paramAry[5] : "";
    }
    public String getActivityStartDates() {
        return activityStartDates;
    }

    public void setActivityStartDates(String activityStartDates) {
        this.activityStartDates = activityStartDates;
    }

    public String getActivityEndDates() {
        return activityEndDates;
    }

    public void setActivityEndDates(String activityEndDates) {
        this.activityEndDates = activityEndDates;
    }

    public String getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(String createTimes) {
        this.createTimes = createTimes;
    }

    public String getUpdateTimes() {
        return updateTimes;
    }

    public void setUpdateTimes(String updateTimes) {
        this.updateTimes = updateTimes;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(String activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public String getActivityEndDate() {
        return activityEndDate;
    }

    public void setActivityEndDate(String activityEndDate) {
        this.activityEndDate = activityEndDate;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey == null ? null : infoKey.trim();
    }

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey == null ? null : vcodeActivityKey.trim();
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType == null ? null : activityType.trim();
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType == null ? null : msgType.trim();
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent == null ? null : msgContent.trim();
    }

    public String getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(String readFlag) {
        this.readFlag = readFlag == null ? null : readFlag.trim();
    }

    public String getTopFlag() {
        return topFlag;
    }

    public void setTopFlag(String topFlag) {
        this.topFlag = topFlag == null ? null : topFlag.trim();
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag == null ? null : deleteFlag.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}