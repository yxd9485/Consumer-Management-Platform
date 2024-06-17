package com.dbt.vpointsshop.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 礼品卡卡号订单表
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vps_gift_card_qrcode_order_info")
public class VpsGiftCardQrcodeOrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
      @TableId("qrcode_order_key")
    private String qrcodeOrderKey;

    /**
     * 礼品卡卡号码源订单名称
     */
    @TableField("order_name")
    private String orderName;

    /**
     * 绑定的礼品卡主键
     */
    @TableField("gift_card_info_key")
    private String giftCardInfoKey;

    /**
     * 卡号个数
     */
    @TableField("qrcode_num")
    private Integer qrcodeNum;

    /**
     * 订单状态：0未生成，1生成成功，2生成失败，3进行中
     */
    @TableField("order_status")
    private String orderStatus;

    /**
     * 激活状态：0 未激活 1 已激活
     */
    @TableField("activate_status")
    private String activateStatus;

    /**
     * 激活时间
     */
    @TableField("activate_time")
    private Date activateTime;

    /**
     * 激活人
     */
    @TableField("activate_user")
    private String activateUser;

    /**
     * 激活人手机号
     */
    @TableField("activate_phone")
    private String activatePhone;
    /**
     * 制卡人
     */
    @TableField("card_maker")
    private String cardMaker;
    /**
     * 制卡人手机号
     */
    @TableField("card_maker_phone")
    private String cardMakerPhone;
    /**
     * 有效期：开始时间
     */
    @TableField("start_date")
    private String startDate;
    /**
     * 有效期：结束时间
     */
    @TableField("end_date")
    private String endDate;
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    /**
     * 0未删除 1 已删除
     */
    @TableField("delete_flag")
    private String deleteFlag;

    @TableField("create_user")
    private String createUser;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_user")
    private String updateUser;

    @TableField("update_time")
    private Date updateTime;
    @TableField(select = false)
    private String giftCardName;
    /** 抄送 */
    @TableField(select = false)
    private String csEmail;
    @TableField(select = false)
    private String passwordEmail;

}
