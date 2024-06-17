package com.dbt.vpointsshop.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
/**
 * 电子券批次
 * @author zhaohongtao
 *2017年12月11日
 */
public class VpointsCouponBatch extends BasicProperties{
	private static final long serialVersionUID = 1L;
	private String batchKey;
	private String companyKey;
	private String batchName;
	private int batchCount;
	private String batchTable;
	private String companyName;
	public VpointsCouponBatch(){}
	public VpointsCouponBatch(String queryParam){
		if (!StringUtils.isEmpty(queryParam)) {
			queryParam = trickParam(queryParam);
			String[] params = queryParam.split(",");
			this.batchName = params.length > 0 ? !StringUtils.isEmpty(params[0]) ? params[0]: null : null;
			this.companyKey = params.length > 1 ? !StringUtils.isEmpty(params[1]) ? params[1]: null : null;
		}
	}
	public String getBatchKey() {
		return batchKey;
	}
	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}
	public String getCompanyKey() {
		return companyKey;
	}
	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public int getBatchCount() {
		return batchCount;
	}
	public void setBatchCount(int batchCount) {
		this.batchCount = batchCount;
	}
	public String getBatchTable() {
		return batchTable;
	}
	public void setBatchTable(String batchTable) {
		this.batchTable = batchTable;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
