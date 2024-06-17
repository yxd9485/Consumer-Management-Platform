package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpointsGroupBuyingActivityCog;

import feign.Param;

/**
 *  商城秒杀活动DAO
 * @author Administrator
 *
 */

public interface IVpointsGroupBuyingActivityCogDao extends IBaseDao<VpointsGroupBuyingActivityCog> {

	/**
	 * 活动列表
	 * @param map keys:queryBean、pageInfo
	 * @return
	 */
	public List<VpointsGroupBuyingActivityCog> queryForLst(Map<String, Object> map);

	/**
	 * 活动列表 -- 条数
	 * @param map keys:queryBean
	 * @return
	 */
	public int queryForCount(Map<String, Object> map);

	public void updateActivityStop(@Param("infoKey") String infoKey);

	public void updateActivityDel(@Param("infoKey") String infoKey);

	/**
	 * 根据goodsId获取秒杀列表
	 * @param goodsId
	 * @return
	 */
	public List<VpointsGroupBuyingActivityCog> queryByGoodsId(@Param("goodsId")String goodsId);

}
