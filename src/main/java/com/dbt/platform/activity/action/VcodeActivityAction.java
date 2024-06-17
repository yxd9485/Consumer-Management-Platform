package com.dbt.platform.activity.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.*;
import com.dbt.platform.activity.service.*;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.ladder.bean.LadderRuleCog;
import com.dbt.platform.ladder.service.LadderRuleService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author RoyFu
 * @createTime 2016年1月19日 下午5:44:05
 * @description V码活动Action
 */

@Controller
@RequestMapping("/vcodeActivity")
public class VcodeActivityAction extends BaseAction {

	@Autowired
	private VcodeActivityService vcodeActivityService;
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private VcodeActivityDoubtTempletService doubtTempletService;
	@Autowired
	private VcodeActivityMorescanCogService moreScanCogService;
	@Autowired
	private VcodeActivityPerhundredCogService perHundredCogService;
	@Autowired
	private LadderRuleService ladderRuleService;
	@Autowired
	private VcodeQrcodeBatchInfoService batchInfoService;

	//比较器，根据sku名字返回排序
	public static final Comparator<SkuInfo> COMPARATOR_BY_SKUNAME = new Comparator<SkuInfo>() {
		@Override
		public int compare(SkuInfo s1, SkuInfo s2) {
			return s1.getSkuName().compareTo(s2.getSkuName());
		}
	};

