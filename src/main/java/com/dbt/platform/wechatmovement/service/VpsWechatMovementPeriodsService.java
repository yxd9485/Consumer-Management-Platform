package com.dbt.platform.wechatmovement.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.wechatmovement.bean.VpsWechatMovementPeriods;
import com.dbt.platform.wechatmovement.dao.IVpsWechatMovementPeriodsDao;

@Service
public class VpsWechatMovementPeriodsService extends BaseService<VpsWechatMovementPeriods>{
	
	@Autowired
	private IVpsWechatMovementPeriodsDao  wechatMovementPeriodsDao;
	
	/**
	 * 获取某期微信运动数据统计
	 * @param periodsKey 期数
	 * @return
	 */
	public VpsWechatMovementPeriods findWechatMovemenPeriods(String periodsKey) {
		if(StringUtils.isBlank(periodsKey)){
			periodsKey = DateUtil.getDate().replaceAll("-", "");
		}
		return wechatMovementPeriodsDao.findWechatMovemenPeriods(periodsKey);
	}

	/**
	 * 新增
	 * @param periods
	 */
	public void create(VpsWechatMovementPeriods periods) {
		wechatMovementPeriodsDao.create(periods);
	}
	
	/**
	 * 更新期表的虚拟数据倍数
	 * @param periodsKey
	 * @param currencyPrice
	 * @param activityMagnification
	 */
	public void updateActivityMagnification(String periodsKey, String currencyPrice, String activityMagnification,String bonusPool){
		Map<String, Object> map = new HashMap<>();
		map.put("periodsKey", periodsKey);
		map.put("currencyPrice", currencyPrice);
		map.put("activityMagnification", activityMagnification);
		map.put("bonusPool", bonusPool);
		wechatMovementPeriodsDao.updatePeriodsRule(map);
	}
}
