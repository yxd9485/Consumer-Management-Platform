package com.dbt.smallticket.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.json.reply.BaseReplyResult;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CheckUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.HttpsUtil;
import com.dbt.framework.util.MailUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.framework.wechat.message.bean.MsgAppletBean;
import com.dbt.framework.wechat.message.bean.MsgAppletData;
import com.dbt.framework.wechat.message.bean.MsgValue;
import com.dbt.platform.activity.bean.VcodePrizeBasicInfo;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.appuser.bean.VpsConsumerThirdAccountInfo;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.prize.service.MajorInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.wctaccesstoken.bean.WechatCheckUserTemplateMsg;
import com.dbt.smallticket.bean.VpsTicketDayExcelTemplet;
import com.dbt.smallticket.bean.VpsTicketLotteryRecord;
import com.dbt.smallticket.bean.VpsTicketRecord;
import com.dbt.smallticket.bean.VpsTicketRecordSkuDetail;
import com.dbt.smallticket.bean.VpsTicketTerminalCog;
import com.dbt.smallticket.bean.VpsTicketTerminalInsideDetail;
import com.dbt.smallticket.dao.IVpsTicketLotteryRecordDao;
import com.dbt.smallticket.dao.VpsTicketRecordDao;

@Service
public class VpsTicketRecordService extends BaseService<VpsTicketRecord>{
	@Autowired
	private VpsTicketRecordDao ticketRecordDao;
	@Autowired
	private VpsTicketTerminalInsideDetailService insideDetailService;
	@Autowired
	private VpsTicketRecordSkuDetailService skuDetailService;
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	@Autowired
	private IVpsTicketLotteryRecordDao lotteryRecordDao;
	@Autowired
	private VpsConsumerUserInfoService vpsConsumerUserInfoService;
	@Autowired
	private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
	@Autowired
	private MajorInfoService majorInfoService;
	
	private static final String[] HEARDS = { "用户主键", "奖品类型", "联系人", "手机号码", "配送地址", "中奖区域", "中奖时间", "领取时间" };
	private static final  String[] VALUETAGE = { "userKey", "prizeName", "userName", "phoneNum", "address", "prizeAddress","earnTime", "useTime" };

	public List<VpsTicketRecord> queryForLst(VpsTicketRecord queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		return ticketRecordDao.queryForLst(queryMap);
	}

	public int queryForCount(VpsTicketRecord queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return ticketRecordDao.queryForCount(queryMap);
	}

	public VpsTicketRecord findById(String infoKey) {
		return ticketRecordDao.findById(infoKey);
	}
	


	/**
	 * 查询门店名称以及流水号
	 * @param detail
	 * @return
	 */
	public String checkTicket(VpsTicketRecord detail) {
		return ticketRecordDao.checkTicket(detail);
	}
	
