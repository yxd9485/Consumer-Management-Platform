package com.dbt.platform.ticket.action;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.ticket.bean.VpsSysTicketCategory;
import com.dbt.platform.ticket.bean.VpsSysTicketInfo;
import com.dbt.platform.ticket.service.VpsSysTicketCategoryService;
import com.dbt.platform.ticket.service.VpsSysTicketInfoService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author bin.zhang
 **/
@Controller
@RequestMapping("/vpsVcodeTicketDenomination")
public class VpsVcodeTicketDenominationAction extends BaseAction {
    @Autowired
    private VpsSysTicketInfoService vpsSysTicketInfoService;
    @Autowired
    private VpsSysTicketCategoryService sysTicketCategoryService;
    /**
     * 时间格式
     */
    private final String formatter = "yyyy-MM-dd HH:mm:ss";

    /**
     * 优惠券活动列表
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showTicketDenominationList")
    public String showTicketDenominationList(HttpSession session,
                                             String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsSysTicketCategory queryBean = new VpsSysTicketCategory(queryParam);
            List<VpsSysTicketCategory> resultList =
                    vpsSysTicketInfoService.findTicketDenomination(queryBean, pageInfo);
            for (VpsSysTicketCategory ticketCategory:resultList) {
                String categoryTypeSuffix = ticketCategory.getCategoryType().substring(1);
                if (StringUtils.isNotBlank(categoryTypeSuffix)){
                    int categoryTypeSuffixNum = Integer.parseInt(categoryTypeSuffix);
                    ticketCategory.setCategoryTypeNum(categoryTypeSuffixNum);
                    if(categoryTypeSuffixNum>=950){
                        List<VpsSysTicketInfo> sysTicketInfoList = ticketCategory.getSysTicketInfoList();
                        for (VpsSysTicketInfo ticket: sysTicketInfoList) {
                            int couponGenre = ticket.getCouponGenre();
                            String couponDesc = null;
                            if (couponGenre==1){
                                couponDesc = "无门槛优惠券：优惠券面额 "+ticket.getTicketMoney()+" 元";
                            }else if(couponGenre==2){
                                couponDesc = "满减优惠券：满 "+ticket.getAtLeast()+" 元减 "+ticket.getTicketMoney()+" 元";
                            }else if (couponGenre==3){
                                couponDesc = "折扣优惠券："+ticket.getDiscount()+"折，满 "+ticket.getAtLeast()+" 元可用";
                            }
                            if ("1".equals(ticket.getCouponEffectType())){
                                couponDesc = couponDesc + "<br>" + "有效期：领取后"+ticket.getEffectDay()+"天内有效";
                            }else{
                                couponDesc = couponDesc + "<br>" + "有效期："+ticket.getCouponStartTime()+"至"+ticket.getCouponEndTime();
                            }
                            ticket.setCouponDesc(couponDesc);
                        }
                    }
                }
            }
            int countResult = vpsSysTicketInfoService.countTicketList(queryBean);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("nowTime", new LocalDate());
            model.addAttribute("ticketCategoryList", sysTicketCategoryService.loadTicketCategory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/ticket/showVcodeTicketDenominationList";
    }

    /**
     * 优惠面额 添加页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showTicketDenominationAdd")
    public String showTicketDenominationAdd(HttpSession session,
                                            String queryParam, String pageParam, Model model,String categoryKey1) {
        List<VpsSysTicketCategory> ticketCategoryList = new ArrayList<>();
        List<VpsSysTicketCategory> vpsSysTicketCategories = sysTicketCategoryService.loadTicketCategory();
        for (VpsSysTicketCategory VpsSysTicketCategory:vpsSysTicketCategories) {
            if (!"M999".equals(VpsSysTicketCategory.getCategoryType())){
                ticketCategoryList.add(VpsSysTicketCategory);
            }
        }
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);
        model.addAttribute("categoryKey", categoryKey1);
        model.addAttribute("ticketCategoryList", ticketCategoryList);
        return "vcode/ticket/showVcodeTicketDenominationAdd";
    }

    /**
     * 创建优惠券面额
     *
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/doTicketDenominationAdd")
    public String doTicketDenominationAdd(HttpSession session, @RequestParam("ticketFile") MultipartFile batchFile, VpsSysTicketInfo vpsSysTicketInfo,
                                          Model model) {
        Map<String, String> resurtMap = new HashMap<String, String>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsSysTicketInfo.setCreateTime(DateUtil.getDate(formatter));
            vpsSysTicketInfo.setUpdateTime(DateUtil.getDate(formatter));
            vpsSysTicketInfo.setCreateUser(currentUser.getUserName());
            vpsSysTicketInfo.setUpdateUser(currentUser.getUserName());
            resurtMap = vpsSysTicketInfoService.writeTicketDenomination(vpsSysTicketInfo,batchFile);
        } catch (Exception ex) {
            resurtMap.put("errMsg", "编辑失败");
            ex.printStackTrace();
        }
        return JSON.toJSONString(resurtMap);
    }

    /**
     * 删除记录
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doTicketDenominationDel")
    public String doTicketDenominationDel(HttpSession session, String infoKey, Model model) {
        try {
            String errMsg = vpsSysTicketInfoService.delTicketDenomination(infoKey);
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("删除失败", ex);
        }
        return "forward:showTicketDenominationList.do";
    }

    /**
     * 跳转商城轮播编辑页面
     */
    @RequestMapping("/showTicketDenominationEdit")
    public String showTicketDenominationEdit(HttpSession session, String infoKey, Model model) {
        VpsSysTicketInfo vpsSysTicketInfo = vpsSysTicketInfoService.findTicketDenominationByKey(infoKey);
        List<VpsSysTicketCategory> sysTicketCategorys = sysTicketCategoryService.loadTicketCategory();
        model.addAttribute("ticketInfo", vpsSysTicketInfo);
        model.addAttribute("ticketCategoryList", sysTicketCategorys);
        return "vcode/ticket/showVcodeTicketDenominationEdit";
    }

