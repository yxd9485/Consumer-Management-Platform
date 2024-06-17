package com.dbt.platform.activityui.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * @Author bin.zhang
 **/
public class VpsAuthInfo extends BasicProperties {
    private String authKey;
    private String templateKey;
    private String isDefault;
    private String authType;
    private String authPage;
    private String authCondition;
    private String authStatus;
    /**
     * 暂时存放图片url
     */
    private String templateProperty;

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(String templateKey) {
        this.templateKey = templateKey;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthPage() {
        return authPage;
    }

    public void setAuthPage(String authPage) {
        this.authPage = authPage;
    }

    public String getAuthCondition() {
        return authCondition;
    }

    public void setAuthCondition(String authCondition) {
        this.authCondition = authCondition;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getTemplateProperty() {
        return templateProperty;
    }

    public void setTemplateProperty(String templateProperty) {
        this.templateProperty = templateProperty;
    }
}
