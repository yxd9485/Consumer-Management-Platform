package com.dbt.smallticket.bean;



import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.UUIDTools;
/**
 * 门店bean
 */
public class VpsTicketTerminalCog extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String terminalKey;
	private String terminalName;
	private String insideCodeType;
	private String terminalStatus;
	private String province;
	private String city;
	private String county;
	public VpsTicketTerminalCog(){
		
	}
	public VpsTicketTerminalCog(VpsTicketRecord vpsTicketRecord){
		this.terminalKey = UUIDTools.getInstance().getUUID();
		this.terminalName = vpsTicketRecord.getTicketTerminalName();
		this.province = vpsTicketRecord.getProvince();
		this.city = vpsTicketRecord.getCity();
		this.county = vpsTicketRecord.getCounty();
		this.terminalStatus = "0";
		this.insideCodeType = vpsTicketRecord.getInsideCodeType();
	}
	
	public String getTerminalKey() {
		return terminalKey;
	}
	public void setTerminalKey(String terminalKey) {
		this.terminalKey = terminalKey;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getInsideCodeType() {
		return insideCodeType;
	}
	public void setInsideCodeType(String insideCodeType) {
		this.insideCodeType = insideCodeType;
	}
	public String getTerminalStatus() {
		return terminalStatus;
	}
	public void setTerminalStatus(String terminalStatus) {
		this.terminalStatus = terminalStatus;
	}
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	
	
	
	
}
