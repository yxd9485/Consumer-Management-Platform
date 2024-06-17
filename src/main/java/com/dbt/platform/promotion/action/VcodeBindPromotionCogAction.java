package com.dbt.platform.promotion.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.promotion.bean.VpsVcodeBindPromotionCog;
import com.dbt.platform.promotion.service.VpsVcodeBindPromotionCogService;
import com.dbt.platform.signin.service.VpsVcodeSigninSkuCogService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 捆绑促销活动配置表Action
 */
@Controller
@RequestMapping("/promotion")
public class VcodeBindPromotionCogAction extends BaseAction {

	@Autowired
	private VpsVcodeBindPromotionCogService promotionCogService;
	@Autowired
	private VpsVcodeSigninSkuCogService signinSkuCogService;
	@Autowired
	private SkuInfoService skuInfoService;

	/**
	 * 活动列表
	 */
	@RequestMapping("/showBindPromotionList")
	public String showBindPromotionList(HttpSession session, String tabsFlag,
	                        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			VpsVcodeBindPromotionCog queryBean = new VpsVcodeBindPromotionCog(queryParam);
			queryBean.setCompanyKey(companyKey);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			if(StringUtils.isBlank(tabsFlag)){
				tabsFlag = "1";	// 已上线
			}
			queryBean.setTabsFlag(tabsFlag);
			List<VpsVcodeBindPromotionCog> resultList = 
			        promotionCogService.findVcodeActivityList(queryBean, pageInfo);
			int countResult = promotionCogService.countVcodeActivityList(queryBean);
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
			model.addAttribute("tabsFlag", tabsFlag);
			model.addAttribute("nowTime", new LocalDate());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/promotion/showBindPromotionList";
	}

	/**
	 * 跳转添加页面
	 */
	@RequestMapping("/showBindPromotionAdd")
	public String showBindPromotionAdd(HttpSession session, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			model.addAttribute("companyKey", companyKey);
			
			List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(companyKey);
			model.addAttribute("skuList", skuList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/promotion/showBindPromotionAdd";
	}

	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showBindPromotionEdit")
	public String showBindPromotionEdit(HttpSession session, String activityKey,  Model model) {
		try {
			// 查询当前活动
		    VpsVcodeBindPromotionCog currPromotionCog = promotionCogService.findActivityByKey(activityKey);
		    model.addAttribute("promotionCog", promotionCogService.findActivityByKey(activityKey));
			model.addAttribute("signinSkuCogLst", signinSkuCogService.queryByActivitykey(activityKey));
			model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(currPromotionCog.getCompanyKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/promotion/showBindPromotionEdit";
	}
	
	/**
	 * 创建活动
	 */
	@RequestMapping("/doBindPromotionAdd")
	public String doBindPromotionAdd(HttpSession session, VpsVcodeBindPromotionCog promotionCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			promotionCogService.writeActivityCog(promotionCog, currentUser.getUserKey(), model);
		} catch (Exception ex) {
			model.addAttribute("errMsg", "添加失败");
			ex.printStackTrace();
		}
		return "forward:showBindPromotionList.do";
	}

	/**
	 * 修改活动
	 */
	@RequestMapping("/doBindPromotionEdit")
	public String doVcodeActivityEdit(HttpSession session,
			VpsVcodeBindPromotionCog promotionCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			promotionCogService.updateActivityCog(promotionCog, currentUser.getUserKey(), model);
		} catch (Exception ex) {
			model.addAttribute("errMsg", "修改失败");
			ex.printStackTrace();
		}
		return "forward:showBindPromotionList.do";
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
	@RequestMapping("/showBindPromotionView")
	public String showBindPromotionView(HttpSession session, String activityKey, Model model) {
		try {
			// 查询当前活动
            VpsVcodeBindPromotionCog currPromotionCog = promotionCogService.findActivityByKey(activityKey);
            model.addAttribute("promotionCog", promotionCogService.findActivityByKey(activityKey));
            model.addAttribute("signinSkuCogLst", signinSkuCogService.queryByActivitykey(activityKey));
            model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(currPromotionCog.getCompanyKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/promotion/showBindPromotionView";
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
		return promotionCogService.checkBussionName(infoKey, bussionName);
	}
}
