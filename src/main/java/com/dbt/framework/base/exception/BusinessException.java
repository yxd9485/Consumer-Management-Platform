package com.dbt.framework.base.exception;

/**
 * 自定义消息异常
 * @author hongen
 *
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 7315450680428339465L;
    
    private String errCode;
    
    public BusinessException() {
        super();
    }
    
    public BusinessException(String errMsg) {
        super(errMsg);
    }
    
    public BusinessException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

}
