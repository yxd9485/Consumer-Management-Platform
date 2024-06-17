package com.dbt.platform.vdhInvitation.bean;

public class VpsTerminalGroupInfo {
    /** 终端主键 */
    private String terminalGroupKey;
    /** 企业key companyKey */
    private String companyKey;
    /** 终端分组名称 */
    private String terminalGroup;
    /** 门店keys */
    private String terminalKeys;
    /** 类型：0门店同一分组，1单店单分组 */
    private String groupType;
    /** 是否删除 deleteFlag */
    private String deleteFlag;
    /** 创建时间 createTime */
    private String createTime;
    /** 创建人 createUser */
    private String createUser;
    /** 修改时间 updateTime */
    private String updateTime;
    /** 修改人 updateUser */
    private String updateUser;
    /** 企业名称 companyName */
    private String companyName;
    /**统计数量**/
    private String groupCount;
    /**备注**/
    private String remark;

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getGroupCount () {
        return groupCount;
    }

    public void setGroupCount (String groupCount) {
        this.groupCount = groupCount;
    }

    public String getCompanyName () {
        return companyName;
    }

    public void setCompanyName (String companyName) {
        this.companyName = companyName;
    }

    public String getTerminalGroupKey () {
        return terminalGroupKey;
    }

    public void setTerminalGroupKey (String terminalGroupKey) {
        this.terminalGroupKey = terminalGroupKey;
    }

    public String getCompanyKey () {
        return companyKey;
    }

    public void setCompanyKey (String companyKey) {
        this.companyKey = companyKey;
    }

    public String getTerminalGroup () {
        return terminalGroup;
    }

    public void setTerminalGroup (String terminalGroup) {
        this.terminalGroup = terminalGroup;
    }

    public String getTerminalKeys() {
        return terminalKeys;
    }

    public void setTerminalKeys(String terminalKeys) {
        this.terminalKeys = terminalKeys;
    }

    public String getDeleteFlag () {
        return deleteFlag;
    }

    public void setDeleteFlag (String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreateTime () {
        return createTime;
    }

    public void setCreateTime (String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser () {
        return createUser;
    }

    public void setCreateUser (String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateTime () {
        return updateTime;
    }

    public void setUpdateTime (String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser () {
        return updateUser;
    }

    public void setUpdateUser (String updateUser) {
        this.updateUser = updateUser;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
