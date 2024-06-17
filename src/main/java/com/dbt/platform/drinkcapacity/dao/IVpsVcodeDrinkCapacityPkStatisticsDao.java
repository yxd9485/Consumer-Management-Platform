package com.dbt.platform.drinkcapacity.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.drinkcapacity.bean.VpsVcodeDrinkCapacityPkStatistics;

/**
 * 用户酒量PK1V1情况Dao
 */
public interface IVpsVcodeDrinkCapacityPkStatisticsDao 
                extends IBaseDao<VpsVcodeDrinkCapacityPkStatistics> {
    
    /**
     * 获取用户的比赛情况
     * 
     * @param userKey   用户主键
     * @return
     */
    VpsVcodeDrinkCapacityPkStatistics findByUserKey(@Param("userKey")String userKey);
    
    /**
     * 更新最后 一次比赛结果
     * 
     * @param map   keys:userKey、winNum、loseNum、drawNum、unpkNum、lastPkTime、lastPkResult
     * @return
     */
    void updatePkResult(Map<String, Object> map);
}
