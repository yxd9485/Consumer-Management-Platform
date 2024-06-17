package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 实物奖bean
 * @author zhaohongtao
 *2017年11月16日
 */
public class VpointsPrizeBean extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String infoKey;
	private String exchangeId;//兑换ID
	private String exchangeChannel;//兑换渠道(兑换、抽奖、扫码)
	private String companyKey;//省区
	private String categoryType;//商品分类
	private String goodsId;//商品ID
	private String openid;
	private String userKey;//用户ID
	private int exchangeNum;//兑换个数
	private String earnTime;//兑换时间
	private String categoryParent;//商品分类
	private String goodsName;//商品名称
	private String realName;
	private String phoneNum;
	private String address;
	private String idCard;
	
	private String earnStartTime;
	private String earnEndTime;
	private String firstName;
	private String secondName;
	private String companyName;
	private String nickName;
	private String skuName;
	private String prizeVcode;
	public VpointsPrizeBean(){}
	public VpointsPrizeBean(String queryParam){
		if (!StringUtils.isEmpty(queryParam)) {
			queryParam = trickParam(queryParam);
			String[] params = queryParam.split(",");
			this.exchangeChannel = params.length > 0 ? !StringUtils.isEmpty(params[0]) ? params[0]: null : null;
			this.categoryParent = params.length > 1 ? !StringUtils.isEmpty(params[1]) ? params[1]: null : null;
			this.categoryType = params.length > 2 ? !StringUtils.isEmpty(params[2]) ? params[2]: null : null;
			this.goodsName = params.length > 3 ? !StringUtils.isEmpty(params[3]) ? params[3]: null : null;
			this.userKey = params.length > 4 ? !StringUtils.isEmpty(params[4]) ? params[4]: null : null;
			this.earnStartTime = params.length > 5 ? !StringUtils.isEmpty(params[5]) ? params[5]: null : null;
			this.earnEndTime = params.length > 6 ? !StringUtils.isEmpty(params[6]) ? params[6]+" 23:59:59": null : null;
			this.companyKey = params.length > 7 ? !StringUtils.isEmpty(params[7]) ? params[7]: null : null;
		}
	}
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getExchangeId() {
		return exchangeId;
	}
	public void setExchangeId(String exchangeId) {
		this.exchangeId = exchangeId;
	}
	public String getExchangeChannel() {
		return exchangeChannel;
	}
	public void setExchangeChannel(String exchangeChannel) {
		this.exchangeChannel = exchangeChannel;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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
	public int getExchangeNum() {
		return exchangeNum;
	}
	public void setExchangeNum(int exchangeNum) {
		this.exchangeNum = exchangeNum;
	}
	public String getEarnTime() {
		return earnTime;
	}
	public void setEarnTime(String earnTime) {
		this.earnTime = earnTime;
	}
	public String getCategoryParent() {
		return categoryParent;
	}
	public void setCategoryParent(String categoryParent) {
		this.categoryParent = categoryParent;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getEarnStartTime() {
		return earnStartTime;
	}
	public void setEarnStartTime(String earnStartTime) {
		this.earnStartTime = earnStartTime;
	}
	public String getEarnEndTime() {
		return earnEndTime;
	}
	public void setEarnEndTime(String earnEndTime) {
		this.earnEndTime = earnEndTime;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSecondName() {
		return secondName;
	}
	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getPrizeVcode() {
		return prizeVcode;
	}
	public void setPrizeVcode(String prizeVcode) {
		this.prizeVcode = prizeVcode;
	}
}
