package com.dbt.platform.fission.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuda
 * @since 2022-02-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vps_vcode_activate_share_record")
public class VpsVcodeActivateShareRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;

    /**
     * 二维码串码
     */
    @TableField("qrcode_content")
    private String qrcodeContent;

    /**
     * 规则key
     */
    @TableField("rule_key")
    private String ruleKey;

    /**
     * 红包领取时间
     */
    @TableField("receive_time")
    private Date receiveTime;

    /**
     * 分享用户的key
     */
    @TableField("share_user_key")
    private String shareUserKey;

    /**
     * 红包领取个数
     */
    @TableField("receive_num")
    private Integer receiveNum;

    /**
     * 激活红包总金额
     */
    @TableField("activation_amount_total")
    private Double activationAmountTotal;
    /**
     * 激活红包总金额
     */
    @TableField("activation_point_total")
    private Integer activationPointTotal;

    /**
     * 奖励红包总金额
     */
    @TableField("reward_amount_total")
    private Double rewardAmountTotal;

    /**
     * 奖励红包总积分
     */
    @TableField("reward_point_total")
    private Integer rewardPointTotal;
    @TableField("get_num")
    private Integer getNum;

    /**
     * 激活用户的key集合
     */
    @TableField(select = false)
    private String activationUserKeyList;
    @TableField("create_time")
    private Date createTime;
    @TableField("create_user")
    private String createUser;
    @TableField("update_time")
    private Date updateTime;
    @TableField("update_user")
    private String updateUser;


}
