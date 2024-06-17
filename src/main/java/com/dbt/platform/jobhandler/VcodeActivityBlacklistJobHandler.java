package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.VcodeActivityBlacklistJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="vcodeActivityBlacklistJobHandler")
public class VcodeActivityBlacklistJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private VcodeActivityBlacklistJob vcodeActivityBlacklistJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			vcodeActivityBlacklistJob.dubiousUserConvertBlackUser();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"VcodeActivityBlacklistJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("可疑用户一个月后自动进入黑名单Job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"VcodeActivityBlacklistJobHandler FAIL");
	}

}
