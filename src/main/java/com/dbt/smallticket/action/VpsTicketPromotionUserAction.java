package com.dbt.smallticket.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.JsonUtil;
import com.dbt.framework.util.ReadExcel;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.wctaccesstoken.bean.WechatCheckTemplateMsg;
import com.dbt.smallticket.bean.VpsTicketPromotionUser;
import com.dbt.smallticket.bean.VpsTicketWararea;
import com.dbt.smallticket.service.VpsTicketPromotionUserService;

@Controller
@RequestMapping("/promotionUserAction")
public class VpsTicketPromotionUserAction extends BaseAction {

	@Autowired
	private VpsTicketPromotionUserService promotionUserService;
	@Autowired
    private SysAreaService areaService;
	@Autowired
    private ThreadPoolTaskExecutor taskExecutor;
	
	/**
	 * 	促销员列表
	 */
	@RequestMapping("/showPromotionUserList")
	public String showPromotionUserList(HttpSession session, String pageParam, String queryParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);	
			
			// 是否拥有战区角色,如果拥有则判断操作促销员的按钮显示权限
			String warAreaFlag = validRole(session, "98") ? "1" : "0";
			VpsTicketPromotionUser queryBean = new VpsTicketPromotionUser(queryParam, warAreaFlag);
			if("1".equals(warAreaFlag)) {
				queryBean.setWarAreaName(currentUser.getUserName().substring(0, 4));
			}
			if("姓名/手机号/门店名称".equals(queryBean.getSearchVal())) {
				queryBean.setSearchVal(null);
			}
						
			if(StringUtils.isNotBlank(queryBean.getProvinceCode())) {
	    		queryBean.setProvince(areaService.findById(queryBean.getProvinceCode()).getAreaName());
	    	}
	    	if(StringUtils.isNotBlank(queryBean.getCityCode())) {
	    		queryBean.setCity(areaService.findById(queryBean.getCityCode()).getAreaName());
	    	}
	    	if(StringUtils.isNotBlank(queryBean.getCountyCode())) {
	    		queryBean.setCounty(areaService.findById(queryBean.getCountyCode()).getAreaName());
	    	}
			
			// 查询列表
			List<VpsTicketPromotionUser> resultList = promotionUserService.queryForLst(queryBean, pageInfo);
			int countResult = promotionUserService.queryForCount(queryBean);
			
			String areaCode = StringUtils.isNotBlank(queryBean.getCountyCode()) 
					? queryBean.getCountyCode() : StringUtils.isNotBlank(queryBean.getCityCode()) 
							? queryBean.getCityCode() : queryBean.getProvinceCode();
							
			// 战区
			List<VpsTicketWararea> ticketWarareaList = promotionUserService.queryWarareaAll();
			
