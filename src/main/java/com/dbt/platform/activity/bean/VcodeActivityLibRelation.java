package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 文件名: VcodeActivityLibRelation.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: V码活动活动与码库关联<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-22 10:37:12<br>
 * 修改内容：新增<br>
 */
public class VcodeActivityLibRelation extends BasicProperties {

	/** **/
	private static final long serialVersionUID = 1L;
	/** */
	private String infoKey;
	/** 主键 */
	private String vcodeActivityKey;
	/** */
	private String batchKey;
	/** 活动唯一标识码 */
	private String vcodeUniqueCode;
	/** 码库名称 */
	private String libName;
	/** V码数量 */
	private Long vcodeCounts;

	public VcodeActivityLibRelation() {
		super();
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	public String getInfoKey() {
		return infoKey;
	}

	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}

	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}

	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}

	public String getBatchKey() {
		return batchKey;
	}

	public void setVcodeUniqueCode(String vcodeUniqueCode) {
		this.vcodeUniqueCode = vcodeUniqueCode;
	}

	public String getVcodeUniqueCode() {
		return vcodeUniqueCode;
	}

	public void setLibName(String libName) {
		this.libName = libName;
	}

	public String getLibName() {
		return libName;
	}

	public void setVcodeCounts(Long vcodeCounts) {
		this.vcodeCounts = vcodeCounts;
	}

	public Long getVcodeCounts() {
		return vcodeCounts;
	}
}
