package com.dbt.platform.vdhInvitation.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.vdhInvitation.bean.VpsVcodeVdhInvitationActivityCog;
import com.dbt.platform.vdhInvitation.service.VdhInvitationActivityService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/vdhInvitation")
public class VdhInvitationActivityAction extends BaseAction {
    @Autowired
    private VdhInvitationActivityService vdhInvitationService;
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 获取活动列表
     * @param session
     * @param model
     * @param queryParam
     * @param pageParam
     * @param isBegin
     * @return
     */
    @RequestMapping("/showActivityList")
    public String showActivityList(HttpSession session, Model model, String queryParam, String pageParam, String isBegin) {
        try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodeVdhInvitationActivityCog queryBean = new VpsVcodeVdhInvitationActivityCog(queryParam,isBegin);
            SysUserBasis currentUser = this.getUserBasis(session);

            List<VpsVcodeVdhInvitationActivityCog> resultList = vdhInvitationService.queryForList(queryBean, pageInfo, isBegin);
            int countResult = vdhInvitationService.queryForCount(queryBean);

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
            model.addAttribute("isBegin", StringUtils.isBlank(isBegin) ? "1": isBegin);
        } catch (Exception e) {
            model.addAttribute("errMsg", "获取活动列表失败");
            e.printStackTrace();
        }
        return "vcode/vdhInvitation/showActivationCogList";
    }

    /**
     * 新增活动页面跳转
     */
    @RequestMapping("/showAddActivity")
    public String showAddActivity(HttpSession session, Model model){
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
            vdhInvitationService.getAreaJsonAll(model, "");
            vdhInvitationService.getVdhRegionAndGroup(model);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("companyKey", currentUser.getCompanyKey());
            model.addAttribute("skuList", skuList);
        } catch (Exception e) {
            model.addAttribute("errMsg", "新增活动页面跳转失败，请稍后重试。");
            e.printStackTrace();
            return "forward:showActivityList.do";
        }
        return "vcode/vdhInvitation/showActivationCogAdd";
    }

    /**
     * 新增活动
     */
    @RequestMapping("/addActivity")
    public String addActivity(HttpSession session, Model model, VpsVcodeVdhInvitationActivityCog activityCog){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            vdhInvitationService.addActivity(activityCog,currentUser);
            model.addAttribute("errMsg", "新增活动成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增活动失败");
            ex.printStackTrace();
        }
        return "forward:showActivityList.do";
    }

    /**
     * 修改活动页面跳转
     */
    @RequestMapping("/showEditActivity")
    public String showEditActivity(HttpSession session, Model model, String infoKey){
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsVcodeVdhInvitationActivityCog activity = vdhInvitationService.showEditActivity(model,infoKey);
            List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
            vdhInvitationService.getAreaJsonAll(model, activity.getInviteeArea());
            vdhInvitationService.getVdhRegionAndGroup(model);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("companyKey", currentUser.getCompanyKey());
            model.addAttribute("skuList", skuList);
            model.addAttribute("entity", activity);
            model.addAttribute("bigRegionArray", activity.getBigRegionArray());
            model.addAttribute("terminalGroupArray", activity.getTerminalGroupArray());
        } catch (Exception e) {
            model.addAttribute("errMsg", "修改活动页面跳转失败，请稍后重试。");
            e.printStackTrace();
            return "forward:showActivityList.do";
        }
        return "vcode/vdhInvitation/showActivationCogEdit";
    }

    /**
     * 修改活动
     */
    @RequestMapping("/editActivity")
    public String editActivity(HttpSession session, Model model, VpsVcodeVdhInvitationActivityCog activityCog){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            vdhInvitationService.editActivity(activityCog,currentUser);
            // 删除缓存
            String cacheKeyStrInfoKey = CacheKey.cacheKey.vcode.KEY_VCODE_VDH_INVITATION_ACTIVITY_COG + Constant.DBTSPLIT + activityCog.getInfoKey();
            CacheUtilNew.removeGroupByKey(cacheKeyStrInfoKey);
            model.addAttribute("errMsg", "修改活动成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改活动失败");
            ex.printStackTrace();
        }
        return "forward:showActivityList.do";
    }

    /**
     * 删除活动
     * @param session
     * @param model
     * @param infoKey
     * @param queryParam
     * @param pageParam
     * @param isBegin
     * @return
     */
    @RequestMapping("/deleteActivity")
    public String deleteActivity(HttpSession session, Model model, String infoKey, String queryParam, String pageParam, String isBegin){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            String msg = vdhInvitationService.deleteActivity(infoKey, currentUser.getUserName());
            if (StringUtils.isNotBlank(msg)){
                model.addAttribute("errMsg", msg);
            }else{
                model.addAttribute("errMsg", "删除活动成功");
            }
        }catch(Exception e){
            model.addAttribute("errMsg", "删除活动失败");
            e.printStackTrace();
        }
        return showActivityList(session, model, queryParam, pageParam, isBegin);
    }

    /**
     * 检验活动名称是否重复
     */
    @RequestMapping("/checkName")
    @ResponseBody
    public String checkName(HttpSession session, Model model, String checkName, String infoKey) {
        SysUserBasis currentUser = this.getUserBasis(session);
        return vdhInvitationService.checkName(currentUser,checkName,infoKey);
    }
}
