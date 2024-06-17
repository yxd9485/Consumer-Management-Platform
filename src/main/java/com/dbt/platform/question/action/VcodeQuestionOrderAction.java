package com.dbt.platform.question.action;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.question.bean.VpsQuestionnaireOrder;
import com.dbt.platform.question.service.QuestionOrderService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * Action
 * @version V1.0.0 </br>      
 * @createTime 2021年6月1日 </br>
 */

@Controller
@RequestMapping("/questionOrderAction")
public class VcodeQuestionOrderAction extends BaseAction {

	@Autowired
	private QuestionOrderService questionOrderService;
	
	/**
	 * 京东编号列表
	 */
	@RequestMapping("/showQuestionOrderList")
	public String showQuestionOrderList(HttpSession session, String tabsFlag,
	                        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			VpsQuestionnaireOrder queryBean = new VpsQuestionnaireOrder(queryParam);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			List<VpsQuestionnaireOrder> resultList = 
					questionOrderService.queryQuestionOrderList(queryBean, pageInfo);
			int countResult = questionOrderService.countQuestionOrderList(queryBean);
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
			ex.printStackTrace();
		}
		return "vcode/questionOrder/showQuestionOrderList";
	}
	
	/**
	 * 批量导入京东编号
	 */
	@ResponseBody
	@RequestMapping(value = "/importFileList", method = RequestMethod.POST)
	public String importFileList(Model model, HttpSession session, @RequestParam("file") MultipartFile file) {
		HashMap<String, String> map = new HashMap<String, String>();
		String result = "";
		List<String> list = null;
		try {
			list = questionOrderService.checkFile(file);
			if (list == null || list.size() == 0) {
				result = "addFalse";
				model.addAttribute("refresh", result);
				map.put("errMsg", "导入失败,清上传有效excel文件");
				return JSON.toJSONString(map);
			}
			questionOrderService.createBatch(list);
		} catch (Exception e) {
			result = "addFalse";
			map.put("errMsg", "导入失败,请检查是否有重复订单号!");
			e.printStackTrace();
			return JSON.toJSONString(map);
		}
		model.addAttribute("refresh", result);
		map.put("errMsg", "导入成功,总条数:"+list.size());
		return JSON.toJSONString(map);
	}
	
}
