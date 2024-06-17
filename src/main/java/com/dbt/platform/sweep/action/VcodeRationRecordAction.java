package com.dbt.platform.sweep.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.sweep.bean.VpsVcodeRationRecord;
import com.dbt.platform.sweep.service.VcodeRationRecordService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 提现记录Action
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2017年12月1日 </br>
 */

@Controller
@RequestMapping("/vcodeRationRecord")
public class VcodeRationRecordAction extends BaseAction {

	@Autowired
	private VcodeRationRecordService vcodeRationRecordService;

	/**
	 * 提现列表
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showUserRationRecordList")
	public String showVcodeActivityList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			VpsVcodeRationRecord queryBean = new VpsVcodeRationRecord(queryParam);
			if(StringUtils.isEmpty(queryBean.getStartDate())){
			    queryBean.setStartDate(DateUtil.getDate());
			    queryParam = "," + queryBean.getStartDate();
			    model.addAttribute("queryBean", queryBean);
	            model.addAttribute("queryParam", queryParam);
                return "userinfo/showUserRationRecordList";
			} 
            if(StringUtils.isNotBlank(queryBean.getPaymentNo())){
                pageInfo.setPagePerCount(1);
            }else{
                pageInfo.setPagePerCount(20);
            }
			
			List<VpsVcodeRationRecord> rationRecordList = 
					vcodeRationRecordService.queryList(queryBean, pageInfo);
			int countResult = vcodeRationRecordService.queryCount(queryBean);
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", rationRecordList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("queryBean", queryBean);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
			model.addAttribute("nowTime", new LocalDate());
			model.addAttribute("serverName", DbContextHolder.getDBType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "userinfo/showUserRationRecordList";
	}
	
}
