package com.dbt.platform.permantissa.bean;
import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 逢尾数中奖明细Dean
 */
@SuppressWarnings("serial")
public class VpsVcodePerMantissaPrizeRecord extends BasicProperties {
	
	/** 主键KEY **/
	private String infoKey;
	/** 企业KEY **/
	private String companyKey;
	/** 逢尾数规则主键 **/
	private String perMantissaKey;
	/** 用户openid **/
	private String openid;
	/** 用户主键 **/
	private String userKey;
	/** 微信昵称 **/
	private String nickName;
	/** 中奖码 **/
	private String qrcodeContent;
	/** 中奖SKU*/
	private String skuKey;
	/** 中奖SKU名称*/
	private String skuName;
	/** 中奖扫码瓶数 **/
	private int prizeScanNum;
	/** 奖项类型 **/
	private String prizeType;
	/** 中奖积分 **/
	private int earnVpoints;
	/** 中奖金额 **/
	private double earnMoney;
	/** 奖项名称*/
	private String prizeName;
	private String province;
	private String city;
	private String county;
	private String address;
	
	/** 扩充字段 **/
	/** 开始时间 **/
	private String startDate;
	/** 结束时间 **/
	private String endDate;
	
	public VpsVcodePerMantissaPrizeRecord(){}
	public VpsVcodePerMantissaPrizeRecord(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
		this.openid = paramAry.length > 0 ? paramAry[0] : "";
        this.nickName = paramAry.length > 1 ? paramAry[1] : "";
        this.qrcodeContent = paramAry.length > 2 ? paramAry[2] : "";
        this.startDate = paramAry.length > 3 ? paramAry[3] : "";
        this.endDate = paramAry.length > 4 ? paramAry[4] : "";
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
    public String getPerMantissaKey() {
        return perMantissaKey;
    }
    public void setPerMantissaKey(String perMantissaKey) {
        this.perMantissaKey = perMantissaKey;
    }
    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getUserKey() {
        return userKey;
    }
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getQrcodeContent() {
        return qrcodeContent;
    }
    public void setQrcodeContent(String qrcodeContent) {
        this.qrcodeContent = qrcodeContent;
    }
    public String getSkuKey() {
        return skuKey;
    }
    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }
    public String getSkuName() {
        return skuName;
    }
    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
    public int getPrizeScanNum() {
        return prizeScanNum;
    }
    public void setPrizeScanNum(int prizeScanNum) {
        this.prizeScanNum = prizeScanNum;
    }
    public String getPrizeType() {
        return prizeType;
    }
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }
    public int getEarnVpoints() {
        return earnVpoints;
    }
    public void setEarnVpoints(int earnVpoints) {
        this.earnVpoints = earnVpoints;
    }
    public double getEarnMoney() {
        return earnMoney;
    }
    public void setEarnMoney(double earnMoney) {
        this.earnMoney = earnMoney;
    }
    public String getPrizeName() {
        return prizeName;
    }
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
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
}
