package com.dbt.platform.org.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.mn.bean.MnAgencyEntity;
import com.dbt.platform.mn.bean.MnDepartmentEntity;
import com.dbt.platform.org.bean.OrganizationInfo;
import com.dbt.platform.org.dao.IOrganizationDao;

/**
 * <p>
 *  	组织机构服务实现类
 * </p>
 *
 */
@Service
public class OrganizationService extends BaseService<OrganizationInfo>{
    @Autowired
    private IOrganizationDao organizationDao;

    /**
     * 	查询组织架构List
     * @param pid	父Id，从0开始，-1不查询
     * @param level	级别：2冰品事业部、3销管中心、4大区、5省区、6市区、7区/县
     * @return
     */
    public List<OrganizationInfo> queryListByMap(long pid, int level) {
    	Map<String, Object> map = new HashMap<>();
    	map.put("pid", pid);
    	map.put("level", level);
    	return organizationDao.queryListByMap(map);
    }

	public OrganizationInfo findById(String id) {
		return organizationDao.findById(id);
	}

	/**
	 * 	查询大区
	 * @return
	 */
	public List<OrganizationInfo> queryBigRegionList() {
		return organizationDao.queryBigRegionList();
	}

	/**
	 * 	组织机构（大区,二级办）
	 * @param organizationIds
	 * @return
	 */
	public String getOrganizationNames(String organizationIds) {
	    if (StringUtils.isBlank(organizationIds)) return "";
	    
        String organizationNames = "";
        String[] groupOrganizationId = organizationIds.split(",");
        if (groupOrganizationId.length > 0) {
                organizationNames += groupOrganizationId[0];
        }
        if (groupOrganizationId.length > 1 && StringUtils.isNotBlank(groupOrganizationId[1])) {
                organizationNames += " - " + groupOrganizationId[1];
        }
        return organizationNames;
	}

	/**
	 * 	根据大区查询二级办
	 * @param bigRegionName
	 * @return
	 */
	public List<OrganizationInfo> queryListByBigRegionName(String bigRegionName) {
		return organizationDao.queryListByBigRegionName(bigRegionName);
	}
}
