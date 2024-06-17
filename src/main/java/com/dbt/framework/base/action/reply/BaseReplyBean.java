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

import java.util.List;


/**
 * <pre>
 * 基础回复报文信息.
 * </pre>
 *
 * @param <T> the generic type
 * @author 孙顺博  2015-1-16
 */
public class BaseReplyBean<T> {
	
	/** 返回多条. */
	private List<T> objList;
	
	/** 返回一条. */
	private T obj;
	
	/** 返回一个字符串. */
	private String args = null;
	
	/**
	 * <pre>
	 * Gets the args.
	 * </pre>
	 *
	 * @return the string
	 * @author 孙顺博  2015-1-16
	 */
	public String getArgs() {
		return args;
	}
	
	/**
	 * <pre>
	 * Sets the args.
	 * </pre>
	 *
	 * @param args the args
	 * @author 孙顺博  2015-1-16
	 */
	public void setArgs(String args) {
		this.args = args;
	}
	
	/**
	 * <pre>
	 * Gets the obj list.
	 * </pre>
	 *
	 * @return the list
	 * @author 孙顺博  2015-1-16
	 */
	public List<T> getObjList() {
		return objList;
	}
	
	/**
	 * <pre>
	 * Sets the obj list.
	 * </pre>
	 *
	 * @param objList the obj list
	 * @author 孙顺博  2015-1-16
	 */
	public void setObjList(List<T> objList) {
		this.objList = objList;
	}
	
	/**
	 * <pre>
	 * Gets the obj.
	 * </pre>
	 *
	 * @return the t
	 * @author 孙顺博  2015-1-16
	 */
	public T getObj() {
		return obj;
	}
	
	/**
	 * <pre>
	 * Sets the obj.
	 * </pre>
	 *
	 * @param obj the obj
	 * @author 孙顺博  2015-1-16
	 */
	public void setObj(T obj) {
		this.obj = obj;
	}
}
