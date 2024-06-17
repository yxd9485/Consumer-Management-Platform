package com.dbt.platform.promotion.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.promotion.bean.VpsVcodeBindPromotionCog;

/**
 * 捆绑促销活动配置表Dao
 */

public interface IVpsVcodeBindPromotionCogDao extends IBaseDao<VpsVcodeBindPromotionCog> {

	/**
	 * 活动列表
	 * @param queryMap
	 * @return
	 */
	public List<VpsVcodeBindPromotionCog> loadActivityList(Map<String, Object> queryMap);

	/**
	 * 活动列表 -- 条数
	 * @param queryMap
	 * @return
	 */
	public int countActivityList(Map<String, Object> queryMap);
	
	/**
	 * 获取与促销SKU当前活动有交叉的活动
	 * 
	 * @param map key:promotionSkuKey、startDate、endDate
	 * @return
	 */
	public List<VpsVcodeBindPromotionCog> queryByPromotionSkuForActivityDate(Map<String, Object> map);

}
