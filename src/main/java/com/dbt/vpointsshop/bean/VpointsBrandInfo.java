package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 品牌分类
 */
public class VpointsBrandInfo extends BasicProperties {

    private static final long serialVersionUID = -1439066841103580446L;
    
    private String brandId;
    private String brandName;
    private String brandParent;
    
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    public String getBrandParent() {
        return brandParent;
    }
    public void setBrandParent(String brandParent) {
        this.brandParent = brandParent;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
