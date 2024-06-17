package com.dbt.vpointsshop.bean;

public class VpointsGoodsGiftRelation {
    private String  relationId;
    private String  giftId;
    private String  goodsId;
    private String  giftLimitType;
    private String  giftLimitCondition;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGiftLimitType() {
        return giftLimitType;
    }

    public void setGiftLimitType(String giftLimitType) {
        this.giftLimitType = giftLimitType;
    }

    public String getGiftLimitCondition() {
        return giftLimitCondition;
    }

    public void setGiftLimitCondition(String giftLimitCondition) {
        this.giftLimitCondition = giftLimitCondition;
    }
}
