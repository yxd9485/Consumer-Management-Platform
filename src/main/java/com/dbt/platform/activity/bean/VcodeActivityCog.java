package com.dbt.platform.activity.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.QrCodeUtil;

import java.util.List;

/**
 * @author RoyFu
 * @createTime 2016年1月19日 下午5:42:04
 * @description V码活动配置
 */

@SuppressWarnings("serial")
public class VcodeActivityCog extends BasicProperties {

	private String vcodeActivityKey;
	/** 企业Key */
	private String companyKey;
	/** SKUKey */
	private String skuKey;
	/** 活动名称 */
	private String vcodeActivityName;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 是否一码多扫活动，默认0否，1是 **/
	private String isMoreSweep;
	/** 一码多扫开始日期 **/
	private String sDate;
	/** 一码多扫结束日期 **/
	private String eDate;
	/** 一码多扫开始时间 **/
	private String sTime;
	/** 一码多扫开始时间 **/
	private String eTime;
	/** job是否执行完毕， 默认0否，1是**/
	private String jobFinish;
	/** 奖项模式：默认1随机奖项，2固定奖项 **/
	private String prizePattern;
	/** 有效扫码次数 **/
	private String validCount;
	/** 活动状态 */
	private String vcodeActivityStatus;
	/** 关注引导页链接 */
	private String guideLink;
	/** 是否需要首次返利 */
	private String firstRebateFlag;
    /** 首次返利类型：0 金额， 1 积分 **/
    private String firstRebateType;
	/** 首次返利积分最小值 */
	private String firstRebateMin;
	/** 首次返利积分最大值 */
	private String firstRebateMax;
	/** 首次预约配置 */
	private String firstRebateReserver;
	/** 逢百中奖规则 **/
    private String dotRuleInfo;
	/** 是否需要黑名单配置 */
	private String blacklistFlag;
	/** 规则一：同一分钟扫码次数大于等于X次 */
	private Integer sameMinuteRestrict;
	/** 规则二：同一天扫码次数大于等于X次 */
	private Integer sameDayRestrict;
	/** 规则三：历史累计扫码次数大于等于X次 */
	private Integer historyTimesRestrict;
	/** 规则四：同一月扫码次数大于等于X次 */
	private Integer sameMonthRestrict;
	/** 可疑用户可疑时间限制类型 */
	private Integer doubtfulTimeLimitType;
    /** 可疑返利类型：0 金额， 1 积分 **/
    private String doubtRebateType;
	/** 疑似黑名单规则类型：1：系数；2：区间*/
	private String doubtRuleType;
	/** 疑似黑名单规则系数值 */
	private Integer doubtRuleCoe;
	/** 疑似黑名单规则最小区间 */
	private String doubtRuleRangeMin;
	/** 疑似黑名单规则最大区间 */
	private String doubtRuleRangeMax;
	/** 黑名单规则类型 */
	private Integer dangerRuleType;
	/** 黑名单规则系数值 */
	private Integer dangerRuleCoe;
	/** 黑名单规则最小区间 */
	private String dangerRuleRangeMin;
	/** 黑名单规则最大区间 */
	private String dangerRuleRangeMax;
	/** 是否已有金额配置 */
	private String moneyConfigFlag;
	private String skuName;
	private String skuType;
	/** 活动版本：1 广东省版；2 海南省版. */
	private String activityVersion;
	/** 是否开始 0 待上线；1 已上线；2 已下线 */
	private String isBegin;
	/** 活动类型：1：一罐一码活动、2：一瓶一码活动、3：一万一批活动*/
	private String activityType;
	/** 活动年份*/
	private String activityYear;

	/** 关联的SKU列表*/
	private List<String> skuList;


    private String vcodeActivityNo;
	private String currDate;
	private String tabsFlag;
	private String surplusDays;
	private String commodityCode;
	/** 活动已开始但未配置规则异常标志，1异常*/
	private String isWarning;
	
	/** 每天扫码限制次数 **/
	private Integer everyDayScanCount;
	/** 每周扫码限制次数 **/
	private Integer everyWeekScanCount;
	/** 每月扫码限制次数 **/
	private Integer everyMonthScanCount;
    /** 活动累计扫码次数限制 **/
    private Integer activityScanCount;
	/** 阶梯主建 **/
	private String ladderInfoKey;
	/** 蒙牛扫码可扫码的角色：角色1,角色2,......*/
	private String allowScanRole;
	/** 暂时用于判断 是不是新增蒙牛活动，是蒙牛 添加 allowScanRole 字段*/
	private String dbType;
	/** 级联激励角色*/
	private String cascadeIncentiveRole;
//	private List<VcodeActivityCogExtends> jingGaoList;
//
//	public List<VcodeActivityCogExtends> getJingGaoList() {
//		return jingGaoList;
//	}
//
//	public void setJingGaoList(List<VcodeActivityCogExtends> jingGaoList) {
//		this.jingGaoList = jingGaoList;
//	}
	private String doubtableList;

