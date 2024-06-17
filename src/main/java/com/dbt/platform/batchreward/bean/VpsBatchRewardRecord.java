package com.dbt.platform.batchreward.bean;


import org.apache.commons.lang.StringUtils;

public class VpsBatchRewardRecord {

  private String infoKey;
  private String grantNo;
  private long userCount;
  private long grantVpoints;
  private double grantMoney;
  private long grantBigprize;
  private String grantTime;
  private String grantUser;
  private String filePath;
  private String keyword;
  private String startTime;
  private String endTIme;
  public VpsBatchRewardRecord() {};
  public VpsBatchRewardRecord(String queryParam) {
    String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
    this.keyword = paramAry.length > 0 ? paramAry[0] : "";
    this.startTime = paramAry.length > 1 ? paramAry[1] : "";
    this.endTIme = paramAry.length > 2 ? paramAry[2] : "";
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTIme() {
    return endTIme;
  }

  public void setEndTIme(String endTIme) {
    this.endTIme = endTIme;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getInfoKey() {
    return infoKey;
  }

  public void setInfoKey(String infoKey) {
    this.infoKey = infoKey;
  }

  public String getGrantNo() {
    return grantNo;
  }

  public void setGrantNo(String grantNo) {
    this.grantNo = grantNo;
  }

  public long getUserCount() {
    return userCount;
  }

  public void setUserCount(long userCount) {
    this.userCount = userCount;
  }

  public long getGrantVpoints() {
    return grantVpoints;
  }

  public void setGrantVpoints(long grantVpoints) {
    this.grantVpoints = grantVpoints;
  }

  public double getGrantMoney() {
    return grantMoney;
  }

  public void setGrantMoney(double grantMoney) {
    this.grantMoney = grantMoney;
  }

  public long getGrantBigprize() {
    return grantBigprize;
  }

  public void setGrantBigprize(long grantBigprize) {
    this.grantBigprize = grantBigprize;
  }

  public String getGrantTime() {
    return grantTime;
  }

  public void setGrantTime(String grantTime) {
    this.grantTime = grantTime;
  }

  public String getGrantUser() {
    return grantUser;
  }

  public void setGrantUser(String grantUser) {
    this.grantUser = grantUser;
  }
}
