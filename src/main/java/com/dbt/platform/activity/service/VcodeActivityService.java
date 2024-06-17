package com.dbt.platform.activity.service;

import java.io.IOException;
import java.util.*;

import com.dbt.platform.activity.bean.VcodeActivityCogSkuRelation;
import com.dbt.platform.activity.dao.IVcodeActivityCogSkuRelationDao;
import com.dbt.platform.waitActivation.service.WaitActivationService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityCogExtends;
import com.dbt.platform.activity.dao.IVcodeActivityCogDao;
import com.dbt.platform.doubleprize.service.VpsVcodeDoublePrizeCogService;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.fission.service.IVpsVcodeActivateRedEnvelopeRuleCogService;
import com.dbt.platform.promotion.service.VpsVcodeBindPromotionCogService;
import com.dbt.platform.signin.service.VpsVcodeSigninCogService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.turntable.service.TurntableService;

import javax.annotation.Resource;

/**
 * @author RoyFu
 * @createTime 2016年1月19日 下午5:49:44
 * @description
 */
@Service
public class VcodeActivityService extends BaseService<VcodeActivityCog> {

    Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private IVcodeActivityCogDao activityCogDao;
    @Autowired
    private VpsVcodeSigninCogService signinCogService;
    @Autowired
    private VpsVcodeBindPromotionCogService bindPromotionCogService;
    @Autowired
    private VpsVcodeDoublePrizeCogService doublePrizeCogService;
    @Autowired
    private IVpsVcodeActivateRedEnvelopeRuleCogService iVpsVcodeActivateRedEnvelopeRuleCogService;
	@Autowired
	private TurntableService turntableService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private WaitActivationService waitActivationService;
    @Resource
    private IVcodeActivityCogSkuRelationDao vcodeActivityCogSkuRelationDao;

    /**
     * 获取某个V码活动
     *
     * @param vcodeActivityKey
     * @return
     */
    public VcodeActivityCog loadActivityByKey(String vcodeActivityKey) {
        VcodeActivityCog activityCog = activityCogDao.findById(vcodeActivityKey);
        return activityCog;
    }

    /**
     * 获取某个V码活动
     *
     * @param vcodeActivityKey
     * @return
     */
    public VcodeActivityCog loadActivityByKey(String vcodeActivityKey, String activityType) {
        VcodeActivityCog activityCog = null;
        if (Constant.activityType.activity_type4.equals(activityType)) {
            activityCog = signinCogService.transformForActivityCog(signinCogService.findActivityByKey(vcodeActivityKey));

        } else if (Constant.activityType.activity_type5.equals(activityType)) {
            activityCog = bindPromotionCogService.transformForActivityCog(bindPromotionCogService.findActivityByKey(vcodeActivityKey));

        } else if (Constant.activityType.activity_type6.equals(activityType)) {
            activityCog = doublePrizeCogService.transformForActivityCog(doublePrizeCogService.findActivityByKey(vcodeActivityKey));

        } else if (Constant.activityType.activity_type7.equals(activityType)) {
            activityCog = iVpsVcodeActivateRedEnvelopeRuleCogService.transformForActivityCog(iVpsVcodeActivateRedEnvelopeRuleCogService.getOneByActivityKey(vcodeActivityKey));

        } else if (Constant.activityType.activity_type8.equals(activityType) ||Constant.activityType.activity_type9.equals(activityType)) {
			activityCog = turntableService.transformForActivityCog(turntableService.queryTurntableActivityInfoByKey(vcodeActivityKey));
        } else if(Constant.activityType.activity_type13.equals(activityType)){
            activityCog = waitActivationService.transformForActivityCog(waitActivationService.findWaitActivationRule());

        }else {
            activityCog = activityCogDao.findById(vcodeActivityKey);
        }

        return activityCog;
    }

    /**
     * 活动列表
     *
     * @param queryBean
     * @param pageInfo
     * @return
     * @throws Exception
     */
    public List<VcodeActivityCog> findVcodeActivityList(
            VcodeActivityCog queryBean, PageOrderInfo pageInfo) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        List<VcodeActivityCog> activityList = activityCogDao.queryForLst(map);

