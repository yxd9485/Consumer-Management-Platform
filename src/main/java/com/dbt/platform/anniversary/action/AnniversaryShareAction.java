package com.dbt.platform.anniversary.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.anniversary.bean.VpsAnniversaryShare;
import com.dbt.platform.anniversary.service.AnniversaryService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author: LiangRunBin
 * @create-date: 2023/12/21 17:46
 */

@Controller
@RequestMapping("/anniversaryShare")
public class AnniversaryShareAction extends BaseAction {


    @Autowired
    AnniversaryService anniversaryService;

    @RequestMapping("/showAnniversaryShareList")
    public String showAnniversaryShareList(HttpSession session, String pageParam, String queryParam, VpsAnniversaryShare queryBean, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            List<VpsAnniversaryShare> resultLst = anniversaryService.queryShareForList(queryBean, pageInfo);
            int countResult = anniversaryService.queryShareForCount(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultLst);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "vcode/anniversaryShare/showAnniversaryShareList";
    }

    @RequestMapping("/showAnniversaryShareAdd")
    public String showAnniversaryShareAdd(HttpSession session, String pageParam, String queryParam, VpsAnniversaryShare queryBean, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            List<VpsAnniversaryShare> resultLst = anniversaryService.queryShareForList(queryBean, pageInfo);
            int countResult = anniversaryService.queryShareForCount(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultLst);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "vcode/anniversaryShare/showAnniversaryShareAdd";
    }

    @RequestMapping("/showAnniversaryShareEdit")
    public String showAnniversaryShareEdit(HttpSession session, String shareKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsAnniversaryShare vpsAnniversaryShare = anniversaryService.queryByKey(shareKey);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("vpsAnniversaryShare", vpsAnniversaryShare);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "vcode/anniversaryShare/showAnniversaryShareEdit";
    }

    @RequestMapping("/doAnniversaryShareEdit")
    public String doAnniversaryShareEdit(HttpSession session, VpsAnniversaryShare vpsAnniversaryShare, Model model) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            anniversaryService.updateByKey(vpsAnniversaryShare, currentUser);
            errMsg = "编辑成功";
        } catch (Exception ex) {
            errMsg = "编辑失败";
            ex.printStackTrace();
        }
        model.addAttribute("errMsg", errMsg);
        return showAnniversaryShareList(session, null, null, new VpsAnniversaryShare(), model);
    }

    @RequestMapping("/doAnniversaryShareAdd")
    public String doAnniversaryShareAdd(HttpSession session, VpsAnniversaryShare vpsAnniversaryShare, Model model) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            anniversaryService.insertAnniversaryShare(vpsAnniversaryShare, currentUser);
            errMsg = "保存成功";
        } catch (Exception ex) {
            errMsg = "保存失败";
            ex.printStackTrace();
        }
        model.addAttribute("errMsg", errMsg);
        return showAnniversaryShareList(session, null, null, new VpsAnniversaryShare(), model);
    }

    @RequestMapping("/doAnniversaryShareDelete")
    public String doAnniversaryShareDelete(HttpSession session, String shareKey, Model model) {
        String errMsg = "";
        try {
            anniversaryService.deleteAnniversaryShare(shareKey);
            errMsg = "删除成功";
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            errMsg = "删除失败";
            model.addAttribute("errMsg", errMsg);
            ex.printStackTrace();
        }
        model.addAttribute("errMsg", errMsg);
        return "forward:/anniversaryShare/showAnniversaryShareList.do";
    }


}
