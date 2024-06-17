package com.dbt.platform.ladder.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.dbt.platform.ladder.bean.LadderUI;
import com.dbt.platform.ladder.service.LadderUIService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.ladder.bean.LadderRuleCog;
import com.dbt.platform.ladder.service.LadderRuleService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @author RoyFu
 * @createTime 2016年1月19日 下午5:44:05
 * @description V码活动Action
 */

@Controller
@RequestMapping("/ladderRule")
public class LadderRuleAction extends BaseAction {

	@Autowired
	private LadderRuleService ladderRuleService;
	@Autowired
	private VcodeActivityService activityService;


	/**
	 * 阶梯规则列表
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showLadderRuleList")
	public String showLadderRuleList(HttpSession session, 
	        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			LadderRuleCog queryBean = new LadderRuleCog(queryParam);
			queryBean.setCurrDate(DateUtil.getDate());
			List<LadderRuleCog> resultList = 
					ladderRuleService.findLadderRuleList(queryBean, pageInfo);
			int countResult = ladderRuleService.findLadderRuleCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/ladder/showLadderRuleList";
	}

	/**
	 * 添加页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showLadderRuleAdd")
	public String showLadderRuleAdd(HttpSession session, Model model) {
		try {
			// 查询可配置阶梯的活动
			model.addAttribute("activityList", 
					activityService.queryListForLadder(new HashMap<String, Object>()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/ladder/showLadderRuleAdd";
	}

	/**
	 * 编辑页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showLadderRuleEdit")
	public String showVcodeActivityEdit(HttpSession session, String infoKey, Model model) {
		try {
			//查询当前规则
			LadderRuleCog ladder = ladderRuleService.findById(infoKey);
			if(null != ladder){
				Map<String, Object> map = new HashMap<>();
				map.put("ruleKey", ladder.getInfoKey());
				// 查询可配置阶梯的活动
				model.addAttribute("activityList", 
						activityService.queryListForLadder(map));
			}
			model.addAttribute("ladder", ladder);
			model.addAttribute("pageFlag", "edit");
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vcode/ladder/showLadderRuleEdit";
	}
	
	/**
	 * 查看页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showLadderRuleView")
	public String showLadderRuleView(HttpSession session, String infoKey, Model model) {
		try {
			//查询当前规则
			LadderRuleCog ladder = ladderRuleService.findById(infoKey);
			if(null != ladder){
				Map<String, Object> map = new HashMap<>();
				map.put("ruleKey", ladder.getInfoKey());
				// 查询可配置阶梯的活动
				model.addAttribute("activityList", 
						activityService.queryListForLadder(map));
			}
			model.addAttribute("ladder", ladder);
			model.addAttribute("pageFlag", "view");
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vcode/ladder/showLadderRuleEdit";
	}
	
	/**
	 * 创建规则
	 */
	@RequestMapping("/doLadderRuleAdd")
	public String doVcodeActivityAdd(HttpSession session, LadderRuleCog ladderRuleCog, Model model) {
		String infoKey = "";
		String errMsg = "";
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			ladderRuleCog.fillFields(currentUser.getUserKey());
			infoKey = ladderRuleService.create(ladderRuleCog);
			model.addAttribute("errMsg", "添加成功");
			errMsg = "保存成功";
		}catch (Exception ex) {
			model.addAttribute("errMsg", "添加失败");
		    log.error(ex.getMessage(), ex);
        }
		return "forward:showLadderRuleList.do";
	}


	/**
	 * 修改活动
	 */
	@RequestMapping("/doLadderRuleEdit")
	public String doVcodeActivityEdit(HttpSession session, LadderRuleCog ladderRuleCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			ladderRuleCog.fillFields(currentUser.getUserKey());
			ladderRuleService.update(ladderRuleCog);
			
			// 删除缓存
			String key = CacheUtilNew.cacheKey.vodeActivityKey
					.KEY_LADDER_RULE_COG + Constant.DBTSPLIT + DateUtil.getDate();
			CacheUtilNew.removeGroupByKey(key);
			model.addAttribute("errMsg", "编辑成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "编辑失败");
		    log.error(ex.getMessage(), ex);
		}
		return "forward:showLadderRuleList.do";
	}
	
	/**
	 * 删除活动
	 */
	@RequestMapping("/doLadderRuleDel")
	public String doLadderRuleDel(HttpSession session, String infoKey, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			ladderRuleService.delete(infoKey, currentUser);
			model.addAttribute("errMsg", "删除成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "删除失败");
			log.error(ex.getMessage(), ex);
		}
		return "forward:showLadderRuleList.do";
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkBussionName")
	public String checkBussionName(String infoKey, String bussionName){
		return ladderRuleService.checkBussionName(infoKey, bussionName);
	}
	
	
	/**
	 * 检验活动是否可配置
	 * @param activityKeys
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkActivityForLadder")
	public String checkActivityForLadder(String activityKeys, String ladderInfoKey,String startDate,String endDate){
		return JSON.toJSONString(ladderRuleService.checkActivityForLadder(activityKeys, ladderInfoKey,startDate,endDate)); 
	}

	
	
}
