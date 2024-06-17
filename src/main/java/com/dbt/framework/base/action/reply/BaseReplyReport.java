/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 * 
 * Original Author: 孙顺博
 * 
 * ChangeLog:
 * 2015-1-16 by 孙顺博 create
 * </pre>
 */
package com.dbt.framework.base.action.reply;

/**
 * <pre>
 * 基础回复报文信息.
 * </pre>
 *
 * @author 孙顺博 2015-1-16
 */
public class BaseReplyReport {
	
	/** The protocol. */
	private String protocol = "1.0.0";
	
	/** The reply time. */
	private String replyTime;
	
	/** The result. */
	private BaseReplyResult result;
	
	/** The command type. */
	private String commandType;
	
	public String getCommandType() {
		return commandType;
	}

	
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	/**
	 * <pre>
	 * Gets the protocol.
	 * </pre>
	 *
	 * @return the string
	 * @author 孙顺博 2015-1-16
	 */
	public String getProtocol() {
		return protocol;
	}
	
	/**
	 * <pre>
	 * Sets the protocol.
	 * </pre>
	 *
	 * @param protocol the protocol
	 * @author 孙顺博 2015-1-16
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * <pre>
	 * Gets the reply time.
	 * </pre>
	 *
	 * @return the string
	 * @author 孙顺博 2015-1-16
	 */
	public String getReplyTime() {
		return replyTime;
	}
	
	/**
	 * <pre>
	 * Sets the reply time.
	 * </pre>
	 *
	 * @param replyTime the reply time
	 * @author 孙顺博 2015-1-16
	 */
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	
	/**
	 * <pre>
	 * Gets the result.
	 * </pre>
	 *
	 * @return the base reply result
	 * @author 孙顺博 2015-1-16
	 */
	public BaseReplyResult getResult() {
		return result;
	}
	
	/**
	 * <pre>
	 * Sets the result.
	 * </pre>
	 *
	 * @param result the result
	 * @author 孙顺博 2015-1-16
	 */
	public void setResult(BaseReplyResult result) {
		this.result = result;
	}
	
}
