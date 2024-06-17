package com.dbt.platform.benediction.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.benediction.bean.VpsVcodeBenedictionExpandRule;
import com.dbt.platform.permantissa.bean.VpsVcodePerMantissaCog;

/**
 * 膨胀红包规则配置Dao
 */
public interface IVpsVcodeBenedictionExpandRuleDao extends IBaseDao<VpsVcodeBenedictionExpandRule> {
    /**
     * 获取列表List
     * 
     * @param map keys:queryBean、pageInfo
     * @return
     */
    public List<VpsVcodeBenedictionExpandRule> queryForList(Map<String, Object> map);

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
     * 查询与当前配置有交集的规则
     * 
     * @param map keys:infoKey、beginDate、endDate、vcodeActivityKey
     * @return
     */
    public List<VpsVcodeBenedictionExpandRule> queryByDateForActivity(Map<String, Object> map);
}
