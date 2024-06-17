package com.dbt.framework.wechat.bean;
import org.apache.commons.lang.StringUtils;

/**
 * 消息模板Bean
 */
import com.dbt.framework.base.bean.BaseBean;
import com.dbt.framework.base.bean.BasicProperties;

@SuppressWarnings("serial")
public class SendTemplateMsg extends BasicProperties{

	/** 主键 **/
	private String infoKey;
	/** 企业主键 **/
	private String companyKey;
	/** 模板消息编号 **/
	private String templateNo;
	/** 推送消息类型：1.公众号，2.小程序 **/
	private String messageType;
	/** 模板消息类型：1.服务通知，2.单品推送，3.特殊推送 **/
	private String templateType;
	/** 模板标题 **/
	private String first;
	/** 模板关键字1 **/
	private String keyword1;
	/** 模板关键字2 **/
	private String keyword2;
	/** 模板关键字3 **/
	private String keyword3;
	/** 模板关键字4 **/
	private String keyword4;
	/** 模板关键字4 **/
	private String keyword5;
	/** 模板跳转类型：0无跳转，1跳转商城 **/
	private String urlType;
	/** 模板跳转URL **/
	private String url;
	/** 模板备注 **/
	private String remark;
	/** 描述信息 **/
	private String describes;
	/** 指定用户KEY **/
	private String userKey;
	/** 积分范围最小值 **/
	private String minVpoint;
	/** 积分范围最大值 **/
	private String maxVpoint;
	/** 金额范围最小值 **/
	private String minMoney;
	/** 金额范围最大值 **/
	private String maxMoney;
	/** 消息推送时间:yyyy-MM-dd HH:00:00 **/
	private String sendTime;
	/** 状态：0 未推送， 1已推送 **/
	private String status;
	/** 推送条数 **/
	private int sendCount;
	
	/** 扩展字段 ***/
	private String keyword;
	private String startDate;
	private String endDate;
	
	public SendTemplateMsg(){}
	public SendTemplateMsg(String queryParam){
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyword = paramAry.length > 0 ? paramAry[0] : "";
        this.startDate = paramAry.length > 1 ? paramAry[1] : "";
        this.endDate = paramAry.length > 2 ? paramAry[2] : "";
        this.status = paramAry.length > 3 ? paramAry[3] : "";
        this.userKey = paramAry.length > 4 ? paramAry[4] : "";
        this.templateType = paramAry.length > 5 ? paramAry[5] : "";
	}
	
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(String templateNo) {
		this.templateNo = templateNo;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getKeyword1() {
		return keyword1;
	}
	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}
	public String getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}
	public String getKeyword3() {
		return keyword3;
	}
	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}
	public String getKeyword4() {
		return keyword4;
	}
	public void setKeyword4(String keyword4) {
		this.keyword4 = keyword4;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDescribes() {
		return describes;
	}
	public void setDescribes(String describes) {
		this.describes = describes;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getMinVpoint() {
		return minVpoint;
	}
	public void setMinVpoint(String minVpoint) {
		this.minVpoint = minVpoint;
	}
	public String getMaxVpoint() {
		return maxVpoint;
	}
	public void setMaxVpoint(String maxVpoint) {
		this.maxVpoint = maxVpoint;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrlType() {
		return urlType;
	}
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public int getSendCount() {
		return sendCount;
	}
	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}
	public String getKeyword5() {
		return keyword5;
	}
	public void setKeyword5(String keyword5) {
		this.keyword5 = keyword5;
	}
	public String getMinMoney() {
		return minMoney;
	}
	public void setMinMoney(String minMoney) {
		this.minMoney = minMoney;
	}
	public String getMaxMoney() {
		return maxMoney;
	}
	public void setMaxMoney(String maxMoney) {
		this.maxMoney = maxMoney;
	}
}
