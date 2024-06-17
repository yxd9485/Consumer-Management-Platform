package com.dbt.platform.marquee.bean;

import org.apache.commons.lang.StringUtils;

public class MarqueeCogInfo {

    private String infoKey;
    private String marqueeName;
    private String startDate;
    private String endDate;
    private String status;
    private String showNum;
    private String showType;
    private String prizeCog;
    private String couponCog;
    private String vpointsCog;
    private String moneyCog;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String deleteFlag;
    private String winType;


    public MarqueeCogInfo() {
    }


    public MarqueeCogInfo(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.marqueeName = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
    }

    public String getWinType() {
        return winType;
    }

    public void setWinType(String winType) {
        this.winType = winType;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getMarqueeName() {
        return marqueeName;
    }

    public void setMarqueeName(String marqueeName) {
        this.marqueeName = marqueeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShowNum() {
        return showNum;
    }

    public void setShowNum(String showNum) {
        this.showNum = showNum;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getPrizeCog() {
        return prizeCog;
    }

    public void setPrizeCog(String prizeCog) {
        this.prizeCog = prizeCog;
    }

    public String getCouponCog() {
        return couponCog;
    }

    public void setCouponCog(String couponCog) {
        this.couponCog = couponCog;
    }

    public String getVpointsCog() {
        return vpointsCog;
    }

    public void setVpointsCog(String vpointsCog) {
        this.vpointsCog = vpointsCog;
    }


    public String getMoneyCog() {
        return moneyCog;
    }

    public void setMoneyCog(String moneyCog) {
        this.moneyCog = moneyCog;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
