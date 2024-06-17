package com.dbt.platform.firstscanrule.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.commonrule.bean.CommonRule;
import com.dbt.platform.commonrule.dao.ICommonRuleDao;
import com.dbt.platform.commonrule.service.CommonRuleService;
import com.dbt.platform.firstscanrule.bean.FirstScanRule;
import com.dbt.platform.firstscanrule.bean.SkuGroupRule;
import com.dbt.platform.system.bean.SysUserBasis;

@Service
public class FirstScanRuleService extends BaseService<FirstScanRule>{

	@Autowired
	private CommonRuleService commonRuleService;
	
	public FirstScanRule findFirstScanRule() {
		return commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_1, FirstScanRule.class);
	}

	/**
	 * 新增和修改首扫规则
	 * @param firstScanType
	 * @param ruleValue
	 * @param userKey
	 * @throws Exception 
	 */
	public void addOrUpdateFirstScanRule(FirstScanRule firstScanRule, SysUserBasis currentUser) throws Exception {
		
		// 只有分组的规则才执行
		if(Constant.FirstSweepType.TYPE_GROUP.equals(firstScanRule.getFirstScanType())
				&& firstScanRule.getRuleValue().indexOf("#") > -1){
			// 格式：分组名称#skuKey1#skuKey2...@分组名称#skuKey1#skuKey2...
			List<SkuGroupRule> skuGroupRuleList = new ArrayList<>();
			String[] ruleGroup  = firstScanRule.getRuleValue().split("@");
			for (String ruleInfo : ruleGroup) {
				String[] ruleItem = ruleInfo.split("#");
				String skuGroupName = "";
				String skuKeys = "";
				if(ruleItem.length > 0){
					skuGroupName = ruleItem[0];
					for (int i = 1; i < ruleItem.length; i++) {
						skuKeys += ruleItem[i] + ",";
					}
					skuKeys = skuKeys.substring(0, skuKeys.length() - 1);
					skuGroupRuleList.add(new SkuGroupRule(skuGroupName, skuKeys));
				}
			}
			
			// 解析后的规则
			firstScanRule.setSkuGroupRuleList(skuGroupRuleList);
		}
		
		// 转换并入库
		commonRuleService.createOrUpdate(new CommonRule(
				Constant.COMMON_RULE_TYPE.status_1, 
				JSONObject.toJSONString(firstScanRule),
				currentUser.getUserKey()));
	}
	
}
