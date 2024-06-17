package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BaseBean;

/**
 * 文件名: VcodePacksRecord.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: V码活动扫码红包领取记录<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-26 16:01:39<br>
 * 修改内容：新增<br>
 */
public class VcodePacksRecord extends BaseBean {

	/** **/
	private static final long serialVersionUID = 1L;
	/** */
	private String infoKey;
	/** */
	private String vpointsCogKey;
	/** */
	private String companyKey;
	/** */
	private String vcodeActivityKey;
	/**  规则主键. */
	private String rebateRuleKey;
	/** */
	private String packKey;
	/** */
	private String batchKey;
	/** 扫码用户 */
	private String userKey;
	/** 扫码时间 */
	private String earnTime;
	/** */
	private String skuKey;
	/** 奖项类型*/
	private String prizeType;
	/** 领取金额 */
	private Double earnMoney;
	/** 领取积分 */
	private String earnVpoints;
	/** 领取大奖 */
	private String earnPrizeName;
	
	/** 已扫描个数 **/
	private int num;
	
	private String nickName;
	/** */
	private String province;
	/** */
	private String city;
	/** */
	private String county;

	public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getEarnVpoints() {
        return earnVpoints;
    }

    public void setEarnVpoints(String earnVpoints) {
        this.earnVpoints = earnVpoints;
    }

    public String getEarnPrizeName() {
        return earnPrizeName;
    }

    public void setEarnPrizeName(String earnPrizeName) {
        this.earnPrizeName = earnPrizeName;
    }

    public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
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

	public String getRebateRuleKey() {
		return rebateRuleKey;
	}

	public void setRebateRuleKey(String rebateRuleKey) {
		this.rebateRuleKey = rebateRuleKey;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getVpointsCogKey() {
		return vpointsCogKey;
	}

	public void setVpointsCogKey(String vpointsCogKey) {
		this.vpointsCogKey = vpointsCogKey;
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

	public String getPackKey() {
		return packKey;
	}

	public void setPackKey(String packKey) {
		this.packKey = packKey;
	}

	public String getBatchKey() {
		return batchKey;
	}

	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
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

	public String getSkuKey() {
		return skuKey;
	}

	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}

	public Double getEarnMoney() {
		return earnMoney;
	}

	public void setEarnMoney(Double earnMoney) {
		this.earnMoney = earnMoney;
	}

}
