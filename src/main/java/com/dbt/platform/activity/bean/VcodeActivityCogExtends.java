package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @author RoyFu
 * @createTime 2016年1月19日 下午5:42:04
 * @description V码活动配置
 */

@SuppressWarnings("serial")
public class VcodeActivityCogExtends extends BasicProperties {

	private String extendsId;
	private String vcodeActivityKey;
	private String scanRole;
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

    public Integer getActivityScanCount() {
        return activityScanCount;
    }

    public void setActivityScanCount(Integer activityScanCount) {
        this.activityScanCount = activityScanCount;
    }

    public String getScanRole() {
		return scanRole;
	}

	public void setScanRole(String scanRole) {
		this.scanRole = scanRole;
	}

	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}

	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
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

	public Integer getSameMonthRestrict() {
		return sameMonthRestrict;
	}

	public void setSameMonthRestrict(Integer sameMonthRestrict) {
		this.sameMonthRestrict = sameMonthRestrict;
	}

	public Integer getDoubtfulTimeLimitType() {
		return doubtfulTimeLimitType;
	}

	public void setDoubtfulTimeLimitType(Integer doubtfulTimeLimitType) {
		this.doubtfulTimeLimitType = doubtfulTimeLimitType;
	}

	public String getDoubtRebateType() {
		return doubtRebateType;
	}

	public void setDoubtRebateType(String doubtRebateType) {
		this.doubtRebateType = doubtRebateType;
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

	public String getExtendsId() {
		return extendsId;
	}

	public void setExtendsId(String extendsId) {
		this.extendsId = extendsId;
	}
}
