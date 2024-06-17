package com.dbt.platform.home.action;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.service.VcodeQrcodeBatchInfoService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.expireremind.bean.VpsMsgExpireRemindInfo;
import com.dbt.platform.expireremind.service.VpsExpireRemindService;
import com.dbt.platform.home.bean.ReportMainInfo;
import com.dbt.platform.home.homeService.VpsHomeService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/homeData")
public class HomeDataAction extends BaseAction {

    @Autowired
    VpsExpireRemindService remindService;

    @Autowired
    VpsHomeService homeService;

    /**
     * 首页
     * @return
     */
    @RequestMapping("/homeDataPage")
    public String homeDataPage(HttpSession session, Model model,String pageParam) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        if (!StringUtils.isEmpty(String.valueOf(pageInfo.getStartCount()))) {
            pageInfo.setStartCount(0);

        }
        if (!StringUtils.isEmpty(String.valueOf(pageInfo.getPagePerCount()))) {
            pageInfo.setPagePerCount(15);

        }

        map.put("pageInfo",pageInfo);
        try{
           List<VpsMsgExpireRemindInfo>  msgList=   remindService.selectByActivityParamer(map);
            ReportMainInfo   dayInfo= homeService.getHomeData();
           int totals=  remindService.selectTotalNum();
            model.addAttribute("dayInfo", dayInfo);
            model.addAttribute("msgList", msgList);
            model.addAttribute("totals",totals);
            model.addAttribute("currentUser", this.getUserBasis(session));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "home/homePage";
    }

}
