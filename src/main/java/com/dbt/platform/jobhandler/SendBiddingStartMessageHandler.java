package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.SendBiddingMessageJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="sendBiddingStartMessageHandler")
public class SendBiddingStartMessageHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private SendBiddingMessageJob biddingMessageJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			biddingMessageJob.activityStart();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"SendBiddingStartMessageHandler SUCCESS");
		} catch (Exception e) {
			log.error("竞价活动开始提醒消息job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"SendBiddingStartMessageHandler FAIL");
	}

}
