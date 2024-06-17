package com.dbt.vpointsshop.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsVerificationDetail;
import com.dbt.vpointsshop.bean.VpointsVerificationInfo;
import com.dbt.vpointsshop.dao.IVpointsVerificationInfoDao;

/**
 * 积分商场实物奖兑换核销表Service
 */
@Service
public class VpointsVerificationInfoService extends BaseService<VpointsVerificationInfo> {

	@Autowired
	private IVpointsVerificationInfoDao verificationInfoDao;
	@Autowired
	private ExchangeService exchangeService;
	@Autowired
	private VpointsVerificationDetailService verificationDetailService;
    
    /**
     * 获取列表
     */
    public List<VpointsVerificationInfo> queryForLst(VpointsVerificationInfo queryBean, PageOrderInfo pageInfo) {
        
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return verificationInfoDao.queryForLst(map);
    }

    /**
     * 获取列表记录总个数
     */
    public int queryForCount(VpointsVerificationInfo queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return verificationInfoDao.queryForCount(map);
    }
    
    /**
     * 依据核销记录主键获取记录
     */
    public VpointsVerificationInfo findById(String verificationId) {
        return verificationInfoDao.findById(verificationId);
    }

    /**
     * 获取指定状态的核销记录 
     * 
     * @param map keys:statusLst
     */
    public List<VpointsVerificationInfo> queryByStatus(String... statusAry) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusLst", statusAry);
        return verificationInfoDao.queryByStatus(map);
    }
    
    /**
     * 创建核实记录
     * @param verificationEndDate 核销结束时间
     * @param brandKeys	品牌id数组
     */
    public void addVerificationInfo(String verificationEndDate, String brandKeys, String optUserKey) throws BusinessException {
        if (StringUtils.isBlank(verificationEndDate)) {
            throw new BusinessException("操作失败，参数不完整！");
        }
        
        // 查询未确认或终止的核销记录
        List<VpointsVerificationInfo> verificationInfoLst
                        = queryByStatus(Constant.verificationStatus.STATUS_0);
        if(CollectionUtils.isNotEmpty(verificationInfoLst)) {
            throw new BusinessException("已存在未处理的核销记录:" + verificationInfoLst.get(0).getVerificationId());
        }
        
        // 获取要核销的已发货订单
        List<VpointsExchangeLog> exchangeLogLst = exchangeService.queryVerificationByDate(verificationEndDate, brandKeys);
        if (CollectionUtils.isEmpty(exchangeLogLst)) {
            throw new BusinessException("操作失败，截止" + verificationEndDate + "没有未核销的已发货订单");
        }
        
        // 整理核销记录及明细
        String verificationId = getBussionNo("vpointsVerification",
                            "verification_id", Constant.OrderNoType.type_HX); // 核销记录主键
        int goodsNum = 0; // 核销商品总数
        long totalExchangeVpoints = 0L; // 核销兑换总积分
        double totalDaiCaiMoney = 0D; // 核销总价格
        String verificationStartDate = verificationEndDate; // 核销开始日期
        Set<String> goodsClientNoSet = new HashSet<>(); // 商品种类
        VpointsVerificationDetail detailItem = null; // 核实明细
        List<VpointsVerificationDetail> detailLst = new ArrayList<>();
        for (VpointsExchangeLog item : exchangeLogLst) {
            
            // 核销记录
            goodsNum += item.getExchangeNum();
            totalExchangeVpoints += item.getExchangeVpoints();
            totalDaiCaiMoney += (item.getDaicaiMoney() * item.getExchangeNum());
            goodsClientNoSet.add(item.getGoodsClientNo());
            if (item.getExpressSendTime().compareTo(verificationStartDate) < 0) {
                verificationStartDate = item.getExpressSendTime();
            }
            
            // 核销明细
            detailItem = new VpointsVerificationDetail();
            detailItem.setInfoKey(UUID.randomUUID().toString());
            detailItem.setVerificationId(verificationId);
            detailItem.setExchangeId(item.getExchangeId());
            detailItem.fillFields(optUserKey);
            detailLst.add(detailItem);
        }
        
        //  核销记录主表
        VpointsVerificationInfo verificationInfo = new VpointsVerificationInfo();
        verificationInfo.setVerificationId(verificationId);
        verificationInfo.setGoodsTypeNum(goodsClientNoSet.size());
        verificationInfo.setGoodsNum(goodsNum);
        verificationInfo.setTotalVpoints(totalExchangeVpoints);
        verificationInfo.setTotalMoney(totalDaiCaiMoney);
        verificationInfo.setStartDate(verificationStartDate.substring(0, 10));
        verificationInfo.setEndDate(verificationEndDate);
        verificationInfo.setStatus(Constant.verificationStatus.STATUS_0);
        verificationInfo.fillFields(optUserKey);
        verificationInfoDao.create(verificationInfo);
        
        // 批量插入核销明细
        verificationDetailService.batchWrite(detailLst);

        // 更新兑换记录中核销记录主键
        for (VpointsExchangeLog item : exchangeLogLst) {
            exchangeService.updateVerificationId(item.getExchangeId(), verificationId);
        }
        
        // 操作日志
        logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_1,
                        JSON.toJSONString(verificationInfo), "新建核销记录" + verificationId);
    }

	/**
	 * 更改核销状态
	 */
	public void updateVerificationStatus(String verificationId, 
	                        String status, String optUserKey) throws BusinessException {
	    
	    if (StringUtils.isBlank(verificationId) || StringUtils.isBlank(status)) {
	        throw new BusinessException("操作失败，参数不完整！");
	    }
	    
	    Map<String, Object> map = new HashMap<>();
	    map.put("verificationId", verificationId);
	    map.put("status", status);
	    map.put("optUserKey", optUserKey);
	    verificationInfoDao.updateVerificationStatus(map);

        // 终止核销时清除兑换记录中核销记录主键
	    if (Constant.verificationStatus.STATUS_2.equals(status)) {
	        exchangeService.updateVerificationIdForClear(verificationId);
	    }
	    
	    StringBuffer logBuffer = new StringBuffer(verificationId);
	    if (Constant.verificationStatus.STATUS_1.equals(status)) {
	        logBuffer.append("确认核销");
	    } else if (Constant.verificationStatus.STATUS_2.equals(status)) {
	        logBuffer.append("终止核销");
	    }
	    logService.saveLog("shopping", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(map), logBuffer.toString());
	}

	/**
	 * 查询预览核销记录
	 * @param verificationEndDate 核销结束时间
	 * @param brandKeys 品牌数组
	 * @return
	 */
	public VpointsVerificationInfo findPreviewForVerificationInfo(String verificationEndDate, String brandKeys) {
		Map<String, Object> map = new HashMap<>();
		map.put("verificationEndDate", verificationEndDate);
		map.put("brandKeys", Arrays.asList(brandKeys.split(",")));
		return verificationInfoDao.findPreviewForVerificationInfo(map);
	}
}
