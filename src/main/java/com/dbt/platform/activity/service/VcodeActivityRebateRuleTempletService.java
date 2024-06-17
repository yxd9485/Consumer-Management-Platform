package com.dbt.platform.activity.service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleTemplet;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleTempletDetail;
import com.dbt.platform.activity.dao.IVcodeActivityRebateRuleTempletDao;
import com.dbt.platform.activity.dao.IVcodeActivityRebateRuleTempletDetailDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 活动规则模板主表Service
 */
@Service
public class VcodeActivityRebateRuleTempletService extends BaseService<VcodeActivityRebateRuleTemplet> {

	@Autowired
	private IVcodeActivityRebateRuleTempletDao ruleTempletDao;
	@Autowired
	private IVcodeActivityRebateRuleTempletDetailDao templetDetailDao;
	
	private static final String S4RNWPS4SK = "S4RnWpS4sk";
    
    /**
     * 获取列表
     */
    public List<VcodeActivityRebateRuleTemplet> queryForLst(VcodeActivityRebateRuleTemplet queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return ruleTempletDao.queryForLst(map);
    }

    /**
     * 获取列表记录总个数
     */
    public int queryForCount(VcodeActivityRebateRuleTemplet queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return ruleTempletDao.queryForCount(map);
    }

	/**
	 * 根据id查询实体对象
	 */
	public VcodeActivityRebateRuleTemplet findById(String infoKey) {
	    
	    // 获取规则模板
	    VcodeActivityRebateRuleTemplet ruleTemplet = ruleTempletDao.findById(infoKey);
	    ruleTemplet.setFirstDetailLst(new ArrayList<VcodeActivityRebateRuleTempletDetail>());
        ruleTemplet.setCommonDetailLst(new ArrayList<VcodeActivityRebateRuleTempletDetail>());
        
        // 获取奖项配置项详情
        Map<String, Object> map = new HashMap<>();
        map.put("templetKey", ruleTemplet.getInfoKey());
        List<VcodeActivityRebateRuleTempletDetail> templetDetailLst = templetDetailDao.queryByTempletKey(map);
        for (VcodeActivityRebateRuleTempletDetail item : templetDetailLst) {
            if (Constant.ScanType.type_0.equals(item.getScanType())) {
                ruleTemplet.getFirstDetailLst().add(item);
            } else if (Constant.ScanType.type_1.equals(item.getScanType())) {
                ruleTemplet.getCommonDetailLst().add(item);
            }
        }
        
        // 如果未配置首扫，则初始化一个默认奖项配置项
        if (CollectionUtils.isEmpty(ruleTemplet.getFirstDetailLst())) {
            VcodeActivityRebateRuleTempletDetail emptyDetail = new VcodeActivityRebateRuleTempletDetail();
            emptyDetail.setScanType(Constant.ScanType.type_0);
            emptyDetail.setRandomType(Constant.PrizeRandomType.type_0);
            emptyDetail.setMinMoney("0.00");
            emptyDetail.setMaxMoney("0.00");
            emptyDetail.setMinVpoints("0");
            emptyDetail.setMaxVpoints("0");
            emptyDetail.setPrizePercent("0");
            ruleTemplet.getFirstDetailLst().add(emptyDetail);
        }
	    
		return ruleTemplet;
	}

