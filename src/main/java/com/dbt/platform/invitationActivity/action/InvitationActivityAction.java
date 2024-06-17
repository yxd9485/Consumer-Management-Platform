package com.dbt.platform.invitationActivity.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.invitationActivity.bean.VpsVcodeInvitationActivityCog;
import com.dbt.platform.invitationActivity.service.InvitationActivityService;
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
@RequestMapping("/invitationWithGifts")
public class InvitationActivityAction extends BaseAction {
    @Autowired
    private InvitationActivityService invitationService;
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
            VpsVcodeInvitationActivityCog queryBean = new VpsVcodeInvitationActivityCog(queryParam,isBegin);
            SysUserBasis currentUser = this.getUserBasis(session);

            List<VpsVcodeInvitationActivityCog> resultList = invitationService.queryForList(queryBean, pageInfo, isBegin);
            int countResult = invitationService.queryForCount(queryBean);

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
            model.addAttribute("errMsg", "新增失败");
            e.printStackTrace();
        }
        return "vcode/invitationWithGifts/showActivationCogList";
    }

    /**
     * 新增活动页面跳转
     */
    @RequestMapping("/showAddActivity")
    public String showAddActivity(HttpSession session, Model model){
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
            invitationService.getAreaJsonAll(model, "");
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("companyKey", currentUser.getCompanyKey());
            model.addAttribute("skuList", skuList);
        } catch (Exception e) {
            model.addAttribute("errMsg", "新增活动页面跳转失败");
            e.printStackTrace();
        }
        return "vcode/invitationWithGifts/showActivationCogAdd";
    }

    /**
     * 新增活动
     */
    @RequestMapping("/addActivity")
    public String addActivity(HttpSession session, Model model, VpsVcodeInvitationActivityCog activityCog){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            invitationService.addActivity(activityCog,currentUser);
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
            VpsVcodeInvitationActivityCog activity = invitationService.showEditActivity(model,infoKey);
            List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
            invitationService.getAreaJsonAll(model, activity.getArea());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("companyKey", currentUser.getCompanyKey());
            model.addAttribute("skuList", skuList);
            model.addAttribute("entity", activity);
        } catch (Exception e) {
            model.addAttribute("errMsg", "修改活动页面跳转失败");
            e.printStackTrace();
        }
        return "vcode/invitationWithGifts/showActivationCogEdit";
    }

    /**
     * 修改活动
     */
    @RequestMapping("/editActivity")
    public String editActivity(HttpSession session, Model model, VpsVcodeInvitationActivityCog activityCog){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            invitationService.editActivity(activityCog,currentUser);
            // 删除缓存
            String cacheKeyStrFlag = CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_INVITATION_COG + Constant.DBTSPLIT + activityCog.getActivityFlag();
            CacheUtilNew.removeGroupByKey(cacheKeyStrFlag);
            String cacheKeyStrInfoKey = CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_INVITATION_COG + Constant.DBTSPLIT + activityCog.getInfoKey();
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
            String msg = invitationService.deleteActivity(infoKey, currentUser.getUserName());
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
        return invitationService.checkName(currentUser,checkName,infoKey);
    }

    /**
     * 检验活动日期及地区
     */
    @RequestMapping("/checkDateAndArea")
    @ResponseBody
    public String checkDateAndArea(HttpSession session, Model model, String startTime, String endTime, String area, String infoKey) {
        return invitationService.checkDateAndArea(startTime, endTime, area, infoKey);
    }
}
