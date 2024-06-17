package com.dbt.framework.base.json.reply;
/**
* 文件名：BaseRequestReport<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述: 基础回复报文信息<br>
* 修改人: guchangpeng<br>
* 修改时间：2014-02-26 20:22:52<br>
* 修改内容：新增<br>
*/
public class BaseReplyReport {
	private String protocol="1.0.0";
	private String replyTime;
	private BaseReplyResult result;
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public BaseReplyResult getResult() {
		return result;
	}
	public void setResult(BaseReplyResult result) {
		this.result = result;
	} 
	
}
