/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 *
 * Original Author: sunshunbo
 *
 * ChangeLog:
 * 2016-10-31 by sunshunbo create
 * </pre>
 */
package com.dbt.framework.util;


/**
 * The Class PackRecordRouterException.
 */
public class PackRecordRouterException extends RuntimeException {

	/**  *. */
	private static final long serialVersionUID = 1L;

	/**
	 * The Interface EXCEPTION_MSG.
	 */
	public static interface EXCEPTION_MSG {
		
		/**  分表异常 *. */
		static String SPLICT_EXCEPTION_MSG = "获取分表后缀异常！";
		
		/**  分表范围过大异常 *. */
		static String SPLICT_RANGE_EXCEPTION_MSG = "分表范围过大！";
	}

	/**
	 * Instantiates a new pack record router exception.
	 */
	public PackRecordRouterException() {
		super();
	}

	/**
	 * Instantiates a new pack record router exception.
	 *
	 * @param msg the msg
	 */
	public PackRecordRouterException(String msg) {
		super();
	}

}
