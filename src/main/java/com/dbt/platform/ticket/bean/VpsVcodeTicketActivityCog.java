package com.dbt.platform.ticket.bean;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *优惠券活动Bean
 */
public class VpsVcodeTicketActivityCog extends BasicProperties {

    private static final long serialVersionUID = 1009656056443543868L;
    
    /** 主键 */
    private String activityKey;
    /** 企业KEY */
    private String companyKey;
    /** 券活动编号 */
    private String activityNo;
    /** 券活动类型主键 */
    private String categoryKey;
    /** 券活动类型名称 */
    private String categoryName;
    /** 券活动名称 */
    private String activityName;
    /** 券活动描述 */
    private String activityDesc;
    /** 有效开始日期*/
    private String startDate;
    /** 有效结束日期*/
    private String endDate;
    /** 中出方式：1每天一次，2活动期间一次 **/
    private String ticketLimit;
	/** 中出方式次数 **/
    private Integer ticketLimitNum;
    /** 优惠券码库表 **/
    private String libName;
    /** 活动状态：0未上线，1已上线，2已过期 **/
    private String isBegin;
    /** 状态：0未启用、1已启用 **/
    private String status;
    /** 优惠券面额信息 **/
    private List<VpsVcodeTicketInfo> vcodeTicketInfoList;
    
    /** 优惠券基础面额list **/
    private List<VpsSysTicketInfo> sysTicketInfoList;
    private String sysTicketInfoGro;
    
    public VpsVcodeTicketActivityCog() {}
    public VpsVcodeTicketActivityCog(String queryparam) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryparam, "").split(",");
        this.activityName = paramAry.length > 0 ? paramAry[0] : "";
        this.categoryKey = paramAry.length > 1 ? paramAry[1] : "";
        this.startDate = paramAry.length > 2 ? paramAry[2] : "";
        this.endDate = paramAry.length > 3 ? paramAry[3] : "";
    }
    
    /** 拼接优惠券基础面额 **/
	public String getSysTicketInfoGro() {
		StringBuilder builder = new StringBuilder();
		if(!CollectionUtils.isEmpty(sysTicketInfoList)){
			for (VpsSysTicketInfo item : sysTicketInfoList) {
				builder.append(item.getTicketNo());
				builder.append(":");
				builder.append(item.getTicketName());
				builder.append(":");
				if("0".equals(item.getTicketType())){
					builder.append("（链接）");
				}else if("1".equals(item.getTicketType())){
					builder.append("（券码）");
				}else if("2".equals(item.getTicketType())){
				    builder.append("（识别图片）");
				}else if("3".equals(item.getTicketType())){
					builder.append("（动态券码）");
				}else if("4".equals(item.getTicketType())){
					builder.append("（活动编码）");
				}
				builder.append(";");
			}
			sysTicketInfoGro = builder.substring(0, builder.length()-1);
		}
		return sysTicketInfoGro;
	}

	public Integer getTicketLimitNum() {
		return ticketLimitNum;
	}

	public void setTicketLimitNum(Integer ticketLimitNum) {
		this.ticketLimitNum = ticketLimitNum;
	}

	public String getLibName() {
		return libName;
	}
	public void setLibName(String libName) {
		this.libName = libName;
	}
	public List<VpsVcodeTicketInfo> getVcodeTicketInfoList() {
		return vcodeTicketInfoList;
	}
	public void setVcodeTicketInfoList(List<VpsVcodeTicketInfo> vcodeTicketInfoList) {
		this.vcodeTicketInfoList = vcodeTicketInfoList;
	}
	public String getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getActivityNo() {
		return activityNo;
	}
	public void setActivityNo(String activityNo) {
		this.activityNo = activityNo;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getActivityDesc() {
		return activityDesc;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
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
	public String getIsBegin() {
		return isBegin;
	}
	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
	}
	public String getCategoryKey() {
		return categoryKey;
	}
	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}
	public List<VpsSysTicketInfo> getSysTicketInfoList() {
		return sysTicketInfoList;
	}
	public void setSysTicketInfoList(List<VpsSysTicketInfo> sysTicketInfoList) {
		this.sysTicketInfoList = sysTicketInfoList;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTicketLimit() {
		return ticketLimit;
	}
	public void setTicketLimit(String ticketLimit) {
		this.ticketLimit = ticketLimit;
	}
}
