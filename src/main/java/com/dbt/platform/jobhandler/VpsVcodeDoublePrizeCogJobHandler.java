package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.VpsVcodeDoublePrizeCogJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="vpsVcodeDoublePrizeCogJobHandler")
public class VpsVcodeDoublePrizeCogJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private VpsVcodeDoublePrizeCogJob vpsVcodeDoublePrizeCogJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			vpsVcodeDoublePrizeCogJob.clearDoublePrizeForUser();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"VpsVcodeDoublePrizeCogJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("清除已结束且未清除过的活动的标签Job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"VpsVcodeDoublePrizeCogJobHandler FAIL");
	}

}
