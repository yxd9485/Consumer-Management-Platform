package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BaseBean;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;

import java.util.List;

/**
 * 一等奖用户信息
 * @author:Jiquanwei<br>
 * @date:2016-4-26 上午11:30:53<br>
 * @version:1.0.0<br>
 * 
 */
public class VpsVcodePrizeRecord extends BaseBean{

	private static final long serialVersionUID = -2341263278103196955L;
	
	/** 主键*/
	private String infoKey;
	/** 副主键*/
	private String prizeInfoKey;
	/** 企业主键 */
	private String companyKey;
	/** 活动主键 */
	private String vcodeActivityKey;
    /** 规则主键*/
    private String rebateRuleKey;
    /** 奖项配置项主键*/
    private String vpointsCogKey;
    /** 领取用户 userkey*/
    private String userKey;
    /** 奖品类型 */
    private String grandPrizeType;
    /** 奖项名称*/
    private String prizeName;
	/** 奖品图片 */
	private String prizeImg;
    /** 领取时间 */
    private String earnTime;
    /** 领取金额 */
    private String earnMoney;
    /** 中奖SKU */
    private String skuKey;
    /** 中奖V码 */
    private String prizeVcode;
	/** 领取用户openid */
	private String openid;
	/** 用户姓名 */
	private String userName;
	/** 身份证号 */
	private String idCard;
	/** 手机号 */
	private String phoneNum;
	/** 省 */
	private String province;
	/** 市 */
	private String city;
	/** 县 */
	private String county;
	/** 地址*/
	private String address;
    private String customerMessage;
	/** 领取状态：0:未领取、1:已领取*/
	private String useStatus;
    /** 领取时间*/
    private String useTime;
    /** 兑换截止日期 */
    private String expireTime;
    /** 兑换日期（用于提示用户哪天兑奖） */
    private String exchangeDate;
    /** 中奖渠道：1商城兑换，2商城抽奖，3扫码中奖，4一码双奖，5 逢尾数 */
    private String exchangeChannel;
    /** 商品主键 */
    private String goodsId;
	/** 兑奖主键 */
	private String exchangeId;
	/** 核销状态：默认0未核销，1已核销 **/
	private String checkStatus;
	/** 核销人openid **/
	private String checkOpenid;
	/** 核销人 **/
	private String checkUserName;
	/** 核销时间 **/
	private String checkTime;
	/** 核销备注 **/
	private String checkRemarks;
	//-------------------------------扩展字段--------------------------------
	
	/** 奖项个数*/
	private String prizeTotal;
	/** 兑奖截止标志，1已截止*/
	private String expireFlag;
	/** 联通转盘URL **/
	private String unicomUrl;
	/** 大奖需支付金额，单位:元*/
	private double prizePayMoney;
	/** 尊享卡商品   **/
    private List<VpointsGoodsInfo> goodsInfos;
    /** 尊享卡折扣 **/
    private double prizeDiscount;
    private String goodsName;
    private String goodsShortName;
    private String goodsMoney;
    //中奖图片
    private String prizeWinPic;
    //兑奖图片
    private String prizeEarnPic;
    //大奖列表图
    private String prizeListPic;
    //奖项详情是否显示
    private String isShow;

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getPrizeWinPic() {
        return prizeWinPic;
    }

    public void setPrizeWinPic(String prizeWinPic) {
        this.prizeWinPic = prizeWinPic;
    }

    public String getPrizeEarnPic() {
        return prizeEarnPic;
    }

    public void setPrizeEarnPic(String prizeEarnPic) {
        this.prizeEarnPic = prizeEarnPic;
    }

    public String getPrizeListPic() {
        return prizeListPic;
    }

    public void setPrizeListPic(String prizeListPic) {
        this.prizeListPic = prizeListPic;
    }

    public double getPrizePayMoney() {
        return prizePayMoney;
    }
    public void setPrizePayMoney(double prizePayMoney) {
        this.prizePayMoney = prizePayMoney;
    }
	
