package com.dbt.platform.mn.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.mn.bean.MnAgencyEntity;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bing_huang
 * @since 2021-12-17
 */
public interface IMnAgencyDao extends IBaseDao<MnAgencyEntity> {

    void removeAll();
    void deleteById(@Param("id") Long id);

    void insert(MnAgencyEntity mnAgencyEntity);
	List<MnAgencyEntity> queryListByMap(Map<String, Object> map);
}
