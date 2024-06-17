package com.dbt.platform.activityui.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 活动关联模板UIBean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsActivityTemplateUi extends BasicProperties{
	
	/** 主键 **/
	private String infoKey; 
	/** 活动主键（包含活动、万能签到等） **/
	private String activityKey;
	/** 活动名称 **/
	private String activityName;
	/** 模板主键 **/
	private String templateKey;
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	public String getTemplateKey() {
		return templateKey;
	}
	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
}
