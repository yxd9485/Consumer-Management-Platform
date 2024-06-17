package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 商城优惠券适用商品限制
 */
public class VpointsCouponCogGoodsLimit extends BasicProperties {

    private static final long serialVersionUID = 1L;
    private String infoKey;
    private String couponKey;
    private String goodsId;
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getCouponKey() {
        return couponKey;
    }
    public void setCouponKey(String couponKey) {
        this.couponKey = couponKey;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