    public String getUnicomUrl() {
		return unicomUrl;
	}
	public void setUnicomUrl(String unicomUrl) {
		this.unicomUrl = unicomUrl;
	}
	public String getExchangeDate() {
        return exchangeDate;
    }
    public void setExchangeDate(String exchangeDate) {
        this.exchangeDate = exchangeDate;
    }
    public String getCheckUserName() {
		return checkUserName;
	}
	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCheckOpenid() {
		return checkOpenid;
	}
	public void setCheckOpenid(String checkOpenid) {
		this.checkOpenid = checkOpenid;
	}
	public String getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckRemarks() {
		return checkRemarks;
	}
	public void setCheckRemarks(String checkRemarks) {
		this.checkRemarks = checkRemarks;
	}
	public String getExpireFlag() {
        return expireFlag;
    }
    public void setExpireFlag(String expireFlag) {
        this.expireFlag = expireFlag;
    }
    public String getPrizeImg() {
        return prizeImg;
    }
    public void setPrizeImg(String prizeImg) {
        this.prizeImg = prizeImg;
    }
    public String getPrizeName() {
        return prizeName;
    }
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
    public String getPrizeTotal() {
        return prizeTotal;
    }
    public void setPrizeTotal(String prizeTotal) {
        this.prizeTotal = prizeTotal;
    }
    public String getExpireTime() {
        return expireTime;
    }
    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
    public String getInfoKey() {
        return infoKey;
    }
    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }
    public String getCompanyKey() {
        return companyKey;
    }
    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }
    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }
    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getGrandPrizeType() {
        return grandPrizeType;
    }
    public void setGrandPrizeType(String grandPrizeType) {
        this.grandPrizeType = grandPrizeType;
    }
    public String getEarnTime() {
        return earnTime;
    }
    public void setEarnTime(String earnTime) {
        this.earnTime = earnTime;
    }
    public String getEarnMoney() {
        return earnMoney;
    }
    public void setEarnMoney(String earnMoney) {
        this.earnMoney = earnMoney;
    }
    public String getSkuKey() {
        return skuKey;
    }
    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }
    public String getPrizeVcode() {
        return prizeVcode;
    }
    public void setPrizeVcode(String prizeVcode) {
        this.prizeVcode = prizeVcode;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
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
    /** 领取状态：0:未领取、1:已领取 */
    public String getUseStatus() {
        return useStatus;
    }
    /** 领取状态：0:未领取、1:已领取 */
    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }
    public String getUseTime() {
        return useTime;
    }
    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }
    public String getExchangeChannel() {
        return exchangeChannel;
    }
    public void setExchangeChannel(String exchangeChannel) {
        this.exchangeChannel = exchangeChannel;
    }
    public String getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    public String getExchangeId() {
        return exchangeId;
    }
    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }
    public String getRebateRuleKey() {
        return rebateRuleKey;
    }
    public void setRebateRuleKey(String rebateRuleKey) {
        this.rebateRuleKey = rebateRuleKey;
    }
    public String getVpointsCogKey() {
        return vpointsCogKey;
    }
    public void setVpointsCogKey(String vpointsCogKey) {
        this.vpointsCogKey = vpointsCogKey;
    }
	public List<VpointsGoodsInfo> getGoodsInfos() {
		return goodsInfos;
	}
	public void setGoodsInfos(List<VpointsGoodsInfo> goodsInfos) {
		this.goodsInfos = goodsInfos;
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
    public String getGoodsMoney() {
        return goodsMoney;
    }
    public void setGoodsMoney(String goodsMoney) {
        this.goodsMoney = goodsMoney;
    }
	public String getPrizeInfoKey() {
		return prizeInfoKey;
	}
	public void setPrizeInfoKey(String prizeInfoKey) {
		this.prizeInfoKey = prizeInfoKey;
	}
	public double getPrizeDiscount() {
		return prizeDiscount;
	}
	public void setPrizeDiscount(double prizeDiscount) {
		this.prizeDiscount = prizeDiscount;
	}
	
    
}