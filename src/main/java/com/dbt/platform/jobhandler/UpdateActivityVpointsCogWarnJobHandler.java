package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.UpdateActivityVpointsCogJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="updateActivityVpointsCogWarnJobHandler")
public class UpdateActivityVpointsCogWarnJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private UpdateActivityVpointsCogJob updateActivityVpointsCogJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			updateActivityVpointsCogJob.updateBathWaitActivityVpointsCogWarn();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"UpdateActivityVpointsCogWarnJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("奖项占比预警Job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"UpdateActivityVpointsCogWarnJobHandler FAIL");
	}

}
