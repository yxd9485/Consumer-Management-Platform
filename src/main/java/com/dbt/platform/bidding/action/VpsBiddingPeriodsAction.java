package com.dbt.platform.bidding.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.bidding.bean.VpsBiddingPeriods;
import com.dbt.platform.bidding.service.VpsBiddingPeriodsService;
import com.dbt.platform.prize.service.MajorInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
  * 竞价活动期数Action
 */
@Controller
@RequestMapping("/biddingPeriods")
public class VpsBiddingPeriodsAction extends BaseAction {

	@Autowired
	private VpsBiddingPeriodsService biddingPeriodsService;
	@Autowired
	private MajorInfoService majorInfoService;

	/**
	 * 期数列表
	 */
	@RequestMapping("/showBiddingPeriodsList")
	public String showBiddingPeriodsList(HttpSession session, 
			String queryParam, String pageParam, String activityKey, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			VpsBiddingPeriods queryBean = new VpsBiddingPeriods();
			queryBean.setActivityKey(activityKey);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			List<VpsBiddingPeriods> resultList = 
					biddingPeriodsService.queryPeriodsList(queryBean, pageInfo);
			int countResult = biddingPeriodsService.countPeriodsList(queryBean);
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/bidding/showBiddingPeriodsList";
	}

	
	/**
	 * 跳转查看页面
	 */
	@RequestMapping("/showBiddingPeriodsView")
	public String showBiddingPeriodsView(HttpSession session, String infoKey,  Model model) {
		try {
			// 查询当前活动
			VpsBiddingPeriods periods = biddingPeriodsService.findPeriodsByKey(infoKey);
			model.addAttribute("periods", periods);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/bidding/showBiddingPeriodsData";
	}
	
	/**
	 * 查询中奖用户名单
	 *
	 * @param session
	 * @param request
	 * @param exchangeId
	 * @param model
	 */
	@RequestMapping("/findMajorInfoByExchangeId")
	public String findMajorInfoByExchangeId(HttpSession session,
			HttpServletRequest request, String exchangeId, String activityKey, Model model) throws Exception {
		model.addAttribute("majorInfo", majorInfoService.findMajorInfoByExchangeId(exchangeId));
		model.addAttribute("activityKey", activityKey);
		return "vcode/bidding/showMajorInfoView";
	}
}
