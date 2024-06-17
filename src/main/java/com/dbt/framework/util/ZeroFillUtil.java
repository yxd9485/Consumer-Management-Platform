package com.dbt.framework.util;

/**
 * 补零工具类
 * @author jiquanwei
 *
 */
public class ZeroFillUtil {
	/**
	 * 补零工具类(目前只支持四位)
	 * @param str
	 * @return
	 */
	public static final String stringFillZero(String str){
		String reStr = "";
		String[] zeroArray = {"","000","00","0",""};
		reStr = zeroArray[str.length()] + str;
		return reStr;
	}
}
