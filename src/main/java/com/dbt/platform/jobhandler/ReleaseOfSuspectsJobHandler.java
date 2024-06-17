package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.ReleaseOfSuspectsJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="releaseOfSuspectsJobHandler")
public class ReleaseOfSuspectsJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private ReleaseOfSuspectsJob releaseOfSuspectsJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			releaseOfSuspectsJob.updateReleaseOfSuspects();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"ReleaseOfSuspectsJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("定时释放可疑人员任务异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"ReleaseOfSuspectsJobHandler FAIL");
	}

}
