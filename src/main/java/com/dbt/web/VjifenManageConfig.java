package com.dbt.web;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//v积分-管理平台配置
@Component
@ConfigurationProperties(prefix = "vjifen.manage")
public class VjifenManageConfig {
    //session免验证接口集合l
    private String sessionFreeUrl;
    //防sql注入免验证接口集合
    private String antiSqlFreeUrl;
    // 参数长度500接口配置
    private String parameterLengthLongUrl;
    //不验证参数长度接口配置
    private String antiParameterLengthFreeUrl;
    //mem-cache服务地址
    private String memCacheAddress;
    //mem-cache版本号
    private String memCacheVersion;

    //xxJob
    private String xxJobAddresses;
    private String xxJobAppName;
    private String xxJobIp;
    private int xxJobPort;
    private String xxJobAccessToken;
    private String xxJobLogPath;
    private int xxJobLogRetentionDays;

    //扫码登录
    private String authAppid;
    private String authSecret;
    private String authCallback;
    
    // 码源自动化
    private Map<String, String> autoQrcodeAppid;
    private Map<String, String> autoQrcodeSecret;
    
    // 单省区管理平台
    private String singleProjectServerName;


    public String[] getParameterLengthLongUrl() {
        return parameterLengthLongUrl.split(",");
    }

    public void setParameterLengthLongUrl(String parameterLengthLongUrl) {
        this.parameterLengthLongUrl = parameterLengthLongUrl;
    }

    public String[] getAntiParameterLengthFreeUrl() {
        return antiParameterLengthFreeUrl.split(",");
    }

    public void setAntiParameterLengthFreeUrl(String antiParameterLengthFreeUrl) {
        this.antiParameterLengthFreeUrl = antiParameterLengthFreeUrl;
    }

    public String[] getSessionFreeUrl() {
        return sessionFreeUrl.split(",");
    }

    public void setSessionFreeUrl(String sessionFreeUrl) {
        this.sessionFreeUrl = sessionFreeUrl;
    }

    public String[] getAntiSqlFreeUrl() {
        return antiSqlFreeUrl.split(",");
    }

    public void setAntiSqlFreeUrl(String antiSqlFreeUrl) {
        this.antiSqlFreeUrl = antiSqlFreeUrl;
    }

    public String getMemCacheAddress() {
        return memCacheAddress;
    }

    public void setMemCacheAddress(String memCacheAddress) {
        this.memCacheAddress = memCacheAddress;
    }

    public String getMemCacheVersion() {
        return memCacheVersion;
    }

    public void setMemCacheVersion(String memCacheVersion) {
        this.memCacheVersion = memCacheVersion;
    }

    public String getXxJobAddresses() {
        return xxJobAddresses;
    }

    public void setXxJobAddresses(String xxJobAddresses) {
        this.xxJobAddresses = xxJobAddresses;
    }

    public String getXxJobAppName() {
        return xxJobAppName;
    }

    public void setXxJobAppName(String xxJobAppName) {
        this.xxJobAppName = xxJobAppName;
    }

    public String getXxJobIp() {
        return xxJobIp;
    }

    public void setXxJobIp(String xxJobIp) {
        this.xxJobIp = xxJobIp;
    }

    public int getXxJobPort() {
        return xxJobPort;
    }

    public void setXxJobPort(int xxJobPort) {
        this.xxJobPort = xxJobPort;
    }

    public String getXxJobAccessToken() {
        return xxJobAccessToken;
    }

    public void setXxJobAccessToken(String xxJobAccessToken) {
        this.xxJobAccessToken = xxJobAccessToken;
    }

    public String getXxJobLogPath() {
        return xxJobLogPath;
    }

    public void setXxJobLogPath(String xxJobLogPath) {
        this.xxJobLogPath = xxJobLogPath;
    }

    public int getXxJobLogRetentionDays() {
        return xxJobLogRetentionDays;
    }

    public void setXxJobLogRetentionDays(int xxJobLogRetentionDays) {
        this.xxJobLogRetentionDays = xxJobLogRetentionDays;
    }

    public String getAuthAppid() {
        return authAppid;
    }

    public void setAuthAppid(String authAppid) {
        this.authAppid = authAppid;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }

    public String getAuthCallback() {
        return authCallback;
    }

    public void setAuthCallback(String authCallback) {
        this.authCallback = authCallback;
    }

    public Map<String, String> getAutoQrcodeAppid() {
        return autoQrcodeAppid;
    }

    public void setAutoQrcodeAppid(Map<String, String> autoQrcodeAppid) {
        this.autoQrcodeAppid = autoQrcodeAppid;
    }

    public Map<String, String> getAutoQrcodeSecret() {
        return autoQrcodeSecret;
    }

    public void setAutoQrcodeSecret(Map<String, String> autoQrcodeSecret) {
        this.autoQrcodeSecret = autoQrcodeSecret;
    }

    public String getSingleProjectServerName() {
        return singleProjectServerName;
    }

    public void setSingleProjectServerName(String singleProjectServerName) {
        this.singleProjectServerName = singleProjectServerName;
    }
}
