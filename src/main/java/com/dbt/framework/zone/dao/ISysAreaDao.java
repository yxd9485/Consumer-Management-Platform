package com.dbt.framework.zone.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.zone.bean.SysAreaM;

/**
 * 文件名: ISysAreaMDao.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 区域码表<br>
 * 修改人: FuZhongYue<br>
 * 修改时间：2014-03-17 15:40:45<br>
 * 修改内容：新增<br>
 */
public interface ISysAreaDao {
    
	/**
     * 根据父编号查找数据
     * 
     * @param parentId
     * @return
     */
    public List<SysAreaM> findByParentId(String parentId);

	/**
	 * 根据父编号查找符合项目区域数据
	 * 
	 * @param map keys:parentId、projectArea
	 * @return
	 */
	public List<SysAreaM> findByParentIdForProject(Map<String, Object> map);

	/**
	 * 根据当前编号查找数据
	 * 
	 * @param zoneId
	 * @return
	 */
	public SysAreaM findById(String zoneId);

	/**
	 * 根据名称查找数据
	 * 
	 * @param zoneId
	 * @return
	 */
	public SysAreaM findSubChildCode(String areaname);
	public SysAreaM findChildCode(String areaname,String type,String parentCode);

	/**
	 * 查询所有区域名称为“全部”的list
	 * 
	 * @return List<String> 区域id list
	 */
	public List<String> queryAllAreaNameIsAll();

	/**
	 * 根据MAP查询code
	 * 
	 * @param zoneId
	 * @return
	 */
	public SysAreaM findCodeByMap(Map<String, Object> map);
	/**
	 * 根据多个区域码信息得到区域对象信息
	 * @param list 多个区域码表
	 * @return 
	 * @author cpgu 20150523
	 */
	public List<SysAreaM> queryAreamByAreaCodes(List<String> list);
	
	/**
	 * 获取需要放入缓存的信息
	 * @return
	 */
	public List<SysAreaM> findRegionList();

	public List<SysAreaM> queryAllAreamByAreaCodes(List<String> areaCodeList);

	public List<SysAreaM> queryAllList();

}
