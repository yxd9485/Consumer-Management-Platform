package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  商城秒杀活动Bean
 * @author Administrator
 *
 */

@SuppressWarnings("serial")
public class VpointsSeckillActivityCog extends BasicProperties {

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
	/** 开始时间 */
	private String beginTime;
	/** 结束时间 */
	private String endTime;
	/** 商品ID */
	private String goodsId;
	/** 秒杀金额（分） */
	private String seckillPay;
	/** 秒杀积分 */
	private int  seckillVpoints;
	/** 每期秒杀库存 */
	private int seckillPeriodsNum;
	/** 每期总库存 */
	private int seckillTotalNum;
	/** 每期秒杀剩余库存 */
	private int seckillPeriodsRemainsNum;
	/** 未支付订单自动取消时间（分钟） */
	private int unpaidOrderTime;
	/** 是否秒杀限购，默认0否，1是 */
	private String isSeckillLimit;
	/** 每人限购数量*/
	private int seckillLimitNum;
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
	
	
	private String goodsName;
	private String tabsFlag;
	private String currDate;
	private String keyWord;
	
    public VpointsSeckillActivityCog() {
        super();
    }

    public VpointsSeckillActivityCog(String queryParam) {
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getSeckillVpoints() {
		return seckillVpoints;
	}

	public void setSeckillVpoints(int seckillVpoints) {
		this.seckillVpoints = seckillVpoints;
	}

	public int getSeckillPeriodsNum() {
		return seckillPeriodsNum;
	}

	public void setSeckillPeriodsNum(int seckillPeriodsNum) {
		this.seckillPeriodsNum = seckillPeriodsNum;
	}

	public int getUnpaidOrderTime() {
		return unpaidOrderTime;
	}

	public void setUnpaidOrderTime(int unpaidOrderTime) {
		this.unpaidOrderTime = unpaidOrderTime;
	}

	public String getIsSeckillLimit() {
		return isSeckillLimit;
	}

	public void setIsSeckillLimit(String isSeckillLimit) {
		this.isSeckillLimit = isSeckillLimit;
	}

	public int getSeckillLimitNum() {
		return seckillLimitNum;
	}

	public void setSeckillLimitNum(int seckillLimitNum) {
		this.seckillLimitNum = seckillLimitNum;
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

	public String getIsBegin() {
		return isBegin;
	}

	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
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

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSeckillPay() {
		return seckillPay;
	}

	public void setSeckillPay(String seckillPay) {
		this.seckillPay = seckillPay;
	}

	public int getNoticeHour() {
		return noticeHour;
	}

	public void setNoticeHour(int noticeHour) {
		this.noticeHour = noticeHour;
	}

	public String getIsStop() {
		return isStop;
	}

	public void setIsStop(String isStop) {
		this.isStop = isStop;
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

	public int getSeckillPeriodsRemainsNum() {
		return seckillPeriodsRemainsNum;
	}

	public void setSeckillPeriodsRemainsNum(int seckillPeriodsRemainsNum) {
		this.seckillPeriodsRemainsNum = seckillPeriodsRemainsNum;
	}

	public int getSeckillTotalNum() {
		return seckillTotalNum;
	}

	public void setSeckillTotalNum(int seckillTotalNum) {
		this.seckillTotalNum = seckillTotalNum;
	}
    
}
