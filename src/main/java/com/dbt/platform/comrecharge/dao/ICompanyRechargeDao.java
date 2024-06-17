package com.dbt.platform.comrecharge.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.comrecharge.bean.CompanyRechargeInfo;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2015年9月6日 下午1:41:29
 * @description 类说明
 */

public interface ICompanyRechargeDao extends IBaseDao<CompanyRechargeInfo> {

	/**
	 * 获取企业充值记录 -- 列表
	 * @param companyKey
	 * @return
	 */
	public List<CompanyRechargeInfo> loadRechargeInfoByCompany(Map<String, Object> map);

	/**
	 * 获取企业充值记录 -- 条数
	 * @param companyKey
	 * @return
	 */
	public int countRechargeInfoByCompany(String companyKey);

}
