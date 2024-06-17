package com.dbt.vpointsshop.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class VpointsGoodsExchangeActivityRecordVO implements Serializable {

    private String infoKey;
    private String activityInfoKey;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 订单号
     */
    private String exchangeId;

    /**
     * 商品价格
     */
    private Long goodsPrice;

    /**
     * 换购商品名称
     */
    private String exchangeGoodsName;

    /**
     * 换购商品价格
     */
    private Long exchangeGoodsPrice;

    /**
     * 换购订单号
     */
    private String exchangeOrderId;
    private String exchangePay;
    private Long startExchangePay;
    private Long endExchangePay;
    private String exchangeStartTime;
    private String exchangeEndTime;
    private VpointsExchangeLog exchangeLog;
    private VpointsExchangeLog exchangeOrderLog;


    public VpointsGoodsExchangeActivityRecordVO(String queryParam) {
        BigDecimal hundred = new BigDecimal("100");
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.exchangeId = paramAry.length > 0 ? paramAry[0] : "";
        this.goodsName = paramAry.length > 1 ? paramAry[1] : "";
        this.startExchangePay = paramAry.length > 2 && StringUtils.isNotEmpty(paramAry[2])  ? new BigDecimal(paramAry[2]).multiply(hundred).longValue() : null;
        this.endExchangePay = paramAry.length > 3 && StringUtils.isNotEmpty(paramAry[3])  ?new BigDecimal(paramAry[3]).multiply(hundred).longValue() : null;
        this.exchangeStartTime = paramAry.length > 4 ? paramAry[4] : "";
        this.exchangeEndTime = paramAry.length > 5 ? paramAry[5] : "";
        this.exchangeGoodsName = paramAry.length > 6 ? paramAry[6] : "";

    }
}
