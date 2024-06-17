package com.dbt.platform.appuser.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.appuser.bean.VpsConsumerCheckUserInfo;
import com.dbt.platform.appuser.bean.VpsConsumerUserInfo;
import com.dbt.platform.appuser.service.VpsConsumerCheckUserInfoService;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 核销人员Action
 * @author hanshimeng
 *
 */
@Controller
@RequestMapping("/checkUser")
public class CheckUserAction extends BaseAction {

	@Autowired
	private VpsConsumerCheckUserInfoService  checkUserInfoService;
	
	/**
	 * 查询核销用户信息List
	 * 
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showCheckUserInfoList")
	public String showCheckUserInfoList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsConsumerCheckUserInfo queryBean = new VpsConsumerCheckUserInfo(queryParam);
            
			List<VpsConsumerCheckUserInfo> userInfoList =
					checkUserInfoService.findCheckUserInfoList(queryBean, pageInfo);
			int countResult = checkUserInfoService.findCheckUserInfoCount(queryBean);
			
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
            model.addAttribute("queryBean", queryBean);
			model.addAttribute("projectServerName", DbContextHolder.getDBType());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "userinfo/checkUser/showCheckUserInfoList";
	}
	
	/**
     * 跳转核销人员注册页面
     */
    @RequestMapping("/showCheckUserInfoQrcode")
    public String showCheckUserInfoQrcode(HttpSession session, Model model) {
		if (PropertiesUtil.getPropertyValue("run_env").equals("TEST")){
			//测试
			model.addAttribute("serverName", "http://vd.vjifen.com/v/verification/auth.html?serverName=" + DbContextHolder.getDBType());
		}else{
			//线上
			if ("huanan2022".equals(DbContextHolder.getDBType())) {
			    model.addAttribute("serverName", "HTTP://VJ1.TV/U2/verifyregister");
			} else {
			    model.addAttribute("serverName", "http://w.vjifen.com/v/verification/auth.html?serverName=" + DbContextHolder.getDBType());
			}
		}
        return "userinfo/checkUser/showCheckUserInfoQrcode";
    } 
	
	/**
	 * 更新核销用户状态
	 * @param </br> 
	 * @return String </br>
	 */
	@RequestMapping("/updateCheckUserStatus")
	public String updateCheckUserStatus(HttpSession session, String infoKey, String status, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			checkUserInfoService.updateCheckUserStatus(infoKey, status, currentUser);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "forward:showCheckUserInfoList.do";
	}
	
	/**
	 * 删除核销人员
	 * @param session
	 * @param infoKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/deleteCheckUser")
	public String deleteCheckUser(HttpSession session, String infoKey, Model model){
		try{
			checkUserInfoService.deleteCheckUser(infoKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "forward:showCheckUserInfoList.do";
	}
	
	/**
     * 导出查询结果
     * 
     * @return
     */
    @RequestMapping("/exportUserList")
    public void exportPrizeList(HttpSession session, HttpServletResponse response,
                                                String queryParam, String pageParam, Model model) {
        try {
            // 导出
            checkUserInfoService.exportUserList(new VpsConsumerCheckUserInfo(queryParam), response);
        } catch (Exception e) {
            log.error("导出查询结果下载失败", e);
        }
    }
}
