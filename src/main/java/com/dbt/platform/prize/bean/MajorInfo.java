package com.dbt.platform.prize.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
/**
 * @author hanshimeng
 * @createTime 2016年4月21日 下午4:26:50
 * @description 一等奖中奖名单Bean
 */
public class MajorInfo implements Serializable{

	private static final long serialVersionUID = -3652941427383061347L;
	/** 主键 */
	private String infoKey;
	/** 用户KEY */
	private String userKey;
	/** 用户名称 */
	private String userName;
	/** 身份证 */
	private String idCard;
	/** 手机号 */
	private String phoneNum;
	/** SKUKEY */
	private String skuKey;
	/** SKU名称 */
	private String skuName;
    /** V码 */
    private String prizeVcode;
    /** 是否码中台码源：0否、1是*/
    private String mztFlag;
	/** 领取时间 */
	private String earnTime;
	/** 金额 */
	private String earnMoney;
	/** 奖品类型：0:一等奖、1:二等奖*/
	private String grandPrizeType;
	/** 用户openid*/
	private String openid;
	/** 用户昵称 */
	private String nickName;
	/** 配送地址 */
	private String address;
	/** 中出省 */
	private String province;
	/** 中出市 */
	private String city;
	/** 中出县 */
	private String county;
	private String startDate;
	private String endDate;
	private String useTime;
	private String expireTime;
    private boolean expireFlag;
	private String useStartDate;
	private String useEndDate;
    /** 领取状态：0:未领取、1:已领取*/
	private String useStatus;
	private String checkStartDate;
	private String checkEndDate;
	/** 核销状态：默认0未核销，1已核销 **/
	private String checkStatus;
	/** 核销人openid **/
	private String checkOpenid;
	/** 核销人 **/
	private String checkUserName;
	/** 核销人手机号 **/
	private String checkPhoneNum;
	/** 核销时间 **/
	private String checkTime;
	/** 核销备注 **/
	private String checkRemarks;
	/** 活动主键*/
	private String vcodeActivityKey;
	/** 规则主键*/
	private String rebateRuleKey;
	/** 奖项配置项主键*/
	private String vpointsCogKey;
	private int version;
	private String prizeName;
	/** 大奖过期回收标志:1已回收*/
	private String recycleFlag;
	/** 大奖来源*/
	private String prizeSource;
	private String typeString;
	private String terminalName;
	private String terminalKey;
	private String prizeAddress;
	private String expressCompany;
	private String expressNumber;
	private String expressSendMessage;
	private String companyKey;
	/** 所属合同年份*/
	private String contractYear;
	/** 奖品图片 */
	private String prizeImg;
	/** 中奖渠道：1商城兑换，2商城抽奖，3扫码中奖，4一码双奖，5 逢尾数 */
	private String exchangeChannel;
	/** 折现金额 **/
	private String exchangeMoney;
	private String prizeVcodeUrl;
    /** 中出渠道项目 (终端/消费端) **/
    private String projectChannel;

	public MajorInfo() {
	    super();
	}

    public MajorInfo(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.grandPrizeType = paramAry.length > 0 ? paramAry[0] : "";
        if(StringUtils.isNotEmpty(this.grandPrizeType)) {
			String[] types=this.grandPrizeType.split(";");
			String typeString="";
			for (String type : types) {
				typeString+=",'"+type+"'";
			}
			this.typeString=typeString.substring(1);
		}else {
			this.typeString=null;
		}
        this.phoneNum = paramAry.length > 1 ? paramAry[1] : "";
        this.startDate = paramAry.length > 2 ? paramAry[2] : "";
        this.endDate = paramAry.length > 3 ? paramAry[3] : "";
        this.prizeVcode = paramAry.length > 4 ? paramAry[4] : "";
        this.useStartDate = paramAry.length > 5 ? paramAry[5] : "";
        this.useEndDate = paramAry.length > 6 ? paramAry[6] : "";
        this.checkStartDate = paramAry.length > 7 ? paramAry[7] : "";
        this.checkEndDate = paramAry.length > 8 ? paramAry[8] : "";
        this.checkPhoneNum = paramAry.length > 9 ? paramAry[9] : "";
        this.checkStatus = paramAry.length > 10 ? paramAry[10] : "";
        this.terminalName = paramAry.length > 11 ? paramAry[11] : "";
    }

