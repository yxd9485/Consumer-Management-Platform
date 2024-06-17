package com.dbt.platform.activityui.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activityui.bean.VpsTemplateUi;
/**
 * 活动模板UI接口Dao
 * @author hanshimeng
 *
 */
public interface IVpsTemplateUiDao extends IBaseDao<VpsTemplateUi>{

	List<VpsTemplateUi> queryForLst(Map<String, Object> map);

	int queryForCount(Map<String, Object> map);

    void addTemplateUi(VpsTemplateUi vpsTemplateUi);

    void updateTemplateUi(VpsTemplateUi vpsTemplateUi);

    VpsTemplateUi getTemplateUi(String templateKey);
}
