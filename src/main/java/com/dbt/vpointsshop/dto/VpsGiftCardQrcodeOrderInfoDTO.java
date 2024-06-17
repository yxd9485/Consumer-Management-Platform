package com.dbt.vpointsshop.dto;

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
 * 礼品卡卡号订单表
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VpsGiftCardQrcodeOrderInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
    private String qrcodeOrderKey;

    /**
     * 礼品卡卡号码源订单名称
     */
    private String orderName;

    /**
     * 绑定的礼品卡主键
     */
    private String giftCardInfoKey;

    /**
     * 卡号个数
     */
    private Integer qrcodeNum;

    /**
     * 订单状态：0未生成，1生成成功，2生成失败，3进行中
     */
    private String orderStatus;

    /**
     * 激活状态：0 未激活 1 已激活
     */
    private String activateStatus;

    /**
     * 激活时间
     */
    private Date activateTime;

    /**
     * 激活人
     */
    private String activateUser;

    /**
     * 激活人手机号
     */
    private String activatePhone;
    /**
     * 制卡人
     */
    private String cardMaker;
    /**
     * 制卡人手机号
     */
    private String cardMakerPhone;
    /**
     * 有效期：开始时间
     */
    private String startDate;
    /**
     * 有效期：结束时间
     */
    private String endDate;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 礼品卡名称
     */
    private String giftCardName;
    /** 抄送邮箱 */
    private String csEmail;
    /** 主密码邮箱 **/
    private String passwordEmail;
    private String passwordEmailName;
    private String password;

}
