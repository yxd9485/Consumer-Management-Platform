package com.dbt.framework.token.bean;

import java.io.Serializable;

public class TokenInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String access_token; // 获取到的凭证
    private String token_type; // 凭证类型
    private long expires_in; // 有效时⻓
    private long created_at; // 凭证⽣成时间
    
    // 扩展参数
    private String serverName;
    private String clientId;
    
    public String getAccess_token() {
        return access_token;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
    public String getToken_type() {
        return token_type;
    }
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
    public long getExpires_in() {
        return expires_in;
    }
    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }
    public long getCreated_at() {
        return created_at;
    }
    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
