package com.dbt.platform.ticket.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 *优惠券基础类型
 */
public class VpsSysTicketCategory extends BasicProperties {

    private static final long serialVersionUID = 1009656056443543868L;
    
    /** 主键 */
    private String categoryKey;
    /** 券类型：Q京东券 */
    private String categoryType;
    /** 券类型名称 */
    private String categoryName;
    /** 券描述 */
    private String categoryDesc;
	/** 跳转id/h5 */
	private String jumpId;
	/** 优惠券类型图片 */
	private String categoryPicUrl;
	/** 优惠券面额 */
    private List<VpsSysTicketInfo> sysTicketInfoList;

    /** 扩展字段 */
    private int categoryTypeNum;
    
    public VpsSysTicketCategory() {}
    public VpsSysTicketCategory(String queryParam) {
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
		this.categoryName = paramAry.length > 0 ? paramAry[0] : "";
		this.categoryKey = paramAry.length > 1 ? paramAry[1] : "";
    }

	public String getCategoryPicUrl() {
		return categoryPicUrl;
	}

	public void setCategoryPicUrl(String categoryPicUrl) {
		this.categoryPicUrl = categoryPicUrl;
	}

	public String getJumpId() {
		return jumpId;
	}

	public void setJumpId(String jumpId) {
		this.jumpId = jumpId;
	}

	public List<VpsSysTicketInfo> getSysTicketInfoList() {
		return sysTicketInfoList;
	}

	public void setSysTicketInfoList(List<VpsSysTicketInfo> sysTicketInfoList) {
		this.sysTicketInfoList = sysTicketInfoList;
	}

	public String getCategoryKey() {
		return categoryKey;
	}
	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public int getCategoryTypeNum() {
		return categoryTypeNum;
	}

	public void setCategoryTypeNum(int categoryTypeNum) {
		this.categoryTypeNum = categoryTypeNum;
	}
}
