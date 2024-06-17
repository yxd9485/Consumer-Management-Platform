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
 * @author 孙顺博  2015-1-16
 */
public class BaseReplyResult {
	
	/** 状态码，0：成功 1：失败. */
	private String code = "0";
	
	/** 业务状态码. */
	private String businessCode;
	
	/** 提示消息. */
	private String msg = null;
	
	/** 校验状态，默认为false失败*/
	private boolean validFlage = false;
	
	/** 批次是否过期，默认为true表示已过期*/
	private boolean batchFlage = false;

    /**
     * Set result.
     *
     * @param code the code
     * @param businessCode the business code
     * @param msg the msg
     */
    public void setResult(String code, String businessCode, String msg) {
        this.code = code;
        this.businessCode = businessCode;
        this.msg = msg;
    }

	/**
	 * <pre>
	 * Gets the g.
	 * </pre>
	 *
	 * @return the g
	 * @author 孙顺博  2015-1-16
	 */
	public String getMsg() {
		return msg;
	}
	
	/**
	 * <pre>
	 * Sets the g.
	 * </pre>
	 *
	 * @param msg the new g
	 * @author 孙顺博  2015-1-16
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * <pre>
	 * Gets the code.
	 * </pre>
	 *
	 * @return the string
	 * @author 孙顺博  2015-1-16
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * <pre>
	 * Sets the code.
	 * </pre>
	 *
	 * @param code the code
	 * @author 孙顺博  2015-1-16
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * <pre>
	 * Gets the business code.
	 * </pre>
	 *
	 * @return the string
	 * @author 孙顺博  2015-1-16
	 */
	public String getBusinessCode() {
		return businessCode;
	}
	
	/**
	 * <pre>
	 * Sets the business code.
	 * </pre>
	 *
	 * @param businessCode the business code
	 * @author 孙顺博  2015-1-16
	 */
	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

    public boolean isValidFlage() {
        return validFlage;
    }
    
    public void setValidFlage(boolean validFlage) {
        this.validFlage = validFlage;
    }

    /**批次是否过期，默认为true表示已过期*/
    public boolean isBatchFlage() {
        return batchFlage;
    }

    /**批次是否过期，默认为true表示已过期*/
    public void setBatchFlage(boolean batchFlage) {
        this.batchFlage = batchFlage;
    }
	
}
