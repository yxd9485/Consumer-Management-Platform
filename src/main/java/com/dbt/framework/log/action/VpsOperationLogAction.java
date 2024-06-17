package com.dbt.framework.log.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.dbt.platform.system.service.SysUserBasisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.log.bean.VpsOperationLog;
import com.dbt.framework.log.service.VpsOperationLogService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 操作日志Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/vpsOperationLog")
public class VpsOperationLogAction extends BaseAction{

	@Autowired
	private VpsOperationLogService vpsOperationLogService;

    @Autowired
    SysUserBasisService sysUserBasisService;

	@RequestMapping("/showVpsOperationLogList")
	public String showVpsOperationLogList(HttpSession session,
	        String pageParam, VpsOperationLog queryBean, String pageFlag, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			if(StringUtils.isBlank(pageFlag)){
				String date = DateUtil.getDate();
				if(null == queryBean){
					queryBean = new VpsOperationLog();
				}
				queryBean.setStartDate(date);
				queryBean.setEndDate(date);
			}

			List<VpsOperationLog> resultList =
					vpsOperationLogService.queryVpsOperationLogList(queryBean, pageInfo);

           List<SysUserBasis> listUser= sysUserBasisService.queryUserBasisList();
			int countResult = vpsOperationLogService.queryVpsOperationLogCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("listUser", listUser);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("queryBean", queryBean);
			model.addAttribute("pageParam", pageParam);
			model.addAttribute("menuMap", Constant.menuMap);
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            //model.addAttribute("queryParam", queryParam);
            //model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "log/showVpsOperationLogList";
	}
}
