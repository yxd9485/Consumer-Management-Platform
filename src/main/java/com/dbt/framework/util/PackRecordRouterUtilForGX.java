package com.dbt.framework.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicUtil;

/**
 * 红包领取记录分表规则
 * 
 * @author:Jiquanwei<br>
 * @date:2016-4-26 下午01:36:51<br>
 * @version:1.0.0<br>
 * 
 */
public class PackRecordRouterUtilForGX {
	
	/**
	 * 根据当前时间判断路由
	 * @addTime 2016-10-18 14:46:00
	 * @param currentDate</br> 
	 * @return String </br>
	 */
	public static String getRecordRouter(String currentDate) throws Exception {
		boolean isFlag = false;
		String splitKey = null;
		Integer nowMonth = LocalDate.parse(currentDate).getMonthOfYear();
		String keyBuffer = CacheUtilNew.cacheKey.vcodeScanCounts.KEY_VCODE_SCAN_SPLIT_TABLE; 
		Object cacheSplitKey = CacheUtilNew.getCacheValue(keyBuffer);
		if(null != cacheSplitKey){
			String[] splitValue = cacheSplitKey.toString().split(Constant.DBTSPLIT);
			if(splitValue.length >1){
				Integer cahceMonth = Integer.parseInt(splitValue[0]);
				String cahceValue = splitValue[1];
				if(nowMonth == cahceMonth){
					splitKey = cahceValue;
					isFlag = true;
				}
			}
		}
		
		if(!isFlag){
			String firstDuringDate = DatadicUtil.getDataDicValue(
					DatadicUtil.dataDicCategory.PACK_RECORD_ROUTER,
					DatadicUtil.dataDic.packRecordRouter.FIRST_PACK_RECORD_ROUTER_NEW);
			String secondDuringDate = DatadicUtil.getDataDicValue(
					DatadicUtil.dataDicCategory.PACK_RECORD_ROUTER,
					DatadicUtil.dataDic.packRecordRouter.SECOND_PACK_RECORD_ROUTER_NEW);
			String thirdDuringDate = DatadicUtil.getDataDicValue(
					DatadicUtil.dataDicCategory.PACK_RECORD_ROUTER,
					DatadicUtil.dataDic.packRecordRouter.THIRD_PACK_RECORD_ROUTER_NEW);
			String fourDuringDate = DatadicUtil.getDataDicValue(
					DatadicUtil.dataDicCategory.PACK_RECORD_ROUTER,
					DatadicUtil.dataDic.packRecordRouter.FOUR_PACK_RECORD_ROUTER_NEW);
			if (StringUtils.isNotBlank(firstDuringDate) 
					&& StringUtils.isNotBlank(secondDuringDate)
					&& StringUtils.isNotBlank(thirdDuringDate)
					&& StringUtils.isNotBlank(fourDuringDate)) {
				
				List<String[]> monthGroupList = new ArrayList<String[]>();
				monthGroupList.add(firstDuringDate.split(","));
				monthGroupList.add(secondDuringDate.split(","));
				monthGroupList.add(thirdDuringDate.split(","));
				monthGroupList.add(fourDuringDate.split(","));
				
				for (int i = 0; i < monthGroupList.size() && !isFlag; i++) {
					String[] monthGroup = monthGroupList.get(i);
					for (int x = 0; x < monthGroup.length; x++) {
						if(nowMonth == Integer.parseInt(monthGroup[x])){
							splitKey = getTableSuffix(i + 1);
							isFlag = true;
							break;
						}
					}
				}
				CacheUtilNew.setCacheValue(keyBuffer, nowMonth + Constant.DBTSPLIT +splitKey);
			}
		}
		return splitKey;
	}
	
    /**
     * 获取指定时间段包含的分表索引
     * 
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @return
     */
    public static List<String> getRecordRouter(String startDate, String endDate) {
        List<String> indexLst = new ArrayList<String>();
        int startIndex = -1, endIndex = -1;
        try {
            startIndex = Integer.valueOf(getRecordRouter(startDate));
            endIndex = Integer.valueOf(getRecordRouter(endDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (startIndex > -1) {
            if (startIndex > endIndex) {
                endIndex += 4;
            }
            for (int i = startIndex; i <= endIndex; i++) {
                indexLst.add(String.valueOf(i % 4));
            }
        }
        return indexLst;
    }
    
    private static String getTableSuffix(int tableSuffix) {
		if(tableSuffix == 4){
			tableSuffix = 0;
		}
		return tableSuffix + "";
	}
}
