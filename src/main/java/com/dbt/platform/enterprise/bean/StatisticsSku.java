package com.dbt.platform.enterprise.bean;

import com.dbt.framework.base.bean.BasicProperties;

public  class StatisticsSku extends BasicProperties {
    private String infoKey;
    private String skuKey;
    private String statInfoKey;
    private String skuName;
    private String serverName;
    private String projectServerName;
    private String skuWord;
    private String statName;

    public String getSkuWord() {
        return skuWord;
    }

    public void setSkuWord(String skuWord) {
        this.skuWord = skuWord;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }

    public String getStatInfoKey() {
        return statInfoKey;
    }

    public void setStatInfoKey(String statInfoKey) {
        this.statInfoKey = statInfoKey;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getProjectServerName() {
        return projectServerName;
    }

    public void setProjectServerName(String projectServerName) {
        this.projectServerName = projectServerName;
    }

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }
}