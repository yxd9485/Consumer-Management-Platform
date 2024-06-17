package com.dbt.platform.signin.action;

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
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.signin.bean.VpsVcodeSigninCog;
import com.dbt.platform.signin.service.VpsVcodeSigninCogService;
import com.dbt.platform.signin.service.VpsVcodeSigninSkuCogService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @author RoyFu
 * @createTime 2016年1月19日 下午5:44:05
 * @description 签到活动Action
 */

@Controller
@RequestMapping("/vcodeSignin")
public class VcodeSigninCogAction extends BaseAction {

	@Autowired
	private VpsVcodeSigninCogService signinCogService;
	@Autowired
	private VpsVcodeSigninSkuCogService signinSkuCogService;
	@Autowired
	private SkuInfoService skuInfoService;

	/**
	 * 签到活动列表
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showVcodeSigninList")
	public String showVcodeActivityList(HttpSession session, String tabsFlag,
	                        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			VpsVcodeSigninCog queryBean = new VpsVcodeSigninCog(queryParam);
			queryBean.setCompanyKey(companyKey);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			if(StringUtils.isBlank(tabsFlag)){
				tabsFlag = "1";	// 已上线
			}
			queryBean.setTabsFlag(tabsFlag);
			List<VpsVcodeSigninCog> resultList = 
			        signinCogService.findVcodeActivityList(queryBean, pageInfo);
			int countResult = signinCogService.countVcodeActivityList(queryBean);
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
		return "vcode/signin/showVcodeSigninList";
	}

	/**
	 * 签到活动 添加页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showVcodeSigninAdd")
	public String showVcodeActivityAdd(HttpSession session, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			String companyKey = currentUser.getCompanyKey();
			model.addAttribute("companyKey", companyKey);
			
			List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(companyKey);
			model.addAttribute("skuList", skuList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/signin/showVcodeSigninAdd";
	}

	/**
	 * 签到活动 编辑页面
	 * 
	 * @param session
	 * @param vcodeActivityKey
	 * @param requestType
	 * @param model
	 * @return
	 */
	@RequestMapping("/showVcodeSigninEdit")
	public String showVcodeActivityEdit(HttpSession session, String activityKey, Model model) {
		try {
			// 查询当前活动
		    VpsVcodeSigninCog currSignCog = signinCogService.findActivityByKey(activityKey);
		    model.addAttribute("signinCog", signinCogService.findActivityByKey(activityKey));
			model.addAttribute("signinSkuCogLst", signinSkuCogService.queryByActivitykey(activityKey));
			model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(currSignCog.getCompanyKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/signin/showVcodeSigninEdit";
	}
	
	/**
	 * 创建签到活动
	 */
	@RequestMapping("/doVcodeSigninAdd")
	public String doVcodeActivityAdd(HttpSession session, VpsVcodeSigninCog signinCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			signinCogService.writeActivityCog(signinCog, currentUser.getUserKey());
			model.addAttribute("errMsg", "添加成功");
		} catch (BusinessException ex) {
		    model.addAttribute("errMsg", ex.getMessage());
		} catch (Exception ex) {
			model.addAttribute("errMsg", "添加失败");
			log.error("签到活动添加失败", ex);
		}
		return "forward:showVcodeSigninList.do";
	}

	/**
	 * 修改签到活动
	 */
	@RequestMapping("/doVcodeSigninEdit")
	public String doVcodeActivityEdit(HttpSession session, VpsVcodeSigninCog signinCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			signinCogService.updateActivityCog(signinCog, currentUser.getUserKey(), model);
			model.addAttribute("errMsg", "修改成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "修改失败");
            log.error("签到活动修改失败", ex);
        }
		return "forward:showVcodeSigninList.do";
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
	@RequestMapping("/showVcodeSigninView")
	public String showVcodeActivityView(HttpSession session, String activityKey, Model model) {
		try {
			// 查询当前活动
            VpsVcodeSigninCog currSignCog = signinCogService.findActivityByKey(activityKey);
            model.addAttribute("signinCog", signinCogService.findActivityByKey(activityKey));
            model.addAttribute("signinSkuCogLst", signinSkuCogService.queryByActivitykey(activityKey));
            model.addAttribute("skuList", skuInfoService.loadSkuListByCompany(currSignCog.getCompanyKey()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/signin/showVcodeSigninView";
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
		return signinCogService.checkBussionName(infoKey, bussionName);
	}
    
    /**
     * 校验签到的开始及结束日期的合法性
     */
    @ResponseBody
    @RequestMapping("/validSignDate")
    public String validSignDate(VpsVcodeSigninCog signinCog){
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            signinCogService.validSignDate(signinCog);
        } catch (Exception e) {
            resultMap.put("errMsg", e.getMessage());
        }
        return JSON.toJSONString(resultMap);
    }
}
