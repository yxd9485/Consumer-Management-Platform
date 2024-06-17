package com.dbt.platform.vdhInvitation.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

public class VpsVcodeVdhInvitationActivityCog extends BasicProperties {

    private static final long serialVersionUID = -8983788329721872328L;

    private String infoKey; //'活动主键',
    private String companyKey; //'企业主键',
    private String activityNo; //'活动编号',
    private String activityName; //'活动名称',
    private String shortName; //'活动简称',
    private String startTime; //'开始时间',
    private String endTime; //'结束时间',
    private String status; //'0未启用1已启用',
    private String backgroundUrl; //'活动背景图地址',
    private String introductionUrl; //'活动攻略图地址',
    private String posterBackgroundUrl; //'分享海报图地址',
    private String rule; //'活动规则',
    private String sku; //'活动SKU',
    private String inviterAreaType; //'邀请者活动区域类型0大区1分组',
    private String bigRegion; //'活动大区',
    private String terminalGroup; //'活动分组',
    private String newUserType; //'新用户类型',
    private String newUserRule; //'新用户激励规则',
    private String oldUserIsInvolved; //'老用户是否参与',
    private String oldUserRule; //'老用户激励规则',
    private Integer relationDays; //'邀请者与被邀请者绑定关系天数',
    private Integer incentiveNum; //'每扫一次邀请码给予多少次激励机会',
    private Integer maxIncentiveNum; //'单人领取总次数上限',
    private Integer maxIncentiveNumDay; //'单人单日领取次数上限',
    private Integer maxIncentiveForInviter; //'邀请者与同一被邀请者只能产生多少次奖励',
    private Integer maxIncentiveForInviterDay; //'邀请者与同一被邀请者单日只能产生多少次奖励',
    private Double money; //'拟投放金额',
    private String inviteeArea; //'被邀请者活动区域',
    private String routineRebateSwitch; //'被邀请者常规返利开关0关闭1开启(开启时在活动区域内需与门店建立邀请关系才能得常规返利)',
    private String prompt; //'消费者强制绑定门店提示语'
    private String modalWindowUrl; //'弹窗图片',


    //扩展字段
    private String isBegin; //'待上线 已上线 已下线',
    private String minCreateTime; //'创建时间开始值',
    private String maxCreateTime; //'创建时间结束值',
    private String[] skuKeyArray; //'完成任务sku',
    private String[] bigRegionArray; //'大区',
    private String[] terminalGroupArray; //'分组',
    private Double putInMoney; //'已投放金额',

    public VpsVcodeVdhInvitationActivityCog(){}

    public VpsVcodeVdhInvitationActivityCog(String queryParam, String isBegin){
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

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getInviterAreaType() {
        return inviterAreaType;
    }

    public void setInviterAreaType(String inviterAreaType) {
        this.inviterAreaType = inviterAreaType;
    }

    public String getBigRegion() {
        return bigRegion;
    }

    public void setBigRegion(String bigRegion) {
        this.bigRegion = bigRegion;
    }

    public String getTerminalGroup() {
        return terminalGroup;
    }

    public void setTerminalGroup(String terminalGroup) {
        this.terminalGroup = terminalGroup;
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

    public Integer getMaxIncentiveForInviterDay() {
        return maxIncentiveForInviterDay;
    }

    public void setMaxIncentiveForInviterDay(Integer maxIncentiveForInviterDay) {
        this.maxIncentiveForInviterDay = maxIncentiveForInviterDay;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getInviteeArea() {
        return inviteeArea;
    }

    public void setInviteeArea(String inviteeArea) {
        this.inviteeArea = inviteeArea;
    }

    public String getRoutineRebateSwitch() {
        return routineRebateSwitch;
    }

    public void setRoutineRebateSwitch(String routineRebateSwitch) {
        this.routineRebateSwitch = routineRebateSwitch;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getModalWindowUrl() {
        return modalWindowUrl;
    }

    public void setModalWindowUrl(String modalWindowUrl) {
        this.modalWindowUrl = modalWindowUrl;
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

    public String[] getBigRegionArray() {
        return bigRegionArray;
    }

    public void setBigRegionArray(String[] bigRegionArray) {
        this.bigRegionArray = bigRegionArray;
    }

    public String[] getTerminalGroupArray() {
        return terminalGroupArray;
    }

    public void setTerminalGroupArray(String[] terminalGroupArray) {
        this.terminalGroupArray = terminalGroupArray;
    }

    public Double getPutInMoney() {
        return putInMoney;
    }

    public void setPutInMoney(Double putInMoney) {
        this.putInMoney = putInMoney;
    }
}
