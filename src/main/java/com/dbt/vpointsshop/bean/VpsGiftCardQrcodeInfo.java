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

/**
 * <p>
 * 礼品卡卡号表
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vps_gift_card_qrcode_info")
public class VpsGiftCardQrcodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 礼品二维码
     */
      @TableId("gift_card_qrcode")
    private String giftCardQrcode;
    /**
     * 卡号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 码源订单主键
     */
    @TableField("qrcode_order_key")
    private String qrcodeOrderKey;

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
     * 0待激活、1已激活
     */
    @TableField("qrcode_status")
    private String qrcodeStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;


    public static final String GIFT_CARD_QRCODE = "gift_card_qrcode";

    public static final String QRCODE_ORDER_KEY = "qrcode_order_key";

    public static final String ACTIVATE_TIME = "activate_time";

    public static final String ACTIVATE_USER = "activate_user";

    public static final String QRCODE_STATUS = "qrcode_status";

    public static final String CREATE_TIME = "create_time";

}
