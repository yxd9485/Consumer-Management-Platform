package com.dbt.platform.ticket.bean;

import org.apache.commons.lang.StringUtils;

public class VpsVcodeTicketUserRecord {
    private String infoKey;
    private String ticketName;
    private Long consumeVpoints;
    private String userName;
    private String phoneNumber;
    private String earnTime;
    private Long minVpointCog;
    private Long maxVpointCog;
    private String startTime;
    private String endTime;

    public VpsVcodeTicketUserRecord(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.userName = paramAry.length > 0 ? paramAry[0] : "";
        this.phoneNumber = paramAry.length > 1 ? paramAry[1] : "";
        this.minVpointCog = paramAry.length > 2 ? Long.parseLong(StringUtils.isBlank(paramAry[2])?"0":paramAry[2]) : null;
        this.maxVpointCog = paramAry.length > 3 ? Long.parseLong(StringUtils.isBlank(paramAry[3])?"0":paramAry[3]) : null;
        this.startTime = paramAry.length > 4 ? paramAry[4] : "";
        this.endTime = paramAry.length > 5 ? paramAry[5] : "";
    }

    public VpsVcodeTicketUserRecord() {

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Long getConsumeVpoints() {
        return consumeVpoints;
    }

    public void setConsumeVpoints(Long consumeVpoints) {
        this.consumeVpoints = consumeVpoints;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEarnTime() {
        return earnTime;
    }

    public void setEarnTime(String earnTime) {
        this.earnTime = earnTime;
    }
}
