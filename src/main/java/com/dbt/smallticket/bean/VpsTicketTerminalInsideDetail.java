package com.dbt.smallticket.bean;

import com.dbt.framework.util.DateUtil;

/**
 * 店内码详情bean
 */
public class VpsTicketTerminalInsideDetail{
	private static final long serialVersionUID = 1L;
	
	private String terminalInsideCode;
	private String terminalKey;
	private String insideCodeType;
	private String skuKey;
	private String ticketSkuName;
	private String ticketSkuUnitMoney;
	private String codeStatus;
	/** 删除标识 DELETE_FLAG */
	private String deleteFlag;
	/** 创建时间 CREATE_TIME */
	private String createTime;
	/** 创建人 CREATE_USER */
	private String createUser;
	/** 修改时间 UPDATE_TIME */
	private String updateTime;
	/** 修改人 UPDATE_USER */
	private String updateUser;
	
	/**-------**/
	private String terminalName;
	private String province;
	private String ticketTerminalName;
	
	public VpsTicketTerminalInsideDetail(){
		
	}
	
	public VpsTicketTerminalInsideDetail(VpsTicketRecordSkuDetail skuDetail,VpsTicketRecord vpsTicketRecord,boolean addFlag) {
		if(addFlag){
			this.codeStatus = "0";
			this.createUser = vpsTicketRecord.getInputUserKey();
			this.createTime = DateUtil.getDate();
		}
		this.terminalKey = vpsTicketRecord.getTerminalKey();
		this.insideCodeType = vpsTicketRecord.getInsideCodeType();
		this.terminalInsideCode = skuDetail.getTerminalInsideCode();
		this.updateTime = DateUtil.getDate();
		this.updateUser = vpsTicketRecord.getInputUserKey();
		this.skuKey = skuDetail.getSkuKey();
		this.ticketSkuName= skuDetail.getTicketSkuName();
		this.ticketSkuUnitMoney = skuDetail.getTicketSkuUnitMoney();
	}
	public String getTerminalKey() {
		return terminalKey;
	}
	public void setTerminalKey(String terminalKey) {
		this.terminalKey = terminalKey;
	}
	public String getInsideCodeType() {
		return insideCodeType;
	}
	public void setInsideCodeType(String insideCodeType) {
		this.insideCodeType = insideCodeType;
	}
	public String getSkuKey() {
		return skuKey;
	}
	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}
	public String getTicketSkuName() {
		return ticketSkuName;
	}
	public void setTicketSkuName(String ticketSkuName) {
		this.ticketSkuName = ticketSkuName;
	}
	public String getTicketSkuUnitMoney() {
		return ticketSkuUnitMoney;
	}
	public void setTicketSkuUnitMoney(String ticketSkuUnitMoney) {
		this.ticketSkuUnitMoney = ticketSkuUnitMoney;
	}
	public String getCodeStatus() {
		return codeStatus;
	}
	public void setCodeStatus(String codeStatus) {
		this.codeStatus = codeStatus;
	}
	public String getTerminalInsideCode() {
		return terminalInsideCode;
	}
	public void setTerminalInsideCode(String terminalInsideCode) {
		this.terminalInsideCode = terminalInsideCode;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getTicketTerminalName() {
		return ticketTerminalName;
	}
	public void setTicketTerminalName(String ticketTerminalName) {
		this.ticketTerminalName = ticketTerminalName;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	
}
	