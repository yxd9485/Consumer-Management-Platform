package com.dbt.platform.activity.action;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodePrizeBasicInfo;
import com.dbt.platform.activity.bean.VpsPrizeTerminalRelation;
import com.dbt.platform.activity.bean.VpsTerminalInfo;
import com.dbt.platform.activity.service.VcodeActivityBigPrizeService;
import com.dbt.platform.activity.service.VcodeTeminalInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 终端信息Action
 */
@Controller
@RequestMapping("/terminal")
public class VpsTerminalPrizeAction extends BaseAction {

    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private VcodeTeminalInfoService terminalInfoService;
    @Autowired
    private VcodeActivityBigPrizeService vcodeActivityBigPrizeService;
    
    /**
     * 授权门店列表
     */
    @RequestMapping("/showTerminalPrizeAdd")
    public String showTerminalPrizeAdd(HttpSession session,String prizeType, String pageParam,Model model) {
    	VpsTerminalInfo queryBean = new VpsTerminalInfo(prizeType);
    	PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
    	model.addAttribute("prizeType", queryBean.getPrizeType());
    	model.addAttribute("sqStatus", queryBean.getSqStatus());
    	model.addAttribute("showCount", terminalInfoService.queryForCount(queryBean));
    	model.addAttribute("resultList", terminalInfoService.queryForList(queryBean,new PageOrderInfo(null)));
        model.addAttribute("startIndex", pageInfo.getStartCount());
        model.addAttribute("countPerPage", pageInfo.getPagePerCount());
        model.addAttribute("currentPage", pageInfo.getCurrentPage());
        model.addAttribute("pageParam", pageParam);
        model.addAttribute("orderCol", pageInfo.getOrderCol());
        model.addAttribute("orderType", pageInfo.getOrderType());
        return "vcode/bigprize/showTerminalPrizeAdd";
    }
    
   
    /**
     * 根据大奖类型查询终端门店列表
     */
    @RequestMapping("/showPrizeTerminalList")
    public String showPrizeTerminalList(HttpSession session, String queryParam,String prizeType, String pageParam, Model model) {
    	try{

			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			VpsTerminalInfo queryBean = new VpsTerminalInfo(queryParam, false);
			if (StringUtils.isNotBlank(queryBean.getCounty())) {
				queryBean.setAreaCode(queryBean.getCounty());
			} else if (StringUtils.isNotBlank(queryBean.getCity())) {
				queryBean.setAreaCode(queryBean.getCity().substring(0, 4));
			} else if (StringUtils.isNotBlank(queryBean.getProvince())) {
				queryBean.setAreaCode(queryBean.getProvince().substring(0, 2));
			}
			queryBean.setPrizeType(prizeType);
            SysUserBasis currentUser = this.getUserBasis(session);
			List<VpsTerminalInfo> resultList =terminalInfoService.queryForList(queryBean, pageInfo);
			int countResult = terminalInfoService.queryForCount(queryBean);
			String areaCode = "";
			areaCode = StringUtils.isNotBlank(queryBean.getCounty()) 
					? queryBean.getCounty() : StringUtils.isNotBlank(queryBean.getCity()) 
							? queryBean.getCity() : queryBean.getProvince();
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("areaCode", areaCode);
			model.addAttribute("resultList", resultList); 
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("prizeType", queryBean.getPrizeType());
            model.addAttribute("sqStatus", queryBean.getSqStatus());
          
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return "vcode/bigprize/showTerminalPrizeAdd";
	}
    

    /**
     * 授权门店
     */
    @RequestMapping("/addPrizeTerminal")
    public String addPrizeTerminal(HttpSession session, String terminalKey, String prizeType, Model model) {
    	try{			
			terminalInfoService.addPrizeTerminal(new VpsPrizeTerminalRelation(terminalKey,prizeType));
			model.addAttribute("errMsg", "授权成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "授权失败");
			ex.printStackTrace();
			log.error(ex);	
		}
		return "forward:showPrizeTerminalList.do";
    }
    
    /**
     * 取消授权
     */
    @RequestMapping("/delPrizeTerminal")
    public String delPrizeTerminal(HttpSession session, String terminalKey, String prizeType, Model model) {
    	try{			
			terminalInfoService.deleteRlation(new VpsPrizeTerminalRelation(terminalKey,prizeType));
			model.addAttribute("errMsg", "取消授权成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "取消授权失败");
			ex.printStackTrace();
			log.error(ex);	
		}
		return "forward:showPrizeTerminalList.do";
    }
    
    /**
     * 批量授权门店
     */
	@RequestMapping("/addTerminalLst")
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public String addTerminalLst(HttpSession session, String terminalKeyLst, String prizeType, Model model) {
    	try{			
			terminalInfoService.addorUpdTerminalRelation(new ArrayList(Arrays.asList(terminalKeyLst.split(","))),prizeType);
			model.addAttribute("errMsg", "授权成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "授权失败");
			ex.printStackTrace();
			log.error(ex);	
		}
		return "forward:showPrizeTerminalList.do";
    }
    /**
     * 批量取消授权门店
     */
    @RequestMapping("/delTerminalLst")
    public String delTerminalLst(HttpSession session, String terminalKeyLst, String prizeType, Model model) {
    	try{			
    		terminalInfoService.deleteRlationLst(Arrays.asList(terminalKeyLst.split(",")),prizeType,true,"1");
			model.addAttribute("errMsg", "授权成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "授权失败");
			ex.printStackTrace();
			log.error(ex);	
		}
		return "forward:showPrizeTerminalList.do";
    }
    
  
    
    
    /**
     * 门店列表
     */
    @RequestMapping("/showTerminalList")
    public String showTerminalList(HttpSession session, String queryParam,String pageParam, Model model) {
    	try{
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			VpsTerminalInfo queryBean = new VpsTerminalInfo(queryParam,true);
            SysUserBasis currentUser = this.getUserBasis(session);
			List<VpsTerminalInfo> resultList =terminalInfoService.queryForList(queryBean, pageInfo);
			int countResult = terminalInfoService.queryForCount(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList); 
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("bigPrizes",vcodeActivityBigPrizeService.getBigPrizeList());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return "vcode/terminal/showTerminalList";
	}
    
    
    
    /**
     * 新增门店页面
     */
    @RequestMapping("/addTerminal")
    public String addTerminal(HttpSession session,Model model) {
    	try {
    		model.addAttribute("bigPrizes",vcodeActivityBigPrizeService.getBigPrizeList());
		} catch (Exception e) {
			 log.error(e.getMessage(), e);
		}
        return "vcode/terminal/showTerminalAdd";
    }
    
    /**
     * 新增门店
     */
    @RequestMapping("/createTerminal")
    public String createTerminal(HttpSession session,VpsTerminalInfo terminalInfo ,Model model) {
    	try {
    		terminalInfoService.createTerminal(terminalInfo,this.getUserBasis(session));
    		model.addAttribute("errMsg", "新增成功");
		} catch (Exception e) {
			model.addAttribute("errMsg", "新增失败");
			log.error(e.getMessage(), e);
		}
        return "forward:showTerminalList.do";
    }
    
    
    /**
     * 修改门店
     */
    @RequestMapping("/updateTerminal")
    public String updateTerminal(HttpSession session,VpsTerminalInfo terminalInfo ,Model model) {
    	try {
    		terminalInfoService.updateTerminal(terminalInfo,this.getUserBasis(session));
    		model.addAttribute("errMsg", "修改成功");
		} catch (Exception e) {
			model.addAttribute("errMsg", "修改失败");
			log.error(e.getMessage(), e);
		}
        return "forward:showTerminalList.do";
    }
    
    
    
    
    /**
     * 查看门店
     */
    @RequestMapping("/showTerminal")
    public String showTerminal(HttpSession session, String terminalKey, Model model) {
    	try {
            // 查询当前活动
    		VpsTerminalInfo vpsTerminalInfo = terminalInfoService.findTerminalByKey(terminalKey);
            model.addAttribute("terminalInfo", vpsTerminalInfo);
            model.addAttribute("bigPrizes",vcodeActivityBigPrizeService.getBigPrizeList());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "vcode/terminal/showTerminalEdit";
    }
    
    
    /**
     * 删除门店
     */
    @RequestMapping("/delTerminal")
    public String delTerminal(HttpSession session, String terminalKey, Model model) {
    	try{			
			terminalInfoService.delTerminal(terminalKey);
			model.addAttribute("errMsg", "删除成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "删除失败");
			ex.printStackTrace();
			log.error(ex);	
		}
		return "forward:showTerminalList.do";
    }
    
    
    /**
     * 禁用启用门店
     */
    @RequestMapping("/updateTerminalStatus")
    public String updateTerminalStatus(HttpSession session, String status, String terminalKey, Model model) {
    	try{			
			status = "0".equals(status) ? "1" : "0";
			terminalInfoService.updateTerminalStatus(status,terminalKey,this.getUserBasis(session));
			model.addAttribute("errMsg", "编辑成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "编辑失败");
			ex.printStackTrace();
			log.error(ex);	
		}
		return "forward:showTerminalList.do";
    }



    
    /**
     * 门店授权奖品列表
     */
    @RequestMapping("/showPrizeAddLst")
    public String showPrizeAddLst(HttpSession session,String terminalKey,String queryParam,String pageParam, Model model) {
		VcodePrizeBasicInfo queryBean = new VcodePrizeBasicInfo();
		PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
		queryBean.setTerminalKey(terminalKey);
		model.addAttribute("terminalKey", terminalKey);
		model.addAttribute("showCount", vcodeActivityBigPrizeService.queryTerminalPrizeCount(queryBean));
		model.addAttribute("resultList",vcodeActivityBigPrizeService.queryTerminalPrizeLst(queryBean, new PageOrderInfo(null)));
		model.addAttribute("startIndex", pageInfo.getStartCount());
		model.addAttribute("countPerPage", pageInfo.getPagePerCount());
		model.addAttribute("currentPage", pageInfo.getCurrentPage());
		model.addAttribute("queryParam", queryParam);
		model.addAttribute("pageParam", pageParam);
		model.addAttribute("orderCol", pageInfo.getOrderCol());
		model.addAttribute("orderType", pageInfo.getOrderType());
		return "vcode/terminal/showTerminalPrizeList";
    }
    
    /**
     * 门店对应的奖项列表
     * @param session
     * @param queryParam
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("/showTerminalPrizeList")
    public String showTerminalPrizeList(HttpSession session,
                                        String queryParam,String terminalKey, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodePrizeBasicInfo queryBean = new VcodePrizeBasicInfo();
            queryBean.setPrizeBasicQueryParam(queryParam,terminalKey);
            List<VcodePrizeBasicInfo> resultList = vcodeActivityBigPrizeService.queryTerminalPrizeLst(queryBean, pageInfo);
            int countResult = vcodeActivityBigPrizeService.queryTerminalPrizeCount(queryBean);

            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/terminal/showTerminalPrizeList";
    }
    
    /**
     * 停用启用奖品
     */
    @RequestMapping("/stopPrize")
    public String stopPrize(HttpSession session,String terminalKeys,String prizeStatus, String prize, Model model) {
    	try{			
    		prizeStatus = "0".equals(prizeStatus) ? "1" : "0";
    		VpsPrizeTerminalRelation prizeTerminalRelation= new VpsPrizeTerminalRelation(terminalKeys,prize);
    		prizeTerminalRelation.setStatus(prizeStatus);
			terminalInfoService.stopPrize(prizeTerminalRelation);
			model.addAttribute("errMsg", "操作成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "操作失败");
			ex.printStackTrace();
			log.error(ex);	
		}
		return "forward:showTerminalPrizeList.do";
    }
    
   
    
}
