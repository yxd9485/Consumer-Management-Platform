package com.dbt.platform.bottlecap.action;

import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.bottlecap.bean.VpsVcodeBottlecapActivityCogInfo;
import com.dbt.platform.bottlecap.bean.VpsVcodeBottlecapPrizeCogInfo;
import com.dbt.platform.bottlecap.service.BottleCapService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积盖活动action
 */
@Controller
@RequestMapping("/bottleCap")
public class BottleCapAction extends BaseAction {

    @Autowired
    private BottleCapService bottleCapService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;

    /**
     * 积盖活动列表
     *
     * @param session
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showBottleCapActivityList")
    public String showBottleCapActivityList(HttpSession session,
                                            String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsVcodeBottlecapActivityCogInfo queryBean = null;
            queryParam = StringUtils.defaultIfBlank(queryParam, "");
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            queryBean = new VpsVcodeBottlecapActivityCogInfo(queryParam);

            List<VpsVcodeBottlecapActivityCogInfo> resultList = bottleCapService.queryForList(queryBean, pageInfo);
            int countResult = bottleCapService.queryForListCount(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("params", queryBean);
            model.addAttribute("pageParams", pageParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/bottlecap/showBottleCapActivityList";
    }

    /**
     * 新增积盖活动页面
     */
    @RequestMapping("/showBottleCapActivityAdd")
    public String showBottleCapActivityAdd(HttpSession session, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
        model.addAttribute("bigPrizes", vcodeActivityBigPrizeService.getBigPrizeList());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("skuList", skuList);
        return "vcode/bottlecap/showBottleCapActivityAdd";
    }

    /**
     * 新增活动
     */
    @RequestMapping("/doBottleCapActivityAdd")
    public String addBottleCapActivity(HttpSession session, Model model, VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo) throws Exception {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = bottleCapService.addBottleCapActivity(vpsVcodeBottlecapActivityCogInfo, currentUser);
            String cacheStr = CacheKey.cacheKey.vcode
                    .KEY_VCODE_BOOTTLECAP_ACTIVITY_COG
                    + Constant.DBTSPLIT + currentUser.getProjectServerName();
            CacheUtilNew.removeByKey(cacheStr);
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增失败");
            log.error("集盖活动新增失败", ex);
        }
        return showBottleCapActivityList(session, null, null, model);
    }

    /**
     * 校验活动时间是否冲突
     *
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkActivityRepeatDate")
    public String checkActivityRepeatDate(HttpSession session, Model model, String startDate, String endDate, String activityKey) {
        SysUserBasis currentUser = this.getUserBasis(session);
        String result = "验证通过";
        try {
            Boolean aBoolean = bottleCapService.checkActivityRepeatDate(startDate, endDate, activityKey);
            if (!aBoolean) {
                result = "验证失败";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return JSONObject.toJSONString(result);
    }

    /**
     * 跳转活动修改页面
     */
    @RequestMapping("/showBottleCapActivityEdit")
    public String showBottleCapActivityEdit(HttpSession session, Model model, String activityKey) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
        VpsVcodeBottlecapActivityCogInfo activityCogInfo = bottleCapService.queryActivityCogByKey(activityKey);
        List<VpsVcodeBottlecapPrizeCogInfo> prizeCogInfoList = bottleCapService.queryPrizeCogByActivityKey(activityKey);

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("skuList", skuList);
        model.addAttribute("activityCogInfo", activityCogInfo);
        model.addAttribute("prizeCogInfoList", prizeCogInfoList);
        model.addAttribute("bigPrizes", vcodeActivityBigPrizeService.getBigPrizeList());
        return "vcode/bottlecap/showBottleCapActivityEdit";
    }

    /**
     * 修改活动
     */
    @RequestMapping("/doBottleCapActivityEdit")
    public String updateBottleCapActivity(HttpSession session, Model model, VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo) throws Exception {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = bottleCapService.doBottleCapActivityEdit(vpsVcodeBottlecapActivityCogInfo, currentUser);
            String cacheStr = CacheKey.cacheKey.vcode
                    .KEY_VCODE_BOOTTLECAP_ACTIVITY_COG
                    + Constant.DBTSPLIT + currentUser.getProjectServerName();
            CacheUtilNew.removeByKey(cacheStr);
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            log.error("积盖活动修改失败", ex);
        }
        return showBottleCapActivityList(session, null, null, model);
    }
}
