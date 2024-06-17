package com.dbt.platform.doubleprize.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import com.dbt.platform.doubleprize.bean.VpsVcodeDoublePrizeCog;
import com.dbt.platform.doubleprize.service.VpsVcodeDoublePrizeCogService;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 一码双奖活动配置表Action
 */
@Controller
@RequestMapping("/doubleprize")
public class VpsVcodeDoublePrizeCogAction extends BaseAction {

	@Autowired
	private VpsVcodeDoublePrizeCogService doublePrizeCogService;
	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private SysAreaService areaService;
	@Autowired
	private VcodeActivityHotAreaCogService hotAreaCogService;

	/**
	 * 活动列表
	 */
	@RequestMapping("/showDoublePrizeList")
	public String showDoublePrizeList(HttpSession session, String tabsFlag,
	                        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			VpsVcodeDoublePrizeCog queryBean = new VpsVcodeDoublePrizeCog(queryParam);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			if(StringUtils.isBlank(tabsFlag)){
				tabsFlag = "1";	// 已上线
			}
			queryBean.setTabsFlag(tabsFlag);
			List<VpsVcodeDoublePrizeCog> resultList = 
			        doublePrizeCogService.queryVcodeActivityList(queryBean, pageInfo);
			int countResult = doublePrizeCogService.countVcodeActivityList(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
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
		return "vcode/doubleprize/showDoublePrizeList";
	}

	/**
	 * 跳转添加页面
	 */
	@RequestMapping("/showDoublePrizeAdd")
	public String showDoublePrizeAdd(HttpSession session, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			model.addAttribute("companyKey", companyKey);
			
			model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(companyKey));
			model.addAttribute("prizeMap", doublePrizeCogService.getPrizeType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/doubleprize/showDoublePrizeAdd";
	}

	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showDoublePrizeEdit")
	public String showDoublePrizeEdit(HttpSession session, String activityKey,  Model model) {
		try {
			// 查询当前活动
		    VpsVcodeDoublePrizeCog doublePrizeCog = doublePrizeCogService.findActivityByKey(activityKey);
		    doublePrizeCog.setPromotionSkuKeyAry(doublePrizeCog.getPromotionSkuKey().split(","));
		    doublePrizeCog.setFilterSkuKeyAry(StringUtils.defaultIfBlank(doublePrizeCog.getFilterSkuKey(), "").split(","));
		    
		    model.addAttribute("doublePrizeCog", doublePrizeCog);
		    model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(doublePrizeCog.getCompanyKey()));
            model.addAttribute("prizeMap", doublePrizeCogService.getPrizeType());
            model.addAttribute("prizeLimitAry", doublePrizeCog.getPrizeLimit().split(","));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/doubleprize/showDoublePrizeEdit";
	}
	
	/**
	 * 创建活动
	 */
	@RequestMapping("/doDoublePrizeAdd")
	public String doDoublePrizeAdd(HttpSession session, VpsVcodeDoublePrizeCog doublePrizeCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			doublePrizeCogService.writeActivityCog(doublePrizeCog, currentUser.getUserKey(), model);
            
            // 删除所有Memcache缓存，（因为要删除缓存的用户过多，所以采用清空所有Memcache缓存）
            CacheUtilNew.removeAll();
            model.addAttribute("errMsg", "添加成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "添加失败");
			ex.printStackTrace();
		}
		return "forward:showDoublePrizeList.do";
	}

	/**
	 * 修改活动
	 */
	@RequestMapping("/doDoublePrizeEdit")
	public String doVcodeActivityEdit(HttpSession session,VpsVcodeDoublePrizeCog doublePrizeCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			doublePrizeCogService.updateActivityCog(doublePrizeCog, currentUser.getUserKey(), model);
            
            // 删除所有Memcache缓存，（因为要删除缓存的用户过多，所以采用清空所有Memcache缓存）
            CacheUtilNew.removeAll();
            model.addAttribute("errMsg", "修改成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "修改失败");
			ex.printStackTrace();
		}
		return "forward:showDoublePrizeList.do";
	}

	/**
	 * 签到活动 查看页面
	 * 
	 * @param session
	 * @param vcodeActivityKey
	 * @param requestType
	 * @param model
	 * @return
	 */
	@RequestMapping("/showDoublePrizeView")
	public String showDoublePrizeView(HttpSession session, 
	        String activityKey, String queryParam, String pageParam, Model model) {
		try {
			// 查询当前活动
            VpsVcodeDoublePrizeCog doublePrizeCog = doublePrizeCogService.findActivityByKey(activityKey);
            
            if (StringUtils.isNotBlank(doublePrizeCog.getFilterAreaCode())) {
                String[] areaCodeAry = doublePrizeCog.getFilterAreaCode().split(",");
                String[] areaNameAry = new String[areaCodeAry.length];
                for (int i = 0; i < areaCodeAry.length; i++) {
                    areaNameAry[i] = areaService.getAreaNameByCode(areaCodeAry[i]);
                }
                doublePrizeCog.setFilterAreaNameAry(areaNameAry);
            } else {
                doublePrizeCog.setFilterAreaNameAry(new String[]{"--"});
            }
            doublePrizeCog.setPromotionSkuKeyAry(doublePrizeCog.getPromotionSkuKey().split(","));
            doublePrizeCog.setFilterSkuKeyAry(StringUtils.defaultIfBlank(doublePrizeCog.getFilterSkuKey(), "").split(","));
            
            if (StringUtils.isNotBlank(doublePrizeCog.getHotAreaKey())) {
                VcodeActivityHotAreaCog hotAreaCog = hotAreaCogService.findById(doublePrizeCog.getHotAreaKey());
                if (hotAreaCog != null) {
                    hotAreaCog.setHotAreaName(hotAreaCog.getHotAreaName());
                }
            }
            
            model.addAttribute("doublePrizeCog", doublePrizeCog);
            model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(doublePrizeCog.getCompanyKey()));
            model.addAttribute("prizeMap", doublePrizeCogService.getPrizeType());
            model.addAttribute("prizeLimitAry", doublePrizeCog.getPrizeLimit().split(","));

			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/doubleprize/showDoublePrizeView";
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkBussionName")
	public String checkBussionName(String infoKey, String bussionName){
		return doublePrizeCogService.checkBussionName(infoKey, bussionName);
	}
	
	/**
	 * 校验周期类型对应的开始及结束日期的合法性
	 * 
	 * @param periodType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkPeriodType")
	public String checkPeriodType(String periodType, String startDate, String endDate) {
	    Map<String, Object> resultMap = new HashMap<>();
	    try {
	        doublePrizeCogService.checkPeriodType(periodType, startDate, endDate);
        } catch (Exception e) {
            resultMap.put("errMsg", e.getMessage());
        }
	    return JSON.toJSONString(resultMap);
	}
}
