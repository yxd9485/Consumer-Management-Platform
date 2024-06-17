/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 *
 * Original Author: sunshunbo
 *
 * ChangeLog:
 * 2016-11-15 by sunshunbo create
 * </pre>
 */
package com.dbt.framework.util;

import org.apache.log4j.Logger;


/**
 * 性能监控工具类.
 */
public class PerformanceUtil {
    
    private static Logger log = Logger.getLogger(PerformanceUtil.class);
	 
 	/** The performance. */
 	private static ThreadLocal<Long> performance=new ThreadLocal<Long>();
	 
	 /**
 	 * Begin.
 	 */
 	public static void begin(){
		 long begin=System.currentTimeMillis();
		 performance.set(begin);
	 }
	 
	 /**
 	 * End.
 	 */
 	public static void end(){
		long begin= performance.get();
		long end =System.currentTimeMillis();
		log.info(Thread.currentThread().getId()+"业务运行到此共消耗"+(end-begin)+"毫秒");
	 }
 	
 	public static void end(String mark){
 		long begin= performance.get();
		long end =System.currentTimeMillis();
		log.info(Thread.currentThread().getId()+"线程业务"+mark+"运行到此共消耗"+(end-begin)+"毫秒");
 	}
 	
// 	public static void main(String[] args) {
// 		PerformanceUtil.begin();
// 		try {
//			Thread.currentThread().sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		PerformanceUtil.end();
//	}
}
