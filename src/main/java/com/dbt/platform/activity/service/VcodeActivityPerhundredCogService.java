package com.dbt.platform.activity.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityPerhundredCog;
import com.dbt.platform.activity.dao.IVcodeActivityPerhundredCogDao;

/**
 * 逢百规则Service
 * @author hanshimeng
 *
 */
@Service
public class VcodeActivityPerhundredCogService extends BaseService<VcodeActivityPerhundredCog>{
	@Autowired
	private IVcodeActivityPerhundredCogDao perhundredCogDao;

	/**
	 * 获取列表List
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public List<VcodeActivityPerhundredCog> queryForList(
			VcodeActivityPerhundredCog queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		List<VcodeActivityPerhundredCog> list = perhundredCogDao.queryForList(map);
		if(CollectionUtils.isNotEmpty(list)){
		    // 时间类型
	        String timeType = "";
			for (VcodeActivityPerhundredCog item : list) {
			    if("1".equals(item.getRestrictTimeType())){
			        timeType = Constant.DBTSPLIT + DateUtil.getDate();
			    }
				item.setRuleTotalBottle(Integer.parseInt(StringUtils.defaultIfBlank(
						RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.dotRedpacketCog.DOT_RULE_TOTAL_PRIZE 
								+ Constant.DBTSPLIT + item.getInfoKey() + timeType + "bottle"), "0")));
				item.setRuleTotalMoney(Double.parseDouble(StringUtils.defaultIfBlank(
						RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.dotRedpacketCog.DOT_RULE_TOTAL_PRIZE 
								+ Constant.DBTSPLIT + item.getInfoKey() + timeType + "money"), "0.00")));
				item.setRuleTotalVpoints(Integer.parseInt(StringUtils.defaultIfBlank(
						RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.dotRedpacketCog.DOT_RULE_TOTAL_PRIZE 
								+ Constant.DBTSPLIT + item.getInfoKey() + timeType + "vpoint"), "0")));
			}
		}
		return list;
	}

	/**
	 * 获取列表Count
	 * @param queryBean
	 * @return
	 */
	public int queryForCount(VcodeActivityPerhundredCog queryBean) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		return perhundredCogDao.queryForCount(map);
	}

	/**
	 * 根据主键查询信息
	 * @param infoKey
	 * @return
	 */
	public VcodeActivityPerhundredCog findById(String infoKey) {
		return perhundredCogDao.findById(infoKey);
	}

	/**
	 * 添加
	 * @param perhundredCog
	 */
	public void addPerhundredCog(VcodeActivityPerhundredCog perhundredCog) throws Exception{
		if(StringUtils.isNotBlank(perhundredCog.getVcodeActivityKey())
				&& perhundredCog.getVcodeActivityKey().indexOf("@") > -1){
			perhundredCog.setVcodeActivityKey(perhundredCog.getVcodeActivityKey().split("@")[0]);
		}
		
		// 生成编号
		perhundredCog.setInfoKey(UUID.randomUUID().toString());
		perhundredCog.setPerhundredNo(getBussionNo(
		        "vcodeActivityPerhundredCog", "perhundred_no", Constant.OrderNoType.type_FB));
		
		// 获取最小和最大日期
		perhundredCog = queryMinMaxDate(perhundredCog);
		
		// 倍数数据排序
		perhundredCog = dealPerItemsSort(perhundredCog);
		
		// 保存
		perhundredCogDao.create(perhundredCog);
		logService.saveLog("vcodeActivityPerhundredCog", Constant.OPERATION_LOG_TYPE.TYPE_1,
		        JSON.toJSONString(perhundredCog), "添加逢百规则:" + perhundredCog.getPerhundredName());
	}

	/**
	 * 修改
	 * @param perhundred
	 */
	public void editPerhundredCog(VcodeActivityPerhundredCog perhundred) {
		if(StringUtils.isNotBlank(perhundred.getVcodeActivityKey())
				&& perhundred.getVcodeActivityKey().indexOf("@") > -1){
			perhundred.setVcodeActivityKey(perhundred.getVcodeActivityKey().split("@")[0]);
		}
		
		// 获取最小和最大日期
		perhundred = queryMinMaxDate(perhundred);
		
		// 倍数数据排序
		perhundred = dealPerItemsSort(perhundred);
		
		perhundredCogDao.update(perhundred);
		logService.saveLog("vcodeActivityPerhundredCog", Constant.OPERATION_LOG_TYPE.TYPE_2,
		        JSON.toJSONString(perhundred), "修改逢百规则:" + perhundred.getPerhundredName());
	}
	
	/**
	 * 删除记录
	 * @param infoKey
	 * @param optUserKey
	 */
	public String deletePerhundredCog(String infoKey, String updateUser) {
		VcodeActivityPerhundredCog perhundred = findById(infoKey);
		if(null != perhundred){
			Map<String, Object> map = new HashMap<>();
		    map.put("infoKey", infoKey);
		    map.put("optUserKey", updateUser);
		    perhundredCogDao.deleteById(map);
		    logService.saveLog("vcodeActivityPerhundredCog", 
		    		Constant.OPERATION_LOG_TYPE.TYPE_3, null, "删除逢百规则:" + infoKey);
		}
	    return perhundred.getVcodeActivityKey();
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("vcodeActivityPerhundredCog", "info_key", infoKey, "perhundred_name", bussionName);
	}
	
	/**
	 * 获取最小和最大日期
	 * @param perhundredCog
	 * @return
	 */
	private VcodeActivityPerhundredCog queryMinMaxDate(VcodeActivityPerhundredCog perhundredCog) {
		String[] validDateRangeGro = perhundredCog.getValidDateRange().split(",");
		if(validDateRangeGro.length == 1){
			perhundredCog.setMinValidDate(validDateRangeGro[0].split("#")[0]);
			perhundredCog.setMaxValidDate(validDateRangeGro[0].split("#")[1]);
		}else{
			List<String> minDateList = new ArrayList<>();
			List<String> maxDateList = new ArrayList<>();
			for (int i = 0; i < validDateRangeGro.length; i++) {
				minDateList.add(validDateRangeGro[i].split("#")[0]);
				maxDateList.add(validDateRangeGro[i].split("#")[1]);
			}
			Collections.sort(minDateList);
			Collections.sort(maxDateList);
			perhundredCog.setMinValidDate(minDateList.get(0));
			perhundredCog.setMaxValidDate(maxDateList.get(maxDateList.size()-1));
		}
		return perhundredCog;
	}
	
	/**
	 * 倍数数据排序
	 * @param perhundredCog
	 * @return
	 */
	private VcodeActivityPerhundredCog dealPerItemsSort(VcodeActivityPerhundredCog perhundredCog) {
		// 格式：10:100.00:20;30:300.00:0;20:200.00:10
		String[] items = perhundredCog.getPerItems().split(";");
		Arrays.sort(items, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.parseInt(o1.split(":")[0]) - Integer.parseInt(o2.split(":")[0]); 
			}
		});
		perhundredCog.setPerItems(StringUtils.join(items,";"));
		return perhundredCog;
	}

    
    /**
     * 获取该活动下逢百有效规则
     * 
     * @param vcodeActivityKey
     * @return
     */
    public VcodeActivityPerhundredCog findValidByActivityKey(String vcodeActivityKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("vcodeActivityKey", vcodeActivityKey);
        return perhundredCogDao.findValidByActivityKey(map);
    }
}
