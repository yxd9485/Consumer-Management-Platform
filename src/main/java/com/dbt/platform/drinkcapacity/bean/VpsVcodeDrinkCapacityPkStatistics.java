package com.dbt.platform.drinkcapacity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 用户酒量PK1V1情况
 */
public class VpsVcodeDrinkCapacityPkStatistics extends BasicProperties {

    private static final long serialVersionUID = 1L;
    
    /** 用户主键*/
    private String userKey;
    /** PK胜的次数*/
    private int winNum;
    /** PK负的次数*/
    private int loseNum;
    /** PK平局的次数*/
    private int drawNum;
    /** PK流局的次数*/
    private int unpkNum;
    /** 最后一次有判定结果的PK时间*/
    private String lastPkTime;
    /** 最后一次PK结果：胜、负、平、流局*/
    private String lastPkResult;
    /** 最新分享比赛时间*/
    private String lastShareTime;
    /** 最新比赛开始时间*/
    private String lastBeginTime;
    /** 最新比赛记录主键*/
    private String lastPkInfoKey;
    
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getLastPkTime() {
        return lastPkTime;
    }
    public void setLastPkTime(String lastPkTime) {
        this.lastPkTime = lastPkTime;
    }
    public int getWinNum() {
        return winNum;
    }
    public void setWinNum(int winNum) {
        this.winNum = winNum;
    }
    public int getLoseNum() {
        return loseNum;
    }
    public void setLoseNum(int loseNum) {
        this.loseNum = loseNum;
    }
    public int getDrawNum() {
        return drawNum;
    }
    public void setDrawNum(int drawNum) {
        this.drawNum = drawNum;
    }
    public int getUnpkNum() {
        return unpkNum;
    }
    public void setUnpkNum(int unpkNum) {
        this.unpkNum = unpkNum;
    }
    public String getLastPkResult() {
        return lastPkResult;
    }
    public void setLastPkResult(String lastPkResult) {
        this.lastPkResult = lastPkResult;
    }
    public String getLastShareTime() {
        return lastShareTime;
    }
    public void setLastShareTime(String lastShareTime) {
        this.lastShareTime = lastShareTime;
    }
    public String getLastBeginTime() {
        return lastBeginTime;
    }
    public void setLastBeginTime(String lastBeginTime) {
        this.lastBeginTime = lastBeginTime;
    }
    public String getLastPkInfoKey() {
        return lastPkInfoKey;
    }
    public void setLastPkInfoKey(String lastPkInfoKey) {
        this.lastPkInfoKey = lastPkInfoKey;
    }
}
