package com.dbt.platform.vdhInvitation.bean;

import com.dbt.framework.base.bean.BasicProperties;
import org.apache.commons.lang.StringUtils;

public class VpsVcodeVdhInvitationOrder extends BasicProperties {

    private static final long serialVersionUID = 9038033493946091134L;

    private String infoKey; //'主键',
    private String companyKey; //'企业主键',
    private String orderName; //'订单名称',
    private Integer orderNum; //'订单数量',
    private String status; //'订单状态0未生成1进行中2已生成3生成失败',

    //扩展字段
    private String isBegin; //'待上线 已上线 已下线',
    private String minCreateTime; //'创建时间开始值',
    private String maxCreateTime; //'创建时间结束值',

    public VpsVcodeVdhInvitationOrder(){}

    public VpsVcodeVdhInvitationOrder(String queryParam){
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

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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
