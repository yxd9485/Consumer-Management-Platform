package com.dbt.platform.waitActivation.bean;

/**
 * @author: LiangRunBin
 * @create-date: 2023/12/26 17:45
 * 待激活红包活动配置
 */
public class VpsWaitActivationPrizeCog {

    // 主键
    private String prizeKey;

    // 奖项名称
    private String prizeName;

    // 中出后有效天数
    private String cashPrizeEndDay;

    // 单人单日可中出个数
    private String everyoneLimitNum;
    
    // 每日可中出金额（元）
    private String dayMoneyLimit;
    
    // 累计可中出金额（元）
    private String totalMoneyLimit;
    
    // 每日已中出金额（元）
    private String dayMoney;
    
    // 累计已中出金额（元）
    private String totalMoney;

    // 激活产品图
    private String activeImgUrl;

    // 奖项说明
    private String prizeExplain;

    // 状态：0未启用、1已启用
    private String status;

    // 删除标识
    private String deleteFlag;

    // 创建时间
    private String createTime;

    // 创建人
    private String createUser;

    // 修改时间
    private String updateTime;

    // 修改人
    private String updateUser;

    // 查询用字段
    private String skuNames;
    private String keyword;
    private String stGmt;
    private String endGmt;
    private String[] skus;
    private String skuKey;

    public String getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }

    public String[] getSkus() {
        return skus;
    }

    public void setSkus(String[] skus) {
        this.skus = skus;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getStGmt() {
        return stGmt;
    }

    public void setStGmt(String stGmt) {
        this.stGmt = stGmt;
    }

    public String getEndGmt() {
        return endGmt;
    }

    public void setEndGmt(String endGmt) {
        this.endGmt = endGmt;
    }

    public String getSkuNames() {
        return skuNames;
    }

    public void setSkuNames(String skuNames) {
        this.skuNames = skuNames;
    }




    public String getPrizeKey() {
        return prizeKey;
    }

    public void setPrizeKey(String prizeKey) {
        this.prizeKey = prizeKey;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getCashPrizeEndDay() {
        return cashPrizeEndDay;
    }

    public void setCashPrizeEndDay(String cashPrizeEndDay) {
        this.cashPrizeEndDay = cashPrizeEndDay;
    }

    public String getEveryoneLimitNum() {
        return everyoneLimitNum;
    }

    public void setEveryoneLimitNum(String everyoneLimitNum) {
        this.everyoneLimitNum = everyoneLimitNum;
    }

    public String getPrizeExplain() {
        return prizeExplain;
    }

    public void setPrizeExplain(String prizeExplain) {
        this.prizeExplain = prizeExplain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

	public String getDayMoneyLimit() {
		return dayMoneyLimit;
	}

	public void setDayMoneyLimit(String dayMoneyLimit) {
		this.dayMoneyLimit = dayMoneyLimit;
	}

	public String getTotalMoneyLimit() {
		return totalMoneyLimit;
	}

	public void setTotalMoneyLimit(String totalMoneyLimit) {
		this.totalMoneyLimit = totalMoneyLimit; 
	}

	public String getDayMoney() {
		return dayMoney;
	}

	public void setDayMoney(String dayMoney) {
		this.dayMoney = dayMoney;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

    public String getActiveImgUrl() {
        return activeImgUrl;
    }

    public void setActiveImgUrl(String activeImgUrl) {
        this.activeImgUrl = activeImgUrl;
    }
}
