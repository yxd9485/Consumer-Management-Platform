package com.dbt.platform.redenveloperain.bean;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * vps_red_envelope_rain_activity_record
 */
@TableName("vps_red_envelope_rain_activity_record")
public class RedEnvelopeRainActivityRecord implements Serializable {
    /**
     * 主键
     */
    @TableId("info_key")
    private String infoKey;
    /**
     * 红包雨活动主键
     */
    private String activityKey;
    /**
     * 关联活动
     */
    private String vcodeActivityKey;
    /**
     * 中出用户
     */
    private String userKey;
    /**
     * 规则中出金额
     */
    private String earnMoney;
    /**
     * 实际点击红包回传金额
     */
    private String realMoney;
    /**
     * 0 红包雨活动， 1 秒杀活动
     */
    private String activityType;
    /**
     * 扫码中奖的二维码
     */
    private String prizeVcode;
    /**
     * 0 未回传 1、已回传
     */
    private String backFlag;
    private String createTime;
    private String updateTime;

    public String getPrizeVcode() {
        return prizeVcode;
    }

    public void setPrizeVcode(String prizeVcode) {
        this.prizeVcode = prizeVcode;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

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

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getEarnMoney() {
        return earnMoney;
    }

    public void setEarnMoney(String earnMoney) {
        this.earnMoney = earnMoney;
    }

    public String getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(String realMoney) {
        this.realMoney = realMoney;
    }

    public String getBackFlag() {
        return backFlag;
    }

    public void setBackFlag(String backFlag) {
        this.backFlag = backFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
