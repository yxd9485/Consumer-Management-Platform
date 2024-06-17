package com.dbt.platform.drinkcapacity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.drinkcapacity.bean.VpsVcodeDrinkCapacityPkRecord;

/**
 * 酒量1V1记录表Dao
 */
public interface IVpsVcodeDrinkCapacityPkRecordDao 
                    extends IBaseDao<VpsVcodeDrinkCapacityPkRecord> {
    
    /**
     * 获取指定区间比赛记录
     * 
     * @param map keys:splitTableSuffix、pkStatus、beginStartTime、beginEndTime
     * @return
     */
    List<VpsVcodeDrinkCapacityPkRecord> queryByPkTime(Map<String, Object> map);
    
    /**
     * 更新比赛结果
     * 
     * @param pkRecord
     */
    void updatePkStatus(VpsVcodeDrinkCapacityPkRecord pkRecord);
    
    /**
     * 查询指定比赛记录
     * 
     * @param map keys:splitTableSuffix、infoKey
     */
    VpsVcodeDrinkCapacityPkRecord findByIdWithAppletOpenid(Map<String, Object> map);
    
}
