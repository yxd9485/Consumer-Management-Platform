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
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.wechatmovement.service.VpsWechatMovementUserDetailService;

/**
 * 用户参与微信运动详情Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/wechatMovementUserDetail")
public class VpsWechatMovementUserDetailAction extends BaseAction{

	@Autowired
	private VpsWechatMovementUserDetailService wechatMovementUserDetailService;
	
	/**
	 * 手动处理某一期的比赛结果
	 * http://IP:port/vjifenPlatform/wechatMovementUserDetail/executeCompetitionResult.do?periodsKey=20190114&projectServerName=henanpz
	 */
	@ResponseBody
	@RequestMapping(value="/executeCompetitionResult", method=RequestMethod.GET)
	public String executeCompetitionResult(HttpSession session, String periodsKey, String projectServerName, Model model){
		if(StringUtils.isBlank(projectServerName)) {
			return "projectServerName 不能为空";
		}
		DbContextHolder.setDBType(projectServerName);
		
		String msg = null;
		try{
			wechatMovementUserDetailService.executeCompetitionResult(periodsKey);
			String lua="redis.call('del',unpack(redis.call('keys','*keyWechatmovementUserDetail*')))";
	        RedisApiUtil.getInstance().evalEx(lua ,0, "");
			msg = periodsKey + "期比赛结果处理成功！";
		}catch(Exception e){
			msg = periodsKey + "期比赛结果处理异常：" + e;
			log.error(e);
		}
		return msg;
	}
	
	/**
	 * 手动处理PK匹配job
	 * http://IP:port/vjifenPlatform/wechatMovementUserDetail/executePkRelation.do?periodsKey=20190114&projectServerName=henanpz
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
			msg = periodsKey + "PK匹配结果处理成功！";
		}catch(Exception e){
			msg = periodsKey + "PK匹配结果处理异常：" + e;
			log.error(e);
		}
		return msg;
	}
	
	/**
	 * 手动处理PK结果job
	 * http://IP:port/vjifenPlatform/wechatMovementUserDetail/executePkResult.do?periodsKey=20190114&projectServerName=henanpz
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
			msg = periodsKey + "PK结果结果处理成功！";
		}catch(Exception e){
			msg = periodsKey + "PK结果结果处理异常：" + e;
			log.error(e);
		}
		return msg;
	}
}
