package com.dbt.platform.taoeasteregg.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 淘彩蛋规则
 * @author hanshimeng
 *
 */

@SuppressWarnings("serial")
public class VpsVcodeTaoEasterEggCog extends BasicProperties {

	/** 规则主键 **/
	private String infoKey;
	/** 规则编号 **/
	private String taoNo;
	/** 淘彩蛋规则名称 **/
	private String taoName;
	/** 规则开始时间 **/
	private String startDate;
	/** 入会口令集合 **/
	private String taoMemberOrder;
	/** 规则结束时间 **/
	private String endDate;
	/** 活动扫码计数类型：0单活动、1组合活动 **/
	private String ladderType;
	/** 活动keys，多个用逗号分隔 **/
	private String vcodeActivityKeys;
	/** 阶梯规则*/
	private String ladderRule;
    private String currDate;
    private String isBegin;
    
    public VpsVcodeTaoEasterEggCog() {
        super();
    }

    public VpsVcodeTaoEasterEggCog(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.taoName = paramAry.length > 0 ? paramAry[0] : "";
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getTaoNo() {
        return taoNo;
    }

    public void setTaoNo(String taoNo) {
        this.taoNo = taoNo;
    }

    public String getTaoName() {
        return taoName;
    }

    public void setTaoName(String taoName) {
        this.taoName = taoName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTaoMemberOrder() {
        return taoMemberOrder;
    }

    public void setTaoMemberOrder(String taoMemberOrder) {
        this.taoMemberOrder = taoMemberOrder;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLadderType() {
        return ladderType;
    }

    public void setLadderType(String ladderType) {
        this.ladderType = ladderType;
    }

    public String getVcodeActivityKeys() {
        return vcodeActivityKeys;
    }

    public void setVcodeActivityKeys(String vcodeActivityKeys) {
        this.vcodeActivityKeys = vcodeActivityKeys;
    }

    public String getLadderRule() {
        return ladderRule;
    }

    public void setLadderRule(String ladderRule) {
        this.ladderRule = ladderRule;
    }

    public String getCurrDate() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public String getIsBegin() {
        return isBegin;
    }

    public void setIsBegin(String isBegin) {
        this.isBegin = isBegin;
    }
    
}
