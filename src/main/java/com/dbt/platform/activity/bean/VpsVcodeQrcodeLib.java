package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BaseBean;

/**
 * 码库表
 * @author:Jiquanwei<br>
 * @date:2016-4-21 上午11:30:02<br>
 * @version:1.0.0<br>
 * 
 */
public class VpsVcodeQrcodeLib extends BaseBean{

	private static final long serialVersionUID = 8639150611067276633L;
	private String vcodeKey;
	private String batchKey;
	private String vcodeActivityKey;
	private String companyKey;
	private String packKey;
	/** 二维码内容**/
	private String qrcodeContent;
	/** 二维码类型 0.普通二维码；1.指定金额二维码*/
	private String qrcodeType;
	/** 码库表名**/
	private String libName;
	/** 扫码用户**/
	private String userKey;
	/** 积分配置主键**/
	private String vpointsCogKey;
	/** 扫码状态**/
	private String useStatus;
	/** 扫码状态**/
	private String useTime;
	/** 扫码金额**/
	private String earnMoney;
	/**
	 * 瓜分固定金额
	 */
	private Integer fixedAmount;
	
	private String batchAutocodeNo;
	/** 中粮五星溯源地址，保存到 col1**/
	private String traceabilityURL;
	private int qrcodeAmount;

	public String getTraceabilityURL() {
		return traceabilityURL;
	}

	public void setTraceabilityURL(String traceabilityURL) {
		this.traceabilityURL = traceabilityURL;
	}

	public String getQrcodeType() {
		return qrcodeType;
	}
	public void setQrcodeType(String qrcodeType) {
		this.qrcodeType = qrcodeType;
	}
	public String getVcodeKey() {
		return vcodeKey;
	}
	public void setVcodeKey(String vcodeKey) {
		this.vcodeKey = vcodeKey;
	}
	public String getBatchKey() {
		return batchKey;
	}
	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}
	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}
	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getPackKey() {
		return packKey;
	}
	public void setPackKey(String packKey) {
		this.packKey = packKey;
	}
	public String getQrcodeContent() {
		return qrcodeContent;
	}
	public void setQrcodeContent(String qrcodeContent) {
		this.qrcodeContent = qrcodeContent;
	}
	public String getLibName() {
		return libName;
	}
	public void setLibName(String libName) {
		this.libName = libName;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
    public String getEarnMoney() {
        return earnMoney;
    }
    public void setEarnMoney(String earnMoney) {
        this.earnMoney = earnMoney;
    }
    public String getVpointsCogKey() {
        return vpointsCogKey;
    }
    public void setVpointsCogKey(String vpointsCogKey) {
        this.vpointsCogKey = vpointsCogKey;
    }
    public String getBatchAutocodeNo() {
        return batchAutocodeNo;
    }
    public void setBatchAutocodeNo(String batchAutocodeNo) {
        this.batchAutocodeNo = batchAutocodeNo;
    }

	public Integer getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(Integer fixedAmount) {
		this.fixedAmount = fixedAmount;
	}
    public int getQrcodeAmount() {
        return qrcodeAmount;
    }
    public void setQrcodeAmount(int qrcodeAmount) {
        this.qrcodeAmount = qrcodeAmount;
    }
}