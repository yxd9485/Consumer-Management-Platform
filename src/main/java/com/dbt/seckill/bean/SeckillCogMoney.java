package com.dbt.seckill.bean;

import java.io.Serializable;

public class SeckillCogMoney implements Serializable{
	private static final long serialVersionUID = 1L;
	private String infoKey;
	private String seckillRuleKey;
	private String prizeType;
	private Double vcodeMoney; 
	private int cogamounts;
	private int rangeVal;
	private String percent;
	
	public String getPrizeType() {
		return prizeType;
	}
	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getSeckillRuleKey() {
		return seckillRuleKey;
	}
	public void setSeckillRuleKey(String seckillRuleKey) {
		this.seckillRuleKey = seckillRuleKey;
	}
	public Double getVcodeMoney() {
		return vcodeMoney;
	}
	public void setVcodeMoney(Double vcodeMoney) {
		this.vcodeMoney = vcodeMoney;
	}
	public int getCogamounts() {
		return cogamounts;
	}
	public void setCogamounts(int cogamounts) {
		this.cogamounts = cogamounts;
	}
	public int getRangeVal() {
		return rangeVal;
	}
	public void setRangeVal(int rangeVal) {
		this.rangeVal = rangeVal;
	}
}
