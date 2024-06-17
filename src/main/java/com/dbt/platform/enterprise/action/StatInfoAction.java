package com.dbt.platform.enterprise.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vjifen.server.base.datasource.DDS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.bean.StatInfo;
import com.dbt.platform.enterprise.bean.StatisticsSku;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.enterprise.service.StatInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/stat")
public class StatInfoAction extends BaseAction {
    @Autowired
    private StatInfoService statInfoService;
    @Autowired
    private ServerInfoService serverInfoService;
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 统计组合列表
     */
    @RequestMapping("/showStatInfoList")
    public String showStatInfoList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            DDS.use(null);
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            StatInfo queryBean = new StatInfo(queryParam);
            List<StatInfo> resultList = statInfoService.queryForLst(queryBean, pageInfo);
            int countResult = statInfoService.queryForCount(queryBean);
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
            model.addAttribute("serverList", statInfoService.queryServerInfo());
        } catch (Exception ex) {
            log.error("统计分组查询失败", ex);
        }
        return "statistics/showStatInfoList";
    }

    /**
     * 跳转spu新增页面
     */
    @RequestMapping("/showStatInfoAdd")
    public String showStatInfoAdd(HttpSession session, Model model) {
        DDS.use(null);
        List<SkuInfo> skuInfoList = new ArrayList<>();
        List<SkuInfo> skuInfos = new ArrayList<>();
        List<String> serverUnless = new ArrayList<>();
        List<ServerInfo> serverResult = new ArrayList<>();
        DbContextHolder.setDBType(null);
        List<ServerInfo> serverInfos = serverInfoService.getAllServer();
        //取数据字典中不查询的省区
        String servers = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
                .DATA_CONSTANT_CONFIG, DatadicKey.dataConstantConfig.UNINCLUDEDSERVER);
        if (StringUtils.isNotBlank(servers)) {
            serverUnless = Arrays.asList(servers.split(","));
        }
        Map<String, String> skuStatMap = statInfoService.queryStatNameForSku();
        for (ServerInfo s : serverInfos) {
            if (serverUnless.contains(s.getProjectServerName())) {
                continue;
            }
            serverResult.add(s);
            DbContextHolder.setDBType(s.getProjectServerName());
            skuInfos = skuInfoService.loadSkuListByCompany("");
            for (SkuInfo a : skuInfos) {
                a.setProjectServerName(s.getProjectServerName());
                a.setServerName(s.getServerName());
                a.setStatName(skuStatMap.get(a.getProjectServerName() + ":" + a.getSkuKey()));
            }
            skuInfoList.addAll(skuInfos);
            DbContextHolder.clearDBType();
        }
        model.addAttribute("serverList", serverResult);
        model.addAttribute("skuList", skuInfoList);
        return "statistics/showStatInfoAdd";
    }

    /**
     * 跳转spu编辑页面
     */
    @RequestMapping("/showStatInfoEdit")
    public String showStatInfoEdit(HttpSession session, String statInfoKey, Model model) {
        DDS.use(null);
        List<SkuInfo> skuInfoList = new ArrayList<>();
        List<SkuInfo> skuInfos = new ArrayList<>();
        List<String> serverUnless = new ArrayList<>();
        StatInfo statInfo = statInfoService.findById(statInfoKey);
        List<StatisticsSku> skus = skuInfoService.queryForListByStatKey(statInfoKey);
        List<ServerInfo> serverResult = new ArrayList<>();
        List<ServerInfo> serverInfos = serverInfoService.getAllServer();
        //取数据字典中不查询的省区
        String servers = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
                .DATA_CONSTANT_CONFIG, DatadicKey.dataConstantConfig.UNINCLUDEDSERVER);
        if (StringUtils.isNotBlank(servers)) {
            serverUnless = Arrays.asList(servers.split(","));
        }
        Map<String, String> skuStatMap = statInfoService.queryStatNameForSku();
        for (ServerInfo s : serverInfos) {
            if (serverUnless.contains(s.getProjectServerName())) {
                continue;
            }
            serverResult.add(s);
            DbContextHolder.setDBType(s.getProjectServerName());
            skuInfos = skuInfoService.loadSkuListByCompany("");
            for (SkuInfo a : skuInfos) {
                a.setProjectServerName(s.getProjectServerName());
                a.setServerName(s.getServerName());
                a.setSkuWord(s.getProjectServerName() + "/" + s.getServerName() + "/" + a.getSkuKey() + "/" + a.getSkuName());
                a.setStatName(skuStatMap.get(a.getProjectServerName() + ":" + a.getSkuKey()));
            }
            skuInfoList.addAll(skuInfos);
            DbContextHolder.clearDBType();
        }
        List<String> skuList = new ArrayList<>();
        for (StatisticsSku s : skus) {
            String sku = s.getProjectServerName() + "/" + s.getServerName() + "/" + s.getSkuKey() + "/" + s.getSkuName();
            skuList.add(sku);
            s.setSkuWord(sku);
        }
        statInfo.setSkuKey((skuList.toArray(new String[skuList.size()])));
        model.addAttribute("skuList", skuInfoList);
        model.addAttribute("skus", skuList);
        model.addAttribute("statInfo", statInfo);
        model.addAttribute("serverList", serverResult);
        return "statistics/showStatInfoEdit";
    }

    /**
     * 添加spu信息
     */
    @RequestMapping("/doStatInfoAdd")
    public String doStatInfoAdd(HttpSession session, StatInfo statInfo, Model model) {
        try {
            DDS.use(null);
            SysUserBasis currentUser = this.getUserBasis(session);
            statInfo.setStatInfoKey(UUIDTools.getInstance().getUUID());
            statInfo.setCreateTime(DateUtil.getDateTime());
            statInfo.setCreateUser(currentUser.getUserName());
            statInfoService.insertStatInfo(statInfo);
            if (statInfo.getSkuKey() != null) {
                List<String> skuInfos = Arrays.asList(statInfo.getSkuKey());
                for (String s : skuInfos) {
                    String[] skuInfo = s.split("/");
                    StatisticsSku sku = new StatisticsSku();
                    sku.setInfoKey(UUIDTools.getInstance().getUUID());
                    sku.setProjectServerName(skuInfo[0]);
                    sku.setServerName(skuInfo[1]);
                    sku.setSkuKey(skuInfo[2]);
                    sku.setSkuName(skuInfo[3]);
                    sku.setCreateUser(currentUser.getUserName());
                    sku.setCreateTime(DateUtil.getDateTime());
                    sku.setStatInfoKey(statInfo.getStatInfoKey());
                    skuInfoService.insertStatSku(sku);

                }
            }
            model.addAttribute("errMsg", "新增成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增失败");
            log.error("统计分组新增失败", ex);
        }
        return "forward:showStatInfoList.do";
    }

    /**
     * 编辑spu信息
     */
    @RequestMapping("/doStatInfoEdit")
    public String doStatInfoEdit(HttpSession session, StatInfo statInfo, Model model) {
        try {
            DDS.use(null);
            SysUserBasis currentUser = this.getUserBasis(session);
            statInfo.setUpdateTime(DateUtil.getDateTime());
            statInfo.setUpdateUser(currentUser.getUserName());
            statInfoService.updateStatInfo(statInfo);
            //先删除旧的sku的对照关系
            skuInfoService.deleteStatSkuInfo(statInfo.getStatInfoKey());
            if (statInfo.getSkuKey() != null) {
                List<String> skuInfos = Arrays.asList(statInfo.getSkuKey());
                for (String s : skuInfos) {
                    String[] skuInfo = s.split("/");
                    StatisticsSku sku = new StatisticsSku();
                    sku.setInfoKey(UUIDTools.getInstance().getUUID());
                    sku.setProjectServerName(skuInfo[0]);
                    sku.setServerName(skuInfo[1]);
                    sku.setSkuKey(skuInfo[2]);
                    sku.setSkuName(skuInfo[3]);
                    sku.setCreateUser(currentUser.getUserName());
                    sku.setCreateTime(DateUtil.getDateTime());
                    sku.setStatInfoKey(statInfo.getStatInfoKey());
                    skuInfoService.insertStatSku(sku);

                }
            }

            model.addAttribute("errMsg", "编辑成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "编辑失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("统计分组编辑失败", ex);
        }
        return "forward:showStatInfoList.do";
    }

    /**
     * 删除spu信息
     */
    @RequestMapping("/doStatInfoDelete")
    public String doStatInfoDelete(HttpSession session, String statInfoKey, Model model) {
        try {
            DDS.use(null);
            statInfoService.deleteStatInfo(statInfoKey);
            skuInfoService.deleteStatSkuInfo(statInfoKey);
            model.addAttribute("errMsg", "删除成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "删除失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("统计分组删除失败", ex);
        }
        return "forward:showStatInfoList.do";
    }

}
