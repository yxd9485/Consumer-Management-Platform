package com.dbt.platform.integral.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 任务Bean
 */
public class VpsTaskShareInfo extends BasicProperties {

    private static final long serialVersionUID = 1009656056443543868L;
    
    /** 主键 */
    private String infoKey;
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String endDate;
    /** 图片Url */
    private String picUrl;
    /** 每天限制次数 */
    private int dayCount;
    
    public VpsTaskShareInfo() {}
    
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
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
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public int getDayCount() {
		return dayCount;
	}
	public void setDayCount(int dayCount) {
		this.dayCount = dayCount;
	}
    
    
}
