package com.dbt.platform.turntable.action;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.*;
import com.dbt.platform.activity.dao.IVcodeActivityRebateRuleCogDao;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.mn.service.MnDepartmentService;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.prize.service.MajorInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.ticket.bean.VpsSysTicketInfo;
import com.dbt.platform.ticket.service.VpsSysTicketInfoService;
import com.dbt.platform.turntable.bean.TurntableActivityCogInfo;
import com.dbt.platform.turntable.bean.VpsTurntablePacksRecord;
import com.dbt.platform.turntable.bean.VpsTurntablePrizeCog;
import com.dbt.platform.turntable.service.TurntableService;
import com.dbt.vpointsshop.service.VpointsCouponCogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.dbt.framework.util.CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_TURNTABLE_PRIZE_ACTIVITY_COG;

/**
 * 幸运转盘 action
 */
@Controller
@RequestMapping("/turntable")
public class TurntableAction extends BaseAction {

    @Autowired
    private TurntableService turntableService;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
    @Autowired
    private IVcodeActivityRebateRuleCogDao rebateRuleCogDao;
    @Autowired
    private VcodeActivityRebateRuleCogService rebateRuleCogService;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;
    @Autowired
    private MajorInfoService majorInfoService;
    @Autowired
    private MnDepartmentService mnDepartmentService;
    @Autowired
    private VpsSysTicketInfoService sysTicketInfoService;
    @Autowired
    private VpointsCouponCogService couponCogService;
    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 转盘活动列表
     */
    @RequestMapping("/showTurntableActivityList")
    public String showMarqueeList(HttpSession session, String queryParam, String pageParam, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        TurntableActivityCogInfo queryBean = new TurntableActivityCogInfo(queryParam);

        List<TurntableActivityCogInfo> resultList = null;
        try {
            resultList = turntableService.queryForLst(queryBean, pageInfo);
            for (TurntableActivityCogInfo turntableActivityCogInfo : resultList) {
                StringBuilder areaName = new StringBuilder();
                if (StringUtils.isNotBlank(turntableActivityCogInfo.getAreaCode())) {
                    String[] split = turntableActivityCogInfo.getAreaCode().split(",");
                    for (String areaCode : split) {
                        areaName.append(",").append(rebateRuleCogService.getAreaNameByCode(areaCode));
                    }
                    turntableActivityCogInfo.setAreaName(areaName.substring(1));
                } else if (StringUtils.isNotBlank(turntableActivityCogInfo.getDepRegionId())) {
                    String[] groupDepRegionId = turntableActivityCogInfo.getDepRegionId().split(",");
                    String[] groupDepProvinceId = turntableActivityCogInfo.getDepProvinceId().split(",");
                    String[] groupFirstDealerKey = turntableActivityCogInfo.getFirstDealerKey().split(",");
                    StringBuilder departmentNames = new StringBuilder();
                    for (int i = 0; i < groupDepRegionId.length; i++) {
                        String departmentName = rebateRuleCogService.getDepartmentNames(groupDepRegionId[i] + "," + (groupDepProvinceId.length > i ? groupDepProvinceId[i] : "") + "," + (groupFirstDealerKey.length > i ? groupFirstDealerKey[i] : ""));
                        departmentNames.append(departmentName).append(",");
                    }
                    turntableActivityCogInfo.setAreaName(departmentNames.toString());
                }
            }

            int countResult = turntableService.queryForCount(queryBean);
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
            model.addAttribute("projectName", "mengniu".equals(DbContextHolder.getDBType()) ? "1" : "0");
            model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
                    "data_constant_config", "role_info").split(",")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/turntable/showTurntableActivityCogList";
    }

    /**
     * 幸运转盘新增页面
     */
    @RequestMapping("/showTurntableActivityAdd")
    public String showMarqueeCogInfoAdd(HttpSession session, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        // 查询蒙牛组织架构，默认查询大区级别
        if ("mengniu".equals(DbContextHolder.getDBType())) {
            model.addAttribute("departmentList", mnDepartmentService.queryListByMap(-1, 4));
        }
        List<SkuInfo> skuInfos = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
        model.addAttribute("skuList", skuInfos);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        return "vcode/turntable/showTurntableActivityAdd";
    }

