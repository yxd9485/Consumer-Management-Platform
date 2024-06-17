package com.dbt.platform.ticket.bean;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.util.RedisApiUtil;

/**
 *优惠券活动Bean
 */
public class VpsVcodeTicketInfo extends BasicProperties {

    private static final long serialVersionUID = 1009656056443543868L;
    
    /** 主键 */
    private String ticketKey;
    /** 活动KEY */
    private String activityKey;
    /** 券编号：券码格式：QV0001，连接格式：QU0001 */
    private String ticketNo;
    /** 优惠券详情URL */
    private String ticketUrl;
    /** 中出方式：1每天一次，2活动期间一次 **/
    private String ticketLimit;
    private Integer ticketLimitNum;
    /** 导入文件名称 */
    private String fileName;
    /** 优惠券数量 **/
    private Long ticketCount;
    /** 优惠券剩余数量 **/
    private Long surplusCount;
    /** 优惠券使用数量 **/
    private Long useCount;
    private String ticketType;
    private String ticketName;
	private String jumpFlag;
	private String ticketUseCount;
    
    public VpsVcodeTicketInfo() {}
    public VpsVcodeTicketInfo(String queryparam) {
        
    }

	public String getTicketUseCount() {
		return ticketUseCount;
	}

	public void setTicketUseCount(String ticketUseCount) {
		this.ticketUseCount = ticketUseCount;
	}

	public String getJumpFlag() {
		return jumpFlag;
	}

	public void setJumpFlag(String jumpFlag) {
		this.jumpFlag = jumpFlag;
	}

	public String getTicketKey() {
		return ticketKey;
	}
	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public String getTicketUrl() {
		return ticketUrl;
	}
	public void setTicketUrl(String ticketUrl) {
		this.ticketUrl = ticketUrl;
	}
	public String getActivityKey() {
		return activityKey;
	}
	public void setActivityKey(String activityKey) {
		this.activityKey = activityKey;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	public Long getTicketCount() {
		return ticketCount;
	}
	public void setTicketCount(Long ticketCount) {
		this.ticketCount = ticketCount;
	}
	public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Long getSurplusCount() {
		return surplusCount;
	}
	public void setSurplusCount(Long surplusCount) {
		this.surplusCount = surplusCount;
	}
	public Long getUseCount() {
		return useCount;
	}
	public void setUseCount(Long useCount) {
		this.useCount = useCount;
	}
	public String getTicketLimit() {
		return ticketLimit;
	}
	public void setTicketLimit(String ticketLimit) {
		this.ticketLimit = ticketLimit;
	}

	public Integer getTicketLimitNum() {
		return ticketLimitNum;
	}

	public void setTicketLimitNum(Integer ticketLimitNum) {
		this.ticketLimitNum = ticketLimitNum;
	}
}
