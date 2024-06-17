package com.dbt.framework.datadic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.bean.SysDicCategoryInfo;
import com.dbt.framework.datadic.dao.ISysDataDicDao;
import com.dbt.framework.datadic.dao.ISysDicCategoryInfoDao;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.securityauth.PermissionAnnotation;
import com.dbt.framework.securityauth.PermissionCode;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.RedisApiUtil;

@Service("sysDataDicService")
public class SysDataDicService extends BaseService<SysDataDic> {

	@Autowired
	private ISysDataDicDao sysDataDicDao;
	@Autowired
	private ISysDicCategoryInfoDao sysDicCategoryInfoDao;

	/**
	 * 查询数据字典列表
	 * 
	 * @param companykey
	 * @param PageOrderInfo
	 * @param SysDataDic
	 * @return list
	 **/
	public List<SysDataDic> findDataDicList(PageOrderInfo info, SysDataDic sysDataDic) {
		List<SysDataDic> list = new ArrayList<SysDataDic>();
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(info.getOrderCol()) && StringUtils.isEmpty(info.getOrderType())) {
			info.setOrderCol("sequence_num");
			info.setOrderType(PageOrderInfo.ORDER_ASC);
		}
		map.put("sysDataDic", sysDataDic);
		map.put("param", info);
		list = sysDataDicDao.findDataDicList(map);
		for (SysDataDic sysData : list) {
			String categoryKey = sysData.getCategoryKey();
			SysDicCategoryInfo dicCategoryInfo = sysDicCategoryInfoDao.findById(categoryKey);
			sysData.setCategoryName(dicCategoryInfo.getCategoryName());
			sysData.setCategoryCode(dicCategoryInfo.getCategoryCode());
			sysData.setDicType(dicCategoryInfo.getDicType());
		}
		return list;
	}

	/**
	 * 统计数据字典列表个数
	 * 
	 * @param companykey
	 * @return int
	 **/
	public int countDataDicList(SysDataDic sysDataDic) {
		return sysDataDicDao.countDataDicList(sysDataDic);
	}

	/**
	 * 保存数据字典
	 * 
	 * @param companykey
	 * @return int
	 * @throws Exception 
	 **/
	@PermissionAnnotation(code = PermissionCode.dataDic.ADD_DIC_DATA)
	public void saveDataDic(SysDataDic sysDataDic) throws Exception {
	    sysDataDic.setDataValue(StringUtils.trim(sysDataDic.getDataValue()));
		sysDataDicDao.create(sysDataDic);
		// 设置企业工厂
		if ("company_factory_name".equals(sysDataDic.getDataId())) {
			String factoryKey = CacheKey.cacheKey.company.KEY_COMPANY_FACTORY_INFO;
			String lua = "redis.call('hset',KEYS[1],KEYS[2],ARGV[1])";
			RedisApiUtil.getInstance().evalEx(lua, 2, factoryKey, DbContextHolder.getDBType(), sysDataDic.getDataValue());	
		} 
		// 定投大奖投放数量
		String dataId = StringUtils.defaultIfBlank(sysDataDic.getDataId(), "").toUpperCase();
		if (dataId.startsWith("FIRSTPRIZE_STATUS") && dataId.endsWith("FIXLIMIT")) {
		    if (StringUtils.isNotBlank(RedisApiUtil.getInstance().get(dataId))) {
		        throw new Exception("dataId已存在于Redis！");
			} else {
				if (StringUtils.isNotBlank(sysDataDic.getDataValue())) {
					RedisApiUtil.getInstance().incrBy(dataId, Long.valueOf(sysDataDic.getDataValue().split("_")[1]));
				}
			}
		}
		
		SysDicCategoryInfo sysDicCategoryInfo = sysDicCategoryInfoDao.findById(sysDataDic.getCategoryKey());
		CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT 
                + sysDicCategoryInfo.getCategoryCode().replace("_", ""));
		
		logService.saveLog("sysDataDic", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(sysDataDic), "新增数据字典");
	}

	/**
	 * 查看数据字典
	 * 
	 * @param dataDicKey
	 * @return int
	 **/
	public SysDataDic findById(String dataDicKey) {
		return sysDataDicDao.findById(dataDicKey);
	}

	/**
	 * 修改数据字典
	 * 
	 * @param SysDataDic
	 * @return int
	 * @throws Exception 
	 **/
	public void updateDataDic(SysDataDic sysDataDic){
        sysDataDic.setDataValue(StringUtils.trim(sysDataDic.getDataValue()));
		sysDataDicDao.update(sysDataDic);
        SysDicCategoryInfo sysDicCategoryInfo = sysDicCategoryInfoDao.findById(sysDataDic.getCategoryKey());
        
        // 设置企业工厂
        if("company_factory_name".equals(sysDataDic.getDataId())){
			String factoryKey = CacheKey.cacheKey.company.KEY_COMPANY_FACTORY_INFO;
			String lua="redis.call('hset',KEYS[1],KEYS[2],ARGV[1])";
			RedisApiUtil.getInstance().evalEx(lua,2, factoryKey, DbContextHolder.getDBType(), sysDataDic.getDataValue());	
		} 
        // 定投大奖投放数量
        String dataId = StringUtils.defaultIfBlank(sysDataDic.getDataId(), "").toUpperCase();
		if (dataId.startsWith("FIRSTPRIZE_STATUS") && dataId.endsWith("FIXLIMIT")) {
			String limitNum = StringUtils.defaultIfBlank(RedisApiUtil.getInstance().get(dataId), "0");
			if (StringUtils.isBlank(sysDataDic.getDataValue())) {
				log.error(dataId + "剩余数量由 " + limitNum + " 变更为 不限制");
				RedisApiUtil.getInstance().del(true, dataId);

			} else {
				log.error(dataId + "剩余数量由 " + limitNum + " 变更为 " + sysDataDic.getDataValue().split("_")[1]);
				RedisApiUtil.getInstance().set(dataId, sysDataDic.getDataValue().split("_")[1]);
			}
		}
        
        try {
            CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT 
                    + sysDicCategoryInfo.getCategoryCode().replace("_", "") + Constant.DBTSPLIT + sysDataDic.getDataId().replace("_", ""));
            CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT 
                    + sysDicCategoryInfo.getCategoryCode().replace("_", ""));
        } catch (Exception e) {
            log.error("删除数据字典缓存失败", e);
        }
        logService.saveLog("sysDataDic", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(sysDataDic), "修改数据字典");
	}
	
	/**
	 * 防并发更新数据字典
	 * 
	 * @param sysDataDic
	 * @return
	 * @throws Exception
	 */
	public boolean updateDataValueForRowNum(SysDataDic sysDataDic) throws Exception {
        int rowNum = sysDataDicDao.updateDataValueForRowNum(sysDataDic);
        SysDicCategoryInfo sysDicCategoryInfo = sysDicCategoryInfoDao.findById(sysDataDic.getCategoryKey());
        CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT 
                + sysDicCategoryInfo.getCategoryCode().replace("_", "") + Constant.DBTSPLIT + sysDataDic.getDataId().replace("_", ""));
        CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT 
                + sysDicCategoryInfo.getCategoryCode().replace("_", ""));
        return rowNum > 0 ? true : false;
	}

	/**
	 * 删除数据字典
	 * 
	 * @param String
	 *            []
	 * @return
	 **/
	@PermissionAnnotation(code = PermissionCode.dataDic.DEL_DIC_DATA)
	public void deleteDataDic(String dataDicKey) {
		// 更新缓存
		SysDataDic sysDataDic = sysDataDicDao.findById(dataDicKey);

		// 删除企业工厂
		if ("company_factory_name".equals(sysDataDic.getDataId())) {
			String factoryKey = CacheKey.cacheKey.company.KEY_COMPANY_FACTORY_INFO;
			String lua = "redis.call('hdel',KEYS[1],KEYS[2])";
			RedisApiUtil.getInstance().evalEx(lua, 2, factoryKey, DbContextHolder.getDBType());
		}
		SysDicCategoryInfo sysDicCategoryInfo = sysDicCategoryInfoDao.findById(sysDataDic.getCategoryKey());
		if (sysDataDic != null && sysDicCategoryInfo != null) {
			try {
				CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT
						+ sysDicCategoryInfo.getCategoryCode().replace("_", "") + Constant.DBTSPLIT
						+ sysDataDic.getDataId().replace("_", ""));
				CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT 
		                + sysDicCategoryInfo.getCategoryCode().replace("_", ""));
			} catch (Exception e) {
				log.error("删除数据字典缓存失败", e);
			}

			// 删除操作
			sysDataDicDao.deleteById(dataDicKey);
		}
		logService.saveLog("sysDataDic", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(sysDataDic), "删除数据字典");

	}

	/**
	 * 根据类型Code获取数据信息
	 * 
	 * @param categoryCode
	 * @return
	 */
	public List<SysDataDic> getDataDicByCagegoryCode(String categoryCode) {
		List<SysDataDic> dataDicList = this.sysDataDicDao
				.findDataDicListByCategoryCode(categoryCode);
		return dataDicList;
	}

	/**
	 * 通过字典类型和dataid获取数据字典中的数据
	 * 
	 * @param categoryCode
	 * @param dataid
	 * @return
	 */
	public SysDataDic getDatadicByCatCodeAndDataid(String categoryCode, String dataid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("categoryCode", categoryCode);
		map.put("dataid", dataid);
		return this.sysDataDicDao.getDatadicByCatCodeAndDataid(map);
	}

	/**
	 * 根据ID更新数据字典
	 * 
	 * @param sysDataDic
	 * @return
	 * @throws Exception 
	 */
	public void updateDataDicByDataId(SysDataDic sysDataDic) throws Exception {

		this.sysDataDicDao.updateDataDicByDataId(sysDataDic);
		
		SysDicCategoryInfo sysDicCategoryInfo = sysDicCategoryInfoDao.findById(sysDataDic.getCategoryKey());
		CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT 
                + sysDicCategoryInfo.getCategoryCode().replace("_", ""));
	}
}
