package com.dbt.platform.anniversary.bean;

public class VpsAnniversaryShare {
    // 活动主键
    private String shareKey;

    // 活动类型 0微信公众号，1微信视频号
    private String shareType;

    // 活动标题
    private String shareTitle;

    // 展示开始时间
    private String stgmt;

    // 展示结束时间
    private String endgmt;

    // 排序
    private String shareOrder;

    // 公众号文章链接
    private String jumpUrl;

    // 跳转视频号ID
    private String videoChannelId;

    // 视频ID
    private String videoId;

    // 素材图片路径
    private String picUrl;

    // 创建时间
    private String creGmt;

    // 创建人
    private String creUser;

    // 最后修改人
    private String modUser;

    // 最后修改时间
    private String modGmt;

    // 查询用-关键字
    private String keyword;
    // 查询用-发布状态
    private String picStatus;


    public String getShareKey() {
        return shareKey;
    }

    public void setShareKey(String shareKey) {
        this.shareKey = shareKey;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getStgmt() {
        return stgmt;
    }

    public void setStgmt(String stgmt) {
        this.stgmt = stgmt;
    }

    public String getEndgmt() {
        return endgmt;
    }

    public void setEndgmt(String endgmt) {
        this.endgmt = endgmt;
    }

    public String getShareOrder() {
        return shareOrder;
    }

    public void setShareOrder(String shareOrder) {
        this.shareOrder = shareOrder;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getVideoChannelId() {
        return videoChannelId;
    }

    public void setVideoChannelId(String videoChannelId) {
        this.videoChannelId = videoChannelId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCreGmt() {
        return creGmt;
    }

    public void setCreGmt(String creGmt) {
        this.creGmt = creGmt;
    }

    public String getCreUser() {
        return creUser;
    }

    public void setCreUser(String creUser) {
        this.creUser = creUser;
    }

    public String getModUser() {
        return modUser;
    }

    public void setModUser(String modUser) {
        this.modUser = modUser;
    }

    public String getModGmt() {
        return modGmt;
    }

    public void setModGmt(String modGmt) {
        this.modGmt = modGmt;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPicStatus() {
        return picStatus;
    }

    public void setPicStatus(String picStatus) {
        this.picStatus = picStatus;
    }
}