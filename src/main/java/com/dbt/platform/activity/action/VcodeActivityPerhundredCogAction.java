package com.dbt.platform.activity.action;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityPerhundredCog;
import com.dbt.platform.activity.service.VcodeActivityPerhundredCogService;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 逢百规则Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/activityPerhundredCog")
public class VcodeActivityPerhundredCogAction extends BaseAction{

	@Autowired
	private VcodeActivityService activityService;
	@Autowired
	private VcodeActivityPerhundredCogService perhundredCogService;
	
	/**
	 * 获取列表
	 */
	@RequestMapping("/showPerhundredCogList")
	public String showPerhundredCogList(HttpSession session, String tabsFlag, String queryParam, String pageParam, Model model) {
		try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeActivityPerhundredCog queryBean = new VcodeActivityPerhundredCog(queryParam);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            if(StringUtils.isEmpty(tabsFlag)){
            	tabsFlag = "1";
            }
            queryBean.setTabsFlag(tabsFlag);
			List<VcodeActivityPerhundredCog> resultList = perhundredCogService.queryForList(queryBean, pageInfo);
			int countResult = perhundredCogService.queryForCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("tabsFlag", tabsFlag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/perhundred/showPerhundredCogList";
	}
	
	/**
	 * 跳转新增页面
	 */
	@RequestMapping("/showPerhundredCogAdd")
	public String showPerhundredAdd(HttpSession session, Model model){
		// 查询没有关联一码多扫或已关联并过期的活动
		model.addAttribute("activityList", activityService.queryListForSuitPerhundred(null));
        return "vcode/perhundred/showPerhundredCogAdd";
	}
	
	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showPerhundredCogEdit")
	public String showPerhundredCogEdit(HttpSession session, String infoKey, Model model){
	    VcodeActivityPerhundredCog perhundredCog = perhundredCogService.findById(infoKey);
	    model.addAttribute("activityList", activityService.queryListForSuitPerhundred(infoKey));
	    model.addAttribute("perhundredCog", perhundredCog);
	    return "vcode/perhundred/showPerhundredCogEdit";
	}
	
	/**
	 * 跳转查看页面
	 */
	@RequestMapping("/showPerhundredCogView")
	public String showPerhundredCogView(HttpSession session, String infoKey, Model model){
		VcodeActivityPerhundredCog perhundredCog = perhundredCogService.findById(infoKey);
		VcodeActivityCog activityCog = activityService.findById(perhundredCog.getVcodeActivityKey());
		if (activityCog != null) {
		    perhundredCog.setVcodeActivityName(activityCog.getVcodeActivityName());
		    perhundredCog.setSkuName(activityCog.getSkuName());
		    if ("0".equals(activityCog.getSkuType())) {
		        perhundredCog.setSkuType("瓶码");
		    } else if ("1".equals(activityCog.getSkuType())) {
		        perhundredCog.setSkuType("罐码");
		    } else if ("2".equals(activityCog.getSkuType())) {
		        perhundredCog.setSkuType("箱码");
            } else {
                perhundredCog.setSkuType("其它");
            }
		}
		model.addAttribute("perhundredCog", perhundredCog);
        return "vcode/perhundred/showPerhundredCogView";
	}
	
	/**
	 * 添加记录
	 */
	@RequestMapping("/doPerhundredCogAdd")
	public String doPerhundredCogAdd(HttpSession session, VcodeActivityPerhundredCog perhundredCog, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			perhundredCog.setCompanyKey(currentUser.getCompanyKey());
			perhundredCog.fillFields(currentUser.getUserKey());
			perhundredCogService.addPerhundredCog(perhundredCog);
            
            String vcodeActivityKey = perhundredCog.getVcodeActivityKey();
            
            // 逢百规则当天有效标志
            String redisKey = CacheUtilNew.cacheKey.dotRedpacketCog
                        .DOT_RULE_IS_VALID + Constant.DBTSPLIT + perhundredCog.getInfoKey();
            RedisApiUtil.getInstance().del(true,redisKey);
            RedisApiUtil.getInstance().del(true,redisKey + Constant.DBTSPLIT + DateUtil.getDate());
            // 删除规则缓存
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_ACTIVITY_PERHUNDRED_COG + Constant.DBTSPLIT + vcodeActivityKey);
			model.addAttribute("errMsg", "添加成功");
		} catch (Exception ex) {
		    model.addAttribute("errMsg", "添加失败");
			ex.printStackTrace();
		}
		return "forward:showPerhundredCogList.do";
	}
	
	/**
	 * 修改记录
	 */
	@RequestMapping("/doPerhundredCogEdit")
	public String doPerhundredCogEdit(HttpSession session, VcodeActivityPerhundredCog perhundred, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			perhundred.setCompanyKey(currentUser.getCompanyKey());
			perhundred.fillFields(currentUser.getUserKey());
			perhundredCogService.editPerhundredCog(perhundred);
			
			String vcodeActivityKey = perhundred.getVcodeActivityKey();
			
			// 逢百规则当天有效标志
			String redisKey = CacheUtilNew.cacheKey.dotRedpacketCog
			            .DOT_RULE_IS_VALID + Constant.DBTSPLIT + perhundred.getInfoKey();
			RedisApiUtil.getInstance().del(true,redisKey);
	        RedisApiUtil.getInstance().del(true, redisKey + Constant.DBTSPLIT + DateUtil.getDate());
	        // 删除规则缓存
	        CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
					.KEY_VCODE_ACTIVITY_PERHUNDRED_COG + Constant.DBTSPLIT + vcodeActivityKey);
			model.addAttribute("errMsg", "修改成功");
		} catch (Exception ex) {
		    model.addAttribute("errMsg", "修改失败");
			ex.printStackTrace();
		}
		return "forward:showPerhundredCogList.do";
	}
	
	/**
	 * 刪除记录
	 * @param session
	 * @param infoKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/doPerhundredCogDelete")
	public String doPerhundredCogDelete(HttpSession session, String tabsFlag, String infoKey,
            String queryParam, String pageParam, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			String vcodeActivityKey = perhundredCogService.deletePerhundredCog(infoKey, currentUser.getUserKey());
			
			// 删除逢百规则过期标志缓存
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog
	        		.DOT_RULE_IS_VALID + Constant.DBTSPLIT + infoKey);
	        // 删除统计数据缓存
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog
	        		.DOT_RULE_TOTAL_PRIZE + Constant.DBTSPLIT + infoKey + "bottle");
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog
	        		.DOT_RULE_TOTAL_PRIZE + Constant.DBTSPLIT + infoKey + "money");
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog
	        		.DOT_RULE_TOTAL_PRIZE + Constant.DBTSPLIT + infoKey + "vpoint");
	        // 删除规则缓存
	        CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey 
	        		.KEY_VCODE_ACTIVITY_PERHUNDRED_COG + Constant.DBTSPLIT + vcodeActivityKey);
	        
			model.addAttribute("errMsg", "删除成功");
		}catch(Exception e){
			model.addAttribute("errMsg", "删除失败");
			
		}
		return showPerhundredCogList(session, tabsFlag, queryParam, pageParam, model); 
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
		return perhundredCogService.checkBussionName(infoKey, bussionName);
	}
}
