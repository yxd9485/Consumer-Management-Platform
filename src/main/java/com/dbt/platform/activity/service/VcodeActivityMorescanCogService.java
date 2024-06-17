package com.dbt.platform.activity.service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.VcodeActivityMorescanCog;
import com.dbt.platform.activity.dao.IVcodeActivityMorescanCogDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 一码多扫规则Service
 * @author hanshimeng
 *
 */
@Service
public class VcodeActivityMorescanCogService extends BaseService<VcodeActivityMorescanCog>{
	@Autowired
	private IVcodeActivityMorescanCogDao morescanCogDao;

	/**
	 * 获取列表List
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 */
	public List<VcodeActivityMorescanCog> queryForList(
			VcodeActivityMorescanCog queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return morescanCogDao.queryForList(map);
	}

	/**
	 * 获取列表Count
	 * @param queryBean
	 * @return
	 */
	public int queryForCount(VcodeActivityMorescanCog queryBean) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		return morescanCogDao.queryForCount(map);
	}

	/**
	 * 根据主键查询信息
	 * @param infoKey
	 * @return
	 */
	public VcodeActivityMorescanCog findById(String infoKey) {
		return morescanCogDao.findById(infoKey);
	}

	/**
	 * 添加一码多扫规则
	 * @param morescanCog
	 */
	public void doMorescanCogAdd(VcodeActivityMorescanCog morescanCog) throws Exception{
		if(StringUtils.isNotBlank(morescanCog.getVcodeActivityKey())
				&& morescanCog.getVcodeActivityKey().indexOf("@") > -1){
			morescanCog.setVcodeActivityKey(morescanCog.getVcodeActivityKey().split("@")[0]);
		}

		// 生成编号
		morescanCog.setMorescanNo(
				getBussionNo("vcodeActivityMorescanCog", "morescan_no", Constant.OrderNoType.type_DS));
		
		// 获取最小和最大日期
		morescanCog = queryMinMaxDate(morescanCog);

	//	checkDate(morescanCog);

		String key = StringUtils.isNotBlank(DbContextHolder.getDBType()) ? DbContextHolder.getDBType() : "dataSource";
		// 缓存key
		String cacheKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_MORESCAN_COGS + Constant.DBTSPLIT + key;
		CacheUtilNew.removeGroupByKey(cacheKey);
		// 保存
		morescanCogDao.create(morescanCog);
		logService.saveLog("vcodeActivityMorescanCog", Constant.OPERATION_LOG_TYPE.TYPE_1,
		        JSON.toJSONString(morescanCog), "添加一码多扫规则:" + morescanCog.getMorescanName());
	}

	/**
	 * 修改一码多扫记录
	 * @param morescanCog
	 */
	public void doMorescanCogEdit(VcodeActivityMorescanCog morescanCog) throws Exception {
		if(StringUtils.isNotBlank(morescanCog.getVcodeActivityKey())
				&& morescanCog.getVcodeActivityKey().indexOf("@") > -1){
			morescanCog.setVcodeActivityKey(morescanCog.getVcodeActivityKey().split("@")[0]);
		}
		
		// 获取最小和最大日期
		morescanCog = queryMinMaxDate(morescanCog);

	//	checkDate(morescanCog);

		String key = StringUtils.isNotBlank(DbContextHolder.getDBType()) ? DbContextHolder.getDBType() : "dataSource";
		// 缓存key
		String cacheKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_MORESCAN_COGS + Constant.DBTSPLIT + key;
		CacheUtilNew.removeGroupByKey(cacheKey);
		morescanCogDao.update(morescanCog);
		logService.saveLog("vcodeActivityMorescanCog", Constant.OPERATION_LOG_TYPE.TYPE_2,
		        JSON.toJSONString(morescanCog), "修改一码多扫规则:" + morescanCog.getMorescanName());
	}
	
	/**
	 * 删除记录
	 * @param infoKey
	 * @param optUserKey
	 */
	public String doMorescanCogDelete(String infoKey, String updateUser) {
		String vcodeActivityKey = null;
		VcodeActivityMorescanCog morescan =  findById(infoKey);
		if(morescan != null){
			vcodeActivityKey = morescan.getVcodeActivityKey();
			Map<String, Object> map = new HashMap<>();
		    map.put("infoKey", infoKey);
		    map.put("optUserKey", updateUser);
		    morescanCogDao.deleteById(map);
		    logService.saveLog("vcodeActivityPerhundredCogCog", 
		    		Constant.OPERATION_LOG_TYPE.TYPE_3, null, "删除一码多扫规则:" + infoKey);
		}
		return vcodeActivityKey;
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("vcodeActivityMorescanCog", "info_key", infoKey, "morescan_name", bussionName);
	}
	
	/**
	 * 获取最小和最大日期
	 * @param morescanCog
	 * @return
	 */
	private VcodeActivityMorescanCog queryMinMaxDate(VcodeActivityMorescanCog morescanCog) {
		String[] validDateRangeGro = morescanCog.getValidDateRange().split(",");
		if(validDateRangeGro.length == 1){
			morescanCog.setMinValidDate(validDateRangeGro[0].split("#")[0]);
			morescanCog.setMaxValidDate(validDateRangeGro[0].split("#")[1]);
		}else{
			List<String> minDateList = new ArrayList<>();
			List<String> maxDateList = new ArrayList<>();
			for (int i = 0; i < validDateRangeGro.length; i++) {
				minDateList.add(validDateRangeGro[i].split("#")[0]);
				maxDateList.add(validDateRangeGro[i].split("#")[1]);
			}
			Collections.sort(minDateList);
			Collections.sort(maxDateList);
			morescanCog.setMinValidDate(minDateList.get(0));
			morescanCog.setMaxValidDate(maxDateList.get(maxDateList.size()-1));
		}
		return morescanCog;
	}
	
	/**
	 * 获取该活动下一码多扫有效规则
	 * 
	 * @param vcodeActivityKey
	 * @return
	 */
	public VcodeActivityMorescanCog findValidByActivityKey(String vcodeActivityKey) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("vcodeActivityKey", vcodeActivityKey);
	    return morescanCogDao.findValidByActivityKey(map);
	}


	/**
	 * 判断同一个活动是否有日期重复
	 *
	 * @param morescanCog
	 * @return
	 */
	private void checkDate(VcodeActivityMorescanCog morescanCog) throws Exception{
		//判断同一个活动是否有日期重复
		List<VcodeActivityMorescanCog> existList = morescanCogDao.getByActivityKey(morescanCog.getVcodeActivityKey());
		if (existList != null && existList.size() != 0) {
			//需要新增或编辑的时间区间
			String[] newDateRange = morescanCog.getValidDateRange().split(",");
			for (VcodeActivityMorescanCog existMorescanCog : existList) {
				//编辑时跳过本条记录的校验
				if (existMorescanCog.getInfoKey().equals(morescanCog.getInfoKey())){
					continue;
				}
				//已存在的时间区间
				String[] existDateRange = existMorescanCog.getValidDateRange().split(",");

				for (String existDateRangeEach:existDateRange){
					for (String newDateRangeEach:newDateRange){
						//新增的开始时间
						Date startDateNew = DateUtil.parse(newDateRangeEach.split("#")[0], null);
						//新增的结束时间
						Date endDateNew = DateUtil.parse(newDateRangeEach.split("#")[1],null);
						//已存在的开始时间
						Date startDateExist = DateUtil.parse(existDateRangeEach.split("#")[0], null);
						//已存在的结束时间
						Date endDateExist = DateUtil.parse(existDateRangeEach.split("#")[1], null);
						//判断已存在的开始日期大于新增的结束日期，或者新增的开始日期大于已存在的结束日期，则代表没有重复
						if (startDateExist.compareTo(endDateNew) <= 0 && startDateNew.compareTo(endDateExist) <= 0) {
							throw new Exception("该活动该时间内已有一码多扫规则！");
						}
					}
				}
			}
		}

	}
}
