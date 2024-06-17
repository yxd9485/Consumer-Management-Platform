package com.dbt.platform.enterprise.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.enterprise.bean.CompanyInfo;
import com.dbt.platform.enterprise.bean.ConsumerCenterCogInfo;
import com.dbt.platform.enterprise.bean.StatInfo;
import com.dbt.platform.enterprise.service.ConsumerCenterService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.turntable.bean.TurntableActivityCogInfo;
import com.vjifen.server.base.datasource.DDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 客服中心action
 */
@Controller
@RequestMapping("/consumerCenter")
public class ConsumerCenterAction extends BaseAction {

    @Autowired
    private ConsumerCenterService consumerCenterService;

    @RequestMapping("/showConsumerCenterInfoList")
    public String showStatInfoList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);

            List<ConsumerCenterCogInfo> resultList = consumerCenterService.queryForLst();

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception ex) {
            log.error("客服中心查询失败", ex);
        }
        return "vcode/consumerCenter/showConsumerCenterInfoList";
    }

    /**
     * 修改转盘活动
     */
    @RequestMapping("/doConsumerCenterAdd")
    public String doConsumerCenterAdd(HttpSession session, ConsumerCenterCogInfo consumerCenterCogInfo, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = consumerCenterService.doConsumerCenterAdd(consumerCenterCogInfo,currentUser);
            model.addAttribute("errMsg", errMsg);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "修改失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            log.error("转盘活动修改失败", ex);
        }
        return "forward:showConsumerCenterInfoList.do";
    }
}
