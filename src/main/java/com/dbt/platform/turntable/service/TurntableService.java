package com.dbt.platform.turntable.service;

import static com.dbt.framework.util.CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_TURNTABLE_ACTIVITY_COG;
import static com.dbt.framework.util.CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_TURNTABLE_PRIZE_ACTIVITY_COG;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.log.service.VpsOperationLogService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.ExcelUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityMoneyImport;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleTemplet;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.turntable.bean.TurntableActivityCogInfo;
import com.dbt.platform.turntable.bean.VpsTurntablePacksRecord;
import com.dbt.platform.turntable.bean.VpsTurntablePrizeCog;
import com.dbt.platform.turntable.dao.TurntableDao;
import com.dbt.platform.turntable.dao.TurntablePacksRecordDao;
import com.dbt.platform.turntable.dao.TurntablePrizeDao;
import com.dbt.platform.turntable.dao.TurntableRuleDao;

@Service
public class TurntableService {
    @Autowired
    private TurntableDao turntableDao;
    @Autowired
    private TurntablePrizeDao turntablePrizeDao;
    @Autowired
    private TurntableRuleDao turntableRuleDao;
    @Autowired
    private TurntablePacksRecordDao turntablePacksRecordDao;
    @Autowired
    protected VpsOperationLogService logService;

    private final String formatter = "yyyy-MM-dd HH:mm:ss";


    public List<TurntableActivityCogInfo> queryForLst(TurntableActivityCogInfo queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return turntableDao.queryForList(map);
    }

    public int queryForCount(TurntableActivityCogInfo queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return turntableDao.queryForCount(map);
    }

    public String createTurntableActivity(TurntableActivityCogInfo turntableActivityCogInfo, SysUserBasis currentUser, String filterDepartmentIds) {
        // 同一时间同一类型仅能有一个活动 查询时间是否重叠
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", turntableActivityCogInfo.getStartDate());
        map.put("endDate", turntableActivityCogInfo.getEndDate());
        if (!"2".equals(turntableActivityCogInfo.getDrainageType()) && !"3".equals(turntableActivityCogInfo.getDrainageType())) {
            map.put("activityType", turntableActivityCogInfo.getActivityType());
        }
        // 蒙牛支码，校验时和地区
        if ("2".equals(turntableActivityCogInfo.getDrainageType()) || "3".equals(turntableActivityCogInfo.getDrainageType()) && !"mengniuzhi".equals(DbContextHolder.getDBType())) {
            if(!turntableActivityCogInfo.getAreaCode().contains("000000")){
                String[] split = StringUtils.split(turntableActivityCogInfo.getAreaCode(), ",");
                List<String> areaCodeList = new ArrayList<>();
                areaCodeList.addAll(Arrays.asList(split));
                areaCodeList.add("000000");
                map.put("areaCode", areaCodeList);
            }
        }
        map.put("drainageType", turntableActivityCogInfo.getDrainageType());
        map.put("activityKey", "");
        int count = turntableDao.queryOverlapActivity(map);
        if (count != 0) {
            return "添加失败!所选时间段或区域已有活动!";
        }

        StringBuilder depRegionIds = new StringBuilder();
        StringBuilder depProvinceIds = new StringBuilder();
        StringBuilder firstDealerKeys = new StringBuilder();
        if (StringUtils.isNotBlank(filterDepartmentIds)) {
            // 拼接蒙牛组织结构
            for (String departmentIds : filterDepartmentIds.split(";")) {
                // 拆解组织机构ids，格式：大区id,省区id,经销商id
                if (StringUtils.isNotBlank(departmentIds) && departmentIds.split(",").length > 0) {
                    String[] groupDepartmentId = departmentIds.split(",");
                    depRegionIds.append(groupDepartmentId[0]).append(",");
                    depProvinceIds.append(groupDepartmentId.length > 1 ? groupDepartmentId[1] : "").append(",");
                    firstDealerKeys.append(groupDepartmentId.length > 2 ? groupDepartmentId[2] : "").append(",");
                }
            }
        }
        String[] skuKeyArray = turntableActivityCogInfo.getSkuKeyArray();
        if(skuKeyArray!=null && skuKeyArray.length>0){
            List<String> skuKeyList = Arrays.asList(skuKeyArray);
            Object[] objects = new HashSet(skuKeyList).toArray();
            turntableActivityCogInfo.setSkuKeys(StringUtils.join(objects, ","));
        }
        String areaCode = turntableActivityCogInfo.getAreaCode();
        if(StringUtils.isNotEmpty(areaCode)){
            String[] split = StringUtils.split(areaCode, ",");
            Object[] objects = new HashSet(Arrays.asList(split)).toArray();
            turntableActivityCogInfo.setAreaCode(StringUtils.join(objects, ","));
        }
        turntableActivityCogInfo.setDepRegionId(depRegionIds.toString());
        turntableActivityCogInfo.setDepProvinceId(depProvinceIds.toString());
        turntableActivityCogInfo.setFirstDealerKey(firstDealerKeys.toString());
        turntableActivityCogInfo.setActivityKey(UUIDTools.getInstance().getUUID());
        turntableActivityCogInfo.setCreateTime(DateUtil.getDate(formatter));
        turntableActivityCogInfo.setUpdateTime(DateUtil.getDate(formatter));
        turntableActivityCogInfo.setCreateUser(currentUser.getUserName());
        turntableActivityCogInfo.setUpdateUser(currentUser.getUserName());
        turntableDao.createTurntableActivity(turntableActivityCogInfo);

        // 设置活动默认奖品
        for (Integer i = 0; i < turntableActivityCogInfo.getTurntableNum(); i++) {
            VpsTurntablePrizeCog vpsTurntablePrizeCog = new VpsTurntablePrizeCog();
            // 第一次添加奖品 将此活动最后一个转盘位置配置成默认的谢谢参与
            vpsTurntablePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
            vpsTurntablePrizeCog.setTurntableActivityKey(turntableActivityCogInfo.getActivityKey());
            vpsTurntablePrizeCog.setTurntablePrizeType("3");
            vpsTurntablePrizeCog.setTurntablePrizeName("谢谢参与");
            vpsTurntablePrizeCog.setTurntablePic("prizeImg/xxcy.png");
            vpsTurntablePrizeCog.setTurntablePosition((i + 1));
            vpsTurntablePrizeCog.setReceiveNumber(0L);
            vpsTurntablePrizeCog.setCreateTime(DateUtil.getDate(formatter));
            vpsTurntablePrizeCog.setUpdateTime(DateUtil.getDate(formatter));
            vpsTurntablePrizeCog.setCreateUser(currentUser.getUserName());
            vpsTurntablePrizeCog.setUpdateUser(currentUser.getUserName());
            turntablePrizeDao.createTurntablePrize(vpsTurntablePrizeCog);
        }
        return "添加成功!";
    }

