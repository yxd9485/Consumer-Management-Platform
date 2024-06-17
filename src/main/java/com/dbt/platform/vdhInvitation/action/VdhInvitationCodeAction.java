package com.dbt.platform.vdhInvitation.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationCode;
import com.dbt.platform.vdhInvitation.service.VdhInvitationCodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/vdhInvitationCode")
public class VdhInvitationCodeAction extends BaseAction {

    @Autowired
    private VdhInvitationCodeService vdhInvitationCodeService;

    /**
     * 查询邀请码列表
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showCodeLst")
    public String showOrderLst(HttpSession session, Model model, String queryParam, String pageParam, String tabsFlag) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeVdhInvitationCode queryBean = new VpsVcodeVdhInvitationCode(queryParam);
            List<VpsVcodeVdhInvitationCode> resultList = vdhInvitationCodeService.queryForList(queryBean, pageInfo);
            int countResult = vdhInvitationCodeService.queryForCount(queryBean);

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
            model.addAttribute("tabsFlag", StringUtils.isBlank(tabsFlag) ? "1": tabsFlag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/vdhInvitation/showCodeLst";
    }

    /**
     * 导出邀请码
     */
    @RequestMapping("/uploadCode")
    public void uploadCode(HttpServletResponse response, VpsVcodeVdhInvitationCode queryBean, String uploadCodeType){
        try{
            vdhInvitationCodeService.uploadCode(response,queryBean,uploadCodeType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
