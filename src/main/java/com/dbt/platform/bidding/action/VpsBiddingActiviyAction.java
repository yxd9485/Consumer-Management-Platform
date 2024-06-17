package com.dbt.platform.bidding.action;

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
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.bidding.bean.VpsBiddingActiviy;
import com.dbt.platform.bidding.bean.VpsBiddingPeriods;
import com.dbt.platform.bidding.service.VpsBiddingActiviyService;
import com.dbt.platform.bidding.service.VpsBiddingPeriodsService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.service.VpointsGoodsService;

/**
  * 竞价活动配置表Action
 */
@Controller
@RequestMapping("/biddingActivity")
public class VpsBiddingActiviyAction extends BaseAction {

	@Autowired
	private VpsBiddingActiviyService biddingActiviyService;
	@Autowired
	private VpsBiddingPeriodsService biddingPeriodsService;
	@Autowired
	private VpointsGoodsService goodsService;

	/**
	 * 活动列表
	 */
	@RequestMapping("/showBiddingActivityList")
	public String showBiddingActivityList(HttpSession session, String tabsFlag,
	                        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			VpsBiddingActiviy queryBean = new VpsBiddingActiviy(queryParam);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			if(StringUtils.isBlank(tabsFlag)){
				tabsFlag = "1";	// 已上线
			}
			queryBean.setTabsFlag(tabsFlag);
			List<VpsBiddingActiviy> resultList = 
					biddingActiviyService.queryVcodeActivityList(queryBean, pageInfo);
			int countResult = biddingActiviyService.countVcodeActivityList(queryBean);
			model.addAttribute("goodsList", 
					goodsService.queryGoodsList(Constant.exchangeChannel.CHANNEL_9, "1", null));
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
		return "vcode/bidding/showBiddingActivityList";
	}

	/**
	 * 跳转添加页面
	 */
	@RequestMapping("/showBiddingActivityAdd")
	public String showBiddingActivityAdd(HttpSession session, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			model.addAttribute("companyKey", companyKey);
			model.addAttribute("goodsList", 
					goodsService.queryGoodsList(Constant.exchangeChannel.CHANNEL_9, "1", null));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/bidding/showBiddingActivityAdd";
	}

	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showBiddingActivityEdit")
	public String showBiddingActivityData(HttpSession session, String activityKey,  Model model) {
		try {
			// 查询当前活动
			VpsBiddingActiviy biddingActivity = biddingActiviyService.findActivityByKey(activityKey);
		    model.addAttribute("biddingActivity", biddingActivity);
		    model.addAttribute("goodsList", 
					goodsService.queryGoodsList(Constant.exchangeChannel.CHANNEL_9, "1", null));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/bidding/showBiddingActivityEdit";
	}
	
	/**
	 * 跳转数据页面
	 */
	@RequestMapping("/showBiddingActivityData")
	public String showBiddingActivityEdit(HttpSession session, String activityKey,  Model model) {
		try {
			// 查询当前活动
			VpsBiddingActiviy biddingActivity = biddingActiviyService.findActivityByKey(activityKey);
			model.addAttribute("biddingActivity", biddingActivity);
			model.addAttribute("goodsList", goodsService.queryGoodsList(Constant.exchangeChannel.CHANNEL_9, "1", null));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/bidding/showBiddingActivityData";
	}
	
	/**
	 * 创建活动
	 */
	@RequestMapping("/doBiddingActivityAdd")
	public String doBiddingActivityAdd(HttpSession session, VpsBiddingActiviy biddingActivity, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			biddingActiviyService.writeActivityCog(biddingActivity, currentUser.getUserKey(), model);
            
            // 删除所有Memcache缓存，（因为要删除缓存的用户过多，所以采用清空所有Memcache缓存）
            CacheUtilNew.removeAll();
            model.addAttribute("errMsg", "添加成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "添加失败");
			ex.printStackTrace();
		}
		return "forward:showBiddingActivityList.do";
	}

	/**
	 * 修改活动
	 */
	@RequestMapping("/doBiddingActivityEdit")
	public String doBiddingActivityEdit(HttpSession session,VpsBiddingActiviy biddingActivity, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			biddingActiviyService.updateActivityCog(biddingActivity, currentUser.getUserKey(), model);
            
            // 删除所有Memcache缓存，（因为要删除缓存的用户过多，所以采用清空所有Memcache缓存）
            CacheUtilNew.removeAll();
            model.addAttribute("errMsg", "修改成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "修改失败");
			ex.printStackTrace();
		}
		return "forward:showBiddingActivityList.do";
	}
	
	/**
     * 删除活动
     * 
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/doBiddingActivityDel")
    public String doBiddingActivityDel(HttpSession session, String activityKey, Model model) {
    	String errMsg = "";
        try {
        	VpsBiddingPeriods queryBean = new VpsBiddingPeriods();
        	queryBean.setActivityKey(activityKey);
        	if(biddingPeriodsService.countPeriodsList(queryBean) > 0) {
        		errMsg = "该活动下已经产生数据，无法删除";
        	}else {
        		biddingActiviyService.delete(activityKey);
        		errMsg = "删除成功";
        	}
        } catch (Exception e) {
        	errMsg = "删除失败";
            log.error("删除失败", e);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("errMsg", errMsg);
        return JSON.toJSONString(map);
    }

	/**
	 * 查看页面
	 * 
	 * @param session
	 * @param vcodeActivityKey
	 * @param requestType
	 * @param model
	 * @return
	 */
	@RequestMapping("/showBiddingActivityView")
	public String showBiddingActivityView(HttpSession session, 
	        String activityKey, String queryParam, String pageParam, Model model) {
		try {
			// 查询当前活动
			VpsBiddingActiviy biddingActivity = biddingActiviyService.findActivityByKey(activityKey);
            
            model.addAttribute("biddingActivity", biddingActivity);
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/bidding/showBiddingActivityView";
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
		return biddingActiviyService.checkBussionName(infoKey, bussionName);
	}
	
	/**
	 * 检验月擂台是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param startDate	开始日期
	 * @param endDate	结束日期
	 * @return 1重复
	 */
	@ResponseBody
	@RequestMapping("/checkMonthBidding")
	public String checkMonthBidding(String infoKey, String startDate, String endDate){
		return biddingActiviyService.checkMonthBidding(infoKey, startDate, endDate);
	}
}
