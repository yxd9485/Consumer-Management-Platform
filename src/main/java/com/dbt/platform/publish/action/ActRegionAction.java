package com.dbt.platform.publish.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.publish.bean.VpsAdRegion;
import com.dbt.platform.publish.bean.VpsAdShop;
import com.dbt.platform.publish.service.ActRegionService;
import com.dbt.platform.publish.service.AdPubService;
import com.dbt.platform.system.bean.SysRole;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 活动专区
 */
@Controller
@RequestMapping("/actRegion")
public class ActRegionAction extends BaseAction {

    @Autowired
    private ActRegionService actRegionService;
    @Autowired
    private AdPubService adPubService;

    /**
     * 活动专区展示列表
     */
    @RequestMapping("/showActRegionList")
    public String showActRegionList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsAdRegion queryBean = new VpsAdRegion(queryParam);
            List<VpsAdRegion> resultLst = actRegionService.queryForLst(queryBean, pageInfo);
            int countResult = actRegionService.queryForCount(queryBean);

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
            List<SysRole> sysRoleList = this.getSecurityContext(session).getSysRoleList();
            List<String> roleList = sysRoleList.stream().map(SysRole::getRoleKey).collect(Collectors.toList());
            if(roleList.contains("13")){
                model.addAttribute("roleType", "ShopOperate");
            }
            model.addAttribute("tabsFlag", "6");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "publish/mainpage/pubSysMain";
    }

    /**
     * 新增活动专区页面
     */
    @RequestMapping("/showAdRegionAdd")
    public String showAdShopAdd(HttpSession session, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            model.addAttribute("currentUser", currentUser);
            adPubService.initCrmGroup(model);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "/publish/adpub/addRegionAd";
    }

    /**
     * 新增活动专区
     */
    @RequestMapping("/doAdRegionAdd")
    public String doAdShopAdd(HttpSession session, VpsAdRegion vpsAdRegion, Model model) {
        String errMsg = "";
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsAdRegion.setCreGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdRegion.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdRegion.setModUser(currentUser.getUserName());
            if (vpsAdRegion.getAreaCode().equals(Constant.pubFalg.ALLLOCAL)) {
                vpsAdRegion.setAreaCode(Constant.pubFalg.ALLLOCAL + ",");
            }
            //默认0为热门活动
            vpsAdRegion.setSectionGroup(StringUtils.defaultIfBlank(vpsAdRegion.getSectionGroup(),"0"));
            //不允许二次跳转时将二次跳转类型置空
            if (!"1".equals(vpsAdRegion.getCanDoubleJump())){
                vpsAdRegion.setDoubleJumpType(null);
            }
            actRegionService.addAdRegion(vpsAdRegion);
            String redisKey = Constant.pubFalg.ACTREGION + Constant.DBTSPLIT + vpsAdRegion.getPosition();
            RedisApiUtil.getInstance().del(true, redisKey);
            errMsg = "保存成功";
            model.addAttribute("errMsg", errMsg);
        } catch (Exception ex) {
            errMsg = "保存失败";
            model.addAttribute("errMsg", errMsg);
            log.error(ex.getMessage(), ex);
        }
        model.addAttribute("errMsg", errMsg);
        return "forward:/actRegion/showActRegionList.do";
    }

    /**
     * 跳转活动专区编辑页面
     */
    @RequestMapping("/showAdRegionEdit")
    public String showAdRegionEdit(HttpSession session, String infoKey, Model model) {
        VpsAdRegion vpsAdRegion = actRegionService.findById(infoKey);
        vpsAdRegion.setUnmodifiedPosition(vpsAdRegion.getPosition());
        model.addAttribute("vpsAdRegion", vpsAdRegion);
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        try {
            adPubService.initCrmGroup(model);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "/publish/adpub/editRegionAd";
    }

    /**
     * 修改商城轮播图
     */
    @RequestMapping("/doAdRegionEdit")
    public String adShopEdit(HttpSession session, VpsAdRegion vpsAdRegion, Model model) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsAdRegion.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsAdRegion.setModUser(currentUser.getUserName());
            if (vpsAdRegion.getAreaCode().equals(Constant.pubFalg.ALLLOCAL)) {
                vpsAdRegion.setAreaCode(Constant.pubFalg.ALLLOCAL + ",");
            }
            //默认0为热门活动
            vpsAdRegion.setSectionGroup(StringUtils.defaultIfBlank(vpsAdRegion.getSectionGroup(),"0"));
            //不允许二次跳转时将二次跳转类型置空
            if (!"1".equals(vpsAdRegion.getCanDoubleJump())){
                vpsAdRegion.setDoubleJumpType(null);
            }
            actRegionService.updateVpsAdRegion(vpsAdRegion);
            String redisKey = Constant.pubFalg.ACTREGION + Constant.DBTSPLIT + vpsAdRegion.getPosition();
            RedisApiUtil.getInstance().del(true, redisKey);
            String unmodifiedRedisKey = Constant.pubFalg.ACTREGION + Constant.DBTSPLIT + vpsAdRegion.getUnmodifiedPosition();
            RedisApiUtil.getInstance().del(true, unmodifiedRedisKey);
            model.addAttribute("errMsg", "修改成功");
        } catch (Exception e) {
            model.addAttribute("errMsg", "修改失败");
            log.error("广告发布修改失败", e);
        }
        return "forward:/actRegion/showActRegionList.do";
    }


    /**
     * 删除活动专区
     */
    @RequestMapping("/adRegionDelete")
    public String adRegionDelete(HttpSession session, String infoKey, Model model) {
        try {
            actRegionService.adRegionDelete(infoKey);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("删除失败", ex);
        }
        return "forward:/actRegion/showActRegionList.do";
    }
}
