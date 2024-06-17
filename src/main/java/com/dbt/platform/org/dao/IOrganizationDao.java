package com.dbt.platform.org.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.org.bean.OrganizationInfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 	青啤组织机构Dao
 * @author hanshimeng
 *
 */
public interface IOrganizationDao extends IBaseDao<OrganizationInfo> {

	List<OrganizationInfo> queryListByMap(Map<String, Object> map);
	
	/**
	 * 	查询大区
	 * @return
	 */
	List<OrganizationInfo> queryBigRegionList();

	/**
	 * 	根据大区查询二级办
	 * @param bigRegionName
	 * @return
	 */
	List<OrganizationInfo> queryListByBigRegionName(String bigRegionName);
}
