package com.dbt.platform.invitationActivity.action;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExportUtil;
import com.dbt.framework.util.PropertiesUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationActivityCog;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationOrder;
import com.dbt.platform.invitationActivity.service.InvitationCodeService;
import com.dbt.platform.invitationActivity.service.InvitationOrderService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.vjifen.server.base.datasource.redis.RedisUtils;
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
@RequestMapping("/invitationOrder")
public class InvitationOrderAction extends BaseAction {

    @Autowired
    private InvitationOrderService invitationOrderService;
    @Autowired
    private InvitationCodeService invitationCodeService;

    private final static String COMMON_PATH = PropertiesUtil.getPropertyValue("create_code_common_path");

    /**
     * 查询二维码订单
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showOrderLst")
    public String showOrderLst(HttpSession session, Model model, String queryParam, String pageParam, String activityKey, String tabsFlag) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeInvitationOrder queryBean = new VpsVcodeInvitationOrder(queryParam,activityKey);
            List<VpsVcodeInvitationOrder> resultList = invitationOrderService.queryForList(queryBean, pageInfo);
            int countResult = invitationOrderService.queryForCount(queryBean);

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
            model.addAttribute("projectServerName", DbContextHolder.getDBType());
            model.addAttribute("tabsFlag", StringUtils.isBlank(tabsFlag) ? "0": tabsFlag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/invitationWithGifts/showOrderLst";
    }

    /**
     * 新增订单
     */
    @ResponseBody
    @RequestMapping("/addOrder")
    public String addOrder(HttpSession session, Model model, VpsVcodeInvitationOrder order){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            invitationOrderService.addOrder(order,currentUser);
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
    public String editOrder(HttpSession session, Model model, VpsVcodeInvitationOrder order){
        Map<String, Object> resultMap = new HashMap<>();
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            String msg = invitationOrderService.editOrder(order, currentUser);
            resultMap.put("errMsg", StringUtils.isBlank(msg) ? "修改订单成功" : msg);
        } catch (Exception ex) {
            resultMap.put("errMsg", "修改订单失败");
            ex.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 生成邀请码
     * 订单状态0未生成1进行中2已生成3生成失败
     */
    @RequestMapping("/createCode")
    public String createCode(HttpSession session, Model model, VpsVcodeInvitationOrder order, String projectServerName){
        SysUserBasis currentUser = this.getUserBasis(session);
        String lockKey = "";
        String lockId = "";
        try{
            VpsVcodeInvitationOrder byId = invitationOrderService.findById(order.getInfoKey());
            if (byId!=null){
                if ("1".equals(byId.getStatus()) || "2".equals(byId.getStatus())){
                    model.addAttribute("errMsg", "1".equals(byId.getStatus()) ? "无法生成邀请码原因：正在生成中":"无法生成邀请码原因：已生成");
                }else{
                    lockKey = byId.getActivityKey();
                    lockId = RedisApiUtil.getInstance().tryGetDistributedLock(lockKey, projectServerName, 1000 * 60 * 30);
                    if (lockId == null){
                        model.addAttribute("errMsg", "请稍后生成，订单所属活动下有邀请码正在生成中");
                    }else{
                        // 更新订单状态为生成中
                        VpsVcodeInvitationOrder orderTemp = new VpsVcodeInvitationOrder();
                        orderTemp.setInfoKey(byId.getInfoKey());
                        orderTemp.setStatus("1");
                        orderTemp.setUpdateUser(currentUser.getUserName());
                        orderTemp.setUpdateTime(DateUtil.getDateTime());
                        invitationOrderService.updateOrder(orderTemp);
                        invitationCodeService.addCode(currentUser,byId);
                        model.addAttribute("errMsg", "生成邀请码成功");
                    }
                }
            }
        } catch (Exception ex) {
            VpsVcodeInvitationOrder orderTemp = new VpsVcodeInvitationOrder();
            orderTemp.setInfoKey(order.getInfoKey());
            orderTemp.setStatus("3");
            orderTemp.setUpdateUser(currentUser.getUserName());
            orderTemp.setUpdateTime(DateUtil.getDateTime());
            invitationOrderService.updateOrder(orderTemp);
            model.addAttribute("errMsg", "生成邀请码失败");
            ex.printStackTrace();
        }finally {
            if (StringUtils.isNotBlank(lockId)){
                RedisApiUtil.getInstance().releaseDistributedLock(lockKey, lockId, projectServerName);
            }
        }
        return showOrderLst(session, model, null, null, order.getActivityKey(),null);
    }

    /**
     * 下载码包文件
     */
    @GetMapping("/exportCode")
    public String downloadCode(HttpServletRequest request, HttpServletResponse response, String projectServerName, String orderKey) {
        try {
            DbContextHolder.setDBType(projectServerName);
            VpsVcodeInvitationOrder orderInfo = invitationOrderService.findById(orderKey);
            // 订单创建时间
            String date = orderInfo.getCreateTime().split(" ")[0].replaceAll("-", "");
            String downLoadFileName = orderInfo.getOrderName() + "_" +orderInfo.getQrcodeNum();
            //格式：/data/upload/autocode/inviteActivity/shandongagt/活动主键/订单主键
            String path = COMMON_PATH + "/inviteActivity" + "/" + projectServerName
                    + "/" + orderInfo.getActivityKey() + "/" + orderInfo.getInfoKey() + "/4compress/" + downLoadFileName + ".zip";
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

    /**
     * 删除订单
     * @param session
     * @param model
     * @param infoKey
     * @return
     */
    @RequestMapping("/deleteOrder")
    public String deleteOrder(HttpSession session, Model model, String infoKey, String activityKey){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            String msg = invitationOrderService.deleteOrder(infoKey);
            model.addAttribute("errMsg", StringUtils.isBlank(msg) ? "删除订单成功" : msg);
        }catch(Exception e){
            model.addAttribute("errMsg", "删除订单失败");
            e.printStackTrace();
        }
        return showOrderLst(session, model, null, null, activityKey,null);
    }

    /**
     * 检验活动名称是否重复
     */
    @RequestMapping("/checkName")
    @ResponseBody
    public String checkName(HttpSession session, Model model, String checkName, String infoKey, String activityKey) {
        SysUserBasis currentUser = this.getUserBasis(session);
        return invitationOrderService.checkName(currentUser,checkName,infoKey,activityKey);
    }

    public void outPrint(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
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
