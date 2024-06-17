package com.dbt.platform.permantissa.action;
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
import com.dbt.platform.permantissa.bean.VpsVcodePerMantissaPrizeRecord;
import com.dbt.platform.permantissa.service.VpsVcodePerMantissaPrizeRecordService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 逢尾数中奖纪录Action
 */
@Controller
@RequestMapping("/perMantissaPrizeRecord")
public class VpsVcodePerMantissaPrizeRecordAction extends BaseAction{

	@Autowired
	private VpsVcodePerMantissaPrizeRecordService perMantissaPrizeRecordService;
	
	/**
	 * 逢尾数中奖详情List
	 */
	@RequestMapping("/showPerMantissaPrizeRecordList")
	public String showPerMantissaPrizeRecordList(HttpSession session, 
			String perMantissaKey, String queryParam, String pageParam, Model model){
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodePerMantissaPrizeRecord queryBean = new VpsVcodePerMantissaPrizeRecord(queryParam);
            queryBean.setPerMantissaKey(perMantissaKey);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
			List<VpsVcodePerMantissaPrizeRecord> resultList = 
			        perMantissaPrizeRecordService.queryForList(queryBean, pageInfo, true);
			int countResult = perMantissaPrizeRecordService.queryForCount(queryBean);
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/permantissa/showPerMantissaPrizeRecordList";
	}
	
	/**
     * 导出
     * @return
     */
    @RequestMapping("/exportPerMantissaPrizeRecord")
    public void exportPerMantissaPrizeRecord(HttpSession session, HttpServletResponse response,
			                        String perMantissaKey, String queryParam, String pageParam, Model model) {
        try {

        	String fileName = URLEncoder.encode("逢尾数中奖名单列表", "UTF-8") + DateUtil.getDate() + ".xls";
        	response.reset();
        	response.setCharacterEncoding("GBK");
        	response.setContentType("application/msexcel;charset=UTF-8");
    		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
    		
    		SysUserBasis currentUser = this.getUserBasis(session);
        	PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        	VpsVcodePerMantissaPrizeRecord queryBean = new VpsVcodePerMantissaPrizeRecord(queryParam);
            queryBean.setPerMantissaKey(perMantissaKey);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
    		perMantissaPrizeRecordService.exportPrizeRecord(queryBean, pageInfo, response);
            response.flushBuffer();
        } catch (Exception e) {
            model.addAttribute("errMsg", "导出明细失败！");
            e.printStackTrace();
        }
    }
}
