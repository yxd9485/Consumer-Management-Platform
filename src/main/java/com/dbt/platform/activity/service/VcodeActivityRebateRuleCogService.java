/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:27:23 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.service;

import com.alibaba.fastjson.JSON;
import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.*;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.bean.*;
import com.dbt.platform.activity.dao.IVcodeActivityRebateRuleCogDao;
import com.dbt.platform.activity.util.VcodeActivityMoneyExcel;
import com.dbt.platform.doubleprize.service.VpsVcodeDoublePrizeCogService;
import com.dbt.platform.fission.service.IVpsVcodeActivateRedEnvelopeRuleCogService;
import com.dbt.platform.mn.bean.MnAgencyEntity;
import com.dbt.platform.mn.bean.MnDepartmentEntity;
import com.dbt.platform.mn.service.MnAgencyService;
import com.dbt.platform.mn.service.MnDepartmentService;
import com.dbt.platform.promotion.service.VpsVcodeBindPromotionCogService;
import com.dbt.platform.signin.service.VpsVcodeSigninCogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

/**
 * V码活动返利规则Service
 *
 * @version V1.0.0 </br>
 * @auther hanshimeng </br>
 * @createTime 2016年12月5日 </br>
 */
@Service
public class VcodeActivityRebateRuleCogService extends BaseService<VcodeActivityRebateRuleCog> {

    @Autowired
    private IVcodeActivityRebateRuleCogDao rebateRuleCogDao;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;
    @Autowired
    private VcodeActivityService vcodeActivityService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private VpsVcodeSigninCogService signinCogService;
    @Autowired
    private VpsVcodeBindPromotionCogService promotionCogService;
    @Autowired
    private VpsVcodeDoublePrizeCogService doublePrizeCogService;
    @Autowired
    private VcodeActivityRebateRuleTempletService rebateRuleTempletService;
    @Autowired
    private CRMServiceServiceImpl crmServiceService;
    @Autowired
    private IVpsVcodeActivateRedEnvelopeRuleCogService iVpsVcodeActivateRedEnvelopeRuleCogService;
    @Autowired
    private MnDepartmentService mnDepartmentService;
    @Autowired
    private MnAgencyService mnAgencyService;

    /**
     * 插入活动规则
     *
     * @param rebateRuleCog
     */
    public void addRebateRuleCog(VcodeActivityRebateRuleCog rebateRuleCog) {
        rebateRuleCog.setAllowanceaMinMoney(StringUtils.defaultIfBlank(rebateRuleCog.getAllowanceaMinMoney(), null));
        rebateRuleCog.setAllowanceaMaxMoney(StringUtils.defaultIfBlank(rebateRuleCog.getAllowanceaMaxMoney(), null));
        rebateRuleCog.setSpecialLabel(StringUtils.defaultIfBlank(rebateRuleCog.getSpecialLabel(), "").trim());
        rebateRuleCogDao.create(rebateRuleCog);
        logService.saveLog("vcodeActivityRebateRule", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(rebateRuleCog), "创建规则:" + getRebateRuleCogName(rebateRuleCog));
    }

    /**
     * 根据规则主键删除活动规则
     *
     * @param rebateRuleKey 活动规则主键
     */
    public void deleteRebateRuleCogById(String rebateRuleKey, String vcodeActivityKey, String userKey) {
        VcodeActivityRebateRuleCog ruleCog = findById(rebateRuleKey);

        // 组建参数
        Map<String, Object> map = new HashMap<>();
        map.put("rebateRuleKey", rebateRuleKey);
        map.put("updateUser", userKey);
        map.put("updateTime", DateUtil.getDateTime());

        // 删除活动规则
        rebateRuleCogDao.deleteByrebateRuleKey(map);

        // 废弃此活动规则对应的配置
        vpointsCogService.removeByrebateRuleKey(map);

        // 如果有效规则记录条数为0，则修改活动配置状态
        int ruleNum = rebateRuleCogDao.getValidRebateRuleCogNumByActivityKey(vcodeActivityKey);
        if (ruleNum == 0) {
            // 改写活动中的配置状态
            VcodeActivityCog activity = new VcodeActivityCog();
            activity.setVcodeActivityKey(vcodeActivityKey);
            activity.setMoneyConfigFlag("0");
            vcodeActivityService.changeStatus(activity);
        }

        Map<String, Object> logMap = new HashMap<>();
        logMap.put("rebateRuleKey", rebateRuleKey);
        logMap.put("vcodeActivityKey", vcodeActivityKey);
        logService.saveLog("vcodeActivityRebateRule", Constant.OPERATION_LOG_TYPE.TYPE_3,
                JSON.toJSONString(logMap), "删除规则:" + getRebateRuleCogName(ruleCog));

        // 刷新缓存
        try {
            // 删除活动规则List
            CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_ACTIVITY_REBATE_RULE_COG + Constant.DBTSPLIT + vcodeActivityKey);

            // 删除规则对应的配置项
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey
                    .vodeActivityKey.KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST +
                    Constant.DBTSPLIT + vcodeActivityKey + Constant.DBTSPLIT + rebateRuleKey);

