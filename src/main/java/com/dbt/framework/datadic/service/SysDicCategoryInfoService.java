package com.dbt.framework.datadic.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.bean.SysDicCategoryInfo;
import com.dbt.framework.datadic.dao.ISysDataDicDao;
import com.dbt.framework.datadic.dao.ISysDicCategoryInfoDao;
import com.dbt.framework.securityauth.PermissionAnnotation;
import com.dbt.framework.securityauth.PermissionCode;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
/**
* 文件名: DatadicService
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.
* 描述: 数据字典service
* 修改人: HaoQi
* 修改时间：2014-03-18 13:40:45
* 修改内容：新增
*/
@Service("sysDicCategoryInfoService")
public class SysDicCategoryInfoService extends BaseService<SysDicCategoryInfo>{

	@Autowired
	private ISysDicCategoryInfoDao iSysDicCategoryInfoDao;

	@Autowired
	private ISysDataDicDao iSysDataDicDao;

	/**
	 * 查询分页数据
	 * @param map
	 * @return
	 */
	public List<SysDicCategoryInfo> findPageList(Map<String, Object> map) {
		return iSysDicCategoryInfoDao.findPageList(map);
	}

	/**
	 * 查询总数
	 * @param map
	 * @return
	 */
	public int countAll(Map<String, Object> map) {
		return iSysDicCategoryInfoDao.countAll(map);
	}

	/**
	 * 获取所有字典类型
	 * @return
	 */
	public List<SysDicCategoryInfo> findAll(Map<String,Object> map){
		return iSysDicCategoryInfoDao.findAll(map);
	}

	/**
	 * 添加类型
	 * @param category
	 */
	@PermissionAnnotation(code=PermissionCode.dataDic.ADD_DIC_CATEGORY)
	public void addCategory(SysDicCategoryInfo category, SysUserBasis sysUserBasis) {
		category.setCategoryKey(callUUID());
		category.setCreateTime(DateUtil.getDateTime());
		category.setCreateUser(sysUserBasis.getUserKey());
		category.setUpdateTime(DateUtil.getDateTime());
		category.setUpdateUser(sysUserBasis.getUserKey());

		iSysDicCategoryInfoDao.insertCategory(category);
	}

	public SysDicCategoryInfo findById(String categoryKey) {
		return iSysDicCategoryInfoDao.findById(categoryKey);
	}

	/**
	 * 更新类型
	 * @param category
	 * @param sysUserBasis
	 */
	@PermissionAnnotation(code=PermissionCode.dataDic.EDIT_DIC_CATEGORY)
	public void editCategory(SysDicCategoryInfo category, SysUserBasis sysUserBasis) {
		SysDicCategoryInfo temp = this.findById(category.getCategoryKey());
		//获取类型下的字典循环删除
		List<SysDataDic> dataDicList=iSysDataDicDao.findDataDicListByCategoryKey(category.getCategoryKey());
		if(dataDicList!=null&&dataDicList.size()>0){
            try {
                for(SysDataDic dataDic:dataDicList){
                    CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT
                            + temp.getCategoryCode().replace("_", "") + Constant.DBTSPLIT + dataDic.getDataId().replace("_", ""));
                }
                CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM
                        + Constant.DBTSPLIT  + temp.getCategoryCode().replace("_", ""));
            } catch (Exception e) {
                log.error("删除数据字典缓存失败", e);
            }
		}
		category.setUpdateTime(DateUtil.getDateTime());
		category.setUpdateUser(sysUserBasis.getUserKey());

		iSysDicCategoryInfoDao.updateCategory(category);
	}

	/**
	 * 根据ID删除类型
	 * @param categoryKey
	 * @param sysUserBasis
	 */
	@PermissionAnnotation(code=PermissionCode.dataDic.DEL_DIC_CATEGORY)
	public void deleteCategoryById(String categoryKey, SysUserBasis sysUserBasis) {
		SysDicCategoryInfo category = this.findById(categoryKey);
		//获取类型下的字典循环删除
		List<SysDataDic> dataDicList=iSysDataDicDao.findDataDicListByCategoryKey(categoryKey);
		if(dataDicList!=null&&dataDicList.size()>0){
		    try {
    			for(SysDataDic dataDic:dataDicList){
		            CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM + Constant.DBTSPLIT
		                    + category.getCategoryCode().replace("_", "") + Constant.DBTSPLIT + dataDic.getDataId().replace("_", ""));
    			}
    			CacheUtilNew.removeByKey(CacheKey.cacheKey.KEY_VPS_SYS_DATA_DICM
    			        + Constant.DBTSPLIT  + category.getCategoryCode().replace("_", ""));
		    } catch (Exception e) {
		        log.error("删除数据字典缓存失败", e);
		    }
		}
		category.setCategoryKey(categoryKey);
		category.setUpdateTime(DateUtil.getDateTime());
		category.setUpdateUser(sysUserBasis.getUserKey());
		iSysDicCategoryInfoDao.deleteCategoryById(category);
		iSysDataDicDao.delDataDicByCategoryKey(categoryKey);
	}

}
