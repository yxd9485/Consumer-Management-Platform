package com.dbt.vpointsshop.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.vpointsshop.bean.VpointsGoodsConsumerStatusInfo;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;
import com.dbt.vpointsshop.dao.IVpointsGoodsConsumerStatusInfoDao;

/**
 * 商品到货通知Service
 */
@Service
public class VpointsGoodsConsumerStatusInfoService extends BaseService<VpointsGoodsConsumerStatusInfo> {
    
    @Autowired
    private IVpointsGoodsConsumerStatusInfoDao statusInfoDao;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    
    /**
     * 依据商品id获取要通知的用户
     */
    public List<VpointsGoodsConsumerStatusInfo> queryArrivalNoticeByGoodsId(String goodsId) {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goodsId);
        return statusInfoDao.queryArrivalNoticeByGoodsId(map);
    }
    
    /**
     * 依据商品id更新订阅状态
     */
    public void updateArrivalNoticeFlag(String goodsId, String arrivalNoticeFlag) {
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goodsId);
        map.put("arrivalNoticeFlag", arrivalNoticeFlag);
        statusInfoDao.updateArrivalNoticeFlag(map);
    }
    
    /**
     * 通知订阅了本商品到货通知的用户
     * @param goodsId
     */
    public void noticeByGoodsId(VpointsGoodsInfo goodsInfo) {
        String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
        String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
        String templateId = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_GOODSARRIVAL);
        
        // 获取要通知的用户
        List<VpointsGoodsConsumerStatusInfo> relationLst = queryArrivalNoticeByGoodsId(goodsInfo.getGoodsId());
        taskExecutor.execute(new VpointsGoodsNoticeTempletMsg(appid, appsec, templateId, goodsInfo, relationLst));
        
        // 取消订阅
        updateArrivalNoticeFlag(goodsInfo.getGoodsId(), "0");
    }

    /**
     * 商城秒杀预约提醒JOB
     * @param goodsId
     */
    public void secKillRemind() {
        
        String remindStartTime = DateUtil.getDateTime();
        String remindEndTime = DateUtil.add(remindStartTime, 35, Calendar.MINUTE, DateUtil.DEFAULT_DATETIME_FORMAT);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("remindStartTime", remindStartTime);
        map.put("remindEndTime", remindEndTime);
        List<VpointsGoodsConsumerStatusInfo> statusInfoLst = statusInfoDao.querySecKillRemind(map);
        if (CollectionUtils.isNotEmpty(statusInfoLst)) {
            String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
            String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
            String templateId = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_SECKILLREMIND);
            List<String> infoKeyLst = new ArrayList<>();
            for (VpointsGoodsConsumerStatusInfo item : statusInfoLst) {
                infoKeyLst.add(item.getInfoKey());
                taskExecutor.execute(new VpointsSecKillRemindTempletMsg(appid, appsec, templateId, item));
            }
            
            statusInfoDao.updateSecKillRemindFlagForSend(infoKeyLst);
            log.warn("商城秒杀预约提醒发送：" + infoKeyLst.size() + "条");
        } else {
            log.warn("暂无要提醒的商城秒杀预约");
        }
    }
    
    /**
        * 商城团购活动提醒JOB
     * @param goodsId
     */
    public void groupRemind() {
    	
    	// TODO 是否要考虑通用模板通用方法
    	// 1. 模板替换
    	// 2. 发送成功需要修改活动已发送状态（什么情况下置为初始状态？每次更新活动吗？）
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("remindTime", DateUtil.add(DateUtil.getDateTime(), 2, Calendar.HOUR, DateUtil.DEFAULT_DATETIME_FORMAT));
        List<VpointsGoodsConsumerStatusInfo> statusInfoLst = statusInfoDao.queryGroupActivityRemind(map);
        if (CollectionUtils.isNotEmpty(statusInfoLst)) {
            String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
            String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
            String templateId = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PAAPPLET_SECKILLREMIND);
            List<String> infoKeyLst = new ArrayList<>();
            for (VpointsGoodsConsumerStatusInfo item : statusInfoLst) {
                infoKeyLst.add(item.getInfoKey());
                taskExecutor.execute(new VpointsSecKillRemindTempletMsg(appid, appsec, templateId, item));
            }
            
            statusInfoDao.updateSecKillRemindFlagForSend(infoKeyLst);
            log.warn("商城拼团活动提醒发送：" + infoKeyLst.size() + "条");
        } else {
            log.warn("暂无要提醒的商城拼团活动");
        }
    }
}

class VpointsGoodsNoticeTempletMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String appid;
    private String appsec;
    private String templateId;
    private VpointsGoodsInfo goodsInfo;
    private List<VpointsGoodsConsumerStatusInfo> relationLst;
    
    public VpointsGoodsNoticeTempletMsg(String appid, String appsec, String templateId, 
            VpointsGoodsInfo goodsInfo, List<VpointsGoodsConsumerStatusInfo> relationLst) {
        this.appid = appid;
        this.appsec = appsec;
        this.templateId = templateId;
        this.goodsInfo = goodsInfo;
        this.relationLst = relationLst;
    }
    
    @Override
    public void run() {
        String currDate = DateUtil.getDate();
        for (VpointsGoodsConsumerStatusInfo item : relationLst) {
            try {
        		String messageOpenid = item.getPaOpenid();
                if(DatadicUtil.isSwitchON(DatadicUtil.dataDicCategory.FILTER_SWITCH_SETTING, DatadicUtil.dataDic.filterSwitchSetting.SWITCH_VIP_SYSTEM)) {
                	appid = DatadicUtil.getDataDicValue(
                            DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                            DatadicKey.filterWxPayTemplateInfo.MEMBERAPPLET_APPID);
                	messageOpenid = item.getMemberOpenid();
                }
                		
                String sendText = this.initMsg(messageOpenid, currDate);
                if(StringUtils.isNotBlank(sendText)){
                    boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
                    log.error("到货通知小程序订阅消息发送" + (sendFlag ? "成功" : "失败") +"，paOpenid:" + item.getPaOpenid());
                }
            } catch (Exception e) {
                log.error("到货通知小程序订阅消息发送失败，paOpenid:" + item.getPaOpenid()); 
            }
        }
        
    }
    
    private String initMsg(String openid, String currDate) throws Exception {
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(openid);
        msg.setTemplate_id(templateId);
        
        // 跳转连接及参数 
        if (Constant.exchangeChannel.CHANNEL_6.equals(goodsInfo.getExchangeChannel())) {
            msg.setPage("pages/index/goods/goods?id=" + goodsInfo.getGoodsId());
        } else {
            // TODO 积分商场的暂无
            // msg.setPage("pages/push/hw");
        }
        String goodsName = StringUtils.defaultIfBlank(goodsInfo.getGoodsShortName(), "");
        double goodsRealPayMoney = goodsInfo.getGoodsPay() * Integer.valueOf(goodsInfo.getGoodsDiscount()) / 100D;
        MsgAppletData data=new MsgAppletData();
        data.setName1(new MsgValue("关注商品已上架", null));
        data.setAmount2(new MsgValue(String.format("%.2f", goodsRealPayMoney / 100D) + "元", null));
        data.setThing8(new MsgValue(goodsName.length() > 20 ? goodsName.substring(0, 20) : goodsName, null));
        msg.setData(data);
        return JSON.toJSONString(msg);
    }
}
class VpointsSecKillRemindTempletMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String appid;
    private String appsec;
    private String templateId;
    private VpointsGoodsConsumerStatusInfo statusInfo;
    
    public VpointsSecKillRemindTempletMsg(String appid, 
            String appsec, String templateId, VpointsGoodsConsumerStatusInfo statusInfo) {
        this.appid = appid;
        this.appsec = appsec;
        this.templateId = templateId;
        this.statusInfo = statusInfo;
    }
    
    @Override
    public void run() {
    	String messageOpenid = statusInfo.getPaOpenid();
        try {
            if(DatadicUtil.isSwitchON(DatadicUtil.dataDicCategory.FILTER_SWITCH_SETTING, DatadicUtil.dataDic.filterSwitchSetting.SWITCH_VIP_SYSTEM)) {
            	appid = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.MEMBERAPPLET_APPID);
            	messageOpenid = statusInfo.getMemberOpenid();
            }
            
            boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(this.initMsg(), appid, appsec);
            log.error("商城秒杀预约小程序订阅消息发送" + (sendFlag ? "成功" : "失败") +"，paOpenid:" + statusInfo.getPaOpenid());
        } catch (Exception e) {
            log.error("商城秒杀预约小程序订阅消息发送失败，paOpenid:" + messageOpenid);
        }
    }
    
    private String initMsg() throws Exception {
    	// 获取推送消息的openid
    	String messageOpenid = DatadicUtil.isSwitchON(DatadicUtil.dataDicCategory.FILTER_SWITCH_SETTING,
    			DatadicUtil.dataDic.filterSwitchSetting.SWITCH_VIP_SYSTEM) ? statusInfo.getMemberOpenid() : statusInfo.getPaOpenid();
    			
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(messageOpenid);
        msg.setTemplate_id(templateId);
        if(DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM))) {
        	msg.setPage("pagesMall/mall/goodsDetail?id=" + statusInfo.getGoodsId() + "&projectFlag=" + 
        			DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG));
        }else {
        	if ("henanpz".equals(DbContextHolder.getDBType())) {
                msg.setPage("pagesMall/mall/goodsDetail?id=" + statusInfo.getGoodsId());
            } else {
                msg.setPage("pages/goodsDetail/goodsDetail?id=" + statusInfo.getGoodsId());
            }
        }
        
        msg.setEmphasis_keyword("name1.DATA");
        
        String name = statusInfo.getGoodsShortName().length() > 10 ? statusInfo.getGoodsShortName().substring(0, 10) : statusInfo.getGoodsShortName();
        name = name.replaceAll( "\\d+" , "" );
        
        MsgAppletData data=new MsgAppletData();
        data.setName1(new MsgValue(name, null));
        data.setDate5(new MsgValue(statusInfo.getGoodsStartTime().substring(0, 16), null));
        data.setAmount2(new MsgValue(String.format("%.2f", statusInfo.getRealPay() / 100D) + "元", null));
        data.setNumber7(new MsgValue(String.valueOf(statusInfo.getGoodsNum()), null));
        data.setThing8(new MsgValue("你关注的秒杀商品马上要开始了，请及时关注", null));
        msg.setData(data);
        System.out.println(JSON.toJSONString(msg));
        return JSON.toJSONString(msg);
    }
}

class VpointsTempletMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String appid;
    private String appsec;
    private String templateId;
    private VpointsGoodsConsumerStatusInfo statusInfo;
    
    public VpointsTempletMsg(String appid, 
            String appsec, String templateId, VpointsGoodsConsumerStatusInfo statusInfo) {
        this.appid = appid;
        this.appsec = appsec;
        this.templateId = templateId;
        this.statusInfo = statusInfo;
    }
    
    @Override
    public void run() {
        try {
            boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(this.initMsg(), appid, appsec);
            log.error("商城秒杀预约小程序订阅消息发送" + (sendFlag ? "成功" : "失败") +"，paOpenid:" + statusInfo.getPaOpenid());
        } catch (Exception e) {
            log.error("商城秒杀预约小程序订阅消息发送失败，paOpenid:" + statusInfo.getPaOpenid()); 
        }
    }
    
    private String initMsg() throws Exception {
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(statusInfo.getPaOpenid());
        msg.setTemplate_id(templateId);
        msg.setEmphasis_keyword("name1.DATA");
        
        if(DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM))) {
        	msg.setPage("pagesMall/mall/goodsDetail?id=" + statusInfo.getGoodsId() + "&projectFlag=" + 
        			DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG));
        }else {
        	if ("henanpz".equals(DbContextHolder.getDBType())) {
                msg.setPage("pagesMall/mall/goodsDetail?id=" + statusInfo.getGoodsId());
            } else {
                msg.setPage("pages/goodsDetail/goodsDetail?id=" + statusInfo.getGoodsId());
            }
        }
        
        String name = statusInfo.getGoodsShortName().length() > 10 ? statusInfo.getGoodsShortName().substring(0, 10) : statusInfo.getGoodsShortName();
        name = name.replaceAll( "\\d+" , "" );
        
        MsgAppletData data=new MsgAppletData();
        data.setName1(new MsgValue(name, null));
        data.setDate5(new MsgValue(statusInfo.getGoodsStartTime().substring(0, 16), null));
        data.setAmount2(new MsgValue(String.format("%.2f", statusInfo.getRealPay() / 100D) + "元", null));
        data.setNumber7(new MsgValue(String.valueOf(statusInfo.getGoodsNum()), null));
        data.setThing8(new MsgValue("你关注的秒杀商品马上要开始了，请及时关注", null));
        msg.setData(data);
        return JSON.toJSONString(msg);
    }
}
