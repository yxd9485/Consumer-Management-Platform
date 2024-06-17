package com.dbt.platform.prize.dao;
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VpsVcodePrizeRecord;
import com.dbt.platform.prize.bean.MajorInfo;
/**
 * @author hanshimeng
 * @createTime 2016年4月21日 下午4:26:50
 * @description 一等奖中奖名单Dao
 */
public interface IMajorInfoDao extends IBaseDao<MajorInfo> {
    
    /**
     * 分页查询中奖名单
     * 
     * @param params quaryParam    查询条件
     * @param params pageParam     分页条件
     * @return
     */
    public List<MajorInfo> queryMajorInfoListForPage(Map<String, Object> params);
    
	/**
	 * 中奖名单总条数
	 * 
	 * @param params quaryParam    查询条件
	 * @return
	 */
	public int queryMajorInfoListForTotal(Map<String, Object> params);
	
	/**
	 * 依据主键查询
	 * 
	 * @param infoKey
	 * @return
	 */
	public MajorInfo findMajorInfoByInfoKey(String infoKey);

	/**
	 * 获取当天过期要回收的扫码中出的大奖
	 * 
	 * @param
	 * @return
	 */
	List<MajorInfo> queryForExpired(Map<String, Object> params);
	
	/**
	 * 标记为已回收
	 * @param infoKey 记录主键
	 */
	void updateRecycleFlag(String infoKey);

	/**
	 * 依据订单主键查询
	 * @param exchangeId
	 * @return
	 */
	public MajorInfo findMajorInfoByExchangeId(String exchangeId);
	
	/**
	 * 查询某个奖的中奖次数
	 * @param param
	 * @return
	 */
	List<MajorInfo> queryPrizeTypeLst(Map<String, Object> param);

	void addFirstPrizeRecord(MajorInfo majorInfo);
}
