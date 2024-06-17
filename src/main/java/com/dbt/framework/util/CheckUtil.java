 /**
  * 项目名称：V积分
  * 文件名：CheckUtil.java
  * 作者：@王建
  * 创建时间：2014/1/1
  * 功能描述: 校验工具类
  * 版本: V1.0
  */
package com.dbt.framework.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	
	
	/**
     * 判断字符串是否为空.
     *
     * @param strIn the str in
     * @return true 为空 false 不为空
     * @author 孙顺博  2015-1-31
     */
    public static boolean isEmpty(String strIn){
    	return strIn == null || strIn.length() == 0;
    }
    
    /**
     * <pre>
     * 判断对象是否为空.
     * </pre>
     *
     * @param object the object
     * @return true, if is empty
     * @author 孙顺博  2015-1-31
     */
    public static boolean isEmpty(Object object){
    	return object == null || object.toString().trim().length() == 0;
    }
    
    /**
     * 判断是否为空
     * 
     * @param 
     * @return true 为空 false 不为空
     */
    public static boolean IsEmpty(String strIn){
        if ((strIn != null) && (!strIn.equals("")) && (strIn.length() > 0)){
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * 判断字符串是否为空或者null
     * 
     * @param strParam  验证参数
     * @return 为空或null时返回True
     */
    public static boolean isBlankOrNull(String strParam) {
        return (strParam == null) || (strParam.trim().length() == 0);
    }
            
    /**
     * 判断是否为数字
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isNumeric(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为整数
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isInteger(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^-?\\d+$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为（非零的）正整数
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isPosInteger(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^\\+?[1-9][0-9]*$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为（非零的）负整数
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isNegInteger(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^\\-[1-9][0-9]*$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为非负整数（正整数 + 0）  
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isNonNegInteger(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为非正整数（负整数 + 0）
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isNonPosInteger(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^((-\\d+)|(0+))$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为浮点数
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isFloat(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为非负浮点数（正浮点数 + 0）
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isNonNegFloat(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为正浮点数
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isPosFloat(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为非正浮点数（负浮点数 + 0）
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isNonPosFloat(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为负浮点数
     * 
     * @param strNum 待查字符串
     * @return 是则返回true
     */
    public static boolean isNegFloat(String strNum) {
        
        // 空串不为数字
        if (null == strNum || "".equals(strNum))
            return false;
        Pattern pattern = Pattern.compile("^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$");
        Matcher isNum = pattern.matcher(strNum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为日期
     * 
     * @param strDate 需要解析的日期字符串
     * @return 合法日期返回true
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 判断是否为指定格式的日期
     * 
     * @param strDate 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 合法日期返回true
     */
    @SuppressWarnings("unused")
    public static boolean isDate(String strDate, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        try {
            Date date = new SimpleDateFormat(pattern).parse(strDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 判断第二个日期是否在第一个日期之后
     * 
     * @param strDate1 日期字符串一 格式：“yyyyMMdd HH:mm:ss”
     * @param strDate2 日期字符串二格式：“yyyyMMdd HH:mm:ss”
     * @return 是则返回true
     */
    public static boolean checkDateDiff(String strDate1, String strDate2) throws Exception {
        
        // 将字符串转为日期类型
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date1;
        Date date2;
        try {
            date1 = sdf.parse(strDate1);
            date2 = sdf.parse(strDate2);
            return (date2.getTime() - date1.getTime()) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 判断字符串长度是否合法
     * 
     * @param str 待检查字符串
     * @param length 要求的长度
     * @return 符合长度要求返回true
     */
    public static boolean checkStrLength(String str, int length) {
        if (null == str) str = "";
        return str.length() <= length;
    }
    
    /**
     * 判断字符串范围是否合法
     * 
     * @param strNum 待检查字符串
     * @param min 最小值
     * @param max 最大值
     * @return 符合范围返回true
     */
    public static boolean checkStrRange(String strNum, int min, int max) {
        if (null == strNum) strNum = "";
        int intNum;
        try {
            intNum = Integer.parseInt(strNum);
            return intNum >= min && intNum <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * 判断性别是否与身份证号中的信息匹配
     * 
     * @param strGender 性别 0：男 1：女
     * @param strId 身份证号码
     * @return 是则返回true
     */
    public static boolean checkGenderInIDCard(String strGender, String strId) {
        if (null == strGender || null == strId 
                || "".equals(strGender) || strId.length() < 15)
            
            return false;
        
        // 获取性别 
        String id17 = strId.substring(16, 17);
        String strIDGender = "";
        if (Integer.parseInt(id17) % 2 != 0) {    
            strIDGender = "0";    
        } else {    
            strIDGender = "1";
        }
        return strIDGender.equals(strGender);
    }
    
    /**
     * 判断出生日期是否与身份证号中的信息匹配
     * 
     * @param strDate 出生日期 格式 “yyyyMMdd”
     * @param strId 身份证号码
     * @return 是则返回true
     */
    public static boolean checkBirthIDCard(String strDate, String strId) {
        if (null == strDate || null == strId 
                || "".equals(strDate) || strId.length() < 15)
            
            return false;
        
        // 获取出生日期
        String birthday = strId.substring(6, 14);
        try {
            Date date = new SimpleDateFormat("yyyyMMdd").parse(strDate);
            Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
            return (birthdate.getTime() - date.getTime()) == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 测试
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	
    	System.out.println(isDate("2011-05-31"));
    	
    }
}
