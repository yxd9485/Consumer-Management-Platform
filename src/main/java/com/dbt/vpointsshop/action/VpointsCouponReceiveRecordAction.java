package com.dbt.vpointsshop.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsBrandInfo;
import com.dbt.vpointsshop.bean.VpointsCouponReceiveRecord;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.service.VpointsCouponReceiveRecordService;
import com.dbt.vpointsshop.service.VpointsGoodsService;

/**
 * 商城优惠券用户领取Action
 */
@Controller
@RequestMapping("/couponReceiveRecord")
public class VpointsCouponReceiveRecordAction extends BaseAction {

	@Autowired
	private VpointsCouponReceiveRecordService couponReceiveRecordService;
	@Autowired
	private VpointsGoodsService goodsService;

	/**
	 * 数据列表
	 */
	@RequestMapping("/showReceiveRecordList")
	public String showReceiveRecordList(HttpSession session, String pageParam, String couponKey, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			List<VpointsCouponReceiveRecord> resultList = 
					couponReceiveRecordService.queryReceiveRecordList(pageInfo, couponKey);
			int countResult = couponReceiveRecordService.countReceiveRecordList(couponKey);
			model.addAttribute("couponKey", couponKey);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", "");
			model.addAttribute("pageParam", pageParam);
			model.addAttribute("nowTime", new LocalDate());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showReceiveRecordList";
	}
	
	/**
     * 导出发行数据
     * 
     * @return
     */
    @RequestMapping("/exportReceiveRecord")
    public void exportReceiveRecord(HttpSession session, HttpServletResponse response, String couponKey, Model model) {
        try {
            couponReceiveRecordService.exportReceiveRecord(couponKey, response);
        } catch (Exception e) {
            log.error("优惠券发行数据导出下载失败", e);
        }
    }
    
    /**
     * 导出核销数据数据
     * 
     * @return
     */
    @RequestMapping("/exportVerificationRecord")
    public void exportVerificationRecord(HttpSession session, HttpServletResponse response, String couponKey, Model model) {
    	try {
    		 String tabsFlag = "5"; // 全部
             SysUserBasis currentUser = getUserBasis(session);
             VpointsExchangeLog queryBean = new VpointsExchangeLog(null, tabsFlag);
             queryBean.setTabsFlag(tabsFlag);
             queryBean.setCouponKey(couponKey);
             
             
             // 当前用户可查看品牌信息
             List<VpointsBrandInfo> brandLst = goodsService.queryBrandByParentId("0", currentUser.getUserName());
             List<String> brandIdLst = ReflectUtil.getFieldsValueByName("brandId", brandLst);
             queryBean.setBrandIdLst(brandIdLst);
             
    		 couponReceiveRecordService.exportVerificationRecord(queryBean, couponKey, response);
    	} catch (Exception e) {
    		log.error("优惠券核销数据导出下载失败", e);
    	}
    }
}
