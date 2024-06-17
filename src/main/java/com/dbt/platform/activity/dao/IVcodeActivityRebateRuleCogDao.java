/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:25:52 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;

/**
 * 接口：V码活动返利配置规则Dao
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2016年12月5日 </br>
 */
public interface IVcodeActivityRebateRuleCogDao extends IBaseDao<VcodeActivityRebateRuleCog>{
    
    /**
     * 根据活动KEY获取活动规则列表
     * 
     * @param map key：vcodeActivityKey、areaCode、flag、tabsFlag
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryRebateRuleCogListByActivityKey(Map<String, Object> map);
    
    /**
     * 根据活动主键删除活动规则
     * 
     * @param map   Key:rebateRuleKey、updateTime、updateUser
     */
    public void deleteByrebateRuleKey(Map<String, Object> map); 
    
    /**
     * 根据活动规则主键获取已导入过配置项的区域编码列表
     * 
     * @param vcodeActivityKey 活动主键
     */
    public List<String> queryCityCodeByActivityKey(String vcodeActivityKey); 
    
    /**
     * 查询有效记录活动规则列表
     * 
     * @param vcodeActivityKey
     * @return
     */
    public Integer getValidRebateRuleCogNumByActivityKey(String vcodeActivityKey);
    
    /**
     * 更新活动规则 
     * 
     * @param rebabeRuleCog
     * @return
     */
    public Integer updateRebateRuleCog(VcodeActivityRebateRuleCog rebabeRuleCog);

    /**
     * 根据规则父ID查询规则List
     * @param rebateRuleKey 规则主键</br> 
     * @return List<VcodeActivityRebateRuleCog> </br>
     */
	public List<VcodeActivityRebateRuleCog> queryRebateRuleAreaListByParentId(String rebateRuleKey);
	
	/**
	 * 查询活动下指定区域的规则
	 * 
	 * @param map key：vcodeActivityKey、areaCode
	 * @return
	 */
	public List<VcodeActivityRebateRuleCog> queryByAreaCode(Map<String, Object> map);
	
	/**
     * 依据热区主键查询有效的相关规则
     * 
     * @param map key：hotAreaKey、nowTime(yyyy-MM-dd)
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryValidByHotAreaKey(Map<String, Object> map);

    /**
     * 查询已过期的规则List
     * @param map:vcodeActivityKey,areaCode
     * @return
     */
	public List<VcodeActivityRebateRuleCog> queryOverdueRebateRuleCogList(Map<String, Object> map);


    /**
     * 到期提醒列表List
     * @param
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryRuleExpireRemind();

    /**
     * 红包到期
     * @param
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryRedPacketRule();
    
    /**
     * 获取指定活动下的可继承规则
     * 
     * @param map key:vcodeActivityKey、rebateRuleKey
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryAppointRebateRule(Map<String, Object> map);

    /**
     * 查询设置有爆点规则的返利规则
     * @param map
     * @return
     */
    List<VcodeActivityRebateRuleCog> findEruptRuleInfo(Map<String, String> map);
}