    public String createTurntablePrize(String activityKey, VpsTurntablePrizeCog turntablePrizeCog, SysUserBasis currentUser) {
        TurntableActivityCogInfo turntableActivityCogInfo = this.queryTurntableActivityByKey(activityKey);
        // 查询所属活动奖品数量
        int num = this.queryTurntablePrizeNumByActivity(activityKey);
        if (num >= turntableActivityCogInfo.getTurntableNum()) {
            return "添加失败:此活动奖品已达上限!";
        }
        if (num == 0) {
            VpsTurntablePrizeCog vpsTurntablePrizeCog = new VpsTurntablePrizeCog();
            // 第一次添加奖品 将此活动最后一个转盘位置配置成默认的谢谢参与
            vpsTurntablePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
            vpsTurntablePrizeCog.setTurntableActivityKey(activityKey);
            vpsTurntablePrizeCog.setTurntablePrizeType("3");
            vpsTurntablePrizeCog.setTurntablePrizeName("谢谢参与");
            vpsTurntablePrizeCog.setTurntablePosition(turntableActivityCogInfo.getTurntableNum());
            vpsTurntablePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
            vpsTurntablePrizeCog.setCreateTime(DateUtil.getDate(formatter));
            vpsTurntablePrizeCog.setUpdateTime(DateUtil.getDate(formatter));
            vpsTurntablePrizeCog.setCreateUser(currentUser.getUserName());
            vpsTurntablePrizeCog.setUpdateUser(currentUser.getUserName());
            turntablePrizeDao.createTurntablePrize(vpsTurntablePrizeCog);
        }
        if (num == 0) {
            num = num + 1;
        }
        turntablePrizeCog.setTurntablePosition(num);
        turntablePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
        turntablePrizeCog.setTurntableActivityKey(activityKey);
        turntablePrizeCog.setCreateTime(DateUtil.getDate(formatter));
        turntablePrizeCog.setUpdateTime(DateUtil.getDate(formatter));
        turntablePrizeCog.setCreateUser(currentUser.getUserName());
        turntablePrizeCog.setUpdateUser(currentUser.getUserName());
        turntablePrizeDao.createTurntablePrize(turntablePrizeCog);
        return "添加成功";
    }

