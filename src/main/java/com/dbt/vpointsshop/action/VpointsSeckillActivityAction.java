package com.dbt.vpointsshop.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsSeckillActivityCog;
import com.dbt.vpointsshop.bean.VpointsSeckillOrderStatistics;
import com.dbt.vpointsshop.service.ExchangeService;
import com.dbt.vpointsshop.service.VpointsGoodsService;
import com.dbt.vpointsshop.service.VpointsSeckillActivityService;

/**
 *  商城秒杀活动Action
 * @author Administrator
 *
 */

@Controller
@RequestMapping("/seckillActivity")
public class VpointsSeckillActivityAction extends BaseAction {

	@Autowired
	private VpointsSeckillActivityService activityService;
	@Autowired
	private VpointsGoodsService goodsService;
	@Autowired
	private ExchangeService exchangeService;
	@Autowired
    private CRMServiceServiceImpl crmServiceService;

	/**
	 *  活动列表
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showSeckillActivityList")
	public String showSeckillActivityList(HttpSession session, String queryParam, String pageParam, String tabsFlag, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			VpointsSeckillActivityCog queryBean = new VpointsSeckillActivityCog(queryParam);
			if (StringUtils.isBlank(tabsFlag)) {
			    tabsFlag = "2";
			}
			queryBean.setTabsFlag(tabsFlag);
			queryBean.setCurrDate(DateUtil.getDate());
			
			List<VpointsSeckillActivityCog> resultList = 
					activityService.findVcodeActivityList(queryBean, pageInfo);
			int countResult = activityService.countVcodeActivityList(queryBean);
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
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
		return "vpointsGoods/seckill/showSeckillActivityList";
	}

	/**
	 * V码活动 添加页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showSeckillActivityAdd")
	public String showSeckillActivityAdd(HttpSession session, Model model) {
		try {
			String groupSwitch = DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
					DatadicKey.filterSwitchSetting.SWITCH_GROUP);
			if(DatadicUtil.isSwitchON(groupSwitch)) {
				model.addAttribute("groupList", crmServiceService.queryVcodeActivityCrmGroup());
			}
			model.addAttribute("goodsList", goodsService.queryGoodsList("1", null));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/seckill/showSeckillActivityAdd";
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
	@RequestMapping("/showSeckillActivityEdit")
	public String showSeckillActivityEdit(HttpSession session, String infoKey, String childTab, Model model) {
		try {
			// 查询当前活动
			VpointsSeckillActivityCog currentActivity = activityService.loadActivityByKey(infoKey);
			
			String groupSwitch = DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
					DatadicKey.filterSwitchSetting.SWITCH_GROUP);
			if(DatadicUtil.isSwitchON(groupSwitch)) {
				model.addAttribute("groupList", crmServiceService.queryVcodeActivityCrmGroup());
			}
			
			model.addAttribute("goodsList", goodsService.queryGoodsList("1", null));
            model.addAttribute("activityCog", currentActivity);
            model.addAttribute("childTab", childTab);
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vpointsGoods/seckill/showSeckillActivityEdit";
	}
	
	/**
	 * V码活动 数据页面
	 * 
	 * @param session
	 * @param vcodeActivityKey
	 * @param requestType
	 * @param model
	 * @return
	 */
	@RequestMapping("/showSeckillActivityData")
	public String showSeckillActivityData(HttpSession session, String infoKey, String childTab, Model model) {
		try {
			// 查询当前活动
			VpointsSeckillActivityCog currentActivity = activityService.loadActivityByKey(infoKey);
			
			// 数据汇总
			VpointsSeckillOrderStatistics statistics =  exchangeService.findOrderStatistics(infoKey, Constant.EXCHANGE_FLAG.flag_3);
			if(null != statistics) {
				statistics.setGoodsRemains(currentActivity.getSeckillPeriodsNum());
			}
			
			model.addAttribute("statistics", statistics);
            model.addAttribute("seckillActivityKey", infoKey);
            model.addAttribute("activityCog", currentActivity);
            model.addAttribute("serverName", DbContextHolder.getDBType());
			model.addAttribute("childTab", childTab);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
		return "vpointsGoods/seckill/showSeckillActivityData";
	}
	
	/**
	 * 创建活动
	 */
	@ResponseBody
	@RequestMapping("/doSeckillActivityAdd")
	public String seckillActivityAdd(HttpSession session, VpointsSeckillActivityCog activityCog) {
	    String errMsg = "";
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			activityService.writeActivityCog(activityCog, currentUser);
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
	 * 修改V码活动
	 */
	@ResponseBody
	@RequestMapping("/doSeckillActivityEdit")
	public String seckillActivityEdit(HttpSession session, VpointsSeckillActivityCog activityCog) {
	    String errMsg = "";
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			activityService.updateActivityCog(activityCog, currentUser);
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
	@RequestMapping("/showSeckillActivityView")
	public String showSeckillActivityView(HttpSession session, String infoKey, String childTab, Model model) {
		try {
            // 查询当前活动
			VpointsSeckillActivityCog currentActivity = activityService.loadActivityByKey(infoKey);
            
            model.addAttribute("activityCog", currentActivity);
            model.addAttribute("childTab", childTab);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/seckill/showSeckillActivityView";
	}
	
	/**
	 * 停用正在进行中的活动
	 * @param </br> 
	 * @return String </br>
	 */
	@RequestMapping("/updateActivityStop")
	@ResponseBody
	public String updateActivityStop(HttpSession session, String infoKey) {
		String errMsg = "";
		try {
			activityService.updateActivityStop(infoKey);
			errMsg = "操作成功";
		}catch(Exception e) {
			errMsg = "操作失败";
		}
		Map<String, Object> map = new HashMap<>();
        map.put("errMsg", errMsg);
        return JSON.toJSONString(map);
	}
	
	/**
	 * 删除活动
	 * @param </br> 
	 * @return String </br>
	 */
	@RequestMapping("/delActivity")
	@ResponseBody
	public String delActivity(HttpSession session, String infoKey) {
		String errMsg = "";
		try {
			activityService.updateActivityDel(infoKey);
			errMsg = "删除成功";
		}catch(Exception e) {
			errMsg = "删除失败";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("errMsg", errMsg);
		return JSON.toJSONString(map);
	}

	
}
