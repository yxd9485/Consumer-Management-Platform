package com.dbt.platform.activity.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 文件名: VcodeQrcodePackInfo.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: V码二维码码包信息bean<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-22 10:22:00<br>
 * 修改内容：新增<br>
 */
public class VcodeQrcodePackInfo extends BasicProperties {

	/** **/
	private static final long serialVersionUID = 1L;
	/** */
	private String packKey;
	/** */
	private String batchKey;
	/** */
	private String packCode;
	/** 起始时间 */
	private String startDate;
	/** 结束时间 */
	private String endDate;
	/** 二维码数量 */
	private Long qrcodeAmounts;

	/** v码活动key **/
	private String vcodeActivityKey;

	/** 多个包编码，以逗号“,”分割 **/
	private String packCodeArr;

	public VcodeQrcodePackInfo() {
		super();
	}

	public String getPackKey() {
		return packKey;
	}

	public void setPackKey(String packKey) {
		this.packKey = packKey;
	}

	public String getBatchKey() {
		return batchKey;
	}

	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}

	public String getPackCode() {
		return packCode;
	}

	public void setPackCode(String packCode) {
		this.packCode = packCode;
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

	public Long getQrcodeAmounts() {
		return qrcodeAmounts;
	}

	public void setQrcodeAmounts(Long qrcodeAmounts) {
		this.qrcodeAmounts = qrcodeAmounts;
	}

	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}

	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}

	public String getPackCodeArr() {
		return packCodeArr;
	}

	public void setPackCodeArr(String packCodeArr) {
		this.packCodeArr = packCodeArr;
	}

}
