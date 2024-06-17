 /**
  * 项目名称：V积分
  * 文件名：StringUtil.java
  * 作者：@王建
  * 创建时间： 2014/1/1
  * 功能描述: 字符串工具类
  * 版本: V1.0
  */
package com.dbt.framework.util;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.springframework.util.StringUtils;

public class StringUtil {

    /**
     * 缺省的字符串分割符
     */
    public static String DEFAULT_DELIM = "$*";

    /**
     * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
     */
    private StringUtil() {
    }

    /**
     * 将字符串数组使用缺省的分隔符合并成一个字符串。
     * 
     * @param array 字符串数组
     * @return 合并后的字符串
     */
    public static String join(String[] array) {
        return join(array, DEFAULT_DELIM);
    }

    /**
     * 将字符串数组使用指定的分隔符合并成一个字符串。
     * 
     * @param array 字符串数组
     * @param delim 分隔符，为null的时候使用缺省分割符（逗号）
     * @return 合并后的字符串
     */
    public static String join(String[] array, String delim) {
        int length = array.length - 1;
        if (delim == null) {
            delim = DEFAULT_DELIM;
        }
        StringBuffer result = new StringBuffer(length * 8);
        for (int i = 0; i < length; i++) {
            result.append(array[i]);
            result.append(delim);
        }
        result.append(array[length]);
        return result.toString();
    }

    /**
     * 将字符串使用缺省分割符（逗号）划分的单词数组。
     * 
     * @param source 需要进行划分的原字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组。
     */
    public static String[] split(String source) {
        return split(source, DEFAULT_DELIM);
    }

    /**
     * 此方法将给出的字符串source使用delim划分为单词数组。 注意：分隔字符串中每一个 <b>(ANY) </b>的字符都作为独立的分割符。 <br>
     * 举个例子： <br>
     * "mofit.com.cn"用"com"分割后的结果是三个字符串"fit."、"."和"n"，而不是"mofit."和".cn"。
     * 
     * @param source 需要进行划分的原字符串
     * @param delim 单词的分隔字符串
     * @return 划分以后的数组，如果source为null的时候返回以source为唯一元素的数组， 如果delim为null则使用逗号作为分隔字符串。
     */
    public static String[] split(String source, String delim) {
        String[] wordLists;
        if (source == null) {
            wordLists = new String[1];
            wordLists[0] = source;
            return wordLists;
        }
        if (delim == null) {
            delim = DEFAULT_DELIM;
        }
        StringTokenizer st = new StringTokenizer(source, delim);

        int total = st.countTokens();
        wordLists = new String[total];
        for (int i = 0; i < total; i++) {
            wordLists[i] = st.nextToken();
        }
        return wordLists;
    }

