package com.dbt.platform.enterprise.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

import java.util.List;

/**
 * @author RoyFu
 * @createTime 2016年4月21日 下午12:34:32
 * @description
 */

@SuppressWarnings("serial")
public class SkuInfo extends BasicProperties {

	private String skuKey;
	private String companyKey;
	private String brandKey;
	private String categoryKey;
	private String commodityCode;
	// 原SPU（分组概念）
	private String spuKey;
	// 新SPU
	private String spuInfoKey;
	private String skuName;
	private String shortName;
	/** 品牌统一编码 **/
	private String unificationCode;
	/** 品牌统一名称 **/
	private String unificationName;
	/** 年份 **/
	private String skuYear;
	/** 品牌 **/
	private String skuBrand;
	/** 建议零售价#.## **/
	private String suggestPrice;
	private String validDay;
	private String skuLogo;

	/** 产品码类型 1消费者 2终端 3消费者以及终端 4无*/
	private String operationsType;
	/** 终端获取消费者权益 0不可获取 1可获取*/
	private String canGetConsumerReward;
	/** 消费者后扫权益 0不可获取 1可获取*/
	private String canConsumerScanAfterTerminal;
	/** 消费者后扫权益时间限制 */
	private int scanAfterTimeLimit;

	/**
	 * 品牌介绍页
	 */
	private String brandIntroduceUrl;
    /** 产品类型：0瓶、1罐、2箱  3其他*/
    private String skuType;
	private String skuDesc;
	/** 日志推送标志：0不推送、1推送*/
	private String pushMsgFlag;
	/** 合同内产品: 0否、1是*/
	private String contractFlag;
	/** 码源生成累计编号 **/
	private String autocodeNo;
	/** 容积 **/
	private  String volume;
	/** 码源生成前缀**/
	private String autocodePrefix;
	private String projectServerName;
	private String serverName;
	private String skuWord;
	private String Channel;
	private List<SkuChannelInfo> skuChannel;
	private String statName;
	private String specificationDesc;
	
	
	private String sku_code; //蒙牛高印-产品编码
	private String sku_name; //蒙牛高印-产品名称
	private String short_name; //蒙牛高印-产品简称
	private String sku_brand; //蒙牛高印-产品品类
	private String bar_code; //蒙牛高印-条形码69码
	private String spec_desc; //蒙牛高印-产品规格
	private String update_time; //蒙牛高印-产品信息更新时间
	
	/** 促销激励*/
	private String skuPromotionMoney;
    /** 码中台SKU*/
    private String mztSku;
    /** 爱创码中台SKU*/
    private String acMztSku;

	/**
	 * 嘉华sku
	 */
	private String jhSku;
    /** 终端sku集合 **/
    private String zdSku;

	/**
	 * 最酒sku
	 */
	private String zjSku;

    /**
     * 青啤通用名称
     */
    private String itemName;

	public String getBrandIntroduceUrl() {
		return brandIntroduceUrl;
	}

	public void setBrandIntroduceUrl(String brandIntroduceUrl) {
		this.brandIntroduceUrl = brandIntroduceUrl;
	}

	public String getZjSku() {
		return zjSku;
	}

	public void setZjSku(String zjSku) {
		this.zjSku = zjSku;
	}


	public String getJhSku() {
		return jhSku;
	}

	public void setJhSku(String jhSku) {
		this.jhSku = jhSku;
	}

	public String getOperationsType() {
		return operationsType;
	}

	public void setOperationsType(String operationsType) {
		this.operationsType = operationsType;
	}

	public String getCanGetConsumerReward() {
		return canGetConsumerReward;
	}

	public void setCanGetConsumerReward(String canGetConsumerReward) {
		this.canGetConsumerReward = canGetConsumerReward;
	}

	public String getCanConsumerScanAfterTerminal() {
		return canConsumerScanAfterTerminal;
	}

	public void setCanConsumerScanAfterTerminal(String canConsumerScanAfterTerminal) {
		this.canConsumerScanAfterTerminal = canConsumerScanAfterTerminal;
	}

	public int getScanAfterTimeLimit() {
		return scanAfterTimeLimit;
	}

	public void setScanAfterTimeLimit(int scanAfterTimeLimit) {
		this.scanAfterTimeLimit = scanAfterTimeLimit;
	}


	public static class SkuChannelInfo{
		private String skuChannelCode;
		private String skuChannelName;

		public String getSkuChannelCode() {
			return skuChannelCode;
		}

		public void setSkuChannelCode(String skuChannelCode) {
			this.skuChannelCode = skuChannelCode;
		}

		public String getSkuChannelName() {
			return skuChannelName;
		}

		public void setSkuChannelName(String skuChannelName) {
			this.skuChannelName = skuChannelName;
		}
	}
	
	public SkuInfo(){
	    
	}
	
