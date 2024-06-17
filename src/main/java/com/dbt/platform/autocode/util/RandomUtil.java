package com.dbt.platform.autocode.util;

import jsr166y.ThreadLocalRandom;
/**
 * 项目名称：V积分V码生成工具类
 * 文件名：RandomUtil
 * 作者：jiquanwei
 * 创建时间：2014-06-16 19:26:20
 * 功能描述: 随机数生成规则
 * 版   本: V 1.0
 */
public class RandomUtil {
	/**
	 * 生成min(包括)到max(包括)范围的随机数
	 * 
	 * @param min
	 *            随机数最小值
	 * @param max
	 *            随机数最大值
	 * @return min(包括)到max(包括)范围的随机数
	 */
	public static long random(long min, long max) {
		return Math.round(ThreadLocalRandom.current().nextDouble()
				* (max - min) + min);
	}
	
	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 *            生成字符串的长度
	 * @return 指定长度的随机字符串
	 */
	public static String randomString(String strRan,int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int index = (int) random(0, strRan.length() - 1);
			sb.append(strRan.charAt(index));
		}
		return sb.toString();
	}
}
