package com.dbt.platform.question.bean;



import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

 /**
  * @description 订单</br>
  */
public class VpsQuestionnaireOrder extends BasicProperties {

    private static final long serialVersionUID = -3167445713560241216L;
    
    // 主键
    private String infoKey;
    // 订单号
    private String orderNo;
    private String userKey;
    private String prizeVcode;
    private String operationType;
    public VpsQuestionnaireOrder(){
    	
    }
    public VpsQuestionnaireOrder(String queryParam) {
    	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.orderNo = paramAry.length > 0 ? paramAry[0] : "";
        this.operationType = paramAry.length > 1 ? paramAry[1] : "";
    }
    
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getPrizeVcode() {
		return prizeVcode;
	}
	public void setPrizeVcode(String prizeVcode) {
		this.prizeVcode = prizeVcode;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	
}
