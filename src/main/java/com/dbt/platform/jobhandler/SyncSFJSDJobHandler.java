package com.dbt.platform.jobhandler;


import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.job.SyncAgencyInfoFromMnJob;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.service.ExchangeService;
import com.dbt.vpointsshop.service.VpointsExpressService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangshuda
 * @date 2023/5/16
 * 顺丰极速达业务 处理订单
 **/
@Component
@JobHandler(value = "SyncSFJSDJobHandler")
public class SyncSFJSDJobHandler extends IJobHandler {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private VpointsExpressService vpointsExpressService;
    @Autowired
    private ExchangeService exchangeService;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        try {

            DbContextHolder.setDBType("hebei");
            //vpoints_exhcnage_id_sf_sjd
            long len = RedisApiUtil.getInstance().lLen("vpoints_shop_order_id_sf_sjd");
            if(len>0){
                List<String> exchangeList = RedisApiUtil.getInstance().getList("vpoints_shop_order_id_sf_sjd",0,len);
                if (exchangeList==null && exchangeList.size() > 0) {
                    for (String shopOrderId : exchangeList) {
                        List<VpointsExchangeLog> exchangeLogList = exchangeService.findByShopOrderId(shopOrderId);
                        if(exchangeLogList.size()>0){
                            if(vpointsExpressService.executeSfJds(exchangeLogList)){
                                RedisApiUtil.getInstance().removeList("vpoints_shop_order_id_sf_sjd", shopOrderId);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("数据job异常", e);
            XxlJobLogger.log(e);
            if(e instanceof InterruptedException){
                throw e;
            }
        }
        return new ReturnT<String>(IJobHandler.FAIL.getCode(),"SyncSFJSDJobHandler FAIL");

    }
}
