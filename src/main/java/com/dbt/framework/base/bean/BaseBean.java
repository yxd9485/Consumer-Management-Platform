package com.dbt.framework.base.bean;

import java.io.Serializable;



import org.springframework.util.StringUtils;

import com.dbt.framework.util.DateUtil;
/**
* 文件名: BaseBean.java<br/>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br/>
* 描述: 基础bean，公用bean<br/>
* 修改人: 谷长鹏<br/>
* 修改时间：2014-02-21 18:50:05<br/>
* 修改内容：新增<br/>
*/
public class BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 顺序号 */
	private String sequenceNo;
	/** 创建日期 */
	private String creDate;
	/**  创建日期【新】 **/
	private String createTime;
	/** 创建人 */
	private String creUser;
	/** 修改时间 */
	private String updateTime;
	/** 修改人 */
	private String updateUser;
	/** 0未删除 1删除 */
	private String deleteFlag;
	/** */
	private int version;
	/** */
	private String environment;
	/** 备注 */
	private String remarks;
	
	public BaseBean(){
		this.creDate = DateUtil.getDateTime();
		this.updateTime = DateUtil.getDateTime();
		this.createTime = DateUtil.getDateTime();
		this.deleteFlag = Constant.DbDelFlag.noDel;
		this.version = Constant.version;
		this.environment = Constant.environment;
	}
	
	public BaseBean(String userId){
		this();
		this.creUser = userId;
		this.updateUser = userId;
	}
	
	public String trickParam(String param){
		if(!StringUtils.isEmpty(param)){
			param = param.replaceAll("\\\'", "");
			param = param.replaceAll("\\\"", "");
		}
		return param;
	}
	
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}
	public String getCreDate() {
		return creDate;
	}
	public void setCreDate(String creDate) {
		this.creDate = DateUtil.trimAfterPointer(creDate);
	}
	public String getCreUser() {
		return creUser;
	}
	public void setCreUser(String creUser) {
		this.creUser = creUser;
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
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setObject(String userId) {
		this.creUser = userId;
		this.updateUser = userId;
		this.creDate = DateUtil.getDateTime();
		this.updateTime = DateUtil.getDateTime();
		this.deleteFlag = Constant.DbDelFlag.noDel;
		this.version = Constant.version;
		this.environment = Constant.environment;
	}
}
