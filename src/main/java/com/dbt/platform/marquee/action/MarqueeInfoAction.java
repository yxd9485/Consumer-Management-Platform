package com.dbt.platform.marquee.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.bean.SpuInfo;
import com.dbt.platform.marquee.bean.MarqueeCogInfo;
import com.dbt.platform.marquee.service.MarqueeInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.dao.IVpointsCouponCogDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 跑马灯action
 */
@Controller
@RequestMapping("/marquee")
public class MarqueeInfoAction extends BaseAction {

    @Autowired
    private MarqueeInfoService marqueeInfoService;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;

    /**
     * 跑马灯列表
     */
    @RequestMapping("/showMarqueeList")
    public String showMarqueeList(HttpSession session, String queryParam, String pageParam, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        MarqueeCogInfo queryBean = new MarqueeCogInfo(queryParam);
        List<MarqueeCogInfo> resultList = marqueeInfoService.queryForLst(queryBean, pageInfo);
        int countResult = marqueeInfoService.queryForCount(queryBean);


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
        return "vcode/marquee/showMarqueeCogList";
    }

    /**
     * 跳转跑马灯新增页面
     */
    @RequestMapping("/showMarqueeCogInfoAdd")
    public String showMarqueeCogInfoAdd(HttpSession session, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("bigPrizes", vcodeActivityBigPrizeService.getBigPrizeList());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("couponMap", marqueeInfoService.queryAllCouponCog());
        return "vcode/marquee/showMarqueeCogInfoAdd";
    }


    /**
     * 添加跑马灯配置信息
     */
    @RequestMapping("/doMarqueeCogInfoAdd")
    public String doSpuInfoAdd(HttpSession session, MarqueeCogInfo marqueeCogInfo, Model model) {
        try {
            if (!marqueeInfoService.existMarquee(marqueeCogInfo.getStartDate(), marqueeCogInfo.getEndDate(), marqueeCogInfo.getWinType(),null)) {
                SysUserBasis currentUser = this.getUserBasis(session);
                marqueeInfoService.createMarqueeCog(marqueeCogInfo, currentUser);
                RedisApiUtil.getInstance().del(true, Constant.marquee.MARQUEECOGKEY);
                model.addAttribute("errMsg", "新增成功");
            } else {
                model.addAttribute("errMsg", "新增失败，该时间段内已存在开启的跑马灯, 请更改跑马灯播放时间");
            }
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增失败");
            log.error("跑马灯配置新增失败", ex);
        }
        return "forward:showMarqueeList.do";
    }

    /**
     * 跳转跑马灯修改页面
     */
    @RequestMapping("/showMarqueeCogInfoEdit")
    public String showMarqueeCogInfoEdit(HttpSession session, Model model, String infoKey) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        MarqueeCogInfo marqueeCogInfo = marqueeInfoService.findById(infoKey);
        model.addAttribute("bigPrizes", vcodeActivityBigPrizeService.getBigPrizeList());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("couponMap", marqueeInfoService.queryAllCouponCog());
        model.addAttribute("marqueeCogInfo", marqueeCogInfo);
        return "vcode/marquee/showMarqueeCogInfoEdit";
    }

    /**
     * 编辑跑马灯信息
     */
    @RequestMapping("/doMarqueeCogInfoEdit")
    public String doSkuInfoEdit(HttpSession session, MarqueeCogInfo marqueeCogInfo, Model model) {
        try {
            if (!marqueeInfoService.existMarquee(marqueeCogInfo.getStartDate(), marqueeCogInfo.getEndDate(), marqueeCogInfo.getWinType(),marqueeCogInfo.getInfoKey())) {
                SysUserBasis currentUser = this.getUserBasis(session);
                marqueeInfoService.doMarqueeCogInfoEdit(marqueeCogInfo, currentUser);
                RedisApiUtil.getInstance().del(true, Constant.marquee.MARQUEECOGKEY);
                model.addAttribute("errMsg", "编辑成功");
            } else {
                model.addAttribute("errMsg", "编辑失败, 该时间段内已存在开启的跑马灯, 请更改跑马灯显示时间或关闭的已有设置");
            }
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "编辑失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("跑马灯编辑失败", ex);
        }
        return "forward:showMarqueeList.do";
    }

    /**
     * 删除跑马灯信息
     */
    @RequestMapping("/doMarqueeCogInfoDelete")
    public String doMarqueeCogInfoDelete(HttpSession session, String infoKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            marqueeInfoService.doMarqueeCogInfoDelete(infoKey, currentUser);
            RedisApiUtil.getInstance().del(true, Constant.marquee.MARQUEECOGKEY);
            model.addAttribute("errMsg", "删除成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "删除失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("跑马灯删除失败", ex);
        }
        return "forward:showMarqueeList.do";
    }
}
