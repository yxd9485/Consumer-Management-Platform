package com.dbt.platform.fission.action;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.fission.bean.VpsVcodeActivateRedEnvelopeRuleCogEntity;
import com.dbt.platform.fission.bean.VpsVcodeActivateSharePrizeCog;
import com.dbt.platform.fission.service.FissionService;
import com.dbt.platform.fission.service.IVpsVcodeActivatePrizeRecordService;
import com.dbt.platform.fission.service.IVpsVcodeActivateRedEnvelopeRuleCogService;
import com.dbt.platform.fission.service.impl.VpsVcodeActivateSharePrizeService;
import com.dbt.platform.fission.vo.VpsVcodeActivateRedEnvelopeRuleCogVO;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 激活红包规则（裂变红包） 前端控制器
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-11
 */
@Controller
@RequestMapping("/activateRedEnvelopeRuleCog")
public class VpsVcodeActivateRedEnvelopeRuleCogAction extends BaseAction {
    @Autowired
    private IVpsVcodeActivateRedEnvelopeRuleCogService iVpsVcodeActivateRedEnvelopeRuleCogService;
    @Autowired
    private IVpsVcodeActivatePrizeRecordService iVpsVcodeActivatePrizeRecordService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private VcodeActivityService vcodeActivityService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private VpsVcodeActivateSharePrizeService activateSharePrizeService;

    /**
     * 活动列表
     */
    @RequestMapping(value = "/showFissionActivityList")
    public String showDoublePrizeList(HttpSession session, String tabsFlag,
                                      String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsVcodeActivateRedEnvelopeRuleCogVO queryBean = new VpsVcodeActivateRedEnvelopeRuleCogVO(queryParam);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            // 1、已上线 0、未上线 2、已下线 3、全部 （默认1）
            if (StringUtils.isBlank(tabsFlag)) {
                tabsFlag = "1";
            }
            queryBean.setTabsFlag(tabsFlag);
            IPage<VpsVcodeActivateRedEnvelopeRuleCogVO>  ruleCogEntityIPage = iVpsVcodeActivateRedEnvelopeRuleCogService.selectPage(pageInfo,queryBean,tabsFlag,currentUser.getCompanyKey());
            List<VpsVcodeActivateRedEnvelopeRuleCogVO> records = ruleCogEntityIPage.getRecords();
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", records);
            model.addAttribute("showCount", ruleCogEntityIPage.getTotal());
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("nowTime", new LocalDate());
            model.addAttribute("tabsFlag", tabsFlag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/fission/showFissionActivityList";
    }
    /**
     * 跳转新增页面
     */
    @RequestMapping(value = "/showFissionActivityAdd")
    public String showFissionActivityAdd(HttpSession session, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        String companyKey = currentUser.getCompanyKey();
        List<VcodeActivityCog> activityCogList = vcodeActivityService
                .findAllVcodeActivityList(Constant.activityType.activity_type0, "");
        List<SkuInfo> skuInfos = skuInfoService.loadSkuListByCompany(companyKey);
        // 获取项目地区配置
        getAreaJson(model, "");
        model.addAttribute("companyKey", companyKey);
        model.addAttribute("shareActivityKeyList", activityCogList);
        model.addAttribute("skuList", skuInfos);
        return "vcode/fission/showFissionActivityAdd";
    }

    /**
     * 跳转修改页面
     */
    @RequestMapping(value = "/showFissionActivityEdit")
    public String showFissionActivityEdit(HttpSession session,String activityKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String companyKey = currentUser.getCompanyKey();
            VpsVcodeActivateRedEnvelopeRuleCogVO vo = iVpsVcodeActivateRedEnvelopeRuleCogService.getOneByActivityKey(activityKey);
            List<VcodeActivityCog> activityCogList = vcodeActivityService
                    .findAllVcodeActivityList(Constant.activityType.activity_type0, "");
            List<SkuInfo> skuInfos = skuInfoService.loadSkuListByCompany(companyKey);
            // 如果是待激活红包版本的海报活动则查询分享者奖励配置
            if (vo.getShareType() == 3) {
                List<VpsVcodeActivateSharePrizeCog> newUserPrizeCogList = activateSharePrizeService.queryPrizeCogByActivityKeyAndUserType(vo.getActivityKey(),"0");
                model.addAttribute("newUserPrizeCogList", newUserPrizeCogList);
                List<VpsVcodeActivateSharePrizeCog> oldUserPrizeCogList = activateSharePrizeService.queryPrizeCogByActivityKeyAndUserType(vo.getActivityKey(),"1");
                model.addAttribute("oldUserPrizeCogList", oldUserPrizeCogList);
            }
            // 获取项目地区配置
            getAreaJson(model, vo.getShareArea());
            model.addAttribute("companyKey", companyKey);
//            model.addAttribute("limitConsumeAmount", limitConsumeAmount);
            model.addAttribute("shareActivityKeyList", activityCogList);
            model.addAttribute("companyKey", companyKey);
            model.addAttribute("ActivateRedEnvelopeRuleCog", vo);
            model.addAttribute("isView", "0");
            model.addAttribute("skuList", skuInfos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "vcode/fission/showFissionActivityEdit";
    }
    /**
     * 跳转查看页面
     */
    @RequestMapping(value = "/showFissionActivityView")
    public String showFissionActivityView(HttpSession session,String activityKey, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        String companyKey = currentUser.getCompanyKey();
        VpsVcodeActivateRedEnvelopeRuleCogVO vo = iVpsVcodeActivateRedEnvelopeRuleCogService.getOneByActivityKey(activityKey);
        List<VcodeActivityCog> activityCogList = vcodeActivityService
                .findAllVcodeActivityList(Constant.activityType.activity_type0, "");
        // 获取项目地区配置
        getAreaJson(model, vo.getShareArea());
        model.addAttribute("companyKey", companyKey);
        model.addAttribute("shareActivityKeyList", activityCogList);
        model.addAttribute("ActivateRedEnvelopeRuleCog", vo);
        model.addAttribute("isView", "1");
        model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(companyKey));
        return "vcode/fission/showFissionActivityView";
    }


    /**
     * 增加
     */
    @RequestMapping(value = "/saveActivityCog")
    public String saveActivityCog(HttpSession session,VpsVcodeActivateRedEnvelopeRuleCogVO param, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            String errMsg = iVpsVcodeActivateRedEnvelopeRuleCogService.saveActivityCog(currentUser,param);
            model.addAttribute("errMsg", errMsg);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errMsg", "添加失败");
        }
        return "forward:/activateRedEnvelopeRuleCog/showFissionActivityList.do";
    }


