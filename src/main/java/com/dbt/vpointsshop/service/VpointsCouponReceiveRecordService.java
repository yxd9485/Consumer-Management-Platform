package com.dbt.vpointsshop.service;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.PackRecordRouterUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.bean.VpointsCouponReceiveRecord;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.dao.IVpointsCouponReceiveRecordDao;

@Service
public class VpointsCouponReceiveRecordService extends BaseService<VpointsCouponReceiveRecord> {
	@Autowired
	private IVpointsCouponReceiveRecordDao couponReceiveRecordDao;
	@Autowired
	private ExchangeService exchangeService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

	public List<VpointsCouponReceiveRecord> queryReceiveRecordList(PageOrderInfo pageInfo, String couponKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pageInfo", pageInfo);
		map.put("couponKey", couponKey);
		return couponReceiveRecordDao.queryReceiveRecordList(map);
	}

	public int countReceiveRecordList(String couponKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("couponKey", couponKey);
		return couponReceiveRecordDao.countReceiveRecordList(map);
	}
	
	/**
     * 增加领取优惠券记录
     * 
     * @param userKey
     * @param couponCog
     * @param address
     */
    public void addReceiveRecord(String userKey, VpointsCouponCog couponCog) {
        VpointsCouponReceiveRecord receiveRecord = new VpointsCouponReceiveRecord();
        receiveRecord.setSplitTableSuffix(PackRecordRouterUtil.getTabSuffixByHash(12, userKey));
        receiveRecord.setInfoKey(UUID.randomUUID().toString());
        receiveRecord.setUserKey(userKey);
        receiveRecord.setCouponKey(couponCog.getCouponKey());
        receiveRecord.setReceiveStatus("0");
        receiveRecord.setReceiveTime(DateUtil.getDateTime());
        
        String expireTime = null;
        if ("0".equals(couponCog.getExpireDateType())) {
            expireTime = couponCog.getExpireDateLimit() + " 23:59:59";
        } else {
            expireTime = DateUtil.getDateTime(DateUtil.addDays(Integer.valueOf(StringUtils
                    .defaultIfBlank(couponCog.getExpireDateDays(), "0"))), "yyyy-MM-dd 23:59:59");
        }
        receiveRecord.setExpireTime(expireTime);
        
        couponReceiveRecordDao.addReceive(receiveRecord);
    }

