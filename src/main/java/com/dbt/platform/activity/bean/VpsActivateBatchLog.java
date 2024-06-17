package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BaseBean;

/**
 * 批次激活日志bean
 * @author hanshimeng
 *
 */
public class VpsActivateBatchLog extends BaseBean{

	private static final long serialVersionUID = 8639150611067276633L;
	
	private String batchKey;
	private String batchDesc;
	private String activateOpenid;
	private String activateUserName;
	private String activatePhone;
	private String activateFactoryName;
	private String activateTime;
	private String startDate;
	private String endDate;
	
	public VpsActivateBatchLog(){}
	public VpsActivateBatchLog(String batchKey, String batchDesc, String activateOpenid, String activateUserName, 
			String activatePhone, String activateFactoryName, String activateTime, String startDate, String endDate){
		this.endDate = endDate;
		this.batchKey = batchKey;
		this.batchDesc = batchDesc;
		this.startDate = startDate;
		this.activateTime = activateTime;
		this.activatePhone = activatePhone;
		this.activateOpenid = activateOpenid;
		this.activateUserName = activateUserName;
		this.activateFactoryName = activateFactoryName;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getActivateUserName() {
		return activateUserName;
	}

	public void setActivateUserName(String activateUserName) {
		this.activateUserName = activateUserName;
	}

	public String getActivateFactoryName() {
		return activateFactoryName;
	}

	public void setActivateFactoryName(String activateFactoryName) {
		this.activateFactoryName = activateFactoryName;
	}

	public String getActivateOpenid() {
        return activateOpenid;
    }

    public void setActivateOpenid(String activateOpenid) {
        this.activateOpenid = activateOpenid;
    }

    public String getActivatePhone() {
        return activatePhone;
    }

    public void setActivatePhone(String activatePhone) {
        this.activatePhone = activatePhone;
    }

    public String getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(String activateTime) {
        this.activateTime = activateTime;
    }

    public String getBatchDesc() {
        return batchDesc;
    }
    
    public void setBatchDesc(String batchDesc) {
        this.batchDesc = batchDesc;
    }
    
    public String getBatchKey() {
		return batchKey;
	}
	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}
}