package com.dbt.platform.jobhandler;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.platform.fission.service.IVpsVcodeActivateRedEnvelopeRuleCogService;
import com.dbt.platform.job.VpointsExchangeJob;
import com.dbt.platform.redenveloperain.service.RedEnvelopeRainActivityService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 红包雨资金锁定处理
 */
@Component
@JobHandler(value="backRedEnvelopeMoneyJobHandler")
public class BackRedEnvelopeMoneyJobHandler extends IJobHandler{
	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private RedEnvelopeRainActivityService redEnvelopeRainActivityService;
	
	@Override
	public ReturnT<String> execute(String param) throws Exception {
		try {
			DbContextHolder.clearDBType();
			Set<String> nameList = null;
			String projectServerNames = DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.PROJECT_JOB,
					DatadicKey.ProjectJob.BACK_MONEY_TO_RED_ENVELOPE_ACTIVITY_JOB);
			if(StringUtils.isBlank(projectServerNames)){
				new ReturnT<String>(IJobHandler.FAIL.getCode(),"backRedEnvelopeMoneyJobHandler FAIL");
			}
			nameList = new HashSet<String>(Arrays.asList(projectServerNames.split(",")));

			for (String serVerName : nameList) {
				try {
					DbContextHolder.setDBType(serVerName);
					Thread.sleep(500);
					redEnvelopeRainActivityService.updateBackRedEnvelopeMoneyJobHandler();
					DbContextHolder.clearDBType();
					Thread.sleep(500);


				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

        	return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"backRedEnvelopeMoneyJobHandler SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("红包雨未领取的金额缓存回滚Job异常:" + e.getCause().getMessage());
			XxlJobLogger.log(e);
			if(e instanceof InterruptedException){
				throw e;
			}
		}
		return new ReturnT<String>(IJobHandler.FAIL.getCode(),"backRedEnvelopeMoneyJobHandler FAIL");
	}

}
