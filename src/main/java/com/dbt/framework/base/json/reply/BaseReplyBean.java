package com.dbt.framework.base.json.reply;

import java.util.List;

/**
 * 文件名：BaseReplyBean<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: 基础回复报文信息<br>
 * 修改人: guchangpeng<br>
 * 修改时间：2014-03-03<br>
 * 修改内容：新增<br>
 */
public class BaseReplyBean<T> {
	/** 返回多条 */
	private List<T> objList;
	/** 返回一条 */
	private T obj;
	/** 返回第二个对象 **/
	private Object obj1;
	/** 返回一个字符串 */
	private String args = null;

	public Object getObj1() {
		return obj1;
	}

	public void setObj1(Object obj1) {
		this.obj1 = obj1;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public List<T> getObjList() {
		return objList;
	}

	public void setObjList(List<T> objList) {
		this.objList = objList;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}
}
