package com.dbt.framework.wechat.message.bean;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.dbt.framework.util.PropertiesUtil;
/**
 * 小程序模板消息主类
 * @author hanshimeng
 *
 */
public class MsgAppletBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/** 接收者（用户）的 openid（必填） **/
	private String touser;
	/** 所需下发的模板消息的id（必填） **/
	private String template_id;
	/** 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。 **/
	private String page;
	/** 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id **/
	private String form_id;
	/** 模板内容（必填） **/
	private MsgAppletData data;
	/** 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版*/
	private String miniprogram_state;
	/** 模板内容字体的颜色，不填默认黑色 【废弃】 **/
	private String color;
	/** 模板需要放大的关键词，不填则默认无放大 **/
	private String emphasis_keyword;
	
	public MsgAppletBean() {}
	public MsgAppletBean(String runEnv) {
		if(StringUtils.isNotBlank(runEnv) && runEnv.equals("TEST")){
        	this.miniprogram_state = "trial";
        }
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getForm_id() {
		return form_id;
	}
	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}
	public MsgAppletData getData() {
		return data;
	}
	public void setData(MsgAppletData data) {
		this.data = data;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getEmphasis_keyword() {
		return emphasis_keyword;
	}
	public void setEmphasis_keyword(String emphasis_keyword) {
		this.emphasis_keyword = emphasis_keyword;
	}
    public String getMiniprogram_state() {
        return miniprogram_state;
    }
    public void setMiniprogram_state(String miniprogram_state) {
        this.miniprogram_state = miniprogram_state;
    }
    
}
