package com.dbt.platform.publish.bean;
import org.apache.commons.lang.StringUtils;
/**
 * 
 * @ClassName: VpsActRule 
 * @Description: 活动规则bean
 * @author: bin.zhang
 * @date: 2019年12月20日 下午2:22:39
 */
public class VpsActRule {
	//关键词
private String keyword;
//活动key
private String actRuleKey;
private String vcodeActivityKey;
private String vcodeActivityName;
//sku的key
private String skuKey;
//sku名称
private String skuNm;
//分辨率
private String resolution;
//限时规则开始时间
private String limitStGmt;
//限时规则结束时间
private String limitEndGmt;
//限时活动规则时间
private String limitGmt;
//修改人
private String modUser;
//修改开始时间
private String modStGmt;
//修改结束时间
private String modEndGmt;
//修改时间
private String modGmt;
//限时规则的图片url
private String limitUrl;
//默认规则图片url
private String defaultUrl;
//创建时间
private String creGmt;
//创建时间
private String creStGmt;
//创建时间
private String creEndGmt;
//删除标识
private String isDel;
	private String defaultPicWidth;
	private String defaultPicHeight;
	private String defaultPicX;
	private String defaultPicY;
	private String limitPicWidth;
	private String limitPicHeight;
	private String limitPicX;
	private String limitPicY;
	private String brandCode;
//入参转换
public VpsActRule(){};
public VpsActRule(String queryParam){
	String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
	this.keyword = paramAry.length > 0 ? paramAry[0] : "";
    this.limitStGmt = paramAry.length > 1 ? paramAry[1] : "";
    this.limitEndGmt = paramAry.length > 2 ? paramAry[2] : "";
    this.modStGmt = paramAry.length > 3 ? paramAry[3] : "";
    this.modEndGmt = paramAry.length > 4 ? paramAry[4] : "";
}


public String getActRuleKey() {
	return actRuleKey;
}
public void setActRuleKey(String actRuleKey) {
	this.actRuleKey = actRuleKey;
}
public String getVcodeActivityKey() {
    return vcodeActivityKey;
}
public void setVcodeActivityKey(String vcodeActivityKey) {
    this.vcodeActivityKey = vcodeActivityKey;
}
public String getVcodeActivityName() {
    return vcodeActivityName;
}
public void setVcodeActivityName(String vcodeActivityName) {
    this.vcodeActivityName = vcodeActivityName;
}
public String getSkuKey() {
	return skuKey;
}
public void setSkuKey(String skuKey) {
	this.skuKey = skuKey;
}
public String getSkuNm() {
	return skuNm;
}
public void setSkuNm(String skuNm) {
	this.skuNm = skuNm;
}
public String getResolution() {
	return resolution;
}
public void setResolution(String resolution) {
	this.resolution = resolution;
}
public String getLimitStGmt() {
	return limitStGmt;
}
public void setLimitStGmt(String limitStGmt) {
	this.limitStGmt = limitStGmt;
}
public String getModUser() {
	return modUser;
}
public void setModUser(String modUser) {
	this.modUser = modUser;
}
public String getModGmt() {
	return modGmt;
}
public void setModGmt(String modGmt) {
	this.modGmt = modGmt;
}
public String getLimitUrl() {
	return limitUrl;
}
public void setLimitUrl(String limitUrl) {
	this.limitUrl = limitUrl;
}
public String getDefaultUrl() {
	return defaultUrl;
}
public void setDefaultUrl(String defaultUrl) {
	this.defaultUrl = defaultUrl;
}
public String getCreGmt() {
	return creGmt;
}
public void setCreGmt(String creGmt) {
	this.creGmt = creGmt;
}
public String getCreStGmt() {
	return creStGmt;
}
public void setCreStGmt(String creStGmt) {
	this.creStGmt = creStGmt;
}
public String getCreEndGmt() {
	return creEndGmt;
}
public void setCreEndGmt(String creEndGmt) {
	this.creEndGmt = creEndGmt;
}
public String getIsDel() {
	return isDel;
}
public void setIsDel(String isDel) {
	this.isDel = isDel;
}


public String getLimitEndGmt() {
	return limitEndGmt;
}


public void setLimitEndGmt(String limitEndGmt) {
	this.limitEndGmt = limitEndGmt;
}


public String getLimitGmt() {
	return limitGmt;
}


public void setLimitGmt(String limitGmt) {
	this.limitGmt = limitGmt;
}
public String getKeyword() {
	return keyword;
}
public void setKeyword(String keyword) {
	this.keyword = keyword;
}
public String getModStGmt() {
	return modStGmt;
}
public void setModStGmt(String modStGmt) {
	this.modStGmt = modStGmt;
}
public String getModEndGmt() {
	return modEndGmt;
}
public void setModEndGmt(String modEndGmt) {
	this.modEndGmt = modEndGmt;
}

	public String getDefaultPicWidth() {
		return defaultPicWidth;
	}

	public void setDefaultPicWidth(String defaultPicWidth) {
		this.defaultPicWidth = defaultPicWidth;
	}

	public String getDefaultPicHeight() {
		return defaultPicHeight;
	}

	public void setDefaultPicHeight(String defaultPicHeight) {
		this.defaultPicHeight = defaultPicHeight;
	}

	public String getDefaultPicX() {
		return defaultPicX;
	}

	public void setDefaultPicX(String defaultPicX) {
		this.defaultPicX = defaultPicX;
	}

	public String getDefaultPicY() {
		return defaultPicY;
	}

	public void setDefaultPicY(String defaultPicY) {
		this.defaultPicY = defaultPicY;
	}

	public String getLimitPicWidth() {
		return limitPicWidth;
	}

	public void setLimitPicWidth(String limitPicWidth) {
		this.limitPicWidth = limitPicWidth;
	}

	public String getLimitPicHeight() {
		return limitPicHeight;
	}

	public void setLimitPicHeight(String limitPicHeight) {
		this.limitPicHeight = limitPicHeight;
	}

	public String getLimitPicX() {
		return limitPicX;
	}

	public void setLimitPicX(String limitPicX) {
		this.limitPicX = limitPicX;
	}

	public String getLimitPicY() {
		return limitPicY;
	}

	public void setLimitPicY(String limitPicY) {
		this.limitPicY = limitPicY;
	}
    public String getBrandCode() {
        return brandCode;
    }
    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
}
