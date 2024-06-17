package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.VpsQrcodeAutoOrderJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="vpsQrcodeAutoOrderBindActivityJobHandler")
public class VpsQrcodeAutoOrderBindActivityJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private VpsQrcodeAutoOrderJob qrcodeAutoOrderJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			qrcodeAutoOrderJob.batchBindActivity();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"vpsQrcodeAutoOrderBindActivityJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("自动码源订单批次绑定活动Job失败:" + e.getCause().getMessage());
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"vpsQrcodeAutoOrderBindActivityJobHandler FAIL");
	}

}
