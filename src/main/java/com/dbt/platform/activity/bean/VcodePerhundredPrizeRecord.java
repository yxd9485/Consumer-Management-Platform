package com.dbt.platform.activity.bean;
import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 逢百中奖明细Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VcodePerhundredPrizeRecord extends BasicProperties {
	
	/** 主键KEY **/
	private String infoKey;
	/** 企业KEY **/
	private String companyKey;
	/** 规则主键 **/
	private String perhundredKey;
	/** 用户openid **/
	private String openid;
	/** 用户主键 **/
	private String userKey;
	/** 微信昵称 **/
	private String nickName;
	/** 中奖码 **/
	private String qrcodeContent;
	/** 中奖倍数 **/
	private int multiple;
	/** 中奖积分 **/
	private int earnVpoints;
	/** 中奖金额 **/
	private double earnMoney;
	
	/** 扩充字段 **/
	/** 开始时间 **/
	private String startDate;
	/** 结束时间 **/
	private String endDate;
	
	public VcodePerhundredPrizeRecord(){}
	public VcodePerhundredPrizeRecord(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.nickName = paramAry.length > 0 ? paramAry[0] : "";
        this.qrcodeContent = paramAry.length > 1 ? paramAry[1] : "";
        this.startDate = paramAry.length > 2 ? paramAry[2] : "";
        this.endDate = paramAry.length > 3 ? paramAry[3] : "";
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
	public String getPerhundredKey() {
		return perhundredKey;
	}
	public void setPerhundredKey(String perhundredKey) {
		this.perhundredKey = perhundredKey;
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
	public int getMultiple() {
		return multiple;
	}
	public void setMultiple(int multiple) {
		this.multiple = multiple;
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
