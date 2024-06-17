package com.dbt.platform.activity.action;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityMorescanCog;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import com.dbt.platform.activity.service.VcodeActivityMorescanCogService;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 一码多扫规则Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/activityMorescanCog")
public class VcodeActivityMorescanCogAction extends BaseAction{

	@Autowired
	private VcodeActivityService activityService;
    @Autowired
    private VcodeActivityHotAreaCogService hotAreaCogService;
	@Autowired
	private VcodeActivityMorescanCogService morescanCogService;
	
	/**
	 * 获取列表
	 */
	@RequestMapping("/showMorescanCogList")
	public String showMorescanCogList(HttpSession session, String tabsFlag, String queryParam, String pageParam, Model model) {
		try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeActivityMorescanCog queryBean = new VcodeActivityMorescanCog(queryParam);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            if(StringUtils.isEmpty(tabsFlag)){
            	tabsFlag = "1";
            }
            queryBean.setTabsFlag(tabsFlag);
			List<VcodeActivityMorescanCog> resultList = morescanCogService.queryForList(queryBean, pageInfo);
			int countResult = morescanCogService.queryForCount(queryBean);
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
			if ("mengniu".equals(DbContextHolder.getDBType())) {
				model.addAttribute("projectName", "1");
			}else{
				model.addAttribute("projectName", "0");
			}
			model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
					"data_constant_config", "role_info").split(",")));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/morescan/showMorescanCogList";
	}
	
	/**
	 * 跳转新增页面
	 */
	@RequestMapping("/showMorescanCogAdd")
	public String showMorescanCogAdd(HttpSession session, Model model){
		// 查询没有关联一码多扫或已关联并停用或过期的活动
		model.addAttribute("activityList", activityService.queryListForSuitMorescan(null));
		if ("mengniu".equals(DbContextHolder.getDBType())) {
			model.addAttribute("projectName", "1");
		}else{
			model.addAttribute("projectName", "0");
		}

        model.addAttribute("hotAreaList", hotAreaCogService.findHotAreaListByAreaCode(null));
		model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
				 "data_constant_config", "role_info").split(",")));
        return "vcode/morescan/showMorescanCogAdd";
	}
	
	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showMorescanCogEdit")
	public String showMorescanCogEdit(HttpSession session, String infoKey, Model model){
	    VcodeActivityMorescanCog morescanCog = morescanCogService.findById(infoKey);
	    model.addAttribute("activityList", activityService.queryListForSuitMorescan(infoKey));
	    model.addAttribute("morescanCog", morescanCog);
		if ("mengniu".equals(DbContextHolder.getDBType())) {
			model.addAttribute("projectName", "1");
		}else{
			model.addAttribute("projectName", "0");
		}
        model.addAttribute("hotAreaList", hotAreaCogService.findHotAreaListByAreaCode(null));
		model.addAttribute("roleInfoAll", Arrays.asList(DatadicUtil.getDataDicValue(
				"data_constant_config", "role_info").split(",")));
	    return "vcode/morescan/showMorescanCogEdit";
	}
	
	/**
	 * 跳转查看页面
	 */
	@RequestMapping("/showMorescanCogView")
	public String showMorescanCogView(HttpSession session, String infoKey, Model model){
		VcodeActivityMorescanCog morescanCog = morescanCogService.findById(infoKey);
		VcodeActivityCog activityCog = activityService.findById(morescanCog.getVcodeActivityKey());
        if (activityCog != null) {
            morescanCog.setVcodeActivityName(activityCog.getVcodeActivityName());
            morescanCog.setSkuName(activityCog.getSkuName());
            if ("0".equals(activityCog.getSkuType())) {
                morescanCog.setSkuType("瓶码");
            } else if ("1".equals(activityCog.getSkuType())) {
                morescanCog.setSkuType("罐码");
            } else if ("2".equals(activityCog.getSkuType())) {
                morescanCog.setSkuType("箱码");
            } else {
                morescanCog.setSkuType("其它");
            }
        }
		model.addAttribute("morescanCog", morescanCog);
        return "vcode/morescan/showMorescanCogView";
	}
	
	/**
	 * 添加记录
	 */
	@RequestMapping("/doMorescanCogAdd")
	public String doMorescanCogAdd(HttpSession session, VcodeActivityMorescanCog morescanCog, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			morescanCog.setCompanyKey(currentUser.getCompanyKey());
			morescanCog.fillFields(currentUser.getUserKey());
			morescanCogService.doMorescanCogAdd(morescanCog);
			model.addAttribute("errMsg", "添加成功");
		} catch (Exception ex) {
			if (ex.getMessage().equals("该活动该时间内已有一码多扫规则！")) {
				model.addAttribute("errMsg", "该活动该时间内已有一码多扫规则！");
			} else {
				model.addAttribute("errMsg", "添加失败");
			}
			ex.printStackTrace();
		}
		return "forward:showMorescanCogList.do";
	}
	
	/**
	 * 修改记录
	 */
	@RequestMapping("/doMorescanCogEdit")
	public String doMorescanCogEdit(HttpSession session, VcodeActivityMorescanCog morescanCog, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			morescanCog.setCompanyKey(currentUser.getCompanyKey());
			morescanCog.fillFields(currentUser.getUserKey());
			morescanCogService.doMorescanCogEdit(morescanCog);
			model.addAttribute("errMsg", "修改成功");
		} catch (Exception ex) {
			if (ex.getMessage().equals("该活动该时间内已有一码多扫规则！")) {
				model.addAttribute("errMsg", "该活动该时间内已有一码多扫规则！");
			} else {
				model.addAttribute("errMsg", "修改失败");
			}
			ex.printStackTrace();
		}
		return "forward:showMorescanCogList.do";
	}
	
	/**
	 * 刪除记录
	 * @param session
	 * @param infoKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/doMorescanCogDelete")
	public String doMorescanCogDelete(HttpSession session, String infoKey, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			String key = StringUtils.isNotBlank(DbContextHolder.getDBType()) ? DbContextHolder.getDBType() : "dataSource";
			// 缓存key
			String cacheKey = CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_MORESCAN_COGS + Constant.DBTSPLIT + key;

			// 清除一码多扫缓存
			CacheUtilNew.removeGroupByKey(cacheKey);
			String vcodeActivityKey = morescanCogService.doMorescanCogDelete(infoKey, currentUser.getUserKey());
			
			// 清除缓存
			if(StringUtils.isNotBlank(vcodeActivityKey)){
				CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey
						.KEY_VCODE_ACTIVITY_MORESCAN_COG + Constant.DBTSPLIT + vcodeActivityKey);
			}
			model.addAttribute("errMsg", "删除成功");
		}catch(Exception e){
			model.addAttribute("errMsg", "删除失败");
			
		}
		return "forward:showMorescanCogList.do";
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
		return morescanCogService.checkBussionName(infoKey, bussionName);
	}
}
