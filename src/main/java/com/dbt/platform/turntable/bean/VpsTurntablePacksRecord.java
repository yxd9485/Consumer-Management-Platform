package com.dbt.platform.turntable.bean;


import org.apache.commons.lang.StringUtils;

public class VpsTurntablePacksRecord {

    private String infoKey;
    private String userKey;
    private String turntableActivityKey;
    private String earnTime;
    private double earnMoney;
    private long earnVpoints;
    private String turntablePrizeType;
    private String turntablePrizeName;
    private long consumeVpoints;
    private String prizePic;
    private String province;
    private String city;
    private String county;
    private String longitude;
    private String latitude;
    private String address;
    private String nickName;
    private String realName;
    private String phoneNumber;
    private String idCard;
    private String prizeInfoKey;
    private String startDate;
    private String endDate;
    private String depRegionId;
    private String depProvinceId;
    private String depMarketId;
    private String depCountyId;
    private String depRegionName;
    private String depProvinceName;
    private String depMarketName;
    private String depCountyName;
    private String firstDealerKey;
    private String agencyName;
    private String turntablePrizeKey;
    private String dbType;
    private String prizeDesc;

    public VpsTurntablePacksRecord(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.turntablePrizeType = paramAry.length > 0 ? paramAry[0] : "";
        this.nickName = paramAry.length > 1 ? paramAry[1] : "";
        this.startDate = paramAry.length > 2 ? paramAry[2] : "";
        this.endDate = paramAry.length > 3 ? paramAry[3] : "";
        this.turntablePrizeKey = paramAry.length > 4 ? paramAry[4] : "";
        this.depRegionId = paramAry.length > 5 ? paramAry[5] : "";
        this.depProvinceId = paramAry.length > 6 ? paramAry[6] : "";
        this.depMarketId = paramAry.length > 7 ? paramAry[7] : "";
        this.depCountyId = paramAry.length > 8 ? paramAry[8] : "";
        this.agencyName = paramAry.length > 9 ? paramAry[9] : "";

    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getTurntablePrizeKey() {
        return turntablePrizeKey;
    }

    public void setTurntablePrizeKey(String turntablePrizeKey) {
        this.turntablePrizeKey = turntablePrizeKey;
    }

    public String getDepRegionName() {
        return depRegionName;
    }

    public void setDepRegionName(String depRegionName) {
        this.depRegionName = depRegionName;
    }

    public String getDepProvinceName() {
        return depProvinceName;
    }

    public void setDepProvinceName(String depProvinceName) {
        this.depProvinceName = depProvinceName;
    }

    public String getDepMarketName() {
        return depMarketName;
    }

    public void setDepMarketName(String depMarketName) {
        this.depMarketName = depMarketName;
    }

    public String getDepCountyName() {
        return depCountyName;
    }

    public void setDepCountyName(String depCountyName) {
        this.depCountyName = depCountyName;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
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

    public String getDepMarketId() {
        return depMarketId;
    }

    public void setDepMarketId(String depMarketId) {
        this.depMarketId = depMarketId;
    }

    public String getDepCountyId() {
        return depCountyId;
    }

    public void setDepCountyId(String depCountyId) {
        this.depCountyId = depCountyId;
    }

    public String getFirstDealerKey() {
        return firstDealerKey;
    }

    public void setFirstDealerKey(String firstDealerKey) {
        this.firstDealerKey = firstDealerKey;
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

    public String getPrizeInfoKey() {
        return prizeInfoKey;
    }

    public void setPrizeInfoKey(String prizeInfoKey) {
        this.prizeInfoKey = prizeInfoKey;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getTurntableActivityKey() {
        return turntableActivityKey;
    }

    public void setTurntableActivityKey(String turntableActivityKey) {
        this.turntableActivityKey = turntableActivityKey;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }


    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getEarnTime() {
        return earnTime;
    }

    public void setEarnTime(String earnTime) {
        this.earnTime = earnTime;
    }

    public double getEarnMoney() {
        return earnMoney;
    }

    public void setEarnMoney(double earnMoney) {
        this.earnMoney = earnMoney;
    }


    public long getEarnVpoints() {
        return earnVpoints;
    }

    public void setEarnVpoints(long earnVpoints) {
        this.earnVpoints = earnVpoints;
    }


    public String getTurntablePrizeType() {
        return turntablePrizeType;
    }

    public void setTurntablePrizeType(String turntablePrizeType) {
        this.turntablePrizeType = turntablePrizeType;
    }


    public String getTurntablePrizeName() {
        return turntablePrizeName;
    }

    public void setTurntablePrizeName(String turntablePrizeName) {
        this.turntablePrizeName = turntablePrizeName;
    }


    public long getConsumeVpoints() {
        return consumeVpoints;
    }

    public void setConsumeVpoints(long consumeVpoints) {
        this.consumeVpoints = consumeVpoints;
    }


    public String getPrizePic() {
        return prizePic;
    }

    public void setPrizePic(String prizePic) {
        this.prizePic = prizePic;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrizeDesc() {
        return prizeDesc;
    }

    public void setPrizeDesc(String prizeDesc) {
        this.prizeDesc = prizeDesc;
    }

}