	/**
	 * 新增返利规则模板
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
    public void addRebateRuleTemplet(VcodeActivityRebateRuleTemplet ruleTemplet) throws Exception {
        
        // 校验名称是否重复
        if ("1".equals(checkBussionName("rebateRuleTemplet", "INFO_KEY", 
                    null, "TEMPLET_NAME", ruleTemplet.getTempletName()))) {
            throw new BusinessException("规则模板名称已存在");
        }
	    
	    // 保存模板主表
	    String templetKey = UUID.randomUUID().toString();
	    ruleTemplet.setInfoKey(templetKey);
	    ruleTemplet.setUnitMoney(ruleTemplet.getUnitMoney().replace("≈", ""));
	    ruleTemplet.setUnitVpoints(ruleTemplet.getUnitVpoints().replace("≈", ""));
	    ruleTemplet.setAllowanceaMinMoney(StringUtils.defaultIfBlank(ruleTemplet.getAllowanceaMinMoney(), null));
	    ruleTemplet.setAllowanceaMaxMoney(StringUtils.defaultIfBlank(ruleTemplet.getAllowanceaMaxMoney(), null));
		ruleTempletDao.create(ruleTemplet);
		
		// 模板奖项配置项明细
		Map<String, Object> detailMap = initTempletDetail(ruleTemplet);
		boolean commonPrizeFlag = (boolean) detailMap.get("commonPrizeFlag");
		List<VcodeActivityRebateRuleTempletDetail> detailLst = 
		        (List<VcodeActivityRebateRuleTempletDetail>) detailMap.get("detailLst");
		
		// 奖项配置项明细不为空且包含普扫
		if (commonPrizeFlag && CollectionUtils.isNotEmpty(detailLst)) {
		    Map<String, Object> map = new HashMap<>();
		    map.put("templetDetailLst", detailLst);
		    templetDetailDao.batchWrite(map);
		} else {
		    throw new Exception("添加失败，模板缺失奖项配置项明细！");
		}
		
		logService.saveLog("rebateRuleTemplet", Constant.OPERATION_LOG_TYPE.TYPE_1,
		            JSON.toJSONString(ruleTemplet), "创建规则模板:" + ruleTemplet.getTempletName());
	}

	/**
	 * 编辑返利规则模板
	 */
	@SuppressWarnings("unchecked")
    public void updateRebateRuleTemplet(VcodeActivityRebateRuleTemplet ruleTemplet) throws Exception {
        
        // 校验名称是否重复
        if ("1".equals(checkBussionName("rebateRuleTemplet", "INFO_KEY", 
                ruleTemplet.getInfoKey(), "TEMPLET_NAME", ruleTemplet.getTempletName()))) {
            throw new BusinessException("规则模板名称已存在");
        }

	    // 模板主表
        ruleTemplet.setUnitMoney(ruleTemplet.getUnitMoney().replace("≈", ""));
        ruleTemplet.setUnitVpoints(ruleTemplet.getUnitVpoints().replace("≈", ""));
        ruleTemplet.setAllowanceaMinMoney(StringUtils.defaultIfBlank(ruleTemplet.getAllowanceaMinMoney(), null));
        ruleTemplet.setAllowanceaMaxMoney(StringUtils.defaultIfBlank(ruleTemplet.getAllowanceaMaxMoney(), null));
		ruleTempletDao.update(ruleTemplet);
		
        // 模板奖项配置项明细
        Map<String, Object> detailMap = initTempletDetail(ruleTemplet);
        boolean commonPrizeFlag = (boolean) detailMap.get("commonPrizeFlag");
        List<VcodeActivityRebateRuleTempletDetail> detailLst = 
                (List<VcodeActivityRebateRuleTempletDetail>) detailMap.get("detailLst");

        // 奖项配置项明细不为空且包含普扫
        if (commonPrizeFlag && CollectionUtils.isNotEmpty(detailLst)) {
            
            // 先物理删除旧的配置项
            templetDetailDao.removeByTempletKey(ruleTemplet.getInfoKey());
            
            // 插入新的配置项
            Map<String, Object> map = new HashMap<>();
            map.put("templetDetailLst", detailLst);
            templetDetailDao.batchWrite(map);
        } else {
            throw new Exception("编辑失败，模板缺失奖项配置项明细！");
        }
		
        logService.saveLog("rebateRuleTemplet", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(ruleTemplet), "修改规则模板:" + ruleTemplet.getTempletName());
	}
	
