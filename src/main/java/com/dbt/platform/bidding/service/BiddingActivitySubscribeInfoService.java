package com.dbt.platform.bidding.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.platform.bidding.bean.BiddingActivitySubscribeInfo;
import com.dbt.platform.bidding.dao.IBiddingActivitySubscribeInfoDao;

/**
 * 竞价活动订阅Service
 * @author Administrator
 *
 */
@Service
public class BiddingActivitySubscribeInfoService extends BaseService<BiddingActivitySubscribeInfo>{
	
	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IBiddingActivitySubscribeInfoDao bSubscribeInfoDao;

	/**
	 * 竞价活动提醒
	 * @param templateFlag 模板标识：1 活动开始，2活动即将结束， 3活动开奖结果
	 */
	public void sendActivityRemind(String templateFlag) {
        String templateName = null;
		Map<String, Object> map = new HashMap<String, Object>();
		if("1".equals(templateFlag)) {
			templateName = "竞价活动开始";
			map.put("activityStartFlag", "1");
		}else if("2".equals(templateFlag)) {
			templateName = "竞价活动即将结束";
			map.put("activityEndFlag", "1");
		}
		map.put("periodsNumber", DateUtil.getDate());
        List<BiddingActivitySubscribeInfo> statusInfoLst = bSubscribeInfoDao.querySubscribeInfoList(map);
        if (CollectionUtils.isNotEmpty(statusInfoLst)) {
            String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
            String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
            String templateId = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
            		DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_BIDDINGREMIND + Constant.DBTSPLIT + templateFlag);
            
            // 竞价活动开场次数累计缓存
			String key = "biddingActivityOpeningNumber:" + DateUtil.getDate();
			List<String> infoKeyLst = new ArrayList<>();
            for (BiddingActivitySubscribeInfo item : statusInfoLst) {
            	// 判断是否已达开场人数
            	if("1".equals(templateFlag) && item.getOpeningNumber() > 0 
            			&& item.getOpeningNumber() > RedisApiUtil.getInstance().getSetNum(key)) continue;
            	
            	// 判断月擂台是否是最后一天
            	if(Constant.BIDDING_ACTIVITY_TYPE.TYPE_2.equals(item.getActivityType()) && DateUtil.getMonthLastDay().compareTo(DateUtil.addDays(0))!= 0) continue;
            	
                infoKeyLst.add(item.getInfoKey());
                taskExecutor.execute(new BiddingRemindTempletMsg(appid, appsec, templateId, item, templateFlag));
            }
            
            if(CollectionUtils.isNotEmpty(infoKeyLst) && infoKeyLst.size() > 0) {
            	map.clear();
                map.put("infoKeyLst", infoKeyLst);
                map.put("updateTime", DateUtil.getDateTime());
                if("1".equals(templateFlag)) {
                	map.put("activityStartFlag", "2");
        		}else if("2".equals(templateFlag)) {
        			map.put("activityEndFlag", "2");
        		}
                bSubscribeInfoDao.updateBiddingRemindFlagForSend(map);
                log.warn(templateName + "提醒发送：" + infoKeyLst.size() + "条");
            }
        } else {
            log.warn("暂无要提醒的" +templateName + "消息");
        }
    }
}

class BiddingRemindTempletMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String appid;
    private String appsec;
    private String templateId;
    private String templateFlag;
    private String templateName;
    private String date;
    private BiddingActivitySubscribeInfo statusInfo;
    
    public BiddingRemindTempletMsg(String appid, String appsec, String templateId, 
    		BiddingActivitySubscribeInfo statusInfo, String templateFlag) {
        this.appid = appid;
        this.appsec = appsec;
        this.templateId = templateId;
        this.templateFlag = templateFlag;
        this.templateName = "1".equals(templateFlag) ? "竞价活动开始": "竞价活动即将结束";
        this.statusInfo = statusInfo;
        date = DateUtil.getDate();
    }
    
    @Override
    public void run() {
        try {
            boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(this.initMsg(), appid, appsec);
            log.error(templateName + "消息发送" + (sendFlag ? "成功" : "失败") +"，paOpenid:" + statusInfo.getPaOpenid());
        } catch (Exception e) {
        	e.printStackTrace();
            log.error(templateName + "消息发送失败，paOpenid:" + statusInfo.getPaOpenid()); 
        }
    }
    
    private String initMsg() throws Exception {
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(statusInfo.getPaOpenid());
        msg.setTemplate_id(templateId);
        msg.setPage("pages/bidd/bidd?queryType=1");
        
        String name = null;
        if("1".equals(statusInfo.getIsDedicated())) {
        	name = "青岛啤酒酒水竞价专场";
        }else {
        	name = statusInfo.getActivityName().length() > 20 ? statusInfo.getActivityName().substring(0, 20) : statusInfo.getActivityName();
        	// name = name.replaceAll( "\\d+" , "" );
        }
        String typeName = "1".equals(statusInfo.getIsDedicated()) ? "酒水专场" : 
        	Constant.BIDDING_ACTIVITY_TYPE.TYPE_1.equals(statusInfo.getActivityType()) ? "日擂台" : "月擂台";
        
        MsgAppletData data=new MsgAppletData();
        if("1".equals(templateFlag)) {
        	data.setThing1(new MsgValue(name, null));
            data.setThing4(new MsgValue(typeName, null));
            data.setDate6(new MsgValue(date + " 09:00:00", null));
            data.setDate7(new MsgValue(date + " 23:59:59", null));
        }else if("2".equals(templateFlag)) {
        	 String goodsName = statusInfo.getActivityName().length() > 20 ? statusInfo.getActivityName().substring(0, 20) : statusInfo.getActivityName();
        	 goodsName = goodsName.replaceAll( "\\d+" , "" );
        	data.setThing6(new MsgValue(name, null));
            data.setThing1(new MsgValue("免费竞价赢大奖，机不可失失不再来哦~", null));
            data.setThing3(new MsgValue(goodsName, null));
            data.setDate4(new MsgValue(date + " 23:59:59", null));
            data.setThing5(new MsgValue("活动还有3.5小时结束", null));
        }
        
        msg.setData(data);
        System.out.println(JSON.toJSONString(msg));
        return JSON.toJSONString(msg);
    }
}
