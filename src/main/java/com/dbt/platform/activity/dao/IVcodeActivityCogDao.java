package com.dbt.platform.activity.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityCogExtends;
import org.apache.ibatis.annotations.Param;

/**
 * @author RoyFu 
 * @createTime 2016年1月19日 下午5:45:30
 * @description 
 */

public interface IVcodeActivityCogDao extends IBaseDao<VcodeActivityCog> {

	/**
	 * 活动列表
	 * @param map keys:queryBean、pageInfo
	 * @return
	 */
	public List<VcodeActivityCog> queryForLst(Map<String, Object> map);

	/**
	 * 活动列表 -- 条数
	 * @param map keys:queryBean
	 * @return
	 */
	public int queryForCount(Map<String, Object> map);

	public void changeStatus(VcodeActivityCog activity);

	/**
	 * 查询所有活动
	 * @param </br> 
	 * @return List<VcodeActivityCog> </br>
	 */
	public List<VcodeActivityCog> findAllVcodeActivityList(Map<String, Object> map);

	/**
	 * 根据skuKey查询活动count
	 * @param skuKey </br> 
	 * @return int </br>
	 */
	public int queryActivityCountBySkuKey(String skuKey);

	/**
	 * 根据skuKey查询活动List
	 * @param skuKey </br> 
	 * @return List<VcodeActivityCog> </br>
	 */
	public List<VcodeActivityCog> queryActivityListBySkuKey(String skuKey);

	/**
	 * 查询可配置一码多扫的活动（没有关联一码多扫或已关联并过期的活动 ）
	 * @param map 
	 * @return
	 */
	public List<VcodeActivityCog> queryListForSuitMorescan(Map<String, Object> map);
	
	/**
	 * 查询可配置逢百规则的活动（没有关联一码多扫或已关联并过期的活动 ）
	 * @param map 
	 * @return
	 */
	public List<VcodeActivityCog> queryListForSuitPerhundred(Map<String, Object> map);

	/**
	 * 查询可配置阶梯的有效活动
	 * @return
	 */
	public List<VcodeActivityCog> queryListForLadder(Map<String, Object> map);
	
	/**
	 * 检查询活动是否可配置阶梯
	 * @param map
	 * @return
	 */
	public List<VcodeActivityCog> queryActivityForLadder(Map<String, Object> map);
    
    /**
     * 查询未结束的活动
     */
    public List<VcodeActivityCog> queryValidActivity();
    
    /**
     * 依据活动主键查询活动
     */
    public List<VcodeActivityCog> queryByActivityKey(Map<String, Object> map);

	/**
	 * 查询结束前48小时进行短信预警的活动
	 * @return
	 */
	public List<VcodeActivityCog> queryActivityExpiredPrize();
	
	/**
	 * 依据主键获取活动名称
	 * @param vcodeActivkktyKey
	 * @return
	 */
	public String findActivityNameByKey(String vcodeActivkktyKey);

    void deleteactivityCogDaoByVcodeActivityKey(@Param("vcodeActivityKey") String vcodeActivityKey);

	void createActivityExtendCog(VcodeActivityCogExtends vcodeActivityCogExtends);

	List<VcodeActivityCogExtends> findActivityExtendsByVcodeActityKey(@Param("vcodeActivityKey") String infoKey);

    List<VcodeActivityCog> queryListForNotFrequencyCog(Map<String, Object> map);
}
