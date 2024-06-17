package com.dbt.platform.benediction.action;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.benediction.bean.VpsVcodeBenedictionExpandRule;
import com.dbt.platform.benediction.service.VpsVcodeBenedictionExpandRuleService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 逢尾数规则Action
 */
@Controller
@RequestMapping("/benediction")
public class VpsVcodeBenedictionExpandRuleAction extends BaseAction{
    
    private Logger log = Logger.getLogger(VpsVcodeBenedictionExpandRuleAction.class);

	@Autowired
	private VpsVcodeBenedictionExpandRuleService expandRuleService;
    @Autowired
    private VcodeActivityService vcodeActivityService;
	
	/**
	 * 获取列表
	 */
	@RequestMapping("/showExpandRuleList")
	public String showExpandRuleList(HttpSession session, 
	            String tabsFlag, String queryParam, String pageParam, Model model) {
		try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeBenedictionExpandRule queryBean = new VpsVcodeBenedictionExpandRule(queryParam);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setTabsFlag(StringUtils.isBlank(tabsFlag) ? "1" : tabsFlag);
            
			List<VpsVcodeBenedictionExpandRule> resultList = expandRuleService.queryForList(queryBean, pageInfo);
			int countResult = expandRuleService.queryForCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("tabsFlag", queryBean.getTabsFlag());
			model.addAttribute("projectServerName", DbContextHolder.getDBType());
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vcode/benediction/showExpandRuleList";
	}
	
	/**
	 * 跳转新增页面
	 */
	@RequestMapping("/showExpandRuleAdd")
	public String showExpandRuleAdd(HttpSession session, Model model) throws Exception {
	    model.addAttribute("activityLst", vcodeActivityService.queryValidActivity());
		model.addAttribute("projectServerName", DbContextHolder.getDBType());
	    
        return "vcode/benediction/showExpandRuleAdd";
	}
	
	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showExpandRuleEdit")
	public String showExpandRuleEdit(HttpSession session, String infoKey, Model model) throws Exception {
	    VpsVcodeBenedictionExpandRule expandRule = expandRuleService.findById(infoKey);
	    if (StringUtils.defaultString(expandRule.getExpireDate()).length() > 10) {
	        expandRule.setExpireDate(expandRule.getExpireDate().substring(0, 10));
	    }
        model.addAttribute("expandRule", expandRule);
        model.addAttribute("activityLst", vcodeActivityService.queryValidActivity());
		model.addAttribute("projectServerName", DbContextHolder.getDBType());
	    return "vcode/benediction/showExpandRuleEdit";
	}
	
	/**
	 * 跳转查看页面
	 */
	@RequestMapping("/showExpandRuleView")
	public String showExpandRuleView(HttpSession session, String infoKey, Model model) throws Exception {
        VpsVcodeBenedictionExpandRule expandRule = expandRuleService.findById(infoKey);
        if (StringUtils.defaultString(expandRule.getExpireDate()).length() > 10) {
            expandRule.setExpireDate(expandRule.getExpireDate().substring(0, 10));
        }
        model.addAttribute("expandRule", expandRule);
        model.addAttribute("activityLst", vcodeActivityService.queryValidActivity());
        
        return "vcode/benediction/showExpandRuleView";
	}
	
	/**
	 * 添加记录
	 */
	@RequestMapping("/doExpandRuleAdd")
	public String doExpandRuleAdd(HttpSession session,  VpsVcodeBenedictionExpandRule expandRule, Model model){
		try{
			SysUserBasis userBasis = this.getUserBasis(session);
			expandRuleService.addExpandRule(expandRule, userBasis);
			model.addAttribute("errMsg", "添加成功");
		} catch (Exception ex) {
		    if (ex instanceof BusinessException) {
		        model.addAttribute("errMsg", "添加失败," + ex.getMessage());
		    } else {
		        model.addAttribute("errMsg", "添加失败");
		    }
			log.error(ex.getMessage(), ex);
		}
		return "forward:showExpandRuleList.do";
	}
	
	/**
	 * 修改记录
	 */
	@RequestMapping("/doExpandRuleEdit")
	public String doExpandRuleEdit(HttpSession session, VpsVcodeBenedictionExpandRule expandRule, Model model){
		try {
			SysUserBasis userBasis = this.getUserBasis(session);
			expandRuleService.editExpandRule(expandRule, userBasis);
	        
			model.addAttribute("errMsg", "修改成功");
		} catch (Exception ex) {
            if (ex instanceof BusinessException) {
                model.addAttribute("errMsg", "修改失败," + ex.getMessage());
            } else {
                model.addAttribute("errMsg", "修改失败");
                try {
                    CacheUtilNew.removeAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.error(ex.getMessage(), ex);
		}
		return "forward:showExpandRuleList.do";
	}
	
	/**
	 * 刪除记录
	 */
	@RequestMapping("/doExpandRuleDelete")
	public String doExpandRuleDelete(HttpSession session, String infoKey, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			expandRuleService.deleteExpandRule(infoKey, currentUser.getUserKey());
	        
			model.addAttribute("errMsg", "删除成功");
		} catch (Exception ex) {
            if (ex instanceof BusinessException) {
                model.addAttribute("errMsg", "删除失败," + ex.getMessage());
            } else {
                model.addAttribute("errMsg", "删除失败");
                try {
                    CacheUtilNew.removeAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.error(ex.getMessage(), ex);
			
		}
		return "forward:showExpandRuleList.do"; 
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
		return expandRuleService.checkBussionName(infoKey, bussionName);
	}
}
