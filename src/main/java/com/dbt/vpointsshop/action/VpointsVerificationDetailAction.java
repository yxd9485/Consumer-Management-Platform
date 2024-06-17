package com.dbt.vpointsshop.action;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsVerificationDetail;
import com.dbt.vpointsshop.bean.VpointsVerificationInfo;
import com.dbt.vpointsshop.service.VpointsVerificationDetailService;
import com.dbt.vpointsshop.service.VpointsVerificationInfoService;

/**
 * 积分商场实物奖兑换核销明细表Action
 */
@Controller
@RequestMapping("/verificationDetail")
public class VpointsVerificationDetailAction extends BaseAction{

	@Autowired
	private VpointsVerificationDetailService detailService;
	@Autowired
	private VpointsVerificationInfoService verificationInfoService;
	
	/**
	 * 获取核销列表
	 */
	@RequestMapping("/showVerificationDetailList")
	public String showVerificationDetailList(HttpSession session, 
	                            String pageParam, String verificationId, Model model) {
		try {
		    SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsVerificationDetail queryBean = new VpointsVerificationDetail();
            queryBean.setVerificationId(verificationId);
            
			List<VpointsVerificationDetail> resultList = detailService.queryForLst(queryBean, pageInfo);
			int countResult = detailService.queryForCount(queryBean);
			
			// 获取核销记录主表
			VpointsVerificationInfo verificationInfo 
			            = verificationInfoService.findById(verificationId);
            
            // 核销权限, admin及账务拥有
            String verificationFlag = validRole(session, "1", "3") ? "1" : "0";
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", "");
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("verificationInfo", verificationInfo);
            model.addAttribute("verificationFlag", verificationFlag);
            model.addAttribute("operationType", "search");
		} catch (Exception ex) {
		    log.error(ex);
		}
		return "vpointsGoods/showVerificationDetailList";
	}
	
	/**
	 * 获取核销预览列表
	 */
	@RequestMapping("/showPreviewVerificationDetailList")
	public String showPreviewVerificationDetailList(HttpSession session, 
			String pageParam,  String verificationEndDate, String brandKeys, Model model) {
		try {
			String errMsg = "";
		    SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsVerificationDetail queryBean = new VpointsVerificationDetail();
            queryBean.setVerificationEndDate(verificationEndDate);
            queryBean.setBrandKeys(brandKeys);

            // 获取核销记录主表
            VpointsVerificationInfo verificationInfo = 
            		verificationInfoService.findPreviewForVerificationInfo(verificationEndDate, brandKeys);
            if(null == verificationInfo){
            	errMsg ="操作失败，截止" + verificationEndDate + "没有未核销的已发货订单";
            }
            
            int countResult = detailService.queryPreviewForCount(queryBean);
			List<VpointsVerificationDetail> resultList = detailService.queryPreviewForLst(queryBean, pageInfo);
			
			if(null != verificationInfo){
				verificationInfo.setEndDate(verificationEndDate);
			}
            
            // 核销权限, admin及账务拥有
            String verificationFlag = validRole(session, "1", "3") ? "1" : "0";
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", "");
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("verificationInfo", verificationInfo);
            model.addAttribute("verificationFlag", verificationFlag);
            model.addAttribute("verificationEndDate", verificationEndDate);
            model.addAttribute("brandKeys", brandKeys);
            model.addAttribute("errMsg", errMsg);
            model.addAttribute("operationType", "preview");
		} catch (Exception ex) {
		    log.error(ex);
		}
		return "vpointsGoods/showVerificationDetailList";
	}
    
    /**
     * 核销表格明细下载
     * @return
     */
    @RequestMapping("/exportVerificationDetailList")
    public void exportVerificationDetailList(HttpSession session, 
                    HttpServletResponse response, String verificationId, String pageParam, Model model) {
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        try {
            // 导出
            String fileName = URLEncoder.encode("核销管理-已发货核销清单" + verificationId + "-", "UTF-8") + DateUtil.getDate() + ".xls";
            response.reset();
            response.setCharacterEncoding("GBK");
            response.setContentType("application/msexcel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename="+ fileName);
            detailService.exportVerificationDetailList(verificationId, pageInfo, response);
            response.flushBuffer();
        } catch (Exception e) {
            log.error("核销表格下载失败", e);
        }
    }
    
    /**
     * 预览核销表格明细下载
     * @return
     */
    @RequestMapping("/exportPreviewVerificationDetailList")
    public void exportPreviewVerificationDetailList(HttpSession session, 
    		HttpServletResponse response, String verificationEndDate, String brandKeys, String pageParam, Model model) {
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        try {
            // 导出
            String fileName = URLEncoder.encode("核销管理-预览核销清单截止日期-" + verificationEndDate + "-", "UTF-8") + DateUtil.getDate() + ".xls";
            response.reset();
            response.setCharacterEncoding("GBK");
            response.setContentType("application/msexcel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment; filename="+ fileName);
            detailService.exportPreviewVerificationDetailList(verificationEndDate, brandKeys, pageInfo, response);
            response.flushBuffer();
        } catch (Exception e) {
            log.error("核销表格下载失败", e);
        }
    }
	
}
