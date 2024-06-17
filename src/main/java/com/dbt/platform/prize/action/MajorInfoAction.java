package com.dbt.platform.prize.action;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.prize.bean.MajorInfo;
import com.dbt.platform.prize.service.MajorInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * @author hanshimeng
 * @createTime 2016年4月21日 下午4:26:50
 * @description 一等奖中奖名单Action
 */

@Controller
@RequestMapping("/major")
public class MajorInfoAction extends BaseAction{
	@Autowired
	private MajorInfoService majorInfoService;
	@Autowired
	private VcodeActivityVpointsCogService vpointsCogService;

	/**
	 * 查询中奖用户名单e
	 *
	 * @param session
	 * @param request
	 * @param firstPrizeInfo
	 * @param model
	 */
	@RequestMapping("/showMajorInfoList")
	public String showMajorInfoList(HttpSession session, String queryParam, String pageParam, Model model) throws Exception {
	    SysUserBasis currentUser = this.getUserBasis(session);
	    PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
	    if(StringUtils.isBlank(queryParam)) {
	        String startDate = DateUtil.add(DateUtil.getDate(), -1, Calendar.MONTH, DateUtil.DEFAULT_DATE_FORMAT);
	        String endDate = DateUtil.getDate();
//	    	queryParam = ",,2021-07-01,2021-07-08,,,,,,,,";
	        queryParam = ",," + startDate + "," + endDate + ",,,,,,,,";
	    }
	    MajorInfo queryBean = new MajorInfo(queryParam);
	    
	    
	    List<MajorInfo> majorInfoList = majorInfoService.queryMajorInfoListForPage(queryBean, pageInfo, currentUser);
	    
        // 奖项类型
        Map<String, String> prizeTypeMap = vpointsCogService.queryAllPrizeType(false, true, false, false, false, false, null);

	    model.addAttribute("majorInfoList", majorInfoList);
	    model.addAttribute("showCount", majorInfoService.queryMajorInfoListForTotal(queryBean, currentUser));
        model.addAttribute("startIndex", pageInfo.getStartCount());
        model.addAttribute("countPerPage", pageInfo.getPagePerCount());
        model.addAttribute("currentPage", pageInfo.getCurrentPage());
        model.addAttribute("orderCol", pageInfo.getOrderCol());
        model.addAttribute("orderType", pageInfo.getOrderType());
	    model.addAttribute("queryParam", queryParam);
	    model.addAttribute("pageParam", pageParam);
		model.addAttribute("projectFlag", DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory
				.FILTER_COMPANY_INFO, DatadicKey.filterCompanyInfo.PROJECT_FLAG));
        model.addAttribute("prizeTypeMap", prizeTypeMap);
		model.addAttribute("projectServerName", DbContextHolder.getDBType());
	    return "prize/showMajorInfoList";
	}

	/**
	 * 查询中奖用户名单
	 *
	 * @param session
	 * @param request
	 * @param firstPrizeInfo
	 * @param model
	 */
	@RequestMapping("/showMajorInfoView")
	public String showMajorInfoView(HttpSession session,
			HttpServletRequest request, String infoKey, Model model) throws Exception {
        SysUserBasis currentUser = this.getUserBasis(session);
		model.addAttribute("majorInfo", majorInfoService.findMajorInfoByInfoKey(infoKey));
		return "prize/showMajorInfoView";
	}
	
	/**
	 * 核销大奖
	 * @param session
	 * @param request
	 * @param infoKey
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkMajorInfo")
	public String checkMajorInfo(HttpSession session, HttpServletRequest request, String infoKey, 
	                        String expressCompany, String expressNumber, String expressSendMessage, Model model){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            majorInfoService.executeCheckMajorInfo(infoKey, expressCompany, expressNumber, expressSendMessage, currentUser);
            resultMap.put("errMsg", "操作成功");
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
        } catch (Exception e) {
        	model.addAttribute("errMsg", "操作失败：" + e.getMessage());
            log.error("核销失败", e);
        }
        return JSON.toJSONString(resultMap);
	}
	
	/**
     * 导出查询结果
     * 
     * @return
     */
    @RequestMapping("/exportPrizeList")
    public void exportPrizeList(HttpSession session, HttpServletResponse response,
                                                String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis user = getUserBasis(session);
            MajorInfo queryBean = new MajorInfo(queryParam);
            
            // 导出
            majorInfoService.exportPrizeList(queryBean, response, user);
        } catch (Exception e) {
            log.error("导出查询结果下载失败", e);
        }
    }
}
