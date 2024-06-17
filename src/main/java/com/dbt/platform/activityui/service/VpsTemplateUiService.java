package com.dbt.platform.activityui.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbt.platform.activityui.bean.VpsAuthInfo;
import com.dbt.platform.activityui.dao.IVpsAuthDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activityui.bean.VpsTemplateUi;
import com.dbt.platform.activityui.dao.IVpsTemplateUiDao;

/**
 * 活动模板UIService
 * @author hanshimeng
 *
 */
@Service("vpsTemplateUiService")	
public class VpsTemplateUiService extends BaseService<VpsTemplateUi>{

	@Autowired
	private IVpsTemplateUiDao templateUiDao;
	@Autowired
	private IVpsAuthDao authDao;

	public List<VpsTemplateUi> queryForLst(VpsTemplateUi queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return templateUiDao.queryForLst(map);
	}

	public int queryForCount(VpsTemplateUi queryBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		return templateUiDao.queryForCount(map);
	}

    public void addTemplateUi(VpsTemplateUi vpsTemplateUi) {
		templateUiDao.addTemplateUi(vpsTemplateUi);
    }

    public void updateTemplateUi(VpsTemplateUi vpsTemplateUi) {
		templateUiDao.updateTemplateUi(vpsTemplateUi);
    }

	public void addAuthInfo(List<VpsAuthInfo> authList) {
		Map<String, Object> map = new HashMap<>();
		map.put("authList", authList);
		authDao.addAuthInfo(map);
	}

    public VpsTemplateUi getTemplateUi(String templateKey) {
		return templateUiDao.getTemplateUi(templateKey);
    }

	public List<VpsAuthInfo> getAuthInfo(String templateKey) {
		return authDao.getAuthInfo(templateKey);
	}

	public void updateAuth(List<VpsAuthInfo> authList) {
		Map<String, Object> map = new HashMap<>();
		map.put("authList", authList);
		authDao.updateAuth(authList);
	}
}
