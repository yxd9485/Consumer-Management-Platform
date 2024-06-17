package com.dbt.platform.permantissa.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.permantissa.bean.VpsVcodePerMantissaCog;

/**
 * 逢尾数中奖规则Dao
 */
public interface IVpsVcodePerMantissaCogDao 
                    extends IBaseDao<VpsVcodePerMantissaCog> {
    /**
     * 获取列表List
     * 
     * @param map keys:queryBean、pageInfo
     * @return
     */
    public List<VpsVcodePerMantissaCog> queryForList(Map<String, Object> map);

    /**
     * 获取列表Count
     * 
     * @param map keys:queryBean
     * @return
     */
    public int queryForCount(Map<String, Object> map);
    
    /**
     * 逻辑删除记录
     * 
     * @param map keys:infoKey、updateUser
     */
    public void deleteById(Map<String, Object> map);
    
    /**
     * 获取指定日期内SKU参与的有效活动集合
     * 
     * @param map keys:infoKey、startDate、endDate、skuLst
     * @return
     */
    public List<VpsVcodePerMantissaCog> queryByDateForSku(Map<String, Object> map);
}
