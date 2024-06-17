package com.dbt.platform.job;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.wechat.bean.SendTemplateMsg;
import com.dbt.framework.wechat.service.SendTemplateMsgService;

/**
 * 推送模板信息job
 * @author hanshimeng
 *
 */
@Service("sendTemplateMsgJob")
public class SendTemplateMsgJob {

	/** The logger. */
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private SendTemplateMsgService msgService;
	
	@SuppressWarnings("unchecked")
	public void sendTemplateMsg() throws Exception {
		logger.error("推送模板信息job开始");
		// 获取job执行的省区
		DbContextHolder.clearDBType();
    	Set<String> nameList = null;
    	String projectServerNames = DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.PROJECT_JOB,
				DatadicKey.ProjectJob.SEND_TEMPLATE_MSG);
    	if(StringUtils.isBlank(projectServerNames)) return;
    	
    	if(!"ALL".equals(projectServerNames)){
    		nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));
    	}else{
    		nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
					.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
    	}
    	
    	// 循环执行任务
    	for (String projectServerName : nameList) {	
			DbContextHolder.setDBType(projectServerName);
			// 获取未推送消息List
			List<SendTemplateMsg> msgList = msgService.queryNonExecutionMsgList();
			if(!msgList.isEmpty()){
				logger.error("推送模板信息执行计划条数" + msgList.size());
				// 发消息
				String msg = null;
				String nowTime = DateUtil.getDateTime("yyyy-MM-dd HH");
				for (SendTemplateMsg sendTemplateMsg : msgList) {
					if(sendTemplateMsg.getSendTime().substring(0, 13).compareTo(nowTime) == 0){
						if(Constant.sendMessageType.messageType_1.equals(sendTemplateMsg.getMessageType())){
							msg = msgService.sendTemplateMsg(sendTemplateMsg);
						}else{
							msg = msgService.sendAppletTemplateMsg(sendTemplateMsg);
						}
						logger.error("模板消息主键：" + sendTemplateMsg.getInfoKey() + ",执行结果为：" +msg);
					}
				}
			}
			DbContextHolder.clearDBType();
			Thread.sleep(500);
		}
		logger.error("推送模板信息job结束");
	}
}
