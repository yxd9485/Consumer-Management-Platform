package com.dbt.vpointsshop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.dbt.framework.util.PackRecordRouterUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.bean.VpointsCouponReceiveRecord;
import com.dbt.vpointsshop.bean.VpointsCouponReceiveStatistics;
import com.dbt.vpointsshop.bean.VpsOrderComment;
import com.dbt.vpointsshop.dao.IVpointsCouponCogDao;
import com.dbt.vpointsshop.dao.VpointsCouponDao;
import com.dbt.vpointsshop.dao.VpsOrderCommentDao;

@Service
public class VpsOrderCommentService extends BaseService<VpsOrderComment>{
	@Autowired
	private VpsOrderCommentDao orderCommentDao;
	@Autowired
	private IVpointsCouponCogDao couponCogDao;
	@Autowired
	private VpointsCouponReceiveRecordService couponReceiveRecordService;
	@Autowired
	private VpointsCouponDao couponDao;
	 @Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	public List<VpsOrderComment> queryForLst(VpsOrderComment queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		return orderCommentDao.queryForLst(queryMap);
	}

	public int queryForCount(VpsOrderComment queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return orderCommentDao.queryForCount(queryMap);
	}

	/**
	 * 查询评论
	 * @param commentId
	 */
	public VpsOrderComment findById(String commentId) {
		return orderCommentDao.findById(commentId);
	}
	
	/**
	 * 删除评论
	 * @param commentId
	 */
	public void del(String commentId) {
		orderCommentDao.deleteById(commentId);		
	}

	/**
	 * 审核评论
	 * @param bean
	 */
	public void update(VpsOrderComment bean) {
		orderCommentDao.update(bean);
		if (StringUtils.isNotBlank(bean.getCouponKey())) {
			// 获取优惠券配置
			VpointsCouponCog couponCog = couponCogDao.findByCouponCog(bean.getCouponKey());
			// 查询优惠券情况表
			VpointsCouponReceiveStatistics couponReceiveStatistics = queryByCouponStatistics(bean.getUserKey(),
					bean.getCouponKey());
			if (couponReceiveStatistics != null) {
				//需要给该用户加上数量
				couponDao.updateReceiveStatisticsNum(couponReceiveStatistics.getInfoKey());
			} else {
				//需要新增
				addReceiveStatistics(bean.getUserKey(), bean.getCouponKey());
			}
			//更新优惠券配置表
			couponCogDao.updateReceiveNum(bean.getCouponKey());
			//增加记录
			couponReceiveRecordService.addReceiveRecord(bean.getUserKey(), couponCog);
			
			if(StringUtils.isNotBlank(bean.getPaOpenid())){
				// 获取模板消息基本配置
				String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
				String appsec = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, DatadicKey.filterWxPayTemplateInfo.PAAPPLET_SECRET);
				taskExecutor.execute(new SendCouponTempletMsg(appid, appsec, bean, couponCog));				
			}
		}
	}	

	public VpointsCouponReceiveStatistics queryByCouponStatistics(String userKey, String couponKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userKey", userKey);
		map.put("couponKey", couponKey);
		return couponDao.queryByCouponStatistics(map);
	}
	
	
	 /**
     * 增加用户优惠券领取情况
     * @param userKey
     * @param couponKey
     * @return
     */
    public void addReceiveStatistics(String userKey, String couponKey) {
        VpointsCouponReceiveStatistics statistics = new VpointsCouponReceiveStatistics();
        statistics.setInfoKey(UUID.randomUUID().toString());
        statistics.setUserKey(userKey);
        statistics.setCouponKey(couponKey);
        statistics.setDayNum(1);
        statistics.setWeekNum(1);
        statistics.setMonthNum(1);
        statistics.setTotalNum(1);
        statistics.setLastReceiveTime(DateUtil.getDateTime());
        couponDao.addReceiveStatistics(statistics);
    }


	public List<VpsOrderComment> queryCommentImg(String expireTime) {
		return orderCommentDao.queryCommentImg(expireTime);
	}

	public void updateImg(VpsOrderComment c) {
		orderCommentDao.updateImg(c);
	}
}

// 优惠券过期提醒模板消息
class SendCouponTempletMsg implements Runnable {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String appid;
	private String appsec;
	private VpsOrderComment vpsOrderComment;
	private VpointsCouponCog couponCog;

	public SendCouponTempletMsg(String appid, String appsec, VpsOrderComment vpsOrderComment,
			VpointsCouponCog couponCog) {
		this.appid = appid;
		this.appsec = appsec;
		this.vpsOrderComment = vpsOrderComment;
		this.couponCog = couponCog;
	}

	@Override
	public void run() {
		try {
			boolean sendFlag = WechatUtil.sendAppletTemplateMsg(this.initMsg(), appid, appsec);
			log.error("优惠券发送" + (sendFlag ? "成功" : "失败") + "，openid:" + vpsOrderComment.getOpenId());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("优惠券发送失败，openid:" + vpsOrderComment.getOpenId());
		}
	}

	private String initMsg() throws Exception {
		MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
		msg.setTouser(vpsOrderComment.getPaOpenid());
		msg.setTemplate_id(DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                DatadicKey.filterWxPayTemplateInfo.APPLET_COUPON_RECEIVE_PROVIDE_REMIND));
		if(StringUtils.isBlank(msg.getTemplate_id())) {
			msg.setTemplate_id("vkIzFfUtci5hW8BLuX4rzEskOw03rizg5TVtt8Ce8Ik");
		}
		
		if(DatadicUtil.isSwitchON(DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM))) {
        	msg.setPage("pagesMall/getCoupon/myCoupon?projectFlag=" + 
        			DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG));
        }else {
        	if ("henanpz".equals(DbContextHolder.getDBType())) {
    		    msg.setPage("pagesMall/getCoupon/myCoupon");
    		} else {
    		    msg.setPage("pages/getCoupon/myCoupon");
    		}
        }
		
		MsgAppletData data = new MsgAppletData();
		data.setThing1(new MsgValue(vpsOrderComment.getCouponName().length() > 20
				? vpsOrderComment.getCouponName().substring(0, 20) : vpsOrderComment.getCouponName(), null));
		data.setTime2(new MsgValue(DateUtil.getDateTime().substring(0, 10), null));
		data.setTime3(new MsgValue(couponCog.getStartDate().substring(0, 10)+"~"+couponCog.getEndDate().substring(0, 10), null));
		data.setThing4(new MsgValue("赶快来领取！", null));
		msg.setData(data);
		return JSON.toJSONString(msg);
	}
}
