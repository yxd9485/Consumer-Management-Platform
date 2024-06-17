package com.dbt.vpointsshop.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 兑换日志bean
 * @author zhaohongtao
 *2017年11月16日
 */
/**
 * @author hanshimeng
 *
 */
public class VpointsExchangeLog extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String exchangeId;//兑换ID
	private String exchangeChannel;//兑换渠道(兑换、抽奖、扫码)
	private String exchangeType;//物品类型(实物、电子券、流量等)
	private String companyKey;//省区
	private String categoryType;//商品分类
	private String goodsId;//商品ID
	private String userKey;//用户ID
	private int exchangeNum;//兑换个数
	private String earnMoney;	// 获取金额
	private Integer earnVpoints;// 获取积分
	private long exchangeVpoints;//消耗积分
	private long exchangePay;//支付金额单位分
	private long discountsVpoints;//优惠积分
	private long discountsMoney;//优惠金额（分）
	private long refundPay; // 累计退款金额
	private long refundVpoints; // 累计退款积分
	
	/** 兑换状态：-1=待支付、0=兑换成功、1=兑换失败、2=兑换中、3=订单已关闭、4=微信退款申请、 5=待发货撤单、6=微信退款失败、7=退货审核中、8=退货处理中、9=退货已完成**/
	private String exchangeStatus;
	private String exchangeTime;//兑换时间
	
	private String categoryParent;//商品分类
	private String goodsName;//商品名称
	private String goodsShortName;//商品名称


    private String userRole;//用户校色
    private String mnRealName;//真实姓名
    private String userName;
	private String phoneNum;
	private String province;
	private String city;
	private String county;
	private String address;
	private String customerMessage;
	private String idCard;
	private String exchangeStartTime;
	private String exchangeEndTime;
	private String firstName;
	private String secondName;
	private String companyName;
	private String nickName;
    /** 可查看品牌ID信息*/
    private List<String> brandIdLst;
    /** */
    private String goodsClientNo;
    /** 快递公司*/
    private String expressCompany;
    /** 快递单号*/
    private String expressNumber;
    /** 物流状态：0未发货、1已发货、2已完成*/
    private String expressStatus;
    /** 发货时间*/
    private String expressSendTime;
    /** 发货备注*/
    private String expressSendMessage;
    /** 签收时间*/
    private String expressSignTime;
    /** 品牌编码*/
    private String brandId;
    /** 品牌名称*/
    private String brandName;
    /** 单口积分*/
    private Long unitVpoints;
    private String index;
    private String tabsFlag;
    /** 代采价格单位元*/
    private Double daicaiMoney;
    /** 核销主键*/
    private String verificationId;
    /** 微信支付单号*/
    private String transactionId;
    /** 购买方式：0积分、1金额, 2混合*/
    private String payType;
    /** 购买方式名称*/
    private String payTypeName;
    /** 权益类型*/
    private String quanYiType;
    /** 支付金额单位元（用于导出excel数据）*/
    private String exchangePayMoney;
    /** 累计退款金额单位元（用于导出excel数据）*/
    private String refundPayMoney;
    /** 订单状态*/
    private String orderStatus;
    /** 退货原因*/
    private String goodsReturnReason;
    /** 发起退货日期*/
    private String goodsReturnTime;
    /** 退货审核备注*/
    private String goodsReturnAudit;
    /** 退货物流公司名称*/
    private String goodsReturnExpressCompany;
    /** 退货物流编号*/
    private String goodsReturnExpressNumber;
    /** 优惠券主键 **/
    private String couponKey;
    /** 优惠券领取主键 **/
    private String couponReceiveInfoKey;
    /** 优惠券优惠金额（分） **/
    private long couponDiscountPay;
    private String couponDiscountPayMoney;
    /** 优惠券优惠积分 **/
    private long couponDiscountVpoints;
    private String goodsGro;
    private String exchangeStartDate;
    private String exchangeEndDate;
    private String sendStartDate;
    private String sendEndDate;
    private String signStartDate;
    private String signEndDate;
    private String prizeName;
    private String seckillActivityKey;
    private String groupBuyingActivityKey;
    
    /** 拼团状态：0成功，1失败，2等待成团 **/
    private String groupBuyingStatus;
    
    // Excel金额转换使用
    private double exchangePayForExcel;
    private double discountsMoneyForExcel;
    private double couponDiscountPayForExcel;
    /** 撤单原因*/
    private String revokeOrderReason;
    private String updateTime;
    
    /** 退货评论*/
    private String returnComment;
    /** 评论图片地址*/
    private String returnImageUrl;
    /** 赠品名称*/
    private String giftGoodsName;
    /** 赠品数量*/
    private int giftGoodsNum;
    /** 平台登录用户KEY **/
    private String platformNickName;
    /** 积分商城订单区分地区显示的节点时间 **/
    private String vpointsOrderAreaTime;
    /** 是否礼品卡 0 是 1 否*/
    private String isGiftCard;
    /** 礼品卡兑换订单外键 */
    private String giftCardExchangeId;
    /** 礼品卡状态 0 未领取 1 已领取*/
    private String giftCardStatus;
    /** 礼品卡领取时间*/
    private String giftCardReceiveTime;
    /** 订单类型 0、常规订单，1、秒杀订单，2、拼团订单，3、买赠订单，4、礼品卡订单，5、组合优惠订单6、折扣订单、7、换购订单*/
    private String orderType;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 退款时间（多个用”,“分割）
     */
    private String returnPayTime;
    /**
     * 退款流水号（多个用”,“分割）
     */
    private String returnTransactionId;
    private String goodsOriginalPrice;
    private String halfPriceActivityInfoKey;
    private String exchangeActivityInfoKey;
    private long startExchangePay;
    private long endExchangePay;
    private String giftCardInfoKey;
    private String giftCardType;
    private String giftCardName;
    private String goodsSpecification;
    private long giftCardPay;
    private String expressOrderFlag;
    private String expressOrderId;
	public VpointsExchangeLog(){}
	public VpointsExchangeLog(String queryParam){
		if (!StringUtils.isEmpty(queryParam)) {
			queryParam = trickParam(queryParam);
			String[] params = queryParam.split(",");
			this.exchangeChannel = params.length > 0 ? !StringUtils.isEmpty(params[0]) ? params[0]: null : null;
			this.exchangeType = params.length > 1 ? !StringUtils.isEmpty(params[1]) ? params[1]: null : null;
			this.categoryParent = params.length > 2 ? !StringUtils.isEmpty(params[2]) ? params[2]: null : null;
			this.categoryType = params.length > 3 ? !StringUtils.isEmpty(params[3]) ? params[3]: null : null;
			this.goodsName = params.length > 4 ? !StringUtils.isEmpty(params[4]) ? params[4]: null : null;
			this.userKey = params.length > 5 ? !StringUtils.isEmpty(params[5]) ? params[5]: null : null;
			this.exchangeId = params.length > 6 ? !StringUtils.isEmpty(params[6]) ? params[6]: null : null;
			this.exchangeStartTime = params.length > 7 ? !StringUtils.isEmpty(params[7]) ? params[7]: null : null;
			this.exchangeEndTime = params.length > 8 ? !StringUtils.isEmpty(params[8]) ? params[8]+" 23:59:59": null : null;
			this.companyKey = params.length > 9 ? !StringUtils.isEmpty(params[9]) ? params[9]: null : null;

		}
	}

    public String getExchangePayMoney() {
        return exchangePayMoney;
    }
    public void setExchangePayMoney(String exchangePayMoney) {
        this.exchangePayMoney = exchangePayMoney;
    }
    public VpointsExchangeLog(String queryParam, String tabsFlag) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.exchangeId = paramAry.length > 0 ? paramAry[0] : "";
        this.brandId = paramAry.length > 1 ? paramAry[1] : "";
        this.goodsId = paramAry.length > 2 ? paramAry[2] : "";
        if(StringUtils.isNotEmpty(this.goodsId)) {
			String[] types=this.goodsId.split(";");
			String goodsGro="";
			for (String type : types) {
				goodsGro+=",'"+type+"'";
			}
			this.goodsId=goodsGro.substring(1);
		}else {
			this.goodsId=null;
		}
        
        // 订单管理查询-未发货
        if ("0".equals(tabsFlag)) {
            this.userName = paramAry.length > 3 ? paramAry[3] : "";
            this.transactionId = paramAry.length > 4 ? paramAry[4] : "";
            this.quanYiType = paramAry.length > 5 ? paramAry[5] : "";
            this.payType = paramAry.length > 6 ? paramAry[6] : "";
            this.exchangeStartDate = paramAry.length > 7 ? paramAry[7] : "";
            this.exchangeEndDate = paramAry.length > 8 ? paramAry[8] : "";
            this.orderType = paramAry.length > 9 ? paramAry[9] : "";

            
        // 订单管理查询-已发货/已完成
        } else if ("1".equals(tabsFlag) || "2".equals(tabsFlag)) {
            this.expressNumber = paramAry.length > 3 ? paramAry[3] : "";
            this.userName = paramAry.length > 4 ? paramAry[4] : "";
            this.transactionId = paramAry.length > 5 ? paramAry[5] : "";
            this.quanYiType = paramAry.length > 6 ? paramAry[6] : "";
            this.payType = paramAry.length > 7 ? paramAry[7] : "";
            this.exchangeStartDate = paramAry.length > 8 ? paramAry[8] : "";
            this.exchangeEndDate = paramAry.length > 9 ? paramAry[9] : "";
            this.sendStartDate = paramAry.length > 10 ? paramAry[10] : "";
            this.sendEndDate = paramAry.length > 11 ? paramAry[11] : "";
            this.signStartDate = paramAry.length > 12 ? paramAry[12] : "";
            this.signEndDate = paramAry.length > 13 ? paramAry[13] : "";
            this.orderType = paramAry.length > 14 ? paramAry[14] : "";
        
        //  订单管理查询-全部订单/退货订单
        } else {
            this.expressNumber = paramAry.length > 3 ? paramAry[3] : "";
            this.userName = paramAry.length > 4 ? paramAry[4] : "";
            this.orderStatus = paramAry.length > 5 ? paramAry[5] : "";
            this.transactionId = paramAry.length > 6 ? paramAry[6] : "";
            this.quanYiType = paramAry.length > 7 ? paramAry[7] : "";
            this.payType = paramAry.length > 8 ? paramAry[8] : "";
            this.exchangeStartDate = paramAry.length > 9 ? paramAry[9] : "";
            this.exchangeEndDate = paramAry.length > 10 ? paramAry[10] : "";
            this.sendStartDate = paramAry.length > 11 ? paramAry[11] : "";
            this.sendEndDate = paramAry.length > 12 ? paramAry[12] : "";
            this.signStartDate = paramAry.length > 13 ? paramAry[13] : "";
            this.signEndDate = paramAry.length > 14 ? paramAry[14] : "";
            this.orderType = paramAry.length > 15 ? paramAry[15] : "";
        }
    }

    public String getGiftCardInfoKey() {
        return giftCardInfoKey;
    }

    public void setGiftCardInfoKey(String giftCardInfoKey) {
        this.giftCardInfoKey = giftCardInfoKey;
    }

    public String getGiftCardName() {
        return giftCardName;
    }

    public void setGiftCardName(String giftCardName) {
        this.giftCardName = giftCardName;
    }

    public long getGiftCardPay() {
        return giftCardPay;
    }

    public void setGiftCardPay(long giftCardPay) {
        this.giftCardPay = giftCardPay;
    }

    public String getGiftCardType() {
        return giftCardType;
    }

    public void setGiftCardType(String giftCardType) {
        this.giftCardType = giftCardType;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public long getExchangePay() {
        return exchangePay;
    }
    public void setExchangePay(long exchangePay) {
        this.exchangePay = exchangePay;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getQuanYiType() {
        return quanYiType;
    }
    public void setQuanYiType(String quanYiType) {
        this.quanYiType = quanYiType;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public String getExpressCompany() {
		return expressCompany;
	}
	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}
	public String getEarnMoney() {
		return earnMoney;
	}
	public void setEarnMoney(String earnMoney) {
		this.earnMoney = earnMoney;
	}
	public Integer getEarnVpoints() {
		return earnVpoints;
	}
	public void setEarnVpoints(Integer earnVpoints) {
		this.earnVpoints = earnVpoints;
	}
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getExchangeChannel() {
		return exchangeChannel;
	}
	public void setExchangeChannel(String exchangeChannel) {
		this.exchangeChannel = exchangeChannel;
	}
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public int getExchangeNum() {
		return exchangeNum;
	}
	public void setExchangeNum(int exchangeNum) {
		this.exchangeNum = exchangeNum;
	}
	public long getExchangeVpoints() {
		return exchangeVpoints;
	}
	public void setExchangeVpoints(long exchangeVpoints) {
		this.exchangeVpoints = exchangeVpoints;
	}
	public String getExchangeStatus() {
		return exchangeStatus;
	}
	public void setExchangeStatus(String exchangeStatus) {
		this.exchangeStatus = exchangeStatus;
	}
	public String getExchangeTime() {
		return exchangeTime;
	}
	public void setExchangeTime(String exchangeTime) {
		this.exchangeTime = exchangeTime;
	}
	public String getCategoryParent() {
		return categoryParent;
	}
	public void setCategoryParent(String categoryParent) {
		this.categoryParent = categoryParent;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsShortName() {
        return goodsShortName;
    }
    public void setGoodsShortName(String goodsShortName) {
        this.goodsShortName = goodsShortName;
    }
    public String getExchangeStartTime() {
		return exchangeStartTime;
	}
	public void setExchangeStartTime(String exchangeStartTime) {
		this.exchangeStartTime = exchangeStartTime;
	}
	public String getExchangeEndTime() {
		return exchangeEndTime;
	}
	public void setExchangeEndTime(String exchangeEndTime) {
		this.exchangeEndTime = exchangeEndTime;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getMnRealName() {
        return mnRealName;
    }
    public void setMnRealName(String mnRealName) {
        this.mnRealName = mnRealName;
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
	public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCounty() {
        return county;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCustomerMessage() {
        return customerMessage;
    }
    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }
    public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
    public String getExpressNumber() {
        return expressNumber;
    }
    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }
    public String getExpressStatus() {
        return expressStatus;
    }
    public void setExpressStatus(String expressStatus) {
        this.expressStatus = expressStatus;
    }
    public String getExpressSendTime() {
        return expressSendTime;
    }
    public void setExpressSendTime(String expressSendTime) {
        this.expressSendTime = expressSendTime;
    }
    public String getExpressSendMessage() {
        return expressSendMessage;
    }
    public void setExpressSendMessage(String expressSendMessage) {
        this.expressSendMessage = expressSendMessage;
    }
    public String getExpressSignTime() {
        return expressSignTime;
    }
    public void setExpressSignTime(String expressSignTime) {
        this.expressSignTime = expressSignTime;
    }
    public String getTabsFlag() {
        return tabsFlag;
    }
    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
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
    public List<String> getBrandIdLst() {
        return brandIdLst;
    }
    public void setBrandIdLst(List<String> brandIdLst) {
        this.brandIdLst = brandIdLst;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public String getGoodsClientNo() {
        return goodsClientNo;
    }
    public void setGoodsClientNo(String goodsClientNo) {
        this.goodsClientNo = goodsClientNo;
    }
    public Long getUnitVpoints() {
        return unitVpoints;
    }
    public void setUnitVpoints(Long unitVpoints) {
        this.unitVpoints = unitVpoints;
    }
    public Double getDaicaiMoney() {
        return daicaiMoney;
    }
    public void setDaicaiMoney(Double daicaiMoney) {
        this.daicaiMoney = daicaiMoney;
    }
    public String getVerificationId() {
        return verificationId;
    }
    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }
    public String getGoodsReturnReason() {
        return goodsReturnReason;
    }
    public void setGoodsReturnReason(String goodsReturnReason) {
        this.goodsReturnReason = goodsReturnReason;
    }
    public String getGoodsReturnTime() {
        return goodsReturnTime;
    }
    public void setGoodsReturnTime(String goodsReturnTime) {
        this.goodsReturnTime = goodsReturnTime;
    }
    public String getGoodsReturnAudit() {
        return goodsReturnAudit;
    }
    public void setGoodsReturnAudit(String goodsReturnAudit) {
        this.goodsReturnAudit = goodsReturnAudit;
    }
    public String getGoodsReturnExpressCompany() {
        return goodsReturnExpressCompany;
    }
    public void setGoodsReturnExpressCompany(String goodsReturnExpressCompany) {
        this.goodsReturnExpressCompany = goodsReturnExpressCompany;
    }
    public String getGoodsReturnExpressNumber() {
        return goodsReturnExpressNumber;
    }
    public void setGoodsReturnExpressNumber(String goodsReturnExpressNumber) {
        this.goodsReturnExpressNumber = goodsReturnExpressNumber;
    }
    public String getExchangeStartDate() {
        return exchangeStartDate;
    }
    public void setExchangeStartDate(String exchangeStartDate) {
        this.exchangeStartDate = exchangeStartDate;
    }
    public String getExchangeEndDate() {
        return exchangeEndDate;
    }
    public void setExchangeEndDate(String exchangeEndDate) {
        this.exchangeEndDate = exchangeEndDate;
    }
    public String getSendStartDate() {
        return sendStartDate;
    }
    public void setSendStartDate(String sendStartDate) {
        this.sendStartDate = sendStartDate;
    }
    public String getSendEndDate() {
        return sendEndDate;
    }
    public void setSendEndDate(String sendEndDate) {
        this.sendEndDate = sendEndDate;
    }
    public String getSignStartDate() {
        return signStartDate;
    }
    public void setSignStartDate(String signStartDate) {
        this.signStartDate = signStartDate;
    }
    public String getSignEndDate() {
        return signEndDate;
    }
    public void setSignEndDate(String signEndDate) {
        this.signEndDate = signEndDate;
    }
	public long getDiscountsVpoints() {
		return discountsVpoints;
	}
	public void setDiscountsVpoints(long discountsVpoints) {
		this.discountsVpoints = discountsVpoints;
	}
	public long getDiscountsMoney() {
		return discountsMoney;
	}
	public void setDiscountsMoney(long discountsMoney) {
		this.discountsMoney = discountsMoney;
	}
	public String getSeckillActivityKey() {
		return seckillActivityKey;
	}
	public void setSeckillActivityKey(String seckillActivityKey) {
		this.seckillActivityKey = seckillActivityKey;
	}
	public String getGroupBuyingActivityKey() {
		return groupBuyingActivityKey;
	}
	public void setGroupBuyingActivityKey(String groupBuyingActivityKey) {
		this.groupBuyingActivityKey = groupBuyingActivityKey;
	}
	public String getCouponKey() {
		return couponKey;
	}
	public void setCouponKey(String couponKey) {
		this.couponKey = couponKey;
	}
	public String getCouponReceiveInfoKey() {
		return couponReceiveInfoKey;
	}
	public void setCouponReceiveInfoKey(String couponReceiveInfoKey) {
		this.couponReceiveInfoKey = couponReceiveInfoKey;
	}
	public String getGroupBuyingStatus() {
		return groupBuyingStatus;
	}
	public void setGroupBuyingStatus(String groupBuyingStatus) {
		this.groupBuyingStatus = groupBuyingStatus;
	}
	public long getCouponDiscountPay() {
		return couponDiscountPay;
	}
	public void setCouponDiscountPay(long couponDiscountPay) {
		this.couponDiscountPay = couponDiscountPay;
	}
	public long getCouponDiscountVpoints() {
		return couponDiscountVpoints;
	}
	public void setCouponDiscountVpoints(long couponDiscountVpoints) {
		this.couponDiscountVpoints = couponDiscountVpoints;
	}
    public String getCouponDiscountPayMoney() {
        return couponDiscountPayMoney;
    }
    public void setCouponDiscountPayMoney(String couponDiscountPayMoney) {
        this.couponDiscountPayMoney = couponDiscountPayMoney;
    }
    public String getGoodsGro() {
        return goodsGro;
    }
    public void setGoodsGro(String goodsGro) {
        this.goodsGro = goodsGro;
    }
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public double getCouponDiscountPayForExcel() {
		return couponDiscountPayForExcel;
	}
	public void setCouponDiscountPayForExcel(double couponDiscountPayForExcel) {
		this.couponDiscountPayForExcel = couponDiscountPayForExcel;
	}
	public double getExchangePayForExcel() {
		return exchangePayForExcel;
	}
	public void setExchangePayForExcel(double exchangePayForExcel) {
		this.exchangePayForExcel = exchangePayForExcel;
	}
	public double getDiscountsMoneyForExcel() {
		return discountsMoneyForExcel;
	}
	public void setDiscountsMoneyForExcel(double discountsMoneyForExcel) {
		this.discountsMoneyForExcel = discountsMoneyForExcel;
	}
    public String getRevokeOrderReason() {
        return revokeOrderReason;
    }
    public void setRevokeOrderReason(String revokeOrderReason) {
        this.revokeOrderReason = revokeOrderReason;
    }
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
    public long getRefundPay() {
        return refundPay;
    }
    public void setRefundPay(long refundPay) {
        this.refundPay = refundPay;
    }
    public long getRefundVpoints() {
        return refundVpoints;
    }
    public void setRefundVpoints(long refundVpoints) {
        this.refundVpoints = refundVpoints;
    }
    public String getRefundPayMoney() {
        return refundPayMoney;
    }
    public void setRefundPayMoney(String refundPayMoney) {
        this.refundPayMoney = refundPayMoney;
    }
	public String getReturnComment() {
		return returnComment;
	}
	public void setReturnComment(String returnComment) {
		this.returnComment = returnComment;
	}
	public String getReturnImageUrl() {
		return returnImageUrl;
	}
	public void setReturnImageUrl(String returnImageUrl) {
		this.returnImageUrl = returnImageUrl;
	}
    public String getGiftGoodsName() {
        return giftGoodsName;
    }
    public void setGiftGoodsName(String giftGoodsName) {
        this.giftGoodsName = giftGoodsName;
    }
    public int getGiftGoodsNum() {
        return giftGoodsNum;
    }
    public void setGiftGoodsNum(int giftGoodsNum) {
        this.giftGoodsNum = giftGoodsNum;
    }
	public String getPlatformNickName() {
		return platformNickName;
	}
	public void setPlatformNickName(String platformNickName) {
		this.platformNickName = platformNickName;
	}
	public String getVpointsOrderAreaTime() {
		return vpointsOrderAreaTime;
	}
	public void setVpointsOrderAreaTime(String vpointsOrderAreaTime) {
		this.vpointsOrderAreaTime = vpointsOrderAreaTime;
	}

    public String getIsGiftCard() {
        return isGiftCard;
    }

    public void setIsGiftCard(String isGiftCard) {
        this.isGiftCard = isGiftCard;
    }

    public String getGiftCardExchangeId() {
        return giftCardExchangeId;
    }

    public void setGiftCardExchangeId(String giftCardExchangeId) {
        this.giftCardExchangeId = giftCardExchangeId;
    }

    public String getGiftCardStatus() {
        return giftCardStatus;
    }

    public void setGiftCardStatus(String giftCardStatus) {
        this.giftCardStatus = giftCardStatus;
    }

    public String getGiftCardReceiveTime() {
        return giftCardReceiveTime;
    }

    public void setGiftCardReceiveTime(String giftCardReceiveTime) {
        this.giftCardReceiveTime = giftCardReceiveTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getReturnPayTime() {
        return returnPayTime;
    }

    public void setReturnPayTime(String returnPayTime) {
        this.returnPayTime = returnPayTime;
    }

    public String getReturnTransactionId() {
        return returnTransactionId;
    }

    public void setReturnTransactionId(String returnTransactionId) {
        this.returnTransactionId = returnTransactionId;
    }

    public String getGoodsOriginalPrice() {
        return goodsOriginalPrice;
    }

    public void setGoodsOriginalPrice(String goodsOriginalPrice) {
        this.goodsOriginalPrice = goodsOriginalPrice;
    }

    public String getHalfPriceActivityInfoKey() {
        return halfPriceActivityInfoKey;
    }

    public void setHalfPriceActivityInfoKey(String halfPriceActivityInfoKey) {
        this.halfPriceActivityInfoKey = halfPriceActivityInfoKey;
    }

    public String getExchangeActivityInfoKey() {
        return exchangeActivityInfoKey;
    }

    public void setExchangeActivityInfoKey(String exchangeActivityInfoKey) {
        this.exchangeActivityInfoKey = exchangeActivityInfoKey;
    }

    public long getStartExchangePay() {
        return startExchangePay;
    }

    public void setStartExchangePay(long startExchangePay) {
        this.startExchangePay = startExchangePay;
    }

    public long getEndExchangePay() {
        return endExchangePay;
    }

    public void setEndExchangePay(long endExchangePay) {
        this.endExchangePay = endExchangePay;
    }

    public String getGoodsSpecification() {
        return goodsSpecification;
    }

    public void setGoodsSpecification(String goodsSpecification) {
        this.goodsSpecification = goodsSpecification;
    }

    public String getExpressOrderFlag() {
        return expressOrderFlag;
    }

    public void setExpressOrderFlag(String expressOrderFlag) {
        this.expressOrderFlag = expressOrderFlag;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }
}
