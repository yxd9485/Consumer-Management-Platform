package com.dbt.platform.question.dao;



import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.question.bean.VpsQuestionnaireOrder;

public interface IVcodeQuestionOrderDao  extends IBaseDao<VpsQuestionnaireOrder>{

	void createBatch(List<String> resultList);

	List<VpsQuestionnaireOrder> queryForLst(Map<String, Object> map);

	int queryForCount(Map<String, Object> map);

	

}
