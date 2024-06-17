package com.dbt.framework.datadic.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.framework.datadic.bean.SysDataDic;

/**
 * 文件名:ISysFuncDao.java <br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 数据字典类型dao<br>
 * 修改人: HaoQi<br>
 * 修改时间：2014-08-19 13:49:05<br>
 * 修改内容：新增<br>
 */
public interface ISysDataDicDao extends IBaseDao<SysDataDic> {
	
	/**
	 * 查询数据字典列表
	 * @param Map
	 * @return list
	 **/
	public List<SysDataDic> findDataDicList(Map<String, Object> map);
	/**
	 * 统计字典列表个数
	 * @param Map
	 * @return list
	 **/
	public int countDataDicList(SysDataDic sysDataDic);
	/**
	 * 根据字典类型查询数据字典
	 * @param Map
	 * @return list
	 **/
	public List<SysDataDic> findDataDicListByCategoryKey(String categoryKey);
	
	/**
	 * 根据字典类型删除数据信息
	 * @param categoryKey
	 * @return
	 */
	public void delDataDicByCategoryKey(String categoryKey);
	
	/**
	 * 根据类型Code查找字典数据
	 * @param categoryCode
	 * @return
	 */
	public List<SysDataDic> findDataDicListByCategoryCode(String categoryCode);
	
	/**
	 * 根据ID更新数据字典
	 * @param sysDataDic
	 * @return
	 */
	public void updateDataDicByDataId(SysDataDic sysDataDic);
	
	/**
	 * 根据类型键和数值键更新数据
	 * @param query
	 * @return
	 */
	public void updateByTypeAndKey(SysDataDic data);
	
	/**
	 * 获取所有字典数据
	 * @return
	 */
	public List<SysDataDic> findAll();
	
	/**
	 * 通过字典类型和dataid获取数据字典中的数据
	 * @return
	 */
	public SysDataDic getDatadicByCatCodeAndDataid(Map<String, Object> map);
	
	/**
	 * 更新数据字典的值并返回响应条数
	 * 
	 * @param sysDataDic
	 * @return
	 */
	public int updateDataValueForRowNum(SysDataDic sysDataDic);
}