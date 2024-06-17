package com.dbt.vpointsshop.bean;

import java.math.BigDecimal;
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
 * 
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vpoints_exchange_gift_card_record")
public class VpointsExchangeGiftCardRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值卡订单id
     */
      @TableId("gift_card_id")
    private String giftCardId;

    /**
     * 礼品卡活动主键
     */
    @TableField("gift_card_info_key")
    private String giftCardInfoKey;

    /**
     * 礼品卡名称
     */
    @TableField("gift_card_name")
    private String giftCardName;

    /**
     * 礼品卡面额(总额度)
     */
    @TableField("account_money")
    private BigDecimal accountMoney;

    /**
     * 礼品卡支付金额
     */
    @TableField("exchange_pay")
    private BigDecimal exchangePay;

    /**
     * 已使用额度
     */
    @TableField("use_money")
    private BigDecimal useMoney;

    /**
     * 剩余额度
     */
    @TableField("surplus_money")
    private BigDecimal surplusMoney;

    /**
     * 冻结额度
     */
    @TableField("freeze_money")
    private BigDecimal freezeMoney;

    /**
     * 订单时间
     */
    @TableField("order_time")
    private Date orderTime;

    /**
     * 付款人手机号
     */
    @TableField("user_phone")
    private String userPhone;

    /**
     * 付款用户
     */
    @TableField("user_key")
    private String userKey;

    /**
     * 微信流水号
     */
    @TableField("transaction_id")
    private Integer transactionId;

    /**
     * 商户交易记录单号
     */
    @TableField("trade_no")
    private Integer tradeNo;

    @TableField("create_time")
    private Date createTime;

    @TableField("create_user")
    private String createUser;

    @TableField("update_time")
    private Date updateTime;

    @TableField("update_user")
    private String updateUser;


    public static final String GIFT_CARD_ID = "gift_card_id";

    public static final String GIFT_CARD_INFO_KEY = "gift_card_info_key";

    public static final String GIFT_CARD_NAME = "gift_card_name";

    public static final String ACCOUNT_MONEY = "account_money";

    public static final String EXCHANGE_PAY = "exchange_pay";

    public static final String USE_MONEY = "use_money";

    public static final String SURPLUS_MONEY = "surplus_money";

    public static final String FREEZE_MONEY = "freeze_money";

    public static final String ORDER_TIME = "order_time";

    public static final String USER_PHONE = "user_phone";

    public static final String USER_KEY = "user_key";

    public static final String TRANSACTION_ID = "transaction_id";

    public static final String TRADE_NO = "trade_no";

    public static final String CREATE_TIME = "create_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_TIME = "update_time";

    public static final String UPDATE_USER = "update_user";

}
