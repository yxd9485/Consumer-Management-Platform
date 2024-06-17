/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:24:17 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.action;

import com.alibaba.fastjson.JSON;
import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.*;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.bean.*;
import com.dbt.platform.activity.service.*;
import com.dbt.platform.doubleprize.bean.VpsVcodeDoublePrizeCog;
import com.dbt.platform.doubleprize.service.VpsVcodeDoublePrizeCogService;
import com.dbt.platform.fission.service.IVpsVcodeActivateRedEnvelopeRuleCogService;
import com.dbt.platform.fission.vo.VpsVcodeActivateRedEnvelopeRuleCogVO;
import com.dbt.platform.mn.service.MnDepartmentService;
import com.dbt.platform.org.service.OrganizationService;
import com.dbt.platform.promotion.bean.VpsVcodeBindPromotionCog;
import com.dbt.platform.promotion.service.VpsVcodeBindPromotionCogService;
import com.dbt.platform.signin.bean.VpsVcodeSigninCog;
import com.dbt.platform.signin.service.VpsVcodeSigninCogService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.turntable.bean.TurntableActivityCogInfo;
import com.dbt.platform.turntable.service.TurntableService;
import com.dbt.platform.waitActivation.bean.VpsWaitActivationPrizeCog;
import com.dbt.platform.waitActivation.bean.WaitActivationRule;
import com.dbt.platform.waitActivation.service.WaitActivationPrizeService;
import com.dbt.platform.waitActivation.service.WaitActivationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * V码活动返利规则Action
 *
 * @version V1.0.0 </br>
 * @auther hanshimeng </br>
 * @createTime 2016年12月5日 </br>
 */
@Controller
@RequestMapping("/vcodeActivityRebateRule")
public class VcodeActivityRebateRuleCogAction extends BaseAction {
    //0:配送员-扫码,1:终端老板-扫码,3:分销商-扫码,0J:配送员-级联激励,2J:经销商-级联激励,3J:分销商-级联激励,4J:网服-级联激励
    private final static String ROLE_ALL = "0:配送员-扫码,1:终端老板-扫码,3:分销商-扫码,0J:配送员-级联激励,2J:经销商-级联激励,3J:分销商-级联激励";
    @Autowired
    private VcodeActivityRebateRuleCogService rebateRuleCogService;
    @Autowired
    private VcodeActivityService vcodeActivityService;
    @Autowired
    private VcodeActivityVpointsCogService vpointsCogService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private VcodeActivityHotAreaCogService hotAreaCogService;
    @Autowired
    private VcodePacksRecordService packsRecordService;
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
    private TurntableService turntableService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private WaitActivationService waitActivationService;
    @Autowired
    private WaitActivationPrizeService waitActivationPrizeService;

    /**
     * V码活动规则列表
     *
     * @param session
     * @param queryParam
     * @param model
     * @return
     */
    @RequestMapping("/showRebateRuleListMain")
    public String showRebateRuleListMain(HttpSession session,
                                         String vcodeActivityKey, String activityType, Model model) {
        try {

            // 页面传参
            model.addAttribute("vcodeActivityKey", vcodeActivityKey);
            model.addAttribute("activityType", activityType);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace();
        }
        return "vcode/rebaterule/showRebateRuleCogListMain";
    }

