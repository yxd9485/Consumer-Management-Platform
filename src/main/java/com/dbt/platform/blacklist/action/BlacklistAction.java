package com.dbt.platform.blacklist.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.blacklist.bean.Blacklist;
import com.dbt.platform.blacklist.service.BlacklistService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 黑名单管理
 * 
 * @author:Jiquanwei<br>
 * @date:2015-7-8 上午09:43:31<br>
 * @version:1.0.0<br>
 * 
 */
@Controller
@RequestMapping("/blacklist")
public class BlacklistAction extends BaseAction {

	@Autowired
	private BlacklistService blacklistService;

	@RequestMapping("/manage")
	public String blacklistManage(HttpSession session, String queryParam, String pageParam, Model model) {

		SysUserBasis currentUser = this.getUserBasis(session);
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        Blacklist queryBean = new Blacklist(queryParam);
        
		List<Blacklist> blackList = blacklistService.findBlacklistByBlackType(queryBean, pageInfo);
		int countAll = blacklistService.countBlacklistByBlackType(queryBean);
		
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("blackList", blackList);
		model.addAttribute("showCount", countAll);
		model.addAttribute("startIndex", pageInfo.getStartCount());
		model.addAttribute("countPerPage", pageInfo.getPagePerCount());
		model.addAttribute("currentPage", pageInfo.getCurrentPage());
        model.addAttribute("orderCol", pageInfo.getOrderCol());
        model.addAttribute("orderType", pageInfo.getOrderType());
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);
		return "blacklist/showBlacklistList";
	}

	/**
	 * 移除黑名单及更新缓存
	 */
    @RequestMapping("/toDelete")
    public String toDelete(HttpSession session, Model model, String blacklistValue) throws IOException {
        try {
            SysUserBasis sysUser = getUserBasis(session);
            blacklistService.deleteBlacklist(blacklistValue, sysUser.getUserKey());
            model.addAttribute("refresh", "deleteSuccess");
        } catch (Exception e) {
            model.addAttribute("refresh", "deleteFalse");
            e.printStackTrace();
        }
        return "forward:manage.do";
    }
}
