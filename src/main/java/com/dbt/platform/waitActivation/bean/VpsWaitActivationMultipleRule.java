package com.dbt.platform.waitActivation.bean;

/**
 * 天降红包倍数中出规则
 */
public class VpsWaitActivationMultipleRule {
    private String infoKey;//倍数规则key
    private String activityKey;//天降红包活动key
    private int drawNum;//抽奖次数
    private double money;//必中金额
    private int putInNum;//每天投放个数
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;

    //扩展字段
    private int todayNum;

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public int getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(int drawNum) {
        this.drawNum = drawNum;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getPutInNum() {
        return putInNum;
    }

    public void setPutInNum(int putInNum) {
        this.putInNum = putInNum;
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

    public int getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(int todayNum) {
        this.todayNum = todayNum;
    }
}
