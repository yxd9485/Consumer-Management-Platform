package com.dbt.platform.jobhandler;

import com.dbt.platform.job.CheckImgByWechatJob;
import com.dbt.platform.job.UpdateGoodShowSalesJob;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@JobHandler(value="CheckImgByWechatJobHandler")
public class CheckImgByWechatJobHandler extends IJobHandler {
    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private CheckImgByWechatJob checkImgByWechatJob;
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        try {
            checkImgByWechatJob.checkImg();
            return new ReturnT<String>(IJobHandler.SUCCESS.getCode(),"checkImgByWechatJob SUCCESS");
        } catch (Exception e) {
            log.error("图片过滤job减少异常", e);
            XxlJobLogger.log(e);
            if(e instanceof InterruptedException){
                throw e;
            }
        }
        return new ReturnT<String>(IJobHandler.FAIL.getCode(),"checkImgByWechatJob FAIL");
    }

}
