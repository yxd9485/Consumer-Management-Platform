package com.dbt.platform.fission.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.log.service.VpsOperationLogService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.MailUtil;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.dao.ISkuInfoDao;
import com.dbt.platform.fission.bean.VpsVcodeActivateRedEnvelopeRuleCogEntity;
import com.dbt.platform.fission.bean.VpsVcodeActivateSharePrizeCog;
import com.dbt.platform.fission.dao.IVpsVcodeActivateRedEnvelopeRuleCogMapper;
import com.dbt.platform.fission.service.FissionService;
import com.dbt.platform.fission.service.IVpsVcodeActivateRedEnvelopeRuleCogService;
import com.dbt.platform.fission.vo.VpsVcodeActivateRedEnvelopeRuleCogVO;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 激活红包规则（裂变红包） 服务实现类
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-11
 */
@Service
public class VpsVcodeActivateRedEnvelopeRuleCogServiceImpl extends ServiceImpl<IVpsVcodeActivateRedEnvelopeRuleCogMapper, VpsVcodeActivateRedEnvelopeRuleCogEntity> implements IVpsVcodeActivateRedEnvelopeRuleCogService {
    @Autowired
    private ISkuInfoDao skuInfoDao;
    @Autowired
    private VcodeActivityRebateRuleCogService vcodeActivityRebateRuleCogService;
    @Autowired
    private VcodeActivityService vcodeActivityService;
    @Autowired
    private VpsVcodeActivateSharePrizeService activateSharePrizeService;
    @Autowired
    private FissionService fissionService;
    @Autowired
    protected VpsOperationLogService logService;

    @Override
    public  IPage<VpsVcodeActivateRedEnvelopeRuleCogVO>  selectPage(PageOrderInfo pageInfo, VpsVcodeActivateRedEnvelopeRuleCogVO queryBean, String tabsFlag,String companyKey) {
        IPage<VpsVcodeActivateRedEnvelopeRuleCogEntity> page = new Page<>();
        page.setCurrent(pageInfo.getCurrentPage());
        page.setPages(pageInfo.getStartCount());
        page.setSize(pageInfo.getPagePerCount());
        queryBean.setTabsFlag(tabsFlag);
        IPage<VpsVcodeActivateRedEnvelopeRuleCogVO> envelopeRuleCogVoPage = this.baseMapper.selectPageVo(page, queryBean,pageInfo);
        List<SkuInfo> skuInfos = skuInfoDao.loadSkuListByCompany(companyKey);
        List<VcodeActivityCog> activityCogList = vcodeActivityService
                .findAllVcodeActivityList(Constant.activityType.activity_type0, "");
        for (VpsVcodeActivateRedEnvelopeRuleCogVO editEntity:envelopeRuleCogVoPage.getRecords()) {
            String[] activationSkuKeyList = editEntity.getActivationSkuKeyList() != null ? editEntity.getActivationSkuKeyList().split(",") : new String[0];
            StringBuilder activationSkuName = new StringBuilder();
            StringBuilder vcodeActivityName = new StringBuilder();
            for (SkuInfo skuInfo: skuInfos) {
                for (int i = 0; i < activationSkuKeyList.length; i++) {
                    if(skuInfo.getSkuKey().equals(activationSkuKeyList[i])){
                        activationSkuName.append(skuInfo.getSkuName()).append(",");
                    }
                }

            }
            for (VcodeActivityCog activityCog : activityCogList) {
                String[] activityKeyArray = editEntity.getShareVcodeActivityKey() != null ? editEntity.getShareVcodeActivityKey().split(",") : new String[0];
                for (int i = 0; i < activityKeyArray.length; i++) {
                    if(activityCog.getVcodeActivityKey().equals(activityKeyArray[i])){
                        vcodeActivityName.append(activityCog.getVcodeActivityName()).append(",");
                    }
                }
            }
            editEntity.setActivationSkuKeyList(activationSkuName.toString());
            editEntity.setShareVcodeActivityKey(vcodeActivityName.toString());
        }


        return envelopeRuleCogVoPage;
    }

