package com.dbt.platform.wechatmovement.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.wechatmovement.bean.VpsWechatActivityBasicInfo;
import com.dbt.platform.wechatmovement.service.VpsWechatActivityBasicInfoService;

/**
 * 文件名：VcodeQrcodeBatchInfoAction.java<br>
 * 描述: Popss活动基础信息 <br>
 * 修改人: zzy<br>
 * 修改时间：2018/6/13 13:56<br>
 * 修改内容：<br>
 */
@Controller
@RequestMapping("/vpsWechatMovementActivityBaseInfo")
public class VpsWechatMovementActivityBaseInfoAction extends BaseAction {

    @Autowired
    VpsWechatActivityBasicInfoService popssService;

    /**
     * 获取最近一条记录
     * @param session
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showPopssActivityInfo")
    public String showPopssActivityInfo(HttpSession session, String pageParam, Model model) {
        try {
            VpsWechatActivityBasicInfo popssActivityBaseInfo =  popssService.findWechatActivityBasicInfo();
            model.addAttribute("popssInfo", popssActivityBaseInfo);
        }catch(Exception e){
            log.error(e);
        }
        return "popss/popssActiviteBaseInfo";
    }
    
    /**
     * 获取记录List
     * @param session
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showPopssActivityInfoList")
    public String showPopssActivityInfoList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
        	SysUserBasis currentUser = this.getUserBasis(session);
        	PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            List<VpsWechatActivityBasicInfo> popssInfoList =  popssService.queryPopssActivityInfoList(pageInfo);
            int countResult = popssService.queryPopssActivityInfoCount(pageInfo);
            model.addAttribute("showCount", countResult);
            model.addAttribute("popssInfoList", popssInfoList);
            model.addAttribute("currentUser", currentUser);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
        }catch(Exception e){
            log.error(e);
        }
        return "popss/showPopssActiviteBaseList";
    }

    /**
     * @author zzy
     * @createTime 2018/6/26 16:02
     * @description:保存或更新Popss活动基本信息
     */
    @RequestMapping("/savePopssActivityInfo")
    public String savePopssActivityInfo(HttpSession session, VpsWechatActivityBasicInfo 
    		popssActivityBaseInfo, String oldCurrencyPrice, String oldActivityMagnification,String oldBonusPool, Model model) {
        try {
            popssActivityBaseInfo.fillFields(getUserBasis(session).getUserKey());
            popssActivityBaseInfo.setPhoneNum(getUserBasis(session).getPhoneNum());
            popssService.savePopssActivityBaseInfo(popssActivityBaseInfo, oldCurrencyPrice, oldActivityMagnification,oldBonusPool);
        } catch (Exception e) {
            log.error(e);
        }
        return showPopssActivityInfo(session, null, model);
    }
}
