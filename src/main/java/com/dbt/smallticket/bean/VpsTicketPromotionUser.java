package com.dbt.smallticket.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

public class VpsTicketPromotionUser extends BasicProperties {

    private static final long serialVersionUID = 3016503899527514382L;

    private String infoKey;
    private String userKey;
    private String phoneNum;
    private String userName;
    private String userCode;
    private String ticketChannel;
    private String terminalSystem;
    private String terminalCode;
    private String terminalName;
    private String terminalAddress;
    private String province;
    private String city;
    private String county;
    private String terminalOpenid;
    
    /** 所属战区 **/
    private String warAreaName;
    /** 是否参与评选：默认0否，1 **/
    private String isJoinVote;
    /** 评选展示图片，多个以英文逗号分隔 **/
    private String uploadPic;
    /** 促销员评选介绍*/
    private String introduce;
    /** 促销员激活状态：0否，1是 **/
    private String status;
    /** 报名审核状态：默认0，1通过，2未通过，3驳回 **/
    private String checkStatus;
    /** 审批意见 **/
    private String checkOpinion;
    /** 省 */
    private String provinceCode;
    /** 市 */
    private String cityCode;
    /** 县*/
    private String countyCode;
    
    private String areaCode;
    
    /** 姓名、手机号、门店名称搜索条件 **/
    private String searchVal;
    private int firstImgH;
    
    public VpsTicketPromotionUser() {}
    public VpsTicketPromotionUser(String queryParam, String warAreaFlag) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
    	this.searchVal = paramAry.length > 0 ? paramAry[0] : "";
        this.provinceCode = paramAry.length > 1 ? paramAry[1] : "";
        this.cityCode = paramAry.length > 2 ? paramAry[2] : "";
        this.countyCode = paramAry.length > 3 ? paramAry[3] : "";
        
    	if("1".equals(warAreaFlag)) {
            this.isJoinVote = paramAry.length > 4 ? paramAry[4] : "";
            this.status = paramAry.length > 5 ? paramAry[5] : "";
            this.checkStatus = paramAry.length > 6 ? paramAry[6] : "";
    	}else {
            this.warAreaName = paramAry.length > 4 ? paramAry[4] : "";
            this.isJoinVote = paramAry.length > 5 ? paramAry[5] : "";
            this.status = paramAry.length > 6 ? paramAry[6] : "";
            this.checkStatus = paramAry.length > 7 ? paramAry[7] : "";
    	}
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
    public String getPhoneNum() {
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public String getTicketChannel() {
        return ticketChannel;
    }
    public void setTicketChannel(String ticketChannel) {
        this.ticketChannel = ticketChannel;
    }
    public String getTerminalSystem() {
        return terminalSystem;
    }
    public void setTerminalSystem(String terminalSystem) {
        this.terminalSystem = terminalSystem;
    }
    public String getTerminalName() {
        return terminalName;
    }
    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }
    public String getTerminalAddress() {
        return terminalAddress;
    }
    public void setTerminalAddress(String terminalAddress) {
        this.terminalAddress = terminalAddress;
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
    public String getTerminalOpenid() {
        return terminalOpenid;
    }
    public void setTerminalOpenid(String terminalOpenid) {
        this.terminalOpenid = terminalOpenid;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
	public String getWarAreaName() {
		return warAreaName;
	}
	public void setWarAreaName(String warAreaName) {
		this.warAreaName = warAreaName;
	}
	public String getIsJoinVote() {
		return isJoinVote;
	}
	public void setIsJoinVote(String isJoinVote) {
		this.isJoinVote = isJoinVote;
	}
	public String getUploadPic() {
		return uploadPic;
	}
	public void setUploadPic(String uploadPic) {
		this.uploadPic = uploadPic;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	public String getCheckOpinion() {
		return checkOpinion;
	}
	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}
	public String getSearchVal() {
		return searchVal;
	}
	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	public String getTerminalCode() {
		return terminalCode;
	}
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
    public int getFirstImgH() {
        return firstImgH;
    }
    public void setFirstImgH(int firstImgH) {
        this.firstImgH = firstImgH;
    }
}
