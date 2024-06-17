/**
* 文件名：RandomUtils.java
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述: 生成随机字符串
* 修改人: 王靖越
* 修改时间：2014-03-04 10:41
* 修改内容：新增
*/
package com.dbt.framework.util;

import java.util.Random;

import jsr166y.ThreadLocalRandom;


abstract public class RandomUtils {
	public static final String ALL_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	

	/**
	 * 所有数字
	 */
	public static final String NUM_CHAR = "0123456789";
	public static final Random r = new Random();
	public static final char[] src = NUM_CHAR.toCharArray(); // 产生随机字符串 private static String
	
	
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
	 * 返回一个随机区段，例如：100:1:32:200:16:30，返回0的概率为100/(100+1+32+200+16+30)
	 * 
	 * @param conf
	 *            区段配置字符串
	 * @return 随机区段下标
	 */
	public static int randomSegment(String conf) {
		String[] tmp = StringUtil.split(conf, ":");
		int[] probability = new int[tmp.length];
		for (int i = 0; i < probability.length; i++)
			probability[i] = Integer.parseInt(tmp[i].trim());

		return randomSegment(probability);
	}

	/**
	 * 返回一个随机区段
	 * 
	 * @param probability
	 *            区段概率值
	 * @return 区段下标
	 */
	public static int randomSegment(int[] probability) {
		int total = 0;
		for (int i = 0; i < probability.length; i++) {
			total += probability[i];
			probability[i] = total;
		}
		int rand = (int) random(0, total - 1);
		for (int i = 0; i < probability.length; i++) {
			if (rand < probability[i]) {
				return i;
			}
		}
		return -1;
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
		int strLen=strRan.length();
		for (int i = 0; i < length; i++) {
			int index = (int) random(0,  strLen- 1);
			sb.append(strRan.charAt(index));
		}
		return sb.toString();
	}
    /**
     * 生成不重复随机字符串
     * @param 获取随机字符串的字符池
     * @param 生成随机字符串位数
     * @return 返回指定长度的随机不重复的字符串
     */
	public static String randomOnlyString(String strRan,int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(strRan.charAt((int) (Math.random() * 32)));	
        }
        return builder.toString();
    }
	
	/**
	 * 6位简单随机数
	 * @param length
	 * @return
	 */
	public static String randString(int length) {
		char[] buf = new char[length];
		int rnd;
		int srcLen=src.length;
		for (int i = 0; i < length; i++) {
			rnd = Math.abs(r.nextInt()) % srcLen;
			buf[i] = src[rnd];
		}
		return new String(buf);
	}
	
	public static void main(String[] args) {
//		String conf = "100:1:32:200:16:30";
//		System.out.println(randomSegment(conf));
//
//		System.out.println(random(0, 100));
//		System.out.println(randomString(NUM_CHAR,6)+">>>>");
//		System.out.println(randomString(ALL_CHAR,18));
		System.out.println(randString(3));
	}
}
