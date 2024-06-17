package com.dbt.platform.comrecharge.bean;

import java.io.Serializable;

import org.springframework.util.StringUtils;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2015年9月6日 上午11:27:12
 * @description 企业充值
 */

@SuppressWarnings("serial")
public class CompanyRechargeInfo implements Serializable {

	private String rechargeKey;
	private String preKey;
	private String companyKey;
	private String contractNum;
	private String contractName;
	private String rechargeTime;
	private String transType;
	private String transactUser;
	private Double rechargeMoney;
	private Long rechargeVpoints;
	private Long previousVpoints;
	private Long currentVpoints;
	private Long totalVpoints;
	private String companyName;
	private String rechargeMoneyStr;
	private String rechargeVpointsStr;
	private String currentVpointsStr;
	private String transTypeName;
	
	public CompanyRechargeInfo(){}

	public CompanyRechargeInfo(String rechargeKey, String preKey, String companyKey,
			String contractNum, String contractName, String rechargeTime, String transType,
			Double rechargeMoney, Long rechargeVpoints, Long previousVpoints, Long currentVpoints,
			String transactUser) {
		this.rechargeKey = rechargeKey;
		this.preKey = preKey;
		this.companyKey = companyKey;
		this.contractNum = contractNum;
		this.contractName = contractName;
		this.rechargeTime = rechargeTime;
		this.transType = transType;
		this.rechargeMoney = rechargeMoney;
		this.rechargeVpoints = rechargeVpoints;
		this.previousVpoints = previousVpoints;
		this.currentVpoints = currentVpoints;
		this.transactUser = transactUser;
	}


	public String getNameByType() {
		if("1".equals(transType)){
			return "网银转账";
		} else if("2".equals(transType)){
			return "第三方支付工具";
		} else if("3".equals(transType)){
			return "支票";
		} else if("4".equals(transType)) {
			return "欠款代付";
		} else {
			return "";
		}
	}

	public String getRechargeKey() {
		return rechargeKey;
	}

	public void setRechargeKey(String rechargeKey) {
		this.rechargeKey = rechargeKey;
	}

	public String getPreKey() {
		return preKey;
	}

	public void setPreKey(String preKey) {
		this.preKey = preKey;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(String rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransactUser() {
		return transactUser;
	}

	public void setTransactUser(String transactUser) {
		this.transactUser = transactUser;
	}

	public Double getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(Double rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}

	public Long getRechargeVpoints() {
		return rechargeVpoints;
	}

	public void setRechargeVpoints(Long rechargeVpoints) {
		this.rechargeVpoints = rechargeVpoints;
	}

	public Long getPreviousVpoints() {
		return previousVpoints;
	}

	public void setPreviousVpoints(Long previousVpoints) {
		this.previousVpoints = previousVpoints;
	}

	public Long getCurrentVpoints() {
		return currentVpoints;
	}

	public void setCurrentVpoints(Long currentVpoints) {
		this.currentVpoints = currentVpoints;
	}

	public Long getTotalVpoints() {
		return totalVpoints;
	}

	public void setTotalVpoints(Long totalVpoints) {
		this.totalVpoints = totalVpoints;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getRechargeMoneyStr() {
		return rechargeMoneyStr;
	}

	public void setRechargeMoneyStr(String rechargeMoneyStr) {
		this.rechargeMoneyStr = rechargeMoneyStr;
	}

	public String getRechargeVpointsStr() {
		return rechargeVpointsStr;
	}

	public void setRechargeVpointsStr(String rechargeVpointsStr) {
		this.rechargeVpointsStr = rechargeVpointsStr;
	}

	public String getCurrentVpointsStr() {
		return currentVpointsStr;
	}

	public void setCurrentVpointsStr(String currentVpointsStr) {
		this.currentVpointsStr = currentVpointsStr;
	}

	public String getTransTypeName() {
		return StringUtils.isEmpty(transType) ? transTypeName : getNameByType();
	}

	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}
	
	
}
