package com.dbt.platform.lotterysign.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.lotterysign.bean.VpsLotterySignCog;
import com.dbt.platform.lotterysign.service.LotterySignService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 抽奖签到action
 */
@Controller
@RequestMapping("/lotterySign")
public class LotterySignAction extends BaseAction {

    @Autowired
    private LotterySignService lotterySignService;
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 抽奖签到活动列表
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showLotterySignList")
    public String showLotterySignList(HttpSession session,
                                      String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsLotterySignCog queryBean = new VpsLotterySignCog(queryParam);
            List<VpsLotterySignCog> resultList =
                    lotterySignService.queryForList(queryBean, pageInfo);
            int countResult = lotterySignService.queryForCount(queryBean);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/lotterySign/showLotterySignList";
    }

    /**
     * 新增抽奖签到活动页面
     */
    @RequestMapping("/showLotterySignAdd")
    public String showLotterySignAdd(HttpSession session, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        List<SkuInfo> skuList = skuInfoService
                .loadSkuListByCompany(currentUser.getCompanyKey());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("skuList", skuList);
        return "vcode/lotterySign/showLotterySignAdd";
    }

    /**
     * 新增抽奖签到活动
     */
    @RequestMapping("/doLotterySignAdd")
    public String createLotterySignAdd(HttpSession session, VpsLotterySignCog vpsLotterySignCog, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = lotterySignService.doLotterySignAdd(vpsLotterySignCog, currentUser);
            model.addAttribute("errMsg", errMsg);
            String cacheStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_LOTTERY_SIGN_COG;
            CacheUtilNew.removeByKey(cacheStr);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增失败");
            log.error("抽奖签到活动新增失败", ex);
        }
        return "forward:showLotterySignList.do";
    }

    /**
     * 修改抽奖签到活动页面
     */
    @RequestMapping("/showLotterySignEdit")
    public String showLotterySignEdit(HttpSession session, Model model,String infoKey) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        List<SkuInfo> skuList = skuInfoService
                .loadSkuListByCompany(currentUser.getCompanyKey());
        VpsLotterySignCog vpsLotterySignCog = lotterySignService.findById(infoKey);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("skuList", skuList);
        model.addAttribute("vpsLotterySignCog", vpsLotterySignCog);
        return "vcode/lotterySign/showLotterySignEdit";
    }

    /**
     * 修改抽奖签到活动
     */
    @RequestMapping("/doLotterySignEdit")
    public String editLotterySignEdit(HttpSession session, VpsLotterySignCog vpsLotterySignCog, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = lotterySignService.doLotterySignEdit(vpsLotterySignCog, currentUser);
            model.addAttribute("errMsg", errMsg);
            String cacheStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_LOTTERY_SIGN_COG;
            CacheUtilNew.removeByKey(cacheStr);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "修改失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            log.error("抽奖签到活动修改失败", ex);
        }
        return "forward:showLotterySignList.do";
    }

    /**
     * 删除抽奖签到活动
     */
    @RequestMapping("/doLotterySignDel")
    public String deleteLotterySignDel(HttpSession session, String infoKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = lotterySignService.doLotterySignDel(infoKey, currentUser);
            model.addAttribute("errMsg", errMsg);
            String cacheStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_LOTTERY_SIGN_COG;
            CacheUtilNew.removeByKey(cacheStr);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "删除失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("抽奖签到活动删除失败", ex);
        }
        return "forward:showLotterySignList.do";
    }
}