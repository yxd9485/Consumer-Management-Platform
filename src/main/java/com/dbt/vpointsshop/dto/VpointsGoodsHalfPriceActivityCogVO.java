package com.dbt.vpointsshop.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dbt.framework.base.bean.BasicProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商城第二件半价活动
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VpointsGoodsHalfPriceActivityCogVO extends BasicProperties {

    /**
     * 主键
     */
    private String infoKey;

    /**
     * 活动名称
     */
    private String infoName;

    /**
     * 活动编号
     */
    private String infoNo;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 用户购买限制
     */
    private String consumerBuyLimit;
    /**
     * 折扣
     */
    private String discount;
    private List<String> goodsIdList;
    private String status;
    private String isBegin;
    private String goodsName;
    private String exchangeId;
    private String startRealPay;
    private String endRealPay;
    private String goodsId;

}
