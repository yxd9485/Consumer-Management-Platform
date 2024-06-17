package com.dbt.platform.integral.bean;

import java.io.Serializable;

public class VpsTask  implements Serializable {
    /** 主键 */
    private String infoKey;
    /** 绑定手机号获取积分 */
    private int phoneNumberVpoints;
    /** 完善用户信息获取积分 */
    private int userInfoVpoints;
    /** 关注公众号获取积分 */
    private int subscribeVpoints;
    /** 首次参加扫码活动获取积分 */
    private int sweepVpoints;
    /** 首次兑换获取积分 */
    private int exchangeVpoints;
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String endDate;

    public int getSweepVpoints() {
        return sweepVpoints;
    }

    public void setSweepVpoints(int sweepVpoints) {
        this.sweepVpoints = sweepVpoints;
    }

    public int getExchangeVpoints() {
        return exchangeVpoints;
    }

    public void setExchangeVpoints(int exchangeVpoints) {
        this.exchangeVpoints = exchangeVpoints;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public int getPhoneNumberVpoints() {
        return phoneNumberVpoints;
    }

    public void setPhoneNumberVpoints(int phoneNumberVpoints) {
        this.phoneNumberVpoints = phoneNumberVpoints;
    }

    public int getUserInfoVpoints() {
        return userInfoVpoints;
    }

    public void setUserInfoVpoints(int userInfoVpoints) {
        this.userInfoVpoints = userInfoVpoints;
    }

    public int getSubscribeVpoints() {
        return subscribeVpoints;
    }

    public void setSubscribeVpoints(int subscribeVpoints) {
        this.subscribeVpoints = subscribeVpoints;
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
}
