package com.dbt.platform.invitationActivity.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

public class VpsVcodeInvitationOrder extends BasicProperties {

    private static final long serialVersionUID = -1541463123783114983L;

    private String infoKey; //'主键',
    private String companyKey; //'企业主键',
    private String activityKey; //'活动主键',
    private String orderName; //'订单名称',
    private String qrcodeType; //'二维码类型',
    private Integer qrcodeNum; //'二维码数量',
    private String status; //'订单状态',

    //扩展字段
    private String isBegin; //'待上线 已上线 已下线',
    private String minCreateTime; //'创建时间开始值',
    private String maxCreateTime; //'创建时间结束值',

    public VpsVcodeInvitationOrder(){}

    public VpsVcodeInvitationOrder(String queryParam, String activityKey){
        this.activityKey = activityKey;
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.orderName = paramAry.length > 0 ? paramAry[0] : "";
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

    public String getActivityKey() {
        return activityKey;
    }

    public void setActivityKey(String activityKey) {
        this.activityKey = activityKey;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getQrcodeType() {
        return qrcodeType;
    }

    public void setQrcodeType(String qrcodeType) {
        this.qrcodeType = qrcodeType;
    }

    public Integer getQrcodeNum() {
        return qrcodeNum;
    }

    public void setQrcodeNum(Integer qrcodeNum) {
        this.qrcodeNum = qrcodeNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }

    public String getMinCreateTime() {
        return minCreateTime;
    }

    public void setMinCreateTime(String minCreateTime) {
        this.minCreateTime = minCreateTime;
    }

    public String getMaxCreateTime() {
        return maxCreateTime;
    }

    public void setMaxCreateTime(String maxCreateTime) {
        this.maxCreateTime = maxCreateTime;
    }
}