    /**
     * 新增转盘活动
     */
    @RequestMapping("/doTurntableActivityInfoAdd")
    public String doTurntableActivityInfoAdd(HttpSession session, TurntableActivityCogInfo turntableActivityCogInfo, String filterDepartmentIds, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = turntableService.createTurntableActivity(turntableActivityCogInfo, currentUser, filterDepartmentIds);
            model.addAttribute("errMsg", errMsg);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增失败");
            log.error("转盘活动新增失败", ex);
        }
        return "forward:showTurntableActivityList.do";
    }

    /**
     * 幸运转盘活动修改页面
     */
    @RequestMapping("/showTurntableActivityEdit")
    public String showTurntableActivityEdit(HttpSession session, String activityKey, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        TurntableActivityCogInfo turntableActivityCogInfo = turntableService.queryTurntableActivityInfoByKey(activityKey);
        List<SkuInfo> skuInfos = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
        model.addAttribute("skuList", skuInfos);
        // 查询蒙牛组织架构，默认查询大区级别
        if ("mengniu".equals(DbContextHolder.getDBType())) {
            model.addAttribute("departmentList", mnDepartmentService.queryListByMap(-1, 4));
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activityCog", turntableActivityCogInfo);
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        return "vcode/turntable/showTurntableActivityEdit";
    }

    /**
     * 修改转盘活动
     */
    @RequestMapping("/doTurntableActivityInfoEdit")
    public String doTurntableActivityInfoEdit(HttpSession session, TurntableActivityCogInfo turntableActivityCogInfo, String filterDepartmentIds, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = turntableService.doTurntableActivityInfoEdit(turntableActivityCogInfo, currentUser, filterDepartmentIds);
            model.addAttribute("errMsg", errMsg);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "修改失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            log.error("转盘活动修改失败", ex);
        }
        return "forward:showTurntableActivityList.do";
    }


    /**
     * 幸运转盘新增奖品页面
     */
    @RequestMapping("/showTurntablePrizeAdd")
    public String showMarqueeCogInfoAdd(HttpSession session, String key, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activityKey", key);
        model.addAttribute("bigPrizes", vcodeActivityBigPrizeService.getBigPrizeList());
        model.addAttribute("couponList", sysTicketInfoService.localList());
        model.addAttribute("shopCouponList", couponCogService.queryValidCouponList(true,null));
        return "vcode/turntable/showTurntablePrizeAdd";
    }

    /**
     * 新增幸运转盘奖品
     */
    @RequestMapping("/doTurntablePrizeAdd")
    public String doTurntablePrizeAdd(HttpSession session, String activityKey, VpsTurntablePrizeCog turntablePrizeCog, Model model) throws Exception {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = turntableService.createTurntablePrize(activityKey, turntablePrizeCog, currentUser);
            model.addAttribute("errMsg", errMsg);
            String prizeKey = KEY_VCODE_TURNTABLE_PRIZE_ACTIVITY_COG + Constant.DBTSPLIT + activityKey;
            // 删除活动缓存
            CacheUtilNew.removeGroupByKey(prizeKey);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增失败");
            log.error("转盘奖品新增失败", ex);
        }
        return "forward:showTurntableActivityList.do";
    }

    /**
     * 幸运转盘修改奖品页面
     */
    @RequestMapping("/showTurntablePrizeEdit")
    public String showTurntablePrizeEdit(HttpSession session, String infoKey, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        VpsTurntablePrizeCog prizeCog = turntableService.queryTurntablePrizeByInfoKey(infoKey);
        List<VcodePrizeBasicInfo> bigPrizeList = vcodeActivityBigPrizeService.getBigPrizeList();
        for (VcodePrizeBasicInfo vcodePrizeBasicInfo : bigPrizeList) {
            String prizeWinPic = vcodePrizeBasicInfo.getPrizeWinPic();
            int vma = prizeWinPic.indexOf("vma");
            vcodePrizeBasicInfo.setPrizeWinPic(prizeWinPic.substring(vma+4));

        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("turntableActivityCog", turntableService.queryTurntableActivityInfoByKey(prizeCog.getTurntableActivityKey()));
        model.addAttribute("bigPrizes", bigPrizeList);
        model.addAttribute("couponList", sysTicketInfoService.localList());
        model.addAttribute("prizeCog", prizeCog);
        model.addAttribute("shopCouponList", couponCogService.queryValidCouponList(true,null));
        model.addAttribute("vpointsType", "1".equals(prizeCog.getTurntablePrizeType()) ? "0" : "1");

        return "vcode/turntable/showTurntablePrizeEdit";
    }

    /**
     * 修改幸运转盘奖品
     */
    @RequestMapping("/doTurntablePrizeEdit")
    public String doTurntablePrizeEdit(HttpSession session, VpsTurntablePrizeCog turntablePrizeCog, Model model) throws Exception {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String[] moneyMins = StringUtils.split(turntablePrizeCog.getMoneyMin(), ",");
            String[] moneyMaxs = StringUtils.split(turntablePrizeCog.getMoneyMax(), ",");
            String money = "";
            for (int i = 0;moneyMins!=null && i < moneyMins.length; i++) {
                money += moneyMins[i] + ":" + moneyMaxs[i] + ";";
            }
            if (StringUtils.isNotEmpty(money)) {
                turntablePrizeCog.setMoney(money);
            }

            String[] rangeVpointsMins = StringUtils.split(turntablePrizeCog.getRangeVpointsMin(), ",");
            String[] rangeVpointsMaxs = StringUtils.split(turntablePrizeCog.getRangeVpointsMax(), ",");
            String rangeVpoints = "";
            for (int i = 0; rangeVpointsMins!=null && i < rangeVpointsMins.length; i++) {
                rangeVpoints += rangeVpointsMins[i] + ":" + rangeVpointsMaxs[i] + ";";
            }
            if (StringUtils.isNotEmpty(rangeVpoints)) {
                turntablePrizeCog.setRangeVpoints(rangeVpoints);
            }

            String errMsg = turntableService.doTurntablePrizeEdit(turntablePrizeCog, currentUser);
            String prizeKey = KEY_VCODE_TURNTABLE_PRIZE_ACTIVITY_COG + Constant.DBTSPLIT + turntablePrizeCog.getTurntableActivityKey();
            // 删除活动缓存
            CacheUtilNew.removeGroupByKey(prizeKey);
            model.addAttribute("errMsg", errMsg);
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "新增失败");
            log.error("转盘奖品新增失败", ex);
        }
        return "forward:showTurntableActivityList.do";
    }

    /**
     * 配置转盘活动规则
     */
    @RequestMapping("/showTurntableActivityRuleCogList")
    public String showTurntableActivityRuleCog(HttpSession session, String activityKey, Model model) throws Exception {

        SysUserBasis currentUser = this.getUserBasis(session);

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("vcodeActivityKey", activityKey);
            List<VcodeActivityRebateRuleCog> resultList =
                    rebateRuleCogDao.queryRebateRuleCogListByActivityKey(map);
            String departmentIds = "";
            for (VcodeActivityRebateRuleCog vcodeActivityRebateRuleCog : resultList) {
                if (StringUtils.isNotBlank(vcodeActivityRebateRuleCog.getAreaCode())) {
                    vcodeActivityRebateRuleCog.setAreaName(rebateRuleCogService.getAreaNameByCode(vcodeActivityRebateRuleCog.getAreaCode()));
                } else if (StringUtils.isNotBlank(vcodeActivityRebateRuleCog.getDepRegionId())) {
                    departmentIds = vcodeActivityRebateRuleCog.getDepRegionId();
                    if (StringUtils.isNotBlank(vcodeActivityRebateRuleCog.getDepProvinceId())) {
                        departmentIds += "," + vcodeActivityRebateRuleCog.getDepProvinceId();
                    }
                    if (StringUtils.isNotBlank(vcodeActivityRebateRuleCog.getFirstDealerKey())) {
                        departmentIds += "," + vcodeActivityRebateRuleCog.getFirstDealerKey();
                    }
                    vcodeActivityRebateRuleCog.setAreaName(rebateRuleCogService.getDepartmentNames(departmentIds));
                }
                for (VcodeActivityVpointsCog vcodeActivityVpointsCog : vcodeActivityRebateRuleCog.getVpointsCogLst()) {
                    if (vcodeActivityVpointsCog.getPrizePercent() != null) {
                        vcodeActivityVpointsCog.setRangePercent(String.format("%.4f", vcodeActivityVpointsCog.getPrizePercent()));
                    }
                }
            }

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("activityKey", activityKey);
            model.addAttribute("projectName", "mengniu".equals(DbContextHolder.getDBType()) ? "1" : "0");
            model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(true, true, true, true, true, true, null));
            model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
                    "data_constant_config", "role_info").split(",")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/turntable/showTurntableActivityRuleCogList";
    }

    /**
     * 幸运转盘新增规则页面
     */
    @RequestMapping("/showTurntableActivityRuleCogAdd")
    public String showTurntableActivityRuleCogAdd(HttpSession session, String activityKey, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        // 查询蒙牛组织架构，默认查询大区级别
        if ("mengniu".equals(DbContextHolder.getDBType())) {
            model.addAttribute("departmentList", mnDepartmentService.queryListByMap(-1, 4));
        }

        Map<String, String> prizeTypeMap = new LinkedHashMap<>();
        prizeTypeMap.putAll(vpointsCogService.queryAllPrizeType(false, true, false, false, false, false, null));
        prizeTypeMap.putAll(sysTicketInfoService.localListForMap());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("activityKey", activityKey);
        model.addAttribute("prizeTypeMap", prizeTypeMap);
        model.addAttribute("projectServerName", DbContextHolder.getDBType());
        return "vcode/turntable/showTurntableActivityRuleCogAdd";
    }

    /**
     * 幸运转盘修改规则页面
     */
    @RequestMapping("/showTurntableActivityRuleCogEdit")
    public String showTurntableActivityRuleCogEdit(HttpSession session, String rebateRuleKey, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        // 查询当前活动规则记录
        VcodeActivityRebateRuleCog rebateRuleCog = rebateRuleCogService.findById(rebateRuleKey);
        String departmentIds = "";
        String areaName = "";
        if (StringUtils.isNotBlank(rebateRuleCog.getAreaCode())) {
            areaName = rebateRuleCogService.getAreaNameByCode(rebateRuleCog.getAreaCode());
        } else if (StringUtils.isNotBlank(rebateRuleCog.getDepRegionId())) {
            departmentIds = rebateRuleCog.getDepRegionId();
            if (StringUtils.isNotBlank(rebateRuleCog.getDepProvinceId())) {
                departmentIds += "," + rebateRuleCog.getDepProvinceId();
            }
            if (StringUtils.isNotBlank(rebateRuleCog.getFirstDealerKey())) {
                departmentIds += "," + rebateRuleCog.getFirstDealerKey();
            }
            areaName = rebateRuleCogService.getDepartmentNames(departmentIds);
        }
        Map<String, String> prizeTypeMap = new LinkedHashMap<>();
        prizeTypeMap.putAll(vpointsCogService.queryAllPrizeType(false, true, false, false, false, false, null));
        prizeTypeMap.putAll(sysTicketInfoService.localListForMap());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("rebateRuleKey", rebateRuleKey);
        model.addAttribute("prizeTypeMap", prizeTypeMap);
        model.addAttribute("rebateRuleCog", rebateRuleCog);
        model.addAttribute("areaName", areaName);
        return "vcode/turntable/showTurntableActivityRuleCogEdit";
    }

    /**
     * 新增转盘活动规则
     */
    @RequestMapping("/doTurntableActivityRuleCogAdd")
    public String doTurntableActivityRuleCogAdd(HttpSession session, VcodeActivityRebateRuleCog rebateRuleCog, String filterDateAry,
                                                String filterAreaCode, String filterDepartmentIds, String filterTimeAry, VcodeActivityRebateRuleTemplet rebateRuleTemplet, Model model) throws Exception {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            // 初始化奖项配置项
            List<VcodeActivityMoneyImport> excelList = turntableService
                    .initPrizeItem(rebateRuleCog, rebateRuleTemplet, "0");
            // 入库前先拿出老的奖项主键集合
            Set<String> oldKeys = null;
            String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                    .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + rebateRuleCog.getRebateRuleKey();
            if (excelList != null && !excelList.isEmpty()) {
                oldKeys = RedisApiUtil.getInstance().hkeys(redisKey);
            }

            // 规则 开始时间 结束时间默认值
            if (StringUtils.isBlank(filterTimeAry)) {
                filterTimeAry = "00:00:00@23:59:59";
            }

            // 写入库
            rebateRuleCogService.writeBatchVpointsCog(excelList, rebateRuleCog,
                    filterAreaCode, filterDepartmentIds, null, filterDateAry, filterTimeAry, currentUser.getUserKey(), "0", "0");


            // 删除规则对应的配置项
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST + Constant.DBTSPLIT
                    + rebateRuleCog.getVcodeActivityKey() + Constant.DBTSPLIT + rebateRuleCog.getRebateRuleKey());
            // 如果操作成功删除旧的奖项主键集合
            if (oldKeys != null && !oldKeys.isEmpty()) {
                RedisApiUtil.getInstance().delHSet(redisKey, oldKeys.toArray(new String[]{}));
            }
            model.addAttribute("errMsg", "保存成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "保存失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "保存失败");
            log.error("转盘规则保存失败", ex);
        }
        return "forward:showTurntableActivityList.do";
    }

    /**
     * 转盘活动抽奖数据
     */
    @RequestMapping("/showTurnTableRecordList")
    public String showTurnTableRecordList(HttpSession session, String queryParam, String pageParam, String activityKey, Model model) throws Exception {

        SysUserBasis currentUser = this.getUserBasis(session);
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        VpsTurntablePacksRecord queryBean = new VpsTurntablePacksRecord(queryParam);
        queryBean.setDbType(currentUser.getProjectServerName());
        try {
            List<VpsTurntablePacksRecord> resultList = turntableService.queryTurntablePacksRecordInfoByActivityKey(activityKey, queryBean, pageInfo);
            int countResult = turntableService.queryTurntablePacksRecordInfoForCount(queryBean, activityKey);
            // 查询蒙牛组织架构，默认查询大区级别
            if ("mengniu".equals(DbContextHolder.getDBType())) {
                model.addAttribute("departmentList", mnDepartmentService.queryListByMap(-1, 4));
            }
            if (StringUtils.isNotBlank(queryBean.getDepRegionId())){
                model.addAttribute("depProvinceList", mnDepartmentService.queryListByMap(Long.parseLong(queryBean.getDepRegionId()), 5));
            }
            if (StringUtils.isNotBlank(queryBean.getDepProvinceId())){
                model.addAttribute("depMarketIdList", mnDepartmentService.queryListByMap(Long.parseLong(queryBean.getDepProvinceId()), 6));
            }
            if (StringUtils.isNotBlank(queryBean.getDepMarketId())){
                model.addAttribute("depCountyList", mnDepartmentService.queryListByMap(Long.parseLong(queryBean.getDepMarketId()), 7));
            }

            model.addAttribute("vpsTurntablePrizeCogList", turntableService.queryTurntablePrizeByActivityKey(activityKey));
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("showCount", countResult);
            model.addAttribute("resultList", resultList);
            model.addAttribute("activityKey", activityKey);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("queryBean", queryBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/turntable/showTurnTableRecordList";
    }

    /**
     * 查询中奖用户名单
     *
     * @param session
     * @param request
     * @param firstPrizeInfo
     * @param model
     */
    @RequestMapping("/showMajorInfoView")
    public String showMajorInfoView(HttpSession session,
                                    HttpServletRequest request, String infoKey, Model model, String activityKey) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("activityKey", activityKey);
        model.addAttribute("majorInfo", majorInfoService.findMajorInfoByInfoKey(infoKey));
        return "vcode/turntable/showMajorInfoView";
    }

    /**
     * 导出查询结果
     *
     * @return
     */
    @RequestMapping("/exportTurnTableRecordList")
    public void exportTurnTableRecordList(HttpSession session, HttpServletResponse response, String activityKey,
                                          String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = getUserBasis(session);
            VpsTurntablePacksRecord queryBean = new VpsTurntablePacksRecord(queryParam);
            queryBean.setDbType(currentUser.getProjectServerName());
            // 导出
            turntableService.exportTurnTableRecordList(activityKey, queryBean, response, currentUser);
        } catch (Exception e) {
            log.error("导出查询结果下载失败", e);
        }
    }

}
