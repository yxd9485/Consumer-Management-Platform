package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.SyncAgencyInfoFromMnJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * @author hanshimeng
 * @date 2021/12/16
 **/
@Component
@JobHandler(value = "syncAgencyInfoFromMnJobHandler")
public class SyncAgencyInfoFromMnJobHandler extends IJobHandler {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private SyncAgencyInfoFromMnJob syncAgencyInfoFromMnJob;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
		try {
			syncAgencyInfoFromMnJob.updateAgencyInfo(param);
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"SyncAgencyInfoFromMnJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("蒙牛同步经销商数据job异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"SyncAgencyInfoFromMnJobHandler FAIL");
	
    }
}
