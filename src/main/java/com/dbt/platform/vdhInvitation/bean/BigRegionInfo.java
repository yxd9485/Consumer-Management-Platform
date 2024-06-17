package com.dbt.platform.vdhInvitation.bean;

import java.util.List;

public class BigRegionInfo {
    private String regionKey;
    private String companyKey;
    private String region;
    private String parentRegion;
    private String company;
    private String parent;
    private String hideStatus;
    private String createTime;
    private String hierarchy;
    private String deleteFlag;

    // 二级办数据
    private List<BigRegionInfo> secondLevelInfoList;

    public String getRegionKey() {
        return regionKey;
    }

    public void setRegionKey(String regionKey) {
        this.regionKey = regionKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getParentRegion() {
        return parentRegion;
    }

    public void setParentRegion(String parentRegion) {
        this.parentRegion = parentRegion;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getHideStatus() {
        return hideStatus;
    }

    public void setHideStatus(String hideStatus) {
        this.hideStatus = hideStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public List<BigRegionInfo> getSecondLevelInfoList() {
        return secondLevelInfoList;
    }

    public void setSecondLevelInfoList(List<BigRegionInfo> secondLevelInfoList) {
        this.secondLevelInfoList = secondLevelInfoList;
    }
}
