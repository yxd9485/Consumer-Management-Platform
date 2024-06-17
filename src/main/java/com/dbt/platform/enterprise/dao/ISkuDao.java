package com.dbt.platform.enterprise.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.enterprise.bean.StatisticsSku;

import java.util.List;

public interface ISkuDao extends IBaseDao<StatisticsSku> {

    /**
     * 根据spukey查询skuList
     * @return
     */
    public List<StatisticsSku> queryForListByStatKey(String statInfoKey);

    void deleteStatSkuInfo(String statInfoKey);
    
    /**
     * 获取SKU参与的统计分组
     */
    public List<StatisticsSku> queryStatName();
}
