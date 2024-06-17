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
 * 商城商品活动记录表
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vpoints_goods_exchange_activity_record")
public class VpointsGoodsExchangeActivityRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("info_key")
    private String infoKey;

    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    /**
     * 订单号
     */
    @TableField("exchange_id")
    private String exchangeId;

    /**
     * 商品价格
     */
    @TableField("goods_price")
    private Long goodsPrice;

    /**
     * 换购商品名称
     */
    @TableField("exchange_goods_name")
    private String exchangeGoodsName;

    /**
     * 换购商品价格
     */
    @TableField("exchange_goods_price")
    private Long exchangeGoodsPrice;

    /**
     * 换购订单号
     */
    @TableField("exchange_order_id")
    private String exchangeOrderId;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableField("create_user")
    private String createUser;

    @TableField("update_user")
    private String updateUser;

}