    private TurntableActivityCogInfo queryTurntableActivityByKey(String activityKey) {
        return turntableDao.queryTurntableActivityByKey(activityKey);
    }

    public int queryTurntablePrizeNumByActivity(String activityKey) {
        return turntablePrizeDao.queryTurntablePrizeNum(activityKey);
    }

    public List<VcodeActivityRebateRuleCog> queryTurntableRuleByActivityKey(String activityKey) {
        return turntableRuleDao.queryTurntableRuleByActivityKey(activityKey);
    }

    public List<VcodeActivityMoneyImport> initPrizeItem(VcodeActivityRebateRuleCog rebateRuleCog, VcodeActivityRebateRuleTemplet ruleTemplet, String activityType) {
        List<VcodeActivityMoneyImport> excelList = new ArrayList<>();
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
            if (ruleTemplet.getScanType() != null) {
                int itemNum = ruleTemplet.getScanType().length;
                for (int i = 0; i < itemNum; i++) {
                    item = new VcodeActivityMoneyImport();
                    item.setScanType(ruleTemplet.getScanType()[i]);
                    item.setRandomType(ruleTemplet.getRandomType()[i]);
                    item.setPrizePayMoney(Double.valueOf(StringUtils.defaultIfBlank(ruleTemplet.getPrizePayMoney()[i], "0")));
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
                    if (ruleTemplet.getBigPrizeType().length > 0 && StringUtils.isNotBlank(ruleTemplet.getBigPrizeType()[i])) {
                        item.setPrizeType(ruleTemplet.getBigPrizeType()[i]);
                    } else if (Double.valueOf(item.getMaxMoney()) > 0
                            && Integer.valueOf(item.getMaxVpoints()) > 0) {
                        item.setPrizeType(Constant.PrizeType.status_2);
                    } else if (Integer.valueOf(item.getMaxVpoints()) > 0) {
                        item.setPrizeType(Constant.PrizeType.status_1);
                    } else if (Double.valueOf(item.getMaxMoney()) > 0) {
                        item.setPrizeType(Constant.PrizeType.status_0);
                    } else {
                        item.setPrizeType(Constant.PrizeType.status_8);
                    }
                    // 奖项占比
                    item.setPrizePercent(Double.valueOf(ruleTemplet.getPrizePercent()[i]));

                    // 奖项占比
                    item.setPrizePercentWarn(null);


                    // 奖项总个数
                    item.setAmounts(Long.valueOf(ruleTemplet.getCogAmounts()[i]));

                    // 概率值
                    itemPrizePercent = Double.valueOf(item.getPrizePercent());
                    item.setRangeVal((long) MathUtil.round(rangeValMap.get("commonBigPrizeRangeVal") * itemPrizePercent, 0));
                    excelList.add(item);
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
                if (Constant.ScanType.type_1.equals(item.getScanType())) {
                    commonPrizeFlag = true;
                    break;
                }
            }
            if (!commonPrizeFlag) throw new BusinessException("缺少普扫奖项配置项");

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
                if (ruleTemplet.getBigPrizeType().length > 0 && StringUtils.isNotBlank(ruleTemplet.getBigPrizeType()[i])) {
                    commonBigPrizeTotalPercent += Double.valueOf(ruleTemplet.getPrizePercent()[i]);
                }
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

    public TurntableActivityCogInfo queryTurntableActivityInfoByKey(String activityKey) {
        return turntableDao.queryTurntableActivityByKey(activityKey);
    }

    public VpsTurntablePrizeCog queryTurntablePrizeByInfoKey(String infoKey) {
        return turntablePrizeDao.queryTurntablePrizeByInfoKey(infoKey);
    }

    public String doTurntablePrizeEdit(VpsTurntablePrizeCog turntablePrizeCog, SysUserBasis currentUser) {
        // 获取活动信息
        TurntableActivityCogInfo activityCogInfo = turntableDao.queryTurntableActivityByKey(turntablePrizeCog.getTurntableActivityKey());
        if (turntablePrizeCog.getTurntablePosition() == activityCogInfo.getTurntableNum()) {
            // 当前转盘是活动中最后一个奖品 此奖品只能是谢谢参与
            if (!"3".equals(turntablePrizeCog.getTurntablePrizeType())) {
                return "修改失败!此奖品只能为谢谢参与!";
            }
        }
        turntablePrizeCog.setUpdateTime(DateUtil.getDate(formatter));
        turntablePrizeCog.setUpdateUser(currentUser.getUserName());
        turntablePrizeDao.update(turntablePrizeCog);
        logService.saveLog("doTurntablePrizeEdit", Constant.OPERATION_LOG_TYPE.TYPE_2, turntablePrizeCog.toString(), "修改积分活动奖品配置");
        return "修改成功";
    }

    public String doTurntableActivityInfoEdit(TurntableActivityCogInfo turntableActivityCogInfo, SysUserBasis currentUser, String filterDepartmentIds) throws Exception {
        // 同一时间仅能有一个活动 查询时间是否重叠
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", turntableActivityCogInfo.getStartDate());
        map.put("endDate", turntableActivityCogInfo.getEndDate());
        map.put("activityKey", turntableActivityCogInfo.getActivityKey());
        //二重惊喜不判断 转盘 盲盒类型，增加区域判断
        if (!"2".equals(turntableActivityCogInfo.getDrainageType()) && !"3".equals(turntableActivityCogInfo.getDrainageType())) {
            map.put("activityType", turntableActivityCogInfo.getActivityType());
        }
        // 蒙牛支码，校验时不管地区
        if ("2".equals(turntableActivityCogInfo.getDrainageType()) || "3".equals(turntableActivityCogInfo.getDrainageType()) && !"mengniuzhi".equals(DbContextHolder.getDBType())) {
            if(!turntableActivityCogInfo.getAreaCode().contains("000000")){
                String[] split = StringUtils.split(turntableActivityCogInfo.getAreaCode(), ",");
                List<String> areaCodeList = new ArrayList<>();
                areaCodeList.addAll(Arrays.asList(split));
                areaCodeList.add("000000");
                map.put("areaCode", areaCodeList);
            }
        }
        map.put("drainageType", turntableActivityCogInfo.getDrainageType());
        int count = turntableDao.queryOverlapActivity(map);
        if (count != 0) {
            return "修改失败!所选时间段或区域已有活动!";
        }
        StringBuilder depRegionIds = new StringBuilder();
        StringBuilder depProvinceIds = new StringBuilder();
        StringBuilder firstDealerKeys = new StringBuilder();
        if (StringUtils.isNotBlank(filterDepartmentIds)) {
            // 拼接蒙牛组织结构
            for (String departmentIds : filterDepartmentIds.split(";")) {
                // 拆解组织机构ids，格式：大区id,省区id,经销商id
                if (StringUtils.isNotBlank(departmentIds) && departmentIds.split(",").length > 0) {
                    String[] groupDepartmentId = departmentIds.split(",");
                    depRegionIds.append(groupDepartmentId[0]).append(",");
                    depProvinceIds.append(groupDepartmentId.length > 1 ? groupDepartmentId[1] : "").append(",");
                    firstDealerKeys.append(groupDepartmentId.length > 2 ? groupDepartmentId[2] : "").append(",");
                }
            }
        }
        String[] skuKeyArray = turntableActivityCogInfo.getSkuKeyArray();
        if(skuKeyArray!=null && skuKeyArray.length>0){
            List<String> skuKeyList = Arrays.asList(skuKeyArray);
            Object[] objects = new HashSet(skuKeyList).toArray();
            turntableActivityCogInfo.setSkuKeys(StringUtils.join(objects, ","));
        }
        String areaCode = turntableActivityCogInfo.getAreaCode();
        if(StringUtils.isNotEmpty(areaCode)){
            String[] split = StringUtils.split(areaCode, ",");
            Object[] objects = new HashSet(Arrays.asList(split)).toArray();
            turntableActivityCogInfo.setAreaCode(StringUtils.join(objects, ","));
        }
        turntableActivityCogInfo.setDepRegionId(depRegionIds.toString());
        turntableActivityCogInfo.setDepProvinceId(depProvinceIds.toString());
        turntableActivityCogInfo.setFirstDealerKey(firstDealerKeys.toString());
        turntableActivityCogInfo.setUpdateTime(DateUtil.getDate(formatter));
        turntableActivityCogInfo.setUpdateUser(currentUser.getUserName());
        turntableDao.update(turntableActivityCogInfo);
        String activityCacheKey = KEY_VCODE_TURNTABLE_ACTIVITY_COG + Constant.DBTSPLIT + turntableActivityCogInfo.getActivityKey();
        String prizeKey = KEY_VCODE_TURNTABLE_PRIZE_ACTIVITY_COG + Constant.DBTSPLIT + turntableActivityCogInfo.getActivityKey();
        // 删除活动缓存
        CacheUtilNew.removeByKey(activityCacheKey);
        CacheUtilNew.removeByKey(prizeKey);
        return "修改成功";
    }


    public List<VpsTurntablePacksRecord> queryTurntablePacksRecordInfoByActivityKey(String activityKey, VpsTurntablePacksRecord queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        map.put("activityKey", activityKey);
        List<String> turntableSuffix = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            turntableSuffix.add(i + 1 + "");
        }
        map.put("turntableSuffix", turntableSuffix);
        return turntablePacksRecordDao.queryTurntablePacksRecordInfoByActivityKey(map);
    }

    public int queryTurntablePacksRecordInfoForCount(VpsTurntablePacksRecord queryBean, String activityKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("activityKey", activityKey);
        List<String> turntableSuffix = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            turntableSuffix.add(i + 1 + "");
        }
        map.put("turntableSuffix", turntableSuffix);
        return turntablePacksRecordDao.queryTurntablePacksRecordInfoForCount(map);
    }

