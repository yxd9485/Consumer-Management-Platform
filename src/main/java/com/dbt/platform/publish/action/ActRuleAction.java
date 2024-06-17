package com.dbt.platform.publish.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.dbt.platform.activity.bean.VcodeActivityCog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.publish.bean.VpsActRule;
import com.dbt.platform.publish.service.ActRuleService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @ClassName: ActRuleAction
 * @Description: 发布系统活动规则类
 * @author: bin.zhang
 * @date: 2019年12月20日 下午1:39:01
 */
@Controller
@RequestMapping("/actRule")
public class ActRuleAction extends BaseAction {
    @Autowired
    private ActRuleService actRuleService;

    /**
     * @Title: showBatchInfoList
     * @Description: 活动规则展示列表
     */
    @RequestMapping("/showActRuleList")
    public String showActRuleList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsActRule queryBean = new VpsActRule(queryParam);
            List<VpsActRule> resultLst = actRuleService.queryForLst(queryBean, pageInfo);
            int countResult = actRuleService.queryForCount(queryBean);

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
            model.addAttribute("tabsFlag", "4");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "publish/mainpage/pubSysMain";
    }

    /**
     * 修改规则
     */
    @RequestMapping("/actRuleEdit")
    public String doActRuleEdit(HttpSession session, VpsActRule vpsActRule, Model model) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsActRule.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsActRule.setModUser(currentUser.getUserName());
            if (vpsActRule.getLimitStGmt().equals("") || vpsActRule.getLimitEndGmt().equals("")) {
                vpsActRule.setLimitEndGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
                vpsActRule.setLimitStGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            actRuleService.updateVpsActRule(vpsActRule);
            String redisKey =  Constant.pubFalg.ACTRULE + vpsActRule.getVcodeActivityKey();
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(vpsActRule), 60 * 60 * 72);
            resultMap.put("errMsg", "修改成功");
            model.addAttribute("errMsg", "修改成功");

        } catch (Exception e) {
            resultMap.put("errMsg", "修改失败");
            model.addAttribute("errMsg", "修改失败");
        }
        return "forward:/actRule/showActRuleList.do";
    }

    /**
     * 跳转编辑页面
     */
    @RequestMapping("/showActRuleEdit")
    public String showActRuleEdit(HttpSession session, String actRuleKey, Model model) {
        VpsActRule vpsActRule = actRuleService.findById(actRuleKey);
        model.addAttribute("activityCogLst", actRuleService.queryValidActiviyCog(vpsActRule.getVcodeActivityKey()));
        model.addAttribute("vpsActRule", vpsActRule);
        return "/publish/adpub/editActRule";
    }
    /**
     * 跳转新增页面
     */
    @RequestMapping("/showActRuleAdd")
    public String showActRuleAdd(HttpSession session, String actRuleKey, Model model) {
        List<VcodeActivityCog> activityCogList = actRuleService.queryValidActiviyCog(null);
        model.addAttribute("activityCogLst", activityCogList);
        return "/publish/adpub/addActRule";
    }
    /**
     * 修改规则
     */
    @RequestMapping("/actRuleAdd")
    public String doActRuleAdd(HttpSession session, VpsActRule vpsActRule, Model model) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            vpsActRule.setModGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            vpsActRule.setModUser(currentUser.getUserName());
            if (vpsActRule.getLimitStGmt().equals("") || vpsActRule.getLimitEndGmt().equals("")) {
                vpsActRule.setLimitEndGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
                vpsActRule.setLimitStGmt(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            }
            actRuleService.addActRule(vpsActRule);
            String redisKey =  Constant.pubFalg.ACTRULE + vpsActRule.getVcodeActivityKey();
            RedisApiUtil.getInstance().del(true, redisKey);
            RedisApiUtil.getInstance().set(redisKey, JSON.toJSONString(vpsActRule), 60 * 60 * 72);
            resultMap.put("errMsg", "新增成功");
            model.addAttribute("errMsg", "新增成功");

        } catch (Exception e) {
            resultMap.put("errMsg", "新增失败");
            model.addAttribute("errMsg", "新增失败");
        }
        return "forward:/actRule/showActRuleList.do";
    }

}
