package com.dbt.platform.activity.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dbt.datasource.util.DbContextHolder;

public class VcodeActivityRebateRuleArea implements Serializable {
    private String areaCode;
    private String areaName;
    private boolean expireWarning;
    private boolean moneyWarning;
    private boolean vpointsWarning;
    private boolean prizeNumWarning;
    /** 蒙牛组织机构大区主键，第4级别 **/
    private String depRegionId;
    /** 蒙牛组织机构省区主键，第5级别 **/
    private String depProvinceId;
    /** 蒙牛经销商第一级主键 **/
    private String firstDealerKey;
    /** 蒙牛组织机构ids，格式：大区id,省区id,经销商id**/
    private String departmentIds;
    /** 青啤组织机构ids，格式：大区名称，二级办名称 **/
    private String organizationIds;
    
    private List<VcodeActivityRebateRuleCog> rebateRuleCogLst;
    
    public VcodeActivityRebateRuleArea() {
        super();
    }
    
    public VcodeActivityRebateRuleArea(String areaCode, 
            String areaName, String departmentIds, VcodeActivityRebateRuleCog rebateRuleCog) {
        this.areaCode = areaCode;
        this.areaName = areaName;
        if("mengniu".equals(DbContextHolder.getDBType())) {
        	this.departmentIds = departmentIds;
        }else {
        	this.organizationIds = departmentIds;
        }
        if (rebateRuleCogLst == null) {
            rebateRuleCogLst = new ArrayList<>();
        }
        this.depRegionId = rebateRuleCog.getDepRegionId();
        this.depProvinceId = rebateRuleCog.getDepProvinceId();
        this.firstDealerKey = rebateRuleCog.getFirstDealerKey();
        rebateRuleCogLst.add(rebateRuleCog);
    }
    
    public String getAreaCode() {
        return areaCode;
    }
    
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    
    public String getAreaName() {
        return areaName;
    }
    
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    
    public List<VcodeActivityRebateRuleCog> getRebateRuleCogLst() {
        return rebateRuleCogLst;
    }
    
    public void setRebateRuleCogLst(List<VcodeActivityRebateRuleCog> rebateRuleCogLst) {
        this.rebateRuleCogLst = rebateRuleCogLst;
    }

    public boolean isMoneyWarning() {
        return moneyWarning;
    }

    public void setMoneyWarning(boolean moneyWarning) {
        this.moneyWarning = moneyWarning;
    }

    public boolean isVpointsWarning() {
        return vpointsWarning;
    }

    public void setVpointsWarning(boolean vpointsWarning) {
        this.vpointsWarning = vpointsWarning;
    }

    public boolean isPrizeNumWarning() {
        return prizeNumWarning;
    }

    public void setPrizeNumWarning(boolean prizeNumWarning) {
        this.prizeNumWarning = prizeNumWarning;
    }

    public boolean isExpireWarning() {
        return expireWarning;
    }

    public void setExpireWarning(boolean expireWarning) {
        this.expireWarning = expireWarning;
    }

	public String getDepRegionId() {
		return depRegionId;
	}

	public void setDepRegionId(String depRegionId) {
		this.depRegionId = depRegionId;
	}

	public String getDepProvinceId() {
		return depProvinceId;
	}

	public void setDepProvinceId(String depProvinceId) {
		this.depProvinceId = depProvinceId;
	}

	public String getFirstDealerKey() {
		return firstDealerKey;
	}

	public void setFirstDealerKey(String firstDealerKey) {
		this.firstDealerKey = firstDealerKey;
	}

	public String getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(String departmentIds) {
		this.departmentIds = departmentIds;
	}

	public String getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(String organizationIds) {
		this.organizationIds = organizationIds;
	}
    
}
