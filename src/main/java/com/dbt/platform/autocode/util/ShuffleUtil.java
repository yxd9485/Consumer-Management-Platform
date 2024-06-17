package com.dbt.platform.autocode.util;

import com.dbt.datasource.util.DbContextHolder;

/**
 * 混排工具类
 **/
public class ShuffleUtil {
	/**
	 * 第一次混排
	 **/
	public static String shuffleMethod(String source, String shuffleKey) {
	    
	    // 长城五星码源不能做混排操作
	    if ("zhongLWX".equals(DbContextHolder.getDBType()) || "zuijiu".equals(DbContextHolder.getDBType())) {
			return source;
		}
	    
		StringBuffer resultStr = new StringBuffer();
		if (source != "" && shuffleKey != "") {
			char[] soucreArr = source.toCharArray();
			int size = shuffleKey.length() - 1;
			int index = 0;
			for (int i = 0, len = soucreArr.length; i < len; i++) {
				resultStr.append(soucreArr[i]);
				if (i % 2 != 0) {
					resultStr.append(shuffleKey.charAt(index));
					if (size >= index) {
						index++;
					} else {
						index = 0;
					}
				}
			}

		}
		return resultStr.toString();
	}

	/**
	 * 第二次 混排
	 */
	public static String shuffleMethodBase64(String source, String shuffleKey) {
		StringBuffer resultStr = new StringBuffer();
		if (source != "" && shuffleKey != "") {
			for (int i = 0; i < shuffleKey.length(); i++) {
				resultStr.append(source.substring(i, i + 1)).append(shuffleKey.charAt(i));
			}
			String lastStr = source.substring(shuffleKey.length());
			for (int t = 0; t < lastStr.length(); t++) {
				resultStr.append(
						source.substring(shuffleKey.length() + t, shuffleKey.length() + t + 1))
						.append(shuffleKey.charAt(t));
			}
		}
		return resultStr.toString();
	}

	/**
	 * @param source
	 *            混排字符串
	 * @param shuffleKey
	 *            第一次混排结果 广西青啤，第二次 混排 length(source)<length(shuffleKey)
	 */
	public static String shuffleMethodBase64ByGXQP(String source, String shuffleKey) {
		StringBuffer resultStr = new StringBuffer("");
		if (source != "" && shuffleKey != "" && source.length() < shuffleKey.length()) {
			for (int i = 0; i < source.length(); i++) {
				resultStr.append(source.charAt(i)).append(shuffleKey.substring(i, i + 1));
			}

			int souceLen = source.length();

			String lastStr = shuffleKey.substring(souceLen);
			for (int t = 0; t < lastStr.length(); t++) {
				resultStr.append(source.charAt(t)).append(
						shuffleKey.substring(souceLen + t, souceLen + t + 1));
			}
		}
		return resultStr.toString();
	}
	
	public static void main(String[] args) {
		ShuffleUtil shuffleUtil = new ShuffleUtil();
		System.out.println(shuffleUtil.shuffleMethod("12345678901", "vjifen"));
		System.out.println(shuffleUtil.shuffleMethodBase64("12345678901", "vjifen"));
		System.out.println(shuffleUtil.shuffleMethodBase64ByGXQP("12345678901", "vjifen"));
		//System.out.println(shuffleUtil.shuffleMethod("12345678901", "asdfgh"));
		//System.out.println(shuffleUtil.shuffleMethod("12345678901", "asdfghj"));
	}
}
