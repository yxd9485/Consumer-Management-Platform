package com.dbt.framework.base.bean;

import java.io.Serializable;

import org.springframework.util.StringUtils;

import com.dbt.framework.util.DateUtil;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2014年8月15日 9:48:09
 * @description 新版Base属性
 */

public class BasicProperties implements Serializable {

	private static final long serialVersionUID = 6380233430039097647L;
	
	/** 排序号 SEQUENCE_NUM */
	private Integer sequenceNum;
	/** 删除标识 DELETE_FLAG */
	private String deleteFlag;
	/** 创建时间 CREATE_TIME */
	private String createTime;
	/** 创建人 CREATE_USER */
	private String createUser;
	/** 修改时间 UPDATE_TIME */
	private String updateTime;
	/** 修改人 UPDATE_USER */
	private String updateUser;
	/** 分表后缀类型 **/
	private String splitTableSuffix;
	
	/**
	 * Default constructor
	 */
	public BasicProperties(){
		this.deleteFlag = Constant.DbDelFlag.noDel;
	}
	
	/**
	 * constructor with current user
	 * @param userKey
	 */
	public BasicProperties(String userKey){
		this();
		this.createTime = DateUtil.getDateTime();
		this.updateTime = DateUtil.getDateTime();
		this.createUser = userKey;
		this.updateUser = userKey;
	}
	
	/**
	 * constructor with current user and seqNum
	 * @param userKey
	 * @param sequenceNum
	 */
	public BasicProperties(String userKey, int sequenceNum){
		this(userKey);
		this.sequenceNum = sequenceNum;
	}
	
	/**
	 * constructor with seqNum
	 * @param sequenceNum
	 */
	public BasicProperties(int sequenceNum){
		this();
		this.sequenceNum = sequenceNum;
	}
	
	/**
	 * refill parameters with userKey
	 * @param userKey
	 */
	public void fillFields(String userKey){
	    this.createTime = DateUtil.getDateTime();
	    this.updateTime = DateUtil.getDateTime();
	    this.createUser = userKey;
	    this.updateUser = userKey;
	}
	
	/**
	 * refill parameters with userKey
	 * @param userKey
	 */
	public void fillUpdateFields(String userKey){
		this.updateTime = DateUtil.getDateTime();
		this.updateUser = userKey;
	}
	
	/**
	 * refill parameters with userKey and sequenceNum
	 * @param userKey
	 * @param sequenceNum
	 */
	public void fillFields(String userKey, int sequenceNum){
		fillFields(userKey);
		this.sequenceNum = sequenceNum;
	}
	
	/**
	 * remove ' or " from parameters
	 * @param param
	 * @return
	 */
	public String trickParam(String param){
		if(!StringUtils.isEmpty(param)){
			param = param.replaceAll("\\\'", "");
			param = param.replaceAll("\\\"", "");
		}
		return param;
	}

	public Integer getSequenceNum() {
		return sequenceNum;
	}

	public void setSequenceNum(Integer sequenceNum) {
		this.sequenceNum = sequenceNum;
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

	public String getSplitTableSuffix() {
		return splitTableSuffix;
	}

	public void setSplitTableSuffix(String splitTableSuffix) {
		this.splitTableSuffix = splitTableSuffix;
	}
	
	
}
