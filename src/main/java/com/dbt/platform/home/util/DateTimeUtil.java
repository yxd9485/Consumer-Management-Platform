package com.dbt.platform.home.util;

import org.joda.time.DateTime;

public class DateTimeUtil {
	/**
	 * 获取上周周一
	 * @return
	 */
	public static String getLastWeek(){
		DateTime date=new DateTime();
		date=date.plusWeeks(-1).plusDays(-(date.getDayOfWeek()-1));
		return date.toString("yyyy-MM-dd");
	}
	/**
	 * 获取参数日期周的周一
	 * @param queryDay
	 * @return
	 */
	public static String getDayOfWeek(String queryDay){
		DateTime date=new DateTime(queryDay);
		date=date.plusDays(-(date.getDayOfWeek()-1));
		return date.toString("yyyy-MM-dd");
	}
	/**
	 * 计算日期
	 * @param day
	 * @param i
	 * @return
	 */
	public static String plusDays(String day,int i){
		DateTime date=new DateTime(day);
		date=date.plusDays(i);
		return date.toString("yyyy-MM-dd");
	}
	/**
	 * 计算日期
	 * @param day
	 * @param i
	 * @return
	 */
	public static String plusWeeks(String day,int i){
		DateTime date=new DateTime(day);
		date=date.plusWeeks(i);
		return date.toString("yyyy-MM-dd");
	}
	/**
	 * 计算日期
	 * @param day
	 * @param i
	 * @return
	 */
	public static String plusMonths(String day,int i){
		DateTime date=new DateTime(day);
		date=date.plusMonths(i);
		return date.toString("yyyy-MM-dd");
	}
	public static String plusYears(String day,int i){
		DateTime date=new DateTime(day);
		date=date.plusYears(i);
		return date.toString("yyyy-MM-dd");
	}
	/**
	 * 获取昨日
	 * @return
	 */
	public  static String getLastDay(){
		DateTime date=new DateTime();
		date=date.plusDays(-1);
		return date.toString("yyyy-MM-dd");
	}
	/**
	 * 获取上月1号
	 * @return
	 */
	public static String getLastMonth(){
		DateTime date=new DateTime();
		date=date.plusDays(-(date.getDayOfMonth()-1)).plusMonths(-1);
		return date.toString("yyyy-MM-dd");
	}
	/**
	 * 获取上月1号
	 * @return
	 */
	public static String getFirstOfMonth(String queryDate){
		DateTime date=new DateTime(queryDate);
		date=date.plusDays(-(date.getDayOfMonth()-1));
		return date.toString("yyyy-MM-dd");
	}
	/**
     * 获取当前季度
     * @param quarterPlus
     * @return
     */
    public static int getQuarter(){
    	DateTime date=new DateTime();
		int month=date.getMonthOfYear();
		int quarter=0;
	        if(month>=1&&month<=3){     
	        	quarter= 1;    
	        }     
	        if(month>=4&&month<=6){     
	        	quarter= 2;      
	        }     
	        if(month>=7&&month<=9){     
	        	quarter= 3;  
	        }     
	        if(month>=10&&month<=12){     
	        	quarter= 4;  
	        }
	        return quarter;
	}
    /**
	 * 获取昨日
	 * @return
	 */
	public  static String getMainLastDay(){
		DateTime date=new DateTime();
		if(date.getHourOfDay()>7){
			date=date.plusDays(-1);
		}else{
			date=date.plusDays(-2);
		}
		String lastDay=date.toString("yyyy-MM-dd");
		return lastDay;
	}
	public static void main(String[] args) {
		System.out.println(getLastDay());
		System.out.println(getLastWeek());
		System.out.println(getLastMonth());
		System.out.println(plusDays(getLastDay(), -13));
		System.out.println(getDayOfWeek("2017-01-05"));
	}
	public static String lastYearWeek(String day){
		DateTime date=new DateTime();
		date=date.plusYears(-1);
		date=date.plusDays(-(date.getDayOfWeek()-1));
		return date.toString("yyyy-MM-dd");
	}
}
