package com.dbt.platform.sweep.bean;

import java.io.Serializable;

/**
 * 公共奖项记录
 */
public class VpsCommonPacksRecord implements Serializable {

    private static final long serialVersionUID = -3620112480657463347L;
    /** 主键 */
    private String infoKey;
    /** 用户主键 */
    private String userKey;
    /** 领取时间  */
    private String earnTime;
    /** 领取金额 */
    private Double earnMoney;
    /** 领取积分 */
    private Integer earnVpoints;
    /** 奖项类型：0现金红包, 1积分红包， 2现金+积分红包 **/
    private String prizeType;
    /** 领取渠道：1集卡兑换, 2商城兑换；3商城抽奖；4扫码中奖 **/
    private String earnChannel;
    /** 集卡类型：1新春福卡 **/
	private String cardType;
    /** 省 */
    private String province;
    /** 市 */
    private String city;
    /** 县 */
    private String county;
    /** 奖项描述 */
    private String prizeDesc;

    public String getPrizeDesc() {
        return prizeDesc;
    }

    public void setPrizeDesc(String prizeDesc) {
        this.prizeDesc = prizeDesc;
    }

    public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getEarnChannel() {
		return earnChannel;
	}
	public void setEarnChannel(String earnChannel) {
		this.earnChannel = earnChannel;
	}
	public Integer getEarnVpoints() {
		return earnVpoints;
	}
	public void setEarnVpoints(Integer earnVpoints) {
		this.earnVpoints = earnVpoints;
	}
	public String getPrizeType() {
		return prizeType;
	}
	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
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
    public String getEarnTime() {
        return earnTime;
    }
    public void setEarnTime(String earnTime) {
        this.earnTime = earnTime;
    }
    public Double getEarnMoney() {
		return earnMoney;
	}
	public void setEarnMoney(Double earnMoney) {
		this.earnMoney = earnMoney;
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
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
