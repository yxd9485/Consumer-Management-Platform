package com.dbt.platform.ticket.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

/**
 *优惠券基础面额Bean
 */
public class VpsSysTicketInfo extends BasicProperties {

    private static final long serialVersionUID = 1009656056443543868L;
    
    /** 主键 */
    private String infoKey;
    /** 券类型主键 */
    private String categoryKey;
    /** 券编号：券码格式：QV0001，连接格式：QU0001 */
    private String ticketNo;
    /** 券活动名称 */
    private String ticketName;
    /** 券面额 */
    private String ticketMoney;
    /** 券类型：0链接，1券码 */
    private String ticketType;
    /** 券活动描述 */
    private String ticketDesc;
    /** 券类型名称 */
    private String categoryName;
    /** 券类型:H */
    private String categoryType;
	/** 券图H */
	private String ticketPicUrl;
	/** 券状态 */
	private String ticketStatus;
	/** 领取后使用天数 */
	private String expiredDay;
	/** 开始时间 */
	private String startDate;
	/** 结束时间 */
	private String endDate;
	/** 结束时间 */
	private String createTime;
	/** 优惠券详情URL */
	private String ticketUrl;
	/** 跳转标识 0小程序 1H5 */
	private String jumpFlag;
	/** 优惠券数量 **/
	private Long ticketCount;
	/** 导入文件名称 */
	private String fileName;
	/** 券码表名称 */
	private String libName;
	/** 发布开始时间 */
	private String releaseStartDate;
	/** 发布结束时间 */
	private String releaseEndDate;
	/** 发布结束时间 */
	private String ticketUseCount;

	//值得买扩展字段
	private String couponId;//优惠券 id
	private int couponGenre;//优惠券类型，1无门槛，2满减，3折扣
	private String atLeast;//满减、折扣类型优惠券的门槛要求满多少可用
	private String discount;//折扣类型优惠券折扣
	private int maxFetch;//每人最大领取数目
	private String couponDesc;//优惠券说明信息
	private String couponEffectType;//值得买-有效时间类型-0自定义时间-1领取后n天内有效
	private String couponStartTime;//有效期-可使用开始时间
	private String couponEndTime;//有效期-可使用结束时间
	private String effectDay;// 领取后有效天数

    public VpsSysTicketInfo() {}
	public VpsSysTicketInfo(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
		this.ticketName = paramAry.length > 0 ? paramAry[0] : "";
		this.categoryKey = paramAry.length > 1 ? paramAry[1] : "";
		this.endDate = paramAry.length > 2 ? paramAry[2] : "";
	}

	public String getTicketUseCount() {
		return ticketUseCount;
	}

	public void setTicketUseCount(String ticketUseCount) {
		this.ticketUseCount = ticketUseCount;
	}

	public String getLibName() {
		return libName;
	}

	public void setLibName(String libName) {
		this.libName = libName;
	}

	public String getTicketUrl() {
		return ticketUrl;
	}

	public void setTicketUrl(String ticketUrl) {
		this.ticketUrl = ticketUrl;
	}

	public String getJumpFlag() {
		return jumpFlag;
	}

	public void setJumpFlag(String jumpFlag) {
		this.jumpFlag = jumpFlag;
	}

	public Long getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(Long ticketCount) {
		this.ticketCount = ticketCount;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getReleaseStartDate() {
		return releaseStartDate;
	}

	public void setReleaseStartDate(String releaseStartDate) {
		this.releaseStartDate = releaseStartDate;
	}

	public String getReleaseEndDate() {
		return releaseEndDate;
	}

	public void setReleaseEndDate(String releaseEndDate) {
		this.releaseEndDate = releaseEndDate;
	}

	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getCategoryKey() {
		return categoryKey;
	}
	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}
	public String getTicketNo() {
		return ticketNo;
	}
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}
	public String getTicketType() {
		return ticketType;
	}
	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
	
	public String getTicketName() {
		return ticketName;
	}
	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getTicketDesc() {
		return ticketDesc;
	}
	public void setTicketDesc(String ticketDesc) {
		this.ticketDesc = ticketDesc;
	}
	public String getTicketMoney() {
		return ticketMoney;
	}
	public void setTicketMoney(String ticketMoney) {
		this.ticketMoney = ticketMoney;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getTicketPicUrl() {
		return ticketPicUrl;
	}

	public void setTicketPicUrl(String ticketPicUrl) {
		this.ticketPicUrl = ticketPicUrl;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getExpiredDay() {
		return expiredDay;
	}

	public void setExpiredDay(String expiredDay) {
		this.expiredDay = expiredDay;
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

	@Override
	public String getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public int getCouponGenre() {
		return couponGenre;
	}

	public void setCouponGenre(int couponGenre) {
		this.couponGenre = couponGenre;
	}

	public String getAtLeast() {
		return atLeast;
	}

	public void setAtLeast(String atLeast) {
		this.atLeast = atLeast;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public int getMaxFetch() {
		return maxFetch;
	}

	public void setMaxFetch(int maxFetch) {
		this.maxFetch = maxFetch;
	}

	public String getCouponDesc() {
		return couponDesc;
	}

	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}

	public String getCouponEffectType() {
		return couponEffectType;
	}

	public void setCouponEffectType(String couponEffectType) {
		this.couponEffectType = couponEffectType;
	}

	public String getCouponStartTime() {
		return couponStartTime;
	}

	public void setCouponStartTime(String couponStartTime) {
		this.couponStartTime = couponStartTime;
	}

	public String getCouponEndTime() {
		return couponEndTime;
	}

	public void setCouponEndTime(String couponEndTime) {
		this.couponEndTime = couponEndTime;
	}

	public String getEffectDay() {
		return effectDay;
	}

	public void setEffectDay(String effectDay) {
		this.effectDay = effectDay;
	}
}
