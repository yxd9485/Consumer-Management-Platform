package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.VpsVcodeMonthCityRankJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="vpsVcodeMonthCityRankJobHandler")
public class VpsVcodeMonthCityRankJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private VpsVcodeMonthCityRankJob vpsVcodeMonthCityRankJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			vpsVcodeMonthCityRankJob.executeRankHistory();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"vpsVcodeMonthCityRankJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("月度城市酒王排名Job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"vpsVcodeMonthCityRankJobHandler FAIL");
	}

}
