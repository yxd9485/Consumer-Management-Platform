package com.dbt.platform.activityui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.activityui.bean.VpsActivityTemplateUi;
import com.dbt.platform.activityui.dao.IVpsActivityTemplateUiDao;

/**
 * 活动关联模板UIService
 * @author hanshimeng
 *
 */
@Service("vpsActivityTemplateUiService")	
public class VpsActivityTemplateUiService extends BaseService<VpsActivityTemplateUi>{

	@Autowired
	private IVpsActivityTemplateUiDao activityTemplateUiDao;
}
