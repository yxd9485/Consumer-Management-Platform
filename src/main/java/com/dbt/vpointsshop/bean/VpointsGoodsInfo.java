package com.dbt.vpointsshop.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * 积分商品bean
 * @author zhaohongtao
 *2017年11月16日
 */
public class VpointsGoodsInfo extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String goodsId;//商品ID
	private String companyKey;//省区
	private String categoryType;//商品分类
	private String exchangeType;//物品类型(实物、电子券、流量等)
	private String exchangeChannel;//兑换渠道 (商城、商城抽奖、扫码)
	private String goodsName;//商品名称
	private String goodsBigUrl;//商品大图标
	private String goodsActivityUrl;//商品活动图片
	private String goodsUrl;//商品图标
	private String goodsContent;//商品介绍
	private String batchKey;//电子券批次号
	private String goodsValue;//商品数据(话费几元或流量几M)
	private String goodsMoney;//商品原价
	private long goodsVpoints;//所需积分
	private String goodsDiscount;//折扣
	private long goodsNum;//商品个数
	private long goodsExchangeNum;//已兑换个数
	private long goodsRemains;//剩余个数
	private long goodsLimit;//兑换限制个数
	private long goodsUserDayLimit;//单商品每人每天兑换限制个数
	private String goodsStartTime;//兑换开启时间
	private String goodsEndTime;//兑换结束时间
	private String secKill;//秒杀活动
	private long goodsPay;
	private String shipmentsArea;//发货区域，格式：省-市-县;省-市;省;省-市-县...
	
	private String categoryParent;//父ID
	private String firstName;
	private String secondName;
	private String companyName;
	private String isCommend;
	private String isGift;//是否赠品
	private String isGiftAsCoupons;//是否与优惠券同享
	private String promotionSkuKeyAry;//赠品SKU
	private String showNum;
	private long realVpoints; 
	private long realPay;
	private long beforeCounts;
	private String isUpdate;
	/** 所属品牌主键*/
	private String brandId;
	/** 所属品牌名称*/
	private String brandName;
	/** 客户自己义商品编号*/
	private String goodsClientNo;
	/** 商品状态：0正常，1暂停兑换，2下架 **/
    private String goodsStatus;
    /** 暂停兑换提示语 */
    private String pauseExchangeTips;
    /** 商品简称*/
    private String goodsShortName;
    /** 商品展示类型：默认空，1.首页商品大图，2首页商品小图，3积分好礼 5、爆款推荐 6会员体系展示商品*/
    private String goodShowFlag;
	/**
	 * 爆款推荐标题
	 */
	private String hotRecommendTitle;
    /** 是否优品/尊品:1否、0是*/
    private String youPinFlag;
    /** 商品展示升序排序字段*/
    private Integer goodShowSequence;
    /** 代采金额(实际核销价值)*/
    private Double daicaiMoney;
    /** 代采积分（充值面额对应的核销积分） **/
    private String daicaiVpoints;
    /** 物流发货提示*/
    private String expressTips;
    /** 商品折扣前支付金额单位元*/
    private double goodsPayMoney;
    /** 是否接收到货通知：0不接收、1接收*/
    private String arrivalNoticeFlag;
    /** 商品对换可使用卡类型*/
    private String exchangeCardType;
    /** 自定义销量排名*/
    private Integer saleNumSequence;
    /** 商品单位名称*/
    private String goodsUnitName;
    /** 商品规格*/
    private String goodsSpecification;
    
    // 拼团
  	/** 是否拼团商品，0否，1是 **/
  	private String isGroupBuying;
  	/** 拼团商品积分 **/
  	private long groupBuyingVpoints;
  	/** 限制开团数量 **/
  	private int limitGroupNum;
  	/** 成团人数 **/
  	private long reachNum;
  	/** 备注信息 **/
  	private String remarkInfo;
    
  	//是否展示销量
  	private String isShowSales;
  	//展示类型
	private String showSalesType;
	//近30天展示销量
	private String showSales;
	//近30天展示销量基数
	private String showSalesBase;
	private List<String> goodsIdLst;
	private String goodsIdArrayStr;
	private String giftGoodsNum;
	private String giftGoodsName;
	/** 是否礼品卡 0 是 1 否*/
	private String isGiftCard;
	private String giftCardInfoKey;
	/** 自定义商品排序 */
	private Integer goodsCustomSequence;
	private String halfPriceActivityType;
	private String exchangeActivityType;
	//express_hotarea_key
	private String expressHotareaKey;


	public VpointsGoodsInfo(){}
	public VpointsGoodsInfo(String queryParam){
		if (!StringUtils.isEmpty(queryParam)) {
			queryParam = trickParam(queryParam);
			String[] params = queryParam.split(",");
			this.exchangeChannel = params.length > 0 ? !StringUtils.isEmpty(params[0]) ? params[0]: null : null;
			this.exchangeType = params.length > 1 ? !StringUtils.isEmpty(params[1]) ? params[1]: null : null;
			this.categoryParent = params.length > 2 ? !StringUtils.isEmpty(params[2]) ? params[2]: null : null;
			this.goodsName = params.length > 3 ? !StringUtils.isEmpty(params[3]) ? params[3]: null : null;
			this.brandId = params.length > 4 ? !StringUtils.isEmpty(params[4]) ? params[4]: null : null;
			this.goodShowFlag = params.length > 5 ? !StringUtils.isEmpty(params[5]) ? params[5]: null : null;
			this.goodsStatus = params.length > 6 ? !StringUtils.isEmpty(params[6]) ? params[6]: null : null;
		}
	}

	public String getExpressHotareaKey() {
		return expressHotareaKey;
	}

	public void setExpressHotareaKey(String expressHotareaKey) {
		this.expressHotareaKey = expressHotareaKey;
	}

	public String getGiftCardInfoKey() {
		return giftCardInfoKey;
	}

	public void setGiftCardInfoKey(String giftCardInfoKey) {
		this.giftCardInfoKey = giftCardInfoKey;
	}

	public String getHalfPriceActivityType() {
		return halfPriceActivityType;
	}

	public void setHalfPriceActivityType(String halfPriceActivityType) {
		this.halfPriceActivityType = halfPriceActivityType;
	}

	public String getExchangeActivityType() {
		return exchangeActivityType;
	}

	public void setExchangeActivityType(String exchangeActivityType) {
		this.exchangeActivityType = exchangeActivityType;
	}

	public Integer getGoodsCustomSequence() {
		return goodsCustomSequence;
	}

	public void setGoodsCustomSequence(Integer goodsCustomSequence) {
		this.goodsCustomSequence = goodsCustomSequence;
	}

	public String getIsShowSales() {
		return isShowSales;
	}

	public void setIsShowSales(String isShowSales) {
		this.isShowSales = isShowSales;
	}

	public String getShowSalesType() {
		return showSalesType;
	}

	public void setShowSalesType(String showSalesType) {
		this.showSalesType = showSalesType;
	}

	public String getExchangeCardType() {
        return exchangeCardType;
    }
    public void setExchangeCardType(String exchangeCardType) {
        this.exchangeCardType = exchangeCardType;
    }
    public String getArrivalNoticeFlag() {
        return arrivalNoticeFlag;
    }
    public void setArrivalNoticeFlag(String arrivalNoticeFlag) {
        this.arrivalNoticeFlag = arrivalNoticeFlag;
    }
    public long getRealPay() {
		return realPay;
	}
	public void setRealPay(long realPay) {
		this.realPay = realPay;
	}
	public long getGoodsPay() {
		return goodsPay;
	}
	public void setGoodsPay(long goodsPay) {
		this.goodsPay = goodsPay;
	}
	public String getSecKill() {
		return secKill;
	}
	public void setSecKill(String secKill) {
		this.secKill = secKill;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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
	public String getExchangeType() {
		return exchangeType;
	}
	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsUrl() {
		return goodsUrl;
	}
	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}
	public String getGoodsContent() {
		return goodsContent;
	}
	public void setGoodsContent(String goodsContent) {
		this.goodsContent = goodsContent;
	}
	public String getIsGift() {
		return isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	public String getIsGiftAsCoupons() {
		return isGiftAsCoupons;
	}

	public void setIsGiftAsCoupons(String isGiftAsCoupons) {
		this.isGiftAsCoupons = isGiftAsCoupons;
	}
	public String getPromotionSkuKeyAry() {
		return promotionSkuKeyAry;
	}

	public void setPromotionSkuKeyAry(String promotionSkuKeyAry) {
		this.promotionSkuKeyAry = promotionSkuKeyAry;
	}
	public String getBatchKey() {
		return batchKey;
	}
	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}
	public String getGoodsValue() {
		return goodsValue;
	}
	public void setGoodsValue(String goodsValue) {
		this.goodsValue = goodsValue;
	}
	public String getGoodsMoney() {
		return goodsMoney;
	}
	public void setGoodsMoney(String goodsMoney) {
		this.goodsMoney = goodsMoney;
	}
	public long getGoodsVpoints() {
		return goodsVpoints;
	}
	public void setGoodsVpoints(long goodsVpoints) {
		this.goodsVpoints = goodsVpoints;
	}
	public String getGoodsDiscount() {
		return goodsDiscount;
	}
	public void setGoodsDiscount(String goodsDiscount) {
		this.goodsDiscount = goodsDiscount;
	}
	public long getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(long goodsNum) {
		this.goodsNum = goodsNum;
	}
	public long getGoodsExchangeNum() {
		return goodsExchangeNum;
	}
	public void setGoodsExchangeNum(long goodsExchangeNum) {
		this.goodsExchangeNum = goodsExchangeNum;
	}
	public long getGoodsRemains() {
		return goodsRemains;
	}
	public void setGoodsRemains(long goodsRemains) {
		this.goodsRemains = goodsRemains;
	}
	public long getGoodsLimit() {
		return goodsLimit;
	}
	public void setGoodsLimit(long goodsLimit) {
		this.goodsLimit = goodsLimit;
	}
	public long getGoodsUserDayLimit() {
        return goodsUserDayLimit;
    }
    public void setGoodsUserDayLimit(long goodsUserDayLimit) {
        this.goodsUserDayLimit = goodsUserDayLimit;
    }
    public String getGoodsStartTime() {
		return goodsStartTime;
	}
	public void setGoodsStartTime(String goodsStartTime) {
		this.goodsStartTime = goodsStartTime;
	}
	public String getGoodsEndTime() {
		return goodsEndTime;
	}
	public void setGoodsEndTime(String goodsEndTime) {
		this.goodsEndTime = goodsEndTime;
	}
	public String getCategoryParent() {
		return categoryParent;
	}
	public void setCategoryParent(String categoryParent) {
		this.categoryParent = categoryParent;
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
	public String getIsCommend() {
		return isCommend;
	}
	public void setIsCommend(String isCommend) {
		this.isCommend = isCommend;
	}
	public String getShowNum() {
		return showNum;
	}
	public void setShowNum(String showNum) {
		this.showNum = showNum;
	}
	public long getRealVpoints() {
		return realVpoints;
	}
	public void setRealVpoints(long realVpoints) {
		this.realVpoints = realVpoints;
	}
	public String getExchangeChannel() {
		return exchangeChannel;
	}
	public void setExchangeChannel(String exchangeChannel) {
		this.exchangeChannel = exchangeChannel;
	}
	public long getBeforeCounts() {
		return beforeCounts;
	}
	public void setBeforeCounts(long beforeCounts) {
		this.beforeCounts = beforeCounts;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getGoodsClientNo() {
        return goodsClientNo;
    }
    public void setGoodsClientNo(String goodsClientNo) {
        this.goodsClientNo = goodsClientNo;
    }
    public String getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	public String getPauseExchangeTips() {
        return pauseExchangeTips;
    }
    public void setPauseExchangeTips(String pauseExchangeTips) {
        this.pauseExchangeTips = pauseExchangeTips;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getGoodsShortName() {
        return goodsShortName;
    }
    public void setGoodsShortName(String goodsShortName) {
        this.goodsShortName = goodsShortName;
    }
    
    public String getGoodShowFlag() {
		return goodShowFlag;
	}
	public void setGoodShowFlag(String goodShowFlag) {
		this.goodShowFlag = goodShowFlag;
	}
	public Integer getGoodShowSequence() {
		return goodShowSequence;
	}
	public void setGoodShowSequence(Integer goodShowSequence) {
		this.goodShowSequence = goodShowSequence;
	}
	public Double getDaicaiMoney() {
        return daicaiMoney;
    }
    public void setDaicaiMoney(Double daicaiMoney) {
        this.daicaiMoney = daicaiMoney;
    }
    public String getExpressTips() {
        return expressTips;
    }
    public void setExpressTips(String expressTips) {
        this.expressTips = expressTips;
    }
    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
	public String getShipmentsArea() {
		return shipmentsArea;
	}
	public void setShipmentsArea(String shipmentsArea) {
		this.shipmentsArea = shipmentsArea;
	}
    public double getGoodsPayMoney() {
        return goodsPayMoney;
    }
    public void setGoodsPayMoney(double goodsPayMoney) {
        this.goodsPayMoney = goodsPayMoney;
    }
    public Integer getSaleNumSequence() {
        return saleNumSequence;
    }
    public void setSaleNumSequence(Integer saleNumSequence) {
        this.saleNumSequence = saleNumSequence;
    }
    public String getGoodsUnitName() {
        return goodsUnitName;
    }
    public void setGoodsUnitName(String goodsUnitName) {
        this.goodsUnitName = goodsUnitName;
    }
	public String getYouPinFlag() {
		return youPinFlag;
	}
	public void setYouPinFlag(String youPinFlag) {
		this.youPinFlag = youPinFlag;
	}
	public String getIsGroupBuying() {
		return isGroupBuying;
	}
	public void setIsGroupBuying(String isGroupBuying) {
		this.isGroupBuying = isGroupBuying;
	}
	public long getGroupBuyingVpoints() {
		return groupBuyingVpoints;
	}
	public void setGroupBuyingVpoints(long groupBuyingVpoints) {
		this.groupBuyingVpoints = groupBuyingVpoints;
	}
	public int getLimitGroupNum() {
		return limitGroupNum;
	}
	public void setLimitGroupNum(int limitGroupNum) {
		this.limitGroupNum = limitGroupNum;
	}
	public long getReachNum() {
		return reachNum;
	}
	public void setReachNum(long reachNum) {
		this.reachNum = reachNum;
	}
	public String getDaicaiVpoints() {
		return daicaiVpoints;
	}
	public void setDaicaiVpoints(String daicaiVpoints) {
		this.daicaiVpoints = daicaiVpoints;
	}
	public String getGoodsBigUrl() {
		return goodsBigUrl;
	}
	public void setGoodsBigUrl(String goodsBigUrl) {
		this.goodsBigUrl = goodsBigUrl;
	}
	public String getGoodsActivityUrl() {
        return goodsActivityUrl;
    }
    public void setGoodsActivityUrl(String goodsActivityUrl) {
        this.goodsActivityUrl = goodsActivityUrl;
    }
    public String getGoodsSpecification() {
		return goodsSpecification;
	}
	public void setGoodsSpecification(String goodsSpecification) {
		this.goodsSpecification = goodsSpecification;
	}
	public String getRemarkInfo() {
		return remarkInfo;
	}
	public void setRemarkInfo(String remarkInfo) {
		this.remarkInfo = remarkInfo;
	}

	public String getShowSales() {
		return showSales;
	}

	public void setShowSales(String showSales) {
		this.showSales = showSales;
	}

	public String getShowSalesBase() {
		return showSalesBase;
	}

	public void setShowSalesBase(String showSalesBase) {
		this.showSalesBase = showSalesBase;
	}
    public List<String> getGoodsIdLst() {
        return goodsIdLst;
    }
    public void setGoodsIdLst(List<String> goodsIdLst) {
        this.goodsIdLst = goodsIdLst;
    }
    public String getGiftGoodsNum() {
        return giftGoodsNum;
    }
    public void setGiftGoodsNum(String giftGoodsNum) {
        this.giftGoodsNum = giftGoodsNum;
    }
    public String getGiftGoodsName() {
        return giftGoodsName;
    }
    public void setGiftGoodsName(String giftGoodsName) {
        this.giftGoodsName = giftGoodsName;
    }

	public String getIsGiftCard() {
		return isGiftCard;
	}

	public void setIsGiftCard(String isGiftCard) {
		this.isGiftCard = isGiftCard;
	}

	public String getHotRecommendTitle() {
		return hotRecommendTitle;
	}

	public void setHotRecommendTitle(String hotRecommendTitle) {
		this.hotRecommendTitle = hotRecommendTitle;
	}

	public String getGoodsIdArrayStr() {
		return goodsIdArrayStr;
	}

	public void setGoodsIdArrayStr(String goodsIdArrayStr) {
		this.goodsIdArrayStr = goodsIdArrayStr;
	}
}
