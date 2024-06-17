package com.dbt.framework.datadic.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.datadic.bean.SysDicCategoryInfo;
import com.dbt.framework.datadic.service.SysDicCategoryInfoService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 文件名:SysDicCategoryInfoAction.java <br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 数据字典类型Action<br>
 * 修改人: HaoQi<br>
 * 修改时间：2014-08-19 13:49:05<br>
 * 修改内容：新增<br>
 */
@Controller
@RequestMapping("/sysDicCategoryInfo")
public class SysDicCategoryInfoAction extends BaseAction {

	@Autowired
	private SysDicCategoryInfoService sysDicCategoryInfoService;

	/**
	 * 字典类型列表页面
	 * @param model
	 * @param queryParam
	 * @param pageParam
	 * @return
	 */
	@RequestMapping("/listCategory")
	public String listCategory(Model model, String queryParam, String pageParam) {
		//分页信息
		PageOrderInfo info = new PageOrderInfo(pageParam);

		SysDicCategoryInfo category = new SysDicCategoryInfo(queryParam);

		//组装条件
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("param", info);
		map.put("category", category);

		//获取快戳的列表信息
		List<SysDicCategoryInfo> categoryList = sysDicCategoryInfoService.findPageList(map);

		//总数
		int countAll = sysDicCategoryInfoService.countAll(map);

		//填充Model
		model.addAttribute("showCount", countAll);
		model.addAttribute("resultList", categoryList);
		model.addAttribute("startIndex", info.getStartCount());
		model.addAttribute("countPerPage", info.getPagePerCount());
		model.addAttribute("currentPage", info.getCurrentPage());
	//	model.addAttribute("params", queryParam);
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
        model.addAttribute("queryParam", queryParam);
        model.addAttribute("pageParam", pageParam);

		//返回页面
		return "datadic/category/listCategory";
	}

	/**
	 * 进入添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model){
		return "datadic/category/addCategory";
	}

	/**
	 * 添加类别
	 * @param model
	 * @param category
	 * @return
	 */
	@RequestMapping("/addCategory")
	public String addCategory(HttpSession session, Model model, SysDicCategoryInfo category){
		try {
			if(category != null){
				SysUserBasis sysUserBasis = getUserBasis(session);

				sysDicCategoryInfoService.addCategory(category, sysUserBasis);
			}
			model.addAttribute("refresh", "add_success");
		} catch (Exception e) {
			model.addAttribute("refresh", "add_fail");
			e.printStackTrace();
		}
		return "forward:listCategory.do";
	}

	/**
	 * 进入添加页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Model model, String categoryKey){
		SysDicCategoryInfo category = sysDicCategoryInfoService.findById(categoryKey);
		model.addAttribute("category", category);
		return "datadic/category/editCategory";
	}

	/**
	 * 添加类别
	 * @param model
	 * @param category
	 * @return
	 */
	@RequestMapping("/editCategory")
	public String editCategory(HttpSession session, Model model, SysDicCategoryInfo category){
		try {
			if(category != null){
				SysUserBasis sysUserBasis = getUserBasis(session);

				sysDicCategoryInfoService.editCategory(category, sysUserBasis);
			}
			model.addAttribute("refresh", "edit_success");
		} catch (Exception e) {
			model.addAttribute("refresh", "edit_fail");
			e.printStackTrace();
		}
		return "forward:listCategory.do";
	}

	/**
	 * 删除类型
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteCategory")
	public String deleteCategory(HttpSession session, Model model, String categoryKey){
		try {
			if(categoryKey != null){
				SysUserBasis sysUserBasis = getUserBasis(session);

				sysDicCategoryInfoService.deleteCategoryById(categoryKey, sysUserBasis);
			}
			model.addAttribute("refresh", "delete_success");
		} catch (Exception e) {
			model.addAttribute("refresh", "delete_fail");
			e.printStackTrace();
		}
		return "forward:listCategory.do";
	}

	/**
	 * 查看类型明细
	 * @param model
	 * @return
	 */
	@RequestMapping("/viewCategory")
	public String viewCategory(Model model, String categoryKey){
		if(categoryKey != null){
			SysDicCategoryInfo category = sysDicCategoryInfoService.findById(categoryKey);
			model.addAttribute("category", category);
		}

		return "datadic/category/viewCategory";
	}

}
