package com.dbt.platform.integral.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleTempletService;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.commonrule.bean.CommonRule;
import com.dbt.platform.commonrule.service.CommonRuleService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.integral.bean.*;
import com.dbt.platform.integral.service.VipDailyService;
import com.dbt.platform.publish.bean.VpsAdUp;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 积分任务配置磊
 */
@Controller
@RequestMapping("/integral")
public class VpsTaskInfoAction extends BaseAction {
    @Autowired
    private CommonRuleService commonRuleService;
    @Autowired
    private VcodeActivityRebateRuleTempletService ruleTempletService;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;
    @Autowired
    private VipDailyService vipDailyService;
    static Mapper mapper = new DozerBeanMapper();

    /**
     * 展示积分配置任务类
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showTaskConfig")
    public String showTaskConfig(HttpSession session, Model model) {
        VpsTask vpsTask = commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_2, VpsTask.class);
        model.addAttribute("vpsTask", vpsTask);
        model.addAttribute("tabsFlag", "1");
        model.addAttribute("vpsTask", vpsTask);
        String vipSwitch = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM);
        model.addAttribute("vipSwitch", vipSwitch);
        return "integral/integralMain";
    }

    /**
     * 积分配置编辑页面
     *
     * @param session
     * @param model
     * @param vpsTask
     * @return
     * @throws Exception
     */
    @RequestMapping("/doTaskConfigEdit")
    public String doTaskConfigEdit(HttpSession session, Model model, VpsTask vpsTask) throws Exception {
        CommonRule commonRule = new CommonRule();
        SysUserBasis currentUser = this.getUserBasis(session);
        commonRule.setRuleType(Constant.COMMON_RULE_TYPE.status_2);
        commonRule.setRuleValue(JSON.toJSONString(vpsTask));
        commonRule.setUpdateUser(currentUser.getUserName());
        commonRule.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
        commonRule.setCreateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
        commonRule.setCreateUser(currentUser.getUserName());
        commonRuleService.createOrUpdate(commonRule);
        model.addAttribute("vpsTask", vpsTask);
        model.addAttribute("tabsFlag", "1");
        String vipSwitch = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                DatadicKey.filterSwitchSetting.SWITCH_VIP_SYSTEM);
        model.addAttribute("vipSwitch", vipSwitch);
        return "integral/integralMain";
    }

    /**
     * 展示签到页面
     */
    @RequestMapping("/showSignInEdit")
    public String showSignInEdit(HttpSession session, Model model) {
        VpsSignIn signIn = commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_3, VpsSignIn.class);
        List<VcodeActivityVpointsCog> signInInfo = null;
        List<VcodeActivityVpointsCog> specialDayInfo = null;
        if (null != signIn) {
            signIn.setWeek(signIn.getTimeType().equals("2") ? signIn.getTimeValue() : null);
            signIn.setCycleDay(signIn.getTimeType().equals("1") ? signIn.getTimeValue() : null);
            signInInfo = vpointsCogService.queryVpointsCogByrebateRuleKey(Constant.VPOINTSkEY.commmonKey);
            specialDayInfo = CollectionUtils.isEmpty(vpointsCogService.queryVpointsCogByrebateRuleKey(Constant.VPOINTSkEY.specialKey)) ? null : vpointsCogService.queryVpointsCogByrebateRuleKey(Constant.VPOINTSkEY.specialKey);
        }
        VpsSignIn signin = commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_3, VpsSignIn.class);
        model.addAttribute("signInInfo", signin);
        model.addAttribute("signInInfo", signInInfo);
        model.addAttribute("specialDayInfo", specialDayInfo);
        model.addAttribute("signIn", signIn);
        model.addAttribute("tabsFlag", "2");
        return "integral/integralMain";
    }

    /**
     * 展示分享页面
     */
    @RequestMapping("/showShareEdit")
    public String showShareEdit(HttpSession session, Model model) {
        List<VcodeActivityVpointsCog> shareRule = null;
        VpsShare share = commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_4, VpsShare.class);
        if (null != share) {
            shareRule = vpointsCogService.queryVpointsCogByrebateRuleKey(share.getInfoKey());
            share.setCommonList(shareRule);
        }
        model.addAttribute("share", share);
        model.addAttribute("tabsFlag", "3");
        return "integral/integralMain";
    }

    /**
     * 提交积分签到编辑
     */
    @RequestMapping("/doSignInEdit")
    public String doSignInEdit(HttpSession session,
                               VpsSignIn singIn, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            CommonRule commonRule = new CommonRule();
            commonRule.setRuleType(Constant.COMMON_RULE_TYPE.status_3);
            List<VcodeActivityVpointsCog> resultList = new ArrayList<VcodeActivityVpointsCog>();
            singIn = initSignInfo(singIn, resultList);
            VpsTaskSigninInfo vpsTaskSigninInfo = mapper.map(singIn, VpsTaskSigninInfo.class);
            vpsTaskSigninInfo.setTimeValue(singIn.getTimeType().equals("2") ? singIn.getWeek() : singIn.getCycleDay());
            commonRule.setRuleValue(JSON.toJSONString(vpsTaskSigninInfo));
            commonRule.setUpdateUser(currentUser.getUserName());
            commonRule.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            commonRule.setCreateUser(currentUser.getUserName());
            commonRule.setCreateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            commonRuleService.createOrUpdate(commonRule);
            // 入库
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("vpointsCogList", resultList);
            vpointsCogService.deleteByrebateRuleKey(Constant.VPOINTSkEY.commmonKey, Constant.VPOINTSkEY.specialKey);
            vpointsCogService.batchWrite(map);
            model.addAttribute("signInInfo", singIn.getSignInInfo());
            if (StringUtils.isBlank(singIn.getTimeType())) {

            }
            model.addAttribute("specialDayInfo", StringUtils.isBlank(singIn.getTimeType()) ? null : singIn.getSpecialDayInfo());
            model.addAttribute("errMsg", "编辑成功");
            model.addAttribute("signIn", singIn);
            model.addAttribute("tabsFlag", "2");


            // 删除缓存内的奖项
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST
                    + Constant.DBTSPLIT + vpsTaskSigninInfo.getInfoKey() + Constant.DBTSPLIT + vpsTaskSigninInfo.getBasicRuleKey());
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST
                    + Constant.DBTSPLIT + vpsTaskSigninInfo.getInfoKey() + Constant.DBTSPLIT + vpsTaskSigninInfo.getSpecialRuleKey());

            // 删除奖项数量
            String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog.VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":";
            RedisApiUtil.getInstance().del(true, redisKey + vpsTaskSigninInfo.getBasicRuleKey());
            RedisApiUtil.getInstance().del(true, redisKey + vpsTaskSigninInfo.getSpecialRuleKey());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("编辑失败", ex);
        }
        return "integral/integralMain";
    }

    /**
     * 提交积分分享编辑
     */
    @RequestMapping("/doShareEdit")
    public String doShareEdit(HttpSession session, VpsShare share, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            CommonRule commonRule = new CommonRule();
            commonRule.setRuleType(Constant.COMMON_RULE_TYPE.status_4);
            List<VcodeActivityVpointsCog> resultList = new ArrayList<VcodeActivityVpointsCog>();
            share = initShare(share, resultList);
            VpsTaskShareInfo vpsTaskShareInfo = mapper.map(share, VpsTaskShareInfo.class);
            commonRule.setRuleValue(JSON.toJSONString(vpsTaskShareInfo));
            commonRule.setUpdateUser(currentUser.getUserName());
            commonRule.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            commonRule.setCreateUser(currentUser.getUserName());
            commonRule.setCreateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
            commonRuleService.createOrUpdate(commonRule);
            // 入库
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("vpointsCogList", resultList);
            vpointsCogService.deleteByrebateRuleKey(share.getInfoKey(), "");
            vpointsCogService.batchWrite(map);
            model.addAttribute("errMsg", "编辑成功");
            model.addAttribute("share", share);
            model.addAttribute("tabsFlag", "3");

            // 获取缓存内的奖项
            String cacheKeyStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST
                    + Constant.DBTSPLIT + vpsTaskShareInfo.getInfoKey() + Constant.DBTSPLIT + vpsTaskShareInfo.getInfoKey();
            CacheUtilNew.removeByKey(cacheKeyStr);

            // 删除奖项数量
            String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                    .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + vpsTaskShareInfo.getInfoKey();
            RedisApiUtil.getInstance().del(true, redisKey);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("编辑失败", ex);
        }
        return "integral/integralMain";
    }

    /**
     * 初始化签到数据
     *
     * @param signIn
     * @param resultList
     * @return
     */
    private VpsSignIn initSignInfo(VpsSignIn signIn, List<VcodeActivityVpointsCog> resultList) {
        List<VcodeActivityVpointsCog> signInInfo = new ArrayList<>();
        List<VcodeActivityVpointsCog> specialDayInfo = new ArrayList<>();
        // 概率的基数
        Map<String, Long> rangeValMap = initBaseRangeVal(signIn);
        int itemNum = signIn.getSpecialType().length;
        signIn.setInfoKey(StringUtils.isNotBlank(signIn.getInfoKey()) ? signIn.getInfoKey() : UUID.randomUUID().toString());
        for (int i = 0; i < itemNum; i++) {
            VcodeActivityVpointsCog detailItem = null;
            detailItem = new VcodeActivityVpointsCog();
            detailItem.setVpointsCogKey(UUID.randomUUID().toString());
            detailItem.setRandomType(signIn.getRandomType()[i]);
            detailItem.setCogAmounts(Long.valueOf(signIn.getCogAmounts()[i]));
            detailItem.setRestAmounts(Long.valueOf(signIn.getCogAmounts()[i]));
            detailItem.setVcodeActivityKey(signIn.getInfoKey());
            detailItem.setPrizeType("1");
            detailItem.setMaxMoney(Double.valueOf("0"));
            detailItem.setMinMoney(Double.valueOf("0"));
            // 奖项获取方式-随机
            if (Constant.PrizeRandomType.type_0.equals(detailItem.getRandomType())) {
                detailItem.setMinVpoints(Integer.valueOf(signIn.getMinVpoints()[i]));
                detailItem.setMaxVpoints(Integer.valueOf(signIn.getMaxVpoints()[i]));
            } else {
                detailItem.setMinVpoints(Integer.valueOf(signIn.getFixationVpoints()[i]));
                detailItem.setMaxVpoints(Integer.valueOf(signIn.getFixationVpoints()[i]));
            }
            detailItem.setPrizePercent(Double.valueOf(signIn.getPrizePercent()[i]));
            detailItem.setScanType(Constant.ScanType.type_1);
            detailItem.setRangeVal((long) MathUtil.round(rangeValMap.get("commonBigPrizeRangeVal") * Double.valueOf(signIn.getPrizePercent()[i]), 0));
            if (signIn.getSpecialType()[i].equals("0")) {
                detailItem.setRebateRuleKey(Constant.VPOINTSkEY.commmonKey);
                signInInfo.add(detailItem);
            }
            if (signIn.getSpecialType()[i].equals("1") && !StringUtils.isBlank(signIn.getTimeType())) {
                detailItem.setRebateRuleKey(Constant.VPOINTSkEY.specialKey);
                specialDayInfo.add(detailItem);
            }
            resultList.add(detailItem);
        }
        signIn.setSignInInfo(signInInfo);
        signIn.setSpecialDayInfo(specialDayInfo);
        signIn.setSpecialRuleKey(Constant.VPOINTSkEY.specialKey);
        signIn.setBasicRuleKey(Constant.VPOINTSkEY.commmonKey);
        return signIn;
    }

    /**
     * 初始化分享数据
     *
     * @param share
     * @param resultList
     * @return
     */
    private VpsShare initShare(VpsShare share, List<VcodeActivityVpointsCog> resultList) {
        // 概率的基数
        Map<String, Long> rangeValMap = initBaseRangeValForShare(share);
        int itemNum = share.getSpecialType().length;
        share.setInfoKey(StringUtils.isNotBlank(share.getInfoKey()) ? share.getInfoKey() : UUID.randomUUID().toString());
        for (int i = 0; i < itemNum; i++) {
            VcodeActivityVpointsCog detailItem = null;
            detailItem = new VcodeActivityVpointsCog();
            detailItem.setVpointsCogKey(UUID.randomUUID().toString());
            detailItem.setRebateRuleKey(share.getInfoKey());
            detailItem.setRandomType(share.getRandomType()[i]);
            detailItem.setCogAmounts(Long.valueOf(share.getCogAmounts()[i]));
            detailItem.setRestAmounts(Long.valueOf(share.getCogAmounts()[i]));
            detailItem.setVcodeActivityKey(share.getInfoKey());
            detailItem.setPrizeType("1");
            detailItem.setMaxMoney(Double.valueOf("0"));
            detailItem.setMinMoney(Double.valueOf("0"));
            // 奖项获取方式-随机
            if (Constant.PrizeRandomType.type_0.equals(detailItem.getRandomType())) {
                detailItem.setMinVpoints(Integer.valueOf(share.getMinVpoints()[i]));
                detailItem.setMaxVpoints(Integer.valueOf(share.getMaxVpoints()[i]));
            } else {
                detailItem.setMinVpoints(Integer.valueOf(share.getFixationVpoints()[i]));
                detailItem.setMaxVpoints(Integer.valueOf(share.getFixationVpoints()[i]));
            }
            detailItem.setPrizePercent(Double.valueOf(share.getPrizePercent()[i]));
            detailItem.setScanType(Constant.ScanType.type_1);
            detailItem.setRangeVal((long) MathUtil.round(rangeValMap.get("commonBigPrizeRangeVal") * Double.valueOf(share.getPrizePercent()[i]), 0));
            resultList.add(detailItem);
        }
        share.setCommonList(resultList);
        return share;
    }

    /**
     * 计算各类奖项配置项概述基数
     */
    private Map<String, Long> initBaseRangeValForShare(VpsShare share) {

        long commonRangeVal = 10000;
        Map<String, Long> map = new HashMap<>();
        map.put("commonRangeVal", commonRangeVal); // 普扫百分之一的基数

        // 概率的基数
        double firstBigPrizeTotalPercent = 0D;
        double commonBigPrizeTotalPercent = 0D;
        if (share.getScanType() != null) {
            int itemNum = share.getScanType().length;
            for (int i = 0; i < itemNum; i++) {
                commonBigPrizeTotalPercent += Double.valueOf(share.getPrizePercent()[i]);

            }
        }
        long firstBigPrizeRangeVal = commonRangeVal;
        if (firstBigPrizeTotalPercent >= 100) {
            firstBigPrizeRangeVal = commonRangeVal * 100;
        } else if (firstBigPrizeTotalPercent > 0) {
            firstBigPrizeRangeVal = (long) (commonRangeVal / (100 - commonBigPrizeTotalPercent) * 100);
        }
        map.put("firstBigPrizeRangeVal", firstBigPrizeRangeVal);
        long commonBigPrizeRangeVal = commonRangeVal;
        if (commonBigPrizeTotalPercent >= 100) {
            commonBigPrizeRangeVal = commonRangeVal * 100;
        } else if (commonBigPrizeTotalPercent > 0) {
            commonBigPrizeRangeVal = (long) (commonRangeVal / (100 - commonBigPrizeTotalPercent) * 100);
        }
        map.put("commonBigPrizeRangeVal", commonBigPrizeRangeVal);
        return map;
    }

    /**
     * 计算各类奖项配置项概述基数
     */
    private Map<String, Long> initBaseRangeVal(VpsSignIn singIn) {

        long commonRangeVal = 10000;
        Map<String, Long> map = new HashMap<>();
        map.put("commonRangeVal", commonRangeVal); // 普扫百分之一的基数

        // 概率的基数
        double firstBigPrizeTotalPercent = 0D;
        double commonBigPrizeTotalPercent = 0D;
        if (singIn.getScanType() != null) {
            int itemNum = singIn.getScanType().length;
            for (int i = 0; i < itemNum; i++) {
                commonBigPrizeTotalPercent += Double.valueOf(singIn.getPrizePercent()[i]);

            }
        }
        long firstBigPrizeRangeVal = commonRangeVal;
        if (firstBigPrizeTotalPercent >= 100) {
            firstBigPrizeRangeVal = commonRangeVal * 100;
        } else if (firstBigPrizeTotalPercent > 0) {
            firstBigPrizeRangeVal = (long) (commonRangeVal / (100 - commonBigPrizeTotalPercent) * 100);
        }
        map.put("firstBigPrizeRangeVal", firstBigPrizeRangeVal);
        long commonBigPrizeRangeVal = commonRangeVal;
        if (commonBigPrizeTotalPercent >= 100) {
            commonBigPrizeRangeVal = commonRangeVal * 100;
        } else if (commonBigPrizeTotalPercent > 0) {
            commonBigPrizeRangeVal = (long) (commonRangeVal / (100 - commonBigPrizeTotalPercent) * 100);
        }
        map.put("commonBigPrizeRangeVal", commonBigPrizeRangeVal);
        return map;
    }

    /**
     * 会员日常任务列表
     */
    @RequestMapping("/showVipDailyList")
    public String showVipDailyList(HttpSession session, String pageParam, String queryParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVipDailyTaskCog queryBean = new VpsVipDailyTaskCog(queryParam);
            List<VpsVipDailyTaskCog> resultList = vipDailyService.queryForLst(queryBean, pageInfo);
            int countResult = vipDailyService.queryForCount(queryBean);

            String dataDicValue = DatadicUtil.getDataDicValue("data_constant_config", "vip_daily_task_type");
            Map<String, String> taskTypeMap = (Map<String, String>) JSONObject.parse(dataDicValue);
            model.addAttribute("taskTypeMap", taskTypeMap);
            for (VpsVipDailyTaskCog vpsVipDailyTaskCog : resultList) {
                vpsVipDailyTaskCog.setTaskType(taskTypeMap.get(vpsVipDailyTaskCog.getTaskType()));
            }
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("taskTypeMap", taskTypeMap);
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
            model.addAttribute("tabsFlag", "4");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "integral/integralMain";
    }

    /**
     * 新增任务页面
     */
    @RequestMapping("/showVipDailyAdd")
    public String showVipDailyAdd(HttpSession session, Model model) {
        String dataDicValue = DatadicUtil.getDataDicValue("data_constant_config", "vip_daily_task_type");
        Map<String, String> taskTypeMap = (Map<String, String>) JSONObject.parse(dataDicValue);
        model.addAttribute("taskTypeMap", taskTypeMap);
        return "integral/showVipDailyAdd";
    }

    /**
     * 新增会员日常任务
     */
    @RequestMapping("/doVipDailyAdd")
    public String doVipDailyAdd(HttpSession session,
                                VpsVipDailyTaskCog vpsVipDailyTaskCog, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String vipDailyTask = vipDailyService.createVipDailyTask(currentUser, vpsVipDailyTaskCog);
            model.addAttribute("errMsg", vipDailyTask);
            String cacheKeyStr = CacheUtilNew.cacheKey.vipTask.KEY_DAILY_TASK;
            CacheUtilNew.removeByKey(cacheKeyStr);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "添加失败");
            log.error("编辑失败", ex);
        }
        model.addAttribute("tabsFlag", "4");
        return "forward:showVipDailyList.do";
    }

    /**
     * 修改任务页面
     */
    @RequestMapping("/showVipDailyEdit")
    public String showVipDailyEdit(HttpSession session, Model model,String infoKey) {
        String dataDicValue = DatadicUtil.getDataDicValue("data_constant_config", "vip_daily_task_type");
        Map<String, String> taskTypeMap = (Map<String, String>) JSONObject.parse(dataDicValue);
        VpsVipDailyTaskCog vpsVipDailyTaskCog = vipDailyService.findById(infoKey);
        model.addAttribute("taskTypeMap", taskTypeMap);
        model.addAttribute("vpsVipDailyTaskCog", vpsVipDailyTaskCog);
        return "integral/showVipDailyEdit";
    }

    /**
     * 修改会员日常任务
     */
    @RequestMapping("/doVipDailyEdit")
    public String doVipDailyEdit(HttpSession session,
                                VpsVipDailyTaskCog vpsVipDailyTaskCog, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String vipDailyTask = vipDailyService.updateVipDailyTask(currentUser, vpsVipDailyTaskCog);
            model.addAttribute("errMsg", vipDailyTask);
            String cacheKeyStr = CacheUtilNew.cacheKey.vipTask.KEY_DAILY_TASK;
            CacheUtilNew.removeByKey(cacheKeyStr);
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            log.error("修改失败", ex);
        }
        model.addAttribute("tabsFlag", "4");
        return "forward:showVipDailyList.do";
    }
}
