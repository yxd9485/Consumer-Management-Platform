package com.dbt.smallticket.bean;



import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
/**
 * 小票bean
 */
public class VpsTicketRecord extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String infoKey;
	private String ticketChannel;
	private String userKey;
	private String ticketNo;
	private String terminalKey;
	private String province;
	private String city;
	private String county;
	private String longitude;
	private String latitude;
	private String geoHash;
	private Double goodsMoney;
	private String ticketMoney;
	private String uploadTime;
	private String ticketUrl;
	private String qrcodeContent;
	private String winShopKey;
	private String ticketStatus;
	private String inputUserKey;
	private String inputUserName;
	private String inputTime;
	private String submintCheckReason;
	private String checkUserKey;
	private String checkUserName;
	private String dismissReason;
	private String prizeType;
	private String earnTime;
	private String earnMoney;	
	private String ticketTerminalName;
	private String ocrContent;
	private String remark;
	private int goodsNum;
	private double skuMoney;
	private String warAreaName;
	private String promotionUserKey;
	private String promotionTerminalName;
	private String promotionEarnFlag;
	private String ticketRecordKey;
	/**-----------**/
	private String searchType;
	private List<VpsTicketRecordSkuDetail> detailLst;
	private String insideCodeType;
	private String terminalInsideCode[];
    private String ticketSkuName[];
    private String skuKey[];
    private String skuNum[];
    private String ticketSkuUnitMoney[];
    private String ticketSkuMoney[];
    private String userName;
    private String startDate;
    private String endDate;
    private String inputStartTime;
    private String inputEndTime;
    private String prizeName;
    private String projressName;
    private String openid;
    private String insideCode;
    private String ticketInsideCodeType;
    private String promotionUserName;
    private String promotionPhoneNum;
	public VpsTicketRecord(){
		
	}
	
	 public VpsTicketRecord(String queryParam) {
	        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
	        this.ticketChannel = paramAry.length > 0 ? paramAry[0] : "";
	        this.province = paramAry.length > 1 ? paramAry[1] : "";
	        this.startDate = paramAry.length > 2 ? paramAry[2] : "";
	        this.endDate = paramAry.length > 3 ? paramAry[3] : "";
	        this.inputUserName = paramAry.length > 4 ? paramAry[4] : "";
	        this.ticketStatus = paramAry.length > 5 ? paramAry[5] : "";
	        this.inputStartTime = paramAry.length > 6 ? paramAry[6] : "";
	        this.inputEndTime = paramAry.length > 7 ? paramAry[7] : "";
	        this.ticketNo = paramAry.length > 8 ? paramAry[8] : "";
	        this.promotionPhoneNum = paramAry.length > 9 ? paramAry[9] : "";
	        this.prizeType = paramAry.length > 10 ? paramAry[10] : "";
	    }
	

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getTicketChannel() {
		return ticketChannel;
	}

	public void setTicketChannel(String ticketChannel) {
		this.ticketChannel = ticketChannel;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getTerminalKey() {
		return terminalKey;
	}

	public void setTerminalKey(String terminalKey) {
		this.terminalKey = terminalKey;
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

	public String getGeoHash() {
		return geoHash;
	}

	public void setGeoHash(String geoHash) {
		this.geoHash = geoHash;
	}

	public Double getGoodsMoney() {
		return goodsMoney;
	}

	public void setGoodsMoney(Double goodsMoney) {
		this.goodsMoney = goodsMoney;
	}

	public String getTicketMoney() {
		return ticketMoney;
	}

	public void setTicketMoney(String ticketMoney) {
		this.ticketMoney = ticketMoney;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getTicketUrl() {
		return ticketUrl;
	}

	public void setTicketUrl(String ticketUrl) {
		this.ticketUrl = ticketUrl;
	}

	public String getQrcodeContent() {
		return qrcodeContent;
	}

	public void setQrcodeContent(String qrcodeContent) {
		this.qrcodeContent = qrcodeContent;
	}

	public String getWinShopKey() {
		return winShopKey;
	}

	public void setWinShopKey(String winShopKey) {
		this.winShopKey = winShopKey;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getInputUserKey() {
		return inputUserKey;
	}

	public void setInputUserKey(String inputUserKey) {
		this.inputUserKey = inputUserKey;
	}

	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}

	public String getSubmintCheckReason() {
		return submintCheckReason;
	}

	public void setSubmintCheckReason(String submintCheckReason) {
		this.submintCheckReason = submintCheckReason;
	}

	public String getCheckUserKey() {
		return checkUserKey;
	}

	public void setCheckUserKey(String checkUserKey) {
		this.checkUserKey = checkUserKey;
	}

	public String getDismissReason() {
		return dismissReason;
	}

	public void setDismissReason(String dismissReason) {
		this.dismissReason = dismissReason;
	}

	public String getPrizeType() {
		return prizeType;
	}

	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
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

	public String getTicketTerminalName() {
		return ticketTerminalName;
	}

	public void setTicketTerminalName(String ticketTerminalName) {
		this.ticketTerminalName = ticketTerminalName;
	}

	public String getOcrContent() {
		return ocrContent;
	}

	public void setOcrContent(String ocrContent) {
		this.ocrContent = ocrContent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}


	public String getInsideCodeType() {
		return insideCodeType;
	}

	public void setInsideCodeType(String insideCodeType) {
		this.insideCodeType = insideCodeType;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public List<VpsTicketRecordSkuDetail> getDetailLst() {
		return detailLst;
	}

	public void setDetailLst(List<VpsTicketRecordSkuDetail> detailLst) {
		this.detailLst = detailLst;
	}

	public String[] getTerminalInsideCode() {
		return terminalInsideCode;
	}

	public void setTerminalInsideCode(String[] terminalInsideCode) {
		this.terminalInsideCode = terminalInsideCode;
	}

	public String[] getTicketSkuName() {
		return ticketSkuName;
	}

	public void setTicketSkuName(String[] ticketSkuName) {
		this.ticketSkuName = ticketSkuName;
	}

	public String[] getSkuKey() {
		return skuKey;
	}

	public void setSkuKey(String[] skuKey) {
		this.skuKey = skuKey;
	}

	public String[] getSkuNum() {
		return skuNum;
	}

	public void setSkuNum(String[] skuNum) {
		this.skuNum = skuNum;
	}

	public String[] getTicketSkuUnitMoney() {
		return ticketSkuUnitMoney;
	}

	public void setTicketSkuUnitMoney(String[] ticketSkuUnitMoney) {
		this.ticketSkuUnitMoney = ticketSkuUnitMoney;
	}

	public String[] getTicketSkuMoney() {
        return ticketSkuMoney;
    }

    public void setTicketSkuMoney(String[] ticketSkuMoney) {
        this.ticketSkuMoney = ticketSkuMoney;
    }

    public String getInputUserName() {
		return inputUserName;
	}

	public void setInputUserName(String inputUserName) {
		this.inputUserName = inputUserName;
	}

	public String getCheckUserName() {
		return checkUserName;
	}

	public void setCheckUserName(String checkUserName) {
		this.checkUserName = checkUserName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getInputStartTime() {
		return inputStartTime;
	}

	public void setInputStartTime(String inputStartTime) {
		this.inputStartTime = inputStartTime;
	}

	public String getInputEndTime() {
		return inputEndTime;
	}

	public void setInputEndTime(String inputEndTime) {
		this.inputEndTime = inputEndTime;
	}

	public String getPrizeName() {
		return prizeName;
	}

	public void setPrizeName(String prizeName) {
		this.prizeName = prizeName;
	}

	public String getProjressName() {
		return projressName;
	}

	public void setProjressName(String projressName) {
		this.projressName = projressName;
	}

	public double getSkuMoney() {
		return skuMoney;
	}

	public void setSkuMoney(double skuMoney) {
		this.skuMoney = skuMoney;
	}

    public String getWarAreaName() {
		return warAreaName;
	}

	public void setWarAreaName(String warAreaName) {
		this.warAreaName = warAreaName;
	}

	public String getPromotionUserKey() {
        return promotionUserKey;
    }

    public void setPromotionUserKey(String promotionUserKey) {
        this.promotionUserKey = promotionUserKey;
    }

    public String getPromotionTerminalName() {
        return promotionTerminalName;
    }

    public void setPromotionTerminalName(String promotionTerminalName) {
        this.promotionTerminalName = promotionTerminalName;
    }

    public String getPromotionEarnFlag() {
        return promotionEarnFlag;
    }

    public void setPromotionEarnFlag(String promotionEarnFlag) {
        this.promotionEarnFlag = promotionEarnFlag;
    }

    public String getTicketRecordKey() {
        return ticketRecordKey;
    }

    public void setTicketRecordKey(String ticketRecordKey) {
        this.ticketRecordKey = ticketRecordKey;
    }

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getInsideCode() {
		return insideCode;
	}

	public void setInsideCode(String insideCode) {
		this.insideCode = insideCode;
	}
	
	public String getTicketInsideCodeType() {
		return ticketInsideCodeType;
	}

	public void setTicketInsideCodeType(String ticketInsideCodeType) {
		this.ticketInsideCodeType = ticketInsideCodeType;
	}

	public String getPromotionUserName() {
        return promotionUserName;
    }

    public void setPromotionUserName(String promotionUserName) {
        this.promotionUserName = promotionUserName;
    }

    public String getPromotionPhoneNum() {
        return promotionPhoneNum;
    }

    public void setPromotionPhoneNum(String promotionPhoneNum) {
        this.promotionPhoneNum = promotionPhoneNum;
    }

    public void setInput(SysUserBasis user){
		  this.inputUserKey=user.getUserKey();
		  this.inputUserName = user.getUserName();
		  this.inputTime = DateUtil.getDate();
		  this.searchType = "1";
	 }
}
