package com.dbt.platform.benediction.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.benediction.bean.VpsVcodeBenedictionExpandRule;
import com.dbt.platform.benediction.dao.IVpsVcodeBenedictionExpandRuleDao;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 膨胀红包规则配置Service
 *
 */
@Service
public class VpsVcodeBenedictionExpandRuleService extends BaseService<VpsVcodeBenedictionExpandRule> {
    
	@Autowired
	private IVpsVcodeBenedictionExpandRuleDao expandRuleDao;

	/**
	 * 获取列表List
	 */
	public List<VpsVcodeBenedictionExpandRule> queryForList(
	        VpsVcodeBenedictionExpandRule queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return expandRuleDao.queryForList(map);
	}

	/**
	 * 获取列表Count
	 */
	public int queryForCount(VpsVcodeBenedictionExpandRule queryBean) {
		Map<String, Object> map = new HashMap<>();	
		map.put("queryBean", queryBean);
		return expandRuleDao.queryForCount(map);
	}

	/**
	 * 根据主键查询信息
	 */
	public VpsVcodeBenedictionExpandRule findById(String infoKey) {
		return expandRuleDao.findById(infoKey);
	}

	/**
	 * 添加
	 */
	public void addExpandRule(VpsVcodeBenedictionExpandRule expandRule, SysUserBasis userBasis) throws Exception{
	    
	    // 校验是否存在活动冲突
	    validForExpandRule(expandRule);
	    
		// 初始化规则
	    expandRule.setInfoKey(UUID.randomUUID().toString());
		expandRule.setExpandRuleNo(getBussionNo("vpsVcodeBenedictionExpandRule", "expand_rule_no", Constant.OrderNoType.type_PZ));
		expandRule.fillFields(userBasis.getUserKey());
		expandRuleDao.create(expandRule);
		
		logService.saveLog("vpsVcodeBenedictionExpandRule", Constant
		        .OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(expandRule), "添加膨胀规则:" + expandRule.getExpandRuleName());
        
        CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_BENEDICTION_EXPAND_RULE 
                + Constant.DBTSPLIT + expandRule.getVcodeActivityKey() + Constant.DBTSPLIT + DateUtil.getDate("yyyMMdd"));
	}

	/**
	 * 修改
	 * @throws Exception 
	 */
	public void editExpandRule(VpsVcodeBenedictionExpandRule expandRule, SysUserBasis userBasis) throws Exception {
	    
	    VpsVcodeBenedictionExpandRule expandRuleOld = this.findById(expandRule.getInfoKey());
	    
        // 校验是否存在活动冲突
	    validForExpandRule(expandRule);
		
	    // 初始化规则
	    expandRule.fillUpdateFields(userBasis.getUserKey());
		expandRuleDao.update(expandRule);
		
		logService.saveLog("vpsVcodeBenedictionExpandRule", Constant
		        .OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(expandRule), "修改膨胀规则:" + expandRule.getExpandRuleName());
        
        CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_BENEDICTION_EXPAND_RULE 
                + Constant.DBTSPLIT + expandRuleOld.getVcodeActivityKey() + Constant.DBTSPLIT + DateUtil.getDate("yyyMMdd"));
        CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_BENEDICTION_EXPAND_RULE 
                + Constant.DBTSPLIT + expandRule.getVcodeActivityKey() + Constant.DBTSPLIT + DateUtil.getDate("yyyMMdd"));
	}
	
	/**
	 * 删除记录
	 * @throws Exception 
	 */
	public void deleteExpandRule(String infoKey, String updateUser) throws Exception {
	    VpsVcodeBenedictionExpandRule expandRule = findById(infoKey);
	    Map<String, Object> map = new HashMap<>();
	    map.put("infoKey", infoKey);
	    map.put("optUserKey", updateUser);
	    expandRuleDao.deleteById(map);
	    
	    map.put("expandRule", expandRule);
	    logService.saveLog("vpsVcodeBenedictionExpandRule", Constant
	            .OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(map), "删除膨胀规则:" + expandRule.getExpandRuleName());
        
        CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_BENEDICTION_EXPAND_RULE 
                + Constant.DBTSPLIT + expandRule.getVcodeActivityKey() + Constant.DBTSPLIT + DateUtil.getDate("yyyMMdd"));
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("vpsVcodeBenedictionExpandRule", "info_key", infoKey, "expand_rule_name", bussionName);
	}
	
	/**
	 * 校验活动有效性
	 * @throws BusinessException 
	 */
	private void validForExpandRule(VpsVcodeBenedictionExpandRule expandRule) throws BusinessException {
	    Map<String, Object> map = new HashMap<>();
	    map.put("infoKey", expandRule.getInfoKey());
	    map.put("beginDate", expandRule.getBeginDate());
	    map.put("endDate", expandRule.getEndDate());
	    map.put("vcodeActivityKey", expandRule.getVcodeActivityKey());
	    
	    List<VpsVcodeBenedictionExpandRule> expandRuleLst = expandRuleDao.queryByDateForActivity(map);
	    if (CollectionUtils.isNotEmpty(expandRuleLst)) {
	        throw new BusinessException("当前规则与" + expandRuleLst.get(0).getExpandRuleName() + "有冲突！");
	    }
	}
}
