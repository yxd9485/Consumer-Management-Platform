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
package com.dbt.platform.mn.action;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.platform.mn.service.MnDepartmentService;

/**
 * 组织机构Action
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/mnDepartment")
public class MnDepartmentAction extends BaseAction{
	@Autowired
	private MnDepartmentService mnDepartmentService; 	
	
	/**
    * 根据父ID查询下级
    */
	@ResponseBody
	@RequestMapping("/queryListByPid")
	public String queryListByPid(HttpSession session, int pid) {
		return JSON.toJSONString(mnDepartmentService.queryListByMap(pid, -1));
	}


	/**
	 * 根据父ID查询下级
	 */
	@ResponseBody
	@RequestMapping("/queryDepartmentInfoByPid")
	public String queryDepartmentInfoByPid(HttpSession session, long pid ,int level) {
		return JSON.toJSONString(mnDepartmentService.queryListByMap(pid, level));
	}

}
