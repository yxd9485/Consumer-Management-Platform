package com.dbt.platform.activity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VpsVcodePrizeRecord;

import java.util.Map;

/**
 * @description 一等奖记录表Dao</br>
 */
public interface IVpsVcodePrizeRecordDao extends IBaseDao<VpsVcodePrizeRecord> {


	int getPrizeCount(Map<String, String> param);
}
