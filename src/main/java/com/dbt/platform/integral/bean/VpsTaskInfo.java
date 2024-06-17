package com.dbt.platform.integral.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 任务Bean
 */
public class VpsTaskInfo extends BasicProperties {

    private static final long serialVersionUID = 1009656056443543868L;
    
    /** 主键 */
    private String infoKey;
    /** 绑定手机号获取积分 */
    private int phoneNumberVpoints;
    /** 完善用户信息获取积分 */
    private int userInfoVpoints;
    /** 关注公众号获取积分 */
    private int subscribeVpoints;
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String endDate;
    
    
    // 扩充字段
    /** 绑定手机号任务状态：1去完成， 2待领取， 3已完成 **/
    private String phoneNumberStatus;
    /** 完善用户信息任务状态：1去完成， 2待领取， 3已完成 **/
    private String userInfoStatus;
    /**关注公众号任务状态：1去完成， 2待领取， 3已完成 **/
    private String subscribeStatus;
    
    public VpsTaskInfo() {}

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

	public String getPhoneNumberStatus() {
		return phoneNumberStatus;
	}

	public void setPhoneNumberStatus(String phoneNumberStatus) {
		this.phoneNumberStatus = phoneNumberStatus;
	}

	public String getUserInfoStatus() {
		return userInfoStatus;
	}

	public void setUserInfoStatus(String userInfoStatus) {
		this.userInfoStatus = userInfoStatus;
	}

	public String getSubscribeStatus() {
		return subscribeStatus;
	}

	public void setSubscribeStatus(String subscribeStatus) {
		this.subscribeStatus = subscribeStatus;
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
