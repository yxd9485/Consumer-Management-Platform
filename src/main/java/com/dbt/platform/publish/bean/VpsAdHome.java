package com.dbt.platform.publish.bean;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @ClassName: 首页广告表
 * @author: bin.zhang
 * @date: 2019年12月20日 下午3:47:54
 */
public class VpsAdHome {
//主键
private String adHomeKey;
//关键词
private String keyword;
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
//限时活动规则时间
private String limitGmt;
//appid
private String appid;
//跳转链接
private String jumpUrl;
//跳转类型
private String jumpTyp;
//顺序号
private String sequenceNo;
//广告弹出次数
private String popNum;
//删除标识
private String isDel;
//是否为默认标识
private String isDefault;
	private String picWidth;
	private String picHeight;
	private String picX;
	private String picY;
	private String picJumpUrl;
	private String picStatus;
    /** 人群限制类型：默认0不限制，1黑名单不可参与，2指定群组参与，3指定群组不可参与 */
    private String crowdLimitType;
    /** CRM群组ids（使用英文逗号分隔） */
    private String userGroupIds;
public VpsAdHome(){};
public VpsAdHome(String queryParam){
	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
	this.keyword = paramAry.length > 0 ? paramAry[0] : "";
	 this.picStatus = paramAry.length > 1 ? paramAry[1] : "";
    this.stGmt = paramAry.length > 2 ? paramAry[2] : "";
    this.endGmt = paramAry.length > 3 ? paramAry[3] : "";
    this.modStGmt = paramAry.length > 4 ? paramAry[4] : "";
    this.modEndGmt = paramAry.length > 5 ? paramAry[5] : "";
   
}

	public String getPicStatus() {
		return picStatus;
	}

	public void setPicStatus(String picStatus) {
		this.picStatus = picStatus;
	}

	public String getPicJumpUrl() {
		return picJumpUrl;
	}

	public void setPicJumpUrl(String picJumpUrl) {
		this.picJumpUrl = picJumpUrl;
	}

	public String getAdHomeKey() {
	return adHomeKey;
}
public void setAdHomeKey(String adHomeKey) {
	this.adHomeKey = adHomeKey;
}
public String getKeyword() {
	return keyword;
}
public void setKeyword(String keyword) {
	this.keyword = keyword;
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
public String getLimitGmt() {
	return limitGmt;
}
public void setLimitGmt(String limitGmt) {
	this.limitGmt = limitGmt;
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

public String getSequenceNo() {
	return sequenceNo;
}
public void setSequenceNo(String sequenceNo) {
	this.sequenceNo = sequenceNo;
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
public String getIsDefault() {
	return isDefault;
}
public void setIsDefault(String isDefault) {
	this.isDefault = isDefault;
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
