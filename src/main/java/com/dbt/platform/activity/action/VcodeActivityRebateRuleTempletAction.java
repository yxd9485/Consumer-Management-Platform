package com.dbt.platform.activity.action;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleTemplet;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleTempletService;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeCog;
import com.dbt.platform.waitActivation.service.WaitActivationPrizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动规则模板主表Action
 */
@Controller
@RequestMapping("/rebateRuleTemplet")
public class VcodeActivityRebateRuleTempletAction extends BaseAction{

	@Autowired
	private VcodeActivityRebateRuleTempletService ruleTempletService;
	@Autowired
	private VcodeActivityVpointsCogService vpointsCogService;
	@Autowired
	private WaitActivationPrizeService waitActivationPrizeService;
	
	/**
	 * 获取列表
	 */
	@RequestMapping("/showRebateRuleTempletList")
	public String showRebateRuleTempletList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {
		    SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeActivityRebateRuleTemplet queryBean = new VcodeActivityRebateRuleTemplet(queryParam);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
			List<VcodeActivityRebateRuleTemplet> resultList = ruleTempletService.queryForLst(queryBean, pageInfo);
			int countResult = ruleTempletService.queryForCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
		    log.error("活动规则模板查询失败", ex);
		}
		return "vcode/rebateRuleTemplet/showRebateRuleTempletList";
	}
	
	/**
	 * 跳转新增页面
	 */
	@RequestMapping("/showRebateRuleTempletAdd")
	public String showRebateRuleTempletAdd(HttpSession session, Model model) throws Exception {
	    model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(false, true, true, true, true, false, null));
	    model.addAttribute("allowanceTypeMap", vpointsCogService.queryAllowanceType());
		model.addAttribute("cardTypeMap", vpointsCogService.queryCardType());
		// 查询待激活红包列表
		List<VpsWaitActivationPrizeCog> waitActivationPrizeList = waitActivationPrizeService.queryAll();
		model.addAttribute("waitActivationPrize", waitActivationPrizeList);
		return "vcode/rebateRuleTemplet/showRebateRuleTempletAdd";
	}
	
	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showRebateRuleTempletEdit")
	public String showRebateRuleTempletEdit(HttpSession session, String infoKey, Model model) throws Exception {
	    VcodeActivityRebateRuleTemplet ruleTemplet = ruleTempletService.findById(infoKey);
	    List<String> couponNoLst = ruleTempletService.queryCouponNoByTempletKey(infoKey);
		model.addAttribute("ruleTemplet", ruleTemplet);
        model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(false, true, true, true, true, false, couponNoLst));
        model.addAttribute("allowanceTypeMap", vpointsCogService.queryAllowanceType());
        model.addAttribute("cardTypeMap", vpointsCogService.queryCardType());
		// 查询待激活红包列表
		List<VpsWaitActivationPrizeCog> waitActivationPrizeList = waitActivationPrizeService.queryAll();
		model.addAttribute("waitActivationPrize", waitActivationPrizeList);
        return "vcode/rebateRuleTemplet/showRebateRuleTempletEdit";
	}
	
	/**
	 * 添加记录
	 */
	@RequestMapping("/doRebateRuleTempletAdd")
	public String doRebateRuleTempletAdd(HttpSession session, 
	                VcodeActivityRebateRuleTemplet ruleTemplet, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			ruleTemplet.setCompanyKey(currentUser.getCompanyKey());
			ruleTemplet.fillFields(currentUser.getUserKey());
			ruleTempletService.addRebateRuleTemplet(ruleTemplet);
			model.addAttribute("errMsg", "添加成功");
		} catch (BusinessException ex) {
		    model.addAttribute("errMsg", ex.getMessage());
		} catch (Exception ex) {
		    model.addAttribute("errMsg", "添加失败");
		    log.error("活动规则模板添加失败", ex);
			ex.printStackTrace();
		}
		return "forward:showRebateRuleTempletList.do"; 
	}
	
	/**
	 * 编辑记录
	 */
	@RequestMapping("/doRebateRuleTempletEdit")
	public String doRebateRuleTempletEdit(HttpSession session, 
	                VcodeActivityRebateRuleTemplet ruleTemplet, Model model){
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
		    ruleTemplet.fillUpdateFields(currentUser.getUserKey());
		    ruleTempletService.updateRebateRuleTemplet(ruleTemplet);
			model.addAttribute("errMsg", "编辑成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", ex.getMessage());
		} catch (Exception ex) {
			model.addAttribute("errMsg", "编辑失败");
            log.error("活动规则模板编辑失败", ex);
		}
        return "forward:showRebateRuleTempletList.do"; 
	} 
	
	/**
	 * 删除记录
	 */
	@RequestMapping("/doRebateRuleTempletDelete")
	public String doRebateRuleTempletDelete(HttpSession session, String infoKey,  Model model){
	    try{
	        SysUserBasis currentUser = this.getUserBasis(session);
	        ruleTempletService.deleteRebateRuleTemplet(infoKey, currentUser.getUserKey());
	        model.addAttribute("errMsg", "删除成功");
	    } catch (BusinessException ex) {
	        model.addAttribute("errMsg", ex.getMessage());
	    } catch (Exception ex) {
	        model.addAttribute("errMsg", "删除失败");
	        log.error("活动规则模板删除失败", ex);
	    }
	    return "forward:showRebateRuleTempletList.do"; 
	}
	
	/**
	 * 克隆活动规则模板
	 */
	@ResponseBody
	@RequestMapping("/doRebateRuleTempletClone")
	public String doRebateRuleTempletClone(HttpSession session,  
                        VcodeActivityRebateRuleTemplet ruleTemplet,  Model model){
        Map<String, String> resultMap = new HashMap<>();
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
		    ruleTempletService.executeCloneRebateRuleTemplet(ruleTemplet, currentUser.getUserKey());
		    resultMap.put("errMsg", "克隆成功");
        } catch (BusinessException ex) {
            resultMap.put("errMsg", ex.getMessage());
        } catch (Exception ex) {
            resultMap.put("errMsg", "克隆失败");
            log.error("活动规则模板克隆失败", ex);
        }
        return JSON.toJSONString(resultMap);
	}
}
