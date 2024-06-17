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
package com.dbt.platform.org.action;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.platform.mn.service.MnDepartmentService;
import com.dbt.platform.org.service.OrganizationService;

/**
 * 组织机构Action
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/organization")
public class OrganizationAction extends BaseAction{
	@Autowired
	private OrganizationService organizationService; 	
	
	/**
    * 	根据大区查询二级办
    */
	@ResponseBody
	@RequestMapping("/queryListByBigRegionName")
	public String queryListByBigRegionName(HttpSession session, String bigRegionName) {
		return JSON.toJSONString(organizationService.queryListByBigRegionName(bigRegionName));
	}


	/**
	 * 	根据父ID查询下级
	 */
	@ResponseBody
	@RequestMapping("/queryOrganizationByPid")
	public String queryOperationByPid(HttpSession session, long pid ,int level) {
		return JSON.toJSONString(organizationService.queryListByMap(pid, level));
	}

}
