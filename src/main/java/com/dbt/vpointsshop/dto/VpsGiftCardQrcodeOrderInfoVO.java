package com.dbt.vpointsshop.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

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
public class VpsGiftCardQrcodeOrderInfoVO implements Serializable {

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
     * 0未删除 1 已删除
     */
    private String deleteFlag;

    private String createUser;

    private String createTime;

    private String updateUser;

    private String updateTime;
    private String createDate;
    private String giftCardName;



    public VpsGiftCardQrcodeOrderInfoVO(){}
    public VpsGiftCardQrcodeOrderInfoVO(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.orderName = paramAry.length > 0 ? paramAry[0] : "";
    }
}