    @Override
    public String checkName(String checkName,String activityKey) {
        VpsVcodeActivateRedEnvelopeRuleCogEntity entity = new VpsVcodeActivateRedEnvelopeRuleCogEntity();
        entity.setRuleName(checkName);
        QueryWrapper<VpsVcodeActivateRedEnvelopeRuleCogEntity> ruleCogEntityQueryWrapper = new QueryWrapper<>(entity);
        if(StringUtils.isNotEmpty(activityKey)){
            ruleCogEntityQueryWrapper.notIn(VpsVcodeActivateRedEnvelopeRuleCogEntity.ACTIVITY_KEY, activityKey);
        }
        VpsVcodeActivateRedEnvelopeRuleCogEntity resultData = this.baseMapper.selectOne(ruleCogEntityQueryWrapper);
        if(resultData==null){
            return "1";
        }
        return "0";

    }

    @Override
    public String checkRule(String ruleKey) {
        Integer validRebateRuleCogNumByActivityKey = vcodeActivityRebateRuleCogService.getValidRebateRuleCogNumByActivityKey(ruleKey);
        if(validRebateRuleCogNumByActivityKey!=null && validRebateRuleCogNumByActivityKey > 0){
            return "1";
        }
        return "0";
    }

    @Override
    public VpsVcodeActivateRedEnvelopeRuleCogVO getOneByActivityKey(String activityKey) {
        VpsVcodeActivateRedEnvelopeRuleCogEntity entity = this.baseMapper.selectById(activityKey);
        VpsVcodeActivateRedEnvelopeRuleCogVO vo = new VpsVcodeActivateRedEnvelopeRuleCogVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setStartDate(DateUtil.getDateTime(entity.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
        vo.setEndDate(DateUtil.getDateTime(entity.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT));
        vo.setActivationSkuKeyArray(entity.getActivationSkuKeyList().split(","));
        vo.setShareVcodeActivityKeyArray(StringUtils.isNotEmpty(entity.getShareVcodeActivityKey())?entity.getShareVcodeActivityKey().split(","):new String[]{});

        vo.setNewUserType(entity.getNewUserType());
        vo.setShareAwardType(entity.getShareAwardType());
        vo.setAcceptShareNewUserMinMoney(entity.getAcceptShareNewUserMinMoney());
        vo.setAcceptShareNewUserMaxMoney(entity.getAcceptShareNewUserMaxMoney());
        vo.setSharerGetRewardDayLimit(entity.getSharerGetRewardDayLimit());
        vo.setSharerUserReceiveNumLimit(entity.getSharerUserReceiveNumLimit());
        vo.setTaskCompletionLimitDay(entity.getTaskCompletionLimitDay());
        vo.setUserReceiveNumberDayLimit(entity.getUserReceiveNumberDayLimit());
        vo.setRoleDescribe(entity.getRoleDescribe());
        vo.setNewUserAsk(entity.getNewUserAsk());
        String convert = MathUtil.convert(Double.parseDouble(entity.getMoneyLimit()), 2,false);
        vo.setMoneyLimit(convert);
        return vo;
    }

    @Override
    public VcodeActivityCog transformForActivityCog(VpsVcodeActivateRedEnvelopeRuleCogVO oneByActivityKey) {
        VcodeActivityCog activityCog = new VcodeActivityCog();
        activityCog.setVcodeActivityKey(oneByActivityKey.getActivityKey());
        activityCog.setVcodeActivityName(oneByActivityKey.getRuleName());
        activityCog.setStartDate(oneByActivityKey.getStartDate());
        activityCog.setEndDate(oneByActivityKey.getStartDate());
        activityCog.setActivityType(Constant.activityType.activity_type7);
        activityCog.setMoneyConfigFlag(oneByActivityKey.getMoneyConfigFlag());
        return activityCog;
    }

    @Override
    public void changeStatus(String vcodeActivityKey)  {
        if(StringUtils.isNotEmpty(vcodeActivityKey)){
            VpsVcodeActivateRedEnvelopeRuleCogEntity entity = new VpsVcodeActivateRedEnvelopeRuleCogEntity();
            entity.setActivityKey(vcodeActivityKey);
            entity.setMoneyConfigFlag("1");
            this.baseMapper.updateById(entity);
        }
    }

    @Override
    public List<String> getTabs1SkuKey(String s,String ruleKey,String startDate,String endDate,String shareType) {
        return  this.baseMapper.getTabs1SkuKey(s,ruleKey,startDate,endDate,shareType);
    }

    @Override
    public List<String> getTabs1ActivateList(String s, String actityKeyList, String startDate, String endDate,String shareType) {
        return  this.baseMapper.getTabs1ActivateList(s,actityKeyList,startDate,endDate,shareType);
    }

    @Override
    public String updateActivityCog(SysUserBasis currentUser, VpsVcodeActivateRedEnvelopeRuleCogVO param) throws Exception {
        if (param.getActivationSkuKeyList().length() > 300) {
            return "您选择的sku超出数量，请联系管理员";
        }
        // 校验配置时间段是否已有活动
        boolean repeatFlag = checkRepeatDate(param.getStartDate(), param.getEndDate(), param.getActivityKey());
        if (!repeatFlag){
            return "当前选择时间已有活动,请重新选择";
        }

        if (param.getTaskCompletionLimitDay().equals("")) {
            param.setTaskCompletionLimitDay(null);
        }
        VpsVcodeActivateRedEnvelopeRuleCogEntity entity = new VpsVcodeActivateRedEnvelopeRuleCogEntity();
        BeanUtils.copyProperties(param, entity);
        // 如果是新版海报分享红包处理分享者激励奖项配置
        if (param.getShareType() ==3 && StringUtils.isNotBlank(param.getShareAwardType())) {

            // 校验奖品配置格式
            String checkResult = checkRuleFormat(param);
            if (StringUtils.isNotBlank(checkResult)) {
                return checkResult;
            }

            // 清除原有奖项配置
            activateSharePrizeService.deletePrizeCogByActivityKey(entity.getActivityKey());
            // 添加奖项配置
            int newUserPrizeLength = param.getNewUserPrizePercent() == null ? 0 : param.getNewUserPrizePercent().length;
            int oldUserPrizeLength = param.getOldUserPrizePercent() == null ? 0 : param.getOldUserPrizePercent().length;
            if (StringUtils.isNotBlank(param.getShareAwardType()) && param.getShareAwardType().contains("0") && newUserPrizeLength > 0) {
                List<VpsVcodeActivateSharePrizeCog> prizeCogList = new ArrayList<>();

                for (int i = 0; i < newUserPrizeLength; i++) {
                    VpsVcodeActivateSharePrizeCog sharePrizeCog = new VpsVcodeActivateSharePrizeCog();
                    sharePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
                    sharePrizeCog.setActivateActivityKey(entity.getActivityKey());
                    sharePrizeCog.setPrizeType("0");
                    sharePrizeCog.setPrizeUserType("0");
                    sharePrizeCog.setMinMoney(Double.parseDouble(param.getNewUserPrizeMinMoney()[i]));
                    sharePrizeCog.setMaxMoney(Double.parseDouble(param.getNewUserPrizeMaxMoney()[i]));
                    sharePrizeCog.setPrizePercent(Integer.parseInt(param.getNewUserPrizePercent()[i]));
                    sharePrizeCog.setPrizeNo(Integer.parseInt(param.getNewUserPrizeNo()[i]));
                    sharePrizeCog.setDeleteFlag("0");
                    sharePrizeCog.setCreateTime(DateUtil.getDateTime());
                    prizeCogList.add(sharePrizeCog);
                }
                activateSharePrizeService.create(prizeCogList);
            }

            if (StringUtils.isNotBlank(param.getShareAwardType()) && param.getShareAwardType().contains("1") && oldUserPrizeLength > 0){
                List<VpsVcodeActivateSharePrizeCog> prizeCogList = new ArrayList<>();
                for (int i = 0; i < oldUserPrizeLength; i++) {
                    VpsVcodeActivateSharePrizeCog sharePrizeCog = new VpsVcodeActivateSharePrizeCog();
                    sharePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
                    sharePrizeCog.setActivateActivityKey(entity.getActivityKey());
                    sharePrizeCog.setPrizeType("0");
                    sharePrizeCog.setPrizeUserType("1");
                    sharePrizeCog.setMinMoney(Double.parseDouble(param.getOldUserPrizeMinMoney()[i]));
                    sharePrizeCog.setMaxMoney(Double.parseDouble(param.getOldUserPrizeMaxMoney()[i]));
                    sharePrizeCog.setPrizePercent(Integer.parseInt(param.getOldUserPrizePercent()[i]));
                    sharePrizeCog.setPrizeNo(Integer.parseInt(param.getOldUserPrizeNo()[i]));
                    sharePrizeCog.setDeleteFlag("0");
                    sharePrizeCog.setCreateTime(DateUtil.getDateTime());
                    prizeCogList.add(sharePrizeCog);
                }
                activateSharePrizeService.create(prizeCogList);
            }
        }


        if(StringUtils.isNotEmpty(param.getStartDate())){
            entity.setStartDate(DateUtil.parse(param.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
        }
        if(StringUtils.isNotEmpty(param.getEndDate())){
            Date parse = DateUtil.parse(param.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT);
            String dateTime = DateUtil.getDateTime(parse, DateUtil.DEFAULT_DATE_FORMAT);
            String endDateTimeByDay = DateUtil.getEndDateTimeByDay(dateTime);
            entity.setEndDate(DateUtil.parse(endDateTimeByDay,DateUtil.DEFAULT_DATETIME_FORMAT));
        }
        if (param.getShareType() == 3) {
            entity.setRiskUserLimitMaxMoney(param.getRiskUserLimitMaxMoney());
            entity.setRiskUserLimitMinMoney(param.getRiskUserLimitMinMoney());
        }
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(currentUser.getUserKey());
        this.baseMapper.updateActivityCog(entity);
        // 操作日志
        logService.saveLog("ActivateRedEnvelopeRuleCog", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(param), "新增分享裂变待激活活动：activityKey" + param.getActivityKey());
        return "修改成功";
    }

    @Override
    public String saveActivityCog(SysUserBasis currentUser, VpsVcodeActivateRedEnvelopeRuleCogVO param) throws Exception {
        if (param.getActivationSkuKeyList().length() > 300) {
            return "您选择的sku超出数量，请联系管理员";
        }
        // 校验配置时间段是否已有活动
        boolean repeatFlag = checkRepeatDate(param.getStartDate(), param.getEndDate(), null);
        if (!repeatFlag){
            return "当前选择时间已有活动,请重新选择";
        }

        if (param.getTaskCompletionLimitDay().equals("")) {
            param.setTaskCompletionLimitDay(null);
        }
        VpsVcodeActivateRedEnvelopeRuleCogEntity entity = new VpsVcodeActivateRedEnvelopeRuleCogEntity();

        param.getActivationSkuKeyList();
        entity.setRuleNo(fissionService.getRuleNo());
        String activityKey = UUIDTools.getInstance().getUUID();
        entity.setActivityKey(activityKey);

        if (param.getShareType() == 3 && StringUtils.isNotBlank(param.getShareAwardType())) {
            // 校验奖品配置格式
            String checkResult = checkRuleFormat(param);
            if (StringUtils.isNotBlank(checkResult)) {
                return checkResult;
            }

            // 处理分享者待激活奖项配置 如果活动类型是新版海报分享且奖项配置不为空
            int newUserPrizeLength = param.getNewUserPrizePercent().length;
            int oldUserPrizeLength = param.getOldUserPrizePercent().length;
            if (param.getShareType() == 3 && newUserPrizeLength > 0) {
                List<VpsVcodeActivateSharePrizeCog> prizeCogList = new ArrayList<>();

                for (int i = 0; i < newUserPrizeLength; i++) {
                    VpsVcodeActivateSharePrizeCog sharePrizeCog = new VpsVcodeActivateSharePrizeCog();
                    sharePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
                    sharePrizeCog.setActivateActivityKey(entity.getActivityKey());
                    sharePrizeCog.setPrizeType("0");
                    sharePrizeCog.setPrizeUserType("0");
                    sharePrizeCog.setMinMoney(Double.parseDouble(param.getNewUserPrizeMinMoney()[i]));
                    sharePrizeCog.setMaxMoney(Double.parseDouble(param.getNewUserPrizeMaxMoney()[i]));
                    sharePrizeCog.setPrizePercent(Integer.parseInt(param.getNewUserPrizePercent()[i]));
                    sharePrizeCog.setPrizeNo(Integer.parseInt(param.getNewUserPrizeNo()[i]));
                    sharePrizeCog.setDeleteFlag("0");
                    sharePrizeCog.setCreateTime(DateUtil.getDateTime());
                    prizeCogList.add(sharePrizeCog);
                }
                activateSharePrizeService.create(prizeCogList);
            }

            if (param.getShareType() == 3 && oldUserPrizeLength > 0){
                List<VpsVcodeActivateSharePrizeCog> prizeCogList = new ArrayList<>();
                for (int i = 0; i < oldUserPrizeLength; i++) {
                    VpsVcodeActivateSharePrizeCog sharePrizeCog = new VpsVcodeActivateSharePrizeCog();
                    sharePrizeCog.setInfoKey(UUIDTools.getInstance().getUUID());
                    sharePrizeCog.setActivateActivityKey(entity.getActivityKey());
                    sharePrizeCog.setPrizeType("0");
                    sharePrizeCog.setPrizeUserType("1");
                    sharePrizeCog.setMinMoney(Double.parseDouble(param.getOldUserPrizeMinMoney()[i]));
                    sharePrizeCog.setMaxMoney(Double.parseDouble(param.getOldUserPrizeMaxMoney()[i]));
                    sharePrizeCog.setPrizePercent(Integer.parseInt(param.getOldUserPrizePercent()[i]));
                    sharePrizeCog.setPrizeNo(Integer.parseInt(param.getOldUserPrizeNo()[i]));
                    sharePrizeCog.setDeleteFlag("0");
                    sharePrizeCog.setCreateTime(DateUtil.getDateTime());
                    prizeCogList.add(sharePrizeCog);
                }
                activateSharePrizeService.create(prizeCogList);
            }
        }


        if (param.getShareType() == 3) {
            entity.setRiskUserLimitMaxMoney(param.getRiskUserLimitMaxMoney());
            entity.setRiskUserLimitMinMoney(param.getRiskUserLimitMinMoney());
        }
        entity.setRuleName(param.getRuleName());
        entity.setStateFlag(param.getStateFlag());
        entity.setActivationSkuKeyList(param.getActivationSkuKeyList());
        entity.setLimitType(param.getLimitType());
        entity.setUserKey(currentUser.getUserKey());
        entity.setTaskCompletionTimeLimit(param.getTaskCompletionTimeLimit());
        entity.setReceiveTimeLimit(param.getReceiveTimeLimit());
        entity.setNumberLimit(param.getNumberLimit());
        entity.setMoneyLimit(param.getMoneyLimit());
        entity.setStartDate(DateUtil.parse(param.getStartDate(),DateUtil.DEFAULT_DATE_FORMAT));
        Date parse = DateUtil.parse(param.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT);
        String dateTime = DateUtil.getDateTime(parse, DateUtil.DEFAULT_DATE_FORMAT);
        String endDateTimeByDay = DateUtil.getEndDateTimeByDay(dateTime);
        entity.setEndDate(DateUtil.parse(endDateTimeByDay,DateUtil.DEFAULT_DATETIME_FORMAT));
        entity.setCreateTime(new Date());
        entity.setCreateUser(currentUser.getUserKey());
        entity.setReceiveNumber(param.getReceiveNumber());
        entity.setShareVcodeActivityKey(param.getShareVcodeActivityKey());
        entity.setUserReceiveNumberLimit(param.getUserReceiveNumberLimit());
        entity.setDeleteFlag("0");
        entity.setMoneyConfigFlag("0");
        entity.setUserRewardFlag(param.getUserRewardFlag());
        entity.setOfficialAccountFlag(param.getOfficialAccountFlag());
        entity.setOfficialAccountUrl(param.getOfficialAccountUrl());
        entity.setRewardType(param.getRewardType());
        entity.setShareArea(param.getShareArea());
        entity.setMoneyRegion(param.getMoneyRegion());
        entity.setPointRegion(param.getPointRegion());
        entity.setShareMoneyLimit(param.getShareMoneyLimit());
        entity.setSharePointLimit(param.getSharePointLimit());
        entity.setShareFlag(param.getShareFlag());
        entity.setShareType(param.getShareType());
        entity.setRewardReceiveLimit(param.getRewardReceiveLimit());
        entity.setSharerGetRewardLimit(param.getSharerGetRewardLimit());
        entity.setNewUserType(param.getNewUserType());
        entity.setShareAwardType(param.getShareAwardType());
        entity.setAcceptShareNewUserMinMoney(param.getAcceptShareNewUserMinMoney());
        entity.setAcceptShareNewUserMaxMoney(param.getAcceptShareNewUserMaxMoney());
        entity.setSharerGetRewardDayLimit(param.getSharerGetRewardDayLimit());
        entity.setSharerUserReceiveNumLimit(param.getSharerUserReceiveNumLimit());
        entity.setTaskCompletionLimitDay(param.getTaskCompletionLimitDay());
        entity.setUserReceiveNumberDayLimit(param.getUserReceiveNumberDayLimit());
        entity.setRoleDescribe(param.getRoleDescribe());
        entity.setNewUserAsk(param.getNewUserAsk());

        save(entity);
        // 操作日志
        logService.saveLog("ActivateRedEnvelopeRuleCog", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(param), "新增分享裂变待激活活动" + activityKey);
        return "添加成功";
    }

    private boolean checkRepeatDate(String startDate, String endDate, String activityKey) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", startDate.split(" ")[0] + " 00:00:00");
        map.put("endDate", endDate.split(" ")[0]  + " 23:59:59");
        map.put("activityKey", activityKey);
        int count = this.baseMapper.queryOverlapActivity(map);
        return count == 0;
    }

    private String checkRuleFormat(VpsVcodeActivateRedEnvelopeRuleCogVO param) {
        int newUserPrizeLength = param.getNewUserPrizePercent() == null ? 0 : param.getNewUserPrizePercent().length;
        int oldUserPrizeLength = param.getOldUserPrizePercent() == null ? 0 : param.getOldUserPrizePercent().length;

        for (String shareAwardType : param.getShareAwardType().split(",")) {
            if (shareAwardType.equals("0") && newUserPrizeLength <= 0) {
                return "新用户奖励配置不能为空";
            }

            if (shareAwardType.equals("1") && oldUserPrizeLength <= 0) {
                return "老用户奖励配置不能为空";
            }

        }

        for (int i = 0; i < newUserPrizeLength; i++) {
            if (StringUtils.isBlank(param.getNewUserPrizeMinMoney()[i])|| Double.parseDouble(param.getNewUserPrizeMinMoney()[i]) <= 0) {
                return "奖励金额最小区间应为正整数";
            }
            if (StringUtils.isBlank(param.getNewUserPrizeMaxMoney()[i])|| Double.parseDouble(param.getNewUserPrizeMaxMoney()[i]) <= 0) {
                return "奖励金额最大区间应为正整数";
            }
            if (Double.parseDouble(param.getNewUserPrizeMaxMoney()[i]) < Double.parseDouble(param.getNewUserPrizeMinMoney()[i])) {
                return "最大金额不能小于最小金额";
            }
            if (StringUtils.isBlank(param.getNewUserPrizePercent()[i]) || Integer.parseInt(param.getNewUserPrizePercent()[i]) <= 0) {
                return "中出占比应为正整数";
            }
        }

        for (int i = 0; i < oldUserPrizeLength; i++) {
            if (StringUtils.isBlank(param.getOldUserPrizeMinMoney()[i])|| Double.parseDouble(param.getOldUserPrizeMinMoney()[i]) <= 0) {
                return "奖励金额最小区间应为正整数";
            }
            if (StringUtils.isBlank(param.getOldUserPrizeMaxMoney()[i])|| Double.parseDouble(param.getOldUserPrizeMaxMoney()[i]) <= 0) {
                return "奖励金额最大区间应为正整数";
            }
            if (Double.parseDouble(param.getOldUserPrizeMaxMoney()[i]) < Double.parseDouble(param.getOldUserPrizeMinMoney()[i])) {
                return "最大金额不能小于最小金额";
            }
            if (StringUtils.isBlank(param.getOldUserPrizePercent()[i]) || Integer.parseInt(param.getOldUserPrizePercent()[i]) <= 0) {
                return "中出占比应为正整数";
            }
        }

        return null;
    }
}
