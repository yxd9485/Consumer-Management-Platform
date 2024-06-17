package com.dbt.platform.enterprise.bean;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 统计信息
 */
public class StatInfo extends BasicProperties {
    private String statInfoKey;
    private String statName;
    private String remark;
    private String statType;
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String[] skuKey;
    private String updateTime;
    private String updateUser;
    private List<StatisticsSku> skuList;
    private String projectServerName;
    private String serverName;

    public StatInfo(){}
    public StatInfo(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.projectServerName = paramAry.length > 0 ? paramAry[0] : "";
        this.statName = paramAry.length > 1 ? paramAry[1] : "";
    }

    public String getProjectServerName() {
        return projectServerName;
    }

    public void setProjectServerName(String projectServerName) {
        this.projectServerName = projectServerName;
    }

    public List<StatisticsSku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<StatisticsSku> skuList) {
        this.skuList = skuList;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getStatInfoKey() {
        return statInfoKey;
    }

    public void setStatInfoKey(String statInfoKey) {
        this.statInfoKey = statInfoKey;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String[] getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String[] skuKey) {
        this.skuKey = skuKey;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
