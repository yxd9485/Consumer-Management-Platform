package com.dbt.platform.turntable.bean;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 转盘抽奖活动配置bean
 */
public class TurntableActivityCogInfo {
    private String activityKey;
    private String activityName;
    private String status;
    private String startDate;
    private String endDate;
    private Integer everyDayLimit;
    private Integer totalLimit;
    private Integer turntableNum;
    private String custcarePhonenum;
    /*抽奖人数*/
    private Integer participateNum;
    /*中奖人数*/
    private Integer lotteryNum;
    private String rulePic;
    private String roleLimit;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String activityStatus;
    private Integer consumeVpoints;
    private Integer bigPrizeLimit;
    private String areaCode;
    private String areaName;
    private Integer activityDrawNumber;
    private Integer activityWinNumber;
    /** 蒙牛组织机构大区主键，第4级别 **/
    private String depRegionId;
    /** 蒙牛组织机构省区主键，第5级别 **/
    private String depProvinceId;
    /** 蒙牛经销商第一级主键 **/
    private String firstDealerKey;

    private List<VpsTurntablePrizeCog> turntablePrizeCogList;
    /** 活动状态：0未上线，1已上线，2已过期 **/
    private String isBegin;
    /** 活动类型：0 转盘，1盲盒 **/
    private String activityType;
    /**
     * 引流类型：0、引流 1、活动
     */
    private String drainageType;
    //抽奖次数
    private String scanSkuNumber;
    /**
     *参与条件: 0 消耗积分 1 消耗抽奖次数
     */
    private String joinCondition;
    //每日获取抽奖次数限制
    private String everyDayDrawLimit;
    private String skuKeys;
    private String[] skuKeyArray;

    public String getSkuKeys() {
        return skuKeys;
    }

    public void setSkuKeys(String skuKeys) {
        this.skuKeys = skuKeys;
        if (StringUtils.isNotEmpty(this.skuKeys)) {
            this.skuKeyArray = StringUtils.split(this.skuKeys, ",");
        }
    }

    public String[] getSkuKeyArray() {
        if (StringUtils.isNotEmpty(this.skuKeys)) {
            this.skuKeyArray =  StringUtils.split(this.skuKeys, ",");
        }
        return skuKeyArray;
    }

    public void setSkuKeyArray(String[] skuKeyArray) {
        this.skuKeys =  StringUtils.join(this.skuKeyArray, ",");
        this.skuKeyArray = skuKeyArray;
    }

    public String getEveryDayDrawLimit() {
        return everyDayDrawLimit;
    }

    public void setEveryDayDrawLimit(String everyDayDrawLimit) {
        this.everyDayDrawLimit = everyDayDrawLimit;
    }

    public String getScanSkuNumber() {
        return scanSkuNumber;
    }

    public void setScanSkuNumber(String scanSkuNumber) {
        this.scanSkuNumber = scanSkuNumber;
    }

    public String getJoinCondition() {
        return joinCondition;
    }

    public void setJoinCondition(String joinCondition) {
        this.joinCondition = joinCondition;
    }

    public String getDrainageType() {
        return drainageType;
    }

    public void setDrainageType(String drainageType) {
        this.drainageType = drainageType;
    }

    public TurntableActivityCogInfo() {};
    public TurntableActivityCogInfo(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.activityName = paramAry.length > 0 ? paramAry[0] : "";
        this.activityStatus = paramAry.length > 1 ? paramAry[1] : "";
        this.status = paramAry.length > 2 ? paramAry[2] : "";
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
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

    public Integer getActivityDrawNumber() {
        return activityDrawNumber;
    }

    public void setActivityDrawNumber(Integer activityDrawNumber) {
        this.activityDrawNumber = activityDrawNumber;
    }

    public Integer getActivityWinNumber() {
        return activityWinNumber;
    }

    public void setActivityWinNumber(Integer activityWinNumber) {
        this.activityWinNumber = activityWinNumber;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }

    public List<VpsTurntablePrizeCog> getTurntablePrizeCogList() {
        return turntablePrizeCogList;
    }

    public void setTurntablePrizeCogList(List<VpsTurntablePrizeCog> turntablePrizeCogList) {
        this.turntablePrizeCogList = turntablePrizeCogList;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getEveryDayLimit() {
        return everyDayLimit;
    }

    public void setEveryDayLimit(Integer everyDayLimit) {
        this.everyDayLimit = everyDayLimit;
    }

    public Integer getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(Integer totalLimit) {
        this.totalLimit = totalLimit;
    }

    public Integer getTurntableNum() {
        return turntableNum;
    }

    public void setTurntableNum(Integer turntableNum) {
        this.turntableNum = turntableNum;
    }

    public String getCustcarePhonenum() {
        return custcarePhonenum;
    }

    public void setCustcarePhonenum(String custcarePhonenum) {
        this.custcarePhonenum = custcarePhonenum;
    }

    public Integer getParticipateNum() {
        return participateNum;
    }

    public void setParticipateNum(Integer participateNum) {
        this.participateNum = participateNum;
    }

    public Integer getLotteryNum() {
        return lotteryNum;
    }

    public void setLotteryNum(Integer lotteryNum) {
        this.lotteryNum = lotteryNum;
    }

    public String getRulePic() {
        return rulePic;
    }

    public void setRulePic(String rulePic) {
        this.rulePic = rulePic;
    }

    public String getRoleLimit() {
        return roleLimit;
    }

    public void setRoleLimit(String roleLimit) {
        this.roleLimit = roleLimit;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getConsumeVpoints() {
        return consumeVpoints;
    }

    public void setConsumeVpoints(Integer consumeVpoints) {
        this.consumeVpoints = consumeVpoints;
    }

    public Integer getBigPrizeLimit() {
        return bigPrizeLimit;
    }

    public void setBigPrizeLimit(Integer bigPrizeLimit) {
        this.bigPrizeLimit = bigPrizeLimit;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
}


