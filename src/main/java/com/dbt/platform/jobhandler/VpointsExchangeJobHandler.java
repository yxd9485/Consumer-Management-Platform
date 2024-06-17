package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.VpointsExchangeJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="vpointsExchangeJobHandler")
public class VpointsExchangeJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private VpointsExchangeJob vpointsExchangeJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			vpointsExchangeJob.updateExpressSignJob();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"vpointsExchangeJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("商城-发货后15天更新订单签收状态Job异常:" + e.getCause().getMessage());
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"vpointsExchangeJobHandler FAIL");
	}

}