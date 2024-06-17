package com.dbt.platform.activity.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodeActivityDoubtTemplet;
import com.dbt.platform.activity.service.VcodeActivityDoubtTempletService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 风控模板主表Action
 */
@Controller
@RequestMapping("/doubtTemplet")
public class VcodeActivityDoubtTempletAction extends BaseAction{

	@Autowired
	private VcodeActivityDoubtTempletService doubtTempletService;
	
	/**
	 * 获取列表
	 */
	@RequestMapping("/showDoubtTempletList")
	public String showDoubtTempletList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeActivityDoubtTemplet queryBean = new VcodeActivityDoubtTemplet(queryParam);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
			List<VcodeActivityDoubtTemplet> resultList = doubtTempletService.queryForLst(queryBean, pageInfo);
			int countResult = doubtTempletService.queryForCount(queryBean);
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
		return "vcode/doubtTemplet/showDoubtTempletList";
	}
	
	/**
	 * 跳转新增页面
	 */
	@RequestMapping("/showDoubtTempletAdd")
	public String showDoubtTempletAdd(HttpSession session, Model model){
		
        return "vcode/doubtTemplet/showDoubtTempletAdd";
	}
	
	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showDoubtTempletEdit")
	public String showDoubtTempletEdit(HttpSession session, String infoKey, Model model){
	    VcodeActivityDoubtTemplet doubtTemplet = doubtTempletService.findById(infoKey);
		model.addAttribute("doubtTemplet", doubtTemplet);
        return "vcode/doubtTemplet/showDoubtTempletEdit";
	}
	
	/**
	 * 添加记录
	 */
	@RequestMapping("/doDoubtTempletAdd")
	public String doDoubtTempletAdd(HttpSession session, VcodeActivityDoubtTemplet doubtTemplet, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			doubtTemplet.setCompanyKey(currentUser.getCompanyKey());
			doubtTemplet.fillFields(currentUser.getUserKey());
			doubtTempletService.addDoubtTemplet(doubtTemplet);
			model.addAttribute("errMsg", "添加成功");
			
		} catch (BusinessException ex) {
            model.addAttribute("errMsg", ex.getMessage());
		    
		} catch (Exception ex) {
		    model.addAttribute("errMsg", "添加失败");
		    log.error("风控模板添加失败", ex);
		}
		return "forward:showDoubtTempletList.do"; 
	}
	
	/**
	 * 编辑记录
	 */
	@RequestMapping("/doDoubtTempletEdit")
	public String doDoubtTempletEdit(HttpSession session,
	                    VcodeActivityDoubtTemplet doubtTemplet, Model model){
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
		    doubtTemplet.fillUpdateFields(currentUser.getUserKey());
		    doubtTempletService.updateDoubtTemplet(doubtTemplet);
			model.addAttribute("errMsg", "编辑成功");
            
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", ex.getMessage());
            
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("风控模板编辑失败", ex);
        }
        return "forward:showDoubtTempletList.do";
	} 
	
	/**
	 * 删除记录
	 */
	@RequestMapping("/doDoubtTempletDelete")
	public String doDoubtTempletDelete(HttpSession session, String infoKey, Model model){
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
		    doubtTempletService.deleteDoubtTemplet(infoKey, currentUser.getUserKey());
		    model.addAttribute("errMsg", "删除成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "删除失败");
            log.error("风控模板删除失败", ex);
		}
        return "forward:showDoubtTempletList.do";
	} 
}
