package com.dbt.platform.fission.bean;


import lombok.Data;

/**
 * 分享者获取激励配置
 */
@Data
public class VpsVcodeActivateSharePrizeCog {

    private String infoKey;
    /**
     *分享裂变活动key
     */
    private String activateActivityKey;
    /**
     *奖品序号
     */
    private int prizeNo;
    /**
     * 奖品类型 0现金 1积分 2实物奖
     */
    private String prizeType;
    /**
     * 奖品领取用户类型 0新用户 1老用户
     */
    private String prizeUserType;
    /**
     * 奖品区间最小金额
     */
    private double minMoney;
    /**
     * 奖品区间最大金额
     */
    private double maxMoney;
    /**
     * 中出占比
     */
    private int prizePercent;
    private String deleteFlag;
    private String createTime;
}