    /**
     * 字符串数组中是否包含指定的字符串。 注意：准确的说应该是匹配，而不是包含。 <br>
     * 举个例子：字符串数组"mofit.com.cn","neusoft.com"里 <b>不包含 </b>"com"， <br>
     * 但是 <b>包含 </b>"mofti.com.cn"。
     * 
     * @param strings 字符串数组
     * @param string 字符串
     * @param caseSensitive 是否大小写敏感
     * @return 包含时返回true，否则返回false
     */
    public static boolean contains(String[] strings, String string, boolean caseSensitive) {
        for (int i = 0; i < strings.length; i++) {
            if (caseSensitive == true) {
                if (strings[i].equals(string)) {
                    return true;
                }
            } else {
                if (strings[i].equalsIgnoreCase(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字符串数组中是否包含指定的字符串。大小写敏感。 <br>
     * 注意：准确的说应该是匹配，而不是包含。 <br>
     * 举个例子：字符串数组"mofit.com.cn","neusoft.com"里 <b>不包含 </b>"com"， <br>
     * 但是 <b>包含 </b>"mofti.com.cn"。
     * 
     * @param strings 字符串数组
     * @param string 字符串
     * @return 包含时返回true，否则返回false
     */
    public static boolean contains(String[] strings, String string) {
        return contains(strings, string, true);
    }

    /**
     * 去除左边多余的空格。
     * 
     * @param value 待去左边空格的字符串
     * @return 去掉左边空格后的字符串
     */
    public static String trimLeft(String value) {
        String result = value;
        if (result == null) {
            return result;
        }
        char ch[] = result.toCharArray();
        int index = -1;
        for (int i = 0; i < ch.length; i++) {
            if (Character.isWhitespace(ch[i])) {
                index = i;
            } else {
                break;
            }
        }
        if (index != -1) {
            result = result.substring(index + 1);
        }
        return result;
    }

    /**
     * 去除右边多余的空格。
     * 
     * @param value 待去右边空格的字符串
     * @return 去掉右边空格后的字符串
     */
    public static String trimRight(String value) {
        String result = value;
        if (result == null) {
            return result;
        }
        char ch[] = result.toCharArray();
        int endIndex = -1;
        for (int i = ch.length - 1; i > -1; i--) {
            if (Character.isWhitespace(ch[i])) {
                endIndex = i;
            } else {
                break;
            }
        }
        if (endIndex != -1) {
            result = result.substring(0, endIndex);
        }
        return result;
    }

    /**
     * 得到字符串的字节长度。汉字占两个字节，字母占一个字节
     * 
     * @param source 字符串
     * @return 字符串的字节长度
     */
    public static int getLength(String source) {
        int len = 0;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            int highByte = c >>> 8;
            len += highByte == 0 ? 1 : 2;
        }
        return len;
    }

    /**
     * 使用给定的字串替换源字符串中指定的字串。
     * 
     * @param mainString 源字符串
     * @param oldString 被替换的字串
     * @param newString 替换字串
     * @return 替换后的字符串
     */
    public final static String replace(String mainString, String oldString, String newString) {
        if (mainString == null) {
            return null;
        }
        int i = mainString.lastIndexOf(oldString);
        if (i < 0) {
            return mainString;
        }
        StringBuffer mainSb = new StringBuffer(mainString);
        while (i >= 0) {
            mainSb.replace(i, i + oldString.length(), newString);
            i = mainString.lastIndexOf(oldString, i - 1);
        }
        return mainSb.toString();
    }

    /**
     * 将给定的字符串转换为中文GBK编码的字符串。
     * 
     * @param str 输入字符串
     * @return 经GBK编码后的字符串，如果有异常，则返回原编码字符串
     */
    public final static String toChinese(final String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        String retVal = str;
        try {
            retVal = new String(str.getBytes("ISO8859_1"), "GBK");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return retVal;
    }

    /**
     * 将给定的中文GBK编码转换为UNICODE编码的字符串。
     * 
     * @param str 输入字符串
     * @return 经GBK编码后的字符串，如果有异常，则返回原编码字符串
     */
    public final static String toUNICODE(final String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        String retVal = str;
        try {
            retVal = new String(str.getBytes("ZHS16GBK"), "GBK");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return retVal;
    }

    /**
     * 用于字符串显示。将html敏感的尖括号、引号、连接号等用转义符替代。 <br>
     * 建议用法：在接收到客户端传来的字符串时，不进行转换，直接存入数据库； <br>
     * 在从数据库中取出，传给客户端做html显示时，才转换。
     * 
     * @param input 需要检查的字符串
     * @return 转化后的字串
     */
    public final static String convertToHTML(String input) {
        if (null == input || "".equals(input)) {
            return input;
        }

        StringBuffer buf = new StringBuffer();
        char ch = ' ';
        for (int i = 0; i < input.length(); i++) {
            ch = input.charAt(i);
            if (ch == '<') {
                buf.append("&lt;");
            } else if (ch == '>') {
                buf.append("&gt;");
            } else if (ch == '&') {
                buf.append("&amp;");
            } else if (ch == '"') {
                buf.append("&quot;");
            } else if (ch == '\n') {
                buf.append("<BR/>");
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }
    
    /**
	 * 判断特殊字符
	 * @Title : FilterStr
	 * @Type : FilterString
	 * @date : 2018年9月18日 下午20:01:21
	 * @Description : 判断特殊字符
	 * @param str
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static String FilterStr(String str) throws PatternSyntaxException
	{
		/**
		 * 特殊字符
		 */
//		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		String regEx="[`~$%^&*()+=|{}';',\\[\\]<>/?~￥%……&*+|{}‘；：”“’。，、？]";
		Pattern pat = Pattern.compile(regEx);     
        Matcher mat = pat.matcher(str);
        return mat.replaceAll("").trim();  
	}

    /**
     * 定义字符串加密时需要用到的标记字符串
     */
    private static String ENCRYPT_IN = "YN8K1JOZVURB3MDETS5GPL27AXW`IHQ94C6F0~qwert!@yuiop#$asdfghj%kl^&*zxc vbn(m)_+|{}:\"<>?-=\\[];,./'";

    /**
     * 定义字符串加密时需要用到的转义字符串
     */
    private static String ENCRYPT_OUT = "qazwsxcderfvbgtyhnmjuiklop~!@#$%^&*()_+|{ }:\"<>?-=\\[];,./'ABCDE`FGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * 对给定字符串进行加密操作
     * 
     * @param inPass 待加密的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String inPass) {
        String stringIn = ENCRYPT_IN;
        String stringOut = ENCRYPT_OUT;
        int time1 = Calendar.getInstance().get(Calendar.MINUTE);
        int time2 = Calendar.getInstance().get(Calendar.SECOND);
        int offset = (time1 + time2) % 95;
        String outPass = stringIn.substring(offset, offset + 1);
        stringIn = stringIn + stringIn;
        stringIn = stringIn.substring(offset, offset + 95);
        String temp = "";
        for (int i = 0; i <= inPass.length() - 1; i++) {
            temp = temp + stringOut.charAt(stringIn.indexOf(inPass.charAt(i)));

        }
        outPass = outPass + temp;
        return outPass;
    }

    /**
     * 对给定字符串进行解密操作
     * 
     * @param outPass 待解密的字符串
     * @return 解密还原后的字符串
     */
    public static String decrypt(String outPass) {
        String stringIn = ENCRYPT_IN;
        String stringOut = ENCRYPT_OUT;
        int offset = stringIn.indexOf(outPass.charAt(0));
        stringIn = stringIn + stringIn;
        stringIn = stringIn.substring(offset, offset + 95);
        outPass = outPass.substring(1);
        String inPass = "";
        for (int i = 0; i <= outPass.length() - 1; i++) {
            inPass = inPass + stringIn.charAt(stringOut.indexOf(outPass.charAt(i)));

        }
        return inPass;
    }

    //指定的字符串累加
    public static String strAdd(String chr, int len) {
        if (len > 0) {
            StringBuffer ret = new StringBuffer(len);
            for (int i = 0; i < len; i++) {
                ret.append(chr);
            }
            return (ret.toString());
        } else {
            return "";
        }
    }

    //给字符串补足到指定的长度，从左边补足chr指定的字符
    public static String lPad(String source, String chr, int len) {
        int lenleft = len - source.length();
        if (lenleft < 0) {
            lenleft = 0;
        }
        return (strAdd(chr, lenleft) + source);
    }

    //给字符串补足到指定的长度，从右边补足chr指定的字符
    public static String rPad(String source, String chr, int len) {
        int lenleft = len - source.length();
        if (lenleft < 0) {
            lenleft = 0;
        }
        return (source + strAdd(chr, lenleft));
    }
    
    /**
     * 用Character类的isDigit函数判断是否为全数字
     * @param str
     * @return
     */
    public static boolean isNumericByChar(String str) {
    	for (int i = str.length();--i>=0;){   
    		if (!Character.isDigit(str.charAt(i))){
    			return false;
    		}
    	}
    	return true;
	}
    
    public static boolean isContainNumber(String company) {
        Pattern p = Pattern.compile("[0-9]");
        Matcher m = p.matcher(company);
        if (m.find()) {
            return true;
        }
        return false;
    }
    
    /**
     * 将StringList的值转成String
     * @param strList
     * @return
     */
    public static String changeListToString(List<String> strList) {
    	StringBuffer buffer = new StringBuffer(strList.size()*10);
    	for (String value : strList) {
			buffer.append(",").append(value);
		}
    	return buffer.substring(1);
    }
    
    /**
     * 用正则表达式判断是否为全数字
     * @param str
     * @return
     */
    public static boolean isNumericByPattern(String str) {
    	Pattern pattern = Pattern.compile("[0-9]*");
    	return pattern.matcher(str).matches();
    }
    /**
     * 特殊字符替换
     * @param str
     * @return
     */
    public static String escapeXML(String str) {
		if (str == null)
			return "";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			switch (c) {
			case '\u00FF':
			case '\u0024':
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '>':
				sb.append("&gt;");
				break;
			case '\"':
				sb.append("&quot;");
				break;
			case '\'':
				sb.append("&apos;");
				break;
			default:
				if (c >= '\u0000' && c <= '\u001F')
					break;
				if (c >= '\uE000' && c <= '\uF8FF')
					break;
				if (c >= '\uFFF0' && c <= '\uFFFF')
					break;
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}
    /**  
     * ASCII表中可见字符从!开始，偏移位值为33(Decimal)  
     */  
    static final char DBC_CHAR_START = 33; // 半角!   
  
    /**  
     * ASCII表中可见字符到~结束，偏移位值为126(Decimal)  
     */  
    static final char DBC_CHAR_END = 126; // 半角~   
  
    /**  
     * 全角对应于ASCII表的可见字符从！开始，偏移值为65281  
     */  
    static final char SBC_CHAR_START = 65281; // 全角！   
  
    /**  
     * 全角对应于ASCII表的可见字符到～结束，偏移值为65374  
     */  
    static final char SBC_CHAR_END = 65374; // 全角～   
  
    /**  
     * ASCII表中除空格外的可见字符与对应的全角字符的相对偏移  
     */  
    static final int CONVERT_STEP = 65248; // 全角半角转换间隔   
  
    /**  
     * 全角空格的值，它没有遵从与ASCII的相对偏移，必须单独处理  
     */  
    static final char SBC_SPACE = 12288; // 全角空格 12288   
  
    /**  
     * 半角空格的值，在ASCII中为32(Decimal)  
     */  
    static final char DBC_SPACE = ' '; // 半角空格   
  
    /**  
     * <PRE>  
     * 半角字符->全角字符转换    
     * 只处理空格，!到&tilde;之间的字符，忽略其他  
     * </PRE>  
     */  
    public static String bj2qj(String src) {   
        if (src == null) {   
            return src;   
        }   
        StringBuilder buf = new StringBuilder(src.length());   
        char[] ca = src.toCharArray();   
        for (int i = 0; i < ca.length; i++) {   
            if (ca[i] == DBC_SPACE) { // 如果是半角空格，直接用全角空格替代   
                buf.append(SBC_SPACE);   
            } else if ((ca[i] >= DBC_CHAR_START) && (ca[i] <= DBC_CHAR_END)) { // 字符是!到~之间的可见字符   
                buf.append((char) (ca[i] + CONVERT_STEP));   
            } else { // 不对空格以及ascii表中其他可见字符之外的字符做任何处理   
                buf.append(ca[i]);   
            }   
        }   
        return buf.toString();   
    }   
  
    /**  
     * <PRE>  
     * 全角字符->半角字符转换    
     * 只处理全角的空格，全角！到全角～之间的字符，忽略其他  
     * </PRE>  
     */  
    public static String qj2bj(String src) {   
        if (src == null) {   
            return src;   
        }   
        StringBuilder buf = new StringBuilder(src.length());   
        char[] ca = src.toCharArray();   
        for (int i = 0; i < src.length(); i++) {   
            if (ca[i] >= SBC_CHAR_START && ca[i] <= SBC_CHAR_END) { // 如果位于全角！到全角～区间内   
                buf.append((char) (ca[i] - CONVERT_STEP));   
            } else if (ca[i] == SBC_SPACE) { // 如果是全角空格   
                buf.append(DBC_SPACE);   
            } else { // 不处理全角空格，全角！到全角～区间外的字符   
                buf.append(ca[i]);   
            }   
        }   
        return buf.toString();   
    }
    
    /**
     * 按需要进行截取
     * @param content
     * @param limit
     * @return
     */
    public static String subByLength(String content, int limit) {
    	if(StringUtils.isEmpty(content)) return "";
    	int conLength = content.length();
    	if(conLength > limit) {
    		content = content.substring(0, limit);
    	}
    	return content;
    }
    /*
    * 中文转unicode编码
    */
   public static String encodeUnicode(String gbString) {
       char[] utfBytes = gbString.toCharArray();
       String unicodeBytes = "";
       for (int i = 0; i < utfBytes.length; i++) {
           String hexB = Integer.toHexString(utfBytes[i]);
           if (hexB.length() <= 2) {
               hexB = "00" + hexB;
           }
           unicodeBytes = unicodeBytes + "\\u" + hexB;
       }
       return unicodeBytes;
   }
   
   /*
    * unicode编码转中文
    */
   public static String decodeUnicode(String dataStr) {
       int start = 0;
       int end = 0;
       final StringBuffer buffer = new StringBuffer();
       while (start > -1) {
           end = dataStr.indexOf("\\u", start + 2);
           String charStr = "";
           if (end == -1) {
               charStr = dataStr.substring(start + 2, dataStr.length());
           } else {
               charStr = dataStr.substring(start + 2, end);
           }
           char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
           buffer.append(new Character(letter).toString());
           start = end;
       }
       return buffer.toString();
   }

    public static void main(String[] args) {

    	String[] test = { "北京mofit.com.cn", " neusoft.com ", "<out\"OfTax>" };
        String all = join(test, "||");

        System.out.println("字符串[" + test[0] + "],[" + test[1] + "],[" + test[2] + "]合并结果：" + all);
        System.out.println("字符串[" + test[0] + "],[" + test[1] + "],[" + test[2]
                        + "]是否包含［<out\"oftax>］(区分大小写)：" + contains(test, "<out\"oftax>"));
        System.out.println("字符串[" + test[0] + "],[" + test[1] + "],[" + test[2]
                        + "]是否包含［<out\"oftax>］(不区分大小写)：" + contains(test, "<out\"oftax>", false));
        System.out.println("字符串[" + all + "]转换为html：" + convertToHTML(all));

        String en = encrypt(test[2]);

        System.out.println("字符串[" + test[2] + "]加密后为：" + en);
        System.out.println("字符串[" + en + "]解密后为：" + decrypt(en));
        System.out.println("字符串[" + test[0] + "]的长度为：" + getLength(test[0]));
        System.out.println("字符串[" + all + "]用[Company]替换[.com]后为：["
                        + replace(all, ".com", "Company") + "]");

        String[] sp = split(test[0], "com");
        System.out.println("字符串[" + test[0] + "]用[com]分隔为" + sp.length + "个字符串。分别为：[" + sp[0]
                        + "],[" + sp[1] + "],[" + sp[2] + "]...");

        System.out.println("字符串[" + test[1] + "]去掉左空格为：[" + trimLeft(test[1]) + "]");
        System.out.println("字符串[" + test[1] + "]去掉右空格为：[" + trimRight(test[1]) + "]");
        
        String tmpStr = "。，：　。...,,, ";
        System.out.println("字符串[" + tmpStr + "]全角转半角为：[" + qj2bj(tmpStr) + "]");
        System.out.println("字符串[" + tmpStr + "]半角转全角为：[" + bj2qj(tmpStr) + "]");
    	
    	
        
    }
    
}