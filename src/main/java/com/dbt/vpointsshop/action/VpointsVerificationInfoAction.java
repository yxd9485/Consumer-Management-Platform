package com.dbt.vpointsshop.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.list.TreeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsBrandInfo;
import com.dbt.vpointsshop.bean.VpointsVerificationInfo;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import com.dbt.vpointsshop.service.VpointsVerificationInfoService;

/**
 * 积分商场实物奖兑换核销表Action
 */
@Controller
@RequestMapping("/verification")
public class VpointsVerificationInfoAction extends BaseAction{

	@Autowired
	private VpointsGoodsService goodService;
	@Autowired
	private VpointsVerificationInfoService verificationInfoService;
	
	/**
	 * 获取列表
	 */
	@RequestMapping("/showVerificationInfoList")
	public String showVerificationInfoList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {
		    SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsVerificationInfo queryBean = new VpointsVerificationInfo(queryParam);
            
			List<VpointsVerificationInfo> resultList = verificationInfoService.queryForLst(queryBean, pageInfo);
			int countResult = verificationInfoService.queryForCount(queryBean);
			
			// 核销权限, admin及账务拥有
			String verificationFlag = validRole(session, "1", "3") ? "1" : "0";
			
			 // 当前用户可查看品牌信息
	        List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", currentUser.getUserName());
			
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
            model.addAttribute("verificationFlag", verificationFlag);
            model.addAttribute("brandLst", brandLst);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showVerificationInfoList";
	}
	
	/**
	 * 添加记录
	 */
	@ResponseBody
	@RequestMapping("/doVerificationInfoAdd")
	public String doDoubtTempletAdd(HttpSession session, String verificationEndDate, String brandKeys){
	    Map<String, Object> resultMap = new HashMap<>();
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			verificationInfoService.addVerificationInfo(verificationEndDate, brandKeys, currentUser.getUserKey());
			resultMap.put("errMsg", "生成核销记录成功");
			
		} catch (BusinessException ex) {
            resultMap.put("errMsg", ex.getMessage());
            
		} catch (Exception ex) {
		    resultMap.put("errMsg", "生成核销记录失败");
		    log.error("生成核销记录失败，verificationEndDate" + verificationEndDate, ex);
		}
		return JSON.toJSONString(resultMap);
	}
	
	/**
	 * 更新核销记录状态
	 */
	@RequestMapping("/updateVerificationStatus")
	public String updateVerificationStatus(HttpSession session, String verificationId, String status, Model model){
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
		    verificationInfoService.updateVerificationStatus(verificationId, status, currentUser.getUserKey());
			model.addAttribute("errMsg", "操作成功");
			
		} catch (BusinessException ex) {
            model.addAttribute("errMsg", ex.getMessage());
            
		} catch (Exception ex) {
			model.addAttribute("errMsg", "操作失败");
			log.error("更新核销记录状态失败，verificationId:"
			                    + verificationId + " status:" + status, ex);
		}
		return "forward:showVerificationInfoList.do"; 
	} 
	
	/**
	 * 转发列表也
	 */
	@RequestMapping("/doforwardList")
	public String doforwardList(HttpSession session, String verificationEndDate){
		return "forward:showVerificationInfoList.do"; 
	}
}
