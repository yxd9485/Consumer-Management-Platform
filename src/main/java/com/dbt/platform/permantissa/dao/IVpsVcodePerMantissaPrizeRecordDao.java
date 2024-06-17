package com.dbt.platform.permantissa.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.permantissa.bean.VpsVcodePerMantissaPrizeRecord;

/**
 * 逢尾数中奖规则Dao
 */
public interface IVpsVcodePerMantissaPrizeRecordDao 
                    extends IBaseDao<VpsVcodePerMantissaPrizeRecord> {
    /**
     * 获取列表List
     * 
     * @param map keys:queryBean、pageInfo、isLimit
     * @return
     */
    List<VpsVcodePerMantissaPrizeRecord> queryForList(Map<String, Object> map);

    /**
     * 获取列表Count
     * 
     * @param map keys:queryBean
     * @return
     */
    int queryForCount(Map<String, Object> map);
}