	/**
	 * 初始化规则的奖项配置项详情
	 * 
	 * @return commonPrizeFlag、detailLst
	 */
	public Map<String, Object> initTempletDetail(VcodeActivityRebateRuleTemplet ruleTemplet) {
	    // 模板奖项配置项明细
        boolean commonPrizeFlag = false;
        VcodeActivityRebateRuleTempletDetail detailItem = null;
        List<VcodeActivityRebateRuleTempletDetail> detailLst = new ArrayList<>();
        if (ruleTemplet.getScanType() != null) {
            int itemNum = ruleTemplet.getScanType().length;
            for (int i = 0; i < itemNum; i++) {
                
                // 是否需要持久化首扫
                if (Double.valueOf(ruleTemplet.getFirstScanPercent()) <= 0 
                            && Constant.ScanType.type_0.equals(ruleTemplet.getScanType()[i])) continue;
                
                // 是否包含普扫
                if (Constant.ScanType.type_1.equals(ruleTemplet.getScanType()[i])
                        && Double.valueOf(ruleTemplet.getPrizePercent()[i]) > 0) commonPrizeFlag = true;
                
                detailItem = new VcodeActivityRebateRuleTempletDetail();
                detailItem.setInfoKey(UUID.randomUUID().toString());
                detailItem.setSequenceNum(i);
                detailItem.setTempletKey(ruleTemplet.getInfoKey());
                detailItem.setScanType(ruleTemplet.getScanType()[i]);
                detailItem.setRandomType(ruleTemplet.getRandomType()[i]);
                // 奖项获取方式-随机
                if (Constant.PrizeRandomType.type_0.equals(detailItem.getRandomType())) {
                    detailItem.setMinMoney(ruleTemplet.getMinMoney()[i]);
                    detailItem.setMaxMoney(ruleTemplet.getMaxMoney()[i]);
                    detailItem.setMinVpoints(ruleTemplet.getMinVpoints()[i]);
                    detailItem.setMaxVpoints(ruleTemplet.getMaxVpoints()[i]);
                } else {
                    detailItem.setMinMoney(ruleTemplet.getFixationMoney()[i]);
                    detailItem.setMaxMoney(ruleTemplet.getFixationMoney()[i]);
                    detailItem.setMinVpoints(ruleTemplet.getFixationVpoints()[i]);
                    detailItem.setMaxVpoints(ruleTemplet.getFixationVpoints()[i]);
                }
                // 奖项类型
                if (StringUtils.isNotBlank(ruleTemplet.getBigPrizeType()[i])) {
                    detailItem.setPrizeType(ruleTemplet.getBigPrizeType()[i]);
                } else if (Double.valueOf(detailItem.getMaxMoney()) > 0 
                        && Integer.valueOf(detailItem.getMaxVpoints()) > 0) {
                    detailItem.setPrizeType(Constant.PrizeType.status_2);
                } else if (Integer.valueOf(detailItem.getMaxVpoints()) > 0) {
                    detailItem.setPrizeType(Constant.PrizeType.status_1);
                } else {
                    detailItem.setPrizeType(Constant.PrizeType.status_0);
                }
                detailItem.setPrizePayMoney(StringUtils.defaultIfBlank(ruleTemplet.getPrizePayMoney()[i], "0.00"));
                detailItem.setAllowanceType(ruleTemplet.getAllowanceType()[i]);
                detailItem.setAllowanceMoney(StringUtils.defaultIfBlank(ruleTemplet.getAllowanceMoney()[i], "0.00"));
                detailItem.setPrizeDiscount(StringUtils.defaultIfBlank(ruleTemplet.getPrizeDiscount()[i], "0.00"));
                detailItem.setScanNum(ruleTemplet.getScanNum()[i]);
                detailItem.setCardNo(ruleTemplet.getCardNo()[i]);
                detailItem.setCogAmounts(StringUtils.defaultIfBlank(ruleTemplet.getCogAmounts()[i], "0"));
                detailItem.setPrizePercent(ruleTemplet.getPrizePercent()[i]);
                detailItem.setPrizePercentWarn(null);
                if (StringUtils.isBlank(detailItem.getScanNum()) && StringUtils.isNotBlank(ruleTemplet.getPrizePercentWarn()[i])) {
                    detailItem.setPrizePercentWarn(ruleTemplet.getPrizePercentWarn()[i]);
                }
                //待激活红包配置
                detailItem.setIsScanqrcodeWaitActivation("0");
                if(StringUtils.isNotBlank(ruleTemplet.getWaitActivationPrizeKey()[i])){
                    detailItem.setIsScanqrcodeWaitActivation("1");
                    detailItem.setWaitActivationPrizeKey(ruleTemplet.getWaitActivationPrizeKey()[i]);
                    detailItem.setWaitActivationMinMoney(Double.valueOf(ruleTemplet.getMinWaitActivationMoney()[i]));
                    detailItem.setWaitActivationMaxMoney(Double.valueOf(ruleTemplet.getMaxWaitActivationMoney()[i]));
                }


                detailItem.fillFields(ruleTemplet.getCreateUser());
                detailLst.add(0, detailItem);
            }
        }
        
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("commonPrizeFlag", commonPrizeFlag);
        resultMap.put("detailLst", detailLst);
        return resultMap;
	}
	
