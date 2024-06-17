/*
 * 
 */
package com.dbt.crm.action;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.crm.bean.MarketingNode;
import com.dbt.crm.bean.MarketingNodeResultBean;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 营销节点列表
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/marketingNode")
public class MarketingNodeAction extends BaseAction {
    
	@Autowired
	private CRMServiceServiceImpl crmServiceImpl;

	/**
	 * 营销节点列表
	 */
	@RequestMapping("/showMarketingNodeList") 
	public String showMarketingNodeList(HttpSession session, String pageParam, String queryParam,  Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
            MarketingNode queryBean = new MarketingNode(queryParam);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            MarketingNodeResultBean bean = crmServiceImpl.queryMarketingNode(queryBean, pageInfo);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", null != bean ? bean.getData() : null);
            model.addAttribute("showCount", null != bean ? bean.getTotal() : 0);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception e) {
            e.printStackTrace();
        }

		return "crm/showMarketingNodeList";
	}

    /**
     * 更新记录
     * @param session
     * @param nodeId
     * @param state
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("/doMarketingNodeEdit")
    public String doMarketingNodeEdit(HttpSession session, String nodeId, String state, Model model) {
    	String message = null;
        try {
        	message = crmServiceImpl.doMarketingNodeEdit(nodeId, state);
       } catch (Exception e) {
            log.error("失败", e);
        }
        return JSON.toJSONString(message);
    }
}
