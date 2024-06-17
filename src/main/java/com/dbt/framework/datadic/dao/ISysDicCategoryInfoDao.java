package com.dbt.framework.datadic.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.framework.datadic.bean.SysDicCategoryInfo;

/**
 * 文件名:ISysFuncDao.java <br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 数据字典类型dao<br>
 * 修改人: HaoQi<br>
 * 修改时间：2014-08-19 13:49:05<br>
 * 修改内容：新增<br>
 */
public interface ISysDicCategoryInfoDao extends IBaseDao<SysDicCategoryInfo> {

	/**
	 * 查询分页数据
	 * @param map
	 * @return
	 */
	public List<SysDicCategoryInfo> findPageList(Map<String, Object> map);

	/**
	 * 查询总数
	 * @param map
	 * @return
	 */
	public int countAll(Map<String, Object> map);

	/**
	 * 获取所有字典类型
	 * @return
	 */
	public List<SysDicCategoryInfo> findAll(Map<String,Object> map);

	/**
	 * 添加类型
	 * @param category
	 */
	public void insertCategory(SysDicCategoryInfo category);

	/**
	 * 更新类型
	 * @param category
	 */
	public void updateCategory(SysDicCategoryInfo category);

	/**
	 * 逻辑删除类型
	 * @param temp
	 */
	public void deleteCategoryById(SysDicCategoryInfo category);

}