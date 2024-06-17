package com.dbt.framework.base.action.reply;

public class BaseDataResult<T> {

    private String errcode;
    private String errmsg;
    private T data;
    
    public String getErrcode() {
        return errcode;
    }
    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }
    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    
    public BaseDataResult<T> initReslut(String errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        return this;
    }
    
}
