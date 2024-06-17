package com.dbt.platform.doubtuser.bean;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * @author hanshimeng 
 * @createTime 2016年4月21日 下午5:44:15
 * @description V码黑名单用户信息
 */

public class VcodeBlacklistAccount extends VcodeBaseCog implements Serializable {

	private static final long serialVersionUID = 7298738569192604150L;
	private String blacklistKey;
	/** 是否疑似黑名单 */
	private String ifBlacklist;
	/** 疑似黑名单类型 */
	private String blacklistType;
	/** 疑似黑名单值 */
	private String blacklistValue;
	/** 活动主键 **/
	private String vcodeActivityKey;
	/** 活动名称 **/
	private String vcodeActivityName;
	/** 疑似原因：对照黑名单配置中的规则 */
	private String doubtReason;
	/** 可疑加入限制时间**/
	private String doubtfulTimeLimitType;
	/** 是否已加入黑名单 */
	private String joinBlacklist;
	/** 加入黑名单时间 */
	private String joinTime;
	/** 用户昵称 */
	private String nickName;
	/** openid */
	private String openid;
	/** 是否添加 */
	private boolean isAddFlag;
    /** 手机号 */
    private String phoneNum;
    private String startDate;
    private String endDate;
    private String province;
    private String city;
    private String county;
    private String address;

    public VcodeBlacklistAccount() {
        super();
    }
    
    public VcodeBlacklistAccount(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.openid = paramAry.length > 0 ? paramAry[0] : "";
        this.nickName = paramAry.length > 1 ? paramAry[1] : "";
        this.vcodeActivityName = paramAry.length > 2 ? paramAry[2] : "";
        this.phoneNum = paramAry.length > 3 ? paramAry[3] : "";
        this.doubtReason = paramAry.length > 4 ? paramAry[4] : "";
        this.startDate = paramAry.length > 5 ? paramAry[5] : "";
        this.endDate = paramAry.length > 6 ? paramAry[6] : "";
    }

    public VcodeBlacklistAccount(String blacklistKey, String ifBlacklist, String blacklistType, String blacklistValue, 
                String vcodeActivityKey, String doubtReason,String doubtfulTimeLimitType,  String joinBlacklist, 
								 String province, String city, 
								 String county) {
        this.blacklistKey = blacklistKey;
        this.ifBlacklist = ifBlacklist;
        this.blacklistType = blacklistType;
        this.blacklistValue = blacklistValue;
        this.doubtReason = doubtReason;
        this.doubtfulTimeLimitType=doubtfulTimeLimitType;
        this.joinBlacklist = joinBlacklist;
        this.vcodeActivityKey = vcodeActivityKey;
        this.province = province;
        this.city = city;
        this.county = county;
    }

	public String getDoubtfulTimeLimitType () {
		return doubtfulTimeLimitType;
	}
	
	public void setDoubtfulTimeLimitType (String doubtfulTimeLimitType) {
		this.doubtfulTimeLimitType = doubtfulTimeLimitType;
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
    public String getVcodeActivityName() {
		return vcodeActivityName;
	}

	public void setVcodeActivityName(String vcodeActivityName) {
		this.vcodeActivityName = vcodeActivityName;
	}

	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}

	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}

	public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setAddFlag(boolean isAddFlag) {
        this.isAddFlag = isAddFlag;
    }

    public boolean getIsAddFlag() {
		return isAddFlag;
	}

	public void setIsAddFlag(boolean isAddFlag) {
		this.isAddFlag = isAddFlag;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getBlacklistKey() {
		return blacklistKey;
	}

	public void setBlacklistKey(String blacklistKey) {
		this.blacklistKey = blacklistKey;
	}

	public String getIfBlacklist() {
		return ifBlacklist;
	}

	public void setIfBlacklist(String ifBlacklist) {
		this.ifBlacklist = ifBlacklist;
	}

	public String getBlacklistType() {
		return blacklistType;
	}

	public void setBlacklistType(String blacklistType) {
		this.blacklistType = blacklistType;
	}

	public String getBlacklistValue() {
		return blacklistValue;
	}

	public void setBlacklistValue(String blacklistValue) {
		this.blacklistValue = blacklistValue;
	}

	public String getDoubtReason() {
		return doubtReason;
	}

	public void setDoubtReason(String doubtReason) {
		this.doubtReason = doubtReason;
	}

	public String getJoinBlacklist() {
		return joinBlacklist;
	}

	public void setJoinBlacklist(String joinBlacklist) {
		this.joinBlacklist = joinBlacklist;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
