package com.dbt.test.express.param;

import com.alibaba.fastjson.JSONArray;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class PreCreateOrderParam implements Serializable {
    /**
     * api开发者ID
     * 必填
     */
    private long dev_id;
    /**
     * 店铺ID
     * 必填
     */
    private String shop_id;
    /**
     * 店铺ID类型	1:顺丰店铺ID 2:接入方店铺ID
     * 非必填
     */
    private int shop_type;
    /**
     * 用户地址经度
     * 必填
     */
    private String user_lng;
    /**
     * 用户地址纬度
     * 必填
     */
    private String user_lat;
    /**
     * 用户详细地址
     * 必填
     */
    private String user_address;
    /**
     * 发单城市	用来校验是否跨城；请填写城市的中文名称，如北京市、深圳市
     * 非必填
     */
    private String city_name;
    /**
     * 物品重量（单位：克）
     * 必填
     */
    private long weight;
    /**
     * 物品类型	枚举值见下面定义
     * https://openic.sf-express.com/open/api/docs/index#/apidoc
     * @see ProductType
     * 必填
     */
    private int product_type;

    /**
     * 坐标类型，1：百度坐标，2：高德坐标	 默认为高德坐标系
     * 非必填
     */
    private int lbs_type=1;

    /**
     * 推单时间	秒级时间戳
     * 必填
     */
    private long push_time = System.currentTimeMillis() / 1000;


    public PreCreateOrderParam(){

    }

    public PreCreateOrderParam( long dev_id,String shop_id,int shop_type, String user_lng, String user_lat, String user_address,String city_name,long weight, int lbs_type,int product_type) {
        this.dev_id = dev_id;
        this.shop_id = shop_id;
        this.shop_type = shop_type;
        this.user_lng = user_lng;
        this.user_lat = user_lat;
        this.user_address = user_address;
        this.city_name = city_name;
        this.lbs_type = lbs_type;
        this.weight = weight;
        this.product_type = product_type;
    }
}