    public String getProjectChannel() {
        return projectChannel;
    }

    public void setProjectChannel(String projectChannel) {
        this.projectChannel = projectChannel;
    }

    public String getExchangeChannel() {
		return exchangeChannel;
	}

	public void setExchangeChannel(String exchangeChannel) {
		this.exchangeChannel = exchangeChannel;
	}

	public String getPrizeImg() {
		return prizeImg;
	}

	public void setPrizeImg(String prizeImg) {
		this.prizeImg = prizeImg;
	}

	public String getContractYear() {
		return contractYear;
	}

	public void setContractYear(String contractYear) {
		this.contractYear = contractYear;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getUseStatus() {
		return useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	public String getCheckOpenid() {
		return checkOpenid;
	}

	public void setCheckOpenid(String checkOpenid) {
		this.checkOpenid = checkOpenid;
	}

	public String getCheckUserName() {
		return checkUserName;
	}

	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
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

	public String getCheckStartDate() {
		return checkStartDate;
	}

	public void setCheckStartDate(String checkStartDate) {
		this.checkStartDate = checkStartDate;
	}

	public String getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(String checkEndDate) {
		this.checkEndDate = checkEndDate;
	}

	public String getCheckPhoneNum() {
		return checkPhoneNum;
	}

	public void setCheckPhoneNum(String checkPhoneNum) {
		this.checkPhoneNum = checkPhoneNum;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getUseStartDate() {
        return useStartDate;
    }

    public void setUseStartDate(String useStartDate) {
        this.useStartDate = useStartDate;
    }

    public String getUseEndDate() {
        return useEndDate;
    }

    public void setUseEndDate(String useEndDate) {
        this.useEndDate = useEndDate;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
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
	public String getSkuKey() {
		return skuKey;
	}
	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
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
    public String getGrandPrizeType() {
        return grandPrizeType;
    }
    public void setGrandPrizeType(String grandPrizeType) {
        this.grandPrizeType = grandPrizeType;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public String getPrizeVcode() {
        return prizeVcode;
    }
    public void setPrizeVcode(String prizeVcode) {
        this.prizeVcode = prizeVcode;
    }

    public boolean isExpireFlag() {
        return expireFlag;
    }

    public void setExpireFlag(boolean expireFlag) {
        this.expireFlag = expireFlag;
    }

    public String getVcodeActivityKey() {
        return vcodeActivityKey;
    }

    public void setVcodeActivityKey(String vcodeActivityKey) {
        this.vcodeActivityKey = vcodeActivityKey;
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getRecycleFlag() {
        return recycleFlag;
    }

    public void setRecycleFlag(String recycleFlag) {
        this.recycleFlag = recycleFlag;
    }

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getPrizeSource() {
		return prizeSource;
	}

	public void setPrizeSource(String prizeSource) {
		this.prizeSource = prizeSource;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getTerminalKey() {
		return terminalKey;
	}

	public void setTerminalKey(String terminalKey) {
		this.terminalKey = terminalKey;
	}

	public String getPrizeAddress() {
		return prizeAddress;
	}

	public void setPrizeAddress(String prizeAddress) {
		this.prizeAddress = prizeAddress;
	}

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressNumber() {
        return expressNumber;
    }

    public void setExpressNumber(String expressNumber) {
        this.expressNumber = expressNumber;
    }

    public String getExpressSendMessage() {
        return expressSendMessage;
    }

    public void setExpressSendMessage(String expressSendMessage) {
        this.expressSendMessage = expressSendMessage;
    }

	public String getExchangeMoney() {
		return exchangeMoney;
	}

	public void setExchangeMoney(String exchangeMoney) {
		this.exchangeMoney = exchangeMoney;
	}

    public String getMztFlag() {
        return mztFlag;
    }

    public void setMztFlag(String mztFlag) {
        this.mztFlag = mztFlag;
    }

    public String getPrizeVcodeUrl() {
        return prizeVcodeUrl;
    }

    public void setPrizeVcodeUrl(String prizeVcodeUrl) {
        this.prizeVcodeUrl = prizeVcodeUrl;
    }
	
}
