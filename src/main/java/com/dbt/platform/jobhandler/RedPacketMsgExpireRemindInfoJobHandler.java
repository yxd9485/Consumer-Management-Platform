package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.RedPacketMsgExpireRemindInfoJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="redPacketMsgExpireRemindInfoJobHandler")
public class RedPacketMsgExpireRemindInfoJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private RedPacketMsgExpireRemindInfoJob redPacketMsgExpireRemindInfoJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			redPacketMsgExpireRemindInfoJob.totalRedPacketMsgExpireRemind();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"RedPacketMsgExpireRemindInfoJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("红包金额/红包个低于阈值统计ob异常", e);
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"RedPacketMsgExpireRemindInfoJobHandler FAIL");
	}

}
