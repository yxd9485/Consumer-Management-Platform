package com.dbt.platform.activity.bean;



import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.base.bean.Constant.PrizeType;

/**
 * 门店Bean
 */
public class VpsTerminalInfo extends BasicProperties {
    
    private static final long serialVersionUID = 1L;
    /** 门店主键 */
    private String terminalKey;
    /** 门店名称 */
    private String terminalName;
    /** 门店编号*/
    private String terminalNo;
    /** 门店图片 */
    private String imageUrl;
    /** 省 */
    private String province;
    /** 市 */
    private String city;
    /** 县*/
    private String county;
    /** 联系电话*/
    private String phoneNum;
    /** 门店地址*/
    private String address;
   
    private String status;

    private String areaCode;
    /** 终端门核销员主键*/
    private String terminalCheckuserKey;
    /** 经度*/
    private String longitude;
    /** 纬度*/
    private String latitude;
    /** 大奖type*/
    private String prizeTyleLst;
    /** 老大奖type*/
    private String oldPrizeTyleLst;
   
    //---------------扩展字段-------------------
    /** 距离*/
    private String distance;
    private String prizeType;
    private String sqStatus;
    private String coordinate;
    
    
    public VpsTerminalInfo(){
    	
    }
    
	public VpsTerminalInfo(String queryParam,boolean flag) {
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
		if (flag){
			this.terminalNo = paramAry.length > 0 ? paramAry[0] : "";
			this.terminalName = paramAry.length > 1 ? paramAry[1] : "";
			this.prizeType = paramAry.length > 2 ? paramAry[2] : "";	
			this.sqStatus =  StringUtils.isNotBlank(prizeType)? "1":"";
		}else {
			 this.province = (paramAry.length > 0 ) ? paramAry[0] : "";
		     this.city = (paramAry.length > 1 ) ? paramAry[1] : "";
		     this.county = (paramAry.length > 2) ? paramAry[2] : "";	
		     this.sqStatus = (paramAry.length > 3  && !"null".equals((paramAry[3]))) ? paramAry[3] : "1";
		     this.terminalNo = paramAry.length > 4 ? paramAry[4] : "";	
		     this.prizeType =  paramAry.length > 5  ? paramAry[5] : "";
		}   
	}
	
	public VpsTerminalInfo(String prizeType) {
		this.prizeType = prizeType.substring(0,prizeType.length()- 1);
		this.sqStatus = "1";
	}


	public String getTerminalKey() {
		return terminalKey;
	}

	public void setTerminalKey(String terminalKey) {
		this.terminalKey = terminalKey;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTerminalCheckuserKey() {
		return terminalCheckuserKey;
	}

	public void setTerminalCheckuserKey(String terminalCheckuserKey) {
		this.terminalCheckuserKey = terminalCheckuserKey;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}

	public String getSqStatus() {
		return sqStatus;
	}

	public void setSqStatus(String sqStatus) {
		this.sqStatus = sqStatus;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getPrizeTyleLst() {
		return prizeTyleLst;
	}

	public void setPrizeTyleLst(String prizeTyleLst) {
		this.prizeTyleLst = prizeTyleLst;
	}

	public String getOldPrizeTyleLst() {
		return oldPrizeTyleLst;
	}

	public void setOldPrizeTyleLst(String oldPrizeTyleLst) {
		this.oldPrizeTyleLst = oldPrizeTyleLst;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

}
