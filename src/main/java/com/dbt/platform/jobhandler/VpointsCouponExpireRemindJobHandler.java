package com.dbt.platform.jobhandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.platform.job.VpointsCouponExpireRemindJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;

@Component
@JobHandler(value="vpointsCouponExpireRemindJobHandler")
public class VpointsCouponExpireRemindJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private VpointsCouponExpireRemindJob vpointsCouponExpireRemindJob;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
		    vpointsCouponExpireRemindJob.couponExpireRemind();
        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"VpointsCouponExpireRemindJobHandler SUCCESS");
		} catch (Exception e) {
			log.error("商城优惠券过期提醒异常");
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"VpointsCouponExpireRemindJobHandler FAIL");
	}
}
