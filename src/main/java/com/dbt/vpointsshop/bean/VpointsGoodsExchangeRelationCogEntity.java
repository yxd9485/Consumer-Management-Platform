package com.dbt.vpointsshop.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 第二件半价活动和商品关联表
 * </p>
 *
 * @author wangshuda
 * @since 2022-07-11
 */
@Data
public class VpointsGoodsExchangeRelationCogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String goodsId;

    private String exchangeActivityInfoKey;

    private String exchangePrice;


}
