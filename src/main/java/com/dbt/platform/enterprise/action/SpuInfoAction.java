/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 上午11:21:52 by hanshimeng create
 * </pre>
 */
package com.dbt.platform.enterprise.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.enterprise.bean.SpuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.enterprise.service.SpuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/spuInfo")
public class SpuInfoAction extends BaseAction{

	@Autowired
	private SpuInfoService spuInfoService;
	@Autowired
	private SkuInfoService skuInfoService;
	
	/**
	 * spu列表
	 */
	@RequestMapping("/showSpuInfoList")
	public String showSpuInfoList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {
		    SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            SpuInfo queryBean = new SpuInfo(queryParam);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
			List<SpuInfo> resultList = spuInfoService.queryForLst(queryBean, pageInfo);
			int countResult = spuInfoService.queryForCount(queryBean);
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
		} catch (Exception ex) {
		    log.error("spu查询失败", ex);
		}
		return "spuinfo/showSpuInfoList";
	}
	
	/**
	 * 跳转spu新增页面
	 */
	@RequestMapping("/showSpuInfoAdd")
	public String showSpuInfoAdd(HttpSession session, Model model){
		model.addAttribute("skuList", skuInfoService.queryListByMap(false, null));
		return "spuinfo/showSpuInfoAdd";
	}
	
	/**
	 * 跳转spu编辑页面
	 */
	@RequestMapping("/showSpuInfoEdit")
	public String showSpuInfoEdit(HttpSession session, String spuInfoKey, Model model){
		SpuInfo spuInfo = spuInfoService.findById(spuInfoKey);
		model.addAttribute("spuInfo", spuInfo);
		model.addAttribute("skuList", skuInfoService.queryListByMap(false, spuInfo.getSpuInfoKey()));
		return "spuinfo/showSpuInfoEdit";
	}
	
	/**
	 * 添加spu信息
	 */
	@RequestMapping("/doSpuInfoAdd")
	public String doSpuInfoAdd(HttpSession session, SpuInfo spuInfo,Model model){
		try{
		    SysUserBasis currentUser = this.getUserBasis(session);
		    spuInfo.setCompanyKey(currentUser.getCompanyKey());
		    spuInfo.fillFields(currentUser.getUserKey());
		    spuInfoService.insertSpuInfo(spuInfo);
			model.addAttribute("errMsg", "新增成功");
		} catch (BusinessException ex) {
		    model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
		} catch (Exception ex) {
			model.addAttribute("errMsg", "新增失败");
			log.error("Spu新增失败", ex);
		}
		return "forward:showSpuInfoList.do"; 
	}
	
	/**
	 * 编辑spu信息
	 */
	@RequestMapping("/doSpuInfoEdit")
	public String doSpuInfoEdit(HttpSession session, SpuInfo spuInfo, Model model){
		try{
			 SysUserBasis currentUser = this.getUserBasis(session);
		    spuInfo.setCompanyKey(currentUser.getCompanyKey());
		    spuInfo.fillFields(currentUser.getUserKey());
		    spuInfoService.updateSpuInfo(spuInfo);
			model.addAttribute("errMsg", "编辑成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "编辑失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("spu编辑失败", ex);
        }
        return "forward:showSpuInfoList.do"; 
	} 
	
	/**
	 * 删除spu信息
	 */
	@RequestMapping("/doSpuInfoDelete")
	public String doSpuInfoDelete(HttpSession session, String spuInfoKey,Model model){
		try{
	        spuInfoService.deleteSpuInfo(spuInfoKey);
	        model.addAttribute("errMsg", "删除成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "删除失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("spu删除失败", ex);
        }
        return "forward:showSpuInfoList.do"; 
	} 
	
	/**
	 * 是否重复 0否，1是
	 * @param spuInfoKey
	 * @param spuName
	 * @return
	 */
	public String checkBussionName(String spuInfoKey, String spuName){
		return spuInfoService.checkBussionName("spuInfo", "spu_info_key", spuInfoKey, "spu_name", spuName);
	}
}
