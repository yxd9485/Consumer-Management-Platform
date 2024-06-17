package com.dbt.platform.vdhInvitation.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

public class VpsVcodeVdhInvitationCode extends BasicProperties {

    private static final long serialVersionUID = -6279217487072537780L;

    private String infoKey; //'主键',
    private String companyKey; //'企业主键',
    private String orderKey; //'订单主键',
    private String orderName; //'订单名称',
    private String codeNo; //'邀请码编号',
    private String codeContent; //'邀请码内容',
    private String codeUrl; //'邀请码图片地址',
    private String registrantKey; //'注册门店Key',
    private String registrantShopName; //'注册门店名称',
    private String registrantPhoneNum; //'注册手机号',
    private String registerTime; //'注册时间',

    //扩展字段
    private String tabsFlag; //'1店主码 2导购码',

    public VpsVcodeVdhInvitationCode(){}

    public VpsVcodeVdhInvitationCode(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.orderName = paramAry.length > 0 ? paramAry[0] : "";
        this.codeNo = paramAry.length > 1 ? paramAry[1] : "";
        this.registrantKey = paramAry.length > 2 ? paramAry[2] : "";
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

    public String getCodeNo() {
        return codeNo;
    }

    public void setCodeNo(String codeNo) {
        this.codeNo = codeNo;
    }

    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getRegistrantKey() {
        return registrantKey;
    }

    public void setRegistrantKey(String registrantKey) {
        this.registrantKey = registrantKey;
    }

    public String getRegistrantShopName() {
        return registrantShopName;
    }

    public void setRegistrantShopName(String registrantShopName) {
        this.registrantShopName = registrantShopName;
    }

    public String getRegistrantPhoneNum() {
        return registrantPhoneNum;
    }

    public void setRegistrantPhoneNum(String registrantPhoneNum) {
        this.registrantPhoneNum = registrantPhoneNum;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getTabsFlag() {
        return tabsFlag;
    }

    public void setTabsFlag(String tabsFlag) {
        this.tabsFlag = tabsFlag;
    }
}
