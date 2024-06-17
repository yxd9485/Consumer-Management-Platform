package com.dbt.smallticket.bean;



import com.dbt.framework.base.bean.BasicProperties;
/**
 * 体系bean
 */
public class VpsTicketTerminalInsideType extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String insideCodeType;
	private String insideCodeName;
	private String province;
	public String getInsideCodeType() {
		return insideCodeType;
	}
	public void setInsideCodeType(String insideCodeType) {
		this.insideCodeType = insideCodeType;
	}
	public String getInsideCodeName() {
		return insideCodeName;
	}
	public void setInsideCodeName(String insideCodeName) {
		this.insideCodeName = insideCodeName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	
	
}
