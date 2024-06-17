package com.dbt.platform.ticket.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.ticket.bean.VpsVcodeTicketLib;

/**
 * 优惠券码库表Dao
 */
public interface IVpsVcodeTicketLibDao extends IBaseDao<VpsVcodeTicketLib> {

    /**
     * 批量插入券码
     * @param map
     */
    public void batchWrite(Map<String, Object> map);
    
    /**
     * 创建优惠券库表
     * 
     * @param map libName
     * @return
     */
    public void createTicketTable(@Param("libName") String libName);
    
    /**
	 * 券码入库
	 * @param paramMap
	 */
	int addTicketCodeToData(Map<String, Object> paramMap);

    /**
     * 根据时间删除券码
     * @param paramMap
     */
	public void deleteByTime(Map<String, Object> paramMap);

}
