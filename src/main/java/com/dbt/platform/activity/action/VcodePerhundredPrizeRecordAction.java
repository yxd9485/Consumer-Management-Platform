package com.dbt.platform.activity.action;
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
import com.dbt.platform.activity.bean.VcodePerhundredPrizeRecord;
import com.dbt.platform.activity.service.VcodePerhundredPrizeRecordService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 逢百中奖纪录Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/activityPerhundredPrizeRecord")
public class VcodePerhundredPrizeRecordAction extends BaseAction{

	@Autowired
	private VcodePerhundredPrizeRecordService perhundredPrizeRecordService;
	
	/**
	 * 逢百中奖详情List
	 */
	@RequestMapping("/showPerhundredPrizeRecordList")
	public String showPerhundredPrizeRecordList(HttpSession session, 
			String perhundredKey, String queryParam, String pageParam, Model model){
		try{
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodePerhundredPrizeRecord queryBean = new VcodePerhundredPrizeRecord(queryParam);
            queryBean.setPerhundredKey(perhundredKey);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
			List<VcodePerhundredPrizeRecord> resultList 
				= perhundredPrizeRecordService.queryForList(queryBean, pageInfo, true);
			int countResult = perhundredPrizeRecordService.queryForCount(queryBean);
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
		return "vcode/perhundred/showPerhundredPrizeRecordList";
	}
	
	/**
     * 导出
     * @return
     */
    @RequestMapping("/exportPerhundredPrizeRecord")
    public void exportPerhundredPrizeRecord(HttpSession session, HttpServletResponse response,
			String perhundredKey, String queryParam, String pageParam, Model model) {
        try {

        	String fileName = URLEncoder.encode("逢百中奖名单列表", "UTF-8") + DateUtil.getDate() + ".xls";
        	response.reset();
        	response.setCharacterEncoding("GBK");
        	response.setContentType("application/msexcel;charset=UTF-8");
    		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
    		
        	PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodePerhundredPrizeRecord queryBean = new VcodePerhundredPrizeRecord(queryParam);
            queryBean.setPerhundredKey(perhundredKey);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
    		perhundredPrizeRecordService.exportPerhundredPrizeRecord(queryBean, pageInfo, response);
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
