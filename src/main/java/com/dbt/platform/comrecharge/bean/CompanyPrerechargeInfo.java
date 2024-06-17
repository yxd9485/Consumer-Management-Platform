package com.dbt.platform.comrecharge.bean;

import java.io.Serializable;

import org.springframework.util.StringUtils;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2015年9月6日 上午11:27:24
 * @description 企业预充值
 */

@SuppressWarnings("serial")
public class CompanyPrerechargeInfo implements Serializable {

	private String preKey;
	private String companyKey;
	private String contractNum;
	private String contractName;
	private String rechargeTime;
	private String transType;
	private String tradeUser;
	private Double rechargeMoney;
	private Long rechargeVpoints;
	private String transStatus;
	private String terminalTime;
	private String permissionType;
	private String companyName;

	private String keyword;
	private String type;
	private String  remark;
	
	private String rechargeMoneyChar;

    public String getRechargeMoneyChar() {
        return rechargeMoneyChar;
    }

    public void setRechargeMoneyChar(String rechargeMoneyChar) {
        this.rechargeMoneyChar = rechargeMoneyChar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public CompanyPrerechargeInfo(){
		
	}
	
	public CompanyPrerechargeInfo(String queryParam, String permissionType) {
		this.permissionType = permissionType;
		if(!StringUtils.isEmpty(queryParam)) {
			String[] params = queryParam.split(",");
			this.keyword = params.length > 0 ? !StringUtils.isEmpty(params[0]) ? params[0] : null : null;
			this.transType = params.length > 1 ? !StringUtils.isEmpty(params[1]) ? params[1] : null : null;
			this.transStatus = params.length > 2 ? !StringUtils.isEmpty(params[2]) ? params[2] : null : null;
		} else {
			//this.transStatus = "1";
		}
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

	public String getTradeUser() {
		return tradeUser;
	}

	public void setTradeUser(String tradeUser) {
		this.tradeUser = tradeUser;
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

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getTerminalTime() {
		return terminalTime;
	}

	public void setTerminalTime(String terminalTime) {
		this.terminalTime = terminalTime;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}
