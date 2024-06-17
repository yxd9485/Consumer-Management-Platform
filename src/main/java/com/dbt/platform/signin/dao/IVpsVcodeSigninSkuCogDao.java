package com.dbt.platform.signin.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.signin.bean.VpsVcodeSigninSkuCog;

/**
 * 签到活动SKU限制配置表Dao
 */

public interface IVpsVcodeSigninSkuCogDao extends IBaseDao<VpsVcodeSigninSkuCog> {


    /**
     * 批量插入配置
     * 
     * @param map keys:signinSkuCogLst
     */
    public void batchWrite(Map<String, Object> map);
    
    /**
     * 获取签到活动对应的SKU限制配置
     * 
     * @param map keys:activityKey
     * @return
     */
    public List<VpsVcodeSigninSkuCog> queryByActivitykey(Map<String, Object> map);
    
    /**
     * 物理删除签到活动对应的SKU限制配置
     * 
     * @param map keys:activityKey
     * @return
     */
    public void deleteByActivitykey(Map<String, Object> map);

}