	/**
	 * 导出发行数据
	 * @param couponKey
	 * @param response
	 * @throws Exception
	 */
	public void exportReceiveRecord(String couponKey, HttpServletResponse response) throws Exception {
		// 发行数据
		List<VpointsCouponReceiveRecord> couponRecordList = queryReceiveRecordList(null, couponKey);
		
		// 响应头
		response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        
		String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        OutputStream outStream = response.getOutputStream();
        
        bookName = "优惠券发行数据";
    	headers = new String[] {"用户主键", "领取人", "手机号", "省", "市", "县", "领券区域", "领取时间", "使用时间", "失效时间"};
    	
    	valueTags = new String[] {"userKey", "nickName", "phoneNumber", "province", "city", "county", "address", "receiveTime", "useTime", "expireTime"};

        response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
        ExcelUtil<VpointsCouponReceiveRecord> excel = new ExcelUtil<VpointsCouponReceiveRecord>(); 
        excel.writeExcel(bookName, headers, valueTags, couponRecordList, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();
	}
	
	/**
	 * 导出核销数据
	 * @param couponKey
	 * @param response
	 * @throws Exception
	 */
	public void exportVerificationRecord(VpointsExchangeLog queryBean, String couponKey, HttpServletResponse response) throws Exception {
		// 订单列表
        List<VpointsExchangeLog> resultList = exchangeService.queryForExpressLst(queryBean, null);
        for (VpointsExchangeLog item : resultList) {
            item.setExchangePayMoney(String.format("%.02f", Double.valueOf(item.getExchangePay()/100D)));
            item.setCouponDiscountPayMoney(String.format("%.02f", Double.valueOf(item.getCouponDiscountPay()/100D)));
        }
		
		// 响应头
		response.reset();
		response.setCharacterEncoding("GBK");
		response.setContentType("application/msexcel;charset=UTF-8");
		
		String bookName = "";
		String[] headers = null;
		String[] valueTags = null;
		OutputStream outStream = response.getOutputStream();
		
		bookName = "优惠券核销数据";
		headers = new String[] {"订单编号", "商品名称", "购买件数", "订单金额", "优惠金额", "订单积分", "优惠积分", "收件人", "联系电话", "收件地址", "订单时间", "订单状态"};
		
		valueTags = new String[] {"exchangeId", "goodsName", "exchangeNum", "exchangePayMoney", "couponDiscountPayMoney", "exchangeVpoints", "couponDiscountVpoints", "userName", "phoneNum", "address", "exchangeTime", "orderStatus"};
		
		response.setHeader("Content-disposition", "attachment; filename="+ URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xls");
		ExcelUtil<VpointsExchangeLog> excel = new ExcelUtil<VpointsExchangeLog>(); 
		excel.writeExcel(bookName, headers, valueTags, resultList, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
		outStream.close();
		response.flushBuffer();
	}


    /**
     *  优惠券过期提醒
     * @param goodsId
     */
    public void couponExpireRemind(int splitTableSuffix) {
        
        // 获取模板消息基本配置
        String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
        String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
        String templateId = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.APPLET_COUPON_RECEIVE_EXPIRE_REMIND);
        
        List<String> infoKeyLst = new ArrayList<>();
        List<VpointsCouponReceiveRecord> recordLst = null;
        Map<String, Object> map = new HashMap<>();
        map.put("splitTableSuffix", splitTableSuffix);
        while (true) {
            recordLst = couponReceiveRecordDao.queryExpireRemind(map);
            if (CollectionUtils.isEmpty(recordLst)) {
                break;
            } else {
                infoKeyLst = new ArrayList<>();
                for (VpointsCouponReceiveRecord item : recordLst) {
                    infoKeyLst.add(item.getInfoKey());
                    if (StringUtils.isNotBlank(item.getMemberOpenid())){
                        item.setPaOpenid(item.getMemberOpenid());
                    }
                    taskExecutor.execute(new CouponExpireRemindTempletMsg(appid, appsec, templateId, item));
                }
                
                // 更新发送标志 
                map.put("infoKeyLst", infoKeyLst);
                couponReceiveRecordDao.updateExpireRemindFlag(map);
            }
        }
    }

}

// 优惠券过期提醒模板消息
class CouponExpireRemindTempletMsg implements Runnable {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private String appid;
    private String appsec;
    private String templateId;
    private VpointsCouponReceiveRecord receiveRecord;
    
    public CouponExpireRemindTempletMsg(String appid, 
            String appsec, String templateId, VpointsCouponReceiveRecord receiveRecord) {
        this.appid = appid;
        this.appsec = appsec;
        this.templateId = templateId;
        this.receiveRecord = receiveRecord;
    }
    
    @Override
    public void run() {
        try {
            boolean  sendFlag = WechatUtil.sendAppletTemplateMsg(this.initMsg(), appid, appsec);
            log.error("优惠券过期提醒发送" + (sendFlag ? "成功" : "失败") +"，paOpenid:" + receiveRecord.getPaOpenid());
        } catch (Exception e) {
            log.error("优惠券过期提醒发送失败，paOpenid:" + receiveRecord.getPaOpenid()); 
            e.printStackTrace();
        }
    }
    
    private String initMsg() throws Exception {
        MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
        msg.setTouser(receiveRecord.getPaOpenid());
        msg.setTemplate_id(templateId);
        msg.setPage("pagesMall/getCoupon/myCoupon");
        if(DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM))) {
        	msg.setPage(msg.getPage() + "?projectFlag=" + 
        			DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG));
        }
        
        MsgAppletData data=new MsgAppletData();
        data.setThing6(new MsgValue(receiveRecord.getCouponName().length() > 20 ? receiveRecord.getCouponName().substring(0, 20) : receiveRecord.getCouponName(), null));
        data.setDate3(new MsgValue(receiveRecord.getReceiveTime().substring(0, 10), null));
        data.setDate4(new MsgValue(receiveRecord.getExpireTime().substring(0, 10), null));
        data.setThing5(new MsgValue("优惠券即将过期，请尽快使用！", null));
        msg.setData(data);
        return JSON.toJSONString(msg);
    }
}