        if (null != activityList && !activityList.isEmpty()) {
            Object cacheObj = null;
            VcodeActivityCog configItem = null;
            String currDate = DateUtil.getDate();
            String cacheStr = CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT;
            for (VcodeActivityCog item : activityList) {
                // 校验缓存
                cacheObj = CacheUtilNew.getCacheValue(cacheStr + item.getVcodeActivityKey());
                if (null == cacheObj) continue;
                configItem = JSON.parseObject(cacheObj.toString(), VcodeActivityCog.class);
                if (null == configItem) continue;
                if (!item.getStartDate().equals(configItem.getStartDate())
                        || !item.getEndDate().equals(configItem.getEndDate())) {

                    if (currDate.compareTo(configItem.getEndDate()) > 0) {
                        item.setIsBegin("缓存异常(已结束)");

                    } else if (currDate.compareTo(configItem.getStartDate()) < 0) {
                        item.setIsBegin("缓存异常(未开始)");

                    } else {
                        item.setIsBegin("缓存异常(进行中)");
                    }
                }
            }
        }
        return activityList;
    }

    /**
     * 活动列表条数
     *
     * @param queryBean
     * @return
     */
    public int countVcodeActivityList(VcodeActivityCog queryBean) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("queryBean", queryBean);
        return activityCogDao.queryForCount(map);
    }

    /**
     * 创建活动
     *
     * @param activityCog
     * @param currentUserKey
     */
    public String writeActivityCog(VcodeActivityCog activityCog, SysUserBasis optUser) throws Exception {

        // 校验名称是否重复
        if ("1".equals(checkBussionName("vcodeActivity", "vcode_activity_key",
                null, "vcode_activity_name", activityCog.getVcodeActivityName()))) {
            throw new BusinessException("活动名称已存在");
        }

        // 完善活动信息
        activityCog.setVcodeActivityKey(UUID.randomUUID().toString());
        activityCog.setVcodeActivityNo(getBussionNo("vcodeActivity", "vcode_activity_no", Constant.OrderNoType.type_HD));
        activityCog.setCompanyKey(optUser.getCompanyKey());
        activityCog.setActivityType(Constant.activityType.activity_type0);
        activityCog.setDoubtRuleType(Constant.doubtRuleType.RULE_TYPE_2); // 3.0版本统一处理为区间模式
        activityCog.fillFields(optUser.getUserKey());

        // 风控规则
        activityCog.setDoubtRuleType("2");

        // 是否已有金额配置：0：否；1：是
        activityCog.setMoneyConfigFlag("0");

        // 保存活动
        activityCogDao.create(activityCog);

        // 保存活动关联的SKU
        List<VcodeActivityCogSkuRelation> list = new ArrayList<>();
        for (String skuKey:activityCog.getSkuList()) {
            VcodeActivityCogSkuRelation relation = new VcodeActivityCogSkuRelation(activityCog.getVcodeActivityKey(), skuKey);
            list.add(relation);
        }
        vcodeActivityCogSkuRelationDao.batchInsert(list);

        logService.saveLog("vcodeActivity", Constant.OPERATION_LOG_TYPE.TYPE_1,
                JSON.toJSONString(activityCog), "创建活动:" + activityCog.getVcodeActivityName());
        
        // 接口各缓存按组清空
        CacheUtilNew.addGroupModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + "_MZTACTIVITY");
        CacheUtilNew.addGroupModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + "_JHACTIVITY_");
        CacheUtilNew.addGroupModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + "_ZJACTIVITY_");

        // 活动主键
        return activityCog.getVcodeActivityKey();
    }

    /**
     * 更新活动
     *
     * @param activityCog
     * @param currentUserKey
     * @throws IOException
     */
    public void updateActivityCog(VcodeActivityCog activityCog, SysUserBasis optUser) throws Exception {

        // 校验名称是否重复
        if ("1".equals(checkBussionName("vcodeActivity", "vcode_activity_key", activityCog
                .getVcodeActivityKey(), "vcode_activity_name", activityCog.getVcodeActivityName()))) {
            throw new BusinessException("活动名称已存在");
        }

        // 保存活动
        activityCog.setDoubtRuleType(Constant.doubtRuleType.RULE_TYPE_2); // 3.0版本统一处理为区间模式
        activityCog.fillUpdateFields(optUser.getUserKey());
        activityCogDao.update(activityCog);

        // 保存活动关联的SKU
        List<VcodeActivityCogSkuRelation> list = new ArrayList<>();
        for (String skuKey:activityCog.getSkuList()) {
            VcodeActivityCogSkuRelation relation = new VcodeActivityCogSkuRelation(activityCog.getVcodeActivityKey(), skuKey);
            list.add(relation);
        }
        vcodeActivityCogSkuRelationDao.deleteByActivityKey(activityCog.getVcodeActivityKey());
        vcodeActivityCogSkuRelationDao.batchInsert(list);


        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
                .KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + activityCog.getVcodeActivityKey());

        // 接口各缓存按组清空
        CacheUtilNew.addGroupModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + "_MZTACTIVITY");
        CacheUtilNew.addGroupModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + "_JHACTIVITY");
        CacheUtilNew.addGroupModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + "_ZJACTIVITY");

        logService.saveLog("vcodeActivity", Constant.OPERATION_LOG_TYPE.TYPE_2,
                JSON.toJSONString(activityCog), "修改活动:" + activityCog.getVcodeActivityName());
    }

    /**
     * 判断时间是否在活动范围内
     *
     * @param vcodeActivityKey</br>
     * @param startDate</br>
     * @param endDate</br>
     * @return String flag 0 是  1 否</br>
     */
    public String judgeActivityTime(String vcodeActivityKey, String startDate,
                                    String endDate) {
        String flag = "0";
        VcodeActivityCog activityCogs = activityCogDao.findById(vcodeActivityKey);
        if (null != activityCogs) {
            LocalDate batchStartDate = new LocalDate(startDate);
            LocalDate batchEndDate = new LocalDate(endDate);
            LocalDate activityStartDate = new LocalDate(activityCogs.getStartDate());
            LocalDate activityEndDate = new LocalDate(activityCogs.getEndDate());
            if (batchStartDate.isBefore(activityStartDate)
                    || batchStartDate.isAfter(activityEndDate)
                    || batchEndDate.isBefore(activityStartDate)
                    || batchEndDate.isAfter(activityEndDate)) {
                flag = "1";
            }
        }
        return flag;
    }

    /**
     * 修改活动的活动配置添加状态
     *
     * @param vcodeActivityCog
     */
    public void changeStatus(VcodeActivityCog vcodeActivityCog) {
        activityCogDao.changeStatus(vcodeActivityCog);
        try {
            CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW
                    + Constant.DBTSPLIT + vcodeActivityCog.getVcodeActivityKey());
        } catch (Exception e) {
            log.error("修改活动的配置奖项标识失败");
            e.printStackTrace();
        }
    }

    /**
     * 查询所有活动
     *
     * @param </br>
     * @return List<VcodeActivityCog> </br>
     */
    public List<VcodeActivityCog> findAllVcodeActivityList(String activityType, String vcodeActivityKey) {
        if(Constant.activityType.activity_type13.equals(activityType)){
            /*VcodeActivityCog activityCog = waitActivationService.transformForActivityCogPadField(waitActivationService.findWaitActivationRule());
            List<VcodeActivityCog> list = new ArrayList<>();
            list.add(activityCog);*/
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("activityType", activityType);
        map.put("currActivityKey", StringUtils.defaultIfBlank(vcodeActivityKey, ""));
        return activityCogDao.findAllVcodeActivityList(map);
    }

    public VcodeActivityCog findById(String vcodeActivityKey) {
        return activityCogDao.findById(vcodeActivityKey);
    }

    /**
     * 根据skuKey查询活动count
     *
     * @param skuKey </br>
     * @return int </br>
     */
    public int queryActivityCountBySkuKey(String skuKey) {
        return activityCogDao.queryActivityCountBySkuKey(skuKey);
    }

    /**
     * 根据skuKey查询活动List
     *
     * @param skuKey </br>
     * @return List<VcodeActivityCog> </br>
     */
    public List<VcodeActivityCog> queryActivityListBySkuKey(String skuKey) {
        return activityCogDao.queryActivityListBySkuKey(skuKey);
    }

    /**
     * 查询可配置一码多扫的活动（没有关联一码多扫或已关联并过期的活动 ）
     *
     * @param infoKey
     * @return
     */
    public List<VcodeActivityCog> queryListForSuitMorescan(String infoKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("infoKey", infoKey);
        return activityCogDao.queryListForSuitMorescan(map);
    }

    /**
     * 查询可配置逢百规则的活动（没有关联逢百规则或已关联并过期的活动 ）
     *
     * @param infoKey
     * @return
     */
    public List<VcodeActivityCog> queryListForSuitPerhundred(String infoKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("infoKey", infoKey);
        return activityCogDao.queryListForSuitPerhundred(map);
    }


    /**
     * 查询可配置频次规则的活动（没有关联频次规则或已关联并过期的活动 ）
     *
     * @param infoKey
     * @return
     */
    public List<VcodeActivityCog> queryListForNotFrequencyCog(String infoKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("infoKey", infoKey);
        return activityCogDao.queryListForNotFrequencyCog(map);
    }

    /**
     * 获取所有活动版本号
     */
    public List<SysDataDic> queryAllActivityVersion() {
        List<SysDataDic> versionLst = new LinkedList<>();

        // 数据字典默认序号正序
        List<SysDataDic> itemList = DatadicUtil
                .getListByCategoryCode(DatadicKey.dataDicCategory.FILTER_ACTIVITYVERSION);
        for (SysDataDic sysDataDic : itemList) {
            if (StringUtils.isBlank(sysDataDic.getDataAlias())) continue;
            versionLst.add(sysDataDic);
        }
        return versionLst;
    }

    /**
     * 查询可配置阶梯的有效活动
     *
     * @return
     */
    public List<VcodeActivityCog> queryListForLadder(Map<String, Object> map) {
        return activityCogDao.queryListForLadder(map);
    }

    /**
     * 查询未结束的活动
     *
     * @return
     */
    public List<VcodeActivityCog> queryValidActivity() {
        return activityCogDao.queryValidActivity();
    }

    /**
     * 依据活动主键查询活动
     *
     * @return
     */
    public List<VcodeActivityCog> queryByActivityKey(String... vcodeActivityKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("activityKeyAry", vcodeActivityKey);
        return activityCogDao.queryByActivityKey(map);
    }

    /**
     * 查询结束前48小时进行短信预警的活动
     */
    public List<VcodeActivityCog> queryActivityExpiredPrize() {
        return activityCogDao.queryActivityExpiredPrize();
    }

    /**
     * 依据主键获取活动名称
     *
     * @param vcodeActivkktyKey
     * @return
     */
    public String findActivityNameByKey(String vcodeActivkktyKey) {
        return activityCogDao.findActivityNameByKey(vcodeActivkktyKey);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateActivityExtendCog(List<VcodeActivityCogExtends> activityCog, String vcodeActivityKey) {
        List<String> roleList = Arrays.asList(DatadicUtil.getDataDicValue(
                "data_constant_config", "role_info").split(","));
//		for (String roleId : roleList) {
//			try {
//				String keyName = CacheKey.cacheKey.vcode
//						.KEY_VCODE_ACTIVITY_COG_EXTENDS + Constant.DBTSPLIT + vcodeActivityKey+ Constant.DBTSPLIT +roleId.split(":")[0];
//				CacheUtilNew.removeGroupByKey(keyName);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
        String keyName = CacheKey.cacheKey.vcode
                .KEY_VCODE_ACTIVITY_COG_EXTENDS + Constant.DBTSPLIT + vcodeActivityKey + Constant.DBTSPLIT;
        try {
            CacheUtilNew.removeGroupByKey(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        activityCogDao.deleteactivityCogDaoByVcodeActivityKey(vcodeActivityKey);
        for (VcodeActivityCogExtends vcodeActivityCogExtends : activityCog) {
            vcodeActivityCogExtends.setExtendsId(UUIDTools.getInstance().getUUID());
            activityCogDao.createActivityExtendCog(vcodeActivityCogExtends);
        }

    }

    public List<VcodeActivityCogExtends> loadActivityExtendsByKey(String infoKey) {
        List<VcodeActivityCogExtends> activityCogExtendsList = activityCogDao.findActivityExtendsByVcodeActityKey(infoKey);
        return activityCogExtendsList;
    }


    public List<String> loadSkuKeyListByActivityKey(String vcodeActivityKey){
        return vcodeActivityCogSkuRelationDao.loadSkuKeysByActivityKey(vcodeActivityKey);
    }
}
