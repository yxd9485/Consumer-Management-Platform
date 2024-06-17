package com.dbt.platform.comrecharge.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.comrecharge.bean.CompanyPrerechargeInfo;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2015年9月6日 下午1:41:29
 * @description 类说明
 */

public interface ICompanyPrerechargeDao extends IBaseDao<CompanyPrerechargeInfo> {

	/**
	 * 列表
	 * @param queryMap
	 * @return
	 */
	public List<CompanyPrerechargeInfo> loadPreInfoList(Map<String, Object> queryMap);

	/**
	 * 条数
	 * @param queryInfo
	 * @return
	 */
	public Integer countPreInfoList(CompanyPrerechargeInfo queryInfo);

	/**
	 * 更新预充值
	 * @param preInfo
	 */
	public void updateStatus(CompanyPrerechargeInfo preInfo);

	/**
	 * 更新预充值
	 * @param rechargeInfo
	 */
	public void updateCompanyPrerechargeInfo(CompanyPrerechargeInfo rechargeInfo);

	/**
	 * 确认充值
	 * @param
	 */
    public void confirmUpdate(HashMap<String, String> map);

    /**
     * 统计
     * @param
     */
   public Integer loadPreInfoListCount(Map<String, Object> queryMap);
}
