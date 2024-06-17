package com.dbt.framework.wechat.message.bean;

import java.io.Serializable;
/**
 * 模板消息单条消息配置
 * @author zhaohongtao
 *2017年5月17日
 */
public class MsgValue implements Serializable{

	private static final long serialVersionUID = 1L;
	private String value;
	private String color="#173177";
	public MsgValue(){}
	public MsgValue(String value, String color){
		this.value = value;
		this.color = color;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
