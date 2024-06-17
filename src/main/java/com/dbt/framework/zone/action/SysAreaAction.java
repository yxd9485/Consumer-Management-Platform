package com.dbt.framework.zone.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;

/**
* 文件名：ZoneAction
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述: 区域码表
* 修改人: FuZhongYue
* 修改时间：2014-02-19 11:31:45
* 修改内容：新增
*/

@Controller
@RequestMapping("/sysArea")
public class SysAreaAction extends BaseAction {

	@Autowired
	private SysAreaService sysAreaService;
	
	@RequestMapping("/findArea")
	public @ResponseBody String findZoneByParentId(String id, boolean isProjectArea){
		List<SysAreaM> resultList = this.sysAreaService.findByParentIdForProject(id, isProjectArea);
	    return JSON.toJSONString(resultList);
	}
	
	@RequestMapping("/findAreaByCurId")
	public @ResponseBody String findZoneByCurId(String id, boolean isProjectArea){
	    return this.sysAreaService.findZoneByCurId(id, isProjectArea);
	}
	
	@RequestMapping("/findAreaByParentId")
	public @ResponseBody String findAreaByParentId(String parentId){
	    return JSON.toJSONString(this.sysAreaService.findByParentId(parentId));
	}
	
}