    /**
     * 待发货表格下载
     */
    public void exportTurnTableRecordList(String activityKey, VpsTurntablePacksRecord queryBean, HttpServletResponse response, SysUserBasis currentUser) throws Exception {
        response.reset();
        response.setCharacterEncoding("GBK");
        response.setContentType("application/msexcel;charset=UTF-8");
        OutputStream outStream = response.getOutputStream();

        // 获取导出信息
        List<VpsTurntablePacksRecord> prizeLst = queryTurntablePacksRecordInfoByActivityKey(activityKey, queryBean, null);
        for (VpsTurntablePacksRecord vpsTurntablePacksRecord : prizeLst) {
            vpsTurntablePacksRecord.setDepRegionName(vpsTurntablePacksRecord.getDepRegionName()
                    +"-"+vpsTurntablePacksRecord.getDepProvinceName()+"-"+vpsTurntablePacksRecord.getDepMarketName()+"-"+vpsTurntablePacksRecord.getDepCountyName());
        }

        String bookName = "";
        String[] headers = null;
        String[] valueTags = null;
        bookName = "中奖记录导出";
        if ("mengniu".equals(currentUser.getProjectServerName())) {
            headers = new String[]{"用户主键", "昵称", "姓名", "手机号", "身份证号", "所属组织架构","所属经销商", "中奖信息", "中奖金额", "奖项描述","消耗积分", "抽奖省份", "市", "县", "详细地址", "中奖时间"};
            valueTags = new String[]{"userKey", "nickName", "realName", "phoneNumber", "idCard", "depRegionName","agencyName","turntablePrizeName", "earnMoney", "prizeDesc", "consumeVpoints", "province", "city", "county", "address", "earnTime"};
            
        } else {
            headers = new String[]{"用户主键", "昵称", "姓名", "手机号", "身份证号", "中奖信息", "中奖金额", "奖项描述","消耗积分", "抽奖省份", "市", "县", "详细地址", "中奖时间"};
            valueTags = new String[]{"userKey", "nickName", "realName", "phoneNumber", "idCard","turntablePrizeName", "earnMoney", "prizeDesc", "consumeVpoints", "province", "city", "county", "address", "earnTime"};
            
        }

        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(bookName, "UTF-8") + DateUtil.getDate() + ".xlsx");
        ExcelUtil<VpsTurntablePacksRecord> excel = new ExcelUtil<>();
        excel.writeSXSSFWorkExcel(bookName, headers, valueTags, prizeLst, DateUtil.DEFAULT_DATETIME_FORMAT, outStream);
        outStream.close();
        response.flushBuffer();

    }

    /**
     * 转换成扫码活动对应
     *
     * @param doublePrizeCog
     * @return
     */
    public VcodeActivityCog transformForActivityCog(TurntableActivityCogInfo turntableActivityCogInfo) {
        VcodeActivityCog activityCog = new VcodeActivityCog();
        activityCog.setVcodeActivityKey(turntableActivityCogInfo.getActivityKey());
        activityCog.setVcodeActivityName(turntableActivityCogInfo.getActivityName());
        activityCog.setStartDate(turntableActivityCogInfo.getStartDate());
        activityCog.setEndDate(turntableActivityCogInfo.getEndDate());
        activityCog.setActivityType(Constant.activityType.activity_type8);
        return activityCog;
    }

    public List<VpsTurntablePrizeCog> queryTurntablePrizeByActivityKey(String activityKey) {
        return turntablePrizeDao.queryTurntablePrizeByActivityKey(activityKey);
    }

}
