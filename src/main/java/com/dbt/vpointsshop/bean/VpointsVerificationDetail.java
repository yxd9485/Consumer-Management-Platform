package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 积分商场实物奖兑换核销明细表
 */
public class VpointsVerificationDetail extends BasicProperties {
    private static final long serialVersionUID = -7454968186084229655L;
    
    /** 主键*/
    private String infoKey;
    /** 核销编号*/
    private String verificationId;
    /** 兑换订单主键*/
    private String exchangeId;
    /** 品牌名称*/
    private String brandName;
    /** 商品名称*/
    private String goodsName;
    /** 客户自己义商品编号*/
    private String goodsClientNo;
    /** 核销数量*/
    private Integer exchangeNum;
    /** 单品积分*/
    private Long unitVpoints;
    /** 兑换积分*/
    private Long exchangeVpoints;
    /** 商品单价*/
    private Double unitMoney;
    /** 商品总价*/
    private Double totalMoney;
    /** 兑换人*/
    private String nickName;
    /** 收件人*/
    private String userName;
    /** 电话*/
    private String phoneNum;
    /** 地址*/
    private String address;
    /** 订单时间*/
    private String exchangeTime;
    /** 物流公司*/
    private String expressCompany;
    /** 物流编号*/
    private String expressNumber;
    /** 发货时间*/
    private String expressSendTime;
    /** 核销省区*/
    private String verificationProvince;
    private String index;
    /** 生成预览核销列表时间 **/
    private String verificationEndDate;
    /** 品牌id数组 **/
    private String brandKeys;
    
    public VpointsVerificationDetail() {
        
    }
    
    public VpointsVerificationDetail(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.verificationId = paramAry.length > 0 ? paramAry[0] : "";
    }

    public String getBrandKeys() {
		return brandKeys;
	}

	public void setBrandKeys(String brandKeys) {
		this.brandKeys = brandKeys;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getVerificationEndDate() {
		return verificationEndDate;
	}

	public void setVerificationEndDate(String verificationEndDate) {
		this.verificationEndDate = verificationEndDate;
	}

	public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsClientNo() {
        return goodsClientNo;
    }

    public void setGoodsClientNo(String goodsClientNo) {
        this.goodsClientNo = goodsClientNo;
    }

    public Integer getExchangeNum() {
        return exchangeNum;
    }

    public void setExchangeNum(Integer exchangeNum) {
        this.exchangeNum = exchangeNum;
    }

    public Long getUnitVpoints() {
        return unitVpoints;
    }

    public void setUnitVpoints(Long unitVpoints) {
        this.unitVpoints = unitVpoints;
    }

    public Long getExchangeVpoints() {
        return exchangeVpoints;
    }

    public void setExchangeVpoints(Long exchangeVpoints) {
        this.exchangeVpoints = exchangeVpoints;
    }

    public Double getUnitMoney() {
        return unitMoney;
    }

    public void setUnitMoney(Double unitMoney) {
        this.unitMoney = unitMoney;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        this.exchangeTime = exchangeTime;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getExpressSendTime() {
        return expressSendTime;
    }

    public void setExpressSendTime(String expressSendTime) {
        this.expressSendTime = expressSendTime;
    }

    public String getVerificationProvince() {
        return verificationProvince;
    }

    public void setVerificationProvince(String verificationProvince) {
        this.verificationProvince = verificationProvince;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
