package com.dbt.platform.ticket.action;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.ticket.bean.VpsVcodeTicketActivityCog;
import com.dbt.platform.ticket.bean.VpsVcodeTicketInfo;
import com.dbt.platform.ticket.service.VpsSysTicketCategoryService;
import com.dbt.platform.ticket.service.VpsVcodeTicketActivityCogService;

/**
 * 优惠券活动Activity
 */

@Controller
@RequestMapping("/vcodeTicketActivity")
public class VpsVcodeTicketActivityAction extends BaseAction {
	
	Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private VpsVcodeTicketActivityCogService ticketActivityCogService;
	@Autowired
	private VpsSysTicketCategoryService sysTicketCategoryService;

	/**
	 * 优惠券活动列表
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showTicketActivityList")
	public String showTicketActivityList(HttpSession session, 
	        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			VpsVcodeTicketActivityCog queryBean = new VpsVcodeTicketActivityCog(queryParam);
			List<VpsVcodeTicketActivityCog> resultList = 
			        ticketActivityCogService.findTicketActivityList(queryBean, pageInfo);
			int countResult = ticketActivityCogService.countTicketActivityList(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("queryBean", queryBean);
			model.addAttribute("pageParam", pageParam);
			model.addAttribute("nowTime", new LocalDate());
			model.addAttribute("ticketCategoryList", sysTicketCategoryService.loadTicketCategory());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/ticket/showVcodeTicketActivityList";
	}

	/**
	 * 优惠券活动 添加页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showTicketActivityAdd")
	public String showTicketActivityAdd(HttpSession session, 
            String queryParam, String pageParam, Model model) {
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);
        model.addAttribute("ticketCategoryList", sysTicketCategoryService.loadTicketCategory());
		return "vcode/ticket/showVcodeTicketActivityAdd";
	}

	/**
	 * 优惠券活动 编辑页面
	 * 
	 * @param session
	 * @param vcodeActivityKey
	 * @param requestType
	 * @param model
	 * @return
	 */
	@RequestMapping("/showTicketActivityEdit")
	public String showTicketActivityEdit(HttpSession session, String vcodeActivityKey,
	                    String queryParam, String pageParam, Model model) {
		try {
		    
		    VpsVcodeTicketActivityCog activityCog = 
                    ticketActivityCogService.loadTicketActivityByKey(vcodeActivityKey);
			// 查询当前活动
			model.addAttribute("activityCog", activityCog);
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
			model.addAttribute("ticketCategoryList", sysTicketCategoryService.loadTicketCategory());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/ticket/showVcodeTicketActivityEdit";
	}
	
	/**
	 * 创建优惠券活动
	 * 
	 * @param session
	 * @param activityCog
	 * @param model
	 * @return
	 */
	@RequestMapping("/doTicketActivityAdd")
	public String doTicketActivityAdd(HttpSession session, VpsVcodeTicketActivityCog activityCog, 
                                String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			activityCog.setCompanyKey(currentUser.getCompanyKey());
			ticketActivityCogService.writeTicketActivityCog(activityCog, currentUser.getUserKey(), model);
			model.addAttribute("errMsg", "保存成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "保存失败");
			ex.printStackTrace();
		}
		return "forward:showTicketActivityList.do";
	}

	/**
	 * 修改优惠券活动
	 * 
	 * @param session
	 * @param activityCog
	 * @param pageParam
	 * @param queryParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/doTicketActivityEdit")
	public String doTicketActivityEdit(HttpSession session, VpsVcodeTicketActivityCog 
	                        activityCog, String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			activityCog.setCompanyKey(currentUser.getCompanyKey());
			ticketActivityCogService.updateTicketActivityCog(activityCog, currentUser.getUserKey(), model);
		} catch (Exception ex) {
			model.addAttribute("flag", "edit_fail");
			ex.printStackTrace();
		}
		return showTicketActivityList(session, queryParam, pageParam, model);
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.getDateTime(DateUtil.addDays(365), "yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
     * 导入优惠券
     * @param model
     * @param session
     * @param info
     * @return
     */
	@Deprecated
    @ResponseBody
    @RequestMapping("/importTicketCode")
    public String importTicketCode(Model model,HttpSession session,
            @RequestParam("ticketFile") MultipartFile batchFile, VpsVcodeTicketInfo ticketInfo){
    	long time = System.currentTimeMillis();
        SysUserBasis user=getUserBasis(session);
        String userKey=user.getUserKey();
        Map<String, String> resurtMap = new HashMap<String, String>();
        ticketInfo.fillFields(userKey);
        try {
            resurtMap = ticketActivityCogService.addTicketInfo(ticketInfo, batchFile);
        } catch (Exception e) {
        	// 券码重复
    		if(e.getMessage().contains("TICKET_CODE_IDX")){
//    			paramMap.clear();
//    			paramMap.put("libName", ticketActivityCog.getLibName());
//	    		paramMap.put("importTime", nowTime);
//    			ticketLibService.deleteByTime(paramMap);
    			resurtMap.put("errMsg", "导入失败：重复券码导入异常");
    		}else{
    			resurtMap.put("errMsg", "导入失败：券码导入异常");
    		}
            e.printStackTrace();
        }
        if(null != batchFile){
        	System.out.println("importTicketCode-" + batchFile.getOriginalFilename() + "使用了" + (System.currentTimeMillis() - time));
        }
        return JSON.toJSONString(resurtMap);
    }
    
    /**
     * 导入优惠券
     * @param model
     * @param session
     * @param info
     * @return
     */
    @ResponseBody
    @RequestMapping("/importTicketCodeByTxt")
    public String importTicketCodeByTxt(Model model,HttpSession session, VpsVcodeTicketInfo ticketInfo){
    	long time = System.currentTimeMillis();
        SysUserBasis user=getUserBasis(session);
        String userKey=user.getUserKey();
        Map<String, String> resurtMap = new HashMap<String, String>();
        ticketInfo.fillFields(userKey);
        try {
            resurtMap = ticketActivityCogService.addTicketInfoByTxt(ticketInfo);
        } catch (Exception e) {
        	// 券码重复
    		if(e.getMessage().contains("TICKET_CODE_IDX")){
    			resurtMap.put("errMsg", "导入失败：重复券码导入异常");
    		}else{
    			resurtMap.put("errMsg", "导入失败：券码导入异常");
    		}
            e.printStackTrace();
        }
        return JSON.toJSONString(resurtMap);
    }
    
}