	/**
	 * 删除返利规则模板
	 */
	public void deleteRebateRuleTemplet(String infoKey, String optUserKey) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("infoKey", infoKey);
	    map.put("optUserKey", optUserKey);
	    ruleTempletDao.deleteById(map);
        logService.saveLog("rebateRuleTemplet", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(map), "删除活动规则模板");
	}

	/**
	 * 克隆活动规则模板
	 */
	public void executeCloneRebateRuleTemplet(VcodeActivityRebateRuleTemplet ruleTempletParam, String optUserKey) {

        // 校验名称是否重复
        if ("1".equals(checkBussionName("rebateRuleTemplet", "INFO_KEY", 
                    null, "TEMPLET_NAME", ruleTempletParam.getTempletName()))) {
            throw new BusinessException("规则模板名称已存在");
        }
	    
	    VcodeActivityRebateRuleTemplet ruleTemplet = ruleTempletDao.findById(ruleTempletParam.getInfoKey());
	    if (ruleTemplet == null) {
	        throw new BusinessException("克隆失败， 要克隆的活动规则不存在");
	    }
	    
	    // 初始化克隆对象
	    ruleTemplet.setInfoKey(UUID.randomUUID().toString());
	    ruleTemplet.setTempletName(ruleTempletParam.getTempletName());
	    ruleTemplet.setStatus("1".equals(ruleTempletParam.getStatus()) ? "1" : "0");
	    ruleTemplet.fillFields(optUserKey);
		ruleTempletDao.create(ruleTemplet);
		
		// 克隆模板对应的奖项配置
        Map<String, Object> map = new HashMap<>();
        map.put("templetKey", ruleTempletParam.getInfoKey());
        List<VcodeActivityRebateRuleTempletDetail> templetDetailLst = templetDetailDao.queryByTempletKey(map);
        for (VcodeActivityRebateRuleTempletDetail item : templetDetailLst) {
            item.setInfoKey(UUID.randomUUID().toString());
            item.setTempletKey(ruleTemplet.getInfoKey());
            item.fillFields(optUserKey);
        }
        map.clear();
        map.put("templetDetailLst", templetDetailLst);
        templetDetailDao.batchWrite(map);
        
		// 记录日志
		map.clear();
		map.put("oldTempletInfoKey", ruleTempletParam.getInfoKey());
		map.put("cloneTempletInfo", ruleTemplet);
        logService.saveLog("rebateRuleTemplet", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(map), "克隆活动规则模板");
	}
	
	/**
	 * 获取所有有效规则模板
	 */
	public List<VcodeActivityRebateRuleTemplet> queryValid() {
	    return ruleTempletDao.queryAllValid();
	}
	
	/**
     * 获取规则下商城优惠券NO
     * @param templetKey
     * @return
     */
    public List<String> queryCouponNoByTempletKey(String templetKey) {
        return templetDetailDao.queryCouponNoByTempletKey(templetKey);
    }
	
}
