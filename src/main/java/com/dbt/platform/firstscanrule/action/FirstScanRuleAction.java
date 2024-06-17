package com.dbt.platform.firstscanrule.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.firstscanrule.bean.FirstScanRule;
import com.dbt.platform.firstscanrule.bean.SkuGroupRule;
import com.dbt.platform.firstscanrule.service.FirstScanRuleService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 首扫规则Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/firstScanRule")
public class FirstScanRuleAction extends BaseAction{

	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private FirstScanRuleService firstScanRuleService;
	
	/**
	 * 查询首扫规则
	 */
	@RequestMapping("/showFirstScanRule")
	public String showFirstScanRule(HttpSession session, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
			FirstScanRule firstScanRule = firstScanRuleService.findFirstScanRule();
			if(null != firstScanRule){
				String skuKeys = null;
				StringBuilder sb = new StringBuilder();
				if(CollectionUtils.isNotEmpty(firstScanRule.getSkuGroupRuleList())){
					for (SkuGroupRule item : firstScanRule.getSkuGroupRuleList()) {
						sb.append(item.getSkuKeys());
						sb.append(",");
					}
				}
				if(StringUtils.isNotBlank(sb.toString())){
					skuKeys = sb.substring(0, sb.length() -1);
				}
				model.addAttribute("skuKeys", skuKeys);
			}
			model.addAttribute("skuList", skuList);
			model.addAttribute("skuKeyList", StringUtils.join(ReflectUtil.getFieldsValueByName("skuKey", skuList),","));
			model.addAttribute("firstScanRule", firstScanRule);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/firstscanrule/showFirstScanRule";
	}
	
	/**
	 * 新增首扫规则
	 */
	@RequestMapping("/doFirstScanRuleAddOrEdit")
	public String addOrUpdateFirstScanRule(HttpSession session, FirstScanRule firstScanRule, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			firstScanRuleService.addOrUpdateFirstScanRule(firstScanRule, currentUser);
			model.addAttribute("errMsg", "操作成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "操作失败");
			ex.printStackTrace();
		}
		return showFirstScanRule(session, model);
	}
}
