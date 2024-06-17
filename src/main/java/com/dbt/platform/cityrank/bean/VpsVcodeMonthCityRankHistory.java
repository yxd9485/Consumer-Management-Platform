package com.dbt.platform.cityrank.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 城市酒王月度历史排名记录表
 */
public class VpsVcodeMonthCityRankHistory extends BasicProperties {

    private static final long serialVersionUID = 1611527739197954598L;
    
    private String infoKey;
    private String cityName;
    private String rankMonth;
    private String userKey;
    private int rankNum;
    private int scanNum;
    private String nickName;
    private String headImgUrl;
    private String rankTitle;
    
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getHeadImgUrl() {
        return headImgUrl;
    }
    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getRankMonth() {
        return rankMonth;
    }
    public void setRankMonth(String rankMonth) {
        this.rankMonth = rankMonth;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public int getRankNum() {
        return rankNum;
    }
    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }
    public String getRankTitle() {
        return rankTitle;
    }
    public void setRankTitle(String rankTitle) {
        this.rankTitle = rankTitle;
    }
    public int getScanNum() {
        return scanNum;
    }
    public void setScanNum(int scanNum) {
        this.scanNum = scanNum;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
