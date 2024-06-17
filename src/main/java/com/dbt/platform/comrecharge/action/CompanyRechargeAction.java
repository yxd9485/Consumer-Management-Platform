package com.dbt.platform.comrecharge.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.securityauth.PermissionCode;
import com.dbt.platform.comrecharge.bean.CompanyPrerechargeInfo;
import com.dbt.platform.comrecharge.service.CompanyRechargeService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @author RoyFu
 * @version 2.0
 * @createTime 2015年9月6日 下午1:45:57
 * @description 类说明
 */
@Controller
@RequestMapping("/companyRecharge")
public class CompanyRechargeAction extends BaseAction {

	@Autowired
	private CompanyRechargeService companyRechargeService;
    
	/**
	 * 预充值列表（财务充值）
	 */
	@RequestMapping("/showCompanyRechargeList")
	public String showCompanyRechargeList(String pageParam, String queryParam, Model model,HttpSession session) {
		showInfo(session,PermissionCode.companyRecharge.FINANCE, pageParam, queryParam, model);
		return "comrecharge/showCompanyRechargeList";
	}

	/**
	 * 充值列表（市场确认）
	 */
	@RequestMapping("/showCompanyConfirmList")
	public String showCompanyConfirmList(HttpSession session,String pageParam, String queryParam, Model model) {
		showInfo(session,PermissionCode.companyRecharge.MARKET, pageParam, queryParam, model);
		return "comrecharge/showCompanyConfirmList";
	}

	private void showInfo(HttpSession session,String permissionType, String pageParam, String queryParam, Model model) {
		try {
			CompanyPrerechargeInfo queryInfo = new CompanyPrerechargeInfo(queryParam,permissionType);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);

			List<CompanyPrerechargeInfo> resultList = 
			        companyRechargeService.loadPreInfoList(queryInfo, pageInfo);
			int countAll = this.companyRechargeService.loadPreInfoListCount(queryInfo, pageInfo);
			// 页面回显
			model.addAttribute("rechargeList", resultList);
			model.addAttribute("showCount", countAll);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("params", queryParam);
			model.addAttribute("startIndex", pageInfo.getStartCount());

			model.addAttribute("orderCol", pageInfo.getOrderCol());
             model.addAttribute("orderType", pageInfo.getOrderType());
			model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 发起新充值
	 */
	@ResponseBody
	@RequestMapping("/doCompanyRecharge")
	public String doCompanyRecharge(HttpSession session, CompanyPrerechargeInfo rechargeInfo, Model model) {
		Map<String, Object>  resultMap = new HashMap<>();
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			this.companyRechargeService.createNewRecharge(rechargeInfo, currentUser.getUserKey());
			resultMap.put("errMsg", "企业充值已提交，等待市场确认");

		} catch (Exception ex) {
            resultMap.put("errMsg", "充值记录提交失败");
			ex.printStackTrace();
		}
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 编辑 - 查询要编辑的充值
	 */
	@ResponseBody
	@RequestMapping("/editCompanyRecharge")
	public String editCompanyRecharge(HttpSession session, String rechargeInfokey, Model model) {
		CompanyPrerechargeInfo  info= null;
		try {
			  info=	this.companyRechargeService.findById(rechargeInfokey);
			model.addAttribute("info", info);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		info.setType("2");// 表示此请求为修改请求，和新增做区分
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rechargeInfo", info);
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 查找充值纪录
	 */
	@ResponseBody
	@RequestMapping("/findConfirmInfo")
	public String findConfirmInfo(String preKey, Model model,String type) {
		CompanyPrerechargeInfo rechargeInfo =null;
		try {
			rechargeInfo = this.companyRechargeService.findById(preKey);
			model.addAttribute("refresh", "delSuccess");
		} catch (Exception ex) {
			model.addAttribute("refresh", "delFail");
			ex.printStackTrace();
		}
		rechargeInfo.setType(type);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rechargeInfo", rechargeInfo);
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 确认充值
	 */
	@ResponseBody
	@RequestMapping("/confirmUpdate")
	public  String confirmUpdate(HttpSession session, String preKey, String status, String type){
		Map<String,String> resultMap= new HashMap<>();
		SysUserBasis currentUser = this.getUserBasis(session);
		CompanyPrerechargeInfo preInfo = companyRechargeService.findById(preKey);
		preInfo.setTransStatus(status);
		preInfo.setType(type);
		companyRechargeService.writeRecharge(preInfo, currentUser.getUserKey());
		if("2".equals(preInfo.getType())) {
			resultMap.put("errMsg", "该企业充值信息已确认，并反馈到了财务部");
		}
		else{
			resultMap.put("errMsg", "该企业充值信息已驳回，并提交至财务部进行核查");
		}
		return JSON.toJSONString(resultMap);
	}
}
