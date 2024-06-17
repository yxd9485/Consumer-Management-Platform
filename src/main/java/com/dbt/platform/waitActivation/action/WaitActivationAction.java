package com.dbt.platform.waitActivation.action;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.commonrule.bean.CommonRule;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationMultipleRule;
import com.dbt.platform.waitActivation.bean.WaitActivationRule;
import com.dbt.platform.waitActivation.service.WaitActivationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 天降红包控制层
 */
@Controller
@RequestMapping("/waitActivation")
public class WaitActivationAction extends BaseAction {
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private WaitActivationService waitActivationService;
    @Autowired
    private VcodeActivityRebateRuleCogService rebateRuleCogService;
    @Autowired
    private VcodeActivityHotAreaCogService hotAreaCogService;

    /**
     * 展示天降红包配置页面
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("/showWaitActivationConfig")
    public String showTaskConfig(HttpSession session, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
        List<SysAreaM> areaLst = rebateRuleCogService.queryAreaByActivity();
        List<VcodeActivityHotAreaCog> hotAreaList = hotAreaCogService.findHotAreaListByAreaCode(null);
        WaitActivationRule waitActivationRule = waitActivationService.findWaitActivationRule();
        List<VpsWaitActivationMultipleRule> multipleRuleList = new ArrayList<>();
        if (waitActivationRule == null){
            waitActivationRule = new WaitActivationRule();
            waitActivationRule.setStartTimeSpan("00:00:00");
            waitActivationRule.setEndTimeSpan("23:59:59");
            waitActivationRule.setPostedAmount("0.00");
        }else {
            String postedAmountCacheKey = RedisApiUtil.CacheKey.waitActivation.WAIT_ACTIVATION_POSTED_AMOUNT + Constant.DBTSPLIT + waitActivationRule.getActivityKey();
            String postedAmountCacheNum = RedisApiUtil.getInstance().get(postedAmountCacheKey);
            if (StringUtils.isBlank(postedAmountCacheNum)){
                postedAmountCacheNum = "0.00";
            }else{
                postedAmountCacheNum = String.format("%.2f", Double.parseDouble(postedAmountCacheNum));
            }
            waitActivationRule.setPostedAmount(postedAmountCacheNum);

            //倍数红包规则处理
            List<String> multipleRuleInfoKeyList = waitActivationRule.getMultipleRuleInfoKeyList();
            if (multipleRuleInfoKeyList!=null && multipleRuleInfoKeyList.size()>0){
                for (String multipleRuleInfoKey:multipleRuleInfoKeyList) {
                    multipleRuleList.add(waitActivationService.queryMultipleRuleByID(multipleRuleInfoKey));
                }
            }

            //激活待激活红包后第N次抽奖中N元处理  uuid:1:1.00:1
            String freItems = waitActivationRule.getFreItems();

            if (StringUtils.isNotBlank(freItems)){
                String[] items = freItems.split(";");
                String[] tempItems = new String[items.length];
                for (int i = 0; i < items.length; i++) {
                    String item = items[i];
                    String grantNumCache = RedisApiUtil.CacheKey.waitActivation.WAIT_ACTIVATION_USER_PC_GRANT_NUM
                            + item.split(":")[0] + DateUtil.getDate();
                    String grantNum = RedisApiUtil.getInstance().get(grantNumCache);
                    int totalNum = Integer.parseInt(StringUtils.defaultString(grantNum,"0"));
                    tempItems[i] = item + ":" + totalNum;
                }
                waitActivationRule.setFreItems(org.apache.commons.lang.StringUtils.join(tempItems,";"));
            }
        }
        model.addAttribute("skuList", skuList);
        model.addAttribute("areaLst", areaLst);
        model.addAttribute("hotAreaList", hotAreaList);
        model.addAttribute("waitActivationRule", waitActivationRule);
        model.addAttribute("multipleRuleList", multipleRuleList);
        return "vcode/waitActivation/waitActivationConfig";
    }

    /**
     * 编辑天降红包
     * @param session
     * @param waitActivationRule
     * @param model
     * @return
     */
    @RequestMapping("/editWaitActivationConfig")
    public String editWaitActivationConfig(HttpSession session, Model model,WaitActivationRule waitActivationRule) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);

            //倍数中出规则处理
            List<VpsWaitActivationMultipleRule> multipleRuleList = null;
            String[] drawNums = waitActivationRule.getDrawNum();
            if (drawNums!=null && drawNums.length>0){
                multipleRuleList = new ArrayList<>();
                String[] infoKeys = waitActivationRule.getInfoKey();
                String[] moneys = waitActivationRule.getMoney();
                String[] putInNums = waitActivationRule.getPutInNum();
                for (int i = 0; i < drawNums.length; i++) {
                    VpsWaitActivationMultipleRule multipleRule = new VpsWaitActivationMultipleRule();
                    multipleRule.setInfoKey(infoKeys == null ? null:infoKeys[i]);
                    multipleRule.setDrawNum(Integer.parseInt(drawNums[i]));
                    multipleRule.setMoney(Double.parseDouble(moneys[i]));
                    multipleRule.setPutInNum(Integer.parseInt(putInNums[i]));
                    multipleRuleList.add(multipleRule);
                }
                waitActivationRule.setInfoKey(null);
                waitActivationRule.setDrawNum(null);
                waitActivationRule.setMoney(null);
                waitActivationRule.setPutInNum(null);
            }

            //新增或修改操作处理
            List<String> resultList = waitActivationService.addOrUpdateWaitActivationConfig(waitActivationRule, multipleRuleList, currentUser);

            //清除缓存
            if (!resultList.isEmpty()){
                for (String cacheKey:resultList) {
                    CacheUtilNew.removeByKey(cacheKey);
                }
            }

            model.addAttribute("errMsg", "操作成功");
        } catch (Exception ex) {
            model.addAttribute("errMsg", "操作失败");
            ex.printStackTrace();
        }
        return "forward:showWaitActivationConfig.do";
    }

    /**
     * 倍数规则-爆点规则-校验
     * @param activityKey
     * @param startDate
     * @param endDate
     * @return
     */
    @ResponseBody
    @RequestMapping("/multipleCheckRule")
    public String multipleCheckRule(String activityKey,String startDate,String endDate) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("activityKey",activityKey);
        queryMap.put("startDate",startDate);
        queryMap.put("endDate",endDate);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<VcodeActivityRebateRuleCog> rebateRuleList = rebateRuleCogService.findRebateRuleCogList(queryMap);
            List<VcodeActivityRebateRuleCog> rebateRuleCogList = new ArrayList<>();
            if(rebateRuleList!=null && rebateRuleList.size()>0){
                for (VcodeActivityRebateRuleCog rebateRule:rebateRuleList) {
                    if (StringUtils.isNotBlank(rebateRule.getEruptRuleInfo())){
                        rebateRuleCogList.add(rebateRule);
                    }
                }
            }
            resultMap.put("rebateRuleCogList", rebateRuleCogList);
            resultMap.put("success", true);
        } catch (Exception ex) {
            resultMap.put("success", false);
            ex.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 爆点规则-倍数规则-校验
     * @return
     */
    @ResponseBody
    @RequestMapping("/ruleCheckMultiple")
    public String ruleCheckMultiple() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            boolean checkSuccess = waitActivationService.ruleCheckMultiple();
            resultMap.put("isHaveMultiple", checkSuccess);
            resultMap.put("success", true);
        } catch (Exception ex) {
            resultMap.put("success", false);
            ex.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }

}
