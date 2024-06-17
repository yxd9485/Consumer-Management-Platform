package com.dbt.platform.activity.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;

/**
 * @author RoyFu 
 * @createTime 2016年1月19日 下午5:47:01
 * @description 
 */

public interface IVcodeActivityVpointsCogDao extends IBaseDao<VcodeActivityVpointsCog> {

	/**
	 * 批量插入积分配置
	 * @param map
	 */
	public void batchWrite(Map<String, Object> map);

    /**
     * 批量更新活动配置剩余数量
     * @param cogLst</br> 
     * @return void </br>
     */
    public void updateBathWaitActivityVpointsCog(List<VcodeActivityVpointsCog> cogLst);

    /**
     * 根据活动规则主键停用中奖配置项
     * 
     * @param map   Key:rebateRuleKey、updateTime、updateUser
     */
    public void removeByrebateRuleKey(Map<String, Object> map);

	/**
	 * 根据活动规则主键查询相应配置项
	 * 
	 * @param rebateRuleKey    活动规则主键</br> 
	 * @return void </br>
	 */
	public List<VcodeActivityVpointsCog> queryVpointsCogByrebateRuleKey(String rebateRuleKey);

	/**
	 * 拷贝配置项
	 * @param map:newRebateRuleKey,copyRebateRuleKey
	 */
	public void copyActivityVpointsItem(Map<String, Object> map);
	
	/**
	 * 获取占比预警规则奖项部个数
	 * @param map rebateRuleKeyAry
	 * @return
	 */
	public List<VcodeActivityVpointsCog> countWarnVpointsCogByRebateRuleKey(Map<String, Object> map);
	
	/**
	 * 获取活动规则下需要占比预警的奖项
	 * @param map rebateRuleKeyAry
	 * @return
	 */
	public List<VcodeActivityVpointsCog> queryWarnVpointsCogByRebateRuleKey(Map<String, Object> map);

	void deleteByrebateRuleKey(Map<String, String> param);
	
	/**
     * 获取规则下商城优惠券NO
     * @param rebateRuleKey
     * @return
     */
    public List<String> queryCouponNoByRebateRuleKey(String rebateRuleKey);
}
