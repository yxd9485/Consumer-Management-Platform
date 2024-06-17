package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 *  商城优惠券配置表Bean
 */
@SuppressWarnings("serial")
public class VpointsCouponCog extends BasicProperties {
    
    /** 优惠券主键*/
    private String couponKey;
    /** 优惠券编号*/
    private String couponNo;
    /** 优惠券名称*/
    private String couponName;
    /** 支付类型0积分、1金额*/
    private String couponPayType;
    /** 优惠券类型0满减券、1直减券、2折扣券*/
    private String couponType;
    /** 满减或折扣券金额限制*/
    private String moneyFullLimit;
    /** 满减或折扣券积分限制*/
    private String vpointsFullLimit;
    /** 满减优惠金额*/
    private String moneyFullReduction;
    /** 满减积分优惠*/
    private String vpointsFullReduction;
    /** 直减金额*/
    private String moneyDirectReduction;
    /** 直减积分*/
    private String vpointsDirectReduction;
    /** 优惠折扣*/
    private String couponDiscount;
    /** 扣减最大金额*/
    private String discountMaxMoney;
    /** 折扣最大积分*/
    private String discountMaxVpoints;
    /** 优惠券适用类型0全品类、1单品类、2指定商品可用、3指定商品不可用*/
    private String couponGoodsType;
    /** 适用商品主键*/
    private String goodsId;
    /** 优惠券发行量*/
    private String couponNum;
    /** 优惠投放渠道:0商城领取*/
    private String couponChannel;
    /** 优惠券领取量*/
    private String couponReceiveNum;
    /** 优惠券领取开始时间*/
    private String receiveStartDate;
    /** 优惠券领取结束时间*/
    private String receiveEndDate;
    /** 优惠券有效开始时间*/
    private String startDate;
    /** 优惠券有效结束时间*/
    private String endDate;
    /** 过期日期类型0指定日期、1有效天数*/
    private String expireDateType;
    /** 使用截止日期*/
    private String expireDateLimit;
    /** 领取后过期有效天数*/
    private String expireDateDays;
    /** 优惠券活动图片全路径*/
    private String couponActivityImgUrl;
    /** 优惠券使用说明*/
    private String couponDesc;
    /** 每人每天限制个数*/
    private String personDayLimit;
    /** 每人每周限制个数*/
    private String personWeekLimit;
    /** 每人每月限制个数*/
    private String personMonthLimit;
    /** 每人限制个数*/
    private String personTotalLimit;
    /** 人群限制类型：默认0不限制，1黑名单不可参与，2指定群组参与，3指定群组不可参与 */
	private String crowdLimitType;
	/** CRM群组ids（使用英文逗号分隔） */
	private String userGroupIds;
    /** 过期提醒提前天数默认0不提醒*/
    private String expireRemindDay;
    /** 指定使用区域*/
    private String areaValidLimit;
    
    //-------------------扩展字段---------------------
    /** 选项卡标识：0待上线，1已上线，2已下线，3全部 **/
    private String tabsFlag;
    private String keyWord;
    private String IsBegin;
    private int couponSurplusNum;
    private String[] goodsIdAry;
    private String[] areaValidLimitAry;
    
