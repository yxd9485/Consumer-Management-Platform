package com.dbt.platform.doubtuser.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.doubtuser.bean.VcodeBlacklistAccount;
import com.dbt.platform.doubtuser.service.VcodeBlacklistService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @author hanshimeng 
 * @createTime 2016年4月21日 下午5:44:15
 * @description V码疑似黑名单Action
 */
@Controller
@RequestMapping("/vcodeDoubtUser")
public class VcodeDoubtUserAction extends BaseAction {

	@Autowired
	private VcodeBlacklistService vcodeBlacklistService; 
	
	/**
	 * 列表
	 * @param session
	 * @param queryParam
	 * @param pageParam
	 * @param model
	 * @return
	 */
	@RequestMapping("/showDoubtUserList")
	public String showDoubtUserList(HttpSession session, String queryParam, String pageParam, Model model) {
		try {

            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeBlacklistAccount queryBean = new VcodeBlacklistAccount(queryParam);
            
			List<VcodeBlacklistAccount> resultList = vcodeBlacklistService.findDoubtUserList(queryBean, pageInfo);
			int countResult = vcodeBlacklistService.countDoubtUserList(queryBean);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
			ex.printStackTrace();
		}
		return "doubt/showDoubtUserList";
	}

    /**
     * (批量)加入到可疑名单
     * @return
     */
    @ResponseBody
    @RequestMapping("/doDoubtUserAdd")
    public String doDoubtUserAdd(HttpSession session, String paramValue, Model model) {
        String result = "Y";
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            if (StringUtils.isNotBlank(paramValue)) {
                String doubtUserLst = "";
                String blackUserlist = "";
                String inexitUserLst = "";
                String bizCode = "";
                String[] userKeyAry = paramValue.split(",");
                for (String userKey : userKeyAry) {
                    userKey = userKey.trim();
                    bizCode = vcodeBlacklistService.addDoubtList(userKey, currentUser.getUserKey(),
                                            null, Constant.DoubtReasonType.DOUBTRESON_FOUR, null, null, null);
                    if ("1".equals(bizCode)) {
                        inexitUserLst = inexitUserLst + userKey + ","; 
                        
                    } else if ("2".equals(bizCode)) {
                        blackUserlist = blackUserlist + userKey + ",";
                        
                    } else if ("3".equals(bizCode)) {
                        doubtUserLst = doubtUserLst + userKey + ",";
                    }
                }
                
                StringBuffer buffer = new StringBuffer();
                if (StringUtils.isNotBlank(inexitUserLst)) {
                    buffer.append("用户").append(inexitUserLst.substring(
                            0, inexitUserLst.length() - 2)).append("不存在").append("\n");
                }
                if (StringUtils.isNotBlank(blackUserlist)) {
                    buffer.append("用户").append(blackUserlist.substring(
                            0, blackUserlist.length() - 2)).append("已在黑名单").append("\n");
                }
                if (StringUtils.isNotBlank(doubtUserLst)) {
                    buffer.append("用户").append(doubtUserLst.substring(
                            0, doubtUserLst.length() - 2)).append("已在可疑").append("\n");
                }
                if (buffer.length() > 0) {
                    errMsg = buffer.substring(0, buffer.length() - 1).toString();
                } else {
                    errMsg = "添加成功";
                }
            }
        } catch (Exception ex) {
            result = "N";
            errMsg = "添加失败";
            ex.printStackTrace();
        }
        
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("bizCode", result);
        resultMap.put("errMsg", errMsg);
        return JSON.toJSONString(resultMap);
    }
	
	/**
	 * 从可疑名单中移除
	 * @param session
	 * @param blacklistKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/doDoubtUserRemove")
	public String doDoubtUserRemove(HttpSession session, String blacklistValue, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			vcodeBlacklistService.removeDoubtUser(blacklistValue, currentUser.getUserKey());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "forward:showDoubtUserList.do";
	}
	
	/**
	 * 添加到黑名单
	 * @param session
	 * @param blacklistKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/doBlacklistAdd")
	public String doBlacklistAdd(HttpSession session, String blacklistValue, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			vcodeBlacklistService.addBlacklist(blacklistValue, currentUser.getUserKey());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        return "forward:showDoubtUserList.do";
	}
}
