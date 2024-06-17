package com.dbt.platform.waitActivation.bean;

public class VpsWaitActivationPrizeSku {

    // 主键
    private String infoKey;

    // 奖项主键
    private String prizeKey;

    // SKU主键
    private String skuKey;

    // 创建时间
    private String createTime;

    // 创建人
    private String createUser;


    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getPrizeKey() {
        return prizeKey;
    }

    public void setPrizeKey(String prizeKey) {
        this.prizeKey = prizeKey;
    }

    public String getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}