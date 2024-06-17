package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
/**
 * 评论管理bean
 */
public class VpsOrderComment extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String commentId;
	/** 评论类型：默认1商城，2竞价活动 **/
    private String commentType;
	private String userKey;
	private String exchangeId;
	private String content;
	private String couponKey;
	private String isDisplay;
	private String isTop;
	private String status;
	private String imageUrl;
	private String level;
	private String remarks;
	private String reply;
	private String auditTime;
	private String couponName;
	/**拓展字段**/
	private String goodsName;
	private String keyword;
	private String userName;
	private String goodsSpecification;
	private String headimgUrl;
	private String headPortrait;
	private String orderContent;
	private String goodsLevel;
	private String logisticsLevel;
	private String isIncognito;
	private String openId;
	private String activityName;
	private String activityType;
	private String openingNumber;
	private String periodsNumber;
	
	private int isShow;
	private String phoneNum;
	private String paOpenid;
	
	public VpsOrderComment(){
		
	}
	
	
	public VpsOrderComment(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
		this.keyword = paramAry.length > 0 ? paramAry[0] : "";
	    this.userName = paramAry.length > 1 ? paramAry[1] : "";
	    this.status = paramAry.length > 2 ? paramAry[2] : "";
	}
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCouponKey() {
		return couponKey;
	}
	public void setCouponKey(String couponKey) {
		this.couponKey = couponKey;
	}
	public String getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(String isDisplay) {
		this.isDisplay = isDisplay;
	}
	public String getIsTop() {
		return isTop;
	}
	public void setIsTop(String isTop) {
		this.isTop = isTop;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}


	public String getGoodsName() {
		return goodsName;
	}


	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}


	

	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getCouponName() {
		return couponName;
	}


	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}


	public String getGoodsSpecification() {
		return goodsSpecification;
	}


	public void setGoodsSpecification(String goodsSpecification) {
		this.goodsSpecification = goodsSpecification;
	}


	public String getHeadimgUrl() {
		return headimgUrl;
	}


	public void setHeadimgUrl(String headimgUrl) {
		this.headimgUrl = headimgUrl;
	}


	public String getOrderContent() {
		return orderContent;
	}


	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}

	public String getGoodsLevel() {
		return goodsLevel;
	}

	public void setGoodsLevel(String goodsLevel) {
		this.goodsLevel = goodsLevel;
	}

	public String getLogisticsLevel() {
		return logisticsLevel;
	}


	public void setLogisticsLevel(String logisticsLevel) {
		this.logisticsLevel = logisticsLevel;
	}


	public String getIsIncognito() {
		return isIncognito;
	}


	public void setIsIncognito(String isIncognito) {
		this.isIncognito = isIncognito;
	}


	public int getIsShow() {
		return isShow;
	}


	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}


	public String getPhoneNum() {
		return phoneNum;
	}


	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}


	public String getHeadPortrait() {
		return headPortrait;
	}


	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}


	public String getOpenId() {
		return openId;
	}


	public void setOpenId(String openId) {
		this.openId = openId;
	}


	public String getPaOpenid() {
		return paOpenid;
	}


	public void setPaOpenid(String paOpenid) {
		this.paOpenid = paOpenid;
	}


	public String getCommentType() {
		return commentType;
	}


	public void setCommentType(String commentType) {
		this.commentType = commentType;
	}

	public String getActivityName() {
		return activityName;
	}


	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}


	public String getActivityType() {
		return activityType;
	}


	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}


	public String getOpeningNumber() {
		return openingNumber;
	}


	public void setOpeningNumber(String openingNumber) {
		this.openingNumber = openingNumber;
	}


	public String getPeriodsNumber() {
		return periodsNumber;
	}


	public void setPeriodsNumber(String periodsNumber) {
		this.periodsNumber = periodsNumber;
	}
}
