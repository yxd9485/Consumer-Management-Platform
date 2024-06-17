package com.dbt.framework.base.json.reply;

/**
 * 文件名：BaseReplyResult<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: result信息<br>
 * 修改人: guchangpeng<br>
 * 修改时间：2014-02-26 20:22:52<br>
 * 修改内容：新增<br>
 */
public class BaseReplyResult {
	/**
	 * 状态码，0：成功 1：失败
	 */
	private String code;
	/**
	 * 业务状态码
	 */
	private String businessCode;
	/**
	 * 提示消息
	 */
	private String msg = null;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
}
