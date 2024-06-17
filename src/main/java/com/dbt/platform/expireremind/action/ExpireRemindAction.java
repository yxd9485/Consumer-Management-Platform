package com.dbt.platform.expireremind.action;

import com.dbt.framework.base.action.BaseAction;

import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.doubtuser.bean.VcodeBlacklistAccount;
import com.dbt.platform.expireremind.bean.VpsMsgExpireRemindInfo;
import com.dbt.platform.expireremind.service.VpsExpireRemindService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;


@Controller
@RequestMapping("/expireRemindAction")
public class ExpireRemindAction extends BaseAction {

    @Autowired
    VpsExpireRemindService remindService;

    @Autowired
    VpsExpireRemindService expireRemindService;

    public void countNum(HttpSession session, Model model) {

        int count = expireRemindService.conutMsgExpireRemind();
        model.addAttribute("count", count);

    }

    @RequestMapping("/expireRemindList")
    public String expireRemindList(HttpSession session, Model model, String pageParam) {
        SysUserBasis currentUser = this.getUserBasis(session);
        HashMap<String, Object> map = new HashMap<String, Object>();
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        if (!StringUtils.isEmpty(String.valueOf(pageInfo.getStartCount()))) {
            pageInfo.setStartCount(0);

        }
        if (!StringUtils.isEmpty(String.valueOf(pageInfo.getPagePerCount()))) {
            pageInfo.setPagePerCount(15);

        }
        map.put("pageInfo", pageInfo);
        try {
            List<VpsMsgExpireRemindInfo> msgList = remindService.selectByActivityParamer(map);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("msgList", msgList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "expireRemind/showExpireRemindList";
    }

    @RequestMapping("/updateStatusForRead")
    public void updateStatusForRead(String keys, HttpSession session, String status) {

        remindService.updateStatusForRead(keys, status);

    }

    @RequestMapping("/stickMsg")
    public String  stickMsg(String infoKey,Model model,String topFlag ){

        remindService.stickMsg(infoKey,topFlag);
        return "forward:/expireRemindAction/expireRemindList";
    }
    @RequestMapping("/expireRemindListCondition")
    public String expireRemindListCondition(HttpSession session, Model model, String pageParam,String queryParam) {
        SysUserBasis currentUser = this.getUserBasis(session);
        HashMap<String, Object> map = new HashMap<String, Object>();
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        VpsMsgExpireRemindInfo  remindInfo= new VpsMsgExpireRemindInfo(queryParam);
        if (StringUtils.isEmpty(String.valueOf(pageInfo.getStartCount()))) {
            pageInfo.setStartCount(0);
        }
        if (StringUtils.isEmpty(String.valueOf(pageInfo.getPagePerCount()))) {
            pageInfo.setPagePerCount(15);
        }
        map.put("pageInfo", pageInfo);
        map.put("remindInfo", remindInfo);

        try {
            List<VpsMsgExpireRemindInfo> msgList = remindService.selectByActivityParamerCondition(map);
           int  countResult =  remindService.conutMsgExpireReminds(map);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("msgList", msgList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
             model.addAttribute("orderType", pageInfo.getOrderType());
         /*      model.addAttribute("queryBean", queryBean);*/
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "expireRemind/showExpireRemindList";
    }
}
