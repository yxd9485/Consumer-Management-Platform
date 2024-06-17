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
import com.dbt.platform.mn.service.MnAgencyService;
import com.dbt.platform.mn.service.MnDepartmentService;

/**
 * 	经销商Action
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/mnAgency")
public class MnAgencyAction extends BaseAction{
	@Autowired
	private MnAgencyService mnAgencyService; 	
	
	/**
    * 	查询经销商数据
    */
	@ResponseBody
	@RequestMapping("/queryListByMap")
	public String queryListByMap(HttpSession session, String departmentId, String agency_type) {
		return JSON.toJSONString(mnAgencyService.queryListByMap(departmentId, agency_type));
	}

}
