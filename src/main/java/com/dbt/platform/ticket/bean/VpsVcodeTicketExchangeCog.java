package com.dbt.platform.ticket.bean;


import org.apache.commons.lang.StringUtils;

public class VpsVcodeTicketExchangeCog {

    private String infoKey;
    private String ticketNo;
    private String startDate;
    private String endDate;
    private String exchangeEndTime;
    private Long vpointsCog;
    private Long exchangeLimit;
    private String deleteFlag;
    private String createTime;
    private String createUser;
    private String updateTime;
    private String updateUser;
    private String ticketName;
    private String jumpId;
    private String ticketUrl;
    private Long minVpointCog;
    private Long maxVpointCog;
    private Long exchangeEndDay;
    private String ticketExchangeCount;
    private String ticketExchangeConsumeVpoints;

    public VpsVcodeTicketExchangeCog() {

    }

    public VpsVcodeTicketExchangeCog(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.ticketNo = paramAry.length > 0 ? paramAry[0] : "";
        this.ticketName = paramAry.length > 1 ? paramAry[1] : "";
        this.jumpId = paramAry.length > 2 ? paramAry[2] : "";
        this.ticketUrl = paramAry.length > 3 ? paramAry[3] : "";
        this.minVpointCog = paramAry.length > 4 ? Long.parseLong(StringUtils.isBlank(paramAry[4])?"0":paramAry[4]) : null;
        this.maxVpointCog = paramAry.length > 5 ? Long.parseLong(StringUtils.isBlank(paramAry[5])?"0":paramAry[5]) : null;
    }

    public String getTicketExchangeCount() {
        return ticketExchangeCount;
    }

    public void setTicketExchangeCount(String ticketExchangeCount) {
        this.ticketExchangeCount = ticketExchangeCount;
    }

    public String getTicketExchangeConsumeVpoints() {
        return ticketExchangeConsumeVpoints;
    }

    public void setTicketExchangeConsumeVpoints(String ticketExchangeConsumeVpoints) {
        this.ticketExchangeConsumeVpoints = ticketExchangeConsumeVpoints;
    }

    public Long getExchangeEndDay() {
        return exchangeEndDay;
    }

    public void setExchangeEndDay(Long exchangeEndDay) {
        this.exchangeEndDay = exchangeEndDay;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
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

    public String getExchangeEndTime() {
        return exchangeEndTime;
    }

    public void setExchangeEndTime(String exchangeEndTime) {
        this.exchangeEndTime = exchangeEndTime;
    }

    public Long getVpointsCog() {
        return vpointsCog;
    }

    public void setVpointsCog(Long vpointsCog) {
        this.vpointsCog = vpointsCog;
    }

    public Long getExchangeLimit() {
        return exchangeLimit;
    }

    public void setExchangeLimit(Long exchangeLimit) {
        this.exchangeLimit = exchangeLimit;
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

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getJumpId() {
        return jumpId;
    }

    public void setJumpId(String jumpId) {
        this.jumpId = jumpId;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public Long getMinVpointCog() {
        return minVpointCog;
    }

    public void setMinVpointCog(Long minVpointCog) {
        this.minVpointCog = minVpointCog;
    }

    public Long getMaxVpointCog() {
        return maxVpointCog;
    }

    public void setMaxVpointCog(Long maxVpointCog) {
        this.maxVpointCog = maxVpointCog;
    }
}