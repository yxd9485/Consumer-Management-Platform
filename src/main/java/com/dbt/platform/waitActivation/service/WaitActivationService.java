package com.dbt.platform.waitActivation.service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.commonrule.bean.CommonRule;
import com.dbt.platform.commonrule.service.CommonRuleService;
import com.dbt.platform.frequency.bean.VpsVcodeActivityFrequencyCog;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationMultipleRule;
import com.dbt.platform.waitActivation.bean.WaitActivationRule;
import com.dbt.platform.waitActivation.dao.WaitActivationMultipleRuleDao;
import com.dbt.platform.waitActivation.dao.WaitActivationPacksRecordDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 天降红包服务类
 */
@Service
public class WaitActivationService{

    @Autowired
    private CommonRuleService commonRuleService;
    @Autowired
    private WaitActivationMultipleRuleDao multipleRuleDao;
    @Autowired
    private WaitActivationPacksRecordDao recordDao;

    /**
     * 查询天降红包活动
     * @return
     */
    public WaitActivationRule findWaitActivationRule() {
        return commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_6, WaitActivationRule.class);
    }

    /**
     * 查询倍数中出规则
     * @return
     */
    public VpsWaitActivationMultipleRule queryMultipleRuleByID(String infoKey) {
        VpsWaitActivationMultipleRule byId = multipleRuleDao.findByInfoKey(infoKey);
        /*//查询当日中出个数
        List<String> tableList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            tableList.add("vps_wait_activation_packs_record_" + i);
        }
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("tableList",tableList);
        queryMap.put("vpointsCogKey",infoKey);
        int totalNum = recordDao.queryAllTables(queryMap);*/

        String prizeGrantNumCacheKey = RedisApiUtil.CacheKey.waitActivation.WAIT_ACTIVATION_PRIZE_GRANT_NUM
                + infoKey + DateUtil.getDate();//倍数规则奖项数量
        String prizeGrantNumStr = RedisApiUtil.getInstance().get(prizeGrantNumCacheKey);
        int totalNum = Integer.parseInt(StringUtils.defaultString(prizeGrantNumStr,"0"));

        byId.setTodayNum(totalNum);
        return byId;
    }

    /**
     * 编辑天降红活动
     * @param waitActivationRule
     * @param multipleRuleList
     * @param currentUser
     * @throws Exception
     */
    public List<String> addOrUpdateWaitActivationConfig(WaitActivationRule waitActivationRule,
                                                List<VpsWaitActivationMultipleRule> multipleRuleList,
                                                SysUserBasis currentUser) throws Exception {
        //构建返回List
        List<String> resultList = new ArrayList<>();

        //构建天降红包新对象
        CommonRule commonRule = new CommonRule();

        //查询天降红包活动（查询数据库）
        WaitActivationRule byRuleType = commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_6, WaitActivationRule.class);

        if (byRuleType == null){
            //没有天降红包活动  新增处理

            //新增处理--设置天降红包活动对象属性
            String infoKey = UUIDTools.getInstance().getUUID();
            waitActivationRule.setActivityKey(infoKey);
            commonRule.setInfoKey(infoKey);
            String date = DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT);
            waitActivationRule.setCreateTime(date);
            commonRule.setCreateUser(currentUser.getUserName());
            commonRule.setCreateTime(date);

            //新增处理--倍数中出规则处理
            List<String> multipleRuleInfoKeyList = new ArrayList<>();
            if (multipleRuleList!=null && multipleRuleList.size()>0){
                for (VpsWaitActivationMultipleRule multipleRule:multipleRuleList) {
                    String multipleRuleInfoKey = UUIDTools.getInstance().getUUID();
                    multipleRule.setInfoKey(multipleRuleInfoKey);
                    multipleRule.setActivityKey(infoKey);
                    multipleRule.setCreateTime(date);
                    multipleRule.setCreateUser(currentUser.getUserName());
                    multipleRule.setUpdateTime(date);
                    multipleRule.setUpdateUser(currentUser.getUserName());
                    multipleRuleDao.createMultipleRule(multipleRule);
                    multipleRuleInfoKeyList.add(multipleRuleInfoKey);
                }
            }

            //新增处理--设置天降红包规则倍数中出规则主键集合属性
            waitActivationRule.setMultipleRuleInfoKeyList(multipleRuleInfoKeyList);
        }else{
            //有天降红包活动  更新处理

            //更新操作删除活动缓存
            String activityCacheKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_VPS_COMMON_RULE_COG + Constant.DBTSPLIT + "ruleTypeSix";
            resultList.add(activityCacheKey);
            String activityKey = byRuleType.getActivityKey();
            waitActivationRule.setActivityKey(activityKey);
            waitActivationRule.setCreateTime(byRuleType.getCreateTime());

            //更新处理--倍数中出规则
            List<String> newMultipleRuleInfoKeyList = new ArrayList<>();
            if (multipleRuleList!=null && multipleRuleList.size()>0){
                for (VpsWaitActivationMultipleRule multipleRule:multipleRuleList) {
                    String date = DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT);
                    multipleRule.setActivityKey(activityKey);
                    if (StringUtils.isBlank(multipleRule.getInfoKey())){
                        //新增倍数中出规则
                        String multipleRuleInfoKey = UUIDTools.getInstance().getUUID();
                        multipleRule.setInfoKey(multipleRuleInfoKey);
                        multipleRule.setCreateTime(date);
                        multipleRule.setCreateUser(currentUser.getUserName());
                        multipleRule.setUpdateTime(date);
                        multipleRule.setUpdateUser(currentUser.getUserName());
                        multipleRuleDao.createMultipleRule(multipleRule);
                        newMultipleRuleInfoKeyList.add(multipleRuleInfoKey);
                    }else{
                        //修改倍数中出规则
                        String infoKey = multipleRule.getInfoKey();
                        multipleRule.setUpdateTime(date);
                        multipleRule.setUpdateUser(currentUser.getUserName());
                        multipleRuleDao.updateMultipleRule(multipleRule);
                        newMultipleRuleInfoKeyList.add(infoKey);
                        //删除缓存
                        String multipleRuleCacheKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_VPS_COMMON_RULE_COG +
                                Constant.DBTSPLIT + "ruleTypeSix"+ Constant.DBTSPLIT + infoKey;
                        resultList.add(multipleRuleCacheKey);
                    }
                }
            }

            //更新处理--倍数中出规则删除
            List<String> oldMultipleRuleInfoKeyList = byRuleType.getMultipleRuleInfoKeyList();
            if (oldMultipleRuleInfoKeyList != null && oldMultipleRuleInfoKeyList.size() > 0){
                //本次更新操作需要删除的倍数中出规则
                for (String oldInfoKey:oldMultipleRuleInfoKeyList) {
                    if (!newMultipleRuleInfoKeyList.contains(oldInfoKey)){
                        //删除规则
                        VpsWaitActivationMultipleRule multipleRule = new  VpsWaitActivationMultipleRule();
                        multipleRule.setInfoKey(oldInfoKey);
                        multipleRule.setDeleteFlag("1");
                        multipleRule.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
                        multipleRule.setUpdateUser(currentUser.getUserName());
                        multipleRuleDao.updateMultipleRule(multipleRule);
                        //删除缓存
                        String multipleRuleCacheKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_VPS_COMMON_RULE_COG +
                                Constant.DBTSPLIT + "ruleTypeSix"+ Constant.DBTSPLIT + oldInfoKey;
                        resultList.add(multipleRuleCacheKey);
                    }
                }
            }

            //更新处理--设置天降红包规则倍数中出规则主键集合属性
            waitActivationRule.setMultipleRuleInfoKeyList(newMultipleRuleInfoKeyList);
        }

        //激活sku处理
        String[] skuKeyArray = waitActivationRule.getSkuKeyArray();
        if(skuKeyArray!=null && skuKeyArray.length>0){
            List<String> skuKeyList = Arrays.asList(skuKeyArray);
            waitActivationRule.setSkuKeys(skuKeyList);
        }
        waitActivationRule.setSkuKeyArray(new String[0]);

        //活动范围处理
        if ("0".equals(waitActivationRule.getActivityScopeType())){
            String areaCode = waitActivationRule.getAreaCode();
            if(org.apache.commons.lang.StringUtils.isNotEmpty(areaCode)){
                String[] split = org.apache.commons.lang.StringUtils.split(areaCode, ",");
                Object[] objects = new HashSet(Arrays.asList(split)).toArray();
                waitActivationRule.setAreaCode(org.apache.commons.lang.StringUtils.join(objects, ","));
            }
            //清除热区
            waitActivationRule.setHotAreaKey("");
        }else {
            //清除行政区域
            waitActivationRule.setAreaCode("");
        }

        //频次规则排序
        dealPerItemsSort(waitActivationRule);

        //保存天降红包规则
        commonRule.setRuleType(Constant.COMMON_RULE_TYPE.status_6);
        commonRule.setRuleValue(JSON.toJSONString(waitActivationRule));
        commonRule.setUpdateUser(currentUser.getUserName());
        commonRule.setUpdateTime(DateUtil.getDate(DateUtil.DEFAULT_DATETIME_FORMAT));
        commonRuleService.createOrUpdate(commonRule);

        //返回需要删除缓存的CacheKey集合
        return resultList;
    }

    /**
     * 转换成扫码活动对应
     * @param waitActivationRule
     * @return
     */
    public VcodeActivityCog transformForActivityCog(WaitActivationRule waitActivationRule) {
        VcodeActivityCog activityCog = new VcodeActivityCog();
        activityCog.setVcodeActivityKey(waitActivationRule.getActivityKey());
        activityCog.setVcodeActivityName(waitActivationRule.getActivityName());
        activityCog.setStartDate(waitActivationRule.getStartDate());
        activityCog.setEndDate(waitActivationRule.getEndDate());
        activityCog.setActivityType(Constant.activityType.activity_type13);
        return activityCog;
    }

    /**
     * 转换成扫码活动对应
     * 增加是否删除
     * 增加是否开始
     * 增加创建时间
     * @param waitActivationRule
     * @return
     */
    public VcodeActivityCog transformForActivityCogPadField(WaitActivationRule waitActivationRule) {
        VcodeActivityCog activityCog = new VcodeActivityCog();
        activityCog.setVcodeActivityKey(waitActivationRule.getActivityKey());
        activityCog.setVcodeActivityName(waitActivationRule.getActivityName());
        activityCog.setStartDate(waitActivationRule.getStartDate());
        activityCog.setEndDate(waitActivationRule.getEndDate());
        activityCog.setActivityType(Constant.activityType.activity_type13);
        activityCog.setDeleteFlag("0");
        activityCog.setCreateTime(waitActivationRule.getCreateTime());

        //是否开始 0 待上线；1 已上线；2 已下线
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date getStartDate = dateFormat.parse(activityCog.getStartDate());
            Date getEndDate = dateFormat.parse(activityCog.getEndDate());
            long getStartDateMillis = getStartDate.getTime();
            long getEndDateMillis = getEndDate.getTime();
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis < getStartDateMillis) {
                activityCog.setIsBegin("0");
            }else if (currentTimeMillis <= getEndDateMillis && currentTimeMillis >= getStartDateMillis){
                activityCog.setIsBegin("1");
            }else{
                activityCog.setIsBegin("2");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return activityCog;
    }

    /**
     * 爆点规则-倍数规则-校验
     * @return
     */
    public boolean ruleCheckMultiple() {
        WaitActivationRule byRuleType = commonRuleService.findByRuleType(Constant.COMMON_RULE_TYPE.status_6, WaitActivationRule.class);
        List<String> multipleRuleInfoKeyList = byRuleType.getMultipleRuleInfoKeyList();
        if (multipleRuleInfoKeyList!=null && multipleRuleInfoKeyList.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 频次规则处理排序
     * @param waitActivationRule
     * @return
     */
    private void dealPerItemsSort(WaitActivationRule waitActivationRule) {
        String[] items = waitActivationRule.getFreItems().split(";");
        String[] tempItems = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            int length = item.split(":").length;
            if (item.split(":").length == 3){
                tempItems[i] = UUIDTools.getInstance().getUUID() + ":" + item;
            }else{
                tempItems[i] = item;
            }
        }

        Arrays.sort(tempItems, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1.split(":")[1]) - Integer.parseInt(o2.split(":")[1]);
            }
        });
        waitActivationRule.setFreItems(org.apache.commons.lang.StringUtils.join(tempItems,";"));
    }
}