	public String getDoubtableList() {
		return doubtableList;
	}

	public void setDoubtableList(String doubtableList) {
		this.doubtableList = doubtableList;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getAllowScanRole() {
		return allowScanRole;
	}

	public void setAllowScanRole(String allowScanRole) {
		this.allowScanRole = allowScanRole;
	}

    public VcodeActivityCog() {
        super();
    }

    public VcodeActivityCog(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.vcodeActivityNo = paramAry.length > 0 ? paramAry[0] : "";
        this.skuKey = paramAry.length > 1 ? paramAry[1] : "";
        this.skuType = paramAry.length > 2 ? paramAry[2] : "";
        this.startDate = paramAry.length > 3 ? paramAry[3] : "";
        this.endDate = paramAry.length > 4 ? paramAry[4] : "";
    }

    public Integer getActivityScanCount() {
        return activityScanCount;
    }

    public void setActivityScanCount(Integer activityScanCount) {
        this.activityScanCount = activityScanCount;
    }

    public Integer getDoubtfulTimeLimitType () {
		return doubtfulTimeLimitType;
	}
	
	public void setDoubtfulTimeLimitType (Integer doubtfulTimeLimitType) {
		this.doubtfulTimeLimitType = doubtfulTimeLimitType;
	}

	public Integer getSameMonthRestrict () {
			return sameMonthRestrict;
		}
		
	public void setSameMonthRestrict (Integer sameMonthRestrict) {
		this.sameMonthRestrict = sameMonthRestrict;
	}

	public String getIsWarning() {
        return isWarning;
    }

    public void setIsWarning(String isWarning) {
        this.isWarning = isWarning;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getSurplusDays() {
        return surplusDays;
    }

    public void setSurplusDays(String surplusDays) {
        this.surplusDays = surplusDays;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String getCurrDate() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public String getTabsFlag() {
        return tabsFlag;
    }

    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }

    public String getVcodeActivityNo() {
		return vcodeActivityNo;
	}

	public void setVcodeActivityNo(String vcodeActivityNo) {
		this.vcodeActivityNo = vcodeActivityNo;
	}

	public String getSkuType() {
		return skuType;
	}

	public void setSkuType(String skuType) {
		this.skuType = skuType;
	}

	public String getSDate() {
		return sDate;
	}

	public void setSDate(String sDate) {
		this.sDate = sDate;
	}

	public String getEDate() {
		return eDate;
	}

	public void setEDate(String eDate) {
		this.eDate = eDate;
	}

	public String getSTime() {
		return sTime;
	}

	public void setSTime(String sTime) {
		this.sTime = sTime;
	}

	public String getETime() {
		return eTime;
	}

	public void setETime(String eTime) {
		this.eTime = eTime;
	}

	public String getJobFinish() {
		return jobFinish;
	}

	public void setJobFinish(String jobFinish) {
		this.jobFinish = jobFinish;
	}

	public String getPrizePattern() {
		return prizePattern;
	}

	public void setPrizePattern(String prizePattern) {
		this.prizePattern = prizePattern;
	}

	public String getValidCount() {
		return validCount;
	}

	public void setValidCount(String validCount) {
		this.validCount = validCount;
	}

	public String getIsMoreSweep() {
		return isMoreSweep;
	}

	public void setIsMoreSweep(String isMoreSweep) {
		this.isMoreSweep = isMoreSweep;
	}

	public String getFirstRebateType() {
        return firstRebateType;
    }

    public void setFirstRebateType(String firstRebateType) {
        this.firstRebateType = firstRebateType;
    }

    public String getDoubtRebateType() {
        return doubtRebateType;
    }

    public void setDoubtRebateType(String doubtRebateType) {
        this.doubtRebateType = doubtRebateType;
    }

    public String getDotRuleInfo() {
		return dotRuleInfo;
	}

	public void setDotRuleInfo(String dotRuleInfo) {
		this.dotRuleInfo = dotRuleInfo;
	}

    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityYear() {
        return activityYear;
    }

    public void setActivityYear(String activityYear) {
        this.activityYear = activityYear;
    }

    public String getIsBegin() {
		return isBegin;
	}

	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
	}

	public String getActivityVersion() {
		return activityVersion;
	}

	public void setActivityVersion(String activityVersion) {
		this.activityVersion = activityVersion;
	}

	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}

	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getSkuKey() {
		return skuKey;
	}

	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}

	public String getVcodeActivityName() {
		return vcodeActivityName;
	}

	public void setVcodeActivityName(String vcodeActivityName) {
		this.vcodeActivityName = vcodeActivityName;
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

	public String getVcodeActivityStatus() {
		return vcodeActivityStatus;
	}

	public void setVcodeActivityStatus(String vcodeActivityStatus) {
		this.vcodeActivityStatus = vcodeActivityStatus;
	}

	public String getGuideLink() {
		return guideLink;
	}

	public void setGuideLink(String guideLink) {
		this.guideLink = guideLink;
	}

	public String getFirstRebateFlag() {
		return firstRebateFlag;
	}

	public void setFirstRebateFlag(String firstRebateFlag) {
		this.firstRebateFlag = firstRebateFlag;
	}

	public String getFirstRebateMin() {
		return firstRebateMin;
	}

	public void setFirstRebateMin(String firstRebateMin) {
		this.firstRebateMin = firstRebateMin;
	}

	public String getFirstRebateMax() {
		return firstRebateMax;
	}

	public void setFirstRebateMax(String firstRebateMax) {
		this.firstRebateMax = firstRebateMax;
	}
    
    public String getFirstRebateReserver() {
        return firstRebateReserver;
    }
    
    public void setFirstRebateReserver(String firstRebateReserver) {
        this.firstRebateReserver = firstRebateReserver;
    }

    public String getBlacklistFlag() {
		return blacklistFlag;
	}

	public void setBlacklistFlag(String blacklistFlag) {
		this.blacklistFlag = blacklistFlag;
	}

	public Integer getSameMinuteRestrict() {
		return sameMinuteRestrict;
	}

	public void setSameMinuteRestrict(Integer sameMinuteRestrict) {
		this.sameMinuteRestrict = sameMinuteRestrict;
	}

	public Integer getSameDayRestrict() {
		return sameDayRestrict;
	}

	public void setSameDayRestrict(Integer sameDayRestrict) {
		this.sameDayRestrict = sameDayRestrict;
	}

	public Integer getHistoryTimesRestrict() {
		return historyTimesRestrict;
	}

	public void setHistoryTimesRestrict(Integer historyTimesRestrict) {
		this.historyTimesRestrict = historyTimesRestrict;
	}

	public String getDoubtRuleType() {
		return doubtRuleType;
	}

	public void setDoubtRuleType(String doubtRuleType) {
		this.doubtRuleType = doubtRuleType;
	}

	public Integer getDoubtRuleCoe() {
		return doubtRuleCoe;
	}

	public void setDoubtRuleCoe(Integer doubtRuleCoe) {
		this.doubtRuleCoe = doubtRuleCoe;
	}

	public String getDoubtRuleRangeMin() {
		return doubtRuleRangeMin;
	}

	public void setDoubtRuleRangeMin(String doubtRuleRangeMin) {
		this.doubtRuleRangeMin = doubtRuleRangeMin;
	}

	public String getDoubtRuleRangeMax() {
		return doubtRuleRangeMax;
	}

	public void setDoubtRuleRangeMax(String doubtRuleRangeMax) {
		this.doubtRuleRangeMax = doubtRuleRangeMax;
	}

	public Integer getDangerRuleType() {
		return dangerRuleType;
	}

	public void setDangerRuleType(Integer dangerRuleType) {
		this.dangerRuleType = dangerRuleType;
	}

	public Integer getDangerRuleCoe() {
		return dangerRuleCoe;
	}

	public void setDangerRuleCoe(Integer dangerRuleCoe) {
		this.dangerRuleCoe = dangerRuleCoe;
	}

	public String getDangerRuleRangeMin() {
		return dangerRuleRangeMin;
	}

	public void setDangerRuleRangeMin(String dangerRuleRangeMin) {
		this.dangerRuleRangeMin = dangerRuleRangeMin;
	}

	public String getDangerRuleRangeMax() {
		return dangerRuleRangeMax;
	}

	public void setDangerRuleRangeMax(String dangerRuleRangeMax) {
		this.dangerRuleRangeMax = dangerRuleRangeMax;
	}

	public String getMoneyConfigFlag() {
		return moneyConfigFlag;
	}

	public void setMoneyConfigFlag(String moneyConfigFlag) {
		this.moneyConfigFlag = moneyConfigFlag;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Integer getEveryDayScanCount() {
		return everyDayScanCount;
	}

	public void setEveryDayScanCount(Integer everyDayScanCount) {
		this.everyDayScanCount = everyDayScanCount;
	}

	public Integer getEveryWeekScanCount() {
		return everyWeekScanCount;
	}

	public void setEveryWeekScanCount(Integer everyWeekScanCount) {
		this.everyWeekScanCount = everyWeekScanCount;
	}

	public Integer getEveryMonthScanCount() {
		return everyMonthScanCount;
	}

	public void setEveryMonthScanCount(Integer everyMonthScanCount) {
		this.everyMonthScanCount = everyMonthScanCount;
	}

	public String getLadderInfoKey() {
		return ladderInfoKey;
	}

	public void setLadderInfoKey(String ladderInfoKey) {
		this.ladderInfoKey = ladderInfoKey;
	}

    public String getCascadeIncentiveRole() {
        return cascadeIncentiveRole;
    }

    public void setCascadeIncentiveRole(String cascadeIncentiveRole) {
        this.cascadeIncentiveRole = cascadeIncentiveRole;
    }

	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}
}
