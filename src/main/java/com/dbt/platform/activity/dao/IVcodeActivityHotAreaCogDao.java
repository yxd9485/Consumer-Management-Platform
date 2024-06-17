/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:25:52 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;

/**
 * 接口：热区配置Dao
 */
public interface IVcodeActivityHotAreaCogDao extends IBaseDao<VcodeActivityHotAreaCog>{

    /**
     * 获取所有有效的区域配置
     * @param map 
     */
    public List<VcodeActivityHotAreaCog> queryAll(Map<String, Object> map);
    
    /**
     * 更新
     * 
     * @param hotAreaCog
     */
    public Integer updateHotAreaCog(VcodeActivityHotAreaCog hotAreaCog);
    
    /**
     * 根据主键删 
     * 
     * @param map   Key:hotAreaKey、updateTime、updateUser
     */
    public void deleteByHotAreaKey(Map<String, Object> map);
    
    /**
     * 根据地区areaCode获取热区List
     * @param areaCodeLst 地区code</br> 
     * @return List<VcodeActivityHotAreaCog> </br>
     */
    public List<VcodeActivityHotAreaCog> findHotAreaListByAreaCode(Map<String, Object> map); 

    /**
     * 根据区域Code获取记录
     * @param areaCode 地区code</br> 
     * @return List<VcodeActivityHotAreaCog> </br>
     */
	public List<VcodeActivityHotAreaCog> queryByAreaCode(String areaCode); 

	

}
