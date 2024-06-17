package com.dbt.platform.invitationActivity.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

public class VpsVcodeInvitationCode extends BasicProperties {

    private static final long serialVersionUID = 8399962748969129310L;

    private String infoKey; //'主键',
    private String activityKey; //'活动主键',
    private String orderKey; //'订单主键',
    private String orderName; //'订单名称',
    private String codeContent; //'二维码内容',
    private String codeNo; //'二维码编号',
    private String codeType; //'二维码类型',二维码类型0门店1导购
    private String registrantKey; //'注册人用户key',
    private String registerTime; //'注册时间',
    private String registrantShopName; //'注册人门店名称',
    private String registrantName; //'注册人姓名',
    private String registrantPhoneNum; //'注册人手机号',

    //扩展字段
    private String tabsFlag; //'1店主码 2导购码',

    public VpsVcodeInvitationCode(){}

    public VpsVcodeInvitationCode(String queryParam, String activityKey, String tabsFlag){
        this.activityKey = activityKey;
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        if ("1".equals(tabsFlag)){
            this.codeType = "0";
            this.orderName = paramAry.length > 0 ? paramAry[0] : "";
            this.codeNo = paramAry.length > 1 ? paramAry[1] : "";
            this.registrantShopName = paramAry.length > 2 ? paramAry[2] : "";
            this.registrantPhoneNum = paramAry.length > 3 ? paramAry[3] : "";
        }else{
            this.codeType = "1";
            this.orderName = paramAry.length > 0 ? paramAry[0] : "";
            this.codeNo = paramAry.length > 1 ? paramAry[1] : "";
            this.registrantName = paramAry.length > 2 ? paramAry[2] : "";
            this.registrantPhoneNum = paramAry.length > 3 ? paramAry[3] : "";
        }
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getRegistrantKey() {
        return registrantKey;
    }

    public void setRegistrantKey(String registrantKey) {
        this.registrantKey = registrantKey;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegistrantShopName() {
        return registrantShopName;
    }

    public void setRegistrantShopName(String registrantShopName) {
        this.registrantShopName = registrantShopName;
    }

    public String getRegistrantName() {
        return registrantName;
    }

    public void setRegistrantName(String registrantName) {
        this.registrantName = registrantName;
    }

    public String getRegistrantPhoneNum() {
        return registrantPhoneNum;
    }

    public void setRegistrantPhoneNum(String registrantPhoneNum) {
        this.registrantPhoneNum = registrantPhoneNum;
    }

    public String getTabsFlag() {
        return tabsFlag;
    }

    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }
}
