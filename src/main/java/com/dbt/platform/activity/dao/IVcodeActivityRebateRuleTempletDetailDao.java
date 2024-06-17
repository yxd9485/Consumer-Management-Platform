package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleTempletDetail;

/**
 * 活动规则模板奖项配置项明细DAO
 */
public interface IVcodeActivityRebateRuleTempletDetailDao extends IBaseDao<VcodeActivityRebateRuleTempletDetail> {

    /**
     * 批量插入
     * 
     * @param map keys:templetDetailLst
     */
    public void batchWrite(Map<String, Object> map);
    
	/**
	 * 依据模板主键物理删除
	 * 
	 * @param templetKey
	 */
	public void removeByTempletKey(String templetKey);
	
    /**
     * 依据模板主键获取列表
     * 
     * @param map keys:templetKey
     */
    public List<VcodeActivityRebateRuleTempletDetail> queryByTempletKey(Map<String, Object> map);
    
    /**
     * 获取规则下商城优惠券NO
     * @param templetKey
     * @return
     */
    public List<String> queryCouponNoByTempletKey(String templetKey);

}
