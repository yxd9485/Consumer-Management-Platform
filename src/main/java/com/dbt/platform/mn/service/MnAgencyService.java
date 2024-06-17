package com.dbt.platform.mn.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.mn.bean.MnAgencyEntity;
import com.dbt.platform.mn.dao.IMnAgencyDao;

/**
 * 组织架构service
 * @author Administrator
 *
 */
@Service
public class MnAgencyService extends BaseService<MnAgencyEntity>{
    @Autowired
    private IMnAgencyDao iMnAgencyDao;
    public void removeAll() {
        iMnAgencyDao.removeAll();
    }

    public void save(MnAgencyEntity mnAgencyEntity) {
        iMnAgencyDao.insert(mnAgencyEntity);
    }

    public void deleteById(Long id) {
        iMnAgencyDao.deleteById(id);
    }

    /**
     * 	查询经销商数据
     * @param vkgrp	省区id
     * @param agency_type 类型：0 经销商   1分销商  2二批
     * @return
     */
	public List<MnAgencyEntity> queryListByMap(String departmentId, String agency_type) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("departmentId", departmentId);
		map.put("agency_type", agency_type);
		return iMnAgencyDao.queryListByMap(map);
	}

	public MnAgencyEntity findById(String id) {
		return iMnAgencyDao.findById(id);
	}
}
