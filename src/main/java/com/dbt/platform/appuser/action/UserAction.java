package com.dbt.platform.appuser.action;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.GeocodingUtil;
import com.dbt.platform.appuser.bean.VpsConsumerAccountInfo;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.service.VpsConsumerUserInfoService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.service.ExchangeService;

/**
 * 用户 UserAction
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2017年3月13日 </br>
 */
@Controller
@RequestMapping("/user")
public class UserAction extends BaseAction {

	@Autowired
	private VpsConsumerUserInfoService vpsConsumerUserInfoService;

	@Autowired
	private ExchangeService exchangeService;
	/**
	 * 查询用户信息List
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showUserInfoList")
	public String showUserInfoList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsConsumerUserInfo queryBean = new VpsConsumerUserInfo(queryParam);
            if (StringUtils.isBlank(queryParam)) {
                queryBean.setIsLuckyUser("1");
            }
			List<VpsConsumerUserInfo> userInfoList = 
			        vpsConsumerUserInfoService.findUserInfoList(queryBean, pageInfo);
			int countResult = vpsConsumerUserInfoService.findUserInfoCount(queryBean);
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", userInfoList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "userinfo/showUserInfoList";
	}
	
	/**
	 * 更新幸运用户信息
	 * @param </br> 
	 * @return String </br>
	 */
    @ResponseBody
	@RequestMapping("/updateIsLuckyUser")
	public String updateIsLuckyUser(HttpSession session, VpsConsumerUserInfo queryBean, 
			                        String userKey, String luckLevel, String pageParam, Model model){
        String errMsg = "更改失败!";
		try{
		    if (vpsConsumerUserInfoService.updateIsLuckyUser(userKey, luckLevel)){
		        errMsg = "更改成功!";   
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return errMsg;
	}

	/**
	 * 查询用户信息List
	 *
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showUserVpointsList")
	public String showUserVpointsList(HttpSession session, String pageParam, Model model,String queryParam) {
		try {
           SysUserBasis currentUser = this.getUserBasis(session);
            HashMap<String, Object> map = new HashMap<String, Object>();
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);

			VpsConsumerAccountInfo remindInfo= new VpsConsumerAccountInfo(queryParam);
			if (StringUtils.isEmpty(String.valueOf(pageInfo.getStartCount()))) {
				pageInfo.setStartCount(0);
			}
			if (StringUtils.isEmpty(String.valueOf(pageInfo.getPagePerCount()))) {
				pageInfo.setPagePerCount(15);
			}
			int countResult = 0;
			List<VpsConsumerAccountInfo> userInfoList = new ArrayList<VpsConsumerAccountInfo>();
			
			if(StringUtils.isNotBlank(remindInfo.getInActivities())) {
				remindInfo.setAddress(remindInfo.getInActivities());
				map.put("pageInfo", pageInfo);
				map.put("remindInfo", remindInfo);
	            map.put("inActivities",remindInfo.getInActivities());
				userInfoList = vpsConsumerUserInfoService.findUserVpoints(map);
				countResult = vpsConsumerUserInfoService.findUserVpointsCount(map);
			}
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", userInfoList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("orderCol", pageInfo.getOrderCol());
			model.addAttribute("orderType", pageInfo.getOrderType());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "userinfo/showUserVpointsList";
	}

	/**
	 * 查询用户信息List
	 *
	 * @param session
	 * @param
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showUserScanList")
	public String showUserScanList(HttpSession session, String userKey, String pageParam, Model model,String tabsFlag) {
		SysUserBasis currentUser = this.getUserBasis(session);
		HashMap<String, Object> map = new HashMap<String, Object>();
		PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
		if (StringUtils.isBlank(tabsFlag)) {
			tabsFlag = "0";
		}
		if (StringUtils.isEmpty(String.valueOf(pageInfo.getStartCount()))) {
			pageInfo.setStartCount(0);
		}
		if (StringUtils.isEmpty(String.valueOf(pageInfo.getPagePerCount()))) {
			pageInfo.setPagePerCount(15);
		}
        String userKeys ="";
        if(userKey.split(",").length>1){
            userKeys=userKey.split(",")[0];
        }else{
            userKeys =userKey.replace(",","");
        }
        VpsConsumerAccountInfo remindInfo = new VpsConsumerAccountInfo();
		remindInfo.setUserKey(userKeys);
		map.put("pageInfo", pageInfo);
		map.put("remindInfo", remindInfo);
		VpsConsumerUserInfo  user=   vpsConsumerUserInfoService.queryUserInfoByUserkey(userKeys);
		if("0".equals(tabsFlag)){


		List<VpsConsumerAccountInfo> resultList = vpsConsumerUserInfoService.findUserScanList(map);
		int countResult = vpsConsumerUserInfoService.countUserScanList(userKeys);
			model.addAttribute("resultList",resultList);
			model.addAttribute("showCount", countResult);
		}
		else{
			VpointsExchangeLog queryBean = new VpointsExchangeLog();
			 queryBean.setUserKey(userKeys);
			 queryBean.setBrandId("");
			 queryBean.setBrandIdLst(null);
			List<VpointsExchangeLog> resultList = exchangeService.queryForExpressLst(queryBean, pageInfo);
			int countResult = exchangeService.queryForExpressCount(queryBean);
			model.addAttribute("resultList",resultList);
			model.addAttribute("showCount", countResult);

		}
		model.addAttribute("user",user);
		model.addAttribute("startIndex", pageInfo.getStartCount());
		model.addAttribute("countPerPage", pageInfo.getPagePerCount());
		model.addAttribute("currentPage", pageInfo.getCurrentPage());
		model.addAttribute("orderCol", pageInfo.getOrderCol());
		model.addAttribute("orderType", pageInfo.getOrderType());
		model.addAttribute("tabsFlag", tabsFlag);
		return "userinfo/showUserScanList";
	}

	/**
	 * 查询用户信息List
	 *
	 * @param session
	 * @param
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/exportUserVpointsList")
	public void exportUserVpointsList(HttpSession session, String pageParam, HttpServletResponse response, Model
			model, String userKey, String type, String queryParam, HttpServletRequest request){
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			HashMap<String, Object> map = new HashMap<String, Object>();
			String fileName="";
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
	    	 if (!StringUtils.isEmpty(String.valueOf(pageInfo.getStartCount()))) {
				pageInfo.setStartCount(1);
			}
			if (!StringUtils.isEmpty(String.valueOf(pageInfo.getPagePerCount()))) {
				pageInfo.setPagePerCount(15);
			}
			pageInfo.setPagePerCount(0);

			if("0".equals(type)){
				VpsConsumerAccountInfo remindInfo= new VpsConsumerAccountInfo(queryParam);
				map.put("remindInfo", remindInfo);
				fileName= URLEncoder.encode("用户积分列表", "UTF-8") + DateUtil.getDate() + ".xls";
			}
			if("1".equals(type)){
				VpsConsumerAccountInfo remindInfo = new VpsConsumerAccountInfo();
				remindInfo.setUserKey(userKey);
				map.put("remindInfo", remindInfo);
				fileName= URLEncoder.encode("获取积分详情", "UTF-8") + DateUtil.getDate() + ".xls";
			}
			if("2".equals(type)){
				VpointsExchangeLog queryBean = new VpointsExchangeLog();
				queryBean.setUserKey(userKey);
				queryBean.setBrandId("");
				queryBean.setBrandIdLst(null);
				map.put("queryBean", queryBean);
				fileName= URLEncoder.encode("兑换积分详情", "UTF-8") + DateUtil.getDate() + ".xls";
			}
			if("1".equals(type)||"2".equals(type)) {
				VpsConsumerUserInfo user = vpsConsumerUserInfoService.queryUserInfoByUserkey(userKey);
				String nikeName = StringUtils.isNotEmpty(user.getNickName()) ? user.getNickName() : "无";
				String phone = StringUtils.isNotEmpty(user.getPhoneNumber()) ? user.getPhoneNumber() : "无";
				map.put("title","userKey: "+userKey+",昵称: "+nikeName+",联系电话:"+phone);
			}

			map.put("pageInfo", pageInfo);



			response.reset();
			response.setCharacterEncoding("GBK");
			response.setContentType("application/msexcel;charset=UTF-8");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			vpsConsumerUserInfoService.exportUserExcelList(map, response,type);
			response.flushBuffer();
		}catch(Exception e){
			log.error("用户积分数据下载失败", e);
		}
	}

	/***
	 *
	 * @param session
	 * @param pageParam
	 * @param response
	 * @param model
	 * @param userKey
	 * @param type
	 * @param queryParam
	 */
	@RequestMapping("/showMap")
    public String  showMap(HttpSession session, String lon,String lat,HttpServletResponse
			response, Model
	     model,String userKey,String type,String queryParam) {
		try{

			Map<String, String> map	=GeocodingUtil.getTrueAddress(lat , lon );

		    model.addAttribute("lon",lon);
		    model.addAttribute("lat",lat);
		    model.addAttribute("address",map.get("formatted_address")+" "+map.get("sematic_description"));
		    model.addAttribute("city",map.get("city"));
		    return "/userinfo/showMap";
	 	}catch(Exception e){
            log.error("加载地图失败", e);
		    return "/userinfo/showMap";
			    }
     }
}
