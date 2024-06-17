package com.dbt.vpointsshop.dto;

import com.dbt.framework.base.bean.BasicProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class VpointsGoodsExchangeActivityCogVO extends BasicProperties {

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
     * 活动商品
     */
    private String activityGoodsId;
    /**
     * 用户购买限制
     */
    private String consumerBuyLimit;
    /**
     * 换购商品id集合
     */
    private List<String> goodsIdList;
    private String status;
    /**
     * 换购商品价格集合
     */
    private List<String> exchangePriceList;
    private String isBegin;

}
