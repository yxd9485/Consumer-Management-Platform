package com.dbt.platform.activity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityCogSkuRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author RoyFu 
 * @createTime 2016年1月19日 下午5:45:30
 * @description 
 */

public interface IVcodeActivityCogSkuRelationDao extends IBaseDao<VcodeActivityCogSkuRelation> {

    void batchInsert(@Param("list") List<VcodeActivityCogSkuRelation> relations);

    void deleteByActivityKey(@Param("vcodeActivityKey") String vcodeActivityKey);

    List<String> loadSkuKeysByActivityKey(@Param("vcodeActivityKey") String vcodeActivityKey);

}
