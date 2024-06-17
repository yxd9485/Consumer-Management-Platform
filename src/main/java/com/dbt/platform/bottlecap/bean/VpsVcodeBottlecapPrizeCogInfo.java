package com.dbt.platform.bottlecap.bean;


public class VpsVcodeBottlecapPrizeCogInfo {

    private String infoKey;
    /** 集盖活动key **/
    private String boottlecapActivityKey;
    /** 阶梯等级 **/
    private String ladder;
    /** 积盖奖项类型(0红包 1积分 2实物奖) **/
    private String boottlecapPrizeType;
    /** 兑换需要瓶盖数量 **/
    private Integer consumeBoottlecap;
    /** 区间最小金额：元 **/
    private Double minMoney;
    /** 区间最大金额：元 **/
    private Double maxMoney;
    /** 区间最小积分 **/
    private Integer minVpoints;
    /** 区间最大积分 **/
    private Integer maxVpoints;
    /** 实物奖奖品类型 **/
    private String bigPrizeType;
    /** 投放数量 **/
    private Integer launchAmount;
    /** 已投放奖项数量 **/
    private Integer receiveAmount;
    /** 已投放金额数量 **/
    private Double receiveMoneyAmount;
    /** 已投放积分数量 **/
    private Integer receiveVpointsAmount;
    /** 限制单人兑换次数 **/
    private Integer limitExchange;
    /** 实物奖领奖截止时间 **/
    private String receiveEndTime;
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;

    public String getReceiveEndTime() {
        return receiveEndTime;
    }

    public void setReceiveEndTime(String receiveEndTime) {
        this.receiveEndTime = receiveEndTime;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getBoottlecapActivityKey() {
        return boottlecapActivityKey;
    }

    public void setBoottlecapActivityKey(String boottlecapActivityKey) {
        this.boottlecapActivityKey = boottlecapActivityKey;
    }

    public String getLadder() {
        return ladder;
    }

    public void setLadder(String ladder) {
        this.ladder = ladder;
    }

    public String getBoottlecapPrizeType() {
        return boottlecapPrizeType;
    }

    public void setBoottlecapPrizeType(String boottlecapPrizeType) {
        this.boottlecapPrizeType = boottlecapPrizeType;
    }

    public Integer getConsumeBoottlecap() {
        return consumeBoottlecap;
    }

    public void setConsumeBoottlecap(Integer consumeBoottlecap) {
        this.consumeBoottlecap = consumeBoottlecap;
    }

    public Double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(Double minMoney) {
        this.minMoney = minMoney;
    }

    public Double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(Double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public Integer getMinVpoints() {
        return minVpoints;
    }

    public void setMinVpoints(Integer minVpoints) {
        this.minVpoints = minVpoints;
    }

    public Integer getMaxVpoints() {
        return maxVpoints;
    }

    public void setMaxVpoints(Integer maxVpoints) {
        this.maxVpoints = maxVpoints;
    }

    public String getBigPrizeType() {
        return bigPrizeType;
    }

    public void setBigPrizeType(String bigPrizeType) {
        this.bigPrizeType = bigPrizeType;
    }

    public Integer getLaunchAmount() {
        return launchAmount;
    }

    public void setLaunchAmount(Integer launchAmount) {
        this.launchAmount = launchAmount;
    }

    public Integer getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(Integer receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public Double getReceiveMoneyAmount() {
        return receiveMoneyAmount;
    }

    public void setReceiveMoneyAmount(Double receiveMoneyAmount) {
        this.receiveMoneyAmount = receiveMoneyAmount;
    }

    public Integer getReceiveVpointsAmount() {
        return receiveVpointsAmount;
    }

    public void setReceiveVpointsAmount(Integer receiveVpointsAmount) {
        this.receiveVpointsAmount = receiveVpointsAmount;
    }

    public Integer getLimitExchange() {
        return limitExchange;
    }

    public void setLimitExchange(Integer limitExchange) {
        this.limitExchange = limitExchange;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
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
}