	public SkuInfo(String queryParam){
	    String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
	    this.skuName = paramAry.length > 0 ? paramAry[0] : "";
		this.skuKey = paramAry.length > 1 ? paramAry[1] : "";
	//	this.commodityCode = paramAry.length > 1 ? paramAry[1] : "";
	    this.validDay = paramAry.length > 2 ? paramAry[2] : "";
	    this.skuType = paramAry.length > 3 ? paramAry[3] : "";
	    this.operationsType = paramAry.length > 4 ? paramAry[4] : "";
	}


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getZdSku() {
		return zdSku;
	}

	public void setZdSku(String zdSku) {
		this.zdSku = zdSku;
	}

	public String getSpuInfoKey() {
		return spuInfoKey;
	}

	public void setSpuInfoKey(String spuInfoKey) {
		this.spuInfoKey = spuInfoKey;
	}

	public String getUnificationCode() {
		return unificationCode;
	}

	public void setUnificationCode(String unificationCode) {
		this.unificationCode = unificationCode;
	}

	public String getUnificationName() {
		return unificationName;
	}

	public void setUnificationName(String unificationName) {
		this.unificationName = unificationName;
	}

	public String getSpuKey() {
        return spuKey;
    }

    public void setSpuKey(String spuKey) {
        this.spuKey = spuKey;
    }

    public String getAutocodeNo() {
		return autocodeNo;
	}

	public void setAutocodeNo(String autocodeNo) {
		this.autocodeNo = autocodeNo;
	}

	public String getValidDay() {
		return validDay;
	}

	public void setValidDay(String validDay) {
		this.validDay = validDay;
	}

	public String getSkuKey() {
		return skuKey;
	}

	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getBrandKey() {
		return brandKey;
	}

	public void setBrandKey(String brandKey) {
		this.brandKey = brandKey;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}

	public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuLogo() {
		return skuLogo;
	}

	public void setSkuLogo(String skuLogo) {
		this.skuLogo = skuLogo;
	}

	public String getSkuType() {
        return skuType;
    }

    public void setSkuType(String skuType) {
        this.skuType = skuType;
    }

    public String getSkuDesc() {
		return skuDesc;
	}

	public void setSkuDesc(String skuDesc) {
		this.skuDesc = skuDesc;
	}

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getPushMsgFlag() {
        return pushMsgFlag;
    }

    public void setPushMsgFlag(String pushMsgFlag) {
        this.pushMsgFlag = pushMsgFlag;
    }

	public String getContractFlag() {
        return contractFlag;
    }

    public void setContractFlag(String contractFlag) {
        this.contractFlag = contractFlag;
    }

    public String getSkuYear() {
		return skuYear;
	}

	public void setSkuYear(String skuYear) {
		this.skuYear = skuYear;
	}

	public String getSkuBrand() {
		return skuBrand;
	}

	public void setSkuBrand(String skuBrand) {
		this.skuBrand = skuBrand;
	}

	public String getSuggestPrice() {
		return suggestPrice;
	}

	public void setSuggestPrice(String suggestPrice) {
		this.suggestPrice = suggestPrice;
	}

	public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getAutocodePrefix() {
		return autocodePrefix;
	}

	public void setAutocodePrefix(String autocodePrefix) {
		this.autocodePrefix = autocodePrefix;
	}

	public String getProjectServerName() {
		return projectServerName;
	}

	public void setProjectServerName(String projectServerName) {
		this.projectServerName = projectServerName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getSkuWord() {
		return skuWord;
	}

	public void setSkuWord(String skuWord) {
		this.skuWord = skuWord;
	}

	public List<SkuChannelInfo> getSkuChannel() {
		return skuChannel;
	}

	public void setSkuChannel(List<SkuChannelInfo> skuChannel) {
		this.skuChannel = skuChannel;
	}

	public String getChannel() {
		return Channel;
	}

	public void setChannel(String channel) {
		Channel = channel;
	}

    public String getStatName() {
        return statName;
    }

    public void setStatName(String statName) {
        this.statName = statName;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getSku_brand() {
        return sku_brand;
    }

    public void setSku_brand(String sku_brand) {
        this.sku_brand = sku_brand;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getSpec_desc() {
        return spec_desc;
    }

    public void setSpec_desc(String spec_desc) {
        this.spec_desc = spec_desc;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getSpecificationDesc() {
        return specificationDesc;
    }

    public void setSpecificationDesc(String specificationDesc) {
        this.specificationDesc = specificationDesc;
    }

    public String getSkuPromotionMoney() {
        return skuPromotionMoney;
    }

    public void setSkuPromotionMoney(String skuPromotionMoney) {
        this.skuPromotionMoney = skuPromotionMoney;
    }

    public String getMztSku() {
        return mztSku;
    }

    public void setMztSku(String mztSku) {
        this.mztSku = mztSku;
    }

    public String getAcMztSku() {
        return acMztSku;
    }

    public void setAcMztSku(String acMztSku) {
        this.acMztSku = acMztSku;
    }
}
