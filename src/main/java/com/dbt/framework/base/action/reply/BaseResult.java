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
 * result信息.
 * </pre>
 *
 * @author 韩士猛  2015-9-16
 */
public class BaseResult<T> {
	
	/** result信息**/
	private BaseReplyResult result;
	
	/** 提示消息. */
	private T reply = null;
	
	/**服务器时间**/
	private long replyTime = System.currentTimeMillis();
	
	public BaseReplyResult getResult() {
		if(result == null){
			result = new BaseReplyResult();
		}
		return result;
	}

	public void setResult(BaseReplyResult result) {
		this.result = result;
	}

	public T getReply() {
		return reply;
	}

	public void setReply(T reply) {
		this.reply = reply;
	}

	public long getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(long replyTime) {
		this.replyTime = replyTime;
	}

    /**
     * 初始化返回结果
     * 
     * @param code
     * @param businessCode
     * @param msg
     * @return
     */
    public BaseResult<T> initReslut(String code, String businessCode, String msg) {
        this.getResult().setResult(code, businessCode, msg);
        return this;
    }
	
}
