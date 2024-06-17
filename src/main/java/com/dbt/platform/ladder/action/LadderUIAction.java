package com.dbt.platform.ladder.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.ladder.bean.LadderUI;
import com.dbt.platform.ladder.service.LadderUIService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @Description: 阶梯的前端样式
 * @Author bin.zhang
 **/
@Controller
@RequestMapping("/ladderUI")
public class LadderUIAction extends BaseAction {
    @Autowired
    private LadderUIService ladderUIService;
    /**
     * 阶梯规则列表
     *
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showLadderUIList")
    public String showLadderUIList(HttpSession session,
                                     String queryParam, String pageParam, String ruleKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            List<LadderUI> resultList = ladderUIService.findLadderUIByKey(ruleKey, pageInfo);
            int countResult = ladderUIService.findLadderUICount(ruleKey);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("ladderUIList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("ruleKey", ruleKey);
            model.addAttribute("orderType", pageInfo.getOrderType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/ladder/showLadderUIList";
    }
    /**
     * 添加阶梯UI页面
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showLadderUIAdd")
    public String showLadderUIAdd(HttpSession session, Model model, String ruleKey) {
        // 查询可配置阶梯的活动
        model.addAttribute("ruleKey",ruleKey);
        return "vcode/ladder/showLadderUIAdd";
    }
    /**
     * 创建规则
     */
    @RequestMapping("/doLadderUIAdd")
    public String doLadderUIAdd(HttpSession session, LadderUI ladderUI , Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            ladderUI.setCreateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            ladderUI.setCreateUser(currentUser.getUserName());
            ladderUI.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));;
            ladderUIService.addLadderUI(ladderUI);
            model.addAttribute("errMsg", "添加成功");
        }catch (Exception ex) {
            model.addAttribute("errMsg", "添加失败");
            log.error(ex.getMessage(), ex);
        }
        return "forward:showLadderUIList.do? ruleKey = " + ladderUI.getRuleKey();
    }
    /**
     * 删除活动
     */
    @RequestMapping("/doLadderUIDel")
    public String doLadderUIDel(HttpSession session, Model model,String info, String ruleKey) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            ladderUIService.deleteById(info);
//            // 删除缓存
//            String key = CacheUtilNew.cacheKey.vodeActivityKey
//                    .KEY_LADDER_RULE_COG + Constant.DBTSPLIT + DateUtil.getDate();
//            CacheUtilNew.removeGroupByKey(key);
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error(ex.getMessage(), ex);
        }
        return "forward:showLadderUIList.do? ruleKey = " + ruleKey;
    }

    /**
     * 跳转编辑页面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showLadderUIEdit")
    public String showLadderUIEdit(HttpSession session, Model model,String info) {
        try {
            //查询当前规则
            LadderUI ladderUI = ladderUIService.findById(info);
            model.addAttribute("ladderUI", ladderUI);
            model.addAttribute("ruleKey",ladderUI.getRuleKey());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "vcode/ladder/showLadderUIEdit";
    }

    /**
     * 更改ui
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/doLadderUIEdit")
    public String doLadderUIEdit(HttpSession session, LadderUI ladderUI, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            ladderUI.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            ladderUI.setUpdateUser(currentUser.getUserName());
            ladderUIService.updateUI(ladderUI);

            // 删除缓存
//            String key = CacheUtilNew.cacheKey.vodeActivityKey
//                    .KEY_LADDER_RULE_COG + Constant.DBTSPLIT + DateUtil.getDate();
//            CacheUtilNew.removeGroupByKey(key);
            model.addAttribute("errMsg", "编辑成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error(ex.getMessage(), ex);
        }
         return "forward:showLadderUIList.do? ruleKey = " + ladderUI.getRuleKey();
    }

}
