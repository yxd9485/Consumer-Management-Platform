package com.dbt.platform.activity.bean;
import org.apache.commons.lang.StringUtils;
import com.dbt.framework.base.bean.BasicProperties;

/**
 * 一码多扫规则Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VcodeActivityMorescanCog extends BasicProperties {
	/** 主键KEY **/
	private String infoKey;
	/** 企业KEY **/
	private String companyKey;
	/** 活动主键 **/
	private String vcodeActivityKey;
	/** 一码多扫活动编号 **/
	private String morescanNo;
	/** 一码多扫名称 **/
	private String morescanName;
	/** 日期范围格式：开始日期#结束日期,... **/
	private String validDateRange;
	/** 时间范围格式：开始时间#结束时间,... **/
	private String validTimeRange;
	/** 最小有效日期 **/
	private String minValidDate;
	/** 最大有效日期 **/
	private String maxValidDate;
	/** 奖项模式：默认1随机奖项，2固定奖项 **/
	private String prizePattern;
	/** 有效扫码次数，默认1次 **/
	private int validCount;
	/** 二维码首扫后有效天数 **/
	private double qrcodeValidHour;
	/** 状态：0未启用、1已启用 **/
	private String status;
    /** 热点区域主键 **/
    private String hotAreaKey;
	/** 用户角色 **/
	private String roleInfo;
	
	/** 扩充字段 **/
	/** 活动名称 **/
	private String vcodeActivityName;
	/** SKU名称 **/
	private String skuName;
    /** SKU类型 **/
    private String skuType;
	/** 上线状态：0 待上线，1 已上线，2 已过期 **/
	private String isBegin;
	/** 选项卡标识 **/
	private String tabsFlag;


	public String getRoleInfo() {
		return roleInfo;
	}
	public void setRoleInfo(String roleInfo) {
		this.roleInfo = roleInfo;
	}
	public String getSkuType() {
        return skuType;
    }
    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }
	public double getQrcodeValidHour() {
        return qrcodeValidHour;
    }
    public void setQrcodeValidHour(double qrcodeValidHour) {
        this.qrcodeValidHour = qrcodeValidHour;
    }
    public String getTabsFlag() {
		return tabsFlag;
	}
	public void setTabsFlag(String tabsFlag) {
		this.tabsFlag = tabsFlag;
	}
	public String getIsBegin() {
		return isBegin;
	}
	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
	}
	public VcodeActivityMorescanCog(){}
	public VcodeActivityMorescanCog(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.morescanNo = paramAry.length > 0 ? paramAry[0] : "";
        this.morescanName = paramAry.length > 1 ? paramAry[1] : "";
        this.status = paramAry.length > 2 ? paramAry[2] : "";
        this.isBegin = paramAry.length > 3 ? paramAry[3] : "";
        this.prizePattern = paramAry.length > 4 ? paramAry[4] : "";
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}
	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}
	public String getMorescanNo() {
		return morescanNo;
	}
	public void setMorescanNo(String morescanNo) {
		this.morescanNo = morescanNo;
	}
	public String getMorescanName() {
		return morescanName;
	}
	public void setMorescanName(String morescanName) {
		this.morescanName = morescanName;
	}
	public String getValidDateRange() {
		return validDateRange;
	}
	public void setValidDateRange(String validDateRange) {
		this.validDateRange = validDateRange;
	}
	public String getValidTimeRange() {
		return validTimeRange;
	}
	public void setValidTimeRange(String validTimeRange) {
		this.validTimeRange = validTimeRange;
	}
	public String getPrizePattern() {
		return prizePattern;
	}
	public void setPrizePattern(String prizePattern) {
		this.prizePattern = prizePattern;
	}
	public int getValidCount() {
		return validCount;
	}
	public void setValidCount(int validCount) {
		this.validCount = validCount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHotAreaKey() {
        return hotAreaKey;
    }
    public void setHotAreaKey(String hotAreaKey) {
        this.hotAreaKey = hotAreaKey;
    }
    public String getVcodeActivityName() {
		return vcodeActivityName;
	}
	public void setVcodeActivityName(String vcodeActivityName) {
		this.vcodeActivityName = vcodeActivityName;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getMaxValidDate() {
		return maxValidDate;
	}
	public void setMaxValidDate(String maxValidDate) {
		this.maxValidDate = maxValidDate;
	}
	public String getMinValidDate() {
		return minValidDate;
	}
	public void setMinValidDate(String minValidDate) {
		this.minValidDate = minValidDate;
	}
}
