package com.dbt.platform.jobhandler;

import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="testJobHandler")
public class TestJobHandler extends IJobHandler{

	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			System.out.println("jobHandler调用成功");
			return SUCCESS;
		} catch (Exception e) {
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return FAIL;
	}

}