    /**
     * V码活动规则列表
     *
     * @param session
     * @param queryParam
     * @param model
     * @return
     */
    @RequestMapping("/showRebateRuleList")
    public String showRebateRuleList(HttpSession session, String vcodeActivityKey,
                                     String activityType, String tabsFlag, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            if (StringUtils.isBlank(tabsFlag)) {
                tabsFlag = "1";
            }

            // 获取活动信息
            VcodeActivityCog activityCog = vcodeActivityService.loadActivityByKey(vcodeActivityKey, activityType);
            if (Constant.activityType.activity_type4.equals(activityType)) {
                VpsVcodeSigninCog signinCog = signinCogService.findActivityByKey(vcodeActivityKey);
                model.addAttribute("signinCog", signinCog);

            } else if (Constant.activityType.activity_type5.equals(activityType)) {
                VpsVcodeBindPromotionCog bindPromotionCog = promotionCogService.findActivityByKey(vcodeActivityKey);
                model.addAttribute("bindPromotionCog", bindPromotionCog);

            } else if (Constant.activityType.activity_type6.equals(activityType)) {
                VpsVcodeDoublePrizeCog doublePrizeCog = doublePrizeCogService.findActivityByKey(vcodeActivityKey);
                model.addAttribute("doublePrizeCog", doublePrizeCog);
            } else if (Constant.activityType.activity_type7.equals(activityType)) {
                VpsVcodeActivateRedEnvelopeRuleCogVO activateRedEnvelopeRuleCog = iVpsVcodeActivateRedEnvelopeRuleCogService.getOneByActivityKey(vcodeActivityKey);
                model.addAttribute("activateRedEnvelopeRuleCog", activateRedEnvelopeRuleCog);
            } else if (Constant.activityType.activity_type8.equals(activityType) || Constant.activityType.activity_type9.equals(activityType)) {
                TurntableActivityCogInfo turntableActivityCog = turntableService.queryTurntableActivityInfoByKey(vcodeActivityKey);
                model.addAttribute("turntableActivityCog", turntableActivityCog);
            } else if (Constant.activityType.activity_type13.equals(activityType)){
                WaitActivationRule waitActivationRule = waitActivationService.findWaitActivationRule();
                model.addAttribute("waitActivationRule", waitActivationRule);
            }

//            // 获取此活动的所有活动规则
            List<VcodeActivityRebateRuleArea> rebateRuleAreaList =
                    rebateRuleCogService.queryRebateRuleAreaListByActivityKey(activityCog, tabsFlag);
            model.addAttribute("resultList", rebateRuleAreaList);
//            model.addAttribute("resultList", new ArrayList<>());
            Map<String, String> prizeTypeMap = vpointsCogService.queryAllPrizeType(true, true, true, true, true, true, null);
            Map<String, String> allowanceType = vpointsCogService.queryAllowanceType();
            // 页面传参
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("activityCog", activityCog);
            model.addAttribute("vcodeActivityKey", vcodeActivityKey);
            model.addAttribute("activityType", activityType);
            model.addAttribute("tabsFlag", tabsFlag);
            model.addAttribute("serverName", DbContextHolder.getDBType());
            model.addAttribute("prizeTypeMap", prizeTypeMap);
            model.addAttribute("prizeTypeJSON", JSON.toJSONString(prizeTypeMap,true));
            model.addAttribute("allowanceTypeMap",allowanceType);
            model.addAttribute("allowanceTypeJSON", JSON.toJSONString(allowanceType));
            model.addAttribute("cardTypeMap", vpointsCogService.queryCardType());
            model.addAttribute("projectName", "mengniu".equals(DbContextHolder.getDBType()) ? "1" : "0");
            if ("mengniu".equals(DbContextHolder.getDBType())) {
                model.addAttribute("roleInfoAll", Arrays.asList(ROLE_ALL.split(",")));
            } else {
                model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue("data_constant_config", "role_info").split(",")));
            }

            // 查询青啤组织架构，默认查询大区级别
            model.addAttribute("bigRegionList", organizationService.queryBigRegionList());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/rebaterule/showRebateRuleCogList";
    }

    @RequestMapping("getRebateRuleList")
    @ResponseBody
    public String getRebateRuleList(String vcodeActivityKey, String tabsFlag, String endDate){
        VcodeActivityCog activityCog = new VcodeActivityCog();
        try {
            activityCog.setVcodeActivityKey(vcodeActivityKey);
            activityCog.setTabsFlag(tabsFlag);
            activityCog.setEndDate(endDate);
            List<VcodeActivityRebateRuleArea> rebateRuleAreaList = rebateRuleCogService.queryRebateRuleAreaListByActivityKey(activityCog, tabsFlag);
            return JSON.toJSONString(rebateRuleAreaList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    @RequestMapping("getVpointsCogById")
    @ResponseBody
    public String getRebateRuleList(String rebateRuleKey){
        try {
            List<VcodeActivityVpointsCog> vcodeActivityVpointsCogs = vpointsCogService.queryVpointsCogByrebateRuleKey(rebateRuleKey);
            for (VcodeActivityVpointsCog vcodeActivityVpointsCog : vcodeActivityVpointsCogs) {
                vcodeActivityVpointsCog.setRangePercent(String.format("%.4f", vcodeActivityVpointsCog.getPrizePercent()));
            }
            return JSON.toJSONString(vcodeActivityVpointsCogs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 	添加活动规则
     * @param session
     * @param vcodeActivityKey	活动主键
     * @param areaCode			行政区域编码（区域编码）
     * @param departmentIds		蒙牛组织机构编码（大区,省,经销商）
     * @param organizationIds	青啤组织机构编码（大区,二级办）
     * @param activityType		活动类型
     * @param model
     * @return
     */
    @RequestMapping("/showRebateRuleCogAdd")
    public String showRebateRuleCogImport(HttpSession session,
    		String vcodeActivityKey, String areaCode, String departmentIds, String organizationIds, String activityType, Model model) {
        try {
            // 获取活动
            VcodeActivityCog activityCog = vcodeActivityService.loadActivityByKey(vcodeActivityKey, activityType);

            // cityCode为空时，添加新的区域规则
            String allAreaFlage = "0"; // 全部区域显示标志
            String dayTypeFlage = "0"; // 每天规则显示标志
            String areaName = "";
            List<SysAreaM> areaLst = new ArrayList<>();

            if (StringUtils.isBlank(areaCode) && StringUtils.isBlank(departmentIds) && StringUtils.isBlank(organizationIds)) {
                // 当前活动对应的省编码
                areaLst = rebateRuleCogService.queryAreaByActivity();

                // 否则，添加指定区域下的规则
            } else {
            	if (StringUtils.isNotBlank(organizationIds)) {
                    // 青啤组织机构名称
                    areaName = organizationService.getOrganizationNames(organizationIds);
                } else if (StringUtils.isNotBlank(departmentIds)) {
                    // 蒙牛组织机构名称
                    areaName = rebateRuleCogService.getDepartmentNames(departmentIds);
                } else {
                    // 行政区域名称
                    areaName = rebateRuleCogService.getAreaNameByCode(areaCode);
                }
            }

            // 捆绑促销活动不需要添加保底规则
            if (!Constant.activityType.activity_type5.equals(activityType)
                    || !Constant.activityType.activity_type6.equals(activityType)
                    || (StringUtils.isBlank(departmentIds) && StringUtils.isBlank(organizationIds))) {
                // 如果活动第一次添加，则只能添加默认项全部
                int ruleNum = rebateRuleCogService.getValidRebateRuleCogNumByActivityKey(vcodeActivityKey);
                if (ruleNum == 0) {
                    List<SysAreaM> tempLst = new ArrayList<>();
                    tempLst.add(areaLst.get(0));
                    areaLst = tempLst;

                    // 第一次添加规则时，要配置全部区域的规则
                    allAreaFlage = "1";

                    // 第一次添加规则时，首先要添加第天规则
                    dayTypeFlage = "1";

                } else if (areaLst.size() > 1) {
                    areaLst.remove(0);
                }
            }

            // 获取该区域下的热区
            List<VcodeActivityHotAreaCog> hotAreaList = null;
            if (StringUtils.isNotBlank(areaCode) && !"000000".equals(areaCode)) {
                hotAreaList = hotAreaCogService.findHotAreaListByAreaCode(areaCode);
            }


            // 获取crm精准营销开关
            String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                    DatadicKey.filterSwitchSetting.SWITCH_GROUP);

            // 获取规则模板
            List<VcodeActivityRebateRuleTemplet> rebateRuleTempletLst = rebateRuleTempletService.queryValid();
            List<String> couponNoLst = new ArrayList<>();
            for (VcodeActivityRebateRuleTemplet item : rebateRuleTempletLst) {
                couponNoLst.addAll(rebateRuleTempletService.queryCouponNoByTempletKey(item.getInfoKey()));
            }

            // 获取同类型活动
            List<VcodeActivityCog> activityCogLst =
                    vcodeActivityService.findAllVcodeActivityList(activityType, vcodeActivityKey);

            // 查询蒙牛组织架构，默认查询大区级别
            if ("mengniu".equals(DbContextHolder.getDBType())) {
                model.addAttribute("departmentList", mnDepartmentService.queryListByMap(-1, 4));
            }
            
            // 查询青啤组织架构，默认查询大区级别
            model.addAttribute("bigRegionList", organizationService.queryBigRegionList());

            // 查询待激活红包列表
            List<VpsWaitActivationPrizeCog> waitActivationPrizeList = waitActivationPrizeService.queryAll();
            model.addAttribute("waitActivationPrize", waitActivationPrizeList);

            // 查询当前活动及相应的市级区域
            model.addAttribute("activityCog", activityCog);
            model.addAttribute("activityCogLst", activityCogLst);
            model.addAttribute("hotAreaList", hotAreaList);
            model.addAttribute("projectServerName", DbContextHolder.getDBType());
            model.addAttribute("areaLst", areaLst);
            model.addAttribute("areaCode", areaCode);
            model.addAttribute("departmentIds", departmentIds);
            model.addAttribute("organizationIds", organizationIds);
            model.addAttribute("areaName", areaName);
            model.addAttribute("groupSwitch", DatadicUtil.isSwitchON(groupSwitch) ? "1" : "0");
            model.addAttribute("activityType", activityType);
            model.addAttribute("allAreaFlage", allAreaFlage);
            model.addAttribute("dayTypeFlage", dayTypeFlage);
            model.addAttribute("rebateRuleTempletLst", rebateRuleTempletLst);
            model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(false, true, true, true, true, false, couponNoLst));
            model.addAttribute("allowanceTypeMap", vpointsCogService.queryAllowanceType());
            model.addAttribute("cardTypeMap", vpointsCogService.queryCardType());
            model.addAttribute("projectName", "mengniu".equals(DbContextHolder.getDBType()) ? "1" : "0");
            if ("mengniu".equals(DbContextHolder.getDBType())) {
                model.addAttribute("roleInfoAll", Arrays.asList(ROLE_ALL.split(",")));
            } else {
                model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue("data_constant_config", "role_info").split(",")));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/rebaterule/showRebateRuleCogAdd";
    }

    /**
     * 修改活动规则
     */
    @RequestMapping("/showRebateRuleCogEdit")
    public String showRebateRuleCogEdit(HttpSession session, String rebateRuleKey, String activityType, Model model) {
        try {
            // 查询当前活动规则记录
            VcodeActivityRebateRuleCog rebateRuleCog = rebateRuleCogService.findById(rebateRuleKey);

            // 获取活动
            VcodeActivityCog activityCog = null;
            if (Constant.activityType.activity_type4.equals(activityType)) {
                VpsVcodeSigninCog signinCog = signinCogService.findActivityByKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = signinCogService.transformForActivityCog(signinCog);

            } else if (Constant.activityType.activity_type5.equals(activityType)) {
                VpsVcodeBindPromotionCog signinCog = promotionCogService.findActivityByKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = promotionCogService.transformForActivityCog(signinCog);

            } else if (Constant.activityType.activity_type6.equals(activityType)) {
                VpsVcodeDoublePrizeCog doublePrizeCog = doublePrizeCogService.findActivityByKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = doublePrizeCogService.transformForActivityCog(doublePrizeCog);

            } else if (Constant.activityType.activity_type7.equals(activityType)) {
                VpsVcodeActivateRedEnvelopeRuleCogVO activateRedEnvelopeRuleCogVO = iVpsVcodeActivateRedEnvelopeRuleCogService.getOneByActivityKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = iVpsVcodeActivateRedEnvelopeRuleCogService.transformForActivityCog(activateRedEnvelopeRuleCogVO);

            } else if (Constant.activityType.activity_type8.equals(activityType) || Constant.activityType.activity_type9.equals(activityType)) {
                TurntableActivityCogInfo turntableActivityCog = turntableService.queryTurntableActivityInfoByKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = turntableService.transformForActivityCog(turntableActivityCog);
            } else if (Constant.activityType.activity_type13.equals(activityType)){
                WaitActivationRule waitActivationRule = waitActivationService.findWaitActivationRule();
                activityCog = waitActivationService.transformForActivityCog(waitActivationRule);

            } else {
                activityCog = vcodeActivityService.loadActivityByKey(rebateRuleCog.getVcodeActivityKey());
            }


            List<VcodeActivityHotAreaCog> hotAreaList = null;
            if (StringUtils.isNotBlank(rebateRuleCog.getAreaCode())) {
                hotAreaList = hotAreaCogService.findHotAreaListByAreaCode(rebateRuleCog.getAreaCode());
            }

            // 是否可修改，默认是0
            String isEditFlag = "0";
            if (Constant.rebateRuleType.RULE_TYPE_1.equals(rebateRuleCog.getRuleType())
                    || Constant.rebateRuleType.RULE_TYPE_2.equals(rebateRuleCog.getRuleType())) {
                // 已开始
                if (DateUtil.getDateTime().compareTo(rebateRuleCog.getBeginDate() + " " + rebateRuleCog.getBeginTime()) >= 0
                        && DateUtil.getDateTime().compareTo(rebateRuleCog.getEndDate() + " " + rebateRuleCog.getEndTime()) <= 0) {
                    isEditFlag = "1";

                    // 已过期
                } else if (DateUtil.getDateTime().compareTo(rebateRuleCog.getEndDate() + " " + rebateRuleCog.getEndTime()) > 0) {
                    isEditFlag = "2";
                }
            }
	    	
	    	/*if(Constant.ActivityRebateRule.RULE_TYPE_1.equals(rebateRuleCog.getRuleType())
	    			|| Constant.ActivityRebateRule.RULE_TYPE_2.equals(rebateRuleCog.getRuleType())){
	    		if(rebateRuleCog.getRestrictMoney() > 0 || rebateRuleCog.getRestrictBottle() > 0){
		    		// 已开始
		    		if(DateUtil.getDateTime().compareTo(rebateRuleCog.getBeginDate() + " " + rebateRuleCog.getBeginTime()) >= 0
		    				&& DateUtil.getDateTime().compareTo(rebateRuleCog.getEndDate() + " " + rebateRuleCog.getEndTime()) <= 0){
		    			isEditFlag = "1";
		    		
		    		// 已过期
		    		}else if(DateUtil.getDateTime().compareTo(rebateRuleCog.getEndDate() + " " + rebateRuleCog.getEndTime()) > 0){
		    			isEditFlag = "2";
		    		}
		    		
//			    	List<VcodeActivityRebateRuleCog> ruleList = 
//	    			rebateRuleCogService.queryRebateRuleAreaListByParentId(rebateRuleKey);
		    		if("1".equals(isEditFlag) || "2".equals(isEditFlag)){
		    			if(StringUtils.isBlank(rebateRuleCog.getAppointRebateRuleKey())
								&& (ruleList !=null && !ruleList.isEmpty())){
							// 主规则
						}else if (StringUtils.isNotBlank(rebateRuleCog.getAppointRebateRuleKey())){
							// 子规则
						}
		    		}
				}
	    	}*/


            // 获取规则模板
            List<VcodeActivityRebateRuleTemplet> rebateRuleTempletLst = rebateRuleTempletService.queryValid();
            List<String> couponNoLst = new ArrayList<>();
            for (VcodeActivityRebateRuleTemplet item : rebateRuleTempletLst) {
                couponNoLst.addAll(rebateRuleTempletService.queryCouponNoByTempletKey(item.getInfoKey()));
            }

            // 获取同类型活动
            List<VcodeActivityCog> activityCogLst =
                    vcodeActivityService.findAllVcodeActivityList(activityType, rebateRuleCog.getVcodeActivityKey());

            // 获取父规则的活动主键
            VcodeActivityRebateRuleCog appointRebateRuleCog =
                    rebateRuleCogService.findById(rebateRuleCog.getAppointRebateRuleKey());


            // 获取crm精准营销开关
            List<VcodeActivityCrmGroup> groupResult = null;
            String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                    DatadicKey.filterSwitchSetting.SWITCH_GROUP);
            if (DatadicUtil.isSwitchON(groupSwitch)) {
                if (Constant.activityType.activity_type4.equals(rebateRuleCog.getRuleType())
                        && "全部".equals(rebateRuleCog.getAreaName())) {
                    groupSwitch = "0";
                } else {
                    groupResult = crmServiceService.queryVcodeActivityCrmGroup();
                }
            }

            // 查询待激活红包列表
            List<VpsWaitActivationPrizeCog> waitActivationPrizeList = waitActivationPrizeService.queryAll();
            model.addAttribute("waitActivationPrize", waitActivationPrizeList);
            
            // 兼容老的爆点配置
            // 旧格式：开始日期#结束日期,开始时间#结束时间,分钟:金额:积分:大奖,分钟:金额:积分:大奖;...;...;
            // 新格式：开始日期#结束日期,开始时间#结束时间,uuid:分钟:金额:积分:大奖:中出数量,uuid:分钟:金额:积分:大奖:中出数量;...;...;
            if(StringUtils.isNotBlank(rebateRuleCog.getEruptRuleInfo())
            		&& rebateRuleCog.getEruptRuleInfo().split(":").length > 1) {
            	boolean updateEruptFlag = false;
            	StringBuffer buffer = new StringBuffer();
            	String eruptRuleInfo = rebateRuleCog.getEruptRuleInfo().replaceAll("\r|\n", "");
            	String[] ruleGroup = eruptRuleInfo.split(";");// 规则数组
            	String rule = null;
        		for (int row = 0; row < ruleGroup.length; row ++) {
        			rule = ruleGroup[row];
        			String[] ruleDetail = rule.split(",");
        			String beginDate = ruleDetail[0].split("#")[0];
        			String endDate = ruleDetail[0].split("#")[1];
        			String beginTime = ruleDetail[1].split("#")[0];
        			String endTime = ruleDetail[1].split("#")[1];
        			
        			buffer.append(beginDate).append("#");
        			buffer.append(endDate).append(",");
        			buffer.append(beginTime).append("#");
        			buffer.append(endTime).append(",");
        			
        			for(int col=2; col < ruleDetail.length; col++){
                        // 格式：分钟:金额:积分:大奖
        				 String[] ruleDetailAry = ruleDetail[col].split(":");
        				 
        				 // 新格式第一位是UUID，如果不是36位需要拼接UUID
        				 if(ruleDetailAry[0].length() != 36) {
        					 String uuid = UUIDTools.getInstance().getUUID();
        					 String min = ruleDetailAry[0];
                             String money = ruleDetailAry[1];
                             String vpoint = ruleDetailAry[2];
                             String bigPrize = ruleDetailAry.length > 3 ? ruleDetailAry[3] : "";
                             String limitCount = "";
                             
                             buffer.append(uuid).append(":");
                             buffer.append(min).append(":");
                             buffer.append(money).append(":");
                             buffer.append(vpoint).append(":");
                             buffer.append(bigPrize).append(":");
                             buffer.append(limitCount).append(",");
                             updateEruptFlag = true;
        				 }
        			}
        			buffer.append(";");
        		}
        		if(updateEruptFlag) {
        			rebateRuleCog.setEruptRuleInfo(buffer.toString());
        		}
            }

            model.addAttribute("isEditFlag", isEditFlag);
            model.addAttribute("hotAreaList", hotAreaList);
            model.addAttribute("rebateRuleCog", rebateRuleCog);
            model.addAttribute("projectServerName", DbContextHolder.getDBType());
            model.addAttribute("activityType", activityType);
            model.addAttribute("groupList", groupResult);
            model.addAttribute("groupSwitch", groupSwitch);
            model.addAttribute("activityCog", activityCog);
            model.addAttribute("activityCogLst", activityCogLst);
            model.addAttribute("rebateRuleTempletLst", rebateRuleTempletLst);
            model.addAttribute("appointRebateRuleCog", appointRebateRuleCog);
            model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(false, true, true, true, true, false, couponNoLst));
            model.addAttribute("allowanceTypeMap", vpointsCogService.queryAllowanceType());
            model.addAttribute("cardTypeMap", vpointsCogService.queryCardType());
            model.addAttribute("projectName", "mengniu".equals(DbContextHolder.getDBType()) ? "1" : "0");
            if ("mengniu".equals(DbContextHolder.getDBType())) {
                model.addAttribute("roleInfoAll", Arrays.asList(ROLE_ALL.split(",")));
            } else {
                model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue("data_constant_config", "role_info").split(",")));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/rebaterule/showRebateRuleCogEdit";
    }

    /**
     * 删除活动规则
     */
    @RequestMapping("/doRebateRuleCogDelete")
    public String doRebateRuleCogDelete(HttpSession session,
                                        String rebateRuleKey, String vcodeActivityKey, String activityType, Model model) {
        try {
            // 获取当前规则下的子规则
            List<VcodeActivityRebateRuleCog> rebateRuleList
                    = rebateRuleCogService.queryRebateRuleAreaListByParentId(rebateRuleKey);
            if (null == rebateRuleList || rebateRuleList.isEmpty()) {
                SysUserBasis currentUser = this.getUserBasis(session);
                rebateRuleCogService.deleteRebateRuleCogById(
                        rebateRuleKey, vcodeActivityKey, currentUser.getUserKey());
                model.addAttribute("flag", "is_delete");
            } else {
                StringBuilder errorMsg = new StringBuilder("删除失败，该规则下包含以下规则:</br>");
                for (VcodeActivityRebateRuleCog rebateRuleCog : rebateRuleList) {
                    if (Constant.rebateRuleType.RULE_TYPE_1.equals(rebateRuleCog.getRuleType())) {
                        errorMsg.append("节假日规则：")
                                .append(rebateRuleCog.getBeginDate()).append(" ").append(rebateRuleCog.getBeginTime())
                                .append(" - ").append(rebateRuleCog.getEndDate()).append(" ").append(rebateRuleCog.getEndTime()).append("</br>");
                    } else if (Constant.rebateRuleType.RULE_TYPE_2.equals(rebateRuleCog.getRuleType())) {
                        errorMsg.append("时间段规则：")
                                .append(rebateRuleCog.getBeginDate()).append(" ").append(rebateRuleCog.getBeginTime())
                                .append(" - ").append(rebateRuleCog.getEndDate()).append(" ").append(rebateRuleCog.getEndTime()).append("</br>");
                    } else if (Constant.rebateRuleType.RULE_TYPE_3.equals(rebateRuleCog.getRuleType())) {
                        errorMsg.append("周几规则：")
                                .append("周" + rebateRuleCog.getBeginDate()).append(" ").append(rebateRuleCog.getBeginTime())
                                .append(" - ").append("周" + rebateRuleCog.getEndDate()).append(" ").append(rebateRuleCog.getEndTime()).append("</br>");
                    } else if (Constant.rebateRuleType.RULE_TYPE_4.equals(rebateRuleCog.getRuleType())) {
                        errorMsg.append("每天规则：").append(rebateRuleCog.getBeginTime()).append(" - ").append(rebateRuleCog.getEndTime()).append("</br>");
                    }
                }
                model.addAttribute("flag", "xx_delete_info");
                model.addAttribute("errorMsg", errorMsg);
            }
        } catch (Exception ex) {
            model.addAttribute("flag", "xx_delete");
            ex.printStackTrace();
        }
        return "forward:showRebateRuleList.do";
    }

    /**
     * 删除活动区域规则
     */
    @RequestMapping("/doRebateRuleDelete")
    public String doRebateRuleDelete(HttpSession session,
                                     String areaCode, String departmentIds, String vcodeActivityKey, String activityType, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            rebateRuleCogService.doRebateRuleDelete(vcodeActivityKey,
                    areaCode, departmentIds, currentUser.getUserKey());
            model.addAttribute("flag", "is_delete");
        } catch (Exception ex) {
            model.addAttribute("flag", "xx_delete");
            ex.printStackTrace();
        }
        return "forward:showRebateRuleList.do";
    }

    /**
     * 查询爆点红包
     *
     * @param </br>
     * @return String </br>
     */
    @RequestMapping("/queryEruptRedpacketList")
    public String queryEruptRedpacketList(HttpSession session,
                                          String queryParam, String pageParam, String rebateRuleKey, Model model) throws Exception {
        int countResult = 0;
        List<VcodePacksRecord> packsRecordList = null;
        SysUserBasis currentUser = this.getUserBasis(session);
        PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
        VcodeActivityRebateRuleCog rebateRuleCog = null;
        if (StringUtils.isNotBlank(rebateRuleKey)) {
            rebateRuleCog = rebateRuleCogService.findById(rebateRuleKey);
            List<String> suffixList = null;
            String dateStart = rebateRuleCog.getBeginDate();
            String dateEnd = rebateRuleCog.getEndDate();
            if (StringUtils.isNotBlank(dateStart) && StringUtils.isNotBlank(dateEnd)) {
                // 广西、浙江
                if ("guangxi".equals(DbContextHolder.getDBType()) || "zhejiang".equals(DbContextHolder.getDBType())) {
                    suffixList = PackRecordRouterUtilForGX.getRecordRouter(dateStart, dateEnd);
                } else {
                    suffixList = PackRecordRouterUtil.getTabSuffixByDate(24, dateStart, dateEnd);
                }

            }
            countResult = packsRecordService.queryEruptRedpacketCount(rebateRuleKey, suffixList);
            packsRecordList = packsRecordService.queryEruptRedpacketList(rebateRuleKey, pageInfo, suffixList);
        }

        model.addAttribute("packsRecordList", packsRecordList);
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("showCount", countResult);
        model.addAttribute("startIndex", pageInfo.getStartCount());
        model.addAttribute("countPerPage", pageInfo.getPagePerCount());
        model.addAttribute("currentPage", pageInfo.getCurrentPage());
        model.addAttribute("rebateRuleKey", rebateRuleKey);
        model.addAttribute("vcodeActivityKey", rebateRuleCog.getVcodeActivityKey());
        return "vcode/rebaterule/showEruptRedpacketList";
    }

    /**
     * 跳转克隆规则页面
     */
    @RequestMapping("/showRebateRuleCogClone")
    public String showRebateRuleCogClone(HttpSession session, String rebateRuleKey, String activityType, Model model) {
        try {
            // 查询当前活动规则记录
            VcodeActivityRebateRuleCog rebateRuleCog = rebateRuleCogService.findById(rebateRuleKey);


            // 获取活动
            VcodeActivityCog activityCog = null;
            if (Constant.activityType.activity_type4.equals(activityType)) {
                VpsVcodeSigninCog signinCog = signinCogService.findActivityByKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = signinCogService.transformForActivityCog(signinCog);

            } else if (Constant.activityType.activity_type5.equals(activityType)) {
                VpsVcodeBindPromotionCog signinCog = promotionCogService.findActivityByKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = promotionCogService.transformForActivityCog(signinCog);

            } else if (Constant.activityType.activity_type6.equals(activityType)) {
                VpsVcodeDoublePrizeCog doublePrizeCog = doublePrizeCogService.findActivityByKey(rebateRuleCog.getVcodeActivityKey());
                activityCog = doublePrizeCogService.transformForActivityCog(doublePrizeCog);

            } else {
                activityCog = vcodeActivityService.loadActivityByKey(rebateRuleCog.getVcodeActivityKey());
            }

            List<VcodeActivityRebateRuleCog> rebateRuleList = null;
            if (StringUtils.isNotBlank(rebateRuleCog.getVcodeActivityKey())) {
                // 获取包含限制金额或瓶数的规则
                rebateRuleList = rebateRuleCogService.queryRebateRuleCogListByActivityKey(
                        rebateRuleCog.getVcodeActivityKey(), rebateRuleCog.getAreaCode(), rebateRuleCog.getIsValid(), true);
            }

            // 获取当前活动及所有未结束的活动
            List<VcodeActivityCog> activityCogList = vcodeActivityService
                    .findAllVcodeActivityList(activityType, activityCog.getVcodeActivityKey());

            // 获取crm精准营销开关
            List<VcodeActivityCrmGroup> groupResult = null;
            String groupSwitch = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
                    DatadicKey.filterSwitchSetting.SWITCH_GROUP);
            if (DatadicUtil.isSwitchON(groupSwitch)) {
                if (Constant.activityType.activity_type4.equals(rebateRuleCog.getRuleType())
                        && "全部".equals(rebateRuleCog.getAreaName())) {
                    groupSwitch = "0";
                } else {
                    groupResult = crmServiceService.queryVcodeActivityCrmGroup();
                }
            }


            // 查询蒙牛组织架构，默认查询大区级别
            if ("mengniu".equals(DbContextHolder.getDBType())) {
                model.addAttribute("departmentList", mnDepartmentService.queryListByMap(-1, 4));
            }
            
            // 查询青啤组织架构，默认查询大区级别
            model.addAttribute("bigRegionList", organizationService.queryBigRegionList());
            
            if ("mengniu".equals(DbContextHolder.getDBType())) {
                model.addAttribute("roleInfoAll", Arrays.asList(ROLE_ALL.split(",")));
            } else {
                model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue("data_constant_config", "role_info").split(",")));
            }
            
            model.addAttribute("groupList", groupResult);
            model.addAttribute("groupSwitch", groupSwitch);
            model.addAttribute("rebateRuleCog", rebateRuleCog);
            model.addAttribute("rebateRuleList", rebateRuleList);
            model.addAttribute("activityType", activityType);
            model.addAttribute("activityCog", activityCog);
            model.addAttribute("activityCogList", activityCogList);
            model.addAttribute("projectServerName", DbContextHolder.getDBType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/rebaterule/showRebateRuleCogClone";
    }


    /**
     * 克隆规则
     */
    @RequestMapping("/cloneRebateRuleCog")
    public String cloneRebateRuleCog(HttpSession session, String rebateRuleKey, String ruleType, String filterVcodeActivityKey,
    		String filterAreaCode, String filterDepartmentIds, String filterOrganizationIds, String filterDateAry, String filterTimeAry, 
    		String isCloneErupt, String groupId, String groupName, Model model) {

        try {
            // 克隆规则
            SysUserBasis currentUser = getUserBasis(session);
            rebateRuleCogService.cloneRebateRuleCog(rebateRuleKey, ruleType, filterVcodeActivityKey, filterAreaCode, filterDepartmentIds, 
            		filterOrganizationIds, filterDateAry, filterTimeAry, currentUser.getUserKey(), isCloneErupt, groupId, groupName);
            model.addAttribute("flag", "clone_success");

        } catch (BusinessException ex) {
            model.addAttribute("errMsg", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("flag", "clone_fail");
            ex.printStackTrace();
        }
        return "forward:showRebateRuleList.do";
    }

    /**
     * 导入并入库
     */
    @RequestMapping("/importRebateRuleCogMoneyConfig")
    public String importActivityMoneyConfig(HttpSession session, @RequestParam("filePath") MultipartFile clientFile,
	    VcodeActivityRebateRuleCog rebateRuleCog, String filterAreaCode, String filterDepartmentIds, String filterOrganizationIds, 
	    VcodeActivityRebateRuleTemplet rebateRuleTemplet, String filterDateAry, String filterTimeAry, String isClear, String activityType, Model model) {
        // 列表界面回显区域名称
        String areaCode = StringUtils.isNotBlank(rebateRuleCog.getRebateRuleKey()) ? rebateRuleCog.getAreaCode() : filterAreaCode;
        if (StringUtils.isNotBlank(areaCode)) {
            String areaName = "";
            if (areaCode.indexOf(",") < 0) {
                // 区域名称
                if (StringUtils.isEmpty(areaCode) || "000000".equals(areaCode)) {
                    areaName = "全部";
                } else if ("000001".equals(areaCode)) {
                    areaName = "省外";
                } else {
                    SysAreaM area = sysAreaService.findById(areaCode);
                    areaName = area.getAreaName();
                }
            } else {
                // 组织机构名称
            	if("mengniu".equals(DbContextHolder.getDBType())) {
            		areaName = organizationService.getOrganizationNames(filterDepartmentIds);
            	}else {
            		areaName = rebateRuleCogService.getDepartmentNames(filterOrganizationIds);
            	}
            }

            model.addAttribute("areaName", areaName);
        }

        try {
            // 初始化奖项配置项
            List<VcodeActivityMoneyImport> excelList = rebateRuleCogService
                    .initPrizeItem(rebateRuleCog, clientFile, rebateRuleTemplet, activityType);

            // 入库前先拿出老的奖项主键集合
            Set<String> oldKeys = null;
            String redisKey = RedisApiUtil.CacheKey.ActivityVpointsCog
                    .VPS_VCODE_ACTIVITY_VPOINTS_COG_NUM + ":" + rebateRuleCog.getRebateRuleKey();
            if (excelList != null && !excelList.isEmpty()) {
                oldKeys = RedisApiUtil.getInstance().hkeys(redisKey);
            }

            // 写入库
            SysUserBasis currentUser = getUserBasis(session);
            rebateRuleCogService.writeBatchVpointsCog(excelList, rebateRuleCog,
                    filterAreaCode, filterDepartmentIds, filterOrganizationIds, filterDateAry, filterTimeAry, currentUser.getUserKey(), isClear, activityType);

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
            model.addAttribute("errMsg", ex.getMessage());

        } catch (Exception ex) {
            model.addAttribute("errMsg", "保存失败");
            log.error(ex.getMessage(), ex);
        }
        return "forward:showRebateRuleList.do";
    }

    /**
     * 获取返利规则模板
     */
    @ResponseBody
    @RequestMapping("/findRebateRuleTemplet")
    public String findRebateRuleTemplet(HttpSession session, String templetKey, String rebateRuleKey) {
        VcodeActivityRebateRuleTemplet ruleTemplet =
                rebateRuleCogService.findRebateRuleTemplet(templetKey, rebateRuleKey);
        Map<String, Object> map = new HashMap<>();
        map.put("ruleTemplet", ruleTemplet);
        return JSON.toJSONString(map);
    }


    /**
     * 校验奖项配置项
     */
    @ResponseBody
    @RequestMapping("/checkActivityVpointsForImport")
    public String checkActivityVpointsForImport(HttpSession session,
                                                @RequestParam("filePath") MultipartFile clientFile, String activityType) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = rebateRuleCogService.checkActivityVpointsForImport(clientFile, activityType);
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultMap.put("errMsg", "奖项配置项解析异常！");
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 获取指定活动下的可继承规则
     *
     * @param vcodeActivityKey
     * @return
     */
    @ResponseBody
    @RequestMapping("queryAppointRebateRule")
    public String queryAppointRebateRule(String vcodeActivityKey, String rebateRuleKey) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<VcodeActivityRebateRuleCog> rebateRuleCogLst
                    = rebateRuleCogService.queryAppointRebateRule(vcodeActivityKey, rebateRuleKey);
            resultMap.put("rebateRuleCogLst", rebateRuleCogLst);
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultMap.put("errMsg", "获取指定活动下的可继承规则异常！");
        }
        return JSON.toJSONString(resultMap);
    }


    /**
     * 获取群组信息
     */
    @ResponseBody
    @RequestMapping("/findGroupInfo")
    public String findGroupInfo(HttpSession session, String groupId, Model model) {
        Map<String, Object> resultMap = new HashMap<>();
        VcodeActivityCrmGroup info = null;
        try {
            info = crmServiceService.findGroupInfo(groupId);
            resultMap.put("groupInfo", info);
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JSON.toJSONString(resultMap);
    }


    /**
     * 根据地区群组下拉框
     *
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping("/findGroupList")
    @ResponseBody
    public String findGroupList(HttpSession session, Model model) {
        try {
            return JSON.toJSONString(crmServiceService.queryVcodeActivityCrmGroup());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 校验奖项配置项三重防护机制
     */
    @ResponseBody
    @RequestMapping("/checkTripleProtection")
    public String checktripleProtection(HttpSession session,
                                        @RequestParam(value = "filePath", required = false) MultipartFile clientFile,
                                        VcodeActivityRebateRuleCog rebateRuleCog, VcodeActivityRebateRuleTemplet rebateRuleTemplet) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            rebateRuleTemplet.setRebateRuleTemplet(rebateRuleTemplet);
            // 初始化奖项配置项
            List<VcodeActivityMoneyImport> excelList = rebateRuleCogService
                    .initPrizeItem(rebateRuleCog, clientFile, rebateRuleTemplet);
            rebateRuleCogService.checkTripleProtection(excelList, rebateRuleCog);
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultMap.put("errMsg", "奖项配置项解析异常");
        }
        return JSON.toJSONString(resultMap);
    }
    
    public static void main(String[] args) {
    	String str = "7d549ff3-84d5-4f4b-a26d-09efc78e1234:2:0.02:0::2";
    	System.out.println(str.split(":").length);
    	System.out.println("----" + str.split(":")[4]);
    	System.out.println("----" + str.split(":")[5]);
    	
    	System.out.println(DateUtil.addDays(-1));
	}

}
