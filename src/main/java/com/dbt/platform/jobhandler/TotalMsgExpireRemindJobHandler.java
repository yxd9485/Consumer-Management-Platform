package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.MsgExpireRemindInfoJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="totalMsgExpireRemindJobHandler")
public class TotalMsgExpireRemindJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private MsgExpireRemindInfoJob msgExpireRemindInfoJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			msgExpireRemindInfoJob.totalMsgExpireRemind();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"TotalMsgExpireRemindJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("统计规则到期提醒异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"TotalMsgExpireRemindJobHandler FAIL");
	}

}
