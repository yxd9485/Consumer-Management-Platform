package com.dbt.platform.waitActivation.bean;

/**
 * 天降红包领取记录
 */
public class VpsWaitActivationPacksRecord {
      private String infoKey;                   //'抽奖记录主键',
      private String sourceChannel;             // '来源渠道，0：天降红包',
      private String userKey;                   // '用户主键',
      private String activityKey;               // '活动主键',
      private String rebateRuleKey;             // '规则主键',
      private String vpointsCogKey;             // '奖项主键',
      private String prizeDesc;                 // '奖品描述',
      private String isWaitActivation;          // '是否待激活，0：否；1是',
      private String expireTime;                // '待激活过期时间',
      private String activationChannel;         // 激活渠道 0 扫码激活 ，1终端引流激活 默认0
      private String activationStatus;          // '是否已激活，0：否；1是',
      private String activationTime;            // '激活时间',
      private String prizeType;                 // '奖项类型，0：现金红包；8：谢谢参与；YHQ：商城优惠券',
      private String earnTime;                  // '领取时间',
      private String earnMoney;                 // '领取金额',
      private String earnVpoints;               // '领取积分',
      private String prizeInfoKey;              // '大奖主键',
      private String prizeRecordKey;            // '大奖领取记录表主键',
      private String prizeName;                 // '大奖名称',
      private String address;                   // '地址',
      private String longitude;                 // '经度',
      private String latitude;                  // '纬度',
      private String province;                  // '省',
      private String city;                      // '市',
      private String county;                    // '县',
      private String testStatus;                // '线上测试标识',
      private String tableNameIdx;

      //扩展字段
      private String residueTime;               // 剩余时间
      private String timeUnit;                  // 剩余时间 单位
      private String isExpire;                  // 未激活红包是否已过期

      public String getInfoKey() {
            return infoKey;
      }

      public void setInfoKey(String infoKey) {
            this.infoKey = infoKey;
      }

      public String getSourceChannel() {
            return sourceChannel;
      }

      public void setSourceChannel(String sourceChannel) {
            this.sourceChannel = sourceChannel;
      }

      public String getUserKey() {
            return userKey;
      }

      public void setUserKey(String userKey) {
            this.userKey = userKey;
      }

      public String getActivityKey() {
            return activityKey;
      }

      public void setActivityKey(String activityKey) {
            this.activityKey = activityKey;
      }

      public String getRebateRuleKey() {
            return rebateRuleKey;
      }

      public void setRebateRuleKey(String rebateRuleKey) {
            this.rebateRuleKey = rebateRuleKey;
      }

      public String getVpointsCogKey() {
            return vpointsCogKey;
      }

      public void setVpointsCogKey(String vpointsCogKey) {
            this.vpointsCogKey = vpointsCogKey;
      }

      public String getPrizeDesc() {
            return prizeDesc;
      }

      public void setPrizeDesc(String prizeDesc) {
            this.prizeDesc = prizeDesc;
      }

      public String getIsWaitActivation() {
            return isWaitActivation;
      }

      public void setIsWaitActivation(String isWaitActivation) {
            this.isWaitActivation = isWaitActivation;
      }

      public String getExpireTime() {
            return expireTime;
      }

      public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
      }

      public String getActivationChannel() {
            return activationChannel;
      }

      public void setActivationChannel(String activationChannel) {
            this.activationChannel = activationChannel;
      }

      public String getActivationStatus() {
            return activationStatus;
      }

      public void setActivationStatus(String activationStatus) {
            this.activationStatus = activationStatus;
      }

      public String getActivationTime() {
            return activationTime;
      }

      public void setActivationTime(String activationTime) {
            this.activationTime = activationTime;
      }

      public String getPrizeType() {
            return prizeType;
      }

      public void setPrizeType(String prizeType) {
            this.prizeType = prizeType;
      }

      public String getEarnTime() {
            return earnTime;
      }

      public void setEarnTime(String earnTime) {
            this.earnTime = earnTime;
      }

      public String getEarnMoney() {
            return earnMoney;
      }

      public void setEarnMoney(String earnMoney) {
            this.earnMoney = earnMoney;
      }

      public String getEarnVpoints() {
            return earnVpoints;
      }

      public void setEarnVpoints(String earnVpoints) {
            this.earnVpoints = earnVpoints;
      }

      public String getPrizeInfoKey() {
            return prizeInfoKey;
      }

      public void setPrizeInfoKey(String prizeInfoKey) {
            this.prizeInfoKey = prizeInfoKey;
      }

      public String getPrizeRecordKey() {
            return prizeRecordKey;
      }

      public void setPrizeRecordKey(String prizeRecordKey) {
            this.prizeRecordKey = prizeRecordKey;
      }

      public String getPrizeName() {
            return prizeName;
      }

      public void setPrizeName(String prizeName) {
            this.prizeName = prizeName;
      }

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
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

      public String getTestStatus() {
            return testStatus;
      }

      public void setTestStatus(String testStatus) {
            this.testStatus = testStatus;
      }

      public String getTableNameIdx() {
            return tableNameIdx;
      }

      public void setTableNameIdx(String tableNameIdx) {
            this.tableNameIdx = tableNameIdx;
      }

      public String getResidueTime() {
            return residueTime;
      }

      public void setResidueTime(String residueTime) {
            this.residueTime = residueTime;
      }

      public String getTimeUnit() {
            return timeUnit;
      }

      public void setTimeUnit(String timeUnit) {
            this.timeUnit = timeUnit;
      }

      public String getIsExpire() {
            return isExpire;
      }

      public void setIsExpire(String isExpire) {
            this.isExpire = isExpire;
      }
}
