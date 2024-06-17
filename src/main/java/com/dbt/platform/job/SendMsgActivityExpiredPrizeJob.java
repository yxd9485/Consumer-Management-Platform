package com.dbt.platform.job;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.wctaccesstoken.bean.WechatRemindTemplateMsg;

@Service("sendMsgActivityExpiredPrizeJob")
public class SendMsgActivityExpiredPrizeJob {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private  VcodeActivityService  vcodeActivityService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    
    @SuppressWarnings("unchecked")
	public void sendMsgActivity() throws Exception {
        log.warn("活动短信预警Job开始执行...");
   		DbContextHolder.clearDBType();
       	Set<String> nameList = ((Map<String, ServerInfo>) RedisApiUtil.getInstance()
					.getObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO)).keySet();
       	// 循环执行任务
       	for (String projectServerName : nameList) {	
       		  this.sendMsgActivity(projectServerName);
       		  DbContextHolder.clearDBType();
       		Thread.sleep(500);
    	}
    } 

    /**
     * 手动触发
     * 【青啤福建】“万能签到”活动中的“端午节经典签到活动”，将于“6月29日”过期，请关注是否需要调整
     */
    @RequestMapping("/sendMsgActivityExpiredPrizeJob")
    public void sendMsgActivity(String projectServerName) {
        log.warn("活动短信预警Job开始执行...");
        StringBuffer smsContent = null;
        DbContextHolder.setDBType(projectServerName);

        // AI助手appid 
        String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
                    .FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.AI_APPID);
        String[] openidAry = StringUtils.defaultIfBlank(DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.AI_REMIND_OPENID, DatadicKey.aiRemindOpenid.AI_REMIND_ACTIVITY_END), "").split(",");
        if (StringUtils.isBlank(appid) || openidAry.length == 0) return;
        
        //项目中文名
        String projectName = DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_NAME);
		// 获取需要活动短信预警
        List<VcodeActivityCog> resultLst = vcodeActivityService.queryActivityExpiredPrize();
        if (resultLst == null || resultLst.isEmpty()) {
            log.warn("time:" + DateUtil.getDate() + " --- 无要预警的活动");
        } else {
            for (VcodeActivityCog vcodeActivityCog : resultLst) {
            	smsContent = new StringBuffer();
				smsContent.append("活动类型:").append(vcodeActivityCog.getActivityType())
				.append("\r\n").append("活动名称:").append(vcodeActivityCog.getVcodeActivityName())
				.append("\r\n").append("过期日期:").append(vcodeActivityCog.getEndDate())
				.append("\r\n").append("请关注是否需要调整");
				for (String openid : openidAry) {
				    taskExecutor.execute(new WechatRemindTemplateMsg(appid, openid).initRemindMsg("活动结束预警", projectName, smsContent.toString()));
                }
			}
        }
        log.warn(projectName+"活动短信预警Job执行结束...");
	}
}
