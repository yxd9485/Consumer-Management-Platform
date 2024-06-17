/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:12:03 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.bean;

import java.util.List;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * V码活动返利规则配置 VPS_VCODE_ACTIVITY_REBATE_RULE_COG
 */
/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class VcodeActivityRebateRuleCog extends BasicProperties {

    /** 主键. */
    private String rebateRuleKey;
    
    /** 规则名称 */
    private String rebateRuleName;

    /** 活动主键. */
    private String vcodeActivityKey;

    /** 指定规则主键 **/
    private String appointRebateRuleKey;

    /** 单瓶成本 **/
    private double moneyDanping;

    /** 实际单瓶成本 **/
    private double moneyDanpingReal;

    /** 规则类型 1 节假日；2 时间段 ；3 周几；4 每天. */
    private String ruleType;

    /** 开始日期. */
    private String beginDate;

    /** 结束日期. */
    private String endDate;

    /** 开始时间. */
    private String beginTime;

    /** 结束时间. */
    private String endTime;

    /** 奖项配置方式：0Excel、1规则模板 */
    private String prizeCogType;

    /** 热点区域主键 **/
    private String hotAreaKey;

    /** 爆点红包规则信息 **/
    private String eruptRuleInfo;

    /** 爆点限制类型: 默认0规则时间内一次、 1每天一次、2不限制 **/
    private String eruptRestrictTimeType;

    /** 消费限制积分 **/
    private int restrictVpoints;

    /** 消费限制金额 **/
    private double restrictMoney;

    /** 消费限制瓶数 **/
    private int restrictBottle;

    /** 用户消费限制次数 **/
    private int restrictCount;

    /** 已消费积分 **/
    private int consumeVpoints;

    /** 已消费积分预警 **/
    private int consumeVpointsRemind;

    /** 已消费金额 **/
    private double consumeMoney;

    /** 已消费金额预警 **/
    private double consumeMoneyRemind;

    /** 已消费瓶数 **/
    private int consumeBottle;

    /** 已消费瓶数预警 **/
    private int consumeBottleRemind;

    /** 已消费总积分（指定规则） **/
    private int totalConsumeVpoints;

    /** 已消费总金额（指定规则） **/
    private double totalConsumeMoney;

    /** 已消费总瓶数（指定规则） **/
    private int totalConsumeBottle;

    /** 规则状态 0未开始 1已开始 2已结束 **/
    private String ruleStatus;

    /** 限制是否有效 **/
    private String isValid;

    /** 首扫占比 */
    private String firstScanPercent;

    /** 区域编码 */
    private String areaCode;

    /** 限制时间类型: 默认0 规则时间， 1每天 **/
    private String restrictTimeType;

    /** 规则备注 */
    private String remarks;

    /** 首扫限制当天次数 **/
    private int restrictFirstDayBottle;

    /** 首扫限制当天金额 **/
    private double restrictFirstDayMoney;

    /** 首扫限制当月次数 **/
    private int restrictFirstMonthBottle;

    /** 首扫限制当月金额 **/
    private double restrictFirstMonthMoney;

    /** 首扫限制总次数 **/
    private int restrictFirstTotalBottle;

    /** 首扫限制总金额 **/
    private double restrictFirstTotalMoney;
    /** 首扫限制标记 **/
    private String restrictFirstTime;
    /** 特殊优先级标签*/
    private String specialLabel;

    // -------------------扩展字段-------------------
    /** 活动名称 */
    private String vcodeActivityName;

    /** 活动版本 */
    private String activityVersion;

    /** 区域名称 */
    private String areaName;

    /** 规则对应的中奖配置项列表 */
    private List<VcodeActivityVpointsCog> vpointsCogLst;

    /** 规则对应的中奖配置项列表 */
    private String total;

    /** 活动类型 */
    private String activityType;

    /** 规则大致总金额 */
    private String ruleTotalMoney;
    private String ruleUnitMoney;

    /** 规则大致最积分成本 */
    private String ruleTotalVpoints;
    private String ruleUnitVpoints;

    /** 规则奖项配置项总个数 */
    private String ruleTotalPrize;
    /** 首扫是否受单瓶限制：0否、1是 */
    private String firstScanDanpingLimit;
    /** 是否启用规则新用户阶梯:0不启用、1启用*/
    private String ruleNewUserLadder;
    /** 配置单瓶成本 **/
    private String unitMoney;

    /** 校验提示信息 */
    private String validMsg;
    private boolean expireWarning;
    private boolean moneyWarning;
    private boolean vpointsWarning;
    private boolean prizeNumWarning;
    /**津贴卡返利类型：0现金，1积分*/
	private String allowanceaRebateType;
	/** 翻倍卡金额取值区间最小值 **/
	private String allowanceaMinMoney;
	/** 翻倍卡金额取值区间最大值 **/
	private String allowanceaMaxMoney;
	/** 翻倍红包最小积分区间 **/
	private String allowanceaMinVpoints;
	/** 翻倍红包最大积分区间 **/
    private String allowanceaMaxVpoints;
    /** 群组ID */
    private String groupId;
    /** 群组名称 */
    private String groupName;
    /** 是否开启红包雨：0不开启、1开启*/
    private String redPacketRain;
    /** 扫码角色 **/
    private String allowScanRole;
    
    /** 蒙牛组织机构大区主键，第4级别 **/
    private String depRegionId;
    /** 蒙牛组织机构省区主键，第5级别 **/
    private String depProvinceId;
    /** 蒙牛经销商第一级主键 **/
    private String firstDealerKey;
    
    /** 开启动效金额，默认null **/
	private String popupMoney;
	
	/** 所属大区 **/
	private String bigRegionName;
	/** 所属二级办 **/
	private String secondaryName;
	

    public String getPopupMoney() {
		return popupMoney;
	}

	public void setPopupMoney(String popupMoney) {
		this.popupMoney = popupMoney;
	}

	public String getAllowScanRole() {
        return allowScanRole;
    }

    public void setAllowScanRole(String allowScanRole) {
        this.allowScanRole = allowScanRole;
    }
    public VcodeActivityRebateRuleCog() {
        super();
    }

    public VcodeActivityRebateRuleCog(String areaCode, String ruleType, String beginDate, String endDate,
            String beginTime, String endTime) {
        this.areaCode = areaCode;
        this.ruleType = ruleType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public String getFirstScanDanpingLimit() {
        return firstScanDanpingLimit;
    }

    public void setFirstScanDanpingLimit(String firstScanDanpingLimit) {
        this.firstScanDanpingLimit = firstScanDanpingLimit;
    }

    public String getRuleNewUserLadder() {
        return ruleNewUserLadder;
    }

    public void setRuleNewUserLadder(String ruleNewUserLadder) {
        this.ruleNewUserLadder = ruleNewUserLadder;
    }

    public String getEruptRestrictTimeType() {
        return eruptRestrictTimeType;
    }

    public void setEruptRestrictTimeType(String eruptRestrictTimeType) {
        this.eruptRestrictTimeType = eruptRestrictTimeType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public double getMoneyDanping() {
        return moneyDanping;
    }

    public void setMoneyDanping(double moneyDanping) {
        this.moneyDanping = moneyDanping;
    }

    public double getMoneyDanpingReal() {
        return moneyDanpingReal;
    }

    public void setMoneyDanpingReal(double moneyDanpingReal) {
        this.moneyDanpingReal = moneyDanpingReal;
    }

    public String getFirstScanPercent() {
        return firstScanPercent;
    }

    public void setFirstScanPercent(String firstScanPercent) {
        this.firstScanPercent = firstScanPercent;
    }

    public String getRebateRuleKey() {
        return rebateRuleKey;
    }

    public void setRebateRuleKey(String rebateRuleKey) {
        this.rebateRuleKey = rebateRuleKey;
    }

    public String getRebateRuleName() {
        return rebateRuleName;
    }

    public void setRebateRuleName(String rebateRuleName) {
        this.rebateRuleName = rebateRuleName;
    }

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }

    public String getAppointRebateRuleKey() {
        return appointRebateRuleKey;
    }

    public void setAppointRebateRuleKey(String appointRebateRuleKey) {
        this.appointRebateRuleKey = appointRebateRuleKey;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPrizeCogType() {
        return prizeCogType;
    }

    public void setPrizeCogType(String prizeCogType) {
        this.prizeCogType = prizeCogType;
    }

    public String getHotAreaKey() {
        return hotAreaKey;
    }

    public void setHotAreaKey(String hotAreaKey) {
        this.hotAreaKey = hotAreaKey;
    }

    public String getEruptRuleInfo() {
        return eruptRuleInfo;
    }

    public void setEruptRuleInfo(String eruptRuleInfo) {
        this.eruptRuleInfo = eruptRuleInfo;
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

    public int getRestrictCount() {
        return restrictCount;
    }

    public void setRestrictCount(int restrictCount) {
        this.restrictCount = restrictCount;
    }

    public int getConsumeVpoints() {
        return consumeVpoints;
    }

    public void setConsumeVpoints(int consumeVpoints) {
        this.consumeVpoints = consumeVpoints;
    }

    public double getConsumeMoney() {
        return consumeMoney;
    }

    public void setConsumeMoney(double consumeMoney) {
        this.consumeMoney = consumeMoney;
    }

    public int getConsumeBottle() {
        return consumeBottle;
    }

    public void setConsumeBottle(int consumeBottle) {
        this.consumeBottle = consumeBottle;
    }

    public int getTotalConsumeVpoints() {
        return totalConsumeVpoints;
    }

    public void setTotalConsumeVpoints(int totalConsumeVpoints) {
        this.totalConsumeVpoints = totalConsumeVpoints;
    }

    public double getTotalConsumeMoney() {
        return totalConsumeMoney;
    }

    public void setTotalConsumeMoney(double totalConsumeMoney) {
        this.totalConsumeMoney = totalConsumeMoney;
    }

    public int getTotalConsumeBottle() {
        return totalConsumeBottle;
    }

    public void setTotalConsumeBottle(int totalConsumeBottle) {
        this.totalConsumeBottle = totalConsumeBottle;
    }

    public String getRuleStatus() {
        return ruleStatus;
    }

    public void setRuleStatus(String ruleStatus) {
        this.ruleStatus = ruleStatus;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getRestrictTimeType() {
        return restrictTimeType;
    }

    public void setRestrictTimeType(String restrictTimeType) {
        this.restrictTimeType = restrictTimeType;
    }

    public String getVcodeActivityName() {
        return vcodeActivityName;
    }

    public void setVcodeActivityName(String vcodeActivityName) {
        this.vcodeActivityName = vcodeActivityName;
    }

    public String getActivityVersion() {
        return activityVersion;
    }

    public void setActivityVersion(String activityVersion) {
        this.activityVersion = activityVersion;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<VcodeActivityVpointsCog> getVpointsCogLst() {
        return vpointsCogLst;
    }

    public void setVpointsCogLst(List<VcodeActivityVpointsCog> vpointsCogLst) {
        this.vpointsCogLst = vpointsCogLst;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getRuleTotalMoney() {
        return ruleTotalMoney;
    }

    public void setRuleTotalMoney(String ruleTotalMoney) {
        this.ruleTotalMoney = ruleTotalMoney;
    }

    public String getRuleUnitMoney() {
        return ruleUnitMoney;
    }

    public void setRuleUnitMoney(String ruleUnitMoney) {
        this.ruleUnitMoney = ruleUnitMoney;
    }

    public String getRuleTotalVpoints() {
        return ruleTotalVpoints;
    }

    public String getRuleUnitVpoints() {
        return ruleUnitVpoints;
    }

    public void setRuleUnitVpoints(String ruleUnitVpoints) {
        this.ruleUnitVpoints = ruleUnitVpoints;
    }

    public void setRuleTotalVpoints(String ruleTotalVpoints) {
        this.ruleTotalVpoints = ruleTotalVpoints;
    }

    public String getRuleTotalPrize() {
        return ruleTotalPrize;
    }

    public void setRuleTotalPrize(String ruleTotalPrize) {
        this.ruleTotalPrize = ruleTotalPrize;
    }

    public String getValidMsg() {
        return validMsg;
    }

    public void setValidMsg(String validMsg) {
        this.validMsg = validMsg;
    }

    public boolean isMoneyWarning() {
        return moneyWarning;
    }

    public void setMoneyWarning(boolean moneyWarning) {
        this.moneyWarning = moneyWarning;
    }

    public boolean isVpointsWarning() {
        return vpointsWarning;
    }

    public void setVpointsWarning(boolean vpointsWarning) {
        this.vpointsWarning = vpointsWarning;
    }

    public boolean isPrizeNumWarning() {
        return prizeNumWarning;
    }

    public void setPrizeNumWarning(boolean prizeNumWarning) {
        this.prizeNumWarning = prizeNumWarning;
    }

    public boolean isExpireWarning() {
        return expireWarning;
    }

    public void setExpireWarning(boolean expireWarning) {
        this.expireWarning = expireWarning;
    }

    public int getConsumeVpointsRemind() {
        return consumeVpointsRemind;
    }

    public void setConsumeVpointsRemind(int consumeVpointsRemind) {
        this.consumeVpointsRemind = consumeVpointsRemind;
    }

    public double getConsumeMoneyRemind() {
        return consumeMoneyRemind;
    }

    public void setConsumeMoneyRemind(double consumeMoneyRemind) {
        this.consumeMoneyRemind = consumeMoneyRemind;
    }

    public int getConsumeBottleRemind() {
        return consumeBottleRemind;
    }

    public void setConsumeBottleRemind(int consumeBottleRemind) {
        this.consumeBottleRemind = consumeBottleRemind;
    }

    public String getAllowanceaMinMoney() {
        return allowanceaMinMoney;
    }

    public void setAllowanceaMinMoney(String allowanceaMinMoney) {
        this.allowanceaMinMoney = allowanceaMinMoney;
    }

    public String getAllowanceaMaxMoney() {
        return allowanceaMaxMoney;
    }

    public void setAllowanceaMaxMoney(String allowanceaMaxMoney) {
        this.allowanceaMaxMoney = allowanceaMaxMoney;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

	public String getAllowanceaRebateType() {
		return allowanceaRebateType;
	}

	public void setAllowanceaRebateType(String allowanceaRebateType) {
		this.allowanceaRebateType = allowanceaRebateType;
	}

	public String getAllowanceaMinVpoints() {
		return allowanceaMinVpoints;
	}

	public void setAllowanceaMinVpoints(String allowanceaMinVpoints) {
		this.allowanceaMinVpoints = allowanceaMinVpoints;
	}

	public String getAllowanceaMaxVpoints() {
		return allowanceaMaxVpoints;
	}

	public void setAllowanceaMaxVpoints(String allowanceaMaxVpoints) {
		this.allowanceaMaxVpoints = allowanceaMaxVpoints;
	}

    public void setRestrictFirstDayMoney(int restrictFirstDayMoney) {
        this.restrictFirstDayMoney = restrictFirstDayMoney;
    }

    public int getRestrictFirstMonthBottle() {
        return restrictFirstMonthBottle;
    }

    public void setRestrictFirstMonthMoney(int restrictFirstMonthMoney) {
        this.restrictFirstMonthMoney = restrictFirstMonthMoney;
    }

    public void setRestrictFirstTotalMoney(int restrictFirstTotalMoney) {
        this.restrictFirstTotalMoney = restrictFirstTotalMoney;
    }

    public String getRestrictFirstTime() {
        return restrictFirstTime;
    }

    public void setRestrictFirstTime(String restrictFirstTime) {
        this.restrictFirstTime = restrictFirstTime;
    }

    public String getSpecialLabel() {
        return specialLabel;
    }

    public void setSpecialLabel(String specialLabel) {
        this.specialLabel = specialLabel;
    }

    public double getRestrictFirstDayMoney() {
        return restrictFirstDayMoney;
    }

    public void setRestrictFirstDayMoney(double restrictFirstDayMoney) {
        this.restrictFirstDayMoney = restrictFirstDayMoney;
    }

    public double getRestrictFirstMonthMoney() {
        return restrictFirstMonthMoney;
    }

    public void setRestrictFirstMonthMoney(double restrictFirstMonthMoney) {
        this.restrictFirstMonthMoney = restrictFirstMonthMoney;
    }

    public double getRestrictFirstTotalMoney() {
        return restrictFirstTotalMoney;
    }

    public void setRestrictFirstTotalMoney(double restrictFirstTotalMoney) {
        this.restrictFirstTotalMoney = restrictFirstTotalMoney;
    }

    public int getRestrictFirstDayBottle() {
        return restrictFirstDayBottle;
    }

    public void setRestrictFirstDayBottle(int restrictFirstDayBottle) {
        this.restrictFirstDayBottle = restrictFirstDayBottle;
    }

    public void setRestrictFirstMonthBottle(int restrictFirstMonthBottle) {
        this.restrictFirstMonthBottle = restrictFirstMonthBottle;
    }

    public int getRestrictFirstTotalBottle() {
        return restrictFirstTotalBottle;
    }

    public void setRestrictFirstTotalBottle(int restrictFirstTotalBottle) {
        this.restrictFirstTotalBottle = restrictFirstTotalBottle;
    }

    public String getUnitMoney() {
        return unitMoney;
    }

    public void setUnitMoney(String unitMoney) {
        this.unitMoney = unitMoney;
    }

    public String getRedPacketRain() {
        return redPacketRain;
    }

    public void setRedPacketRain(String redPacketRain) {
        this.redPacketRain = redPacketRain;
    }

	public String getDepRegionId() {
		return depRegionId;
	}

	public void setDepRegionId(String depRegionId) {
		this.depRegionId = depRegionId;
	}

	public String getDepProvinceId() {
		return depProvinceId;
	}

	public void setDepProvinceId(String depProvinceId) {
		this.depProvinceId = depProvinceId;
	}

	public String getFirstDealerKey() {
		return firstDealerKey;
	}

	public void setFirstDealerKey(String firstDealerKey) {
		this.firstDealerKey = firstDealerKey;
	}

	public String getBigRegionName() {
		return bigRegionName;
	}

	public void setBigRegionName(String bigRegionName) {
		this.bigRegionName = bigRegionName;
	}

	public String getSecondaryName() {
		return secondaryName;
	}

	public void setSecondaryName(String secondaryName) {
		this.secondaryName = secondaryName;
	}
	
}
