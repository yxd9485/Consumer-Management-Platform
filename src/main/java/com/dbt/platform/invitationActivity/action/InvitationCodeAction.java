package com.dbt.platform.invitationActivity.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationCode;
import com.dbt.platform.invitationActivity.service.InvitationCodeService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/invitationCode")
public class InvitationCodeAction extends BaseAction {

    @Autowired
    private InvitationCodeService invitationCodeService;

    /**
     * 查询邀请码列表
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showCodeLst")
    public String showOrderLst(HttpSession session, Model model, String queryParam, String pageParam, String activityKey, String tabsFlag) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeInvitationCode queryBean = new VpsVcodeInvitationCode(queryParam,activityKey,tabsFlag);
            List<VpsVcodeInvitationCode> resultList = invitationCodeService.queryForList(queryBean, pageInfo);
            int countResult = invitationCodeService.queryForCount(queryBean);

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
            model.addAttribute("activityKey", activityKey);
            model.addAttribute("tabsFlag", StringUtils.isBlank(tabsFlag) ? "1": tabsFlag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/invitationWithGifts/showCodeLst";
    }

    /**
     * 导出邀请码
     */
    @RequestMapping("/uploadCode")
    public void uploadCode(HttpServletResponse response, VpsVcodeInvitationCode queryBean, String tabsFlag, String uploadCodeType){
        try{
            queryBean.setCodeType("1".equals(tabsFlag) ? "0" : "1");
            invitationCodeService.uploadCode(response,queryBean,uploadCodeType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
