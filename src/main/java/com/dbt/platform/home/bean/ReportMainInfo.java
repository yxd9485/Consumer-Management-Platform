package com.dbt.platform.home.bean;

import java.util.Date;
/**
 * 报表总表bean
 * @author zhaohongtao
 *2017年1月6日
 */
public class ReportMainInfo {
	private String totalVpoints;//消耗积分
	private String totalUsers;//累计用户
	private String totalNewUsers;//新增用户
	private String totalScans;//累计扫码
	private String firstPercent;//首扫比例
	private String reportDate;
	private Date startDate;//开始日期
	private Date endDate;//结束日期
	private String reportType;//报表类型
	private String city;//城市
	private String totalLuckys;//中奖数量
	private String lastVpoints;//剩余积分
	private String signVpoints;//签到红包
	private String lastDays;//剩余天数
	private String luckyPercent;//中奖概率

	public String getTotalNewUsers() {
		return totalNewUsers;
	}

	public void setTotalNewUsers(String totalNewUsers) {
		this.totalNewUsers = totalNewUsers;
	}

	public String getLuckyPercent() {
		return luckyPercent;
	}

	public void setLuckyPercent(String luckyPercent) {
		this.luckyPercent = luckyPercent;
	}

	public String getLastDays() {
		return lastDays;
	}
	public void setLastDays(String lastDays) {
		this.lastDays = lastDays;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getTotalScans() {
		return totalScans;
	}
	public void setTotalScans(String totalScans) {
		this.totalScans = totalScans;
	}
	public String getTotalVpoints() {
		return totalVpoints;
	}
	public void setTotalVpoints(String totalVpoints) {
		this.totalVpoints = totalVpoints;
	}
	public String getTotalUsers() {
		return totalUsers;
	}
	public void setTotalUsers(String totalUsers) {
		this.totalUsers = totalUsers;
	}


	public String getLastVpoints() {
		return lastVpoints;
	}
	public void setLastVpoints(String lastVpoints) {
		this.lastVpoints = lastVpoints;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getFirstPercent() {
		return firstPercent;
	}
	public void setFirstPercent(String firstPercent) {
		this.firstPercent = firstPercent;
	}

	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getTotalLuckys() {
		return totalLuckys;
	}
	public void setTotalLuckys(String totalLuckys) {
		this.totalLuckys = totalLuckys;
	}

	public String getSignVpoints() {
		return signVpoints;
	}
	public void setSignVpoints(String signVpoints) {
		this.signVpoints = signVpoints;
	}
}