    /**
     * 增加
     */
    @RequestMapping(value = "/checkSave")
    @ResponseBody
    public String checkSave(HttpSession session,String skuList,String activateKeyList,String ruleKey,String startDate,String endDate,String shareType, Model model) {
        String resultStr = "0";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            List<String> skuKeys = iVpsVcodeActivateRedEnvelopeRuleCogService.getTabs1SkuKey("1",ruleKey,startDate,endDate,shareType);
            List<String> activates = iVpsVcodeActivateRedEnvelopeRuleCogService.getTabs1ActivateList("1",ruleKey,startDate,endDate,shareType);
            List<String> skuKeyList = new ArrayList<>();
            List<String> activateList = new ArrayList<>();
            activates.forEach(sk->{
                String[] split = sk.split(",");
                for (int i = 0; i < split.length; i++) {
                    activateList.add(split[i]);
                }
            });
            String[] actityKeyArray = activateKeyList.split(",");
            for (int i = 0; i < actityKeyArray.length; i++) {
                if(activateList.contains(actityKeyArray[i])){
                    resultStr = "1";
                    break;
                }
            }
            skuKeys.forEach(sk->{
                String[] sks = sk.split(",");
                for (int i = 0; i < sks.length; i++) {
                    skuKeyList.add(sks[i]);
                }
            });
            String[] skuArray = skuList.split(",");
            for (int i = 0; i < skuArray.length; i++) {
                if(skuKeyList.contains(skuArray[i])){
                    resultStr ="2";
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultStr= "3";
        }
        return resultStr;
    }
    /**
     * 修改
     */
    @RequestMapping(value = "/updateActivityCog")
    public String updateActivityCog(HttpSession session,VpsVcodeActivateRedEnvelopeRuleCogVO param, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        try {
            String errMsg = iVpsVcodeActivateRedEnvelopeRuleCogService.updateActivityCog(currentUser,param);
            CacheUtilNew.removeGroupByKey("vpsVcodeActivateRedEnvelopeRuleCog_vcodeActivityKey");
            CacheUtilNew.removeGroupByKey("vpsVcodeActivateRedEnvelopeRuleCog_ruleKey");
            String cacheStr = CacheKey.cacheKey.ShareRecord.KEY_SHARE_RECORD + Constant.DBTSPLIT + param.getActivityKey()+DateUtil.getDate();
            CacheUtilNew.removeGroupByKey(cacheStr);
            String activityCachestr =  CacheKey.cacheKey.ShareRecord.SHARE_ACTIVATE_ACTIVITY_KEY + Constant.DBTSPLIT + DbContextHolder.getDBType();
            CacheUtilNew.removeByKey(activityCachestr);
            // 清除奖品缓存
            String prizeCacheStr = CacheKey.cacheKey.ShareRecord.SHARE_ACTIVATE_SHARE_USER_PRIZE_KEY;
            CacheUtilNew.removeGroupByKey(prizeCacheStr);
            model.addAttribute("errMsg", errMsg);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errMsg", "修改失败");
        }

        return "forward:/activateRedEnvelopeRuleCog/showFissionActivityList.do";
    }
    /**
     * 修改状态
     */
    @RequestMapping(value = "/updateState")
    @ResponseBody
    public String updateState(HttpSession session,VpsVcodeActivateRedEnvelopeRuleCogVO param, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        VpsVcodeActivateRedEnvelopeRuleCogEntity entity = new VpsVcodeActivateRedEnvelopeRuleCogEntity();
        BeanUtils.copyProperties(param, entity);
        try {
            entity.setUpdateTime(new Date());
            entity.setUpdateUser(currentUser.getUserKey());
            boolean i = iVpsVcodeActivateRedEnvelopeRuleCogService.updateById(entity);
            CacheUtilNew.removeGroupByKey("vpsVcodeActivateRedEnvelopeRuleCog_vcodeActivityKey");
            CacheUtilNew.removeGroupByKey("vpsVcodeActivateRedEnvelopeRuleCog_ruleKey");
            String cacheStr = CacheKey.cacheKey.ShareRecord.KEY_SHARE_RECORD + Constant.DBTSPLIT + param.getActivityKey()+DateUtil.getDate();
            CacheUtilNew.removeGroupByKey(cacheStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Constant.ResultCode.SUCCESS;
    }
    /**
     * 检验名字是否重复
     */
    @RequestMapping(value = "/checkName")
    @ResponseBody
    public String checkName(HttpSession session,String checkName,String activityKey, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        return iVpsVcodeActivateRedEnvelopeRuleCogService.checkName(checkName,activityKey);
    }
    /**
     * 检验活动是否存在规则
     */
    @RequestMapping(value = "/checkRule")
    @ResponseBody
    public String checkRule(HttpSession session,String ruleKey, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
        return iVpsVcodeActivateRedEnvelopeRuleCogService.checkRule(ruleKey);
    }
    private void getAreaJson(Model model,String shareArea){
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
            areaLst = sysAreaService.queryAllByAreaCode(areaCodeList);

            if(CollectionUtils.isNotEmpty(areaLst)){
                // 山东省-济南市-平阴县;山东省-青岛市；河南省；
                String shipmentsArea = StringUtils.defaultIfBlank(shareArea,"");
                String[] areaGroup = shipmentsArea.split(";");
                JSONObject json = null;
                List<JSONObject> areaList = new ArrayList<>();
                for (SysAreaM item : areaLst) {
                    json = new JSONObject();
                    json.put("id", item.getAreaCode());
                    json.put("pId", item.getParentCode());
                    json.put("name", item.getAreaName());
                    if(areaGroup.length > 0 && shipmentsArea.contains(item.getAreaName())){
                        String dbArea = sysAreaService.getAreaNameByCode(item.getAreaCode()).replaceAll(" ", "");
                        for (String areaItem : areaGroup) {
                            if(areaItem.startsWith(dbArea)){
                                json.put("checked", true);
                            }
                        }
                    }
                    areaList.add(json);
                }
                model.addAttribute("areaJson", JSON.toJSONString(areaList));
            }
        }
    }
}