	/**
	 * V码活动列表
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param activityType 活动类型：1一罐一码，2一瓶一码和一万一批次
	 * @param menuType 菜单类型：activity活动管理，rule规则管理，qrcode码源管理
	 * @param model
	 * @return
	 */
	@RequestMapping("/showVcodeActivityList")
	public String showVcodeActivityList(HttpSession session, 
	        String queryParam, String pageParam, String activityType, String tabsFlag, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			VcodeActivityCog queryBean = new VcodeActivityCog(queryParam);
			queryBean.setCompanyKey(currentUser.getCompanyKey());
			queryBean.setCurrDate(DateUtil.getDate());
			if (StringUtils.isBlank(tabsFlag)) {
			    tabsFlag = "2";
			}
			queryBean.setTabsFlag(tabsFlag);
			
			List<VcodeActivityCog> resultList = 
			        vcodeActivityService.findVcodeActivityList(queryBean, pageInfo);
			int countResult = vcodeActivityService.countVcodeActivityList(queryBean);
			
            // SKU
            List<SkuInfo> skuLst = skuInfoService
                        .loadSkuListByCompany(currentUser.getCompanyKey());
            // 活动版本号
            List<SysDataDic> versionLst = vcodeActivityService.queryAllActivityVersion();
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("skuLst", skuLst);
			model.addAttribute("versionLst", versionLst);
			model.addAttribute("activityType", Constant.activityType.activity_type0);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("tabsFlag", tabsFlag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/activity/showVcodeActivityList";
	}

	/**
	 * V码活动 添加页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showVcodeActivityAdd")
	public String showVcodeActivityAdd(HttpSession session, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			
			// SKU
			List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(companyKey);
			// 使用Collections.sort()进行排序
			Collections.sort(skuList, COMPARATOR_BY_SKUNAME);

			// 活动版本号
			List<SysDataDic> versionLst = vcodeActivityService.queryAllActivityVersion();
			// 风控模板
			List<VcodeActivityDoubtTemplet> doubtTempletLst = 
			                doubtTempletService.queryForAll(Constant.statusFlag.STATUS_1);
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("skuList", skuList);
			model.addAttribute("versionLst", versionLst);
            model.addAttribute("doubtTempletLst", doubtTempletLst);
			model.addAttribute("projectName", "mengniu".equals(DbContextHolder.getDBType())?"1":"0");
			model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
					"data_constant_config", "role_info").split(",")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/activity/showVcodeActivityAdd";
	}

	/**
	 * V码活动 编辑页面
	 * 
	 * @param session
	 * @param vcodeActivityKey
	 * @param requestType
	 * @param model
	 * @return
	 */
	@RequestMapping("/showVcodeActivityEdit")
	public String showVcodeActivityEdit(HttpSession session, String infoKey, String childTab, Model model) {
		try {

			SysUserBasis currentUser = this.getUserBasis(session);
			model.addAttribute("currentUser", currentUser);

			// 查询当前活动
			VcodeActivityCog currentActivity = vcodeActivityService.loadActivityByKey(infoKey);
			List<VcodeActivityCogExtends> activityCogExtendsList = vcodeActivityService.loadActivityExtendsByKey(infoKey);
			// SKU
			List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentActivity.getCompanyKey());
			// 使用Collections.sort()进行排序
			Collections.sort(skuList, COMPARATOR_BY_SKUNAME);
            // 活动版本号
            List<SysDataDic> versionLst = vcodeActivityService.queryAllActivityVersion();
            // 风控模板
            List<VcodeActivityDoubtTemplet> doubtTempletLst = 
                            doubtTempletService.queryForAll(Constant.statusFlag.STATUS_1);
            // 逢百规则
            VcodeActivityPerhundredCog perHundredCog = perHundredCogService.findValidByActivityKey(infoKey);
            // 一码多扫
            VcodeActivityMorescanCog moreScanCog = moreScanCogService.findValidByActivityKey(infoKey);
            // 阶梯规则
            LadderRuleCog ladderRuleCog = ladderRuleService.findByActivityKey(infoKey);
            // 选择的SKU
			List<String> selectSkuList = vcodeActivityService.loadSkuKeyListByActivityKey(infoKey);
			model.addAttribute("selectSkuList",JSONArray.toJSONString(selectSkuList) );
			model.addAttribute("activityCog", currentActivity);
            model.addAttribute("activityCogExtendsList", JSONArray.toJSONString(activityCogExtendsList));
            model.addAttribute("skuList", skuList);
            model.addAttribute("versionLst", versionLst);
            model.addAttribute("doubtTempletLst", doubtTempletLst);
            model.addAttribute("perHundredCog", perHundredCog);
            model.addAttribute("moreScanCog", moreScanCog);
            model.addAttribute("childTab", childTab);
            model.addAttribute("ladderRuleCog", ladderRuleCog);
			model.addAttribute("projectName", "mengniu".equals(DbContextHolder.getDBType())?"1":"0");
			model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
					"data_constant_config", "role_info").split(",")));
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vcode/activity/showVcodeActivityEdit";
	}
	
	/**
	 * 创建V码活动
	 */
	@ResponseBody
	@RequestMapping("/doVcodeActivityAdd")
	public String doVcodeActivityAdd(HttpSession session, VcodeActivityCog activityCog) {
	    String errMsg = "";
	    String vcodeActivityKey = "";
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			activityCog.setDbType(DbContextHolder.getDBType());
			vcodeActivityKey = vcodeActivityService.writeActivityCog(activityCog, currentUser);
			errMsg = "保存成功";
		} catch (BusinessException ex) {
		    errMsg = ex.getMessage();
		} catch (Exception ex) {
		    errMsg = "保存失败";
		    log.error(ex.getMessage(), ex);
        }
		Map<String, Object> map = new HashMap<>();
		map.put("vcodeActivityKey", vcodeActivityKey);
		map.put("errMsg", errMsg);
		return JSON.toJSONString(map);
	}


	/**
	 * 检查活动关联SKU是否包含激活批次内的SKU
	 * 1-检查通过 其他-不通过
	 */
	@ResponseBody
	@RequestMapping("/checkBatchContainSku")
	public String checkBatchContainSku(String vcodeActivityKey, String selectedSkuKeys) {
		try {
			List<String> editSkuList = Arrays.asList(selectedSkuKeys.split(","));
			List<VcodeQrcodeBatchInfo> batchList = batchInfoService.findVcodeQrcodeBatchInfoByActivityId(vcodeActivityKey);
			Set<String> distinctSkuKeysSet = new HashSet<>();
			for (VcodeQrcodeBatchInfo batch : batchList) {
				if (batch.getSkuKey()!=null){
					distinctSkuKeysSet.add(batch.getSkuKey());
				}
			}
			// 将去重后的结果转换回List<String>
			List<String> batchSkuList = new ArrayList<>(distinctSkuKeysSet);
			if (editSkuList.containsAll(batchSkuList)) {
				return "1";
			} else {
				return "0";
			}
		} catch (Exception e) {
			log.warn("检查活动关联SKU是否包含激活批次内的SKU出现异常" + e.getMessage());
			e.printStackTrace();
		}
		return "0";
	}



	/**
	 * 修改V码活动
	 */
	@ResponseBody
	@RequestMapping("/doVcodeActivityEdit")
	public String doVcodeActivityEdit(HttpSession session, VcodeActivityCog activityCog) {
	    String errMsg = "";
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			activityCog.setDbType(DbContextHolder.getDBType());
			List<VcodeActivityCogExtends> doubtableList = JSONArray.parseArray(activityCog.getDoubtableList(),VcodeActivityCogExtends.class);
			List<VcodeActivityCogExtends> addDoubtabltList = new ArrayList<>();

			for (int i = 0; i < doubtableList.size(); i++) {
				VcodeActivityCogExtends vcodeActivityCogExtends = doubtableList.get(i);
				if (StringUtils.isEmpty(vcodeActivityCogExtends.getScanRole())) {
					activityCog.setSameMinuteRestrict(vcodeActivityCogExtends.getSameMinuteRestrict());
					activityCog.setSameDayRestrict(vcodeActivityCogExtends.getSameDayRestrict());
					activityCog.setSameMonthRestrict(vcodeActivityCogExtends.getSameMonthRestrict());
					activityCog.setDoubtfulTimeLimitType(vcodeActivityCogExtends.getDoubtfulTimeLimitType());
					activityCog.setDoubtRebateType(vcodeActivityCogExtends.getDoubtRebateType());
					activityCog.setHistoryTimesRestrict(vcodeActivityCogExtends.getHistoryTimesRestrict());
					activityCog.setDoubtRuleType(vcodeActivityCogExtends.getDoubtRuleType());
					activityCog.setDoubtRuleRangeMin(vcodeActivityCogExtends.getDoubtRuleRangeMin());
					activityCog.setDoubtRuleRangeMax(vcodeActivityCogExtends.getDoubtRuleRangeMax());
					activityCog.setEveryDayScanCount(vcodeActivityCogExtends.getEveryDayScanCount());
					activityCog.setEveryWeekScanCount(vcodeActivityCogExtends.getEveryWeekScanCount());
					activityCog.setEveryMonthScanCount(vcodeActivityCogExtends.getEveryMonthScanCount());
                    activityCog.setActivityScanCount(vcodeActivityCogExtends.getActivityScanCount());
				}else {
					vcodeActivityCogExtends.setVcodeActivityKey(activityCog.getVcodeActivityKey());
					addDoubtabltList.add(vcodeActivityCogExtends);
				}
			}
			vcodeActivityService.updateActivityCog(activityCog, currentUser);
			vcodeActivityService.updateActivityExtendCog(addDoubtabltList,activityCog.getVcodeActivityKey());
			errMsg = "保存成功";
			
		} catch (BusinessException ex) {
		    errMsg = ex.getMessage();
			
		} catch (Exception ex) {
		    errMsg = "保存失败";
		    log.error(ex.getMessage(), ex);
		}
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", errMsg);
        return JSON.toJSONString(map);
	}

	/**
	 * V码活动 查看页面
	 * 
	 * @param session
	 * @param vcodeActivityKey
	 * @param requestType
	 * @param model
	 * @return
	 */
	@RequestMapping("/showVcodeActivityView")
	public String showVcodeActivityView(HttpSession session, String infoKey, String childTab, Model model) {
		try {
            // 查询当前活动
            VcodeActivityCog currentActivity = vcodeActivityService.loadActivityByKey(infoKey);
            // SKU
            List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentActivity.getCompanyKey());
            // 活动版本号
            List<SysDataDic> versionLst = vcodeActivityService.queryAllActivityVersion();
            // 风控模板
            List<VcodeActivityDoubtTemplet> doubtTempletLst = 
                            doubtTempletService.queryForAll(Constant.statusFlag.STATUS_1);
            // 逢百规则
            VcodeActivityPerhundredCog perHundredCog = perHundredCogService.findValidByActivityKey(infoKey);
            // 一码多扫
            VcodeActivityMorescanCog moreScanCog = moreScanCogService.findValidByActivityKey(infoKey);
            
            model.addAttribute("activityCog", currentActivity);
            model.addAttribute("skuList", skuList);
            model.addAttribute("versionLst", versionLst);
            model.addAttribute("doubtTempletLst", doubtTempletLst);
            model.addAttribute("perHundredCog", perHundredCog);
            model.addAttribute("moreScanCog", moreScanCog);
            model.addAttribute("childTab", childTab);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/activity/showVcodeActivityView";
	}
	
	/**
	 * 判断时间是否在活动范围内
	 * @param </br> 
	 * @return String </br>
	 */
	@RequestMapping("/judgeActivityTime")
	@ResponseBody
	public String judgeActivityTime(HttpSession session,
			String vcodeActivityKey, String startDate, String endDate) {
		return vcodeActivityService.judgeActivityTime(vcodeActivityKey, startDate, endDate);
	}
	
}
