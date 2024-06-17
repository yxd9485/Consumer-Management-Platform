package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.RecyclePrizeJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="recyclePrizeJobHandler")
public class RecyclePrizeJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private RecyclePrizeJob recyclePrizeJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			recyclePrizeJob.recyclePrize();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"RecyclePrizeJob SUCCESS");
		} catch (Exception e) {
			log.error("大奖回收Job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"RecyclePrizeJob FAIL");
	}

}
