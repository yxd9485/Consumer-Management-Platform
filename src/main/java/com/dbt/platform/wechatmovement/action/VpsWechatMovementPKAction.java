package com.dbt.platform.wechatmovement.action;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.platform.wechatmovement.service.VpsWechatMovementUserDetailService;

/**
 * 微信运动PK赛Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/wechatMovementPK")
public class VpsWechatMovementPKAction extends BaseAction{

	@Autowired
	private VpsWechatMovementUserDetailService wechatMovementUserDetailService;
	
	/**
	 * 手动处理PK赛人员匹配
	 * https://vct.vjifen.com/vjifenPlatform/wechatMovementPK/executePkRelation.do?periodsKey=20200821&projectServerName=henanpz
	 */
	@ResponseBody
	@RequestMapping(value="/executePkRelation", method=RequestMethod.GET)
	public String executePkRelation(HttpSession session, String periodsKey, String projectServerName, Model model){
		if(StringUtils.isBlank(projectServerName)) {
			return "projectServerName 不能为空";
		}
		DbContextHolder.setDBType(projectServerName);
		
		String msg = null;
		try{
			wechatMovementUserDetailService.executePkRelation(periodsKey);
			msg = periodsKey + "期PK赛匹配成功！";
		}catch(Exception e){
			msg = periodsKey + "期PK赛匹配异常：" + e;
			log.error(e);
		}
		return msg;
	}
	
	/**
	 * 手动处理PK赛结果
	 * https://vct.vjifen.com/vjifenPlatform/wechatMovementPK/executePkResult.do?periodsKey=20200821&projectServerName=henanpz
	 */
	@ResponseBody
	@RequestMapping(value="/executePkResult", method=RequestMethod.GET)
	public String executePkResult(HttpSession session, String periodsKey, String projectServerName, Model model){
		if(StringUtils.isBlank(projectServerName)) {
			return "projectServerName 不能为空";
		}
		DbContextHolder.setDBType(projectServerName);
		
		String msg = null;
		try{
			wechatMovementUserDetailService.executePkResult(periodsKey);
			msg = periodsKey + "期PK赛结果处理成功！";
		}catch(Exception e){
			msg = periodsKey + "期PK赛结果处理异常：" + e;
			log.error(e);
		}
		return msg;
	}
}
