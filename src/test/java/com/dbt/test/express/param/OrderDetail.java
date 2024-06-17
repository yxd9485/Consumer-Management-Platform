package com.dbt.test.express.param;

import lombok.Data;

import java.util.List;

/**
 * 订单详情
 */
@Data
public class OrderDetail {
    /**
     * 用户订单商品总金额（单位：分）	100 表示1元（最大值为100万, 超过此值则按100万计算）
     * 必填
     */
    private long total_price;
    /**
     * 物品类型	枚举值见下面定义
     * https://openic.sf-express.com/open/api/docs/index#/apidoc
     * 47:烟酒行
     * 必填
     */
    private long product_type;
    /**
     * 用户实付商家金额（单位：分）	100 表示1元
     * 非必填
     */
    private long user_money;
    /**
     * 商家实收用户金额（单位：分）	100 表示1元
     * 非必填
     */
    private long shop_money;
    /**
     * 物品重量（单位：克）	100 表示100g
     * 必填
     */
    private long weight_gram;
    /**
     * 物品体积（单位：升）	1 表示1升
     * 必填
     */
    private long volume_litre;
    /**
     * 商家收取用户的配送费（单位：分）	100 表示1元
     * 必填
     */
    private long delivery_money;
    /**
     * 物品个数
     * 必填
     */
    private long product_num;
    /**
     * 物品种类个数
     * 必填
     */
    private long product_type_num;
    /**
     * 物品详情；数组结构，详见product_detail结构
     * 必填
     */
    private List<ProductDetail> product_detail;
}
