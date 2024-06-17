package com.dbt.platform.vdhInvitation.action;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExportUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;
import com.dbt.platform.invitationActivity.service.InvitationCodeService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationOrder;
import com.dbt.platform.vdhInvitation.service.VdhInvitationCodeService;
import com.dbt.platform.vdhInvitation.service.VdhInvitationOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vdhInvitationOrder")
public class VdhInvitationOrderAction extends BaseAction {

    @Autowired
    private VdhInvitationOrderService vdhInvitationOrderService;
    @Autowired
    private VdhInvitationCodeService vdhInvitationCodeService;

    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");
    private final static String LOCK_KEY = "vdhInvitationcCreateCdoe6cc19f8a7ed48f68f2a00b8cb2b992a";

    /**
     * 查询订单
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showOrderLst")
    public String showOrderLst(HttpSession session, Model model, String queryParam, String pageParam, String tabsFlag) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeVdhInvitationOrder queryBean = new VpsVcodeVdhInvitationOrder(queryParam);
            List<VpsVcodeVdhInvitationOrder> resultList = vdhInvitationOrderService.queryForList(queryBean, pageInfo);
            int countResult = vdhInvitationOrderService.queryForCount(queryBean);

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
            model.addAttribute("projectServerName", DbContextHolder.getDBType());
            model.addAttribute("tabsFlag", StringUtils.isBlank(tabsFlag) ? "0": tabsFlag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/vdhInvitation/showOrderLst";
    }

    /**
     * 新增订单
     */
    @ResponseBody
    @RequestMapping("/addOrder")
    public String addOrder(HttpSession session, Model model, VpsVcodeVdhInvitationOrder order){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            vdhInvitationOrderService.addOrder(order,currentUser);
            resultMap.put("errMsg", "创建订单成功");
        } catch (Exception ex) {
            resultMap.put("errMsg", "创建订单失败");
            ex.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 修改订单
     */
    @ResponseBody
    @RequestMapping("/editOrder")
    public String editOrder(HttpSession session, Model model, VpsVcodeVdhInvitationOrder order){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            String msg = vdhInvitationOrderService.editOrder(order, currentUser);
            resultMap.put("errMsg", StringUtils.isBlank(msg) ? "修改订单成功" : msg);
        } catch (Exception ex) {
            resultMap.put("errMsg", "修改订单失败");
            ex.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 删除订单
     * @param session
     * @param model
     * @param infoKey
     * @return
     */
    @RequestMapping("/deleteOrder")
    public String deleteOrder(HttpSession session, Model model, String infoKey){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            String msg = vdhInvitationOrderService.deleteOrder(infoKey);
            model.addAttribute("errMsg", StringUtils.isBlank(msg) ? "删除订单成功" : msg);
        }catch(Exception e){
            model.addAttribute("errMsg", "删除订单失败");
            e.printStackTrace();
        }
        return showOrderLst(session, model, null, null, null);
    }

    /**
     * 检查订单名称
     */
    @RequestMapping("/checkName")
    @ResponseBody
    public String checkName(HttpSession session, Model model, String checkName, String infoKey) {
        SysUserBasis currentUser = this.getUserBasis(session);
        return vdhInvitationOrderService.checkName(currentUser,checkName,infoKey);
    }

    /**
     * 生成邀请码
     * 订单状态0未生成1进行中2已生成3生成失败
     */
    @RequestMapping("/createCode")
    public String createCode(HttpSession session, Model model, VpsVcodeVdhInvitationOrder order, String projectServerName){
        SysUserBasis currentUser = this.getUserBasis(session);
        String lockId = "";
        try{
            VpsVcodeVdhInvitationOrder byId = vdhInvitationOrderService.findById(order.getInfoKey());
            if (byId!=null){
                if ("1".equals(byId.getStatus()) || "2".equals(byId.getStatus())){
                    model.addAttribute("errMsg", "1".equals(byId.getStatus()) ? "该订单正在生成码源":"该订单已生成邀请码，不能重复生成码源");
                }else{
                    lockId = RedisApiUtil.getInstance().tryGetDistributedLock(LOCK_KEY, projectServerName, 1000 * 60 * 30);
                    if (lockId == null){
                        model.addAttribute("errMsg", "请稍后生成，有其他订单正在生成码源。");
                    }else{
                        // 更新订单状态为生成中
                        vdhInvitationOrderService.updateOrderStatus(byId, currentUser, "1");
                        vdhInvitationCodeService.addCode(currentUser,byId);
                        model.addAttribute("errMsg", "生成邀请码成功");
                    }
                }
            }
        } catch (Exception ex) {
            vdhInvitationOrderService.updateOrderStatus(order, currentUser, "3");
            model.addAttribute("errMsg", "生成邀请码失败");
            ex.printStackTrace();
        }finally {
            if (StringUtils.isNotBlank(lockId)){
                RedisApiUtil.getInstance().releaseDistributedLock(LOCK_KEY, lockId, projectServerName);
            }
        }
        return showOrderLst(session, model, null, null, null);
    }

    /**
     * 下载码包文件
     */
    @GetMapping("/exportCode")
    public String downloadCode(HttpServletRequest request, HttpServletResponse response, String projectServerName, String orderKey) {
        try {
            DbContextHolder.setDBType(projectServerName);
            VpsVcodeVdhInvitationOrder orderInfo = vdhInvitationOrderService.findById(orderKey);
            String downLoadFileName = orderInfo.getOrderName() + "_" +orderInfo.getOrderNum();
            String path = COMMON_PATH + "/vdhInviteActivity" + "/" + projectServerName + "/" + orderInfo.getInfoKey() + "/4compress/" + downLoadFileName + ".zip";

            File file = new File(path);
            //文件判断
            if (!file.exists()) {
                System.out.println("file not found");
                outPrint(request, response);
            } else {
                response.setContentType("application/msexcel;charset=UTF-8");
                response.setHeader("Content-disposition", "attachment; filename=invoice.xls");
                ExportUtil.download(file, request, response);
            }
        } catch (Exception e) {
            try {
                outPrint(request, response);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void outPrint(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //去除页面乱码
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            StringBuilder builder = new StringBuilder();
            builder.append("<script type=\'text/javascript\' charset=\'UTF-8\'>");
            builder.append("alert(\'暂无数据\');window.close();");
            builder.append("</script>");
            out.print(builder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
