package com.dbt.platform.commonCog.action;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;
import com.dbt.platform.commonCog.bean.CommonCog;
import com.dbt.platform.commonrule.bean.CommonRule;
import com.dbt.platform.commonrule.service.CommonRuleService;
import com.dbt.platform.integral.bean.VpsSignIn;
import com.dbt.platform.integral.bean.VpsTaskSigninInfo;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用配置
 */
@Controller
@RequestMapping("/commonCog")
public class CommonCogAction extends BaseAction {
    @Autowired
    private CommonRuleService commonRuleService;

    /**
     * 查询通用配置
     */
    @RequestMapping("/showCommonCog")
    public String showCommonCog(HttpSession session, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            CommonCog commonCog = commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_5, CommonCog.class);
            model.addAttribute("commonCog", commonCog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/commonCog/showCommonCog";
    }

    /**
     * 编辑通用配置
     */
    @RequestMapping("/updateCommonCog")
    public String updateCommonCog(HttpSession session,
                                  CommonCog commonCog, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            CommonRule commonRule = new CommonRule();
            commonRule.setRuleType(Constant.COMMON_RULE_TYPE.status_5);
            commonRule.setRuleValue(JSON.toJSONString(commonCog));
            commonRule.setUpdateUser(currentUser.getUserName());
            commonRule.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            commonRule.setCreateUser(currentUser.getUserName());
            commonRule.setCreateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            commonRuleService.createOrUpdate(commonRule);

            model.addAttribute("errMsg", "编辑成功");

        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("编辑失败", ex);
        }
        return "forward:showCommonCog.do";
    }
}
