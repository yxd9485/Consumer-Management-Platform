package com.dbt.vpointsshop.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProCreateOrderParam extends PreCreateOrderParam{

    /**
     * 商家订单号	不允许重复(使用相同商家订单号会幂等返回)
     * 必填
     */
    private String shop_order_id;
    /**
     * 订单接入来源	固定可选值：1：美团；2：饿了么；3：饿了么零售；4：口碑；
     * （支持中文字符串值，其他来源商户系统可自定义。如：抖音、滴滴等）
     * 必填
     */
    private String order_source;
    /**
     * 取货序号	与order_source配合使用
     * 如：饿了么10号单，表示如下：
     * order_source=2;order_sequence=10。
     * 用于骑士快速寻找配送物
     * 非必填
     */
    private String order_sequence;
    /**
     * 用户下单时间	秒级时间戳
     * 必填
     */
    private String order_time;

    /**
     * 是否发送取件码、收件码	0不生成，1发送取件码，2发送收件码，3二者都发送 不传则取顺丰侧店铺配置的值
     * 必填
     */
    private Integer verify_code_type=null;
    /**
     *收货人信息	Obj，详见receive结构
     * 必填
     */
    private Receive receive;
    /**
     *  订单详情	Obj，详见order_detail结构
     * 必填
     */
    private OrderDetail order_detail;

    public ProCreateOrderParam() {
    }

    public ProCreateOrderParam(long dev_id, String shop_id, int shop_type, String user_lng,
                               String user_lat, String user_address, String city_name,long weigth,
                               int lbs_type, int product_type) {
        super(dev_id, shop_id, shop_type, user_lng, user_lat, user_address, city_name,weigth, lbs_type, product_type);
    }
}
