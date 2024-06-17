package com.dbt.platform.taoeasteregg.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.taoeasteregg.bean.VpsVcodeTaoEasterEggCog;
import com.dbt.platform.taoeasteregg.service.VpsVcodeTaoEasterEggCogService;

/**
 * 淘彩蛋
 */
@Controller
@RequestMapping("/taoEasterEgg")
public class VpsVcodeTaoEasterEggCogAction extends BaseAction {

	@Autowired
	private VpsVcodeTaoEasterEggCogService easterEggCogService;
	@Autowired
	private VcodeActivityService activityService;

	/**
	 * 淘彩蛋规则列表
	 */
	@RequestMapping("/showTaoEasterEggCogList")
	public String showTaoEasterEggCogList(HttpSession session, 
	                        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			VpsVcodeTaoEasterEggCog queryBean = new VpsVcodeTaoEasterEggCog(queryParam);
			queryBean.setCurrDate(DateUtil.getDate());
			List<VpsVcodeTaoEasterEggCog> resultList = easterEggCogService.queryForLst(queryBean, pageInfo);
			int countResult = easterEggCogService.queryForCount(queryBean);
			
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/taoEasterEgg/showTaoEasterEggCogList";
	}

	/**
	 * 添加页面
	 */
	@RequestMapping("/showTaoEasterEggCogAdd")
	public String showTaoEasterEggCogAdd(HttpSession session, Model model) {
		try {
			// 查询可配置淘彩蛋的活动
			model.addAttribute("activityList", activityService.queryValidActivity());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vcode/taoEasterEgg/showTaoEasterEggCogAdd";
	}

	/**
	 * 编辑页面
	 */
	@RequestMapping("/showTaoEasterEggCogEdit")
	public String showVcodeActivityEdit(HttpSession session, String infoKey, Model model) {
		try {
			//查询当前规则
			VpsVcodeTaoEasterEggCog easterEggCog = easterEggCogService.findById(infoKey);
			if(null != easterEggCog){
				Map<String, Object> map = new HashMap<>();
				map.put("ruleKey", easterEggCog.getInfoKey());
				// 查询可配置淘彩蛋的活动
	            model.addAttribute("activityList", activityService.queryValidActivity());
			}
			model.addAttribute("easterEggCog", easterEggCog);
			model.addAttribute("pageFlag", "edit");
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vcode/taoEasterEgg/showTaoEasterEggCogEdit";
	}
	
	/**
	 * 查看页面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/showTaoEasterEggCogView")
	public String showTaoEasterEggCogView(HttpSession session, String infoKey, Model model) {
		try {
			//查询当前规则
			VpsVcodeTaoEasterEggCog easterEggCog = easterEggCogService.findById(infoKey);
			if(null != easterEggCog){
				Map<String, Object> map = new HashMap<>();
				map.put("ruleKey", easterEggCog.getInfoKey());
				// 查询可配置淘彩蛋的活动
	            model.addAttribute("activityList", activityService.queryValidActivity());
			}
			model.addAttribute("easterEggCog", easterEggCog);
			model.addAttribute("pageFlag", "view");
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vcode/taoEasterEgg/showTaoEasterEggCogEdit";
	}
	
	/**
	 * 创建规则
	 */
	@RequestMapping("/doTaoEasterEggCogAdd")
	public String doVcodeActivityAdd(HttpSession session, VpsVcodeTaoEasterEggCog easterEggCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			easterEggCog.fillFields(currentUser.getUserKey());
			easterEggCogService.create(easterEggCog);
			model.addAttribute("errMsg", "添加成功");
		}catch (Exception ex) {
			model.addAttribute("errMsg", "添加失败");
		    log.error(ex.getMessage(), ex);
        }
		return "forward:showTaoEasterEggCogList.do";
	}

	/**
	 * 修改活动
	 */
	@RequestMapping("/doTaoEasterEggCogEdit")
	public String doVcodeActivityEdit(HttpSession session, VpsVcodeTaoEasterEggCog easterEggCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			easterEggCog.fillFields(currentUser.getUserKey());
			easterEggCogService.update(easterEggCog);
			
			// 删除缓存
			String key = CacheUtilNew.cacheKey.vodeActivityKey
					.KEY_TAO_EASTEREGG_COG + Constant.DBTSPLIT + DateUtil.getDate();
			CacheUtilNew.removeGroupByKey(key);
			model.addAttribute("errMsg", "编辑成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "编辑失败");
		    log.error(ex.getMessage(), ex);
		}
		return "forward:showTaoEasterEggCogList.do";
	}
	
	/**
	 * 删除活动
	 */
	@RequestMapping("/doTaoEasterEggCogDel")
	public String doTaoEasterEggCogDel(HttpSession session, String infoKey, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			easterEggCogService.delete(infoKey, currentUser);
			model.addAttribute("errMsg", "删除成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "删除失败");
			log.error(ex.getMessage(), ex);
		}
		return "forward:showTaoEasterEggCogList.do";
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
		return easterEggCogService.checkBussionName(infoKey, bussionName);
	}
	
	/**
	 * 检验活动是否可配置
	 * @param activityKeys
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkActivityForTaoEasterEgg")
	public String checkActivityForLadder(String activityKeys, String infoKey, String startDate, String endDate){
	    String errMsg = "";
	    try {
	        easterEggCogService.validActivityConflict(infoKey, activityKeys, startDate, endDate); 
        } catch (BusinessException e) {
            errMsg = e.getMessage();
        }
	    Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("errMsg", errMsg);
		return JSON.toJSONString(resultMap);
	}
}