	public void initRecordSkuDetailLst(VpsTicketRecord vpsTicketRecord) {
		VpsTicketRecordSkuDetail item ;
		List<VpsTicketRecordSkuDetail> itemLst = new ArrayList<>();
		// 不驳回时
		if (!"1".equals(vpsTicketRecord.getTicketStatus())) {
		    if(vpsTicketRecord.getTerminalInsideCode() != null){
		        int itemNum =  vpsTicketRecord.getTerminalInsideCode().length;
		        for (int i = 0; i < itemNum; i++) {
		            item = new VpsTicketRecordSkuDetail();
		            item.setTerminalInsideCode(vpsTicketRecord.getTerminalInsideCode()[i]);
		            item.setTicketSkuName(vpsTicketRecord.getTicketSkuName()[i]);
		            item.setSkuKey(vpsTicketRecord.getSkuKey()[i]);
		            item.setSkuNum(StringUtils.isBlank(vpsTicketRecord.getSkuNum()[i])?"0":vpsTicketRecord.getSkuNum()[i]);
		            item.setTicketSkuUnitMoney(StringUtils.isBlank(vpsTicketRecord.getTicketSkuUnitMoney()[i])?"0":vpsTicketRecord.getTicketSkuUnitMoney()[i]);
		            item.setTicketSkuMoney(StringUtils.isBlank(vpsTicketRecord.getTicketSkuMoney()[i])?"0":vpsTicketRecord.getTicketSkuMoney()[i]);
		            item.setTicketRecordKey(vpsTicketRecord.getInfoKey());
		            itemLst.add(item);
		        }
		    }
		}
		vpsTicketRecord.setDetailLst(itemLst);
	}


	
	/**
	 * 审核小票
	 * @param vpsTicketRecord
	 * @param user
	 * @throws Exception 
	 */
	public void updateicketRecord(VpsTicketRecord vpsTicketRecord, SysUserBasis user) throws Exception {

        // 扫码开关
        if (DatadicUtil.isSwitchOFF(DatadicUtil.dataDicCategory
                .DATA_CONSTANT_CONFIG, DatadicKey.dataConstantConfig.SWEEP_SWITCH)) {
            throw new BusinessException("系统升级中-合同余额不足");
        }
        
		if (!vpsTicketRecord.getProjressName().equals(user.getUserName())) {
			throw new BusinessException("该小票已被" + vpsTicketRecord.getProjressName() + "审核中！");
		}
		if (!"1".equals(vpsTicketRecord.getTicketStatus())
		        && CollectionUtils.isEmpty(vpsTicketRecord.getDetailLst())) {
			throw new BusinessException("商品明细不存在");
		}

        vpsTicketRecord.setSearchType("2");
        if (!"0".equals(vpsTicketRecord.getTicketStatus()) && !"1".equals(vpsTicketRecord.getTicketStatus())
                && Integer.valueOf(StringUtils.defaultIfBlank(ticketRecordDao.checkTicket(vpsTicketRecord), "0")) > 0) {
            throw new BusinessException("流水号重复");
        }
		vpsTicketRecord.setInput(user);
        vpsTicketRecord.fillUpdateFields(user.getUserKey());
        //如果审核过需要判断店内码逻辑以及修改小票详情
		if ("2".equals(vpsTicketRecord.getTicketStatus())) {
			//新增门店
	        if(StringUtils.isBlank(vpsTicketRecord.getTerminalKey())){
	        	VpsTicketTerminalCog ticketTerminalCog = new VpsTicketTerminalCog(vpsTicketRecord);
	        	ticketTerminalCog.fillFields(user.getUserKey());
	        	ticketRecordDao.createTerminal(ticketTerminalCog);
	        	vpsTicketRecord.setTerminalKey(ticketTerminalCog.getTerminalKey());
	        }
	        boolean flag = vpsTicketRecord.getSkuMoney() > 0 ? false : true;
			// 更新插入店内码
			List<VpsTicketTerminalInsideDetail> insertDetailLst = new ArrayList<>();
			List<VpsTicketTerminalInsideDetail> updateDetailLst = new ArrayList<>();
			List<String> oldDetailKeys = insideDetailService.queryInsideDetailByInalnsideCode(vpsTicketRecord);
			//如果店内码存在则更新 否则新增
			double skuMoney = 0;
			for (VpsTicketRecordSkuDetail skuDetail : vpsTicketRecord.getDetailLst()) {
				if (!StringUtils.isEmpty(skuDetail.getTerminalInsideCode())) {
					if (CollectionUtils.isNotEmpty(oldDetailKeys) && oldDetailKeys.contains(skuDetail.getTerminalInsideCode())) {
						updateDetailLst.add(new VpsTicketTerminalInsideDetail(skuDetail, vpsTicketRecord, false));
					} else {
						insertDetailLst.add(new VpsTicketTerminalInsideDetail(skuDetail, vpsTicketRecord, true));
					}
				}
				//增加SKU实际价格
				if (flag) {
					String suggestPrice = skuInfoService.findById(skuDetail.getSkuKey()).getSuggestPrice();
					if (!StringUtils.isEmpty(suggestPrice)) {
						skuMoney = MathUtil.add(skuMoney,MathUtil.mul(Double.valueOf(suggestPrice), Double.valueOf(skuDetail.getSkuNum())));
					}
					vpsTicketRecord.setSkuMoney(skuMoney);
				}
			}
			
        	if(CollectionUtils.isNotEmpty(insertDetailLst)){
        		insideDetailService.addBatch(insertDetailLst);
        	}
        	if(CollectionUtils.isNotEmpty(updateDetailLst)){
        	  	insideDetailService.updateBatch(updateDetailLst);
        	}
        	updateChannelNum(vpsTicketRecord);
        }
		
		// 审核通过或送审时
		if ("2".equals(vpsTicketRecord.getTicketStatus()) || "3".equals(vpsTicketRecord.getTicketStatus())) {
            // 更新小票查询店内体系码适用省份
            if (StringUtils.isNotBlank(vpsTicketRecord.getProvince()) && StringUtils.isNoneBlank(vpsTicketRecord.getInsideCodeType())) {
                Map<String, Object> map = new HashMap<>();
                map.put("insideCodeType", vpsTicketRecord.getInsideCodeType());
                map.put("province", vpsTicketRecord.getProvince());
                ticketRecordDao.updateTerminalInsideProvince(map);
            }
		}
		
		// 驳回时默认不给促销员返利
		if ("1".equals(vpsTicketRecord.getTicketStatus())) {
		    vpsTicketRecord.setPromotionEarnFlag("0");
		} else {
			//更新小票明细以及店内码
			skuDetailService.deleteTicketSkuDetailByRecordKey(vpsTicketRecord.getInfoKey());
			skuDetailService.insertTicketSkuDetail(vpsTicketRecord);			
		}
        
        //修改小票状态
        ticketRecordDao.update(vpsTicketRecord);
        
		//小程序审核结果推送
		if ("1".equals(vpsTicketRecord.getTicketStatus()) || "2".equals(vpsTicketRecord.getTicketStatus())) {
			VpsConsumerThirdAccountInfo accountInfo = vpsConsumerUserInfoService
					.queryOpenidByUserkey(vpsTicketRecord.getUserKey());
			if (!CheckUtil.isEmpty(accountInfo) && !StringUtils.isEmpty(accountInfo.getPaOpenid())) {
				vpsTicketRecord.setOpenid(accountInfo.getPaOpenid());
				taskExecutor.execute(new SendTicketTempletMsg(
						DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
								DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID),vpsTicketRecord));
			}

	        // 未审核小票减1
	        RedisApiUtil.getInstance().incrBy(RedisApiUtil.CacheKey.ticket.TICKET_UNCHECKED_NUM, -1);
		}
		//AI助手送审推送
		if ("3".equals(vpsTicketRecord.getTicketStatus())) {
		    this.sendTicketAudit(vpsTicketRecord.getInfoKey(), true);
		}
	}
	
	/**
	 * 发放推广激励
	 * 
	 * @param ticketInfoKey
	 * @param user
	 * @throws IOException
	 */
	public void executePromotionMoney(String ticketInfoKey, SysUserBasis user) throws IOException {
	    VpsTicketRecord ticketRecord = this.findById(ticketInfoKey);
        if (StringUtils.isNotBlank(ticketRecord.getPromotionUserKey()) 
                && "2".equals(ticketRecord.getTicketStatus()) && "1".equals(ticketRecord.getPromotionEarnFlag())) {
            Map<String, Object> argsMap = new HashMap<>();
            argsMap.put("projectServerName", DbContextHolder.getDBType());
            argsMap.put("ticketRecordKey", ticketRecord.getInfoKey());
            argsMap.put("optUserKey", user.getUserKey());
            String interfaceUrl = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_HTTP_URL,
                    DatadicKey.filterHttpUrl.PROJECT_INTERFACE_URL) + "/vjifenInterface/promotionUser/promotionMoney";
            JSONObject revokeJson = HttpsUtil.doPostForJson(interfaceUrl, JSON.toJSONString(argsMap));
            if(null == revokeJson){
                throw new BusinessException("远程服务器无法连接！");
            } else {
                BaseReplyResult baseResult = revokeJson.getObject("result", BaseReplyResult.class);
                if (baseResult != null) {
                    if (!ResultCode.SUCCESS.equals(baseResult.getCode())
                            || !ResultCode.SUCCESS.equals(baseResult.getBusinessCode())) {
                        throw new BusinessException(StringUtils.defaultIfBlank(baseResult.getMsg(), "发放促销激励失败"));
                    }
                } else {
                    throw new BusinessException("远程服务器无法连接！");
                }
            }
        }
	}
	
	/**
	 * 送审小票
	 * @param ticketKey
	 * @param firstSend
	 */
	public void sendTicketAudit(String ticketKey, boolean firstSend) {
	    VpsTicketRecord ticketRecord = ticketRecordDao.findById(ticketKey);
	    if (ticketRecord != null) {
	        List<String> openidArray = ticketRecordDao.findCheckUserByOpenid(ticketRecord.getWarAreaName(),ticketRecord.getTicketChannel());
	        String appid = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
	                DatadicKey.filterWxPayTemplateInfo.AI_APPID);
	        if(CollectionUtils.isEmpty(openidArray)){
	             openidArray = ticketRecordDao.findCheckUserByOpenid(null,ticketRecord.getTicketChannel());
	        }
	        if (CollectionUtils.isNotEmpty(openidArray) && StringUtils.isNotBlank(appid)) {
	            String url = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
                        DatadicKey.filterWxPayTemplateInfo.WECHAT_H5_DOMAIN) + "zlchangchenghui/to/verify?infoKey=";
	            for (String openid : openidArray) {
	                taskExecutor.execute(new WechatCheckUserTemplateMsg(appid, openid,WechatCheckUserTemplateMsg.msgType.SEND_USER)
	                        .initMsg("待审核通知", "V积分-" + ticketRecord.getInputUserName(), ticketRecord.getSubmintCheckReason() + "\r\n地区:" 
	                                + ticketRecord.getProvince() + ticketRecord.getCity() + ticketRecord.getCounty(), url + ticketRecord.getInfoKey(), firstSend));
	            }
	        }
	    }
	    
	}
	
	
	/**
     * 更新用户定时抽奖的参与情况
     * 
     * @param userKey   用户主键
     * @param channelType     更新渠道类型
     * 
     * @return 返回参与定时抽奖的日期
     */
    public String updateChannelNum(VpsTicketRecord vpsTicketRecord) {
        
		String channelType = "1".equals(vpsTicketRecord.getTicketChannel()) ? Constant.ticketLotteryChannelType.type1
				: Constant.ticketLotteryChannelType.type2;
		// 默认参与当前抽奖，20:00及以后参与第二天定时抽奖
        String lotteryDate = DateUtil.getDate();
        if ("20:00".compareTo(DateUtil.getDateTime("HH:mm")) <= 0) {
            lotteryDate = DateUtil.getDateTime(DateUtil.addDays(1), DateUtil.DEFAULT_DATE_FORMAT);
        }
        
        // 指定活动中奖概率+1
        Map<String, Object> map = new HashMap<>();
        map.put("userKey", vpsTicketRecord.getUserKey());
        map.put("lotteryDate", lotteryDate);
        map.put("channelType", channelType);
        int num = lotteryRecordDao.updateChannelNum(map);
        
        // 更新影响记录为0时
        if (num == 0) {
            VpsTicketLotteryRecord lotteryRecord = new VpsTicketLotteryRecord();
            lotteryRecord.setInfoKey(UUID.randomUUID().toString());
            lotteryRecord.setUserKey(vpsTicketRecord.getUserKey());
            lotteryRecord.setLotteryDate(lotteryDate);
            lotteryRecord.setProvince(vpsTicketRecord.getProvince());
            lotteryRecord.setCity(vpsTicketRecord.getCity());
            lotteryRecord.setCounty(vpsTicketRecord.getCounty());
            lotteryRecord.setChannelNum0(Constant.ticketLotteryChannelType.type0.equals(channelType) ? 1 : 0);
            lotteryRecord.setChannelNum1(Constant.ticketLotteryChannelType.type1.equals(channelType) ? 1 : 0);
            lotteryRecord.setChannelNum2(Constant.ticketLotteryChannelType.type2.equals(channelType) ? 1 : 0);
            lotteryRecord.setChannelNum3(Constant.ticketLotteryChannelType.type3.equals(channelType) ? 1 : 0);
            lotteryRecord.setShareNum(Constant.ticketLotteryChannelType.type4.equals(channelType) ? 1 : 0);
            lotteryRecord.setSubscriptionNum(Constant.ticketLotteryChannelType.type5.equals(channelType) ? 1 : 0);
            lotteryRecordDao.create(lotteryRecord);
        }
        
        return lotteryDate;
    }

	
	/**
	 * 推送过期未处理消息
	 * @throws InterruptedException 
	 */
	public void sendTicketRecordExpireCheckUser() throws InterruptedException {
		List<VpsTicketRecord> expireLst = ticketRecordDao.findExpireTicketRecord();
		if (CollectionUtils.isNotEmpty(expireLst)) {
			for (VpsTicketRecord expireRecord : expireLst) {
				List<String> openidArray = ticketRecordDao.findCheckUserByOpenid(expireRecord.getWarAreaName(),expireRecord.getTicketChannel());
				if(CollectionUtils.isEmpty(openidArray)){
					 openidArray = ticketRecordDao.findCheckUserByOpenid(null,expireRecord.getTicketChannel());
				}
				if (CollectionUtils.isNotEmpty(openidArray)) {
					for (String openid : openidArray) {
						String url = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
								DatadicKey.filterWxPayTemplateInfo.WECHAT_H5_DOMAIN)
								+ "zlchangchenghui/to/verify?infoKey=" + expireRecord.getInfoKey();
						taskExecutor.execute(new WechatCheckUserTemplateMsg(
								DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO,
										DatadicKey.filterWxPayTemplateInfo.AI_APPID),
								openid, WechatCheckUserTemplateMsg.msgType.SEND_USER)
										.initMsg("今日17点前还未审核小票来啦~", "V积分-" + expireRecord.getInputUserName(),
												expireRecord.getSubmintCheckReason() + "\r\n地区:"
														+ expireRecord.getProvince() + expireRecord.getCity()
														+ expireRecord.getCounty(),url, false));
					}
				}
			}
		}
	}

	/**
	 * 推送邮件
	 * @throws Exception 
	 */
	public void sendTicketRecordEmailExcel(List<VpsTicketDayExcelTemplet> jiuHaoResultLst) throws Exception {
	    
	    // 获取每日报表数据
        List<VpsTicketDayExcelTemplet> result = ticketRecordDao.findTicketDayExcel();
        
        // 合并长城玖号服务员日报数据
        for (VpsTicketDayExcelTemplet item : result) {
            for (VpsTicketDayExcelTemplet jhItem : jiuHaoResultLst) {
                if (item.getBigAreaName().equals(jhItem.getBigAreaName()) && item.getWarAreaName().equals(jhItem.getWarAreaName())) {
                    item.setDaySaleNum1(item.getDaySaleNum1() + jhItem.getDaySaleNum1());
                    item.setDaySaleNum(item.getDaySaleNum() + jhItem.getDaySaleNum());
                    item.setTotalSaleNum(item.getTotalSaleNum() + jhItem.getTotalSaleNum());
                    item.setDayPersonNum1(item.getDayPersonNum1() + jhItem.getDayPersonNum1());
                    item.setTotalPersonNum(item.getTotalPersonNum() + jhItem.getTotalPersonNum());
                    item.setDaySaleMoney1(item.getDaySaleMoney1() + jhItem.getDaySaleMoney1());
                    item.setDaySaleMoney(item.getDaySaleMoney() + jhItem.getDaySaleMoney());
                    item.setTotalSaleMoney(item.getTotalSaleMoney() + jhItem.getTotalSaleMoney());
                    break;
                }
            }
        }
        
        // 报表日期
        String reportDate = DateUtil.getDateTime(DateUtil.addDays(-1), DateUtil.DEFAULT_DATE_FORMAT);
        
        // 组建各战区数据、0酒行渠道、1餐饮渠道、2KA渠道、3电商渠道
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("reportYear", reportDate.substring(0, 4));
        dataMap.put("reportDate", reportDate);
        
        int i = 0;
        for (VpsTicketDayExcelTemplet item : result) {
            // 战区各渠道、每天、累计
            dataMap.put(item.getWarAreaName() + "daySaleNum1", item.getDaySaleNum1());
            dataMap.put(item.getWarAreaName() + "daySaleNum2", item.getDaySaleNum2());
            dataMap.put(item.getWarAreaName() + "daySaleNum", item.getDaySaleNum());
            dataMap.put(item.getWarAreaName() + "totalSaleNum", item.getTotalSaleNum());
            dataMap.put(item.getWarAreaName() + "dayPersonNum0", item.getDayPersonNum0());
            dataMap.put(item.getWarAreaName() + "dayPersonNum1", item.getDayPersonNum1());
            dataMap.put(item.getWarAreaName() + "dayPersonNum2", item.getDayPersonNum2());
            dataMap.put(item.getWarAreaName() + "dayPersonNum3", item.getDayPersonNum3());
            dataMap.put(item.getWarAreaName() + "totalPersonNum", item.getTotalPersonNum());
            dataMap.put(item.getWarAreaName() + "daySaleMoney0", item.getDaySaleMoney0());
            dataMap.put(item.getWarAreaName() + "daySaleMoney1", item.getDaySaleMoney1());
            dataMap.put(item.getWarAreaName() + "daySaleMoney2", item.getDaySaleMoney2());
            dataMap.put(item.getWarAreaName() + "daySaleMoney3", item.getDaySaleMoney3());
            dataMap.put(item.getWarAreaName() + "daySaleMoney", item.getDaySaleMoney());
            dataMap.put(item.getWarAreaName() + "totalSaleMoney", item.getTotalSaleMoney());
            
            // 每日Top5
            if (i < 5) {
                i++;
                dataMap.put("dayTopAreaName" + i, item.getWarAreaName());
                dataMap.put("dayTopSaleNum" + i, item.getDaySaleNum());
                dataMap.put("dayTopSaleMoney" + i, item.getDaySaleMoney());
            }
        }
        
        // 累计TOP5
        Collections.sort(result);
        i = 0;
        for (VpsTicketDayExcelTemplet item : result) {
            if (i >= 5) break;
            i++;
            dataMap.put("totalTopAreaName" + i, item.getWarAreaName());
            dataMap.put("totalTopSaleNum" + i, item.getTotalSaleNum());
            dataMap.put("totalTopSaleMoney" + i, item.getTotalSaleMoney());
        }

        // 查询日报明细
        List<VpsTicketDayExcelTemplet> detailLst = ticketRecordDao.findTicketDayExcelDetail();
        
        // 生成Excel
//        String tempReport = "/data/upload/cogFolder/zhongLCNY/长城双节消费者活动数据报表-V积分.xlsx";
//        String reportFile = "/data/upload/cogFolder/zhongLCNY/report/长城双节消费者活动数据报表-V积分" + DateUtil.getDate("yyyyMMdd")+ ".xlsx";
        String tempReport = "/data/upload/cogFolder/zhongLCNY/中粮长城CNY消费者活动数据报表-V积分.xlsx";
        String reportFile = "/data/upload/cogFolder/zhongLCNY/report/中粮长城CNY消费者活动数据报表-V积分" + DateUtil.getDate("yyyyMMdd")+ ".xlsx";
        ExcelWriter excelWriter = EasyExcel.write(reportFile).withTemplate(tempReport).build();
        excelWriter.writeContext().writeWorkbookHolder().getWorkbook().setForceFormulaRecalculation(true);
        excelWriter.fill(dataMap, EasyExcel.writerSheet(0).build());
        excelWriter.fill(dataMap, EasyExcel.writerSheet(1).build());
        excelWriter.fill(detailLst, EasyExcel.writerSheet(1).build()).finish();
        
		// 发送邮件
		InputStream stream = new FileInputStream(new File(reportFile));
		MailUtil.sendMail(stream,
				DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.TICKET_COG,
						DatadicKey.ticketCog.TICKET_SEND_DAILYREPORT_MAIL),
				DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.TICKET_COG,
						DatadicKey.ticketCog.TICKET_CC_DAILYREPORT_MAIL),true);
	}
	
	/**
	 * 推送大奖邮件
	 * @throws Exception
	 * TODO 
	 */
	public void sendTicketPrizeEmailExcel() throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		OutputStream outStream = null;
		ExcelUtil<MajorInfo> excel = new ExcelUtil<MajorInfo>();
		for (Map.Entry<String, VcodePrizeBasicInfo> entry : vcodeActivityBigPrizeService.getPrizeMap().entrySet()) {
			List<MajorInfo> result = majorInfoService.queryPrizeTypeLst(entry.getKey(), true);
			String prizeName = entry.getValue().getPrizeName().replace("*", "X");
			excel.writeExcel(workbook, prizeName, prizeName, HEARDS, VALUETAGE, result, DateUtil.DEFAULT_DATE_FORMAT,
					outStream);
		}
		// 发送邮件
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		MailUtil.sendMail(new ByteArrayInputStream(bos.toByteArray()), DatadicUtil.getDataDicValue(
				DatadicKey.dataDicCategory.TICKET_COG, DatadicKey.ticketCog.TICKET_SEND_PRIZE_MAIL), null, false);
	}
}

