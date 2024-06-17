package com.dbt.framework.wechat.dao;

import java.util.List;
import java.util.Map;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.framework.wechat.bean.SendTemplateMsg;

public interface ISendTemplateMsgDao extends IBaseDao<SendTemplateMsg>{

	List<SendTemplateMsg> queryForLst(Map<String, Object> map);

	int queryForCount(Map<String, Object> map);
	
	int queryForSendMessageCount(Map<String, Object> map);

	List<SendTemplateMsg> queryNonExecutionMsgList();

}
