package com.dbt.risk.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.risk.bean.MethodBean;
import com.dbt.risk.bean.MethodGlueBean;
import com.dbt.risk.service.RiskMethodService;

@Controller
@RequestMapping("/riskMethod")
public class RiskMethodAction extends BaseAction{
	@Autowired
	private RiskMethodService methodService;
	/**
	 * 查询
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @param session
	 * @return
	 */
	@RequestMapping("/getMethodList")
	public String getMethodList(Model model, String pageParam,String queryParam){
		PageOrderInfo info = new PageOrderInfo(pageParam);
		MethodBean bean=new MethodBean(queryParam);
		int countAll=0;
		List<MethodBean> secList=null;
		try {
			secList = methodService.getMethodList(info,bean);
			countAll = methodService.getMethodCount(info, bean);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
		model.addAttribute("secList", secList);
		model.addAttribute("showCount", countAll);
		model.addAttribute("startIndex", info.getStartCount());
		model.addAttribute("countPerPage", info.getPagePerCount());
		model.addAttribute("currentPage", info.getCurrentPage());
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
        model.addAttribute("queryParam", queryParam);
		return "risk/methodList";
	}
	/**
	 * 新增跳转
	 * @return
	 */
	@RequestMapping("/methodAdd")
	public String methodAdd(Model model,HttpSession session){
		return "risk/addMethod";
	}
	
	@RequestMapping("/addMethod")
	public String addMethod(Model model,HttpSession session, MethodBean bean){
		String result = "addSuccess";
		try {
			SysUserBasis user=getUserBasis(session);
			String userKey=user.getUserKey();
			bean.fillFields(userKey);
			methodService.addMethod(bean);
		} catch (Exception e) {
			result = "addFalse";
			e.printStackTrace();
		}
		model.addAttribute("refresh", result);
		return "forward:/riskMethod/getMethodList.do";
	}
	
	/**
	 * 详情
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @return
	 */
	@RequestMapping("/getMethodDetail")
	public String getMethodDetail(Model model,String infoKey,HttpSession session){
		PageOrderInfo info = new PageOrderInfo(null);
		MethodBean bean=new MethodBean();
		bean.setInfoKey(infoKey);
		try {
			methodService.getMethodDetail(info, bean, model);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
				
		return "risk/editMethod";
	}
	/**
	 * 更新
	 * @param model
	 * @param session
	 * @param batchFile
	 * @param batch
	 * @return
	 */
	@RequestMapping("/updateMethod")
	public String updateMethod(Model model,HttpSession session,MethodBean bean){
		bean.setUpdateTime(DateUtil.getDateTime());
		String result="addSuccess";
		try {
			methodService.updateMethod(bean);
		} catch (Exception e) {
			result = "addFalse";
			e.printStackTrace();
		}
		model.addAttribute("refresh", result);
		return "forward:/riskMethod/getMethodList.do";
	}
	/**
	 * 删除
	 * @param model
	 * @param session
	 * @param batchFile
	 * @param batch
	 * @return
	 */
	@RequestMapping("/delMethod")
	public String delMethod(Model model,String infoKey){
		String result="deleteSuccess";
		try {
			methodService.delMethod(infoKey);
		} catch (Exception e) {
			result = "deleteFalse";
			e.printStackTrace();
		}
		model.addAttribute("refresh", result);
		return "forward:/riskMethod/getMethodList.do";
	}
	/**
	 * 名称重复验证
	 * @param seckillName
	 * @return
	 */
	@RequestMapping("/checkName")
	@ResponseBody
	public String checkName(String methodName,String infoKey) {
		try {
			int countAll = methodService.checkName(methodName,infoKey);
			if(countAll>0) {
				return "FAIL";
			}
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAIL";
	}
	/**
	 * 数据同步
	 * @param seckillName
	 * @return
	 */
	@RequestMapping("/syncData")
	@ResponseBody
	public String syncData() {
		try {
			String resultString = methodService.syncData();
			return resultString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAIL";
	}
	/**
	 * 编辑glue跳转
	 * @return
	 */
	@RequestMapping("/glueEdit")
	public String glueEdit(Model model,HttpSession session,String infoKey){
		MethodGlueBean bean=methodService.getMethodGlue(infoKey);
		if(bean==null) {
			bean=new MethodGlueBean();
			bean.setMethodInfoKey(infoKey);
		}
		model.addAttribute("bean",bean);
		model.addAttribute("methodInfoKey",infoKey);
		return "risk/editGlue";
	}
	@RequestMapping("/addGlue")
	public String addGlue(Model model,HttpSession session, MethodGlueBean bean){
		String result = "addSuccess";
		try {
			methodService.addMethodGlue(bean);
		} catch (Exception e) {
			result = "addFalse";
			e.printStackTrace();
		}
		model.addAttribute("refresh", result);
		return "forward:/riskMethod/getMethodList.do";
	}
}
