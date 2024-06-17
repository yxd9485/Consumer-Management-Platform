package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.WechatmovementCompetitionResultJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="sendWmMsgForResultHandler")
public class SendWmMsgForResultHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private WechatmovementCompetitionResultJob wechatmovementCompetitionResultJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			wechatmovementCompetitionResultJob.executeCompetitionResult();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"SendWmMsgForResultHandler SUCCESS");
		} catch (Exception e) {
			log.error("微信运动比赛结果job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"SendWmMsgForResultHandler FAIL");
	}

}
