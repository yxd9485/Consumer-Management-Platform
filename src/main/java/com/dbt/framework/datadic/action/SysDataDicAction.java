package com.dbt.framework.datadic.action;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.datadic.bean.SysDataDic;
import com.dbt.framework.datadic.bean.SysDicCategoryInfo;
import com.dbt.framework.datadic.service.SysDataDicService;
import com.dbt.framework.datadic.service.SysDicCategoryInfoService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 文件名:ISysFuncDao.java <br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 字典数据Action<br>
 * 修改人: HaoQi<br>
 * 修改时间：2014-08-19 13:49:05<br>
 * 修改内容：新增<br>
 */
@Controller
@RequestMapping("/sysDataDic")
public class SysDataDicAction extends BaseAction {

	@Autowired
	private SysDataDicService dataDicService;
	@Autowired
	private SysDicCategoryInfoService dicCategoryInfoService;

	/**
	 * 数据字典管理页面
	 *
	 * @param HttpSession
	 * @param pageParam
	 * @param Model
	 * @return string
	 */
	@RequestMapping("/dataDicList")
	public String dataDicList(String pageParam, String queryParam, HttpSession session, Model model) {
		try {
			if(!StringUtils.isEmpty(queryParam)){
				queryParam = URLDecoder.decode(queryParam, "UTF-8");
			}

			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo info = new PageOrderInfo(pageParam);
			SysDataDic sysDataDic = new SysDataDic(queryParam);
			if("2".equals(currentUser.getRoleKey())){
				// 一等奖设置菜单
				sysDataDic.setCategoryCode("major_info");
			}
            //组装条件
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("param", info);
			List<SysDataDic> list = this.dataDicService.findDataDicList(info, sysDataDic);// 查询数据字典列表
			int countAll = dataDicService.countDataDicList(sysDataDic);// 统计数据字典总数
			List<SysDicCategoryInfo> categoryList = this.dicCategoryInfoService.findAll(null);

			model.addAttribute("categoryList", categoryList);
			model.addAttribute("dataDicList", list);
			model.addAttribute("showCount", countAll);
			model.addAttribute("startIndex", info.getStartCount());
			model.addAttribute("countPerPage", info.getPagePerCount());
			model.addAttribute("currentPage", info.getCurrentPage());
		    //	model.addAttribute("params", queryParam);
            model.addAttribute("orderCol", info.getOrderCol());
            model.addAttribute("orderType", info.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "datadic/data/dataDicList";
	}

	/**
	 * 数据字典添加页面
	 *
	 * @param Model
	 * @param HttpSession
	 * @return string
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model, HttpSession session) {

        Map<String, Object> map = new HashMap<String, Object>();
        PageOrderInfo info = new PageOrderInfo("");
        map.put("param", info);
		List<SysDicCategoryInfo> categoryList = this.dicCategoryInfoService.findAll(map);
		model.addAttribute("categoryList", categoryList);
		return "datadic/data/addDataDic";
	}

	/**
	 * 数据字典保存
	 *
	 * @param Model
	 * @param HttpSession
	 * @return string
	 */
	@RequestMapping("/saveDataDic")
	public String saveDataDic(Model model, SysDataDic sysDataDic, HttpSession session) {
	    try {
	        SysUserBasis sysUser = getUserBasis(session);
	        sysDataDic.setDataDicKey(callUUID());
	        sysDataDic.setCreateTime(DateUtil.getDateTime());
	        sysDataDic.setCreateUser(sysUser.getUserKey());
	        this.dataDicService.saveDataDic(sysDataDic);

	        model.addAttribute("refresh", "add_success");
        } catch (Exception e) {
            model.addAttribute("errMsg", e.getMessage());
        }
		return "forward:dataDicList.do";
	}

	/**
	 * 数据字典修改页面
	 *
	 * @param Model
	 * @param HttpSession
	 * @return string
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Model model, String dataDicKey, String queryParam) {
		SysDataDic sysDataDic = this.dataDicService.findById(dataDicKey);
        Map<String, Object> map = new HashMap<String, Object>();
        PageOrderInfo info = new PageOrderInfo("");
        map.put("param", info);
		List<SysDicCategoryInfo> categoryList = this.dicCategoryInfoService.findAll(map);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("sysDataDic", sysDataDic);
		model.addAttribute("queryParam", queryParam);
		return "datadic/data/editDataDic";
	}

	/**
	 * 数据字典修改功能
	 *
	 * @param Model
	 * @param HttpSession
	 * @return string
	 */
	@RequestMapping("/updateDataDic")
	public String updateDataDic(Model model, SysDataDic sysDataDic, HttpSession session,
			String queryParam) {
		SysUserBasis sysUser = getUserBasis(session);
		sysDataDic.setUpdateTime(DateUtil.getDateTime());
		sysDataDic.setUpdateUser(sysUser.getUserKey());
		this.dataDicService.updateDataDic(sysDataDic);
		model.addAttribute("refresh", "edit");
		model.addAttribute("params", queryParam);
		return "forward:dataDicList.do";
	}

	/**
	 * 数据字典修改删除
	 *
	 * @param Model
	 * @param dataDicKey
	 * @return string
	 */
	@RequestMapping("/deleteDataDic")
	public String deleteDataDic(Model model, String dataDicKey) {
		if (StringUtils.isNotEmpty(dataDicKey)) {
			this.dataDicService.deleteDataDic(dataDicKey);
		}
		model.addAttribute("refresh", "delete");
		return "forward:dataDicList.do";
	}
}
