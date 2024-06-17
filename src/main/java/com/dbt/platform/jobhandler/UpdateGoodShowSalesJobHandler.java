package com.dbt.platform.jobhandler;

import com.dbt.platform.job.RecyclePrizeJob;
import com.dbt.platform.job.UpdateGoodShowSalesJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@JobHandler(value="UpdateGoodShowSalesJobHandler")
public class UpdateGoodShowSalesJobHandler extends IJobHandler {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private UpdateGoodShowSalesJob updateGoodShowSalesJob;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        try {
            updateGoodShowSalesJob.updateShowNum();
            return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"updateGoodShowSalesJob SUCCESS");
        } catch (Exception e) {
            log.error("销量减少异常", e);
            XxlJobLogger.log(e);
            if(e instanceof InterruptedException){
                throw e;
            }
        }
        return new ReturnT<String>(IJobHandler.FAIL.getCode(),"updateGoodShowSalesJob FAIL");
    }

}
