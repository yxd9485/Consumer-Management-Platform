package com.dbt.platform.activate.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activate.bean.VpsVcodeBatchActivateCog;

/**
 * 一万一批次激活人员授权配置表Dao
 */
public interface IVpsVcodeBatchActivateCogDao extends IBaseDao<VpsVcodeBatchActivateCog> {
    
    /**
     * 依据openid获取激活人员
     * 
     * @param openid
     * @return
     */
    public VpsVcodeBatchActivateCog findByOpenid(String openid);
    
    /**
     * 激活人员列表
     * 
     * @param map keys:queryBean、 pageInfo
     * @return
     */
    public List<VpsVcodeBatchActivateCog> queryForList(Map<String, Object> map);

    /**
     * 激活人员列表总条数
     * 
     * @param map keys:queryBean
     * @return
     */
    public int queryForListCount(Map<String, Object> map);
    
    /**
     * 依据openid或手机号获取激活人员
     * 
     * @param map keys:openid、phoneNum
     * @return
     */
    public VpsVcodeBatchActivateCog findByOpenidOrPhoneNum(Map<String, Object> map);

}