//审核结果模板消息
class SendTicketTempletMsg implements Runnable {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String appid;
	private VpsTicketRecord vpsTicketRecord;

	public SendTicketTempletMsg(String appid, VpsTicketRecord vpsTicketRecord) {
		this.appid = appid;
		this.vpsTicketRecord = vpsTicketRecord;
	}

	@Override
	public void run() {
		try {
			boolean sendFlag = WechatUtil.sendAppletTemplateMsg(this.initMsg(), appid, null);
			log.error("审核结果发送" + (sendFlag ? "成功" : "失败") + "，openid:" + vpsTicketRecord.getOpenid());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("审核结果发送失败，openid:" + vpsTicketRecord.getOpenid());
		}
	}

	private String initMsg() throws Exception {
		String ticketStatus = vpsTicketRecord.getTicketStatus();
		String ticketChannel = vpsTicketRecord.getTicketChannel();
		MsgAppletBean msg = new MsgAppletBean(PropertiesUtil.getPropertyValue("run_env"));
		msg.setTouser(vpsTicketRecord.getOpenid());
//		msg.setTemplate_id("SKWYnXiINwsj09BCv5KpOycULQPcJ2t1xFZr2zmVAeQ");
		msg.setTemplate_id("bkNgVG73lQ-WSeypwzMYl7mwd1ssmmJ-rX-L8Zzsh90"); //双节小程序
		// 如果审核通过就跳到抽奖页 否则跳转到我的小票列表 
		msg.setPage("1".equals(ticketStatus) ? "pages/center/mycard?source=approvalTemplate" : "pages/activity/activity?type=verifySuc");
		MsgAppletData data = new MsgAppletData();
		data.setThing6(new MsgValue("招募长城好酒体验官", null));
		data.setThing1(new MsgValue("1".equals(ticketChannel) ? "餐饮渠道" : "KA渠道", null));
		data.setThing4(new MsgValue("1".equals(ticketStatus) ? "审核不通过" : "审核通过", null));
		data.setThing12(new MsgValue("1".equals(ticketStatus) ? "驳回原因:"+ vpsTicketRecord.getDismissReason().split(",")[0] : "恭喜您审核通过,获得一次机会,点击参与", "#FF0000"));
		msg.setData(data);
		return JSON.toJSONString(msg);
	}
}
