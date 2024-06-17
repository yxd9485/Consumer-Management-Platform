package com.dbt.platform.ticket.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.ticket.bean.VpsVcodeTicketExchangeCog;
import com.dbt.platform.ticket.bean.VpsVcodeTicketUserRecord;
import com.dbt.platform.ticket.service.VpsSysTicketInfoService;
import com.dbt.platform.ticket.service.VpsVcodeTicketExchangeService;
import com.dbt.platform.ticket.service.VpsVcodeTicketUserRecordService;
import com.dbt.platform.turntable.bean.VpsTurntablePacksRecord;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 积分兑换三方优惠券action
 */

@Controller
@RequestMapping("/vcodeTicketExchange")
public class VpsVcodeTicketExchangeAction extends BaseAction {
    @Autowired
    private VpsVcodeTicketExchangeService vpsVcodeTicketExchangeService;
    @Autowired
    private VpsSysTicketInfoService sysTicketInfoService;
    @Autowired
    private VpsVcodeTicketUserRecordService vpsVcodeTicketUserRecordService;


    /**
     * 积分兑换优惠券列表
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showTicketExchangeList")
    public String showTicketExchangeList(HttpSession session,
                                         String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeTicketExchangeCog queryBean = new VpsVcodeTicketExchangeCog(queryParam);
            List<VpsVcodeTicketExchangeCog> resultList =
                    vpsVcodeTicketExchangeService.queryForList(queryBean, pageInfo);
            int countResult = vpsVcodeTicketExchangeService.queryForCount(queryBean);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("nowTime", new LocalDate());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/ticket/showVcodeTicketExchangeList";
    }


    /**
     * 添加积分兑换优惠券页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showTicketExchangeAdd")
    public String showTicketActivityAdd(HttpSession session, Model model) {

        Map<String, String> stringStringMap = sysTicketInfoService.queryAllTicket();
        model.addAttribute("stringStringMap", stringStringMap);
        return "vcode/ticket/showTicketExchangeAdd";
    }

    /**
     * 添加积分兑换优惠券
     *
     * @param session
     * @param activityCog
     * @param model
     * @return
     */
    @RequestMapping("/doTicketExchangeAdd")
    public String doTicketExchangeAdd(HttpSession session, VpsVcodeTicketExchangeCog ticketExchangeCog,
                                      Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsVcodeTicketExchangeService.doTicketExchangeAdd(ticketExchangeCog ,currentUser);
            model.addAttribute("errMsg", "保存成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "保存失败");
            ex.printStackTrace();
        }
        return "forward:showTicketExchangeList.do";
    }

    /**
     * 修改积分兑换优惠券页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showTicketExchangeEdit")
    public String showTicketExchangeEdit(HttpSession session,String infoKey, Model model) {

        Map<String, String> stringStringMap = sysTicketInfoService.queryAllTicket();
        VpsVcodeTicketExchangeCog ticketExchangeCog = vpsVcodeTicketExchangeService.findById(infoKey);
        model.addAttribute("stringStringMap", stringStringMap);
        model.addAttribute("ticketExchangeCog", ticketExchangeCog);
        return "vcode/ticket/showTicketExchangeEdit";
    }

    /**
     * 修改积分兑换优惠券
     *
     * @param session
     * @param activityCog
     * @param model
     * @return
     */
    @RequestMapping("/doTicketExchangeEdit")
    public String doTicketExchangeEdit(HttpSession session, VpsVcodeTicketExchangeCog ticketExchangeCog,
                                      Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsVcodeTicketExchangeService.doTicketExchangeEdit(ticketExchangeCog ,currentUser);
            model.addAttribute("errMsg", "修改成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            ex.printStackTrace();
        }
        return "forward:showTicketExchangeList.do";
    }

    /**
     * 积分兑换优惠券数据页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showTicketExchangeData")
    public String showTicketExchangeData(HttpSession session,
                                         String queryParam, String pageParam, Model model,String infoKey) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeTicketUserRecord queryBean = new VpsVcodeTicketUserRecord(queryParam);
            List<VpsVcodeTicketUserRecord> resultList =  vpsVcodeTicketUserRecordService.queryList(queryBean,pageInfo,infoKey);
            int countResult = vpsVcodeTicketUserRecordService.queryForCount(queryBean,infoKey);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("nowTime", new LocalDate());
            model.addAttribute("infoKey", infoKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/ticket/showTicketExchangeData";


    }

    /**
     * 删除优惠券
     *
     * @param session
     * @param activityCog
     * @param model
     * @return
     */
    @RequestMapping("/doTicketExchangeDelete")
    public String doTicketExchangeDelete(HttpSession session, String infoKey,
                                       Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsVcodeTicketExchangeService.doTicketExchangeDelete(infoKey ,currentUser);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            ex.printStackTrace();
        }
        return "forward:showTicketExchangeList.do";
    }

    /**
     * 导出查询结果
     *
     * @return
     */
    @RequestMapping("/exportTicketExchangeRecordList")
    public void exportTicketExchangeRecordList(HttpSession session, HttpServletResponse response, String infoKey,
                                          String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = getUserBasis(session);
            VpsVcodeTicketUserRecord queryBean = new VpsVcodeTicketUserRecord(queryParam);
            // 导出
            vpsVcodeTicketUserRecordService.exportTicketExchangeRecordList(infoKey, queryBean, response);
        } catch (Exception e) {
            log.error("导出查询结果下载失败", e);
        }
    }
}
