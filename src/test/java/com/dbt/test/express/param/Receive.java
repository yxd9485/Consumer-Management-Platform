package com.dbt.test.express.param;

import lombok.Data;

/**
 * 收货人信息
 */
@Data
public class Receive {

    /**
     * 姓名
     * 必填
     */
    private String user_name;
    /**
     * 电话
     * 必填
     */
    private String user_phone;
    /**
     * 地址
     * 必填
     */
    private String user_address;
    /**
     * 经度
     * 必填
     */
    private String user_lng;
    /**
     *纬度
     * 必填
     */
    private String user_lat;
    /**
     *收货人信息	Obj，详见receive结构
     * 非必填
     */
    private String city_name;

}
