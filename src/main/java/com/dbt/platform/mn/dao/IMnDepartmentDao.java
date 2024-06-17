package com.dbt.platform.mn.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.mn.bean.MnDepartmentEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bing_huang
 * @since 2021-12-17
 */
public interface IMnDepartmentDao extends IBaseDao<MnDepartmentEntity> {

    void removeAll();
    void deleteById(@Param("id") Long id);
    void insert(MnDepartmentEntity mnDepartmentEntity);
    
	List<MnDepartmentEntity> queryListByMap(Map<String, Object> map);
}
