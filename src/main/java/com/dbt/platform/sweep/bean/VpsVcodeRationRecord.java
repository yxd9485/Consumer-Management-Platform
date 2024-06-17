/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 上午10:11:43 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.sweep.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
/**
 * 提现记录
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2017年12月1日 </br>
 */
@SuppressWarnings("serial")
public class VpsVcodeRationRecord extends BasicProperties{
	/** 微信openid **/
    private String openid;
    /** 提现主键 **/
	private String infoKey;
	/** 提现用户 **/
	private String userKey;
	/** 提现时间 **/
	private String earnTime;
	/** 提现金额 **/
	private String earnMoney;
    /** 商户订单号 **/
    private String partnerTradeNo;
    /** 微信订单号 **/
    private String paymentNo;
    /** 提现状态：0 成功 1失败 2进行中 **/
    private String extractStatus;
    /** 发送报文 **/
    private String sendMessage;
    /** 接收报文 **/
    private String receiveMessage;
    /** 业务错误码**/
    private String errCode;
    /** 业务错误信息**/
    private String errCodeDes;
    /** 失败提现订单处理job的响应报文**/
    private String jobMessage;
    
    // 扩充字段
    /** 微信昵称 **/
    private String nickName;
    /** 开始时间 **/
    private String startDate;
    /** 结束时间 **/
    private String endDate;
    
    public VpsVcodeRationRecord() {
        super();
    }
    
    public VpsVcodeRationRecord(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.paymentNo = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getEarnTime() {
		return earnTime;
	}

	public void setEarnTime(String earnTime) {
		this.earnTime = earnTime;
	}

	public String getEarnMoney() {
		return earnMoney;
	}

	public void setEarnMoney(String earnMoney) {
		this.earnMoney = earnMoney;
	}

	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}

	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public String getExtractStatus() {
		return extractStatus;
	}

	public void setExtractStatus(String extractStatus) {
		this.extractStatus = extractStatus;
	}

	public String getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}

	public String getReceiveMessage() {
		return receiveMessage;
	}

	public void setReceiveMessage(String receiveMessage) {
		this.receiveMessage = receiveMessage;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getJobMessage() {
		return jobMessage;
	}

	public void setJobMessage(String jobMessage) {
		this.jobMessage = jobMessage;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
