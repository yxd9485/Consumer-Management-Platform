package com.dbt.vpointsshop.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.vpointsshop.bean.VpointsVerificationInfo;

/**
 * 积分商场实物奖兑换核销表DAO
 */
public interface IVpointsVerificationInfoDao extends IBaseDao<VpointsVerificationInfo> {

	/**
	 * 获取列表
	 * @param map keys:queryBean、pageInfo 
	 */
	public List<VpointsVerificationInfo> queryForLst(Map<String, Object> map);

	/**
	 * 获取列表记录总个数
     * @param map keys:queryBean
	 */
	public Integer queryForCount(Map<String, Object> map);
	
	/**
	 * 积分商场实物奖兑换核销表
	 * 
	 * @param map keys:verificationId、status、optUserKey
	 */
	public void updateVerificationStatus(Map<String, Object> map);
	
	/**
	 * 获取指定状态的核销记录 
	 * 
     * @param map keys:statusLst
	 */
	public List<VpointsVerificationInfo> queryByStatus(Map<String, Object> map);

	/**
	 * 查询预览核销记录
	 * @param map:verificationEndDate 核销结束时间
	 * @return
	 */
	public VpointsVerificationInfo findPreviewForVerificationInfo(Map<String, Object> map);

}
