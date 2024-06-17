package com.dbt.platform.invitationActivity.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

public class VpsVcodeInvitationActivityCog extends BasicProperties {
    private static final long serialVersionUID = 3495686244532592608L;

    private String infoKey; //'活动主键',
    private String activityNo; //'活动编号',
    private String activityFlag; //'活动标识',
    private String companyKey; //'企业主键',
    private String activityName; //'活动名称',
    private String shortName; //'活动简称',
    private String area; //'活动区域',
    private String sku; //'活动SKU',
    private String backgroundUrl; //'活动背景图地址',
    private String introductionUrl; //'活动攻略图地址',
    private String posterBackgroundUrl; //'分享海报图地址',
    private String rule; //'活动规则',
    private String remarks; //'备注',
    private String newUserType; //'新用户类型',
    private String newUserRule; //'新用户激励规则',
    private String oldUserIsInvolved; //'老用户是否参与',
    private String oldUserRule; //'老用户激励规则',
    private Integer relationDays; //'邀请者与被邀请者绑定关系天数',
    private Integer incentiveNum; //'每扫一次邀请码给予多少次激励机会',
    private Double money; //'拟投放金额',
    private Integer vpoints; //'拟投放积分',
    private Integer maxIncentiveNum; //'单人领取总次数上限',
    private Integer maxIncentiveNumDay; //'单人单日领取次数上限',
    private Integer maxIncentiveForInviter; //'单人领取总次数上限-针对同一个邀请者',
    private String status; //'0未启用1已启用',
    private String startTime; //'开始时间',
    private String endTime; //'结束时间',

    //扩展字段
    private String isBegin; //'待上线 已上线 已下线',
    private String minCreateTime; //'创建时间开始值',
    private String maxCreateTime; //'创建时间结束值',
    private String[] skuKeyArray; //完成任务sku
    private Double putInMoney;
    private Integer putInVpoints;

    public VpsVcodeInvitationActivityCog(){}

    public VpsVcodeInvitationActivityCog(String queryParam, String isBegin){
        this.isBegin = StringUtils.isBlank(isBegin) ? "1" : isBegin;
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.activityName = paramAry.length > 0 ? paramAry[0] : "";
        this.minCreateTime = paramAry.length > 1 ? paramAry[1] : "";
        this.maxCreateTime = paramAry.length > 2 ? paramAry[2] : "";
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getActivityFlag() {
        return activityFlag;
    }

    public void setActivityFlag(String activityFlag) {
        this.activityFlag = activityFlag;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public String getIntroductionUrl() {
        return introductionUrl;
    }

    public void setIntroductionUrl(String introductionUrl) {
        this.introductionUrl = introductionUrl;
    }

    public String getPosterBackgroundUrl() {
        return posterBackgroundUrl;
    }

    public void setPosterBackgroundUrl(String posterBackgroundUrl) {
        this.posterBackgroundUrl = posterBackgroundUrl;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getNewUserType() {
        return newUserType;
    }

    public void setNewUserType(String newUserType) {
        this.newUserType = newUserType;
    }

    public String getNewUserRule() {
        return newUserRule;
    }

    public void setNewUserRule(String newUserRule) {
        this.newUserRule = newUserRule;
    }

    public String getOldUserIsInvolved() {
        return oldUserIsInvolved;
    }

    public void setOldUserIsInvolved(String oldUserIsInvolved) {
        this.oldUserIsInvolved = oldUserIsInvolved;
    }

    public String getOldUserRule() {
        return oldUserRule;
    }

    public void setOldUserRule(String oldUserRule) {
        this.oldUserRule = oldUserRule;
    }

    public Integer getRelationDays() {
        return relationDays;
    }

    public void setRelationDays(Integer relationDays) {
        this.relationDays = relationDays;
    }

    public Integer getIncentiveNum() {
        return incentiveNum;
    }

    public void setIncentiveNum(Integer incentiveNum) {
        this.incentiveNum = incentiveNum;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getVpoints() {
        return vpoints;
    }

    public void setVpoints(Integer vpoints) {
        this.vpoints = vpoints;
    }

    public Integer getMaxIncentiveNum() {
        return maxIncentiveNum;
    }

    public void setMaxIncentiveNum(Integer maxIncentiveNum) {
        this.maxIncentiveNum = maxIncentiveNum;
    }

    public Integer getMaxIncentiveNumDay() {
        return maxIncentiveNumDay;
    }

    public void setMaxIncentiveNumDay(Integer maxIncentiveNumDay) {
        this.maxIncentiveNumDay = maxIncentiveNumDay;
    }

    public Integer getMaxIncentiveForInviter() {
        return maxIncentiveForInviter;
    }

    public void setMaxIncentiveForInviter(Integer maxIncentiveForInviter) {
        this.maxIncentiveForInviter = maxIncentiveForInviter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }

    public String getMinCreateTime() {
        return minCreateTime;
    }

    public void setMinCreateTime(String minCreateTime) {
        this.minCreateTime = minCreateTime;
    }

    public String getMaxCreateTime() {
        return maxCreateTime;
    }

    public void setMaxCreateTime(String maxCreateTime) {
        this.maxCreateTime = maxCreateTime;
    }

    public String[] getSkuKeyArray() {
        return skuKeyArray;
    }

    public void setSkuKeyArray(String[] skuKeyArray) {
        this.skuKeyArray = skuKeyArray;
    }

    public Double getPutInMoney() {
        return putInMoney;
    }

    public void setPutInMoney(Double putInMoney) {
        this.putInMoney = putInMoney;
    }

    public Integer getPutInVpoints() {
        return putInVpoints;
    }

    public void setPutInVpoints(Integer putInVpoints) {
        this.putInVpoints = putInVpoints;
    }
}
