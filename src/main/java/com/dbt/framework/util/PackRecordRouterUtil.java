/**
 * <pre>
 * Copyright Digital Bay Technology Group. Co. Ltd.All Rights Reserved.
 *
 * Original Author: sunshunbo
 *
 * ChangeLog:
 * 2016-11-1 by sunshunbo create
 * </pre>
 */
package com.dbt.framework.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 分表工具类.
 */
public class PackRecordRouterUtil {
	
	public static final int HN_SPLIT_DEMISION=24;
	
	/**
	 * 按照分表纬度获取所有分表后缀.
	 *
	 * @param 分表数目
	 * @return 结果集如["_1","_3"]
	 */
	public static List<String> getAllSplitTabSuffixList(int splitdemision) {
		List<String> splitTabList = new ArrayList<String>();
		for (int i = 1; i <= splitdemision; i++) {
			splitTabList.add(i+"");
		}
		return splitTabList;
	}
	
	/**
	 * 按照分表纬度获取当前时间对应分表后缀.
	 *
	 * @param 分表数目
	 * @return 结果如"_1"
	 */
	public static String getTabSuffixCurrent(int splitdemision){
		return getTabSuffixBySplitDemision(splitdemision, new Date());
	}
	
	
	/**
	 * 按照分表纬度和日期获取对应分表后缀.
	 *
	 * @param 分表数目
	 * @param 日期 如"2016-10-01"
	 * @return 结果如"_1"
	 */
	public static String getTabSuffixByDate(int splitdemision,String date){
		Date dateT=null;
		try {
			dateT = DateUtil.parse(date, DateUtil.DEFAULT_DATE_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getTabSuffixBySplitDemision(splitdemision, dateT);
	}
	
	/**
	 * 按照分表纬度和起始日期获取区间内分表纬度.
	 *
	 * @param 分表数目
	 * @param 起始日期 如"2016-10-01"
	 * @param 截止日期 如"2016-11-01"
	 * @return ["_1","_3"]
	 */
	public static List<String> getTabSuffixByDate(int splitdemision,String dateStart,String dateEnd) {
		Date dateS=null;
		Date dateE=null;
		try {
			dateS = DateUtil.parse(dateStart, DateUtil.DEFAULT_DATE_FORMAT);
			dateE=DateUtil.parse(dateEnd, DateUtil.DEFAULT_DATE_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String startSuffix=getTabSuffixBySplitDemision(splitdemision, dateS);
		String endSuffix=getTabSuffixBySplitDemision(splitdemision, dateE);
		int start=Integer.parseInt(startSuffix);
		int end=Integer.parseInt(endSuffix);
		
		Calendar calendar = Calendar.getInstance(); 
	    //得到第一个日期的年分和月份数
	    calendar.setTime(dateS);
	    int yearS = calendar.get(Calendar.YEAR);
	    calendar.setTime(dateE);
	    int yearE = calendar.get(Calendar.YEAR);
	    
	    List<String> suffixList=new ArrayList<String>();
	    if(yearS==yearE){
	      //不跨年
	      for(int i=start;i<=end;i++){
	    	  suffixList.add(i+"");
	      }
	    }else{
	      //跨年
	      int df=DateUtil.diffMonths(dateE, dateS);
	      if(df>=12){
	    	  //跨整年
	    	  suffixList=getAllSplitTabSuffixList(splitdemision);
	      }else{
	    	  //未跨整年
	    	  for(int i=start;i<=splitdemision;i++){
		    	  suffixList.add(i+"");
		      }
	    	  for(int i=1;i<=end;i++){
		    	  suffixList.add(i+"");
		      }
	      }
	    }
		return suffixList;
	}
	
	/**
	 * 按照分表纬度和日期对象获取分表后缀(基础方法).
	 *
	 * @param splitdemision 分表数目
	 * @param date 日期 如"2016-10-01"
	 * @return 结果如"_1"
	 * @throws PackRecordRouterException the pack record router exception
	 */
	private static String getTabSuffixBySplitDemision(int splitdemision,
			Date date) throws PackRecordRouterException {
		//分表不合理
		if ((12 % splitdemision != 0 && splitdemision % 12 != 0)
				&& (splitdemision != 365 && splitdemision != 366)) {
			throw new PackRecordRouterException(
					PackRecordRouterException.EXCEPTION_MSG.SPLICT_EXCEPTION_MSG);
		}
		// 分表范围过大
		if (splitdemision > 366) {
			throw new PackRecordRouterException(
					PackRecordRouterException.EXCEPTION_MSG.SPLICT_RANGE_EXCEPTION_MSG);

		}

		int realSplitDemision = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int currentDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		if (splitdemision == 1) {
			return splitdemision+"";
		} else if (splitdemision == 365 || splitdemision == 366) {
			return currentDayOfYear+"";
		}

		// 按照月份来分表
		if (12 % splitdemision == 0) {
			int currentMonth = cal.get(Calendar.MONTH) + 1;
			int monthDiff = 12 / splitdemision;
			for (int i = 1; i <= splitdemision; i++) {
				if (currentMonth <= monthDiff * i) {
					realSplitDemision = i;
					break;
				}
			}
		} else {
			// 按照日期来分表
			int monthlySplitTabNum = splitdemision / 12;// 每个月分表数量
			// 多少天分一个表，如果为小数，则取整数
			int daysTabNum = 30 / monthlySplitTabNum;

			for (int i = 1; i <= splitdemision; i++) {
				if (currentDayOfYear <= daysTabNum * i) {
					realSplitDemision = i;
					break;
				}
			}
			// 当天超出范围，则放入最后一张表
			if ((splitdemision * daysTabNum) < currentDayOfYear) {
				realSplitDemision = splitdemision;
			}

		}
		return realSplitDemision+"";

	}
	
	/**
	 * Gets the diff date.
	 *
	 * @param date 日期 如"2016-10-01"
	 * @return 指定日期-当前日期的差值 天
	 */
	@Deprecated
	public static int getDiffDate(String date) {
		Date oneDate = null;
		try {
			oneDate = DateUtil.parse(date, DateUtil.DEFAULT_DATE_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date twoDate = new Date();
		return (int) DateUtil.diffDays(oneDate, twoDate);
	}

	 /**
     * 根据UUI获取表.
     */
    public static String getTabSuffixByHash(int splitdemision, String uuidStr){
        return String.valueOf(Math.abs(uuidStr.hashCode()) % splitdemision + 1);
    }

	/**
	 * 根据UUID首字母的ASC码获取表.
	 * UUID首字母包含[0123456789abcdefghijklmnopqrstuvwxyz]
	 * 分表个数必须被36整除（1 2 3 4 9 12 18 36）
	 * @param 分表数目
	 * @return 结果如"1"
	 */
	public static String getTabSuffixByUuid(int splitdemision, String uuidStr){
		// 无法被36整除，异常返回-1
		if(36 % splitdemision != 0){
			return "-1";
		}

		// 获取首字母asc值
		int asc = AscUtil.getAsc(uuidStr.substring(0,1));
		if(splitdemision == 1 || splitdemision == 2 || splitdemision == 3
				|| splitdemision == 4 || splitdemision == 12){
			if(asc == 121 || asc == 122){
				asc += 9;
			}
		}else if(splitdemision == 9){
			if(asc == 120){
				asc += 3;
			}
		}else if(splitdemision == 18){
			if(asc == 120 || asc == 121 || asc == 122){
				asc -= 8;
			}
		}else if(splitdemision == 36){
			if(asc == 120 || asc == 121 || asc == 122){
				asc += 10;
			}
		}
		int tableIdx = asc % splitdemision + 1;
		return String.valueOf(tableIdx);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
//	public static void main(String[] args) {
//		System.out.println(PackRecordRouterUtil.getTabSuffixCurrent(1));
//		System.out.println(PackRecordRouterUtil.getTabSuffixCurrent(3));
//		System.out.println(PackRecordRouterUtil.getTabSuffixCurrent(12));
//		System.out.println(PackRecordRouterUtil.getTabSuffixCurrent(24));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(12, "2016-01-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(12, "2016-08-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(12, "2017-03-01"));
//		System.out.println(PackRecordRouterUtil.getAllSplitTabSuffixList(24));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(12, "2016-01-10", "2016-05-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(12, "2016-01-10", "2016-01-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(12, "2016-03-10", "2017-01-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(12, "2016-03-10", "2017-03-01"));
//		
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(24, "2016-01-10", "2016-05-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(24, "2016-01-10", "2016-01-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(24, "2016-03-10", "2017-01-20"));
//		System.out.println(PackRecordRouterUtil.getTabSuffixByDate(24, "2016-03-10", "2017-03-01"));
//	}
}
