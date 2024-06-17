package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpointsVerificationDetail;

/**
 * 积分商场实物奖兑换核销明细表DAO
 */
public interface IVpointsVerificationDetailDao extends IBaseDao<VpointsVerificationDetail> {

	/**
	 * 获取核销列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<VpointsVerificationDetail> queryForLst(Map<String, Object> map);

	/**
	 * 获取列表记录总个数
     * @param map keys:queryBean
	 */
	public Integer queryForCount(Map<String, Object> map);
	
	/**
	 * 获取预览核销列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<VpointsVerificationDetail> queryPreviewForLst(Map<String, Object> map);
	
	/**
	 * 获取预览核销列表记录总个数
	 * @param map keys:queryBean
	 */
	public Integer queryPreviewForCount(Map<String, Object> map);
	
	/**
	 * 批量插入明细
	 * 
     * @param map keys:detailLst
	 */
	public void batchWrite(Map<String, Object> map);
	
	/**
     * 依据核销记录主键获取核销明细
     * 
     * @param map keys:verificationId
     * @return
     */
    public List<VpointsVerificationDetail> queryByVerificationId(Map<String, Object> map);
    
    /**
     * 依据核销截止日期获取核销明细
     * 
     * @param verificationEndDate 核销截止日期
     * @return
     */
    public List<VpointsVerificationDetail> queryByPreviewVerificationId(Map<String, Object> map);

}
