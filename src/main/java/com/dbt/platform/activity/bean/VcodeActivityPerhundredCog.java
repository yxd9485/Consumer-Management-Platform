package com.dbt.platform.activity.bean;
import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 逢百规则Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VcodeActivityPerhundredCog extends BasicProperties {
	
	/** 主键KEY **/
	private String infoKey;
	/** 企业KEY **/
	private String companyKey;
	/** 活动主键 **/
	private String vcodeActivityKey;
	/** 逢百规则活动编号 **/
	private String perhundredNo;
	/** 逢百规则名称 **/
	private String perhundredName;
	/** 日期范围格式：开始日期#结束日期,... **/
	private String validDateRange;
	/** 最小有效日期 **/
	private String minValidDate;
	/** 最大有效日期 **/
	private String maxValidDate;
	/** 倍数格式：倍数:金额:积分;... **/
	private String perItems;
	/** 限制时间类型：0规则时间，1每天 **/
	private String restrictTimeType;
	 /** 消费限制积分 **/
    private int restrictVpoints;
    /** 消费限制金额 **/
	private double restrictMoney;
	/** 消费限制瓶数 **/
	private int restrictBottle;
	/** 状态：0未启用、1已启用 **/
	private String status;
	
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
	/** 规则消耗积分 **/
    private int ruleTotalVpoints;
    /** 规则消耗金额 **/
	private double ruleTotalMoney;
	/** 规则消耗瓶数 **/
	private int ruleTotalBottle;
	
	public VcodeActivityPerhundredCog(){}
	public VcodeActivityPerhundredCog(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.perhundredNo = paramAry.length > 0 ? paramAry[0] : "";
        this.perhundredName = paramAry.length > 1 ? paramAry[1] : "";
        this.status = paramAry.length > 2 ? paramAry[2] : "";
        this.isBegin = paramAry.length > 3 ? paramAry[3] : "";
	}
	
	public int getRuleTotalVpoints() {
		return ruleTotalVpoints;
	}
	public void setRuleTotalVpoints(int ruleTotalVpoints) {
		this.ruleTotalVpoints = ruleTotalVpoints;
	}
	public double getRuleTotalMoney() {
		return ruleTotalMoney;
	}
	public void setRuleTotalMoney(double ruleTotalMoney) {
		this.ruleTotalMoney = ruleTotalMoney;
	}
	public int getRuleTotalBottle() {
		return ruleTotalBottle;
	}
	public void setRuleTotalBottle(int ruleTotalBottle) {
		this.ruleTotalBottle = ruleTotalBottle;
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
	public String getValidDateRange() {
		return validDateRange;
	}
	public void setValidDateRange(String validDateRange) {
		this.validDateRange = validDateRange;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getSkuType() {
        return skuType;
    }
    public void setSkuType(String skuType) {
        this.skuType = skuType;
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
	public String getPerhundredNo() {
		return perhundredNo;
	}
	public void setPerhundredNo(String perhundredNo) {
		this.perhundredNo = perhundredNo;
	}
	public String getPerhundredName() {
		return perhundredName;
	}
	public void setPerhundredName(String perhundredName) {
		this.perhundredName = perhundredName;
	}
	public String getPerItems() {
		return perItems;
	}
	public void setPerItems(String perItems) {
		this.perItems = perItems;
	}
	public int getRestrictVpoints() {
		return restrictVpoints;
	}
	public void setRestrictVpoints(int restrictVpoints) {
		this.restrictVpoints = restrictVpoints;
	}
	public double getRestrictMoney() {
		return restrictMoney;
	}
	public void setRestrictMoney(double restrictMoney) {
		this.restrictMoney = restrictMoney;
	}
	public int getRestrictBottle() {
		return restrictBottle;
	}
	public void setRestrictBottle(int restrictBottle) {
		this.restrictBottle = restrictBottle;
	}
	public String getRestrictTimeType() {
		return restrictTimeType;
	}
	public void setRestrictTimeType(String restrictTimeType) {
		this.restrictTimeType = restrictTimeType;
	}
}