    public VpointsCouponCog() {
        super();
    }
    public VpointsCouponCog(String queryParam) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyWord = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
        this.couponType = paramAry.length > 3 ? paramAry[3] : "";
        this.couponGoodsType = paramAry.length > 4 ? paramAry[4] : "";
        this.couponChannel = paramAry.length > 5 ? paramAry[5] : "";
        this.IsBegin = paramAry.length > 6 ? paramAry[6] : "";
    }
    public String getCouponKey() {
        return couponKey;
    }
    public void setCouponKey(String couponKey) {
        this.couponKey = couponKey;
    }
    public String getCouponNo() {
        return couponNo;
    }
    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }
    public String getCouponName() {
        return couponName;
    }
    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
    public String getCouponPayType() {
        return couponPayType;
    }
    public void setCouponPayType(String couponPayType) {
        this.couponPayType = couponPayType;
    }
    public String getCouponType() {
        return couponType;
    }
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
    public String getMoneyFullLimit() {
        return moneyFullLimit;
    }
    public void setMoneyFullLimit(String moneyFullLimit) {
        this.moneyFullLimit = moneyFullLimit;
    }
    public String getVpointsFullLimit() {
        return vpointsFullLimit;
    }
    public void setVpointsFullLimit(String vpointsFullLimit) {
        this.vpointsFullLimit = vpointsFullLimit;
    }
    public String getMoneyFullReduction() {
        return moneyFullReduction;
    }
    public void setMoneyFullReduction(String moneyFullReduction) {
        this.moneyFullReduction = moneyFullReduction;
    }
    public String getVpointsFullReduction() {
        return vpointsFullReduction;
    }
    public void setVpointsFullReduction(String vpointsFullReduction) {
        this.vpointsFullReduction = vpointsFullReduction;
    }
    public String getMoneyDirectReduction() {
        return moneyDirectReduction;
    }
    public void setMoneyDirectReduction(String moneyDirectReduction) {
        this.moneyDirectReduction = moneyDirectReduction;
    }
    public String getVpointsDirectReduction() {
        return vpointsDirectReduction;
    }
    public void setVpointsDirectReduction(String vpointsDirectReduction) {
        this.vpointsDirectReduction = vpointsDirectReduction;
    }
    public String getCouponDiscount() {
        return couponDiscount;
    }
    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }
    public String getDiscountMaxMoney() {
        return discountMaxMoney;
    }
    public void setDiscountMaxMoney(String discountMaxMoney) {
        this.discountMaxMoney = discountMaxMoney;
    }
    public String getDiscountMaxVpoints() {
        return discountMaxVpoints;
    }
    public void setDiscountMaxVpoints(String discountMaxVpoints) {
        this.discountMaxVpoints = discountMaxVpoints;
    }
    public String getCouponGoodsType() {
        return couponGoodsType;
    }
    public void setCouponGoodsType(String couponGoodsType) {
        this.couponGoodsType = couponGoodsType;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getCouponNum() {
        return couponNum;
    }
    public void setCouponNum(String couponNum) {
        this.couponNum = couponNum;
    }
    public String getCouponChannel() {
        return couponChannel;
    }
    public void setCouponChannel(String couponChannel) {
        this.couponChannel = couponChannel;
    }
    public String getCouponReceiveNum() {
        return couponReceiveNum;
    }
    public void setCouponReceiveNum(String couponReceiveNum) {
        this.couponReceiveNum = couponReceiveNum;
    }
    public String getReceiveStartDate() {
        return receiveStartDate;
    }
    public void setReceiveStartDate(String receiveStartDate) {
        this.receiveStartDate = receiveStartDate;
    }
    public String getReceiveEndDate() {
        return receiveEndDate;
    }
    public void setReceiveEndDate(String receiveEndDate) {
        this.receiveEndDate = receiveEndDate;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getExpireDateType() {
        return expireDateType;
    }
    public void setExpireDateType(String expireDateType) {
        this.expireDateType = expireDateType;
    }
    public String getExpireDateLimit() {
        return expireDateLimit;
    }
    public void setExpireDateLimit(String expireDateLimit) {
        this.expireDateLimit = expireDateLimit;
    }
    public String getExpireDateDays() {
        return expireDateDays;
    }
    public void setExpireDateDays(String expireDateDays) {
        this.expireDateDays = expireDateDays;
    }
    public String getCouponActivityImgUrl() {
        return couponActivityImgUrl;
    }
    public void setCouponActivityImgUrl(String couponActivityImgUrl) {
        this.couponActivityImgUrl = couponActivityImgUrl;
    }
    public String getCouponDesc() {
        return couponDesc;
    }
    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }
    public String getPersonDayLimit() {
        return personDayLimit;
    }
    public void setPersonDayLimit(String personDayLimit) {
        this.personDayLimit = personDayLimit;
    }
    public String getPersonWeekLimit() {
        return personWeekLimit;
    }
    public void setPersonWeekLimit(String personWeekLimit) {
        this.personWeekLimit = personWeekLimit;
    }
    public String getPersonMonthLimit() {
        return personMonthLimit;
    }
    public void setPersonMonthLimit(String personMonthLimit) {
        this.personMonthLimit = personMonthLimit;
    }
    public String getPersonTotalLimit() {
        return personTotalLimit;
    }
    public void setPersonTotalLimit(String personTotalLimit) {
        this.personTotalLimit = personTotalLimit;
    }
    public String getCrowdLimitType() {
        return crowdLimitType;
    }
    public void setCrowdLimitType(String crowdLimitType) {
        this.crowdLimitType = crowdLimitType;
    }
    public String getUserGroupIds() {
        return userGroupIds;
    }
    public void setUserGroupIds(String userGroupIds) {
        this.userGroupIds = userGroupIds;
    }
    public String getExpireRemindDay() {
        return expireRemindDay;
    }
    public void setExpireRemindDay(String expireRemindDay) {
        this.expireRemindDay = expireRemindDay;
    }
    public String getTabsFlag() {
        return tabsFlag;
    }
    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }
    public String getKeyWord() {
        return keyWord;
    }
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
    public String getIsBegin() {
        return IsBegin;
    }
    public void setIsBegin(String isBegin) {
        IsBegin = isBegin;
    }
    public int getCouponSurplusNum() {
        return couponSurplusNum;
    }
    public void setCouponSurplusNum(int couponSurplusNum) {
        this.couponSurplusNum = couponSurplusNum;
    }
    public String[] getGoodsIdAry() {
        return goodsIdAry;
    }
    public void setGoodsIdAry(String[] goodsIdAry) {
        this.goodsIdAry = goodsIdAry;
    }
    public String getAreaValidLimit() {
        return areaValidLimit;
    }
    public void setAreaValidLimit(String areaValidLimit) {
        this.areaValidLimit = areaValidLimit;
    }
    public String[] getAreaValidLimitAry() {
        return areaValidLimitAry;
    }
    public void setAreaValidLimitAry(String[] areaValidLimitAry) {
        this.areaValidLimitAry = areaValidLimitAry;
    }
}
