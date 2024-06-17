package com.dbt.vpointsshop.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.ReflectUtil;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.bean.VpointsBrandInfo;
import com.dbt.vpointsshop.bean.VpointsCategoryType;
import com.dbt.vpointsshop.bean.VpointsExchangeLog;
import com.dbt.vpointsshop.bean.VpointsPrizeBean;
import com.dbt.vpointsshop.bean.VpointsRechargeLog;
import com.dbt.vpointsshop.service.ExchangeService;
import com.dbt.vpointsshop.service.VpointsGoodsService;

@Controller
@RequestMapping("/vpointsExchange")
public class VpointsExchangeAction extends BaseAction{

	@Autowired
	private VpointsGoodsService goodService;
	@Autowired
	private ExchangeService exchangeService;
	@Autowired
	private VcodeActivityVpointsCogService vpointsCogService;

	/**
	 * 查询兑换日志
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @param session
	 * @return
	 */
	@RequestMapping("/getExchangeList")
	public String getExchangeList(Model model, String pageParam,String queryParam,HttpSession session){
		PageOrderInfo info = new PageOrderInfo(pageParam);
		SysUserBasis user=getUserBasis(session);
//		String companyKey=user.getCompanyKey();
		VpointsExchangeLog bean=new VpointsExchangeLog(queryParam);
//		if(StringUtils.isNotEmpty(companyKey)&&StringUtils.isEmpty(bean.getCompanyKey())){
//			bean.setCompanyKey(companyKey);
//		}else{
//			List<CompanyInfo> companyList=goodService.getCompanyList();
//			model.addAttribute("companyList", companyList);
//		}
		int countAll=0;
		List<VpointsExchangeLog> exchangeList=null;
		try {
			exchangeList = exchangeService.getExchangeList(info,bean);
			countAll = exchangeService.getExchangeCount(info, bean);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
		List<VpointsCategoryType> firstList=goodService.getFirstCategoryList();
		if(!StringUtils.isEmpty(bean.getCategoryParent())){
			List<VpointsCategoryType> secondList=goodService.getCategoryByParent(bean.getCategoryParent());
			model.addAttribute("secondList", secondList);
		}
		model.addAttribute("firstList", firstList);
		model.addAttribute("exchangeList", exchangeList);
		model.addAttribute("showCount", countAll);
		model.addAttribute("startIndex", info.getStartCount());
		model.addAttribute("countPerPage", info.getPagePerCount());
		model.addAttribute("currentPage", info.getCurrentPage());
		model.addAttribute("queryParam", queryParam);
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
        model.addAttribute("pageParam", pageParam);
        model.addAttribute("projectServerName", DbContextHolder.getDBType());

        return "vpointsGoods/showExchangeList";
	}
	/**
	 * 查询实物奖记录
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @param session
	 * @return
	 */
	@RequestMapping("/getPrizeList")
	public String getPrizeList(Model model, String pageParam,String queryParam,HttpSession session){
		PageOrderInfo info = new PageOrderInfo(pageParam);
		SysUserBasis user=getUserBasis(session);
//		String companyKey=user.getCompanyKey();
		VpointsPrizeBean bean=new VpointsPrizeBean(queryParam);
//		if(StringUtils.isNotEmpty(companyKey)&&StringUtils.isEmpty(bean.getCompanyKey())){
//			bean.setCompanyKey(companyKey);
//		}else{
//			List<CompanyInfo> companyList=goodService.getCompanyList();
//			model.addAttribute("companyList", companyList);
//		}
		int countAll=0;
		List<VpointsPrizeBean> prizeList=null;
		try {
			prizeList = exchangeService.getPrizeList(info,bean);
			countAll = exchangeService.getPrizeCount(info, bean);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
		List<VpointsCategoryType> firstList=goodService.getFirstCategoryList();
		if(!StringUtils.isEmpty(bean.getCategoryParent())){
			List<VpointsCategoryType> secondList=goodService.getCategoryByParent(bean.getCategoryParent());
			model.addAttribute("secondList", secondList);
		}
		model.addAttribute("firstList", firstList);
		model.addAttribute("prizeList", prizeList);
		model.addAttribute("showCount", countAll);
		model.addAttribute("startIndex", info.getStartCount());
		model.addAttribute("countPerPage", info.getPagePerCount());
		model.addAttribute("currentPage", info.getCurrentPage());
		model.addAttribute("queryParam", queryParam);
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
        model.addAttribute("pageParam", pageParam);
		return "vpointsGoods/showPrizeList";
	}

	/**
	 * 查询实物奖记录
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @param session
	 * @return
	 */
	@RequestMapping("/getPrizeDetail")
	public String getPrizeDetail(Model model, String infoKey,String pageParam,String queryParam){
		PageOrderInfo info = new PageOrderInfo(null);
		VpointsPrizeBean bean=new VpointsPrizeBean();
		bean.setInfoKey(infoKey);
		try {
			bean = exchangeService.getPrizeList(info,bean).get(0);
			String userKey=bean.getUserKey();
			String nickName=exchangeService.getNickNameByKey(userKey);
			String openid=exchangeService.getOpenidByKey(userKey);
			bean.setNickName(nickName);
			bean.setOpenid(openid);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}

		model.addAttribute("bean", bean);
		model.addAttribute("queryParam", queryParam);
		model.addAttribute("pageParam", pageParam);
		return "vpointsGoods/prizeDetail";
	}
	/**
	 * 查询兑换日志
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @param session
	 * @return
	 */
	@RequestMapping("/getRechargeList")
	public String getRechargeList(Model model, String pageParam,String queryParam,HttpSession session){
		PageOrderInfo info = new PageOrderInfo(pageParam);
		SysUserBasis user=getUserBasis(session);
//		String companyKey=user.getCompanyKey();
		VpointsRechargeLog bean=new VpointsRechargeLog(queryParam);
//		if(StringUtils.isNotEmpty(companyKey)&&StringUtils.isEmpty(bean.getCompanyKey())){
//			bean.setCompanyKey(companyKey);
//		}else{
//			List<CompanyInfo> companyList=goodService.getCompanyList();
//			model.addAttribute("companyList", companyList);
//		}
		int countAll=0;
		List<VpointsRechargeLog> rechargeList=null;
            try {
                rechargeList = exchangeService.getRechargeList(info,bean);
                countAll = exchangeService.getRechargeCount(info, bean);
            } catch (Exception e) {
                model.addAttribute("errorMsg","查询异常");
                e.printStackTrace();
		}

		model.addAttribute("rechargeList", rechargeList);
		model.addAttribute("showCount", countAll);
		model.addAttribute("startIndex", info.getStartCount());
		model.addAttribute("countPerPage", info.getPagePerCount());
		model.addAttribute("currentPage", info.getCurrentPage());
		model.addAttribute("queryParam", queryParam);
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
         /*      model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);*/
        model.addAttribute("pageParam", pageParam);
		return "vpointsGoods/showRechargeList";
	}

	/**
     * 订单管理列表
     */
    @RequestMapping("/showExchangeExpressList")
    public String showExchangeExpressList(HttpSession session, String queryParam, String pageParam,
    		String tabsFlag, String seckillActivityKey, String groupBuyingActivityKey, String couponKey,  Model model) {
        try {
            if (StringUtils.isBlank(tabsFlag)) {
                tabsFlag = "0";
            }
            SysUserBasis currentUser = getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsExchangeLog queryBean = new VpointsExchangeLog(queryParam, tabsFlag);
            queryBean.setTabsFlag(tabsFlag);
            queryBean.setSeckillActivityKey(seckillActivityKey);
            queryBean.setGroupBuyingActivityKey(groupBuyingActivityKey);
            queryBean.setCouponKey(couponKey);
            queryBean.setPlatformNickName(currentUser.getNickName());
            queryBean.setVpointsOrderAreaTime(DatadicUtil.getDataDicValue(
					DatadicUtil.dataDicCategory.VPOINTS_ESTORE_COG,
					DatadicUtil.dataDic.vpointsEstoreCog.vpointsOrderAreaTime));

            // 当前用户可查看品牌信息
            List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", currentUser.getUserName());
            List<String> brandIdLst = ReflectUtil.getFieldsValueByName("brandId", brandLst);
            queryBean.setBrandIdLst(brandIdLst);

            // 订单列表 (商城订单除待发货以外默认不加载数据)
            int countResult = 0;
            List<VpointsExchangeLog> resultList = new ArrayList<VpointsExchangeLog>();
            if (StringUtils.isNotBlank(seckillActivityKey) || StringUtils.isNotBlank(groupBuyingActivityKey)
                    || StringUtils.isNotBlank(couponKey) || StringUtils.isNotBlank(queryParam) || "0".equals(tabsFlag)) {
                resultList = exchangeService.queryForExpressLst(queryBean, pageInfo);
                countResult = exchangeService.queryForExpressCount(queryBean);
            }
            resultList.forEach(brand->{
                for (VpointsBrandInfo vpointsBrandInfo : brandLst) {
                    if(vpointsBrandInfo.getBrandId().equals(brand.getBrandId())){
                        brand.setBrandName(vpointsBrandInfo.getBrandName());
                    }
                }
            });

            model.addAttribute("seckillActivityKey", seckillActivityKey);
            model.addAttribute("groupBuyingActivityKey", groupBuyingActivityKey);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("brandLst", brandLst);
            model.addAttribute("exchangeCardTypeMap", vpointsCogService.queryBigPrizeForZunXiangCard()); // 获取尊品卡类型
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("tabsFlag", tabsFlag);
            model.addAttribute("serverName", DbContextHolder.getDBType());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        if(StringUtils.isNotBlank(seckillActivityKey)) {

        	return "vpointsGoods/seckill/showExchangeExpressList";
        }else if(StringUtils.isNotBlank(groupBuyingActivityKey)) {

        	return "vpointsGoods/groupBuying/showExchangeExpressList";
        }else if(StringUtils.isNotBlank(couponKey)){
        	return "vpointsGoods/showCouponExchangeExpressList";
        }else {
        	return "vpointsGoods/showExchangeExpressList";
        }
    }

    /**
     * 订单管理列表
     */
    @RequestMapping("/showExchangeExpressView")
    public String showExchangeExpressView(HttpSession session, String infoKey, Model model) {
        try {
            VpointsExchangeLog exchangeLog = exchangeService.findById(infoKey);
            exchangeLog.setOrderStatus(exchangeService.transExchangeOrderStatus(exchangeLog));
            model.addAttribute("item", exchangeLog);
            model.addAttribute("serverName", DbContextHolder.getDBType());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return "vpointsGoods/showExchangeExpressView";
    }

    /**
     * 退货审核
     */
    @RequestMapping("/goodsReturnAudit")
    public String goodsReturnAudit(HttpSession session, String optType, String exchangeId, String goodsReturnAudit, Model model) {
        String errMsg = "";
        SysUserBasis currentUser = getUserBasis(session);
        try {
            exchangeService.updateGoodsReturnAudit(optType, exchangeId, goodsReturnAudit, currentUser);
            model.addAttribute("errMsg", "退货申请" + ("0".equals(optType) ? "通过" : "驳回"));

        } catch (BusinessException e) {
            model.addAttribute("errMsg", e.getMessage());
            log.error(e.getMessage(), e);

        } catch (Exception e) {
            model.addAttribute("errMsg", "操作失败");
        }
        return "forward:showExchangeExpressList.do";
    }

    /**
     * 更新订单的物流信息
     */
    @ResponseBody
    @RequestMapping("/updateExpressInfo")
    public String updateExpressInfo(HttpSession session, String exchangeId, String expressCompany, String expressNumber) {
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(" exchangeId:").append(StringUtils.defaultIfBlank(exchangeId, ""));
        logBuffer.append(" expressCompany:").append(StringUtils.defaultIfBlank(expressCompany, ""));
        logBuffer.append(" expressNumber:").append(StringUtils.defaultIfBlank(expressNumber, ""));
        Map<String, Object> resultMap = new HashMap<>();
        try {
            exchangeService.updateExpressInfo(exchangeId, expressCompany, expressNumber);
            resultMap.put("errMsg", "更新成功");
            log.warn("物流单号更新成功," + logBuffer.toString());

        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
            log.error(e.getMessage() + logBuffer.toString(), e);

        } catch (Exception e) {
            resultMap.put("errMsg", "更新失败");
            log.error("更新失败，" + logBuffer.toString(), e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 更新订单的收货信息
     */
    @ResponseBody
    @RequestMapping("/updateOrderAddress")
    public String updateOrderAddress(HttpSession session, String exchangeId, String userName, String phoneNum, String address) {
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(" exchangeId:").append(StringUtils.defaultIfBlank(exchangeId, ""));
        logBuffer.append(" userName:").append(StringUtils.defaultIfBlank(userName, ""));
        logBuffer.append(" phoneNum:").append(StringUtils.defaultIfBlank(userName, ""));
        logBuffer.append(" address:").append(StringUtils.defaultIfBlank(userName, ""));
        Map<String, Object> resultMap = new HashMap<>();
        try {
            exchangeService.updateOrderAddress(exchangeId, userName, phoneNum, address);
            resultMap.put("errMsg", "更新成功");
            log.warn("收货信息更新成功," + logBuffer.toString());

        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
            log.error(e.getMessage() + logBuffer.toString(), e);

        } catch (Exception e) {
            resultMap.put("errMsg", "收货信息更新失败");
            log.error("收货信息更新失败，" + logBuffer.toString(), e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 撤单
     */
    @ResponseBody
    @RequestMapping("/revokeOrder")
    public String revokeOrder(HttpSession session, String exchangeId,
            String optType, String refundType, String refundMoney, String refundVpoints, String revokeOrderReason) {
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append(" exchangeId:").append(StringUtils.defaultIfBlank(exchangeId, ""));
        Map<String, Object> resultMap = new HashMap<>();
        String optTypeDesc = "0".equals(optType) ? "撤单" : "确认退款";
        try {
            exchangeService.revokeOrder(exchangeId, refundType, refundMoney, refundVpoints, revokeOrderReason);
            resultMap.put("errMsg", optTypeDesc + "成功");
            log.warn(optTypeDesc + "成功," + logBuffer.toString());

        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage().replace("撤单", optTypeDesc));
            log.error(e.getMessage() + logBuffer.toString());

        } catch (Exception e) {
            resultMap.put("errMsg", optTypeDesc + "失败");
            log.error(optTypeDesc + "失败，" + logBuffer.toString(), e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 待发货表格下载
     * @return
     */
    @RequestMapping("/exportExchangeExpressList")
    public void exportExchangeExpressList(HttpSession session, HttpServletResponse
                                    response, String queryParam, String pageParam, String tabsFlag, Model model) {
        try {
                // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
//                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//                response.setCharacterEncoding("utf-8");
//                // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
//                String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
//                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
//                EasyExcel.write(response.getOutputStream()).sheet("模板").doWrite(data());
            if (StringUtils.isBlank(tabsFlag)) {
                tabsFlag = "0";
            }
            SysUserBasis user = getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpointsExchangeLog queryBean = new VpointsExchangeLog(queryParam, tabsFlag);
            queryBean.setTabsFlag(tabsFlag);
            queryBean.setPlatformNickName(user.getNickName());
            queryBean.setVpointsOrderAreaTime(DatadicUtil.getDataDicValue(
					DatadicUtil.dataDicCategory.VPOINTS_ESTORE_COG,
					DatadicUtil.dataDic.vpointsEstoreCog.vpointsOrderAreaTime));

            log.error("订单区分地区用户nickName=" + queryBean.getPlatformNickName());
            log.error("订单区分地区时间节点=" + queryBean.getVpointsOrderAreaTime());
            // 当前用户可查看品牌信息
            List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", user.getUserName());
            List<String> brandIdLst = ReflectUtil.getFieldsValueByName("brandId", brandLst);
            queryBean.setBrandIdLst(brandIdLst);
            // 导出
            exchangeService.exportExchangeExpressList(queryBean, pageInfo, response,brandLst);
        } catch (Exception e) {
            log.error("待发货表格下载失败", e);
        }
    }

    /**
     * 导入并入库
     */
    @ResponseBody
    @RequestMapping("/importExchangeExpressList")
    public String importActivityMoneyConfig(HttpSession session,
                    @RequestParam("filePath") MultipartFile clientFile) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<VpointsExchangeLog> exchangeLogLst = exchangeService.importExchangeExpressList(clientFile);
            resultMap.put("errMsg", "更新成功");

            // 上传成功时送信息通知
            if (CollectionUtils.isNotEmpty(exchangeLogLst)) {
                exchangeService.sendExpressSMS(exchangeLogLst);
            }
        } catch (BusinessException e) {
            resultMap.put("errMsg", e.getMessage());
            log.error(e.getMessage(), e);

        } catch (Exception e) {
            resultMap.put("errMsg", "发货表格上传失败");
            log.error(e.getMessage(), e);
        }
        return JSON.toJSONString(resultMap);
    }

    /**
     * 导出订单相关数据（秒杀订单、拼团订单、优惠券订单等）
     *
     * @return
     */
    @RequestMapping("/exportOrderRecord")
    public void exportOrderRecord(HttpSession session, HttpServletResponse response,
    		String couponKey,  String seckillActivityKey, String groupBuyingActivityKey, Model model) {
    	try {
    		 String tabsFlag = "3"; // 全部
             SysUserBasis currentUser = getUserBasis(session);
             VpointsExchangeLog queryBean = new VpointsExchangeLog(null, tabsFlag);
             queryBean.setTabsFlag(tabsFlag);
             queryBean.setCouponKey(couponKey);
             queryBean.setSeckillActivityKey(seckillActivityKey);
             queryBean.setGroupBuyingActivityKey(groupBuyingActivityKey);


             // 当前用户可查看品牌信息
             List<VpointsBrandInfo> brandLst = goodService.queryBrandByParentId("0", currentUser.getUserName());
             List<String> brandIdLst = ReflectUtil.getFieldsValueByName("brandId", brandLst);
             queryBean.setBrandIdLst(brandIdLst);

             exchangeService.exportOrderRecord(queryBean, response);
    	} catch (Exception e) {
    		log.error("优惠券核销数据导出下载失败", e);
    	}
    }
}
