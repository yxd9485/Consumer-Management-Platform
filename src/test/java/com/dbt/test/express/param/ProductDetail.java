package com.dbt.test.express.param;

import lombok.Data;

@Data
public class ProductDetail {
    /**
     * 物品名称
     */
    private String product_name;

    /**
     * 物品ID
     */
    private long product_id;

    /**
     * 物品数量
     */
    private long product_num;

    /**
     * 物品价格
     */
    private long product_price;

    /**
     * 物品单位
     */
    private String product_unit;

    /**
     * 备注
     */
    private String product_remark;

    /**
     * 详情
     */
    private String item_detail;

}
