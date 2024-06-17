package com.dbt.vpointsshop.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.crm.CRMServiceServiceImpl;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsCouponCog;
import com.dbt.vpointsshop.bean.VpointsCouponCollect;
import com.dbt.vpointsshop.service.VpointsCouponCogService;
import com.dbt.vpointsshop.service.VpointsGoodsService;

/**
 * 商城优惠券配置表Action
 */
@Controller
@RequestMapping("/coupon")
public class VpointsCouponCogAction extends BaseAction {

	@Autowired
	private VpointsCouponCogService couponCogService;
	@Autowired
	private VpointsGoodsService goodsService;
	@Autowired
    private CRMServiceServiceImpl crmServiceService;
    @Autowired
    private SysAreaService sysAreaService;

	/**
	 * 活动列表
	 */
	@RequestMapping("/showCouponCogList")
	public String showCouponCogList(HttpSession session, String tabsFlag,
	                        String queryParam, String pageParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			VpointsCouponCog queryBean = new VpointsCouponCog(queryParam);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			if(StringUtils.isBlank(tabsFlag)){
				tabsFlag = "1";	// 已上线
			}
			queryBean.setTabsFlag(tabsFlag);
			List<VpointsCouponCog> resultList = 
			        couponCogService.queryVcodeActivityList(queryBean, pageInfo);
			int countResult = couponCogService.countVcodeActivityList(queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
			model.addAttribute("nowTime", new LocalDate());
			model.addAttribute("tabsFlag", tabsFlag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showCouponCogList";
	}

	/**
	 * 跳转添加页面
	 */
	@RequestMapping("/showCouponCogAdd")
	public String showCouponCogAdd(HttpSession session, Model model) {
		try {
			String groupSwitch = DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
					DatadicKey.filterSwitchSetting.SWITCH_GROUP);
			if(DatadicUtil.isSwitchON(groupSwitch)) {
				model.addAttribute("groupList", crmServiceService.queryVcodeActivityCrmGroup());
			}
			
			SysUserBasis currentUser = this.getUserBasis(session);
            model.addAttribute("goodsLst", goodsService.queryByShipmentProvince(null, null));
            model.addAttribute("currentUser", currentUser);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showCouponCogAdd";
	}

	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showCouponCogEdit")
	public String showCouponCogEdit(HttpSession session, String couponKey,  Model model) {
		try {
		    String groupSwitch = DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.FILTER_SWITCH_SETTING,
					DatadicKey.filterSwitchSetting.SWITCH_GROUP);
			if(DatadicUtil.isSwitchON(groupSwitch)) {
				model.addAttribute("groupList", crmServiceService.queryVcodeActivityCrmGroup());
			}
			
			SysUserBasis currentUser = this.getUserBasis(session);
			// 查询当前活动
		    VpointsCouponCog couponCog = couponCogService.findByCouponKey(couponKey);
            model.addAttribute("currentUser", currentUser);
		    model.addAttribute("couponCog", couponCog);
		    model.addAttribute("goodsLst", goodsService.queryByShipmentProvince(couponCog.getAreaValidLimit(), couponCog.getGoodsId()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showCouponCogEdit";
	}
	
	/**
	 * 跳转数据页面
	 */
	@RequestMapping("/showCouponCogData")
	public String showCouponCogData(HttpSession session, String couponKey, String tabsIndex,  Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			// 查询当前活动
			VpointsCouponCog couponCog = couponCogService.findByCouponKey(couponKey);
			
			// 数据汇总
			VpointsCouponCollect couponCollect = couponCogService.findCouponCollect(couponKey);
			
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("couponCog", couponCog);
			model.addAttribute("couponCollect", couponCollect);
			model.addAttribute("couponKey", couponKey);
			model.addAttribute("tabsIndex", StringUtils.isBlank(tabsIndex) ? "0" : tabsIndex);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "vpointsGoods/showCouponCogData";
	}
	
	/**
	 * 创建活动
	 */
	@RequestMapping("/doCouponCogAdd")
	public String doCouponCogAdd(HttpSession session, VpointsCouponCog couponCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			couponCogService.addCouponCog(couponCog, currentUser);
            model.addAttribute("errMsg", "添加成功");
		} catch (Exception ex) {
            this.initErrMsg(ex, model, "添加失败");
            log.error(ex.getMessage(), ex);
		}
		return "forward:showCouponCogList.do";
	}

	/**
	 * 修改活动
	 */
	@RequestMapping("/doCouponCogEdit")
	public String doCouponCogEdit(HttpSession session,VpointsCouponCog couponCog, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			couponCogService.updateCouponCog(couponCog, currentUser);
	        
	        // 删除活动缓存
	        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode
	                .KEY_VPOINTS_COUPON_COG + Constant.DBTSPLIT + couponCog.getCouponKey());
            model.addAttribute("errMsg", "修改成功");
		} catch (Exception ex) {
            this.initErrMsg(ex, model, "修改失败");
            log.error(ex.getMessage(), ex);
		}
		return "forward:showCouponCogList.do";
	}
    
    /**
     * 刪除记录
     */
    @RequestMapping("/doCouponCogDelete")
    public String doCouponCogDelete(HttpSession session, String couponKey, Model model){
        try{
            SysUserBasis currentUser = this.getUserBasis(session);
            couponCogService.deleteCouponCog(couponKey, currentUser);
            
            // 删除活动缓存
            CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_VPOINTS_COUPON_COG + Constant.DBTSPLIT + couponKey);
            
            model.addAttribute("errMsg", "删除成功");
        } catch (Exception ex) {
            this.initErrMsg(ex, model, "删除失败");
            log.error(ex.getMessage(), ex);
            
        }
        return "forward:showCouponCogList.do"; 
    }
}
