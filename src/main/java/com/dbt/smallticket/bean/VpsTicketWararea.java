package com.dbt.smallticket.bean;

import com.dbt.framework.base.bean.BasicProperties;

@SuppressWarnings("serial")
public class VpsTicketWararea extends BasicProperties {
	
	private String bigAreaName;
	private String warAreaName;
	public String getBigAreaName() {
		return bigAreaName;
	}
	public void setBigAreaName(String bigAreaName) {
		this.bigAreaName = bigAreaName;
	}
	public String getWarAreaName() {
		return warAreaName;
	}
	public void setWarAreaName(String warAreaName) {
		this.warAreaName = warAreaName;
	}
	
	
}