			// 默认显示操作按钮
			int operateButStatus = Constant.OperateStatus.enable;
			if("1".equals(warAreaFlag)) {
				// 查询按钮显示时间
				String times = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.TICKET_COG,
						DatadicKey.ticketCog.OPERATION_PROMOTION_TIME);
				if(StringUtils.isNotBlank(times)) {
					String nowTime = DateUtil.getDate();
					String beginTime = times.split(",")[0];
					String endTime = times.split(",")[1];
					if(beginTime.compareTo(nowTime) > 0 || endTime.compareTo(nowTime) < 0) {
						operateButStatus = Constant.OperateStatus.disable;
					}
				}
			}
			
			// 页面传参
			model.addAttribute("areaCode", areaCode);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("ticketWarareaList", ticketWarareaList);
			model.addAttribute("operateButStatus", operateButStatus);
			model.addAttribute("warAreaFlag", warAreaFlag);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("orderCol", pageInfo.getOrderCol());
			model.addAttribute("orderType", pageInfo.getOrderType());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			log.error(ex);
		}
		return "ticket/showPromotionUserList";
	}
	
	/**
     * 	详情
     */
	@RequestMapping("/showEdit")
	public String showEdit(HttpSession session,int isShow, String infoKey, Model model) {
		SysUserBasis currentUser = this.getUserBasis(session);
		// 促销员信息
		VpsTicketPromotionUser promotionUser =  promotionUserService.findById(infoKey);
		// 战区
		List<VpsTicketWararea> ticketWarareaList = promotionUserService.queryWarareaAll();
		model.addAttribute("promotionUser", promotionUser);
		model.addAttribute("ticketWarareaList", ticketWarareaList);
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("infoKey", infoKey);
		model.addAttribute("isShow", isShow);
		return 2 == isShow ? "ticket/editPromotionUser" : "ticket/showPromotionUser";
	}
	
	/**
     * 	跳转添加页
     */
	@RequestMapping("/showAdd")
	public String showAdd(HttpSession session, Model model) {
        SysUserBasis currentUser = this.getUserBasis(session);
		List<VpsTicketWararea> ticketWarareaList = promotionUserService.queryWarareaAll();
		System.out.println("----------------------------"+ticketWarareaList.size());
		
        model.addAttribute("currentUser", currentUser);
		model.addAttribute("ticketWarareaList", ticketWarareaList);
		return "ticket/addPromotionUser";
	}
	
	/**
	 * 	添加
	 */
	@RequestMapping("/doPromotionUserAdd")
	public String doPromotionUserAdd(HttpSession session, VpsTicketPromotionUser promotionUser, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			promotionUser.fillFields(currentUser.getUserKey());
			promotionUserService.insertPromotionUser(promotionUser);
		    model.addAttribute("errMsg", "操作成功");
		} catch (Exception ex) {
			if(ex.getMessage().contains("for key 'idx_phone_num'")) {
				model.addAttribute("errMsg", "操作失败：手机号已存在");
			}else {
				model.addAttribute("errMsg", "操作失败");
			}
			ex.printStackTrace();
		}
		return "forward:showPromotionUserList.do"; 
	}
	
	/**
	 * 	修改
	 */
	@RequestMapping("/doPromotionUserEdit")
	public String doPromotionUserEdit(HttpSession session, VpsTicketPromotionUser promotionUser, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			promotionUser.fillFields(currentUser.getUserKey());
			promotionUserService.updatePromotionUser(promotionUser);
			model.addAttribute("errMsg", "操作成功");
		} catch (Exception ex) {
			if(ex.getMessage().contains("for key 'idx_phone_num'")) {
				model.addAttribute("errMsg", "操作失败，手机号已存在");
			}else {
				model.addAttribute("errMsg", "操作失败");
			}
			ex.printStackTrace();
		}
		return "forward:showPromotionUserList.do"; 
	}
	
	/**
	 * 	审核
	 */
	@RequestMapping("/doPromotionUserCheck")
	public String doPromotionUserCheck(HttpSession session, VpsTicketPromotionUser promotionUser, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			promotionUser.fillFields(currentUser.getUserKey());
			promotionUserService.updatePromotionUserForCheck(promotionUser);
			
			/**
			 * 推送模板消息
			 */
			// 公众号appid
			String appid = DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
					DatadicKey.filterWxPayTemplateInfo.TERMINAL_APPID);
			
			// 小程序appid
			String  paApplet_appid = DatadicUtil.getDataDicValue(
					DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
					DatadicKey.filterWxPayTemplateInfo.PAAPPLET_APPID);
    		
			// 跳转小程序路径
    		String pagePath = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PROMOTION_CHECK_PAGEPATH);
    		
    		// 公众号模板ID
    		String template_id = DatadicUtil.getDataDicValue(
                    DatadicKey.dataDicCategory.FILTER_WXPAY_TEMPLATE_INFO, 
                    DatadicKey.filterWxPayTemplateInfo.WECHAT_TMPMSG_PROMOTION_CHECK_RESULT);
    		
    		taskExecutor.execute(new WechatCheckTemplateMsg(appid, promotionUser.getTerminalOpenid(), null, template_id)
    				.initSyncMsg(promotionUser.getCheckStatus()).initApp(paApplet_appid, pagePath));
    		
			model.addAttribute("errMsg", "操作成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "操作失败");
			ex.printStackTrace();
		}
		return "forward:showPromotionUserList.do"; 
	}
	
	/**
	 * 	删除
	 */
	@RequestMapping("/deleteById")
	public String deleteById(HttpSession session, String infoKey, Model model) {
		try {
			promotionUserService.deleteById(infoKey);
			model.addAttribute("errMsg", "操作成功");
		} catch (Exception ex) {
			model.addAttribute("errMsg", "操作失败");
			ex.printStackTrace();
		}
		return "forward:showPromotionUserList.do"; 
	}
	
	/**
     *	 跳转到导入页面
     */
    @RequestMapping("/toImportPage")
    public String toImportPage(Model model, HttpSession session) {
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        return "ticket/importPromotionUser";
    }
    
    /**
     * 	导入
     * @param model
     * @param session
     * @param batchFile
     * @return
     */
    @RequestMapping("importMessage")
    @ResponseBody
    public String importMessage(Model model, HttpSession session, @RequestParam("batchFile") MultipartFile batchFile) {
        HashMap<String, String> map = new HashMap<String, String>();
        String msg = null;
        String result = null;
        try {

            SysUserBasis currentUser = this.getUserBasis(session);
            List<List<Object>> readExcel = checkFile(batchFile);
            msg = promotionUserService.savePromotionUser(readExcel, currentUser);

        } catch (Exception e) {
            log.error("", e);
            map.put("type", "2");
            map.put("errorMsg", "上传表格格式存在异常，请检查上传表格格式，例如：1、表格中第一列或第一行是否是空行或列。2、表格的数据中间是否有空行。3、sheet页是否是排在第一个，4、可点击下载模板，将数据复制进模板里，注意选择数据时尽量只选中需要复制的数据，在粘贴进模板表格时，尽量选择值粘贴");
            result = JsonUtil.toJson(map);
            return result;
        }

        if (!StringUtils.isNotBlank(msg)) {
            map.put("successMsg", msg);
            map.put("url", "/terminalMessageAction/serarchTerminalByCondition.do");
            map.put("type", "1");
            result = JsonUtil.toJson(map);
            return result;
        } else {
            map.put("errorMsg", msg);
            map.put("type", "2");
            result = JsonUtil.toJson(map);
            return result;
        }
    }
    
    /**
     * 导出查询结果
     * 
     * @return
     */
    @RequestMapping("/exportPromotionUserList")
    public void exportPromotionUserList(HttpSession session, HttpServletResponse response, String queryParam, Model model) {
        try {
            // 是否拥有战区角色,如果拥有则判断操作促销员的按钮显示权限
			String warAreaFlag = validRole(session, "98") ? "1" : "0";
			 VpsTicketPromotionUser queryBean = new VpsTicketPromotionUser(queryParam, warAreaFlag);
			if("1".equals(warAreaFlag)) {
				queryBean.setWarAreaName(this.getUserBasis(session).getUserName().substring(0, 4));
			}
			if("姓名/手机号/门店名称".equals(queryBean.getSearchVal())) {
				queryBean.setSearchVal(null);
			}
			if(StringUtils.isNotBlank(queryBean.getProvinceCode())) {
	    		queryBean.setProvince(areaService.findById(queryBean.getProvinceCode()).getAreaName());
	    	}
	    	if(StringUtils.isNotBlank(queryBean.getCityCode())) {
	    		queryBean.setCity(areaService.findById(queryBean.getCityCode()).getAreaName());
	    	}
	    	if(StringUtils.isNotBlank(queryBean.getCountyCode())) {
	    		queryBean.setCounty(areaService.findById(queryBean.getCountyCode()).getAreaName());
	    	}
         			
            promotionUserService.exportPromotionUser(queryBean, response);
        } catch (Exception e) {
            log.error("导出查询结果下载失败", e);
        }
    }
    
    /**
     * excel解析
     *
     * @param batchFile
     * @return
     * @throws IOException
     */
    public List<List<Object>> checkFile(MultipartFile batchFile) throws IOException {
        String importfileFileName = batchFile.getOriginalFilename();
        InputStream input = batchFile.getInputStream();
        if (input.available() > 0) {
            List<List<Object>> readExcel
                    = ReadExcel.readExcel(input, importfileFileName.substring(importfileFileName.lastIndexOf(".") + 1), 11);

            return readExcel;
        }
        return null;
    }
}
