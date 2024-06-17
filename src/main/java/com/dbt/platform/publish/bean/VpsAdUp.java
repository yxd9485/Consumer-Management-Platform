package com.dbt.platform.publish.bean;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: VpsAdPub
 * @Description: 广告发布表
 * @author: bin.zhang
 * @date: 2019年12月20日 下午3:47:54
 */
public class VpsAdUp {
    //主键
    private String adPubKey;
    //关键词
    private String keyword;
    //发布类型 0弹窗 1 轮播
    private String pubTyp;
    //skukey
    private String skuKey;
    //sku名称
    private String skuNm;
    //标题
    private String title;
    //分辨率
    private String resolution;
    //开始时间
    private String stGmt;
    //结束时间
    private String endGmt;
    //修改人
    private String modUser;
    //修改时间
    private String modGmt;
    //修改开始时间
    private String modStGmt;
    //修改结束时间
    private String modEndGmt;
    //图片的url
    private String picUrl;
    //广告发布状态 0未启用，1未启用
    private String pubStat;
    //创建时间
    private String creGmt;
    //区域编号
    private String areaCode;
    //区域名称
    private String areaName;
    //区域类型：0-行政区域，1- 热区
    private String areaType;
    //限时活动规则时间
    private String limitGmt;
    //appid
    private String appid;
    //跳转链接
    private String jumpUrl;
    //跳转类型
    private String jumpTyp;
    //顺序号
    private String sequenceno;
    //广告弹出次数
    private String popNum;
    //删除标识
    private String isDel;
    private String picWidth;
    private String picHeight;
    private String picX;
    private String picY;
    private String closeTime;
    // 弹窗位置 1.开红包前 2.开红包后 3.提现后 4.小程序商城首页
    private String adLoc;
    private String closeType;
    private String picJumpUrl;
    private String repeateCodeShow;
    private String picStatus;
    // 前置延时
    private String frontDelayed;
    /** 人群限制类型：默认0不限制，1黑名单不可参与，2指定群组参与，3指定群组不可参与 */
    private String crowdLimitType;
    /** CRM群组ids（使用英文逗号分隔） */
    private String userGroupIds;
    private Integer popNumLimit;
    private String popGroupName;
    private String validTimeRange;
    private String beginTime;
    private String endTime;


    public VpsAdUp() {
    }

    public VpsAdUp(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyword = paramAry.length > 0 ? paramAry[0] : "";
        this.picStatus = paramAry.length > 1 ? paramAry[1] : "";
        this.stGmt = paramAry.length > 2 ? paramAry[2] : "";
        this.endGmt = paramAry.length > 3 ? paramAry[3] : "";
        this.modStGmt = paramAry.length > 4 ? paramAry[4] : "";
        this.modEndGmt = paramAry.length > 5 ? paramAry[5] : "";
    }

    public String getValidTimeRange() {
        return validTimeRange;
    }

    public void setValidTimeRange(String validTimeRange) {
        this.validTimeRange = validTimeRange;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getPopNumLimit() {
        return popNumLimit;
    }

    public void setPopNumLimit(Integer popNumLimit) {
        this.popNumLimit = popNumLimit;
    }

    public String getPopGroupName() {
        return popGroupName;
    }

    public void setPopGroupName(String popGroupName) {
        this.popGroupName = popGroupName;
    }

    public String getFrontDelayed() {
        return frontDelayed;
    }

    public void setFrontDelayed(String frontDelayed) {
        this.frontDelayed = frontDelayed;
    }

    public String getPicStatus() {
        return picStatus;
    }

    public void setPicStatus(String picStatus) {
        this.picStatus = picStatus;
    }

    public String getRepeateCodeShow() {
        return repeateCodeShow;
    }

    public void setRepeateCodeShow(String repeateCodeShow) {
        this.repeateCodeShow = repeateCodeShow;
    }

    public String getPicJumpUrl() {
        return picJumpUrl;
    }

    public void setPicJumpUrl(String picJumpUrl) {
        this.picJumpUrl = picJumpUrl;
    }

    public String getCloseType() {
        return closeType;
    }

    public void setCloseType(String closeType) {
        this.closeType = closeType;
    }

    public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getAdLoc() {
		return adLoc;
	}

	public void setAdLoc(String adLoc) {
		this.adLoc = adLoc;
	}

    public String getAdPubKey() {
        return adPubKey;
    }

    public void setAdPubKey(String adPubKey) {
        this.adPubKey = adPubKey;
    }

    public String getPubTyp() {
        return pubTyp;
    }

    public void setPubTyp(String pubTyp) {
        this.pubTyp = pubTyp;
    }

    public String getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }

    public String getSkuNm() {
        return skuNm;
    }

    public void setSkuNm(String skuNm) {
        this.skuNm = skuNm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getStGmt() {
        return stGmt;
    }

    public void setStGmt(String stGmt) {
        this.stGmt = stGmt;
    }

    public String getEndGmt() {
        return endGmt;
    }

    public void setEndGmt(String endGmt) {
        this.endGmt = endGmt;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPubStat() {
        return pubStat;
    }

    public void setPubStat(String pubStat) {
        this.pubStat = pubStat;
    }

    public String getCreGmt() {
        return creGmt;
    }

    public void setCreGmt(String creGmt) {
        this.creGmt = creGmt;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getJumpTyp() {
        return jumpTyp;
    }

    public void setJumpTyp(String jumpTyp) {
        this.jumpTyp = jumpTyp;
    }

    public String getSequenceno() {
        return sequenceno;
    }

    public void setSequenceno(String sequenceno) {
        this.sequenceno = sequenceno;
    }

    public String getPopNum() {
        return popNum;
    }

    public void setPopNum(String popNum) {
        this.popNum = popNum;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getModStGmt() {
        return modStGmt;
    }

    public void setModStGmt(String modStGmt) {
        this.modStGmt = modStGmt;
    }

    public String getModEndGmt() {
        return modEndGmt;
    }

    public void setModEndGmt(String modEndGmt) {
        this.modEndGmt = modEndGmt;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLimitGmt() {
        return limitGmt;
    }

    public void setLimitGmt(String limitGmt) {
        this.limitGmt = limitGmt;
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getCrowdLimitType() {
        return crowdLimitType;
    }

    public void setCrowdLimitType(String crowdLimitType) {
        this.crowdLimitType = crowdLimitType;
    }

    public String getUserGroupIds() {
        return userGroupIds;
    }

    public void setUserGroupIds(String userGroupIds) {
        this.userGroupIds = userGroupIds;
    }
}
