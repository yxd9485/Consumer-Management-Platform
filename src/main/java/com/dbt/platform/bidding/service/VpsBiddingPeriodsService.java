package com.dbt.platform.bidding.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.bidding.bean.VpsBiddingPeriods;
import com.dbt.platform.bidding.dao.IVpsBiddingPeriodsDao;

/**
  * 竞价活动期数Service
 */
@Service
public class VpsBiddingPeriodsService extends BaseService<VpsBiddingPeriods> {

	@Autowired
	private IVpsBiddingPeriodsDao biddingPeriodsDao;

	/**
	 * 获取活动期数信息
	 * 
	 * @param activityKey
	 * @return
	 */
	public VpsBiddingPeriods findPeriodsByKey(String infoKey) {
		return biddingPeriodsDao.findById(infoKey);
	}

	/**
	 * 活动期数列表
	 * 
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 */
	public List<VpsBiddingPeriods> queryPeriodsList(
			VpsBiddingPeriods queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		List<VpsBiddingPeriods> activityList = biddingPeriodsDao.loadPeriodsList(queryMap);
		return activityList;
	}

	/**
	 * 活动期数列表条数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int countPeriodsList(VpsBiddingPeriods queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return biddingPeriodsDao.countPeriodsList(queryMap);
	}

    
    /**
	 *  根据活动ID获取信息
	 */
    public VpsBiddingPeriods findById(String id){
		return biddingPeriodsDao.findById(id);
	}
	
}
