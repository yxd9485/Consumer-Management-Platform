package com.dbt.platform.blacklist.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BaseBean;


/**
 * 黑名单bean
 * 
 * @author:Jiquanwei<br>
 * @date:2015-7-8 上午10:11:34<br>
 * @version:1.0.0<br>
 * 
 */
public class Blacklist extends BaseBean {

	private static final long serialVersionUID = 5244263689860330247L;

	private String blackKey;
	/** 黑名单类型 **/
	private String blackType;
	/** 黑名单值 **/
	private String blackValue;
	/** 黑白名单类型 **/
	private String ifBlackList;
	/** 微信OPENID **/
	private String openid;
	/** 用户昵称 **/
	private String nickName;
	/** 活动主键 **/
	private String vcodeActivityKey;
	private String vcodeActivityName;
	private String phoneNum;
	private String blackReason;
	private String startDate;
	private String endDate;
	private String province;
	private String city;
	private String county;
	private String address;
	private String doubtReason;

	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}

	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}

	public Blacklist() {
		super();
	}
    
    public Blacklist(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.openid = paramAry.length > 0 ? paramAry[0] : "";
        this.nickName = paramAry.length > 1 ? paramAry[1] : "";
        this.vcodeActivityName = paramAry.length > 2 ? paramAry[2] : "";
        this.phoneNum = paramAry.length > 3 ? paramAry[3] : "";
        this.blackReason = paramAry.length > 4 ? paramAry[4] : "";
        this.startDate = paramAry.length > 5 ? paramAry[5] : "";
        this.endDate = paramAry.length > 6 ? paramAry[6] : "";
    }

	public Blacklist(String blackKey, String blackType, String blackValue,String vcodeActivityKey,
	                        String ifBlackList, String province, String city, String county, String doubtReason) {
		this.blackKey = blackKey;
		this.blackType = blackType;
		this.blackValue = blackValue;
		this.ifBlackList = ifBlackList;
		this.vcodeActivityKey = vcodeActivityKey;
		this.province = province;
		this.city = city;
		this.county = county;
		this.doubtReason = doubtReason;
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

    public String getDoubtReason() {
        return doubtReason;
    }

    public void setDoubtReason(String doubtReason) {
        this.doubtReason = doubtReason;
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

	public String getBlackKey() {
		return blackKey;
	}

	public void setBlackKey(String blackKey) {
		this.blackKey = blackKey;
	}

	public String getBlackType() {
		return blackType;
	}

	public void setBlackType(String blackType) {
		this.blackType = blackType;
	}

	public String getBlackValue() {
		return blackValue;
	}

	public void setBlackValue(String blackValue) {
		this.blackValue = blackValue;
	}

	public String getIfBlackList() {
		return ifBlackList;
	}

	public void setIfBlackList(String ifBlackList) {
		this.ifBlackList = ifBlackList;
	}

    public String getVcodeActivityName() {
        return vcodeActivityName;
    }

    public void setVcodeActivityName(String vcodeActivityName) {
        this.vcodeActivityName = vcodeActivityName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBlackReason() {
        return blackReason;
    }

    public void setBlackReason(String blackReason) {
        this.blackReason = blackReason;
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

}