    /**
     * 修改优惠券活动
     *
     * @param session
     * @param pageParam
     * @param queryParam
     * @param model
     * @return
     */
    @RequestMapping("/doTicketDenominationEdit")
    public String doTicketDenominationEdit(HttpSession session, VpsSysTicketInfo vpsSysTicketInfo, String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsSysTicketInfo.setUpdateTime(DateUtil.getDate(formatter));
            vpsSysTicketInfo.setUpdateUser(currentUser.getUserName());
            vpsSysTicketInfoService.updateTicketDenominationEdit(vpsSysTicketInfo);
            model.addAttribute("errMsg", "编辑成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            ex.printStackTrace();
        }
        return "forward:showTicketDenominationList.do";
    }

    /**
     * 优惠券类型添加页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showTicketCategoryAdd")
    public String showTicketCategoryAdd(HttpSession session,
                                        String queryParam, String pageParam, Model model) {
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);
        model.addAttribute("ticketCategoryList", sysTicketCategoryService.loadTicketCategory());
        return "vcode/ticket/showTicketCategoryAdd";
    }

    /**
     * 创建优惠券类型
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doTicketCategoryAdd")
    public String doTicketCategoryAdd(HttpSession session, VpsSysTicketCategory vpsSysTicketCategory,
                                      String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsSysTicketCategory.setCreateTime(DateUtil.getDate(formatter));
            vpsSysTicketCategory.setUpdateTime(DateUtil.getDate(formatter));
            vpsSysTicketCategory.setCreateUser(currentUser.getUserName());
            vpsSysTicketCategory.setUpdateUser(currentUser.getUserName());
            String errMsg = vpsSysTicketInfoService.writeTicketCategory(vpsSysTicketCategory);
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            ex.printStackTrace();
        }
        return "forward:showTicketDenominationList.do";
    }

    /**
     * 跳转优惠券类型编辑
     */
    @RequestMapping("/showTicketCategoryEdit")
    public String showTicketCategoryEdit(HttpSession session, String categoryKey1, Model model) {
        VpsSysTicketCategory vpsSysTicketCategory = sysTicketCategoryService.findById(categoryKey1);
        model.addAttribute("ticketCategory", vpsSysTicketCategory);
        return "vcode/ticket/showTicketCategoryEdit";
    }

    /**
     * 修改优惠券类型
     *
     * @param session
     * @param pageParam
     * @param queryParam
     * @param model
     * @return
     */
    @RequestMapping("/doTicketCategoryEdit")
    public String doTicketCategoryEdit(HttpSession session, VpsSysTicketCategory vpsSysTicketCategory, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsSysTicketCategory.setUpdateTime(DateUtil.getDate(formatter));
            vpsSysTicketCategory.setUpdateUser(currentUser.getUserName());
            String errMsg = vpsSysTicketInfoService.updateTicketCategoryEdit(vpsSysTicketCategory);
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            ex.printStackTrace();
        }
        return "forward:showTicketDenominationList.do";
    }

    /**
     * 删除优惠券类型
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doTicketCategoryDel")
    public String doTicketCategoryDel(HttpSession session, String categoryKey1, Model model) {
        try {
            vpsSysTicketInfoService.doTicketCategoryDel(categoryKey1);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("删除失败", ex);
        }
        return "forward:showTicketDenominationList.do";
    }

    /**
     * 导入券码优惠券文件
     *
     * @param session
     * @param pageParam
     * @param queryParam
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/importFile")
    public String doTicketCategoryEdit(HttpSession session,
                                       @RequestParam("filePath") MultipartFile batchFile,String infoKey, Model model) {
        Map<String, String> resurtMap = new HashMap<String, String>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            resurtMap = vpsSysTicketInfoService.importTicketFile(batchFile,infoKey,currentUser);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            ex.printStackTrace();
        }
        return JSON.toJSONString(resurtMap);
    }
}
