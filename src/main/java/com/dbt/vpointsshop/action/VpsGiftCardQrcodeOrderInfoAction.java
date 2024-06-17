package com.dbt.vpointsshop.action;


import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExportUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpsGiftCardQrcodeOrderInfo;
import com.dbt.vpointsshop.dto.VpsGiftCardQrcodeOrderInfoVO;
import com.dbt.vpointsshop.service.IVpsGiftCardQrcodeOrderInfoService;
import com.vjifen.server.base.datasource.redis.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.dbt.framework.base.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 礼品卡卡号订单表 前端控制器
 * </p>
 *
 * @author wangshuda
 * @since 2022-08-15
 */
@Controller
@RequestMapping("/giftCardQrcodeOrderAction")
public class VpsGiftCardQrcodeOrderInfoAction extends BaseAction {
    @Autowired
    private IVpsGiftCardQrcodeOrderInfoService giftCardQrcodeOrderInfoService;

    /**
     * 列表
     */
    @RequestMapping("/showGiftCardQrcodeOrderList")
    public String showGiftCardQrcodeOrderList(HttpSession session, String pageParam, String queryParam,String giftCardInfoKey,  Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsGiftCardQrcodeOrderInfoVO queryBean = new VpsGiftCardQrcodeOrderInfoVO(queryParam);
            queryBean.setGiftCardInfoKey(giftCardInfoKey);
            List<VpsGiftCardQrcodeOrderInfoVO> resultLst = giftCardQrcodeOrderInfoService.queryForLst(queryBean, pageInfo);
            int countResult = giftCardQrcodeOrderInfoService.queryForCount(queryBean);

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
            model.addAttribute("giftCardInfoKey", giftCardInfoKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vpointsGoods/giftCard/showGiftCardQrcodeOrderList";
    }

    /**
     * 添加
     */
    @ResponseBody
    @RequestMapping("/doGiftCardQrcodeOrderAdd")
    public String doGiftCardQrcodeOrderAdd(HttpSession session, VpsGiftCardQrcodeOrderInfoVO orderInfo, Model model) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            giftCardQrcodeOrderInfoService.addOrderInfo(orderInfo, currentUser.getUserKey());
            resultMap.put("errMsg", "添加成功");

        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());

        } catch (Exception e) {
            resultMap.put("errMsg", "添加失败");
            log.error("兑付卡码源订单创建失败", e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/doGiftCardQrcodeOrderEdit")
    public String doBatchInfoEdit(HttpSession session, VpsGiftCardQrcodeOrderInfo orderInfo, Model model) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            orderInfo.setUpdateUser(currentUser.getUserKey());
            orderInfo.setUpdateTime(DateUtil.getNow());
            resultMap.put("errMsg",giftCardQrcodeOrderInfoService.updateOrderInfo(orderInfo));

        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());

        } catch (Exception e) {
            resultMap.put("errMsg", "修改失败");
            log.error("兑付卡码源订单修改失败", e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 获取批次信息
     */
    @ResponseBody
    @RequestMapping("/findGiftCardQrcodeOrder")
    public String findGiftCardQrcodeOrder(HttpSession session, String batchKey, Model model) {
        VpsGiftCardQrcodeOrderInfoVO orderInfoVO = new VpsGiftCardQrcodeOrderInfoVO();
        try {
            VpsGiftCardQrcodeOrderInfo  orderInfo = giftCardQrcodeOrderInfoService.findById(batchKey);
            BeanUtils.copyProperties(orderInfo, orderInfoVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("orderInfo", orderInfoVO);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 删除记录
     */
    @RequestMapping("/doGiftCardQrcodeOrderDelete")
    public String doGiftCardQrcodeOrderDelete(HttpSession session, String orderKey, Model model){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            giftCardQrcodeOrderInfoService.deleteOrderInfoById(orderKey, currentUser.getUserKey());;
            model.addAttribute("errMsg", "删除成功");
        } catch (BusinessException e) {
            model.addAttribute("errMsg", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errMsg", "删除失败");
            e.printStackTrace();
        }
        return "forward:showGiftCardQrcodeOrderList.do";
    }

    /**
     * 生成门店码源
     */
    @RequestMapping("createCode")
    public String createCode(HttpSession session, String orderKey, Model model){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsGiftCardQrcodeOrderInfo orderInfo = giftCardQrcodeOrderInfoService.findById(orderKey);
            if(Constant.QRCODE_ORDER_STATUS.order_status_3.equals(orderInfo.getOrderStatus())){
                model.addAttribute("errMsg", "码源生成失败：码源生成进行中...，请勿再次生成");
            } else {
                giftCardQrcodeOrderInfoService.createCodeMain(orderInfo, currentUser.getUserKey());
            }
            model.addAttribute("errMsg", "兑付卡码源生成成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "兑付卡码源生成失败："+ ex.getMessage());
            ex.printStackTrace();
        }
        return "forward:showGiftCardQrcodeOrderList.do";
    }

    /**
     * 下载礼品卡码源
     */
    @GetMapping("/downloadGiftCardCode")
    public String downloadCode(String qrcodeOrderKey, String orderName, String qrcodeNum, String createDate,
                               HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = createDate + "/" + qrcodeOrderKey +  "/4compress/"
                    + orderName + "_" + qrcodeNum + ".zip";

            String path = getRootPath(name);
            File file=new File(path);
            //文件判断
            if(!file.exists()){
                System.out.println("file not found");
                outPrint(request, response);
            }else{
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

    private String getRootPath(String name){
        String webappRoot = "/data/upload/giftCardQrcode/";
        return webappRoot + name;
    }

}

