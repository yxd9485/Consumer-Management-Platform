 /**
  * 项目名称：V积分
  * 文件名：DateUtil.java
  * 作者：@王建
  * 创建时间： 2014/1/1
  * 功能描述: 日期时间工具类
  * 版本: V1.0
  */
package com.dbt.framework.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtil {

	/**
	 * 日期显示格式
	 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_YEAR_FORMAT = "yyyy";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DEFAULT_DATETIME_MINTIME = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_DATE_FORMAT_CH = "yyyy年MM月dd日";
    public static final String DEFAULT_DATETIME_FORMAT_CH = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DEFAULT_DATEMIN_CH = "yyyy年MM月dd日 HH时mm分";
    public static final String DEFAULT_DATE_FORMAT_HOUR = "yyyyMMddHH";
    public static final String DEFAULT_DATE_FORMAT_MONTH = "yyyy-MM";


    /**
     * 日期入库格式
     */
    public static final String DEFAULT_DATE_FORMAT_SHT = "yyyyMMdd";
    public static final String DEFAULT_DATETIME_FORMAT_SHT = "yyyyMMddHHmmss";
    public static final String DEFAULT_DATETIME_MINTIME_SHT = "yyyyMMddHHmm";
    public static final String DEFAULT_DATE_FORMAT_MONTH_SHT = "yyyyMM";
    
    /**
     * 时间格式类型
     */
    public static final int DATE_TYPE_DEFAULT = 1;  //yyyy-MM-dd
    public static final int DATE_TYPE_TIMESTAMP = 2; //yyyy-MM-dd HH:mm:ss
    public static final int DATE_TYPE_MINTIME = 3; //yyyy-MM-dd HH:mm
    public static final int DATE_TYPE_DEFAULT_CH = 4; //yyyy年MM月dd日
    public static final int DATE_TYPE_TIMESTAMP_CH = 5; //yyyy年MM月dd日 HH时mm分ss秒
    public static final int DATE_TYPE_DATEMIN_CH = 6; //yyyy年MM月dd日 HH时mm分
    public static final int DATE_TYPE_FORMAT_SHT = 7; //yyyyMMdd
    public static final int DATE_TYPE_8 = 8; //yyyy-MM-dd转yyyy年MM月dd日
    
    /**
     * 判别标准
     */
    public static final String TIME_FORMAT_CH = "CH";  //中国的星期标准
    public static final String TIME_FORMAT_US = "US";  //国外的星期标准
    
    /**
     * 一天中开始时间和结束时间
     */
    public static final String DAY_START_TIME = "00:00:00";
    public static final String DAY_END_TIME = "23:59:59";
    
    /**
     * 私有构造方法，禁止对该类进行实例化
     */
    private DateUtil() {
    }

    /**
     * 得到系统当前日期时间
     * 
     * @return 当前日期时间
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }
    
    /**
     * 根据指定日期或时间转化日期格式
     * @param day
     * @param pattern
     * @return
     */
    public static Date getDateFromDay(String day, String pattern) {
    	Date date = null;
    	try {
    		if (null == pattern || "".equals(pattern)) {
                pattern = DEFAULT_DATETIME_FORMAT;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return null == date ? getNow() : date;
    }

    /**
     * 得到用缺省方式格式化的当前日期
     * 
     * @return 当前日期
     */
    public static String getDate() {
        return getDateTime(DEFAULT_DATE_FORMAT);
    }
    
    public static String getDate(String formatter) {
    	SimpleDateFormat format = new SimpleDateFormat(formatter);
    	return format.format(getNow());
    }

    /**
     * 得到用缺省方式格式化的当前日期及时间
     * 
     * @return 当前日期及时间
     */
    public static String getDateTime() {
        return getDateTime(DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 得到系统当前日期及时间，并用指定的方式格式化
     * 
     * @param pattern 显示格式
     * @return 当前日期及时间
     */
    public static String getDateTime(String pattern) {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, pattern);
    }

    /**
     * 得到用指定方式格式化的日期
     * 
     * @param date 需要进行格式化的日期
     * @param pattern 显示格式
     * @return 日期时间字符串
     */
    public static String getDateTime(Date date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    
    /**
     * 获取系统当前毫秒
     * @return
     */
    public static long getTimeStamp(){
    	return System.currentTimeMillis();
    }
    
    /**
     * 得到昨天日期
     * @return
     */
    public static int getYesterDay(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(addDays(-1));
    	return calendar.get(Calendar.DATE);
    }
    
    /**
     * 指定日期获取系统当前毫秒
     * @param date  当前日期
     * @return
     */
    public static long getTimeStamp(Date date){
    	return date.getTime();
    }
    
    /**
     * 指定字符日期与格式获取系统当前毫秒
     * @param dateStr  字符日期
     * @param pattern  日期格式
     * @return 出错时返回系统当前毫秒，未出错则进行正常转换
     */
    public static long getTimeStamp(String dateStr, String pattern){
    	if(StringUtils.isEmpty(dateStr)) return System.currentTimeMillis();
    	SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
    	Date date = null;
    	try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return date == null ? System.currentTimeMillis() : date.getTime();
    }
    

    /**
     * 得到当前年份
     * 
     * @return 当前年份
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 得到当前月份
     * 
     * @return 当前月份
     */
    public static int getCurrentMonth() {
        //用get得到的月份数比实际的小1，需要加上
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 得到当前日
     * 
     * @return 当前日
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }
    
    /**
     * 得到当前小时(24小时制)
     * @return
     */
    public static int getCurretHour() {
    	return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     * 
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(int days) {
        return add(getNow(), days, Calendar.DATE);
    }

    /**
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     * 
     * @param date 基准日期
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }
    
    public static String getLastWeekStartDay(){
    	Calendar cal = Calendar.getInstance();
    	int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
    	int start_of_week = 1;
    	int sub_days = day_of_week - start_of_week - 1;
    	Date lastWeekDate = addDays(-(7+sub_days));
    	return getDateTime(lastWeekDate, DEFAULT_DATE_FORMAT);
    }
    
    public static String getLastWeekEndDay(){
    	Calendar cal = Calendar.getInstance();
    	int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
    	int end_of_week = 7;
    	int sub_days = end_of_week - day_of_week + 1;
    	Date lastWeekDate = addDays(-(7-sub_days));
    	return getDateTime(lastWeekDate, DEFAULT_DATE_FORMAT);
    }
    
    public static Date getMonthFirstDay() {
    	return getMonthFirstDay(getNow());
    }
    
    public static Date getMonthFirstDay(Date date) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        return calendar.getTime();
    }
    
    public static String getLastMonthLastDate() {
    	Date date = addDays(getMonthFirstDay(), -1);
    	return getDateTime(date, DEFAULT_DATE_FORMAT);
    }
    
    public static String getLastMonthFirstDate() {
    	Date date = addDays(getMonthFirstDay(), -1);
    	Date firstDay = getMonthFirstDay(date);
    	return getDateTime(firstDay, DEFAULT_DATE_FORMAT);
    }

    /**
     * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     * 
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(int months) {
        return add(getNow(), months, Calendar.MONTH);
    }

    /**
     * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
     * 
     * @param date 基准日期
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }
    
    /**
     * 内部方法。为指定日期增加相应的天数或月数
     * 
     * @param date 基准日期
     * @param amount 增加的数量
     * @param field 增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    public static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /**
     * 内部方法。为指定日期增加相应的天数或月数
     * 
     * @param date 基准日期
     * @param amount 增加的数量
     * @param field 增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    public static String add(String date, int amount, int field, String pattern) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtil.parse(date, pattern));
            calendar.add(field, amount);
            return DateUtil.getDateTime(calendar.getTime(), pattern);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     * 
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    public static long diffDays(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }
    /**
     * 计算两个日期相差秒数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     * 
     * @param one
     *            第一个日期数，作为基准
     * @param two
     *            第二个日期数，作为比较
     * @return 两个日期相差秒数
     */
    public static long diffSecond(Date one, Date two) {
        return diffMilliseconds(one, two) / 1000;
    }
    /**
     * 计算两个日期相差毫秒数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     * 
     * @param one
     *            第一个日期数，作为基准
     * @param two
     *            第二个日期数，作为比较
     * @return 两个日期相差毫秒数
     */
    public static long diffMilliseconds(Date one, Date two) {
        return one.getTime() - two.getTime();
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     * 
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差月份数
     */
    public static int diffMonths(Date one, Date two) {
        Calendar calendar = Calendar.getInstance();
        //得到第一个日期的年分和月份数
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);
        int monthOne = calendar.get(Calendar.MONDAY);
        //得到第二个日期的年份和月份
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);
        int monthTwo = calendar.get(Calendar.MONDAY);
        return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
    }
    
    /**
     * 比较两个String类型的日期大小，若前一个小于后一个，返回负数
     * @param dayOne
     * @param dayTwo
     * @return
     * @throws Exception
     */
    public static long diffDays(String dayOne, String dayTwo) throws Exception {
    	Date one = parse(dayOne, "");
    	Date two = parse(dayTwo, "");
    	return diffDays(one, two);
    }
    
    public static boolean bigMatch(String dayOne, String dayTwo, String pattern) throws Exception {
    	Date one = parse(dayOne, pattern);
    	Date two = parse(dayTwo, pattern);
    	return one.getTime() > two.getTime();
    }
    
    public static boolean bigMatch(String dayOne, String dayTwo) throws Exception {
    	return bigMatch(dayOne, dayTwo, "");
    }

    /**
     * 将一个字符串用给定的格式转换为日期类型。 <br>
     * 注意：如果返回null，则表示解析失败
     * 
     * @param datestr 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期
     */
    public static Date parse(String datestr, String pattern) throws Exception {
        Date date = null;
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw e;
        }
        return date;
    }

    /**
     * 返回本月的最后一天
     * 
     * @return 本月最后一天的日期
     */
    public static Date getMonthLastDay() {
        return getMonthLastDay(getNow());
    }

    /**
     * 返回给定日期中的月份中的最后一天
     * 
     * @param date 基准日期
     * @return 该月最后一天的日期
     */
    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //将日期设置为下一月第一天
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 0);
        return calendar.getTime();
    }
    
    /**
     * 获取指定时间距离今天的天数
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getDayBytime(String date) throws ParseException{
    	String dsteStr= date + " 23:59:59 ";
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date dateTime=sdf.parse(dsteStr);
    	long  s1=dateTime.getTime();//将时间转为毫秒
    	long s2=System.currentTimeMillis();//得到当前的毫秒
    	int  day=(int) ((s1-s2)/1000/60/60/24);
    	return day;
    }
    
    /**
     * 处理字符串格式的时间
     * @param timeStr
     * @return
     */
    public static String trimAfterPointer(String timeStr){
		String result = "";
		if(!StringUtils.isBlank(timeStr)){
			result = timeStr.contains(".") ? timeStr.substring(0, timeStr.indexOf(".")) : timeStr;
		}
		return result;
	}
    
    /**
     * 将Timestamp格式（yyyy-MM-dd HH:mm:ss）的时间转为String
     * @param time
     * @return
     */
    public static String formatTimestamp(Timestamp time){
    	return formatTimestamp(time, DateUtil.DEFAULT_DATETIME_FORMAT);
    }
    
    /**
     * 将Timestamp按指定格式的时间转为String
     * @param time
     * @param pattern
     * @return
     */
    public static String formatTimestamp(Timestamp time, String pattern){
    	String result = "";
    	
    	Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		Date current = cal.getTime();
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		result = format.format(current);
		
    	return result;
    }
    
    /**
     * 把时间字符串转换成timestamp
     * @param time
     * @return
     */
    public static Timestamp string2Timestamp(String time){
    	return Timestamp.valueOf(time);
    }
    
    /**
     * 入库 -- 把页面获取来的日期按对应格式进行入库
     * @param dateVals
     * @param type
     * @return
     */
    public static String parseDate(String dateVals, int type){
        String result = "";
        SimpleDateFormat format;
        SimpleDateFormat daters;
        Date current;
        try {
            switch(type){
            case DATE_TYPE_DEFAULT:
                format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                current = format.parse(dateVals);
                daters = new SimpleDateFormat(DEFAULT_DATE_FORMAT_SHT);
                result = daters.format(current);
                break;
            case DATE_TYPE_TIMESTAMP:
                format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
                current = format.parse(dateVals);
                daters = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_SHT);
                result = daters.format(current);
                break;
            case DATE_TYPE_MINTIME:
                format = new SimpleDateFormat(DEFAULT_DATETIME_MINTIME);
                current = format.parse(dateVals);
                daters = new SimpleDateFormat(DEFAULT_DATETIME_MINTIME_SHT);
                result = daters.format(current);
                break;
            case DATE_TYPE_DEFAULT_CH:
            	format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_CH);
            	current = format.parse(dateVals);
            	daters = new SimpleDateFormat(DEFAULT_DATE_FORMAT_CH);
	            result = daters.format(current);
	            break;
            case DATE_TYPE_FORMAT_SHT:
                format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_SHT);
                current = format.parse(dateVals);
                daters = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                result = daters.format(current);
                break;
            case DATE_TYPE_8:
            	format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            	current = format.parse(dateVals);
            	daters = new SimpleDateFormat(DEFAULT_DATE_FORMAT_CH);
	            result = daters.format(current);
	            break;
            default:
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static String changeDate(String dateVals, int type){
    	 String result = "";
         SimpleDateFormat format;
         SimpleDateFormat daters;
         Date current;
         try {
             if(!StringUtils.isEmpty(dateVals)){
            	 String formatType = type == DATE_TYPE_DEFAULT ? DEFAULT_DATE_FORMAT : DEFAULT_DATETIME_FORMAT;
            	 format = new SimpleDateFormat(formatType);
            	 current = format.parse(dateVals);
            	 if(type == DATE_TYPE_TIMESTAMP_CH){
                     daters = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_CH);
                     result = daters.format(current);
            	 } else if(type == DATE_TYPE_DATEMIN_CH) {
                     daters = new SimpleDateFormat(DEFAULT_DATEMIN_CH);
                     result = daters.format(current);
            	 }
            	 
             }
         } catch (ParseException e) {
             e.printStackTrace();
         }
         return result;
    }
    
    public static String changeTime(String dateVals, String pattern) {
    	String result = "";
        SimpleDateFormat format;
        SimpleDateFormat daters;
        Date current;
        try {
            if(!StringUtils.isEmpty(dateVals)){
            	format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
                current = format.parse(dateVals);
                daters = new SimpleDateFormat(pattern);
                result = daters.format(current);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 显示  -- 把库中显示为纯数字格式的日期字符串按对应的显示规则进行显示
     * @param dateVals
     * @param type
     * @return
     */
    public static String generateDate(String dateVals, int type){
        String result = "";
        SimpleDateFormat format;
        SimpleDateFormat daters;
        Date current;
        try {
            if(!StringUtils.isEmpty(dateVals)){
            	switch(type){
                case DATE_TYPE_DEFAULT:
                    format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_SHT);
                    current = format.parse(dateVals);
                    daters = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
                    result = daters.format(current);
                    break;
                case DATE_TYPE_TIMESTAMP:
                    format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_SHT);
                    current = format.parse(dateVals);
                    daters = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
                    result = daters.format(current);
                    break;
                case DATE_TYPE_MINTIME:
                    format = new SimpleDateFormat(DEFAULT_DATETIME_MINTIME_SHT);
                    current = format.parse(dateVals);
                    daters = new SimpleDateFormat(DEFAULT_DATETIME_MINTIME);
                    result = daters.format(current);
                    break;
                case DATE_TYPE_DEFAULT_CH:
                	format = new SimpleDateFormat(DEFAULT_DATE_FORMAT_SHT);
                	current = format.parse(dateVals);
                	daters = new SimpleDateFormat(DEFAULT_DATE_FORMAT_CH);
                	result = daters.format(current);
                	break;
                case DATE_TYPE_TIMESTAMP_CH:
                	format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_SHT);
                	current = format.parse(dateVals);
                	daters = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT_CH);
                	result = daters.format(current);
                	break;
                default:
                    break;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 返回当天前后的特定天的开始或结束时间
     * 
     * @param addDay
     * @param endTime   true:指定天的23:59:59、false:指定天的00:00:00
     * @return
     */
    public static Date getDayOfYear(int addDay, boolean endTime) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, addDay);
        if (endTime) {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        return calendar.getTime();
    }
    
    /**
     * 根据时间字符串，时间类型，定义标准来获取当前时间所在当年的第N周
     * @param dateStr
     * @param type
     * @param normal
     * @return
     */
    public static int getWeekOfYear(String date, String type, String timeZone) {
    	int week = 0; 
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat format = new SimpleDateFormat(type);
    	try {
			if(TIME_FORMAT_CH.equals(timeZone)){
				cal.setFirstDayOfWeek(Calendar.MONDAY);
			}
			cal.setTime(format.parse(date));
			week = cal.get(Calendar.WEEK_OF_YEAR);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return week;
    }
    
    /**
     * 得到当第几周
     * 
     * @return 当前日
     */
    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     * 得到当第几周
     * 
     * @return yyyyWW
     */
    public static String getWeekOfYearFor53(Date date) {

        String year = DateUtil.getDateTime(date, "yyyy");
        String month = DateUtil.getDateTime(date, "MM");
        String weekOfYear = String.format("%02d", DateUtil.getWeekOfYear(date));
        
        if ("12".equals(month) && "01".equals(weekOfYear)) {
            return String.valueOf(Integer.valueOf(year) + 1) + weekOfYear;
            
        } else {
            return year + weekOfYear;
        }
    }
    
    /**
     * 获取当前是周几，
     * 
     * @return 周日 week = 7
     */
    public static int getDayOfWeek() {
        return getDayOfWeek(getNow());
    }
    
    /**
     * 获取指定日期是周几，
     * 
     * @return 周日 week = 7
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if(week==1){
           week = 7;
        }else{
           week = week - 1;
        }
        return week;
    }
    
    /**
     * 根据日期字符串（yyyyMMdd）计算年龄 
     * @param dateStr
     * @return
     * @throws Exception
     */
    public static String getAge(String dateStr){
    	return getAge(dateStr, DEFAULT_DATE_FORMAT_SHT);
    }
    
    /**
     * 根据日期字符串计算年龄 
     * @param dateStr
     * @param pattern（例如：yyyyMMdd）
     * @return
     */
    public static String getAge(String dateStr, String pattern){ 
    	String ageStr = "0";
    	try {
			Date birthday = parse(dateStr, pattern);
			Calendar calendar = Calendar.getInstance();
			if(!calendar.before(birthday)){
				// 获取当前时间戳
				long currentTimestamp = calendar.getTimeInMillis();
				long birthTimestamp = birthday.getTime();
				long diffDays = (currentTimestamp - birthTimestamp) / (1000 * 60 * 60 * 24) + 1;
				ageStr = diffDays / 365 + "";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return ageStr;
    }
    
    /**
     * 日期换时间
     * @param dateStr
     * @return
     */
    public static String changeDateToTime(String dateStr){
    	String result = "";
    	SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    	SimpleDateFormat timeFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
    	try {
			if(!StringUtils.isEmpty(dateStr)){
				Date date = dateFormat.parse(dateStr);
				result = timeFormat.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 根据数字类型的日期进行截取
     * @param numDate
     * @return
     */
    public static String splitDateFromNumberDate(String numDate){
    	String result = "";
    	
    	if(!StringUtils.isEmpty(numDate)){
    		
    		//when occur the marks below, return
    		if(numDate.contains("-") || numDate.contains(":")){
    			return "";
    		}
    		
    		int length = numDate.length();
        	switch(length){
        		case 8:
        			result = numDate;
        			break;
        		default:
        			result = length > 8 ? numDate.substring(0, 8) : "0";
        	}
        	//与当前时间比较，若比当前时间大，进行清空
        	String current = parseDate(getDate(), DATE_TYPE_DEFAULT);
        	int cur = Integer.parseInt(current);
        	int res = Integer.parseInt(result);
        	if(cur < res){
        		result = "";
        	}
    	}
    	
    	
    	return result;
    }
    
    /**
     * 根据数字格式的时间进行切割并展示
     * @param numDate
     * @return
     */
    public static String shatterDate(String numDate){
    	String result = "";
    	String temp = "";
    	if(!StringUtils.isEmpty(numDate)){
    		//when occur the marks below, return
    		if(numDate.contains("-") || numDate.contains(":")){
    			return numDate;
    		}
    		int length = numDate.length();
        	switch(length){
        		case 8:
        			result = generateDate(numDate, DATE_TYPE_DEFAULT);
        			break;
        		case 12:
        			result = generateDate(numDate, DATE_TYPE_MINTIME);
        			break;
        		case 14:
        			result = generateDate(numDate, DATE_TYPE_TIMESTAMP);
        			break;
        		default:
        			if(length > 8){
        				temp = numDate.substring(0, 8);
            			result = generateDate(temp, DATE_TYPE_DEFAULT);
        			} else {
        				result = numDate;
        			}
        			
        	}
    	}
    	return result;
    	
    }
    
    /**
     * 拼装某一天的开始时间
     * @param day(yyyy-MM-dd)
     * @return day(yyyy-MM-dd) 00:00:00
     */
    public static String getStartDateTimeByDay(String day){
    	return day + " " + DAY_START_TIME;
    }
    
    /**
     * 拼装某一天的结束时间
     * @param day(yyyy-MM-dd)
     * @return day(yyyy-MM-dd) 23:59:59
     */
    public static String getEndDateTimeByDay(String day){
    	return day + " " + DAY_END_TIME;
    }
    
    /**
     * 判断当前输入的是否为一个日期
     * @param date
     * @return
     */
    public static boolean isDate(String date){
    	boolean status = true;
    	try {
    		Date current = null;
			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			current = format.parse(date);
			current.compareTo(getNow());
			current = null;
		} catch (Exception e) {
			status = false;
		}
    	return status;
    }
    
    /**
     * 判断当前输入的是否为一个时间点
     * @param time
     * @return
     */
    public static boolean isTime(String time){
    	boolean status = true;
    	try {
			Date current = null;
			SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
			current = format.parse(time);
			current.compareTo(getNow());
			current = null;
		} catch (Exception e) {
			status = false;
		}
    	return status;
    }
    
    /**
     * 根据类型判断当前是否为一个日期类型的数据
     * @param time
     * @param pattern
     * @return
     */
    public static boolean isTime(String time, String pattern){
    	boolean status = true;
    	if(pattern == null) pattern = DEFAULT_DATE_FORMAT;
    	
    	if(pattern == DEFAULT_DATE_FORMAT){
    		status = isDate(time);
    	} else if(pattern == DEFAULT_DATETIME_FORMAT){
    		status = isTime(time);
    	}
    	
    	return status;
    }
    
    public static String compareDatetimeWithCurrent(String time) {
    	if(StringUtils.isEmpty(time)){
    		return "";
    	}
    	String result = "";
    	try {
    		long timestamp = 0;
    		if(isDate(time)){
    			timestamp = getTimeStamp(time, DEFAULT_DATE_FORMAT);
    		} else if(isTime(time)){
    			timestamp = getTimeStamp(time, DEFAULT_DATETIME_FORMAT);
    		}
    		long current = System.currentTimeMillis();
    		result = timestamp > current ? "" : time;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 判断第一个时间是否在第二个和第三个时间的区间内
     * 默认第一个时间为DateTime，指定第二个和第三个时间的时间格式
     * @param current
     * @param start
     * @param end
     * @param pattern
     * @return
     */
    public static boolean isInTheRound(String current, String start, String end, String pattern) {
    	boolean flag = false;
    	try {
			long startStamp = getTimeStamp(start, pattern);
			long endStamp = getTimeStamp(end, pattern);
			String curPattern = isTime(current) ? DEFAULT_DATETIME_FORMAT : DEFAULT_DATE_FORMAT;
			long currentStamp = getTimeStamp(current, curPattern);
			if(currentStamp >= startStamp && currentStamp <= endStamp){
				flag = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return flag;
    }
    
    /**
     * 将日期类型数据，转换到指定的类型
     * @param dateVal
     * @param valueType
     * @return
     */
    public static String changeToFormattedDate(String dateVal, String valueType) {
    	String result = "";
    	try {
			boolean isTime = isTime(dateVal);
			boolean isDate = isDate(dateVal);
			String dateType = isTime ? DEFAULT_DATETIME_FORMAT : DEFAULT_DATE_FORMAT;
			if(!isTime && !isDate) {
				dateType = DEFAULT_DATETIME_MINTIME;
			}
			SimpleDateFormat format = new SimpleDateFormat(dateType);
			Date current = format.parse(dateVal);
	        SimpleDateFormat daters = new SimpleDateFormat(valueType);
	        result = daters.format(current);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 比对：看后两个时间所在的区间是否与前两个时间所在的区间有交集
     * @param start
     * @param end
     * @param curStart
     * @param curEnd
     * @return
     */
    public static boolean matchTime(String start, String end, String curStart, String curEnd) {
		try {
			long stampStart = getTimeStamp(start, DEFAULT_DATETIME_FORMAT);
			long stampEnd = getTimeStamp(end, DEFAULT_DATETIME_FORMAT);
			long stampCurStart = getTimeStamp(curStart, DEFAULT_DATETIME_FORMAT);
			long stampCurEnd = getTimeStamp(curEnd, DEFAULT_DATETIME_FORMAT);;
			// 看起始时间是否在指定的区间范围内 
			boolean hasStart = false;
			if(stampStart <= stampCurStart && stampEnd >= stampCurStart){
				hasStart = true;
			}
			// 看结束时间是否在指定的区间范围内
			boolean hasEnd = false;
			if(stampStart <= stampCurEnd && stampEnd >= stampCurEnd){
				hasEnd = true;
			}
			
			boolean outStart = false;
			if(stampCurStart <= stampStart && stampCurEnd >= stampStart) {
				outStart = true;
			}
			boolean outEnd = false;
			if(stampCurStart <= stampEnd && stampCurEnd >= stampEnd) {
				outEnd = true;
			}
			
			if(!hasStart && !hasEnd && !outStart && !outEnd) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
    
    public static boolean beforeNow(String startDate){
		DateTime f=DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime(startDate);
		if(f.isAfterNow()){
			return false;
		}else{
			return true;
		}
	}
    
    public static String removeEndPointZero(String dateTime) {
        if (StringUtils.isNotBlank(dateTime)) {
            return dateTime.replace(".0", "");
        }
        return dateTime;
    }

    public static Date addMinutes(Date date, int minutes) {
        return add(date, minutes, Calendar.MINUTE);
    }
}