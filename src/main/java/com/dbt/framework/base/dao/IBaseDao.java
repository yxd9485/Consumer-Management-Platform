package com.dbt.framework.base.dao;

import java.util.List;

/**
 * 文件名：IBaseDao<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: 基础dao类 <br>
 * 修改人: 谷长鹏 <br>
 * 修改时间：2014-02-17 14:46:59<br>
 * 修改内容：新增<br>
 */
public interface IBaseDao<T> {
	/**
	 * 添加/新增
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void create(T object);

	/**
	 * 修改
	 * 
	 * @param object
	 * @throws Exception
	 */
	public void update(T object);

	/**
	 * 根据id删除对象
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteById(String id);

	/**
	 * 根据id数组删除对象
	 * 
	 * @param ids
	 */
	public void delete(String[] ids);

	/**
	 * 根据对象条件查询
	 * 
	 * @param object
	 * @return
	 */
	public List<T> getList(T object);

	/**
	 * 条件查询
	 * 
	 * @param value
	 * @return
	 */
	public List<T> getByMap(String value);

	/**
	 * 查询所有对象
	 * 
	 * @return @
	 */
	public List<T> getAll();

	/**
	 * 查询对象总数量
	 * 
	 * @return
	 */
	public int getCount();

	/**
	 * 根据条件查找总数量
	 * 
	 * @param object
	 * @return
	 */
	public int getCountByObj(T object);

	/**
	 * 根据id查询实体对象
	 * 
	 * @param id
	 * @return
	 */
	public T findById(String id);

	/**
	 * 根据条件查询实体对象
	 * 
	 * @param object
	 * @return
	 */
	public T findByObject(T object);

	/**
	 * 条件查询
	 * 
	 * @param t
	 * @return
	 */
	public List<T> findByObjCondition(T t);
}