            // 删除当前规则bean
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_ACTIVITY_REBATE_RULE_BEAN + Constant.DBTSPLIT + rebateRuleKey);

            if (null != ruleCog) {
                if (StringUtils.isBlank(ruleCog.getAppointRebateRuleKey())) {
                    // 删除当前规则消费的总积分
                    RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                            .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + rebateRuleKey);

                    // 删除当前规则消费的总金额
                    RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                            .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + rebateRuleKey);

                    // 删除当前规则消费的总瓶数
                    RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                            .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + rebateRuleKey);

                    // 删除当前规则下用户扫码次数
                    RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                            .VCODE_USER_SCAN_QRCODE_COUNT + Constant.DBTSPLIT + rebateRuleKey);
                }
            }

            // 删除当前规则消费的总积分
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + rebateRuleKey + "self");
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + rebateRuleKey + "selfRemind");

            // 删除当前规则消费的总金额
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + rebateRuleKey + "self");
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + rebateRuleKey + "selfRemind");

            // 删除当前规则消费的总瓶数
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + rebateRuleKey + "self");
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + rebateRuleKey + "selfRemind");

            // 删除奖项配置项剩余数量缓存
            RedisApiUtil.getInstance().del(true, RedisApiUtil.CacheKey.ActivityVpointsCog
                    .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + rebateRuleKey);

        } catch (Exception ex) {
            log.error("删除活动规则配置项，刷新缓存失败", ex);
        }

    }

    /**
     * 根据规则主键删除活动规则
     *
     * @param rebateRuleKey 活动规则主键
     */
    public void doRebateRuleDelete(String vcodeActivityKey, String areaCode, String departmentIds, String userKey) {

        // 获取当前活动下指定区域下的所有规则
        Map<String, Object> map = new HashMap<>();
        map.put("vcodeActivityKey", vcodeActivityKey);
        map.put("areaCode", areaCode);
        if (StringUtils.isNotBlank(departmentIds)) {
            String[] groupdepartmentId = departmentIds.split(",");
            if (groupdepartmentId.length > 0) {
                map.put("depRegionId", groupdepartmentId[0]);
            }
            if (groupdepartmentId.length > 1 && StringUtils.isNotBlank(groupdepartmentId[1])) {
                map.put("depProvinceId", groupdepartmentId[1]);
            }
            if (groupdepartmentId.length > 2 && StringUtils.isNotBlank(groupdepartmentId[2])) {
                map.put("firstDealerKey", groupdepartmentId[2]);
            }
        }
        List<VcodeActivityRebateRuleCog> rebateRuleCogLst =
                rebateRuleCogDao.queryOverdueRebateRuleCogList(map);
        for (VcodeActivityRebateRuleCog item : rebateRuleCogLst) {
            this.deleteRebateRuleCogById(item.getRebateRuleKey(), vcodeActivityKey, userKey);
        }
    }

    /**
     * 根据活动KEY获取活动区域规则列表
     *
     * @param </br>
     * @return List<VcodeActivityRebateRuleArea> </br>
     * @throws Exception
     */
    public List<VcodeActivityRebateRuleArea> queryRebateRuleAreaListByActivityKey(
            VcodeActivityCog activityCog, String tabsFlag) throws Exception {
        if (null == activityCog) {
            return null;
        }

        // 获取活动下的所有有效规则
        String isValid = "2"; // 全部
        List<VcodeActivityRebateRuleCog> rebateRuleCogLst =
                queryRebateRuleCogListByActivityKey(activityCog.getVcodeActivityKey(), null, isValid, false, tabsFlag);

        // 活动有效标志
        boolean activityValidFlag = DateUtil.getDate().compareTo(activityCog.getEndDate()) <= 0;

        // 按区域或组织机构分组
        String areaCode = "";
        String tempDepartmentIds = "";
        String tempOrganizationNames = "";
        String departmentIds = "";
        String organizationNames = "";
        int scanRuleBottle = 0;
        double scanRuleMoney = 0;
        VcodeActivityRebateRuleArea rebateRuleArea = null;
        List<VcodeActivityRebateRuleArea> rebateRuleAreaLst = new ArrayList<>();

        for (VcodeActivityRebateRuleCog ruleICogItem : rebateRuleCogLst) {

            // 计算实际单瓶
            scanRuleBottle = Integer.parseInt(StringUtils.defaultIfBlank(
                    RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.vodeActivityKey.KEY_REBATE_RULE_BUDGET_BOTTLE + ruleICogItem.getRebateRuleKey()), "0"));
            scanRuleMoney = Double.parseDouble(StringUtils.defaultIfBlank(
                    RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.vodeActivityKey.KEY_REBATE_RULE_BUDGET_MONEY + ruleICogItem.getRebateRuleKey()), "0.00"));
            if (scanRuleBottle > 0) {
                ruleICogItem.setMoneyDanpingReal(scanRuleMoney / scanRuleBottle);
            }

            // 规则按区域分组
            if (StringUtils.isNotBlank(ruleICogItem.getAreaCode())) {
                if (areaCode.equals(ruleICogItem.getAreaCode())) {
                    rebateRuleArea.getRebateRuleCogLst().add(ruleICogItem);

                } else {
                    areaCode = ruleICogItem.getAreaCode();
                    if ("000000".equals(areaCode)) {
                        rebateRuleArea = new VcodeActivityRebateRuleArea(
                                ruleICogItem.getAreaCode(), "全部", null, ruleICogItem);

                    } else if ("000001".equals(areaCode)) {
                        rebateRuleArea = new VcodeActivityRebateRuleArea(
                                ruleICogItem.getAreaCode(), "省外", null, ruleICogItem);

                    } else {
                        rebateRuleArea = new VcodeActivityRebateRuleArea(
                                ruleICogItem.getAreaCode(), getAreaNameByCode(ruleICogItem.getAreaCode()), null, ruleICogItem);
                    }
                    rebateRuleAreaLst.add(rebateRuleArea);
                }
            }
            // 蒙牛组织机构
            else if (StringUtils.isNotBlank(ruleICogItem.getDepRegionId())) {
                departmentIds = ruleICogItem.getDepRegionId();
                if (StringUtils.isNotBlank(ruleICogItem.getDepProvinceId())) {
                    departmentIds += "," + ruleICogItem.getDepProvinceId();
                }
                if (StringUtils.isNotBlank(ruleICogItem.getFirstDealerKey())) {
                    departmentIds += "," + ruleICogItem.getFirstDealerKey();
                }

                if (tempDepartmentIds.equals(departmentIds)) {
                    rebateRuleArea.getRebateRuleCogLst().add(ruleICogItem);

                } else {
                    tempDepartmentIds = departmentIds;
                    rebateRuleArea = new VcodeActivityRebateRuleArea(
                            null, getDepartmentNames(departmentIds), departmentIds, ruleICogItem);
                    rebateRuleAreaLst.add(rebateRuleArea);
                }

            }
            // 青啤组织机构
            else if (StringUtils.isNotBlank(ruleICogItem.getBigRegionName())) {
            	organizationNames = ruleICogItem.getBigRegionName();
                if (StringUtils.isNotBlank(ruleICogItem.getSecondaryName())) {
                	organizationNames += "," + ruleICogItem.getSecondaryName();
                }

                if (tempOrganizationNames.equals(organizationNames)) {
                    rebateRuleArea.getRebateRuleCogLst().add(ruleICogItem);

                } else {
                    tempOrganizationNames = organizationNames;
                    rebateRuleArea = new VcodeActivityRebateRuleArea(
                            null, getOrganizationNames(organizationNames), organizationNames, ruleICogItem);
                    rebateRuleAreaLst.add(rebateRuleArea);
                }

            }


            // 初始化有效规则的预警
            if (activityValidFlag && "1".equals(tabsFlag)) {
                initRebateRuleWaring(rebateRuleArea, ruleICogItem);
            }
        }

        return rebateRuleAreaLst;
    }

    /**
     * 初始化规则预警
     */
    private void initRebateRuleWaring(VcodeActivityRebateRuleArea rebateRuleArea, VcodeActivityRebateRuleCog ruleCogItem) {

        // 规则到期 :规则有效期大于等于7天、剩余天数小于等于3天
        try {
            if ((Constant.rebateRuleType.RULE_TYPE_1.equals(ruleCogItem.getRuleType())
                    || Constant.rebateRuleType.RULE_TYPE_2.equals(ruleCogItem.getRuleType()))
                    && DateUtil.diffDays(ruleCogItem.getEndDate(), ruleCogItem.getBeginDate()) > 7) {
                long diffDays = DateUtil.diffDays(ruleCogItem.getEndDate(), DateUtil.getDate());
                if (diffDays >= 0 && diffDays <= 3) {
                    ruleCogItem.setExpireWarning(true);
                    rebateRuleArea.setExpireWarning(true);
                }
            }
        } catch (Exception e) {
            log.error("初始化规则预警-规则到期异常", e);
        }

        // 规则剩余金额小于10%
        if (StringUtils.isNotBlank(ruleCogItem.getRuleTotalMoney())
                && Double.valueOf(ruleCogItem.getRuleTotalMoney()) > 0
                && ruleCogItem.getConsumeMoneyRemind() / Double.valueOf(ruleCogItem.getRuleTotalMoney()) > 0.9) {
            ruleCogItem.setMoneyWarning(true);
            rebateRuleArea.setMoneyWarning(true);
        }

        // 规则剩余积分小于10%
        if (StringUtils.isNotBlank(ruleCogItem.getRuleTotalVpoints())
                && Double.valueOf(ruleCogItem.getRuleTotalVpoints()) > 0
                && ruleCogItem.getConsumeVpointsRemind() / Double.valueOf(ruleCogItem.getRuleTotalVpoints()) > 0.9) {
            ruleCogItem.setVpointsWarning(true);
            rebateRuleArea.setVpointsWarning(true);
        }

        // 规则剩余红包个数小于10%
        if (StringUtils.isNotBlank(ruleCogItem.getRuleTotalPrize())
                && Double.valueOf(ruleCogItem.getRuleTotalPrize()) > 0
                && ruleCogItem.getConsumeBottleRemind() / Double.valueOf(ruleCogItem.getRuleTotalPrize()) > 0.9) {
            ruleCogItem.setPrizeNumWarning(true);
            rebateRuleArea.setPrizeNumWarning(true);
        }

    }

    /**
     * 根据活动KEY获取活动规则列表
     *
     * @param vcodeActivityKey </br>
     * @param areaCode         </br>
     * @param isValid          是否有效 0有效 1无效 2全部</br>
     * @param flag             是否查询含有限制金额、瓶数或次数的规则  true是   false否</br>
     * @return List<VcodeActivityRebateRuleCog> </br>
     * @throws Exception
     */
    public List<VcodeActivityRebateRuleCog> queryRebateRuleCogListByActivityKey(
            String vcodeActivityKey, String areaCode, String isValid, boolean flag) throws Exception {
        return queryRebateRuleCogListByActivityKey(vcodeActivityKey, areaCode, isValid, flag, null);
    }

    public List<VcodeActivityRebateRuleCog> queryRebateRuleCogListByActivityKey(
            String vcodeActivityKey, String areaCode, String isValid, boolean flag, String tabsFlag) throws Exception {

        // 获取活动的规则
        Map<String, Object> map = new HashMap<>();
        map.put("vcodeActivityKey", vcodeActivityKey);
        map.put("areaCode", areaCode);
        map.put("isValid", isValid);
        map.put("flag", flag ? 1 : 0);
        map.put("tabsFlag", tabsFlag);
        List<VcodeActivityRebateRuleCog> rebateRuleCogLst =
                rebateRuleCogDao.queryRebateRuleCogListByActivityKey(map);

        // 缓存校验
        boolean cacheFlag = false;
        Object cacheObj = null;
        VcodeActivityVpointsCog vpointsCogTemp = null;
        String cacheKeyStr = CacheUtilNew.cacheKey.vodeActivityKey
                .KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST + Constant.DBTSPLIT + vcodeActivityKey + Constant.DBTSPLIT;

        // 计算概率占比
        String totalRuleKey;
        List<VcodeActivityVpointsCog> vpointsCogLst = null;
        for (VcodeActivityRebateRuleCog ruleCog : rebateRuleCogLst) {
            totalRuleKey = "";
            if (ruleCog.getRestrictMoney() > 0 || ruleCog.getRestrictBottle() > 0) {
                if (StringUtils.isBlank(ruleCog.getAppointRebateRuleKey())) {
                    totalRuleKey = ruleCog.getRebateRuleKey();
                } else {
                    totalRuleKey = ruleCog.getAppointRebateRuleKey();
                }
            }

            // 限制时间类型：0 规则时间，1每天
            String dateStr = "Total";
            String restrictTimeType = ruleCog.getRestrictTimeType();
            if ("1".equals(restrictTimeType)) {
                dateStr = DateUtil.getDate();
            }

            // 当前规则消费及预警积分
            String consumeVpointsRedisKey = CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + ruleCog.getRebateRuleKey() + "self";
            ruleCog.setConsumeVpoints(Integer.valueOf(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().getHSet(consumeVpointsRedisKey, dateStr), "0")));
            ruleCog.setConsumeVpointsRemind(Integer.valueOf(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().get(consumeVpointsRedisKey + "Remind"), "0")));

            // 当前规则消费及预警金额
            String consumeMoneyRedisKey = CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + ruleCog.getRebateRuleKey() + "self";
            ruleCog.setConsumeMoney(Double.valueOf(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().getHSet(consumeMoneyRedisKey, dateStr), "0.00")));
            ruleCog.setConsumeMoneyRemind(Double.valueOf(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().get(consumeMoneyRedisKey + "Remind"), "0.00")));

            // 当前规则消费及预警的总瓶数
            String consumeBottleRedisKey = CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + ruleCog.getRebateRuleKey() + "self";
            ruleCog.setConsumeBottle(Integer.valueOf(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().getHSet(consumeBottleRedisKey, dateStr), "0")));
            ruleCog.setConsumeBottleRemind(Integer.valueOf(StringUtils.defaultIfBlank(RedisApiUtil.getInstance().get(consumeBottleRedisKey + "Remind"), "0")));


            // 指定规则的总积分、总金额及总瓶数
            if (!StringUtils.isBlank(totalRuleKey)) {
                // 指定规则消费的总积分
                String totalConsumeVpoints = RedisApiUtil.getInstance().getHSet(CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + totalRuleKey, dateStr);
                ruleCog.setTotalConsumeVpoints(Integer.parseInt(StringUtils.isBlank(totalConsumeVpoints) ? "0" : totalConsumeVpoints));

                // 指定规则消费的总金额
                String totalConsumeMoney = RedisApiUtil.getInstance().getHSet(CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + totalRuleKey, dateStr);
                ruleCog.setTotalConsumeMoney(Double.parseDouble(StringUtils.isBlank(totalConsumeMoney) ? "0.00" : totalConsumeMoney));

                // 指定规则消费的总瓶数
                String totalConsumeBottle = RedisApiUtil.getInstance().getHSet(CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + totalRuleKey, dateStr);
                ruleCog.setTotalConsumeBottle(Integer.parseInt(StringUtils.isBlank(totalConsumeBottle) ? "0" : totalConsumeBottle));
            } else {
                ruleCog.setTotalConsumeVpoints(-1);
                ruleCog.setTotalConsumeMoney(-1.00);
                ruleCog.setTotalConsumeBottle(-1);
            }

            // 校验当前规则下的配置项的状态
            cacheFlag = false;
            vpointsCogTemp = null;
            cacheObj = CacheUtilNew.getCacheValue(cacheKeyStr + ruleCog.getRebateRuleKey());
            if (null != cacheObj && !StringUtils.isBlank(cacheObj.toString())) {
                List<VcodeActivityVpointsCog> tempLst = JSON.parseArray(cacheObj.toString(), VcodeActivityVpointsCog.class);
                if (tempLst != null && tempLst.size() > 0) {
                    vpointsCogTemp = tempLst.get(0);
                }
            }


            vpointsCogLst = ruleCog.getVpointsCogLst();
            if (vpointsCogLst != null && !vpointsCogLst.isEmpty()) {
                String totalRangeValKey = "";
                Map<String, Double> totalRangeValMap = new HashMap<>();
                for (VcodeActivityVpointsCog vpointsCog : vpointsCogLst) {
                    if (vpointsCog.getPrizePercent() != null) {
                        vpointsCog.setRangePercent(String.format("%.4f", vpointsCog.getPrizePercent()));
                    }
                    totalRangeValKey = vpointsCog.getScanType() + "_" + StringUtils.defaultIfBlank(vpointsCog.getScanNum(), "");
                    if (totalRangeValMap.containsKey(totalRangeValKey)) {
                        totalRangeValMap.put(totalRangeValKey, totalRangeValMap.get(totalRangeValKey) + vpointsCog.getRangeVal());
                    } else {
                        totalRangeValMap.put(totalRangeValKey, Double.valueOf(vpointsCog.getRangeVal()));
                    }
                    if (vpointsCogTemp == null || vpointsCog
                            .getVpointsCogKey().equals(vpointsCogTemp.getVpointsCogKey())) {
                        cacheFlag = true;
                    }
                }

                // 计算各项的占比(为兼容老数据)
                if (StringUtils.isBlank(vpointsCogLst.get(0).getRangePercent())) {
                    for (VcodeActivityVpointsCog vpointsCog : vpointsCogLst) {
                        totalRangeValKey = vpointsCog.getScanType() + "_" + StringUtils.defaultIfBlank(vpointsCog.getScanNum(), "");
                        if (totalRangeValMap.containsKey(totalRangeValKey)) {
                            vpointsCog.setRangePercent(String.format("%.4f", vpointsCog.getRangeVal() / totalRangeValMap.get(totalRangeValKey) * 100));
                        }
                    }
                }

                // 缓存校验未通过
                if (!cacheFlag) {
                    ruleCog.setValidMsg("缓存异常，重新导入");
                }
            }

            // 设置当天失效规则, 是否有效标志isValid 0有效  1无效
            if ("0".equals(ruleCog.getIsValid())) {
                String invalidEveryDayKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_INVALID_EVERYDAY_ACTIVITY_REBATE_RULE_COG;
                Set<String> invalidKeyList = RedisApiUtil.getInstance().getSet(invalidEveryDayKey);
                if (null != invalidKeyList) {
                    for (String invalidKey : invalidKeyList) {
                        if (invalidKey.equals(ruleCog.getRebateRuleKey())) {
                            ruleCog.setIsValid("1");
                            break;
                        }
                    }
                }
            }
        }
        return rebateRuleCogLst;
    }

    /**
     * 根据规则主键查询活动规则
     *
     * @param rebateRuleKey 活动规则主键
     */
    public VcodeActivityRebateRuleCog findById(String rebateRuleKey) {
        if (StringUtils.isBlank(rebateRuleKey)) return null;
        VcodeActivityRebateRuleCog rebateRuleCog = rebateRuleCogDao.findById(rebateRuleKey);
        if (StringUtils.isNotBlank(rebateRuleCog.getAreaCode())) {
            rebateRuleCog.setAreaName(getAreaNameByCode(rebateRuleCog.getAreaCode()));
        } else {
        	if ("mengniu".equals(DbContextHolder.getDBType())) {
        		String departmentIds = rebateRuleCog.getDepRegionId();
                if (StringUtils.isNotBlank(rebateRuleCog.getDepProvinceId())) {
                    departmentIds += "," + rebateRuleCog.getDepProvinceId();
                }
                if (StringUtils.isNotBlank(rebateRuleCog.getFirstDealerKey())) {
                    departmentIds += "," + rebateRuleCog.getFirstDealerKey();
                }
                rebateRuleCog.setAreaName(getDepartmentNames(departmentIds));
        	}else {
        		String organizationNames = rebateRuleCog.getBigRegionName();
                if (StringUtils.isNotBlank(rebateRuleCog.getSecondaryName())) {
                	organizationNames += "," + rebateRuleCog.getSecondaryName();
                }
                rebateRuleCog.setAreaName(organizationNames);
        	}
            
        }

        return rebateRuleCog;
    }

    @SuppressWarnings("unchecked")
    public void updateRebateRuleCog(VcodeActivityRebateRuleCog rebateRuleCog) {

        // 更新活动规则
        rebateRuleCog.setAllowanceaMinMoney(StringUtils.defaultIfBlank(rebateRuleCog.getAllowanceaMinMoney(), null));
        rebateRuleCog.setAllowanceaMaxMoney(StringUtils.defaultIfBlank(rebateRuleCog.getAllowanceaMaxMoney(), null));
        rebateRuleCog.setSpecialLabel(StringUtils.defaultIfBlank(rebateRuleCog.getSpecialLabel(), "").trim());
        rebateRuleCogDao.updateRebateRuleCog(rebateRuleCog);

        try {
            // 删除活动规则
            CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_ACTIVITY_REBATE_RULE_COG + Constant.DBTSPLIT + rebateRuleCog.getVcodeActivityKey());
            // 删除规则缓存
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_REBATE_RULE_BEAN
                    + Constant.DBTSPLIT + rebateRuleCog.getRebateRuleKey());

            // 活动无效规则主键集合
            String keyString = CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_INVALID_ACTIVITY_REBATE_RULE_COG + Constant.DBTSPLIT + rebateRuleCog.getVcodeActivityKey();
            List<String> invalidRuleInfokeyList = (List<String>) CacheUtilNew.getCacheValue(keyString);
            if (invalidRuleInfokeyList != null && invalidRuleInfokeyList.contains(rebateRuleCog.getRebateRuleKey())) {
                invalidRuleInfokeyList.remove(rebateRuleCog.getRebateRuleKey());
                CacheUtilNew.setCacheValue(keyString, invalidRuleInfokeyList);
            }

        } catch (Exception e) {
            log.error("删除活动规则缓存失败", e);
        }
        logService.saveLog("vcodeActivityRebateRule", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(rebateRuleCog), "修改规则:" + getRebateRuleCogName(rebateRuleCog));
    }

    /**
     * 初始化界面回传的奖项配置项
     *
     * @param prizeCogType
     * @param clientFile
     * @param ruleTemplet
     * @return
     * @throws Exception
     */
    public List<VcodeActivityMoneyImport> initPrizeItem(VcodeActivityRebateRuleCog rebateRuleCog,
                                                        MultipartFile clientFile, VcodeActivityRebateRuleTemplet ruleTemplet, String activityType) throws Exception {
        // Excel方式导入
        List<VcodeActivityMoneyImport> excelList = new ArrayList<>();
        if (Constant.PrizeCogType.type_0.equals(rebateRuleCog.getPrizeCogType())) {
            if (!clientFile.isEmpty()) {
                // 导入文件
                excelList = VcodeActivityMoneyExcel.importExcel(clientFile);
                int row = 2;
                String rowTips = "";
                Double minVal, maxVal;
                String totalRangeValKey = "";
                Map<String, Double> totalRangeValMap = new HashMap<>();
                for (VcodeActivityMoneyImport item : excelList) {
                    row++;
                    rowTips = "导入失败：第" + row + "行, ";
                    if (StringUtils.isBlank(item.getPrizeType())) throw new BusinessException(rowTips + "缺少奖项类型配置");
                    if (StringUtils.isBlank(item.getScanType())) throw new BusinessException(rowTips + "缺少扫码类型配置");
                    if (StringUtils.isBlank(item.getRandomType())) throw new BusinessException(rowTips + "缺少随机类型配置");
                    if (item.getAmounts() == null || item.getAmounts() < 0)
                        throw new BusinessException(rowTips + "缺少奖项总个数配置");
                    if (item.getRangeVal() == null || item.getRangeVal() < 0)
                        throw new BusinessException(rowTips + "缺少概率值配置");
                    if (Constant.PrizeType.status_0.equals(item.getPrizeType())) {
                        item.setVpoints(null);
                        if (StringUtils.isBlank(item.getMoney())) throw new BusinessException(rowTips + "缺少金额配置");

                    } else if (Constant.PrizeType.status_1.equals(item.getPrizeType())) {
                        item.setMoney(null);
                        if (StringUtils.isBlank(item.getVpoints())) throw new BusinessException(rowTips + "缺少积分配置");

                    } else if (Constant.PrizeType.status_2.equals(item.getPrizeType())) {
                        if (StringUtils.isBlank(item.getMoney())) throw new BusinessException(rowTips + "缺少金额配置");
                        if (StringUtils.isBlank(item.getVpoints())) throw new BusinessException(rowTips + "缺少积分配置");
                    }

                    // 计算总概率值
                    totalRangeValKey = item.getScanType() + "_" + StringUtils.defaultIfBlank(item.getScanNum(), "");
                    if (totalRangeValMap.containsKey(totalRangeValKey)) {
                        totalRangeValMap.put(totalRangeValKey, totalRangeValMap.get(totalRangeValKey) + Double.valueOf(item.getRangeVal()));
                    } else {
                        totalRangeValMap.put(totalRangeValKey, Double.valueOf(item.getRangeVal()));
                    }

                    // 随机类型
                    if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                        // 积分
                        if (StringUtils.isBlank(item.getVpoints())) item.setVpoints("0-0");
                        if (!item.getVpoints().contains("-")) {
                            throw new BusinessException(rowTips + "积分配置格式错误");
                        }
                        minVal = Double.valueOf(item.getVpoints().split("-")[0]);
                        maxVal = Double.valueOf(item.getVpoints().split("-")[1]);
                        if (minVal > maxVal) {
                            throw new BusinessException(rowTips + "积分配置错误，前值应小于等于后值");
                        }
                        item.setMinVpoints(minVal.intValue());
                        item.setMaxVpoints(maxVal.intValue());

                        // 金额
                        if (StringUtils.isBlank(item.getMoney())) item.setMoney("0.00-0.00");
                        if (!item.getMoney().contains("-")) {
                            throw new BusinessException(rowTips + "金额配置格式错误");
                        }
                        minVal = Double.valueOf(item.getMoney().split("-")[0]);
                        maxVal = Double.valueOf(item.getMoney().split("-")[1]);

                        if (minVal > maxVal) {
                            throw new BusinessException(rowTips + "金额配置错误，前值应小于等于后值");
                        }
                        if (maxVal <= 0 && (Constant.activityType.activity_type8.equals(activityType) || Constant.activityType.activity_type9.equals(activityType))){
                            item.setPrizeType(Constant.PrizeType.status_8);
                        }
                        item.setMinMoney(minVal);
                        item.setMaxMoney(maxVal);
                    } else if (Constant.PrizeRandomType.type_1.equals(item.getRandomType())) {
                        // 积分
                        if (StringUtils.isBlank(item.getVpoints())) item.setVpoints("0");
                        if (item.getVpoints().contains("-")) {
                            throw new BusinessException(rowTips + "积分配置格式错误");
                        }
                        item.setMinVpoints(Integer.valueOf(item.getVpoints()));
                        item.setMaxVpoints(Integer.valueOf(item.getVpoints()));

                        // 金额
                        if (StringUtils.isBlank(item.getMoney())) item.setMoney("0.00");
                        if (item.getMoney().contains("-")) {
                            throw new BusinessException(rowTips + "金额配置格式错误");
                        }
                        item.setMinMoney(Double.valueOf(item.getMoney()));
                        item.setMaxMoney(Double.valueOf(item.getMoney()));
                        if (Double.parseDouble(item.getMoney()) <= 0 &&
                                (   Constant.activityType.activity_type8.equals(activityType)|| Constant.activityType.activity_type9.equals(activityType) ) && item.getPrizeType().length()==1){
                            item.setPrizeType(Constant.PrizeType.status_8);
                        }
                    }
                }

                // 计算各项的占比
                long ruleTotalPrize = 0;
                long firstScanTotalPrize = 0;
                double ruleTotalVpoints = 0.00D;
                double ruleTotalMoney = 0.00D;
                for (VcodeActivityMoneyImport item : excelList) {
                    totalRangeValKey = item.getScanType() + "_" + StringUtils.defaultIfBlank(item.getScanNum(), "");
                    if (totalRangeValMap.containsKey(totalRangeValKey)) {
                        item.setPrizePercent(MathUtil.round(item.getRangeVal() / totalRangeValMap.get(totalRangeValKey) * 100, 4));
                    }
                    ruleTotalPrize += item.getAmounts();
                    if (Constant.ScanType.type_0.equals(item.getScanType())) firstScanTotalPrize += item.getAmounts();
                    if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                        if (item.getVpoints() != null) {
                            ruleTotalVpoints = ruleTotalVpoints + item.getAmounts() * (item.getMinVpoints() + item.getMaxVpoints()) / 2D;
                        }
                        if (item.getMoney() != null) {
                            ruleTotalMoney = ruleTotalMoney + item.getAmounts() * (item.getMinMoney() + item.getMaxMoney()) / 2D;
                        }

                    } else if (Constant.PrizeRandomType.type_1.equals(item.getRandomType())) {
                        if (item.getVpoints() != null) {
                            ruleTotalVpoints = ruleTotalVpoints + item.getAmounts() * item.getMinVpoints();
                        }
                        if (item.getMoney() != null) {
                            ruleTotalMoney = ruleTotalMoney + item.getAmounts() * item.getMinMoney();
                        }
                    }
                }
                rebateRuleCog.setFirstScanPercent(String.format("%.2f", firstScanTotalPrize * 1D / ruleTotalPrize * 100));
                rebateRuleCog.setRuleTotalPrize(String.valueOf(ruleTotalPrize));
                rebateRuleCog.setRuleTotalVpoints(String.valueOf(ruleTotalVpoints));
                rebateRuleCog.setRuleTotalMoney(String.valueOf(ruleTotalMoney));
                rebateRuleCog.setRuleUnitMoney(String.format("%.2f", ruleTotalMoney / ruleTotalPrize));
            }

            // 规则模板方式配置
        } else {
            // 未配置奖项配置项
            if (Long.valueOf(StringUtils.defaultIfBlank(rebateRuleCog.getRuleTotalPrize(), "0")) <= 0) {
                rebateRuleCog.setFirstScanPercent(null);
                rebateRuleCog.setRuleTotalPrize(null);
                rebateRuleCog.setRuleTotalVpoints(null);
                rebateRuleCog.setRuleTotalMoney(null);
                rebateRuleCog.setRuleUnitMoney(null);

            } else {
                // 规则奖品总个数及成本
                rebateRuleCog.setRuleTotalPrize(rebateRuleCog.getRuleTotalPrize().replace("≈", "").replace(",", ""));
                rebateRuleCog.setRuleTotalVpoints(rebateRuleCog.getRuleTotalVpoints().replace("≈", "").replace(",", ""));
                rebateRuleCog.setRuleTotalMoney(rebateRuleCog.getRuleTotalMoney().replace("≈", "").replace(",", ""));
                rebateRuleCog.setRuleUnitMoney(rebateRuleCog.getRuleUnitMoney().replace("≈", "").replace(",", ""));

                // 概率的基数
                Map<String, Long> rangeValMap = initBaseRangeVal(ruleTemplet);

                VcodeActivityMoneyImport item = null;
                double itemPrizePercent = 0D;
                double firstScanPercent = Double.valueOf(rebateRuleCog.getFirstScanPercent()) / 100D; // 首扫占比
                if (ruleTemplet.getScanType() != null) {
                    int itemNum = ruleTemplet.getScanType().length;
                    for (int i = 0; i < itemNum; i++) {

                        // 是否需要持久化首扫
                        if (firstScanPercent <= 0 && Constant.ScanType
                                .type_0.equals(ruleTemplet.getScanType()[i])) continue;

                        item = new VcodeActivityMoneyImport();
                        item.setScanType(ruleTemplet.getScanType()[i]);
                        item.setRandomType(ruleTemplet.getRandomType()[i]);
                        item.setPrizePayMoney(Double.valueOf(StringUtils.defaultIfBlank(ruleTemplet.getPrizePayMoney()[i], "0")));
                        item.setPrizeDiscount(Double.valueOf(StringUtils.defaultIfBlank(ruleTemplet.getPrizeDiscount()[i], "0")));
                        item.setAllowanceType(ruleTemplet.getAllowanceType()[i]);
                        item.setAllowanceMoney(Double.valueOf(StringUtils.defaultIfBlank(ruleTemplet.getAllowanceMoney()[i], "0")));
                        item.setScanNum(ruleTemplet.getScanNum()[i]);
                        item.setCardNo(ruleTemplet.getCardNo()[i]);

                        // 奖项获取方式-随机
                        if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                            item.setMinMoney(Double.valueOf(ruleTemplet.getMinMoney()[i]));
                            item.setMaxMoney(Double.valueOf(ruleTemplet.getMaxMoney()[i]));
                            item.setMinVpoints(Integer.valueOf(ruleTemplet.getMinVpoints()[i]));
                            item.setMaxVpoints(Integer.valueOf(ruleTemplet.getMaxVpoints()[i]));
                        } else {
                            item.setMinMoney(Double.valueOf(ruleTemplet.getFixationMoney()[i]));
                            item.setMaxMoney(Double.valueOf(ruleTemplet.getFixationMoney()[i]));
                            item.setMinVpoints(Integer.valueOf(ruleTemplet.getFixationVpoints()[i]));
                            item.setMaxVpoints(Integer.valueOf(ruleTemplet.getFixationVpoints()[i]));
                        }
                        // 奖项类型
                        if (StringUtils.isNotBlank(ruleTemplet.getBigPrizeType()[i])) {
                            item.setPrizeType(ruleTemplet.getBigPrizeType()[i]);
                        } else if (Double.valueOf(item.getMaxMoney()) > 0
                                && Integer.valueOf(item.getMaxVpoints()) > 0) {
                            item.setPrizeType(Constant.PrizeType.status_2);
                        } else if (Integer.valueOf(item.getMaxVpoints()) > 0) {
                            item.setPrizeType(Constant.PrizeType.status_1);
                        } else if (Double.valueOf(item.getMaxMoney()) <= 0 && Integer.valueOf(item.getMaxVpoints()) <= 0
                                && (Constant.activityType.activity_type8.equals(activityType)|| Constant.activityType.activity_type9.equals(activityType) || Constant.activityType.activity_type13.equals(activityType))) {
                            item.setPrizeType(Constant.PrizeType.status_8);
                        } else {
                            item.setPrizeType(Constant.PrizeType.status_0);
                            if(Constant.activityType.activity_type13.equals(activityType)){
                                item.setIsWaitActivation(ruleTemplet.getIsWaitActivation()[i-1]);
                            }
                        }
                        // 奖项占比
                        item.setPrizePercent(Double.valueOf(ruleTemplet.getPrizePercent()[i]));

                        // 奖项占比
                        item.setPrizePercentWarn(null);
                        if (StringUtils.isBlank(item.getScanNum()) && StringUtils.isNotBlank(ruleTemplet.getPrizePercentWarn()[i])) {
                            item.setPrizePercentWarn(Double.valueOf(ruleTemplet.getPrizePercentWarn()[i]));
                        }

                        // 奖项总个数
                        item.setAmounts(Long.valueOf(ruleTemplet.getCogAmounts()[i]));

                        // 概率值
                        itemPrizePercent = Double.valueOf(item.getPrizePercent());
                        if (StringUtils.isBlank(ruleTemplet.getBigPrizeType()[i])
                                || StringUtils.isNotBlank(ruleTemplet.getScanNum()[i])) {
                            item.setRangeVal((long) MathUtil.round(rangeValMap.get("commonRangeVal") * itemPrizePercent, 0));
                        } else {
                            if (Constant.ScanType.type_0.equals(item.getScanType())) {
                                item.setRangeVal((long) MathUtil.round(rangeValMap.get("firstBigPrizeRangeVal") * itemPrizePercent, 0));
                            } else {
                                item.setRangeVal((long) MathUtil.round(rangeValMap.get("commonBigPrizeRangeVal") * itemPrizePercent, 0));
                            }
                        }

                        //待激活红包配置
                        item.setIsScanqrcodeWaitActivation("0");

                        if(null != ruleTemplet.getWaitActivationPrizeKey() 
                        		&& ruleTemplet.getWaitActivationPrizeKey().length > 0
                        		&& StringUtils.isNotBlank(ruleTemplet.getWaitActivationPrizeKey()[i])){
                            item.setIsScanqrcodeWaitActivation("1");
                            item.setWaitActivationPrizeKey(ruleTemplet.getWaitActivationPrizeKey()[i]);
                            item.setWaitActivationMinMoney(Double.valueOf(ruleTemplet.getMinWaitActivationMoney()[i]));
                            item.setWaitActivationMaxMoney(Double.valueOf(ruleTemplet.getMaxWaitActivationMoney()[i]));
                        }

                        excelList.add(item);
                    }
                }
            }
        }

        // 新建时奖项配置项不可为空
        if (StringUtils.isBlank(rebateRuleCog.getRebateRuleKey())
                && CollectionUtils.isEmpty(excelList)) {
            throw new BusinessException("奖项配置项不能为空");
        }

        // 校验奖项类型合法性
        if (CollectionUtils.isNotEmpty(excelList)) {
            // 是否包含普扫奖项
            boolean commonPrizeFlag = false;
            for (VcodeActivityMoneyImport item : excelList) {
                if (Constant.ScanType.type_1.equals(item.getScanType()) && item.getAmounts() > 0) {
                    commonPrizeFlag = true;
                    break;
                }
            }
            if (!commonPrizeFlag) throw new BusinessException("缺少普扫奖项配置项");

            boolean prizeType8Flag = false;
            for (VcodeActivityMoneyImport item : excelList) {
                if (Constant.PrizeType.status_8.equals(item.getPrizeType()) || Constant.activityType.activity_type9.equals(item.getPrizeType())) {
                    prizeType8Flag = true;
                    break;
                }
            }
            if (!prizeType8Flag &&
                    (Constant.activityType.activity_type8.equals(activityType) || Constant.activityType.activity_type9.equals(activityType))) {
                throw new BusinessException("缺少谢谢参与配置项");
            }

            // 组合签到及捆绑促销暂时只支持现金红包
            if (Constant.activityType.activity_type4.equals(activityType)
                    || Constant.activityType.activity_type5.equals(activityType)) {
                for (VcodeActivityMoneyImport item : excelList) {
                    if (!Constant.PrizeType.status_0.equals(item.getPrizeType())) {
                        throw new BusinessException("此活动暂只支持现金红包奖项类型");
                    }
                }
            }
        }
        return excelList;
    }

    /**
     * 计算各类奖项配置项概述基数
     */
    private Map<String, Long> initBaseRangeVal(VcodeActivityRebateRuleTemplet ruleTemplet) {

        long commonRangeVal = 10000;
        Map<String, Long> map = new HashMap<>();
        map.put("commonRangeVal", commonRangeVal); // 普扫百分之一的基数

        // 概率的基数
        double firstBigPrizeTotalPercent = 0D;
        double commonBigPrizeTotalPercent = 0D;
        if (ruleTemplet.getScanType() != null) {
            int itemNum = ruleTemplet.getScanType().length;
            for (int i = 0; i < itemNum; i++) {
                if (StringUtils.isNotBlank(ruleTemplet.getBigPrizeType()[i])
                        && StringUtils.isBlank(ruleTemplet.getScanNum()[i])) {
                    if (Constant.ScanType.type_0.equals(ruleTemplet.getScanType()[i])) {
                        firstBigPrizeTotalPercent += Double.valueOf(ruleTemplet.getPrizePercent()[i]);
                    } else {
                        commonBigPrizeTotalPercent += Double.valueOf(ruleTemplet.getPrizePercent()[i]);
                    }
                }
            }
        }
        long firstBigPrizeRangeVal = commonRangeVal;
        commonBigPrizeTotalPercent = new BigDecimal(commonBigPrizeTotalPercent).setScale(6,BigDecimal.ROUND_UP).doubleValue();
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
     * 入库积分配置
     *
     * @param excelList
     * @param currentUserKey
     * @throws Exception
     */
    public void writeBatchVpointsCog(List<VcodeActivityMoneyImport> excelList,
    		VcodeActivityRebateRuleCog rebateRuleCog, String filterAreaCode, String filterDepartmentIds, String filterOrganizationIds, 
    		String filterDateAry, String filterTimeAry, String currentUserKey, String isClear, String activityType) throws Exception {

        String totalRuleKey = "";

        // 有效缓存KEY
        Set<String> validKeyList = new HashSet<String>();

        // 是否清空  0否 1是
        isClear = StringUtils.isBlank(isClear) ? "0" : isClear;

        if (StringUtils.isBlank(rebateRuleCog.getAppointRebateRuleKey())) {
            rebateRuleCog.setAppointRebateRuleKey(null);
        }

        // 新建设置规则限制类型，默认0
        if (StringUtils.isBlank(rebateRuleCog.getRebateRuleKey())
                && StringUtils.isBlank(rebateRuleCog.getRestrictTimeType())) {
            rebateRuleCog.setRestrictTimeType("0");
        }

        // 限制时间类型：0 规则时间，1每天
        String dateStr = "Total";
        String restrictTimeType = rebateRuleCog.getRestrictTimeType();
        if ("1".equals(restrictTimeType)) {
            dateStr = DateUtil.getDate();
        }

        // 如果rebateRuleKey为空，首先创建活动规则记录
        if (StringUtils.isBlank(rebateRuleCog.getRebateRuleKey())) {
            if (StringUtils.isNotBlank(filterAreaCode)) {
                // 区域规则
                for (String areaCode : filterAreaCode.split(",")) {
                    insertRebateInfo(rebateRuleCog, null, areaCode, filterDateAry, filterTimeAry, excelList, activityType, currentUserKey);
                }
            } else {
                // 组织机构规则
            	if("mengniu".equals(DbContextHolder.getDBType())) {
            		for (String departmentIds : filterDepartmentIds.split(";")) {
                        insertRebateInfo(rebateRuleCog, departmentIds, null, filterDateAry, filterTimeAry, excelList, activityType, currentUserKey);
                    }
            	}else {
            		for (String organizationIds : filterOrganizationIds.split(";")) {
                        insertRebateInfo(rebateRuleCog, organizationIds, null, filterDateAry, filterTimeAry, excelList, activityType, currentUserKey);
                    }
            	}
            }

        } else {

            // 有效标志 0有效  1无效
            String isValid = "1";

            if ("1".equals(isClear)) {
                isValid = "0";

                // 限制类型为每天时，如果规则类型为节假日/时间段且当前时间大于等于开始日期小于结束日期
            } else if ("1".equals(rebateRuleCog.getRestrictTimeType())
                    && (Constant.rebateRuleType.RULE_TYPE_1.equals(rebateRuleCog.getRuleType()) || Constant.rebateRuleType.RULE_TYPE_2.equals(rebateRuleCog.getRuleType()))
                    && (rebateRuleCog.getBeginDate() == null || DateUtil.getDate().compareTo(rebateRuleCog.getBeginDate()) >= 0)
                    && (rebateRuleCog.getEndDate() != null && DateUtil.getDate().compareTo(rebateRuleCog.getEndDate()) < 0)) {
                isValid = "0";
            } else {

                // 获取crm精准营销开关
                List<VcodeActivityCrmGroup> groupResult = null;
                String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                        DatadicKey.filterSwitchSetting.SWITCH_GROUP);
                if (DatadicUtil.isSwitchON(groupSwitch) && !CheckUtil.isEmpty(rebateRuleCog.getGroupId())) {
                    groupResult = crmServiceService.queryVcodeActivityCrmGroup();
                    // 校验群组
                    boolean groupFlage = false;
                    if (!CollectionUtils.isEmpty(groupResult)) {
                        for (VcodeActivityCrmGroup vcodeActivityCrmGroup : groupResult) {
                            if (rebateRuleCog.getGroupId().equals(String.valueOf(vcodeActivityCrmGroup.getId()))) {
                                groupFlage = true;
                                break;
                            }
                        }
                    }
                    if (!groupFlage) {
                        throw new BusinessException("群组不存在,请重新选择群组!");
                    }
                }

                // 含有限制的规则主键
                String self = "self";
                if (rebateRuleCog.getRestrictVpoints() > 0
                        || rebateRuleCog.getRestrictMoney() > 0
                        || rebateRuleCog.getRestrictBottle() > 0
                        || rebateRuleCog.getRestrictCount() > 0) {
                    self = "";
                    if (StringUtils.isBlank(rebateRuleCog.getAppointRebateRuleKey())) {
                        totalRuleKey = rebateRuleCog.getRebateRuleKey();
                    } else {
                        totalRuleKey = rebateRuleCog.getAppointRebateRuleKey();
                    }
                } else {
                    totalRuleKey = rebateRuleCog.getRebateRuleKey();
                }

                // 消费积分
                String rebateRuleVpointsStr = CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + totalRuleKey + self;
                int rebateRuleCahceVpoints = Integer.parseInt(StringUtils.isBlank(
                        RedisApiUtil.getInstance().getHSet(rebateRuleVpointsStr, dateStr)) ? "0" : RedisApiUtil.getInstance().getHSet(rebateRuleVpointsStr, dateStr));
                // 消费金额
                String rebateRuleMoneyStr = CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + totalRuleKey + self;
                double rebateRuleCahceMoney = Double.parseDouble(StringUtils.isBlank(
                        RedisApiUtil.getInstance().getHSet(rebateRuleMoneyStr, dateStr)) ? "0.00" : RedisApiUtil.getInstance().getHSet(rebateRuleMoneyStr, dateStr));
                // 消费瓶数
                String rebateRuleBottleStr = CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + totalRuleKey + self;
                int rebateRuleCacheBottle = Integer.parseInt(StringUtils.isBlank(
                        RedisApiUtil.getInstance().getHSet(rebateRuleBottleStr, dateStr)) ? "0" : RedisApiUtil.getInstance().getHSet(rebateRuleBottleStr, dateStr));

                // 检查是够已达到限制
                int restrictVpoints = rebateRuleCog.getRestrictVpoints();
                double restrictMoney = rebateRuleCog.getRestrictMoney();
                int restrictBottle = rebateRuleCog.getRestrictBottle();

                // 限制积分、金额 及个数， 未配置或未达到则视为有效
                if ((restrictVpoints == 0 || restrictVpoints > rebateRuleCahceVpoints)
                        && (restrictMoney == 0.00 || restrictMoney > rebateRuleCahceMoney)
                        && (restrictBottle == 0 || restrictBottle > rebateRuleCacheBottle)) {
                    isValid = "0";
                }
            }
            //首扫活动key
            String rebateFirstScanRestrictStr = CacheUtilNew.cacheKey.vodeActivityKey.KEY_REBATE_RULE_RESTRICT_FIRT + rebateRuleCog.getRebateRuleKey();
            // 判断是否修改了首扫规则
            /*if (oldRebateRuleCog != null) {
                if (oldRebateRuleCog.getRestrictFirstDayBottle() != rebateRuleCog.getRestrictFirstDayBottle()) {
                    RedisApiUtil.getInstance().delHSet(rebateFirstScanRestrictStr, "firstDayBottle");
                }
                if (!String.valueOf(oldRebateRuleCog.getRestrictFirstDayMoney())
                        .equals(String.valueOf(rebateRuleCog.getRestrictFirstDayMoney()))) {
                    RedisApiUtil.getInstance().delHSet(rebateFirstScanRestrictStr, "firstDayMoney");
                }
                if (oldRebateRuleCog.getRestrictFirstMonthBottle() != rebateRuleCog.getRestrictFirstMonthBottle()) {
                    RedisApiUtil.getInstance().delHSet(rebateFirstScanRestrictStr, "firstMonthBottle");
                }
                if (!String.valueOf(oldRebateRuleCog.getRestrictFirstMonthMoney())
                        .equals(String.valueOf(rebateRuleCog.getRestrictFirstMonthMoney()))) {
                    RedisApiUtil.getInstance().delHSet(rebateFirstScanRestrictStr, "firstMonthMoney");
                }
                if (oldRebateRuleCog.getRestrictFirstTotalBottle() != rebateRuleCog.getRestrictFirstTotalBottle()) {
                    RedisApiUtil.getInstance().delHSet(rebateFirstScanRestrictStr, "firstTotalBottle");
                }
                if (!String.valueOf(oldRebateRuleCog.getRestrictFirstTotalMoney())
                        .equals(String.valueOf(rebateRuleCog.getRestrictFirstTotalMoney()))) {
                    RedisApiUtil.getInstance().delHSet(rebateFirstScanRestrictStr, "firstTotalMoney");
                }
            }*/

            rebateRuleCog.setIsValid(isValid);
            rebateRuleCog.setUpdateUser(currentUserKey);
            rebateRuleCog.setUpdateTime(DateUtil.getDateTime());
            updateRebateRuleCog(rebateRuleCog);

            if ("0".equals(isValid)) {
                validKeyList.add(rebateRuleCog.getRebateRuleKey());
            }

            String appointRebateRuleKey = rebateRuleCog.getRebateRuleKey();
            if (rebateRuleCog.getRestrictVpoints() == 0
                    && rebateRuleCog.getRestrictMoney() == 0.00
                    && rebateRuleCog.getRestrictBottle() == 0
                    && rebateRuleCog.getRestrictCount() == 0) {
                appointRebateRuleKey = null;
            }

            // 是否包含子规则，如果存在则修改后设置有效性
            List<VcodeActivityRebateRuleCog> rebateRuleList =
                    this.queryRebateRuleAreaListByParentId(rebateRuleCog.getRebateRuleKey());
            if (null != rebateRuleList && !rebateRuleList.isEmpty()) {
                for (VcodeActivityRebateRuleCog rebateRule : rebateRuleList) {
                    rebateRule.setRestrictTimeType(rebateRuleCog.getRestrictTimeType());
                    rebateRule.setRestrictBottle(rebateRuleCog.getRestrictBottle());
                    rebateRule.setRestrictVpoints(rebateRuleCog.getRestrictVpoints());
                    rebateRule.setRestrictMoney(rebateRuleCog.getRestrictMoney());
                    rebateRule.setRestrictCount(rebateRuleCog.getRestrictCount());
                    rebateRule.setAppointRebateRuleKey(appointRebateRuleKey);
                    rebateRule.setIsValid(isValid);
                    updateRebateRuleCog(rebateRule);

                    if ("0".equals(isValid)) {
                        validKeyList.add(rebateRule.getRebateRuleKey());
                    }
                }
            }

            String rebateRuleKey = rebateRuleCog.getRebateRuleKey();

            // 添加规则奖项
            addActivityVpointsItem(excelList, rebateRuleKey, rebateRuleCog.getVcodeActivityKey(), activityType, currentUserKey, true);

            // 删除当前规则bean
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_ACTIVITY_REBATE_RULE_BEAN + Constant.DBTSPLIT + rebateRuleKey);

            // 爆点时间规则过期标志
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey.LIMIT_TIME_MARK + Constant.DBTSPLIT + rebateRuleKey);

            // 删除规则对应的配置项
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST
                    + Constant.DBTSPLIT + rebateRuleCog.getVcodeActivityKey() + Constant.DBTSPLIT + rebateRuleKey);

            // 删除单瓶预算缓存
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey.KEY_REBATE_RULE_BUDGET_BOTTLE + rebateRuleKey);
            RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey.KEY_REBATE_RULE_BUDGET_MONEY + rebateRuleKey);

            // 页面中勾选了清理缓存
            if ("1".equals(isClear)) {
                // 删除当前规则消费的总积分
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + rebateRuleKey);
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + rebateRuleKey + "self");

                // 删除当前规则消费的总金额
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + rebateRuleKey);
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + rebateRuleKey + "self");

                // 删除当前规则消费的总瓶数
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + rebateRuleKey);
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + rebateRuleKey + "self");

                // 删除当前规则下用户扫码次数
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .VCODE_USER_SCAN_QRCODE_COUNT + Constant.DBTSPLIT + totalRuleKey);
            }

            // 重新导入奖项配置项时删除之前的预警
            if (CollectionUtils.isNotEmpty(excelList)) {

                // 删除当前规则消费的总积分
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + rebateRuleKey + "selfRemind");

                // 删除当前规则消费的总金额
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + rebateRuleKey + "selfRemind");

                // 删除当前规则消费的总瓶数
                RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + rebateRuleKey + "selfRemind");
            }
        }

        // 刷新缓存
        try {
            // 删除活动规则
            CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_ACTIVITY_REBATE_RULE_COG + Constant.DBTSPLIT + rebateRuleCog.getVcodeActivityKey());

            // 删除无效规则主键缓存
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_INVALID_ACTIVITY_REBATE_RULE_COG + Constant.DBTSPLIT + rebateRuleCog.getVcodeActivityKey());
            // 删除每天无效缓存
            if (validKeyList.size() > 0) {
                String invalidEveryDayKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_INVALID_EVERYDAY_ACTIVITY_REBATE_RULE_COG;
                RedisApiUtil.getInstance().removeSetValue(invalidEveryDayKey, validKeyList.toArray(new String[]{}));
            }

            // 删除活动下积分配置项对应的图片路径
            CacheUtilNew.removeGroupByKey(CacheKey.cacheKey.vcode
                    .KEY_VCODE_ACTIVITY_VPOINTS_COG_FORIMG + Constant.DBTSPLIT + rebateRuleCog.getVcodeActivityKey());
        } catch (Exception ex) {
            log.error("导入活动规则配置项，刷新缓存失败", ex);
            throw ex;
        }
    }

    /**
     * 添加规则
     *
     * @param rebateRuleCog
     * @param departmentIds
     * @param object
     * @param filterDateAry
     * @param filterTimeAry
     * @param excelList
     * @param activityType
     * @param currentUserKey
     */
    private void insertRebateInfo(VcodeActivityRebateRuleCog rebateRuleCog, String departmentIds, String areaCode,
                                  String filterDateAry, String filterTimeAry, List<VcodeActivityMoneyImport> excelList, String activityType, String currentUserKey) {

    	// 青啤拆解组织机构ids，格式：大区id, 二级办id
        String bigRegionName = null, secondaryName = null;
        // 蒙牛拆解组织机构ids，格式：大区id,省区id,经销商id
        String depRegionId = null, depProvinceId = null, firstDealerKey = null;
        
        if (StringUtils.isNotBlank(departmentIds) && departmentIds.split(",").length > 0) {
    		String[] groupId = departmentIds.split(",");
    		if ("mengniu".equals(DbContextHolder.getDBType())) {
    			depRegionId = groupId[0];
                depProvinceId = groupId.length > 1 ? groupId[1] : null;
                firstDealerKey = groupId.length > 2 ? groupId[2] : null;
    		}else {
    			bigRegionName = groupId[0];
    			secondaryName = groupId.length > 1 ? groupId[1] : null;
        	}
        }

        for (String date : filterDateAry.split(",")) {
            String beginDate = null;
            String endDate = null;
            if (!StringUtils.isBlank(filterDateAry)) {
                beginDate = date.split("@")[0];
                endDate = date.split("@")[1];
            }
            for (String time : filterTimeAry.split(",")) {
                rebateRuleCog.setRebateRuleKey(UUID.randomUUID().toString());
                rebateRuleCog.setDepRegionId(depRegionId);
                rebateRuleCog.setDepProvinceId(depProvinceId);
                rebateRuleCog.setFirstDealerKey(firstDealerKey);
                rebateRuleCog.setBigRegionName(bigRegionName);
                rebateRuleCog.setSecondaryName(secondaryName);
                rebateRuleCog.setAreaCode(areaCode);
                rebateRuleCog.setBeginDate(beginDate);
                rebateRuleCog.setEndDate(endDate);
                rebateRuleCog.setBeginTime(time.split("@")[0]);
                rebateRuleCog.setEndTime(time.split("@")[1]);
                rebateRuleCog.fillFields(currentUserKey);
                // 添加规则
                addRebateRuleCog(rebateRuleCog);
                // 添加规则奖项
                addActivityVpointsItem(excelList, rebateRuleCog.getRebateRuleKey(),
                        rebateRuleCog.getVcodeActivityKey(), activityType, currentUserKey, false);
            }
        }

    }

    /**
     * 校验导入奖项配置项关键参数
     */
    public Map<String, Object> checkActivityVpointsForImport(MultipartFile clientFile, String activityType) throws Exception {
        VcodeActivityRebateRuleCog rebateRuleCog = new VcodeActivityRebateRuleCog();
        rebateRuleCog.setPrizeCogType(Constant.PrizeCogType.type_0);
        List<VcodeActivityMoneyImport> excelList = initPrizeItem(rebateRuleCog, clientFile, null, activityType);
        long totalPrize = 0L;
        double itemMoney = 0D, totalMoney = 0D;
        double itemVpoints = 0L, totalVpoints = 0L;
        double minMoney = Double.MAX_VALUE, maxMoney = 0D;
        int minVpoints = Integer.MAX_VALUE, maxVpoints = 0;
        if (CollectionUtils.isNotEmpty(excelList)) {
            for (VcodeActivityMoneyImport item : excelList) {
                totalPrize += item.getAmounts();
                if (Constant.PrizeType.status_0.equals(item.getPrizeType())
                        || Constant.PrizeType.status_1.equals(item.getPrizeType())
                        || Constant.PrizeType.status_2.equals(item.getPrizeType())) {
                    if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                        itemMoney = (item.getMinMoney() + item.getMaxMoney()) / 2D;
                        itemVpoints = (item.getMinVpoints() + item.getMaxVpoints()) / 2D;
                    } else {
                        itemMoney = item.getMinMoney();
                        itemVpoints = item.getMinVpoints();
                    }
                    totalMoney = totalMoney + itemMoney * item.getAmounts();
                    totalVpoints = totalVpoints + itemVpoints * item.getAmounts();

                    // 金额
                    if (Constant.PrizeType.status_0.equals(item.getPrizeType())
                            || Constant.PrizeType.status_2.equals(item.getPrizeType())) {
                        if (minMoney > item.getMinMoney()) minMoney = item.getMinMoney();
                        if (maxMoney < item.getMaxMoney()) maxMoney = item.getMaxMoney();
                    }

                    // 积分
                    if (Constant.PrizeType.status_1.equals(item.getPrizeType())
                            || Constant.PrizeType.status_2.equals(item.getPrizeType())) {
                        if (minVpoints > item.getMinVpoints()) minVpoints = item.getMinVpoints();
                        if (maxVpoints < item.getMaxVpoints()) maxVpoints = item.getMaxVpoints();
                    }
                }
            }
        }
        if (minMoney == Double.MAX_VALUE) minMoney = 0D;
        if (minVpoints == Integer.MAX_VALUE) minVpoints = 0;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalPrize", totalPrize); // 奖项个数
        resultMap.put("totalMoney", totalMoney); // 奖项金额总成本
        resultMap.put("totalVpoints", totalVpoints); // 奖项积分总成本
        resultMap.put("xlsUnitMoney", totalMoney / totalPrize); // 单瓶金额成本
        resultMap.put("minMoney", minMoney); // 奖项最小金额
        resultMap.put("maxMoney", maxMoney); // 奖项最大金额
        resultMap.put("minVpoints", minVpoints); // 奖项最小积分
        resultMap.put("maxVpoints", maxVpoints); // 奖项最大积分
        return resultMap;
    }


    /**
     * 克隆规则
     *
     * @param rebateRuleKey     拷贝的规则KEY
     * @param ruleType          规则类型
     * @param filterActivityKey 目前活动主键
     * @param filterAreaCode    地区
     * @param filterDepartmentIds    蒙牛组织机构
     * @param filterOrganizationIds  青啤组织机构
     * @param filterDateAry     日期
     * @param filterTimeAry     时间
     * @param userKey
     * @param isCloneErupt      是否克隆爆点规则，0否，1是
     */
    public void cloneRebateRuleCog(String rebateRuleKey, String ruleType, String filterVcodeActivityKey,
    		String filterAreaCode, String filterDepartmentIds, String filterOrganizationIds, String filterDateAry, 
    		String filterTimeAry, String userKey, String isCloneErupt, String groupId, String groupName) throws Exception {

        // 被复制的规则对象
        VcodeActivityRebateRuleCog copyRebateRuleCog = rebateRuleCogDao.findById(rebateRuleKey);

        // 规则类型为每天或周几时，清空爆点及限制规则
        if (Constant.rebateRuleType.RULE_TYPE_3.equals(ruleType)
                || Constant.rebateRuleType.RULE_TYPE_4.equals(ruleType)) {
            copyRebateRuleCog.setEruptRuleInfo("");
            copyRebateRuleCog.setRestrictTimeType("0");
            copyRebateRuleCog.setRestrictBottle(0);
            copyRebateRuleCog.setRestrictCount(0);
            copyRebateRuleCog.setRestrictMoney(0.00);
            copyRebateRuleCog.setRestrictVpoints(0);
        }

        List<String> cloneRebateRuleKeyLst = new ArrayList<>();
        for (String vcodeActivityKey : filterVcodeActivityKey.split(",")) {
        	if (StringUtils.isNotBlank(filterAreaCode)) {
                // 区域规则
                for (String areaCode : filterAreaCode.split(",")) {
                    cloneRebateInfo(filterDateAry, filterTimeAry, copyRebateRuleCog, vcodeActivityKey, 
                    		ruleType, null, areaCode, isCloneErupt, userKey, groupId, groupName, cloneRebateRuleKeyLst);
                }
            } else {
                // 组织机构规则
            	if("mengniu".equals(DbContextHolder.getDBType())) {
            		for (String departmentIds : filterDepartmentIds.split(";")) {
                        cloneRebateInfo(filterDateAry, filterTimeAry, copyRebateRuleCog, vcodeActivityKey, 
                        		ruleType, departmentIds, null, isCloneErupt, userKey, groupId, groupName, cloneRebateRuleKeyLst);
                    }
            	}else {
            		for (String organizationIds : filterOrganizationIds.split(";")) {
                        cloneRebateInfo(filterDateAry, filterTimeAry, copyRebateRuleCog, vcodeActivityKey, 
                        		ruleType, organizationIds, null, isCloneErupt, userKey, groupId, groupName, cloneRebateRuleKeyLst);
                    }
            	}
            }
        	
            // 刷新缓存
            try {
                // 删除活动规则
                CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_VCODE_ACTIVITY_REBATE_RULE_COG + Constant.DBTSPLIT + vcodeActivityKey);

                // 删除无效规则主键缓存
                CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_INVALID_ACTIVITY_REBATE_RULE_COG + Constant.DBTSPLIT + vcodeActivityKey);

                // 删除活动下积分配置项对应的图片路径
                CacheUtilNew.removeGroupByKey(CacheKey.cacheKey.vcode
                        .KEY_VCODE_ACTIVITY_VPOINTS_COG_FORIMG + Constant.DBTSPLIT + vcodeActivityKey);
            } catch (Exception ex) {
                log.error("导入活动规则配置项，刷新缓存失败", ex);
                throw ex;
            }
        }

        // 操作日志
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("rebateRuleKey", rebateRuleKey);
        logMap.put("cloneRebateRuleKeyLst", cloneRebateRuleKeyLst);
        logService.saveLog("vcodeActivityRebateRule", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(logMap), "克隆规则");
    }
    
    /**
              *    克隆规则、奖项
     * @param filterDateAry
     * @param filterTimeAry
     * @param copyRebateRuleCog
     * @param vcodeActivityKey
     * @param ruleType
     * @param departmentIds
     * @param areaCode
     * @param isCloneErupt
     * @param userKey
     * @param groupId
     * @param groupName
     * @param cloneRebateRuleKeyLst
     * @throws Exception
     */
    public void cloneRebateInfo( String filterDateAry, String filterTimeAry, VcodeActivityRebateRuleCog copyRebateRuleCog, String vcodeActivityKey, String ruleType, 
    		String departmentIds, String areaCode, String isCloneErupt, String userKey, String groupId, String groupName, List<String> cloneRebateRuleKeyLst) throws Exception {
    	VcodeActivityRebateRuleCog newRebateRuleCog = null;
    	
    	// 青啤拆解组织机构ids，格式：大区id, 二级办id
        String bigRegionName = null, secondaryName = null;
        // 蒙牛拆解组织机构ids，格式：大区id,省区id,经销商id
        String depRegionId = null, depProvinceId = null, firstDealerKey = null;
        
        if (StringUtils.isNotBlank(departmentIds) && departmentIds.split(",").length > 0) {
    		String[] groupIds = departmentIds.split(",");
    		if ("mengniu".equals(DbContextHolder.getDBType())) {
    			depRegionId = groupIds[0];
                depProvinceId = groupIds.length > 1 ? groupIds[1] : null;
                firstDealerKey = groupIds.length > 2 ? groupIds[2] : null;
    		}else {
    			bigRegionName = groupIds[0];
    			secondaryName = groupIds.length > 1 ? groupIds[1] : null;
        	}
        }
        
    	for (String date : filterDateAry.split(",")) {
            String beginDate = null;
            String endDate = null;
            if (!StringUtils.isBlank(filterDateAry)) {
                beginDate = date.split("@")[0];
                endDate = date.split("@")[1];
            }
            for (String time : filterTimeAry.split(",")) {
                newRebateRuleCog = new VcodeActivityRebateRuleCog();
                BeanUtils.copyProperties(copyRebateRuleCog, newRebateRuleCog);
                newRebateRuleCog.setRebateRuleKey(UUID.randomUUID().toString());
                newRebateRuleCog.setVcodeActivityKey(vcodeActivityKey);
                newRebateRuleCog.setAppointRebateRuleKey(null);
                newRebateRuleCog.setHotAreaKey(null);
                newRebateRuleCog.setRuleType(ruleType);
                newRebateRuleCog.setDepRegionId(depRegionId);
                newRebateRuleCog.setDepProvinceId(depProvinceId);
                newRebateRuleCog.setFirstDealerKey(firstDealerKey);
                newRebateRuleCog.setBigRegionName(bigRegionName);
                newRebateRuleCog.setSecondaryName(secondaryName);
                newRebateRuleCog.setAreaCode(areaCode);
                newRebateRuleCog.setBeginDate(beginDate);
                newRebateRuleCog.setEndDate(endDate);
                newRebateRuleCog.setBeginTime(time.split("@")[0]);
                newRebateRuleCog.setEndTime(time.split("@")[1]);
                if (StringUtils.isBlank(isCloneErupt)) {
                    newRebateRuleCog.setEruptRuleInfo("");
                }
                newRebateRuleCog.fillFields(userKey);
                newRebateRuleCog.setGroupId(groupId);
                newRebateRuleCog.setGroupName(groupName);
                // 获取crm精准营销开关
                List<VcodeActivityCrmGroup> groupResult = null;
                String groupSwitch = DatadicUtil.getDataDicValue(
                        DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                        DatadicKey.filterSwitchSetting.SWITCH_GROUP);
                if (DatadicUtil.isSwitchON(groupSwitch) && !CheckUtil.isEmpty(newRebateRuleCog.getGroupId())) {
                    groupResult = crmServiceService.queryVcodeActivityCrmGroup();
                    // 校验群组
                    boolean groupFlage = false;
                    if (!CollectionUtils.isEmpty(groupResult)) {
                        for (VcodeActivityCrmGroup vcodeActivityCrmGroup : groupResult) {
                            if (newRebateRuleCog.getGroupId().equals(String.valueOf(vcodeActivityCrmGroup.getId()))) {
                                groupFlage = true;
                                break;
                            }
                        }
                    }
                    if (!groupFlage) {
                        throw new BusinessException("群组不存在,请重新选择群组!");
                    }
                }

                // 添加规则
                addRebateRuleCog(newRebateRuleCog);
                // 复制奖项规则
                vpointsCogService.copyActivityVpointsItem(vcodeActivityKey,
                        newRebateRuleCog.getRebateRuleKey(), copyRebateRuleCog.getRebateRuleKey());
                // 更新奖项导入状态
                VcodeActivityCog activity = new VcodeActivityCog();
                activity.setVcodeActivityKey(vcodeActivityKey);
                activity.setMoneyConfigFlag("1");
                vcodeActivityService.changeStatus(activity);
                cloneRebateRuleKeyLst.add(newRebateRuleCog.getRebateRuleKey());
            }
        }
    }

    /**
     * 添加规则奖项
     *
     * @param excelList
     * @param rebateRuleKey
     * @param vcodeActivityKey
     * @param activityType
     * @param currentUserKey
     */
    public void addActivityVpointsItem(List<VcodeActivityMoneyImport> excelList,
                                       String rebateRuleKey, String vcodeActivityKey, String activityType, String currentUserKey, boolean isUpdate) {
        if (null != excelList && !excelList.isEmpty()) {

            // 废弃此活动规则之前的配置
            if (!StringUtils.isBlank(rebateRuleKey) && isUpdate) {
                Map<String, Object> vpointsCogMap = new HashMap<>();
                vpointsCogMap.put("rebateRuleKey", rebateRuleKey);
                vpointsCogMap.put("updateUser", currentUserKey);
                vpointsCogMap.put("updateTime", DateUtil.getDateTime());
                vpointsCogService.removeByrebateRuleKey(vpointsCogMap);
            }

            // 组装新的配置
            List<VcodeActivityVpointsCog> resultList = new ArrayList<VcodeActivityVpointsCog>();
            VcodeActivityVpointsCog vpointsCog = null;
            int sequenceNum = 1;
            String vpointKey = "";
            Map<String, String> cardTypeMap = vpointsCogService.queryCardType();
            Map<String, String> vpointsMap = new HashMap<>();
            for (VcodeActivityMoneyImport item : excelList) {
                vpointKey = UUID.randomUUID().toString();
                vpointsMap.put(vpointKey, String.valueOf(item.getAmounts()));
                vpointsCog = new VcodeActivityVpointsCog(vpointKey, vcodeActivityKey, rebateRuleKey, item.getPrizeType(), item.getIsWaitActivation(),
                        item.getScanType(), item.getRandomType(), item.getMinVpoints(), item.getMaxVpoints(), item.getMinMoney(), item.getMaxMoney(),
                        item.getPrizePercent(), item.getPrizePercentWarn(), item.getCardNo(), item.getAmounts(), item.getAmounts(), item.getRangeVal(), item.getScanNum(),
                        item.getContent(), cardTypeMap.get(StringUtils.defaultString(item.getCardNo())), item.getPrizePayMoney(), item.getPrizeDiscount(), item.getAllowanceType(), item.getAllowanceMoney(),
                        item.getIsScanqrcodeWaitActivation(),item.getWaitActivationPrizeKey(),item.getWaitActivationMinMoney(),item.getWaitActivationMaxMoney());
                vpointsCog.fillFields(currentUserKey);
                vpointsCog.setSequenceNum(sequenceNum);
                resultList.add(vpointsCog);
                sequenceNum++;
            }

            // 入库
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("vpointsCogList", resultList);
            vpointsCogService.batchWrite(map);

            // 将新的奖项配置项剩余数量初始化到缓存
            String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                    .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + rebateRuleKey;
            RedisApiUtil.getInstance().setHSet(redisKey, vpointsMap);

            // 改写活动中的配置状态
            if (Constant.activityType.activity_type0.equals(activityType)) {
                VcodeActivityCog activity = new VcodeActivityCog();
                activity.setVcodeActivityKey(vcodeActivityKey);
                activity.setMoneyConfigFlag("1");
                vcodeActivityService.changeStatus(activity);

            } else if (Constant.activityType.activity_type4.equals(activityType)) {
                signinCogService.changeStatus(vcodeActivityKey);

            } else if (Constant.activityType.activity_type5.equals(activityType)) {
                promotionCogService.changeStatus(vcodeActivityKey);

            } else if (Constant.activityType.activity_type6.equals(activityType)) {
                doublePrizeCogService.changeStatus(vcodeActivityKey);

            } else if (Constant.activityType.activity_type7.equals(activityType)) {
                iVpsVcodeActivateRedEnvelopeRuleCogService.changeStatus(vcodeActivityKey);
            }
        }
    }

    /**
     * 获取活动下有效的市级区域
     *
     * @param vcodeActivityKey 活动主键
     * @param parentAreaCode   父区域编码
     * @return
     */
    public List<SysAreaM> queryValidArea(String vcodeActivityKey, String parentAreaCode) {

        // 获取parentAreaCode下的子级区域列表 
        List<SysAreaM> areaLst = sysAreaService.findByParentId(parentAreaCode);

        // 直辖市的处理
        if (areaLst != null && areaLst.size() == 2) {
            parentAreaCode = areaLst.get(1).getAreaCode();
            areaLst = sysAreaService.findByParentId(parentAreaCode);
        }

        // 获取活动下已导入过配置的区域
        List<String> cityCodeLst = rebateRuleCogDao.queryCityCodeByActivityKey(vcodeActivityKey);

        // 去除已导入过配置项过的区域
        for (String itemCode : cityCodeLst) {
            for (SysAreaM itemArea : areaLst) {
                if (itemCode.equals(itemArea.getAreaCode()) ||
                        ("000000".equals(itemCode) && "全部".equals(itemArea.getAreaName()))) {
                    areaLst.remove(itemArea);
                    break;
                }
            }
        }
        return areaLst;
    }

    /**
     * 获取活动对应的省
     *
     * @return
     */
    public List<SysAreaM> queryAreaByActivity() {

        // 获取项目地区配置
        List<String> areaCodeList = new ArrayList<>();
        List<SysAreaM> areaLst = new ArrayList<>();
        String areaCode = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_AREA);
        if (!StringUtils.isBlank(areaCode)) {
            String[] areaCodeAry = areaCode.split(",");
            areaCodeList = new ArrayList<>();
            for (String item : areaCodeAry) {
                areaCodeList.add(item + "0000");
            }

            // 返回配置的省级区域
            areaLst = sysAreaService.queryByAreaCode(areaCodeList);

        } else {
            // 返回所有省级区域
            areaLst = sysAreaService.findByParentId("0");
            areaLst.remove(0);
        }
        areaLst.add(0, new SysAreaM("000000", "全部", ""));
        if (!StringUtils.isBlank(areaCode)) {
            areaLst.add(new SysAreaM("000001", "省外", ""));
        }

        return areaLst;
    }

    /**
     * 查询有效记活动规则录列表
     *
     * @param vcodeActivityKey
     * @return
     */
    public Integer getValidRebateRuleCogNumByActivityKey(String vcodeActivityKey) {
        return rebateRuleCogDao.getValidRebateRuleCogNumByActivityKey(vcodeActivityKey);
    }

    /**
     * 根据规则父ID查询规则List
     *
     * @param rebateRuleKey 规则主键</br>
     * @return List<VcodeActivityRebateRuleCog> </br>
     */
    public List<VcodeActivityRebateRuleCog> queryRebateRuleAreaListByParentId(String rebateRuleKey) {

        return rebateRuleCogDao.queryRebateRuleAreaListByParentId(rebateRuleKey);
    }

    /**
     * 查询活动下指定区域的规则
     *
     * @param activityKey
     * @param areaCode
     * @param ruleType
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryByAreaCode(
            String activityKey, String areaCode, String ruleType) {
        Map<String, Object> map = new HashMap<>();
        map.put("vcodeActivityKey", activityKey);
        map.put("areaCode", areaCode);
        map.put("ruleType", ruleType);
        return rebateRuleCogDao.queryByAreaCode(map);
    }

    /**
     * 依据热区主键查询有效的相关规则
     *
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryValidByHotAreaKey(String hotAreaKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("hotAreaKey", hotAreaKey);
        map.put("nowTime", DateUtil.getDate());
        return rebateRuleCogDao.queryValidByHotAreaKey(map);
    }

    /**
     * 依据区域code获取本身及上级名称
     *
     * @param areaCode
     * @return
     */
    public String getAreaNameByCode(String areaCode) {
        if (StringUtils.isBlank(areaCode)) return "";

        String areaName = "";
        // 全部
        if ("000000".equals(areaCode)) {
            areaName = "全部";

        } else if ("000001".equals(areaCode)) {
            areaName = "省外";

        } else {
            SysAreaM areaM = sysAreaService.findById(areaCode);
            String finalName = areaM == null ? areaCode : areaM.getAreaName();

            // 最后2位不为00，则为县
            if (!"00".equals(areaCode.substring(4, 6))) {
                areaM = sysAreaService.findById(areaCode.substring(0, 4) + "00");
                areaName = areaM.getAreaName() + " - " + finalName;
                areaM = sysAreaService.findById(areaCode.substring(0, 2) + "0000");
                areaName = areaM.getAreaName() + " - " + areaName;

                // 市
            } else if (!"00".equals(areaCode.substring(2, 4))) {
                areaM = sysAreaService.findById(areaCode.substring(0, 2) + "0000");
                areaName = areaM.getAreaName() + " - " + finalName;

                // 省
            } else {
                areaName = finalName;
            }
        }
        return areaName;
    }

    /**
     * 依据组织机构id获取名称
     *
     * @param departmentIds 格式：大区ID,省区ID,经销商ID
     * @return
     */
    public String getDepartmentNames(String departmentIds) {
        if (StringUtils.isBlank(departmentIds)) return "";

        String departmentNames = "";
        String[] groupdepartmentId = departmentIds.split(",");
        MnDepartmentEntity department = null;
        if (groupdepartmentId.length > 0) {
            department = mnDepartmentService.findById(groupdepartmentId[0]);
            if (null != department) {
                departmentNames += department.getDep_name();
            }
        }
        if (groupdepartmentId.length > 1 && StringUtils.isNotBlank(groupdepartmentId[1])) {
            department = mnDepartmentService.findById(groupdepartmentId[1]);
            if (null != department) {
                departmentNames += " - " + department.getDep_name();
            }
        }
        if (groupdepartmentId.length > 2 && StringUtils.isNotBlank(groupdepartmentId[2])) {
            MnAgencyEntity agency = mnAgencyService.findById(groupdepartmentId[2]);
            if (null != agency) {
                departmentNames += " - " + agency.getAgency_name();
            }
        }
        return departmentNames;
    }
    
    /**
	 * 	组织机构（大区,二级办）
	 * @param organizationIds
	 * @return
	 */
	public String getOrganizationNames(String organizationIds) {
	    if (StringUtils.isBlank(organizationIds)) return "";
	    
        String organizationNames = "";
        String[] groupOrganizationId = organizationIds.split(",");
        if (groupOrganizationId.length > 0) {
                organizationNames += "大区：" + groupOrganizationId[0];
        }
        if (groupOrganizationId.length > 1 && StringUtils.isNotBlank(groupOrganizationId[1])) {
                organizationNames += " - 二级办：" + groupOrganizationId[1];
        }
        return organizationNames;
	}

    /**
     * 依据活动版本号返回对应的省区code
     *
     * @param activityVersion
     * @return
     */
    public String getAreaCodeForActivityVersion(String activityVersion) {
        String areaCode = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_COMPANY_INFO,
                DatadicKey.filterCompanyInfo.PROJECT_AREA);

        if ("huanan".equals(DbContextHolder.getDBType())) {
            // 广东
            if ("1".equals(activityVersion)) {
                areaCode = areaCode.split(",")[0];

                // 海南
            } else if ("2".equals(activityVersion) || "3".equals(activityVersion)) {
                areaCode = areaCode.split(",")[1];
            }
        }

        return areaCode + "0000";
    }

    /**
     * 获取当前规则名称
     *
     * @param rebateRuleCog
     * @return
     */
    public String getRebateRuleCogName(VcodeActivityRebateRuleCog rebateRuleCog) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getAreaNameByCode(rebateRuleCog.getAreaCode())).append(" ");
        if (Constant.rebateRuleType.RULE_TYPE_1.equals(rebateRuleCog.getRuleType())) {
            buffer.append("节假日 ").append(" ");
            buffer.append(rebateRuleCog.getBeginDate()).append("~").append(rebateRuleCog.getEndDate()).append(" ");
            buffer.append(rebateRuleCog.getBeginTime()).append("~").append(rebateRuleCog.getEndTime());

        } else if (Constant.rebateRuleType.RULE_TYPE_2.equals(rebateRuleCog.getRuleType())) {
            buffer.append("时间段 ").append(" ");
            buffer.append(rebateRuleCog.getBeginDate()).append("~").append(rebateRuleCog.getEndDate()).append(" ");
            buffer.append(rebateRuleCog.getBeginTime()).append("~").append(rebateRuleCog.getEndTime());

        } else if (Constant.rebateRuleType.RULE_TYPE_3.equals(rebateRuleCog.getRuleType())) {
            buffer.append("周几 ").append(" ");
            buffer.append("周").append(rebateRuleCog.getBeginDate()).append("-").append("周").append(rebateRuleCog.getEndDate()).append(" ");
            buffer.append(rebateRuleCog.getBeginTime()).append("~").append(rebateRuleCog.getEndTime());
        } else if (Constant.rebateRuleType.RULE_TYPE_4.equals(rebateRuleCog.getRuleType())) {
            buffer.append("每天  ").append(" ");
            buffer.append(rebateRuleCog.getBeginTime()).append("~").append(rebateRuleCog.getEndTime());
        } else if (Constant.rebateRuleType.RULE_TYPE_5.equals(rebateRuleCog.getRuleType())) {
            buffer.append("自然周签到红包");
        }
        return buffer.toString();
    }

    /**
     * 规则到期提醒
     *
     * @return List
     */
    public List<VcodeActivityRebateRuleCog> queryRuleExpireRemind() {
        return rebateRuleCogDao.queryRuleExpireRemind();
    }

    /**
     * 规则红包消耗量提醒
     *
     * @return List
     */

    public HashMap<String, List<VcodeActivityRebateRuleCog>> queryRedPacketRule() {
        HashMap<String, List<VcodeActivityRebateRuleCog>> result = new HashMap<String, List<VcodeActivityRebateRuleCog>>();

        ArrayList<VcodeActivityRebateRuleCog> redVoptionPacket = new ArrayList<VcodeActivityRebateRuleCog>();
        ArrayList<VcodeActivityRebateRuleCog> redMoneyPacket = new ArrayList<VcodeActivityRebateRuleCog>();
        ArrayList<VcodeActivityRebateRuleCog> redBottlePacket = new ArrayList<VcodeActivityRebateRuleCog>();
        HashMap<String, String> vpointsMap = new HashMap<String, String>();// 积分
        HashMap<String, String> moneyMap = new HashMap<String, String>();  //金额
        HashMap<String, String> bottleMap = new HashMap<String, String>(); //瓶数


        List<VcodeActivityRebateRuleCog> alist = rebateRuleCogDao.queryRedPacketRule();
        double ruleTotalVpoints = 0;
        double ruleTotalMoney = 0.00D;
        for (int i = 0; i < alist.size(); i++) {

            ruleTotalVpoints = 0;
            ruleTotalMoney = 0.00D;
            VcodeActivityRebateRuleCog ruleCog = alist.get(i);
            if (null != ruleCog.getRuleTotalVpoints() && !"0.00".equals(ruleCog.getRuleTotalVpoints())) {
                ruleTotalVpoints = Double.valueOf(ruleCog.getRuleTotalVpoints());

            }
            if (null != ruleCog.getRuleTotalVpoints() && !"0.00".equals(ruleCog.getRuleTotalMoney())) {

                ruleTotalMoney = Double.valueOf(ruleCog.getRuleTotalMoney());
            }

            if (ruleTotalVpoints > 0.00) {
                // 当前规则消费的积分及总金额
                String consumeVpoints = RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_VPOINTS + Constant.DBTSPLIT + ruleCog.getRebateRuleKey() + "selfRemind");
                long vpoints = Long.parseLong(StringUtils.isBlank(consumeVpoints) ? "0" : consumeVpoints);
                if (((1D * ruleTotalVpoints - vpoints) / ruleTotalVpoints) <= Constant.MSG_Type.threshold) {
                    if (vpointsMap.get(ruleCog.getVcodeActivityKey()) != null) {
                        int vopitinNum = Integer.valueOf(vpointsMap.get(ruleCog.getVcodeActivityKey())) + 1;
                        vpointsMap.put(ruleCog.getVcodeActivityKey(), vopitinNum + "");
                        ruleCog.setTotal(vpointsMap.get(ruleCog.getVcodeActivityKey()));

                    } else {
                        vpointsMap.put(ruleCog.getVcodeActivityKey(), "2");
                        ruleCog.setTotal(vpointsMap.get(ruleCog.getVcodeActivityKey()));
                        redVoptionPacket.add(ruleCog);
                    }
                }
            }
            if (ruleTotalMoney > 0) {
                String consumeMoney = RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.vodeActivityKey
                        .KEY_REBATE_RULE_RESTRICT_MONEY + Constant.DBTSPLIT + ruleCog.getRebateRuleKey() + "selfRemind");
                double money = Double.parseDouble(StringUtils.isBlank(consumeMoney) ? "0.00" : consumeMoney);
                if (((ruleTotalMoney - money) / ruleTotalMoney) <= Constant.MSG_Type.threshold) {
                    if (moneyMap.get(ruleCog.getVcodeActivityKey()) != null) {
                        int moneyNum = Integer.valueOf(moneyMap.get(ruleCog.getVcodeActivityKey())) + 1;
                        moneyMap.put(ruleCog.getVcodeActivityKey(), moneyNum + "");
                        ruleCog.setTotal(moneyMap.get(ruleCog.getVcodeActivityKey()));
                    } else {
                        moneyMap.put(ruleCog.getVcodeActivityKey(), "1");
                        ruleCog.setTotal(moneyMap.get(ruleCog.getVcodeActivityKey()));
                        redMoneyPacket.add(ruleCog);
                    }
                }

            }

            // 当前规则消费的总瓶数
            String consumeBottle = RedisApiUtil.getInstance().get(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_REBATE_RULE_RESTRICT_BOTTLE + Constant.DBTSPLIT + ruleCog.getRebateRuleKey() + "selfRemind");
            long bottle = Long.parseLong(StringUtils.isBlank(consumeBottle) ? "0" : consumeBottle);
            //if( null!=ruleCog.getRuleTotalPrize() &&((double)bottle/Double.parseDouble(ruleCog.getRuleTotalPrize()))<=Constant.MSG_Type.threshold){
            if (null != ruleCog.getRuleTotalPrize() && ((Long.parseLong(ruleCog.getRuleTotalPrize()) - bottle) / Double.parseDouble(ruleCog.getRuleTotalPrize())) <= Constant.MSG_Type.threshold) {
                if (null != bottleMap.get(ruleCog.getVcodeActivityKey())) {
                    int bottleNum = Integer.valueOf(bottleMap.get(ruleCog.getVcodeActivityKey())) + 1;
                    bottleMap.put(ruleCog.getVcodeActivityKey(), bottleNum + "");
                    ruleCog.setTotal(bottleMap.get(ruleCog.getVcodeActivityKey()));
                } else {
                    bottleMap.put(ruleCog.getVcodeActivityKey(), "3");
                    ruleCog.setTotal(bottleMap.get(ruleCog.getVcodeActivityKey()));
                    redBottlePacket.add(ruleCog);
                }
            }


        }

        result.put(Constant.MSG_Type.remind_2, redVoptionPacket);
        result.put(Constant.MSG_Type.remind_1, redMoneyPacket);
        result.put(Constant.MSG_Type.remind_3, redBottlePacket);
        return result;
    }

    /**
     * 获取规则模板
     *
     * @param templetKey
     * @param rebateRuleKey
     * @return
     */
    public VcodeActivityRebateRuleTemplet findRebateRuleTemplet(String templetKey, String rebateRuleKey) {
        VcodeActivityRebateRuleTemplet ruleTemplet = rebateRuleTempletService.findById(templetKey);

        List<VcodeActivityRebateRuleTempletDetail> detailLst = new ArrayList<>();
        detailLst.addAll(ruleTemplet.getFirstDetailLst());
        detailLst.addAll(ruleTemplet.getCommonDetailLst());
        for (VcodeActivityRebateRuleTempletDetail item : detailLst) {
            if (Constant.PrizeRandomType.type_1.equals(item.getRandomType())) {
            }
        }

        return ruleTemplet;
    }

    /**
     * 获取指定活动下的可继承规则
     *
     * @param vcodeActivityKey
     * @return
     */
    public List<VcodeActivityRebateRuleCog> queryAppointRebateRule(String vcodeActivityKey, String rebateRuleKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("vcodeActivityKey", vcodeActivityKey);
        map.put("rebate_rule_key", rebateRuleKey);
        List<VcodeActivityRebateRuleCog> rebateRuleCogLst = rebateRuleCogDao.queryAppointRebateRule(map);
        for (VcodeActivityRebateRuleCog item : rebateRuleCogLst) {
            item.setAreaName(getAreaNameByCode(item.getAreaCode()));
        }
        return rebateRuleCogLst;
    }


    /**
     * 三重防护机制
     *
     * @param excelList
     * @param rebateRuleCog
     * @throws Exception
     */
    public void checkTripleProtection(List<VcodeActivityMoneyImport> excelList,
                                      VcodeActivityRebateRuleCog rebateRuleCog) throws Exception {
        String message = "";
        VcodeActivityRebateRuleCog oldRebateRuleCog = findById(rebateRuleCog.getRebateRuleKey());

        VcodeActivityCog activityCog = vcodeActivityService.loadActivityByKey(rebateRuleCog.getVcodeActivityKey(),
                rebateRuleCog.getActivityType());
        // 规则风险提示
        if (rebateRuleCog.getMoneyDanping() > 0) {
            double unitMoney = 0;
            if (CollectionUtils.isNotEmpty(excelList) && Double.valueOf(rebateRuleCog.getRuleUnitMoney()) > 0) {
                unitMoney = Double.valueOf(rebateRuleCog.getRuleUnitMoney());
            } else if (oldRebateRuleCog != null && StringUtils.isNotBlank(oldRebateRuleCog.getRuleTotalMoney())) {
                unitMoney = MathUtil.div(Double.valueOf(oldRebateRuleCog.getRuleTotalMoney()), Double.valueOf(oldRebateRuleCog.getRuleTotalPrize()));
            }
            if (unitMoney > 0) {
                if (MathUtil.sub(MathUtil.mul(MathUtil.div(Math.abs(MathUtil.sub(unitMoney, rebateRuleCog.getMoneyDanping())), unitMoney), 100), 5) > 0) {
                    message += "单瓶成本与模板配置的单瓶成本差额大于5%";
                }
            }
        }

        // 判断是否小于最小可疑金额
        if (StringUtils.isNotBlank(activityCog.getDoubtRuleRangeMin())
                && Double.valueOf(activityCog.getDoubtRuleRangeMin()) > 0) {
            Double doubtRuleRangeMin = Double.valueOf(activityCog.getDoubtRuleRangeMin());
            for (VcodeActivityMoneyImport item : excelList) {
                //如果修改的时候什么都不做 那么就不做校验
                if (item.getMinMoney() == 0 && !CheckUtil.isEmpty(oldRebateRuleCog)) {
                    break;
                }
                if (item.getMinMoney() < doubtRuleRangeMin) {
                    message += "<br>配置金额中存在低于可疑金额奖项</br>";
                    break;
                }
            }
        }

        // 判断规则日期是否大于活动日期
        if (!Constant.activityType.activity_type3.equals(rebateRuleCog.getRuleType())
                && !Constant.activityType.activity_type4.equals(rebateRuleCog.getRuleType())) {
            Date rebateRuleCogEndDate = DateUtil.parse(rebateRuleCog.getEndDate() + " 00:00:00", DateUtil.DEFAULT_DATETIME_FORMAT);
            Date activityEndDate = DateUtil.parse(activityCog.getEndDate() + " 23:59:59", DateUtil.DEFAULT_DATETIME_FORMAT);
            if (DateUtil.diffSecond(activityEndDate, rebateRuleCogEndDate) < 0) {
                message += "<br>活动时间小于规则时间,请确认时间是否正确</br>";
            }
        }
        if (StringUtils.isNotBlank(message)) {
            throw new BusinessException(message + "<br>请确认是否保存？</br>");
        }
    }


    /**
     * 初始化界面回传的奖项配置项
     *
     * @param prizeCogType
     * @param clientFile
     * @param ruleTemplet
     * @return
     * @throws Exception
     */
    public List<VcodeActivityMoneyImport> initPrizeItem(VcodeActivityRebateRuleCog rebateRuleCog,
                                                        MultipartFile clientFile, VcodeActivityRebateRuleTemplet ruleTemplet) throws Exception {
        // Excel方式导入
        List<VcodeActivityMoneyImport> excelList = new ArrayList<>();
        if (Constant.PrizeCogType.type_0.equals(rebateRuleCog.getPrizeCogType())) {
            if (!clientFile.isEmpty()) {
                // 导入文件
                excelList = VcodeActivityMoneyExcel.importExcel(clientFile);
                int row = 2;
                String rowTips = "";
                Double minVal, maxVal;
                String totalRangeValKey = "";
                Map<String, Double> totalRangeValMap = new HashMap<>();
                for (VcodeActivityMoneyImport item : excelList) {
                    row++;
                    rowTips = "导入失败：第" + row + "行, ";
                    if (StringUtils.isBlank(item.getPrizeType())) throw new BusinessException(rowTips + "缺少奖项类型配置");
                    if (StringUtils.isBlank(item.getScanType())) throw new BusinessException(rowTips + "缺少扫码类型配置");
                    if (StringUtils.isBlank(item.getRandomType())) throw new BusinessException(rowTips + "缺少随机类型配置");
                    if (item.getAmounts() == null || item.getAmounts() < 0)
                        throw new BusinessException(rowTips + "缺少奖项总个数配置");
                    if (item.getRangeVal() == null || item.getRangeVal() < 0)
                        throw new BusinessException(rowTips + "缺少概率值配置");
                    if (Constant.PrizeType.status_0.equals(item.getPrizeType())) {
                        item.setVpoints(null);
                        if (StringUtils.isBlank(item.getMoney())) throw new BusinessException(rowTips + "缺少金额配置");

                    } else if (Constant.PrizeType.status_1.equals(item.getPrizeType())) {
                        item.setMoney(null);
                        if (StringUtils.isBlank(item.getVpoints())) throw new BusinessException(rowTips + "缺少积分配置");

                    } else if (Constant.PrizeType.status_2.equals(item.getPrizeType())) {
                        if (StringUtils.isBlank(item.getMoney())) throw new BusinessException(rowTips + "缺少金额配置");
                        if (StringUtils.isBlank(item.getVpoints())) throw new BusinessException(rowTips + "缺少积分配置");
                    }

                    // 计算总概率值
                    totalRangeValKey = item.getScanType() + "_" + StringUtils.defaultIfBlank(item.getScanNum(), "");
                    if (totalRangeValMap.containsKey(totalRangeValKey)) {
                        totalRangeValMap.put(totalRangeValKey, totalRangeValMap.get(totalRangeValKey) + Double.valueOf(item.getRangeVal()));
                    } else {
                        totalRangeValMap.put(totalRangeValKey, Double.valueOf(item.getRangeVal()));
                    }

                    // 随机类型
                    if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                        // 积分
                        if (StringUtils.isBlank(item.getVpoints())) item.setVpoints("0-0");
                        if (!item.getVpoints().contains("-")) {
                            throw new BusinessException(rowTips + "积分配置格式错误");
                        }
                        minVal = Double.valueOf(item.getVpoints().split("-")[0]);
                        maxVal = Double.valueOf(item.getVpoints().split("-")[1]);
                        if (minVal > maxVal) {
                            throw new BusinessException(rowTips + "积分配置错误，前值应小于等于后值");
                        }
                        item.setMinVpoints(minVal.intValue());
                        item.setMaxVpoints(maxVal.intValue());

                        // 金额
                        if (StringUtils.isBlank(item.getMoney())) item.setMoney("0.00-0.00");
                        if (!item.getMoney().contains("-")) {
                            throw new BusinessException(rowTips + "金额配置格式错误");
                        }
                        minVal = Double.valueOf(item.getMoney().split("-")[0]);
                        maxVal = Double.valueOf(item.getMoney().split("-")[1]);
                        if (minVal > maxVal) {
                            throw new BusinessException(rowTips + "金额配置错误，前值应小于等于后值");
                        }
                        item.setMinMoney(minVal);
                        item.setMaxMoney(maxVal);
                    } else if (Constant.PrizeRandomType.type_1.equals(item.getRandomType())) {
                        // 积分
                        if (StringUtils.isBlank(item.getVpoints())) item.setVpoints("0");
                        if (item.getVpoints().contains("-")) {
                            throw new BusinessException(rowTips + "积分配置格式错误");
                        }
                        item.setMinVpoints(Integer.valueOf(item.getVpoints()));
                        item.setMaxVpoints(Integer.valueOf(item.getVpoints()));

                        // 金额
                        if (StringUtils.isBlank(item.getMoney())) item.setMoney("0.00");
                        if (item.getMoney().contains("-")) {
                            throw new BusinessException(rowTips + "金额配置格式错误");
                        }
                        item.setMinMoney(Double.valueOf(item.getMoney()));
                        item.setMaxMoney(Double.valueOf(item.getMoney()));
                    }
                }
                // 计算各项的占比
                long ruleTotalPrize = 0;
                long firstScanTotalPrize = 0;
                double ruleTotalVpoints = 0.00D;
                double ruleTotalMoney = 0.00D;
                for (VcodeActivityMoneyImport item : excelList) {
                    totalRangeValKey = item.getScanType() + "_" + StringUtils.defaultIfBlank(item.getScanNum(), "");
                    if (totalRangeValMap.containsKey(totalRangeValKey)) {
                        item.setPrizePercent(MathUtil.round(item.getRangeVal() / totalRangeValMap.get(totalRangeValKey) * 100, 4));
                    }
                    ruleTotalPrize += item.getAmounts();
                    if (Constant.ScanType.type_0.equals(item.getScanType())) firstScanTotalPrize += item.getAmounts();
                    if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                        if (item.getVpoints() != null) {
                            ruleTotalVpoints = ruleTotalVpoints + item.getAmounts() * (item.getMinVpoints() + item.getMaxVpoints()) / 2D;
                        }
                        if (item.getMoney() != null) {
                            ruleTotalMoney = ruleTotalMoney + item.getAmounts() * (item.getMinMoney() + item.getMaxMoney()) / 2D;
                        }

                    } else if (Constant.PrizeRandomType.type_1.equals(item.getRandomType())) {
                        if (item.getVpoints() != null) {
                            ruleTotalVpoints = ruleTotalVpoints + item.getAmounts() * item.getMinVpoints();
                        }
                        if (item.getMoney() != null) {
                            ruleTotalMoney = ruleTotalMoney + item.getAmounts() * item.getMinMoney();
                        }
                    }
                }
                rebateRuleCog.setFirstScanPercent(String.format("%.2f", firstScanTotalPrize * 1D / ruleTotalPrize * 100));
                rebateRuleCog.setRuleTotalPrize(String.valueOf(ruleTotalPrize));
                rebateRuleCog.setRuleTotalVpoints(String.valueOf(ruleTotalVpoints));
                rebateRuleCog.setRuleTotalMoney(String.valueOf(ruleTotalMoney));
                rebateRuleCog.setRuleUnitMoney(String.format("%.2f", ruleTotalMoney / ruleTotalPrize));
            }

            // 规则模板方式配置
        } else {
            VcodeActivityMoneyImport item = null;
            rebateRuleCog.setRuleUnitMoney(rebateRuleCog.getRuleUnitMoney().replace("≈", "").replace(",", ""));
            double firstScanPercent = Double.valueOf(rebateRuleCog.getFirstScanPercent()) / 100D; // 首扫占比
            if (ruleTemplet.getScanType() != null) {
                int itemNum = ruleTemplet.getScanType().length;
                for (int i = 0; i < itemNum; i++) {
                    // 是否需要持久化首扫
                    if (firstScanPercent <= 0 && Constant.ScanType.type_0.equals(ruleTemplet.getScanType()[i]))
                        continue;

                    item = new VcodeActivityMoneyImport();
                    item.setScanType(ruleTemplet.getScanType()[i]);
                    item.setRandomType(ruleTemplet.getRandomType()[i]);

                    // 奖项获取方式-随机
                    if (Constant.PrizeRandomType.type_0.equals(item.getRandomType())) {
                        item.setMinMoney(Double.valueOf(ruleTemplet.getMinMoney()[i]));
                    } else {
                        item.setMinMoney(Double.valueOf(ruleTemplet.getFixationMoney()[i]));
                    }

                    excelList.add(item);
                }
            }
        }
        return excelList;
    }

    /**
     * 查询设置有爆点规则的返利规则
     * @param queryMap
     * @return
     */
    public List<VcodeActivityRebateRuleCog> findRebateRuleCogList(Map<String,String> queryMap){
        return rebateRuleCogDao.findEruptRuleInfo(queryMap);
    }
}
