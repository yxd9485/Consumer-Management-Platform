package com.dbt.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.springframework.util.StringUtils;

public class SpellUtil {

	/**
	 * 将汉字转换为拼音首字母.
	 * 
	 * @param src
	 *            中文字符串
	 * @return String
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> getPinYinCapital(String src) {
		if (src.contains("-") || src.contains("(") || src.contains(" ") || src.contains("'")) {
			src = src.replaceAll("-", "").replace("(", "").replace(")", "").replaceAll(" ", "")
					.replaceAll("'", "");
		}
		char[] zi = src.toCharArray();// 字的数组
		String[] capital = new String[zi.length]; // 每个字的读音首字母组成的字符串数组
		for (int i = 0; i < zi.length; i++) {
			String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(zi[i]); // 每个字读音的拼音数组
			String capString = "";
			if (pinyin != null) {
				for (int j = 0; j < pinyin.length; j++) {
					String duyin = pinyin[j];// 一个读音
					String s = String.valueOf(duyin.toCharArray()[0]); // 一个首字母
					capString += s;
				}
			} else {
				capString += zi[i];
			}

			capital[i] = capString;
		}
		// 去除重复的读音首字母
		for (int i = 0; i < capital.length; i++) {
			capital[i] = distinct(capital[i]);
		}
		// 整理为ArrayList
		ArrayList inputList = new ArrayList();
		for (int i = 0; i < capital.length; i++) {
			ArrayList<String> inList = new ArrayList<String>();
			// System.out.println(capital[i]);
			char[] a = capital[i].toCharArray();
			for (int j = 0; j < a.length; j++) {
				inList.add(String.valueOf(a[j]));
			}
			inputList.add(inList);
		}
		// 生成首字母组合的字符串(笛卡尔积)
		ArrayList dikaer = descartes(inputList);
		// 整理成一层的ArrayList，并加上原来的名字
		ArrayList<String> resultList = new ArrayList<String>();
		resultList.add(src);
		for (int i = 0; i < dikaer.size(); i++) {
			ArrayList al = null;
			try {
				al = (ArrayList) dikaer.get(i);
			} catch (Exception e) {
				al = new ArrayList();
				al.add((String) dikaer.get(i));
			}
			String s = "";
			for (int j = 0; j < al.size(); j++) {
				s = s + al.get(j);
			}
			resultList.add(s);
		}
		return resultList;
	}

	/**
	 * 去除字符串中重复的char
	 * 
	 * @param str
	 * @return
	 */
	private static String distinct(String str) {
		char[] s = str.toCharArray();
		String string = new String();
		for (int i = 0; i < s.length; i++) {
			/*
			 * string.contains(String.valueOf(s[i])
			 * 
			 * contains（）方法中的参数为一个CharSequence接口；
			 * string类实现了CharSequence接口所以可以使用String.valueof() 来获得字符串做为参数传递
			 */
			if (!string.contains(String.valueOf(s[i]))) {
				string += String.valueOf(s[i]);
			}
		}
		return string;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList descartes(ArrayList inputList) {
		ArrayList MainList = (ArrayList) inputList.get(0);
		for (int i = 1; i < inputList.size(); i++) {

			ArrayList addList = null;
			try {
				addList = (ArrayList) inputList.get(i);
			} catch (Exception e) {
				addList = new ArrayList();
				addList.add((String) inputList.get(i));
			}
			ArrayList temp = new ArrayList();
			for (int j = 0; j < MainList.size(); j++) // 每次先计算两个集合的笛卡尔积，然后用其结果再与下一个计算
			{
				for (int k = 0; k < addList.size(); k++) {
					ArrayList cut = new ArrayList();
					if (MainList.get(j) instanceof ArrayList) {
						cut.addAll((ArrayList) MainList.get(j));
					} else {
						cut.add(MainList.get(j));
					}
					if (addList.get(k) instanceof ArrayList) {
						cut.addAll((ArrayList) addList.get(k));
					} else {
						cut.add(addList.get(k));
					}
					temp.add(cut);
				}
			}
			MainList = temp;
		}
		return MainList;
	}

	public static String escapeSequence(String src) {
		String result = "";
		if (!StringUtils.isEmpty(src)) {
			char[] chs = src.toCharArray();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < chs.length; i++) {
				if (String.valueOf(chs[i]).equals("'")) {
					buffer.append("\\");
				}
				buffer.append(chs[i]);
			}
			result = buffer.toString();
		}
		return result;
	}

	public static String generateSequence(List<String> resultList) {
		String results = resultList.get(1).toLowerCase();
		results = StringFilter(results);
		return results;
	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		// 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		String regEx = "[_`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static void main(String[] args) throws Exception {
		//String src1 = "【奥古特】500*10mL";

		//System.out.println(escapeSequence(src1));
		//System.out.println(generateSequence(getPinYinCapital(src1)));
		String str="经典8度500*12-QPGXJD081220160603001-32W";
		System.out.println(getPinYinCapital(str));
		System.out.println(generateSequence(getPinYinCapital(str)));//jd8d50012qpgxjd08122016060300132w
		System.out.println("jd8d50012qpgxjd08122016060300132w"+"201607040001");
		System.out.println(("jd8d50012qpgxjd08122016060300132w"+"201607040001").length());
		/*
		 * List<String> resultList = getPinYinCapital(src1);
		 * 
		 * System.out.println("结果=" + resultList.size()); for (int i = 0; i <
		 * resultList.size(); i++) { System.out.println(resultList.get(i)); }
		 */
	}
}
