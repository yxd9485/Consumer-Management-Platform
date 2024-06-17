package com.dbt.platform.piclib.bean;

import org.apache.commons.lang.StringUtils;

/**
 * @Author bin.zhang
 **/
public class PicLibrary {
    private String infoKey;
    private String picName;
    private String picTemplate;
    private String picBrandType;
    private String picGroup;
    private String picWidth;
    private String picHeight;
    private String picX;
    private String picY;
    private String picUrl;
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String isDefault;
    private String startPoint;
    public PicLibrary() {
        super();
    }

    public PicLibrary(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.picName = paramAry.length > 0 ? paramAry[0] : "";
        this.picGroup = paramAry.length > 1 ? paramAry[1] : "";
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicTemplate() {
        return picTemplate;
    }

    public void setPicTemplate(String picTemplate) {
        this.picTemplate = picTemplate;
    }

    public String getPicGroup() {
        return picGroup;
    }

    public void setPicGroup(String picGroup) {
        this.picGroup = picGroup;
    }

    public String getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(String picWidth) {
        this.picWidth = picWidth;
    }

    public String getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(String picHeight) {
        this.picHeight = picHeight;
    }

    public String getPicX() {
        return picX;
    }

    public void setPicX(String picX) {
        this.picX = picX;
    }

    public String getPicY() {
        return picY;
    }

    public void setPicY(String picY) {
        this.picY = picY;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicBrandType() {
        return picBrandType;
    }

    public void setPicBrandType(String picBrandType) {
        this.picBrandType = picBrandType;
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
}
