/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午3:24:17 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.activity.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaForAreaCode;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 热区配置Action
 */
@Controller
@RequestMapping("/vcodeActivityHotArea")
public class VcodeActivityHotAreaCogAction extends BaseAction{
    protected Log log = LogFactory.getLog(getClass());
	@Autowired
	private VcodeActivityHotAreaCogService hotAreaCogService;
	
	/**
	 * 热区配置列表
	 */
	@RequestMapping("/showHotAreaList")
	public String showHotAreaList(HttpSession session, String queryParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			VcodeActivityHotAreaForAreaCode queryBean = new VcodeActivityHotAreaForAreaCode(queryParam);
			// 获取所有热区配置
			List<VcodeActivityHotAreaForAreaCode> hotAreaCogLst = hotAreaCogService.queryAllHotArea(queryBean);
			String areaCode = "";
			areaCode = StringUtils.isNotBlank(queryBean.getCountyCode()) 
					? queryBean.getCountyCode() : StringUtils.isNotBlank(queryBean.getCityCode()) 
							? queryBean.getCityCode() : queryBean.getProvinceCode();
			// 页面传参
			model.addAttribute("areaCode", areaCode);
            model.addAttribute("queryParam", queryParam);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", hotAreaCogLst);
		} catch (Exception ex) {
            log.error(ex);
		}
		return "vcode/hotarea/showHotAreaCogList";
	}
	
	/**
	 * 添加热区配置
	 */
	@RequestMapping("/showHotAreaCogAdd")
	public String showHotAreaCogAdd(HttpSession session, String areaCode, Model model) {
	    try {
	        String areaName = "";
	        List<SysAreaM> areaLst = new ArrayList<>();
	        if (StringUtils.isBlank(areaCode)) {
	            areaLst = hotAreaCogService.queryAreaList();
	            
	        // 否则，添加指定区域下的规则
	        } else {
	            areaName = hotAreaCogService.getAreaNameByCode(areaCode);
	        }
	        Map<String, String> localCity = hotAreaCogService.getFinalCityName(areaName);
	        
	        model.addAttribute("areaLst", areaLst);
	        model.addAttribute("areaCode", areaCode);
	        model.addAttribute("areaName", areaName);
	        model.addAttribute("firstCity", localCity.get("firstCity"));
	        model.addAttribute("secondCity", localCity.get("secondCity"));
	        model.addAttribute("currentUser", this.getUserBasis(session));
	        
	    } catch (Exception ex) {
            log.error(ex);
	    }
	    return "vcode/hotarea/showHotAreaCogAdd";
	}
    
    /**
     * 添加热区配置
     */
    @RequestMapping("/doHotAreaCogAdd")
    public String doHotAreaCogAdd(HttpSession session, VcodeActivityHotAreaCog hotAreaCog, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            hotAreaCog.setHotAreaKey(UUID.randomUUID().toString());
            hotAreaCog.fillFields(currentUser.getUserKey());
            hotAreaCogService.addRebateRuleCog(hotAreaCog);
            model.addAttribute("flag", "add_success");
            
        } catch (Exception ex) {
            model.addAttribute("flag", "add_fail");
            log.error(ex);
        }
        return "forward:showHotAreaList.do";
    }
	
	/**
	 * 修改热区配置
	 */
	@RequestMapping("/showHotAreaCogEdit")
	public String showHotAreaCogEdit(HttpSession session, String hotAreaKey, String queryParam, Model model) {
	    try {
	        // 查询当前区域配置
	    	VcodeActivityHotAreaCog hotAreaCog = hotAreaCogService.findById(hotAreaKey);
	    	hotAreaCog.setAreaName(hotAreaCogService.getAreaNameByCode(hotAreaCog.getAreaCode()));
            Map<String, String> localCity = hotAreaCogService.getFinalCityName(hotAreaCog.getAreaName());
	    	
	    	model.addAttribute("hotAreaCog", hotAreaCog);
	    	model.addAttribute("queryParam", queryParam);
	    	model.addAttribute("firstCity", localCity.get("firstCity"));
	    	model.addAttribute("secondCity", localCity.get("secondCity"));
	    	model.addAttribute("currentUser", this.getUserBasis(session));
	    	
	    } catch (Exception ex) {
            log.error(ex);
	    }
	    return "vcode/hotarea/showHotAreaCogEdit";
	}
    
    /**
     * 修改热区配置
     */
    @RequestMapping("/doHotAreaCogEdit")
    public String doHotAreaCogEdit(HttpSession session, VcodeActivityHotAreaCog hotAreaCog, Model model) {
        try {
            hotAreaCogService.updateHotAreaCog(hotAreaCog);
            model.addAttribute("flag", "edit_success");
        } catch (Exception ex) {
            model.addAttribute("flag", "edit_fail");
            log.error(ex);
        }
        return "forward:showHotAreaList.do";
    }
    
    /**
     * 修改热区配置
     */
    @RequestMapping("/showHotAreaCogView")
    public String showHotAreaCogView(HttpSession session, String hotAreaKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            
            // 查询当前区域配置
            VcodeActivityHotAreaCog hotAreaCog = hotAreaCogService.findById(hotAreaKey);
            hotAreaCog.setAreaName(hotAreaCogService.getAreaNameByCode(hotAreaCog.getAreaCode()));
            Map<String, String> localCity = hotAreaCogService.getFinalCityName(hotAreaCog.getAreaName());
            
            // 页面传参
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("hotAreaCog", hotAreaCog);
            model.addAttribute("firstCity", localCity.get("firstCity"));
            model.addAttribute("secondCity", localCity.get("secondCity"));
            
        } catch (Exception ex) {
            log.error(ex);
        }
        return "vcode/hotarea/showHotAreaCogView";
    }
	
	/**
	 * 删除热区配置
	 */
	@RequestMapping("/doHotAreaCogDelete")
	public String doHotAreaCogDelete(HttpSession session, String hotAreaKey, Model model) {
	    try {
            SysUserBasis currentUser = this.getUserBasis(session);
	        if (hotAreaCogService.deleteHotAreaCogById(
	                hotAreaKey, currentUser.getUserKey(), model)) {
	            model.addAttribute("flag", "is_delete");
	        }
	    } catch (Exception ex) {
	        model.addAttribute("flag", "xx_delete");
            log.error(ex);
	    }
	    return "forward:showHotAreaList.do";
	}

    /**
     * 删除区域
     */
    @RequestMapping("/doHotAreaForAreaCodeDelete")
    public String doHotAreaForAreaCodeDelete(HttpSession session, String areaCode, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            hotAreaCogService.deleteHotAreaByAreaCode(areaCode, currentUser.getUserKey(), model);
            
        } catch (Exception ex) {
            model.addAttribute("flag", "xx_delete");
            log.error(ex);
        }
        return "forward:showHotAreaList.do";
    }
    
    /**
    * 根据地区areaCode获取热区List
    */
   @RequestMapping("/findHotAreaListByAreaCode")
   public @ResponseBody String findHotAreaListByAreaCode(HttpSession session, String areaCode, Model model) {
	   return JSON.toJSONString(this.hotAreaCogService.findHotAreaListByAreaCode(areaCode));
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
		return hotAreaCogService.checkBussionName(infoKey, bussionName);
	}
}
