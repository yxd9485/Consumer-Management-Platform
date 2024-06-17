package com.dbt.platform.wechatmovement.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 微信运动基础信息
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsWechatActivityBasicInfo extends BasicProperties	{

	/** 主键 **/
	private String infoKey;
	/** 开始时间 **/
	private String startTime;
	/** 结束时间 **/
	private String endTime;
	/** 泡币价格 **/
	private String currencyPrice;
	/** 关注获得泡币 **/
	private int attentionVpoints;
	/** 完善信息获得泡币 **/
	private int finishUserInfoVpoints;
	/** 邀请好友获得泡币 **/
	private int inviteFriendsVpoints;
	/** 邀请好友上限 **/
	private int inviteFriendsLimit;
	/** 参与活动支付泡币 **/
	private int activityPayVpoints;
	/** 参与周赛支付泡币 **/
	private int activityWeekPayVpoints;
	/** 参与人数上限 **/
	private int activityMaxnum;
	/** 虚拟活动数据倍数 **/
	private String activityMagnification;
	/** 活动数据倍数 **/
    private String bonusPool;
	/** 达标步数限制 **/
	private int stepLimit;
	/** 周赛达标金额 **/
	private String weeksReachMoney;
	/** 是否设置提现门槛:默认0否，1是 **/
	private String isExtractLimit;
	/** 提现设置:次数-金额，格式：1-1.00;2-3.00; **/
	private String extractLimit;
	/**第一次提现设置*/
	private String firstCash;
	/**第二次提现设置*/
	private String secondCash;
	/**判断规则是否存在*/
	private int count;
	/** 操作手机号 **/
	private String phoneNum;

    public String getBonusPool() {
        return bonusPool;
    }
    public void setBonusPool(String bonusPool) {
        this.bonusPool = bonusPool;
    }
	public int getActivityWeekPayVpoints() {
		return activityWeekPayVpoints;
	}

	public void setActivityWeekPayVpoints(int activityWeekPayVpoints) {
		this.activityWeekPayVpoints = activityWeekPayVpoints;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getActivityMagnification() {
		return activityMagnification;
	}

	public void setActivityMagnification(String activityMagnification) {
		this.activityMagnification = activityMagnification;
	}

	public int getCount() {return count;}

	public void setCount(int count) {this.count = count;}

	public String getFirstCash() {return firstCash; }

	public void setFirstCash(String firstCash) { this.firstCash = firstCash; }

	public String getSecondCash() { return secondCash; }

	public void setSecondCash(String secondCash) {
		this.secondCash = secondCash;
	}

	public int getStepLimit() {
		return stepLimit;
	}
	public void setStepLimit(int stepLimit) {
		this.stepLimit = stepLimit;
	}
	public int getInviteFriendsLimit() {
		return inviteFriendsLimit;
	}
	public void setInviteFriendsLimit(int inviteFriendsLimit) {
		this.inviteFriendsLimit = inviteFriendsLimit;
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCurrencyPrice() {
		return currencyPrice;
	}
	public void setCurrencyPrice(String currencyPrice) {
		this.currencyPrice = currencyPrice;
	}
	public int getAttentionVpoints() {
		return attentionVpoints;
	}
	public void setAttentionVpoints(int attentionVpoints) {
		this.attentionVpoints = attentionVpoints;
	}
	public int getFinishUserInfoVpoints() {
		return finishUserInfoVpoints;
	}
	public void setFinishUserInfoVpoints(int finishUserInfoVpoints) {
		this.finishUserInfoVpoints = finishUserInfoVpoints;
	}
	public int getInviteFriendsVpoints() {
		return inviteFriendsVpoints;
	}
	public void setInviteFriendsVpoints(int inviteFriendsVpoints) {
		this.inviteFriendsVpoints = inviteFriendsVpoints;
	}
	public int getActivityPayVpoints() {
		return activityPayVpoints;
	}
	public void setActivityPayVpoints(int activityPayVpoints) {
		this.activityPayVpoints = activityPayVpoints;
	}
	public int getActivityMaxnum() {
		return activityMaxnum;
	}
	public void setActivityMaxnum(int activityMaxnum) {
		this.activityMaxnum = activityMaxnum;
	}
	public String getWeeksReachMoney() {
		return weeksReachMoney;
	}
	public void setWeeksReachMoney(String weeksReachMoney) {
		this.weeksReachMoney = weeksReachMoney;
	}
	public String getIsExtractLimit() {
		return isExtractLimit;
	}
	public void setIsExtractLimit(String isExtractLimit) {
		this.isExtractLimit = isExtractLimit;
	}
	public String getExtractLimit() {
		return extractLimit;
	}
	public void setExtractLimit(String extractLimit) {
		this.extractLimit = extractLimit;
	}
}
