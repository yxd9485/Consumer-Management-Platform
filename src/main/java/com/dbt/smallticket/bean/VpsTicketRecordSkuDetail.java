package com.dbt.smallticket.bean;



import com.dbt.framework.util.DateUtil;
/**
 * 票据上商品bean
 */
public class VpsTicketRecordSkuDetail{
	private static final long serialVersionUID = 1L;
	private String infoKey;
	private String ticketRecordKey;
	private String terminalInsideCode;
	private String ticketSkuName;
	private String skuKey;
	private String skuNum;
	private String ticketSkuUnitMoney;
	private String ticketSkuMoney;
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
	/****/
	private String skuName;
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getTicketRecordKey() {
		return ticketRecordKey;
	}
	public void setTicketRecordKey(String ticketRecordKey) {
		this.ticketRecordKey = ticketRecordKey;
	}

	public String getTerminalInsideCode() {
		return terminalInsideCode;
	}
	public void setTerminalInsideCode(String terminalInsideCode) {
		this.terminalInsideCode = terminalInsideCode;
	}
	public String getTicketSkuName() {
		return ticketSkuName;
	}
	public void setTicketSkuName(String ticketSkuName) {
		this.ticketSkuName = ticketSkuName;
	}
	public String getSkuKey() {
		return skuKey;
	}
	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}
	public String getSkuNum() {
		return skuNum;
	}
	public void setSkuNum(String skuNum) {
		this.skuNum = skuNum;
	}

	public String getTicketSkuUnitMoney() {
		return ticketSkuUnitMoney;
	}
	public void setTicketSkuUnitMoney(String ticketSkuUnitMoney) {
		this.ticketSkuUnitMoney = ticketSkuUnitMoney;
	}
	public String getTicketSkuMoney() {
        return ticketSkuMoney;
    }
    public void setTicketSkuMoney(String ticketSkuMoney) {
        this.ticketSkuMoney = ticketSkuMoney;
    }
    public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
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
	public void fillFields(VpsTicketRecord vpsTicketRecord) {
		this.ticketRecordKey = vpsTicketRecord.getInfoKey();
		this.createTime = vpsTicketRecord.getCreateTime();
		this.updateTime = DateUtil.getDate();
		this.createUser = vpsTicketRecord.getCreateUser();
		this.updateUser = vpsTicketRecord.getInputUserKey();
		
	}

}
