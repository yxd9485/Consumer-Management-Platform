package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  商城拼团活动Bean
 * @author Administrator
 *
 */

@SuppressWarnings("serial")
public class VpointsGroupBuyingActivityCog extends BasicProperties {

	private String infoKey;
	/** 活动编号 */
	private String activityNo;
	/** 活动名称 */
	private String activityName;
	/** 时间类型：1时间段，2周几 **/
	private String ruleType;
	/** 开始日期 */
	private String beginDate;
	/** 结束日期 */
	private String endDate;
	/** 商品ID */
	private String goodsId;
	/** 拼团金额（分） */
	private String groupBuyingPay;
	/** 拼团积分 */
	private int  groupBuyingVpoints;
	/** 拼团有效期（小时） */
	private int  orderValidHour;
	/** 活动总库存 */
	private int groupBuyingTotalNum;
	/** 是否拼团限购，默认0否，1是 */
	private String isGroupBuyingLimit;
	/** 每人限购数量*/
	private int groupBuyingLimitNum;
	/** 限制开团数量 **/
 	private int limitGroupNum;
 	/** 成团人数 **/
 	private long reachNum;
	/** 是否叠加优惠，默认0否，1是 */
	private String isUseDiscounts;
	/** 人群限制类型：默认0不限制，1黑名单不可参与，2指定群组参与，3指定群组不可参与 */
	private String crowdLimitType;
	/** CRM群组ids（使用英文逗号分隔） */
	private String userGroupIds;
	/** 是否预告，默认0否，1是 */
	private String isNotice;
	/** 预告提前X小时显示 */
	private int noticeHour;
	/** 状态：1待上线，2已上线，3已过期 */
	private String isBegin;
	/** 是否停止：默认0否，1是 **/
	private String isStop;
	/** 分享图片url **/
	private String shareImgUrl;
	
	private String goodsName;
	private String tabsFlag;
	private String currDate;
	private String keyWord;
	
    public VpointsGroupBuyingActivityCog() {
        super();
    }

    public VpointsGroupBuyingActivityCog(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyWord = paramAry.length > 0 ? paramAry[0] : "";
        this.beginDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
    }

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
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

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGroupBuyingPay() {
		return groupBuyingPay;
	}

	public void setGroupBuyingPay(String groupBuyingPay) {
		this.groupBuyingPay = groupBuyingPay;
	}

	public int getGroupBuyingVpoints() {
		return groupBuyingVpoints;
	}

	public void setGroupBuyingVpoints(int groupBuyingVpoints) {
		this.groupBuyingVpoints = groupBuyingVpoints;
	}

	public int getOrderValidHour() {
		return orderValidHour;
	}

	public void setOrderValidHour(int orderValidHour) {
		this.orderValidHour = orderValidHour;
	}

	public int getGroupBuyingTotalNum() {
		return groupBuyingTotalNum;
	}

	public void setGroupBuyingTotalNum(int groupBuyingTotalNum) {
		this.groupBuyingTotalNum = groupBuyingTotalNum;
	}

	public int getLimitGroupNum() {
		return limitGroupNum;
	}

	public void setLimitGroupNum(int limitGroupNum) {
		this.limitGroupNum = limitGroupNum;
	}

	public long getReachNum() {
		return reachNum;
	}

	public void setReachNum(long reachNum) {
		this.reachNum = reachNum;
	}

	public String getIsGroupBuyingLimit() {
		return isGroupBuyingLimit;
	}

	public void setIsGroupBuyingLimit(String isGroupBuyingLimit) {
		this.isGroupBuyingLimit = isGroupBuyingLimit;
	}

	public int getGroupBuyingLimitNum() {
		return groupBuyingLimitNum;
	}

	public void setGroupBuyingLimitNum(int groupBuyingLimitNum) {
		this.groupBuyingLimitNum = groupBuyingLimitNum;
	}

	public String getIsUseDiscounts() {
		return isUseDiscounts;
	}

	public void setIsUseDiscounts(String isUseDiscounts) {
		this.isUseDiscounts = isUseDiscounts;
	}

	public String getIsNotice() {
		return isNotice;
	}

	public void setIsNotice(String isNotice) {
		this.isNotice = isNotice;
	}

	public int getNoticeHour() {
		return noticeHour;
	}

	public void setNoticeHour(int noticeHour) {
		this.noticeHour = noticeHour;
	}

	public String getIsBegin() {
		return isBegin;
	}

	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
	}

	public String getIsStop() {
		return isStop;
	}

	public void setIsStop(String isStop) {
		this.isStop = isStop;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getTabsFlag() {
		return tabsFlag;
	}

	public void setTabsFlag(String tabsFlag) {
		this.tabsFlag = tabsFlag;
	}

	public String getCurrDate() {
		return currDate;
	}

	public void setCurrDate(String currDate) {
		this.currDate = currDate;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getShareImgUrl() {
		return shareImgUrl;
	}

	public void setShareImgUrl(String shareImgUrl) {
		this.shareImgUrl = shareImgUrl;
	}

	public String getUserGroupIds() {
		return userGroupIds;
	}

	public void setUserGroupIds(String userGroupIds) {
		this.userGroupIds = userGroupIds;
	}

	public String getCrowdLimitType() {
		return crowdLimitType;
	}

	public void setCrowdLimitType(String crowdLimitType) {
		this.crowdLimitType = crowdLimitType;
	}
}
