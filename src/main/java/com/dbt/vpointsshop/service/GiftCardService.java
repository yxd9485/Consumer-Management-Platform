package com.dbt.vpointsshop.service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.platform.appuser.bean.VpsConsumerThirdAccountInfo;
import com.dbt.platform.appuser.dao.IVpsConsumerThirdAccountInfoDao;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsGoodsInfo;
import com.dbt.vpointsshop.dao.VpointsGoodsDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 兑换记录service
 *
 * @author zhaohongtao
 * 2017年12月11日
 */
@Service
public class GiftCardService extends BaseService<VpointsExchangeLog> {
    /**
     * 撤单方法
     */
    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private VpointsGoodsDao goodsDao;
    @Autowired
    private VpsConsumerUserInfoService vpsConsumerUserInfoService;
    @Autowired
    private IVpsConsumerThirdAccountInfoDao thirdAccountInfoDao;

    public void execute(String projectServerName){
        log.info("**************礼品卡自动退单任务开始*********************");
        long startTime = System.currentTimeMillis();
//        DbContextHolder.setDBType("hebei");
        //refundType 撤单类型 0 退货退款 1 仅退款s
        String refundType = "0";
        //giftCardStatus礼品卡状态 0 未领取 1 已领取
        String giftCardStatus = "0";
        List<VpointsExchangeLog> allGiftCardOrder = exchangeService.queryAllNotReceiveGiftCardOrder(7);
        for (VpointsExchangeLog exchangeLog : allGiftCardOrder) {
            VpointsExchangeLog byId = exchangeService.findById(exchangeLog.getExchangeId());
            //未领取的退款，这个使二次保护，防止退款过程中有人领取了
            if (giftCardStatus.equals(byId.getGiftCardStatus())) {
                //支付金额 除以100
                BigDecimal bigDecimalMoney = new BigDecimal(byId.getExchangePay());
                String money = bigDecimalMoney.divide(BigDecimal.valueOf(100L), 2, BigDecimal.ROUND_DOWN).toString();
                BigDecimal bigDecimalVpoints = new BigDecimal(byId.getExchangeVpoints());
                try {
                    exchangeService.revokeOrder(byId.getExchangeId(), refundType, money, bigDecimalVpoints.toString(), "礼品卡到期自动退款");
                    log.info("礼品卡定时撤单业务-->订单id:" + byId.getExchangeId() + " 退款金额(元):" + money + " 退款积分" + bigDecimalVpoints);
                }catch (Exception businessException){
                    log.info(businessException.getMessage()+"订单id:" + byId.getExchangeId() + " 退款金额(元):" + money + " 退款积分" + bigDecimalVpoints);
                }
                String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
                String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
                String sendText = this.initMsg(appid, byId,money,projectServerName);
                boolean  sendFlag = false;
                try {
                    sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if(sendFlag){
                    log.info("微信通知成功 订单id:"+byId.getExchangeId()+" userKey:"+byId.getUserKey());
                }else{
                    log.error("******微信通知失败 订单id:"+byId.getExchangeId()+" userKey:"+byId.getUserKey());
                }

            }
        }
        long endTime = System.currentTimeMillis();
        log.info("礼品卡自动退单耗时：" + ( endTime- startTime));
        log.info("**************礼品卡自动退单任务结束*********************");
    }

    /**
     * 为赠送礼品卡通知
     */
    public void GiftCardGiveRemidJob(String projectServerName) {
        try {
            List<VpointsExchangeLog> vpointsExchangeLogs = exchangeService.queryAllNotReceiveGiftCardOrder(1);
            String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
            String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
            vpointsExchangeLogs.forEach(vpointsExchangeLog -> {
//                if (StringUtils.isEmpty(RedisApiUtil.getInstance().get("GiftCardGiveRemidJobHandler:" + vpointsExchangeLog.getExchangeId()))) {
                    String sendText = this.initMsg(vpointsExchangeLog,projectServerName);
                    boolean  sendFlag = false;
                    try {
                        sendFlag = WechatUtil.sendAppletTemplateMsg(sendText, appid, appsec);
//                        sendFlag = true;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
//                    if(sendFlag){
//                        RedisApiUtil.getInstance().set("GiftCardGiveRemidJobHandler:" + vpointsExchangeLog.getExchangeId(), "success", 60 * 60 * 24 * 7);
//                        log.info("礼品卡未赠送通知成功 订单id:"+vpointsExchangeLog.getExchangeId()+" userKey:"+vpointsExchangeLog.getUserKey());
//                    }else{
//                        log.error("礼品卡未赠送通知失败 订单id:"+vpointsExchangeLog.getExchangeId()+" userKey:"+vpointsExchangeLog.getUserKey());
//                    }
//                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //礼品卡未赠送通知
    private String initMsg(VpointsExchangeLog vpointsExchangeLog,String projectServerName) {
        VpsConsumerThirdAccountInfo thirdAccountInfo =
                thirdAccountInfoDao.queryThirdAccountInfoByUserKey(vpointsExchangeLog.getUserKey());
        if (StringUtils.isBlank(thirdAccountInfo.getPaOpenid())) return null;
        MsgAppletBean msg = new MsgAppletBean();
        msg.setTemplate_id(DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_GIFT_CARD_GIVE_REMID));
//        msg.setTemplate_id("eWcd0QNc765_ovcJzznm_fHgt1apVlHtWkVjee8mGGo");
        msg.setPage("pagesGiftCard/giftList");
        VpointsGoodsInfo goodsInfo = goodsDao.getGoodsInfo(vpointsExchangeLog.getGoodsId());
        MsgAppletData data=new MsgAppletData();
        // 礼品待赠送提醒
        if(!"hebei".equals(DbContextHolder.getDBType())
        		&& DatadicUtil.isSwitchON(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM)){
            msg.setTouser(thirdAccountInfo.getMemberOpenid());
            data.setThing1(new MsgValue(goodsInfo.getGoodsShortName(), null));
            data.setDate4(new MsgValue(vpointsExchangeLog.getExchangeTime(), null));
            data.setThing3(new MsgValue("礼品卡超24小时未赠送，点击赠送礼品卡。",null));
        }else{
            msg.setTouser(thirdAccountInfo.getPaOpenid());
            data.setThing1(new MsgValue(goodsInfo.getGoodsShortName(), null));
            data.setPhrase2(new MsgValue("待赠送", null));
            data.setThing3(new MsgValue("礼品卡超24小时未赠送，点击赠送礼品卡。",null));
        }
        msg.setData(data);
        msg.setColor("#173177");
        // 标题字体放大
        return JSON.toJSONString(msg);
    }
    // //礼品卡撤单
    private String initMsg(String appletId, VpointsExchangeLog vpointsExchangeLog,  String money,String projectServerName ) {
        VpsConsumerThirdAccountInfo vpsConsumerThirdAccountInfo = vpsConsumerUserInfoService.queryOpenidByUserkey(vpointsExchangeLog.getUserKey());
        MsgAppletBean msg = new MsgAppletBean();
        if(vpsConsumerThirdAccountInfo!= null){
            String openid = vpsConsumerThirdAccountInfo.getPaOpenid();
            if(DatadicUtil.isSwitchON(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM)){
                msg.setTouser(vpsConsumerThirdAccountInfo.getMemberOpenid());
            }else{
                msg.setTouser(vpsConsumerThirdAccountInfo.getPaOpenid());
            }
            msg.setTemplate_id(DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_GIFT_CARD));
//        msg.setTemplate_id("6L5CT5Hvvbn5vFzwh5Rw3eu29rqr_OI4ywupQHvPdZU");
            msg.setPage("pages/index/index");

            MsgAppletData data=new MsgAppletData();
            // 订单号
            data.setCharacter_string1(new MsgValue(vpointsExchangeLog.getExchangeId(),null));
            //下单时间
            data.setTime2(new MsgValue(vpointsExchangeLog.getExchangeTime(), null));
            //退款金额
            data.setAmount3(new MsgValue(money+"元", null));
            //退款原因
            data.setThing4(new MsgValue("您的朋友未填写收货地址", null));
            msg.setData(data);
            msg.setColor("#173177");
            // 标题字体放大
        }
        return JSON.toJSONString(msg);
    }

}

