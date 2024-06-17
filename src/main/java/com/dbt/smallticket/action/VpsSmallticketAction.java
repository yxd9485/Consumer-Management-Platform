package com.dbt.smallticket.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.dbt.framework.zone.bean.SysAreaM;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CheckUtil;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.smallticket.bean.VpsTicketRecord;
import com.dbt.smallticket.bean.VpsTicketTerminalInsideDetail;
import com.dbt.smallticket.dao.VpsTicketRecordDao;
import com.dbt.smallticket.dao.VpsTicketRecordSkuDetailDao;
import com.dbt.smallticket.service.VpsTicketPromotionUserService;
import com.dbt.smallticket.service.VpsTicketRecordService;
import com.dbt.smallticket.service.VpsTicketTerminalInsideDetailService;

/**
 * 小票Action
 */
@Controller
@RequestMapping("/smallticketAction")
public class VpsSmallticketAction extends BaseAction{

	@Autowired
	private VpsTicketRecordService vpsTicketRecordService;
	@Autowired
	private VpsTicketRecordDao ticketRecordDao;
	@Autowired
	private VpsTicketRecordSkuDetailDao ticketRecordSkuDetailDao;
	@Autowired
	private VpsTicketTerminalInsideDetailService insideDetailServic;
	@Autowired
	private  SysAreaService sysAreaService;
	@Autowired
	private VpsTicketPromotionUserService promotionUserService;
	@Autowired
	private VcodeActivityVpointsCogService vpointsCogService;
	
	/**
	 * 小票列表
	 */
	@RequestMapping("/showTicketList")
	public String showTicketList(HttpSession session, String pageParam, String queryParam, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);	
			VpsTicketRecord queryBean = new VpsTicketRecord(queryParam);
			queryBean.setProvince(StringUtils.isBlank(queryBean.getProvince())?"":sysAreaService.findById(queryBean.getProvince()).getAreaName());
			List<SysAreaM> provinceList = sysAreaService.findByParentIdForProject("0", false);
			provinceList.remove(0);
			List<VpsTicketRecord> resultList = vpsTicketRecordService.queryForLst(queryBean, pageInfo);
			int countResult = vpsTicketRecordService.queryForCount(queryBean);
			
			// 奖项类型
	        Map<String, String> prizeTypeMap = vpointsCogService.queryAllPrizeType(false, true, false, false, false, false, null);
			model.addAttribute("prizeTypeMap", prizeTypeMap);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", resultList);
			model.addAttribute("showCount", countResult);
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("orderCol", pageInfo.getOrderCol());
			model.addAttribute("orderType", pageInfo.getOrderType());
			model.addAttribute("queryParam", queryParam);
			model.addAttribute("provinceList", provinceList);
			model.addAttribute("pageParam", pageParam);
		} catch (Exception ex) {
			log.error(ex);
		}
		return "ticket/showTicketList";
	}
	
	
	/**
     * 小票详情
     */
	@RequestMapping("/showEdit")
	public String showEdit(HttpSession session,int isShow, String infoKey, Model model) {
		SysUserBasis currentUser = this.getUserBasis(session);
		VpsTicketRecord vpsTicketRecord =  vpsTicketRecordService.findById(infoKey);
		boolean isInputFlag = true;
		if(isShow == 1){
			if(CheckUtil.isEmpty(vpsTicketRecord.getProjressName())){
				vpsTicketRecord.setProjressName(currentUser.getUserName());
				vpsTicketRecord.fillFields(currentUser.getUserKey());
				ticketRecordDao.update(vpsTicketRecord);							
			}else if (!vpsTicketRecord.getProjressName().equals(currentUser.getUserName())) {
				isInputFlag = false;
			}
		}
		vpsTicketRecord.setDetailLst(ticketRecordSkuDetailDao.queryRecordByTicketKey(infoKey));
		model.addAttribute("isInputFlag", isInputFlag);
		model.addAttribute("vpsTicketRecord", vpsTicketRecord);
		model.addAttribute("insideLst", ticketRecordDao.findTerminalInside(vpsTicketRecord.getProvince()));
		model.addAttribute("skuLst", ticketRecordDao.findSkuLst());
		model.addAttribute("projressName", vpsTicketRecord.getProjressName());
		model.addAttribute("promotionUser", promotionUserService.findByUserKey(vpsTicketRecord.getPromotionUserKey()));
		model.addAttribute("errMsg", !isInputFlag?"该小票已被"+vpsTicketRecord.getProjressName()+"审核!":"");
		return isShow == 0 ? "ticket/showTicket" :"ticket/editTicket";
	}
	
	
	/**
	 * 根据key模糊搜查
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/searchLst")
	@ResponseBody
	public String searchLst(VpsTicketTerminalInsideDetail detail) {
		try {
			List<VpsTicketTerminalInsideDetail> searchList = insideDetailServic.searchLst(detail);
			return JSON.toJSONString(searchList);
		} catch (Exception e) {
			e.printStackTrace();
			return "操作异常";
		}
	}

	
	/**
	 * 根据key查询门店是否存在或者编号是否重复
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/checkTicket")
	@ResponseBody
	public String checkTicket(VpsTicketRecord detail) {
		try {
			return JSON.toJSONString(vpsTicketRecordService.checkTicket(detail));
		} catch (Exception e) {
			e.printStackTrace();
			return "操作异常";
		}
	}
	
	
	/**
	 * 修改小票
	 */
	@RequestMapping("/doTicketRecordEdit")
	public String doTicketRecordEdit(HttpSession session, VpsTicketRecord vpsTicketRecord, Model model) {
		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			vpsTicketRecordService.initRecordSkuDetailLst(vpsTicketRecord);
			vpsTicketRecordService.updateicketRecord(vpsTicketRecord, currentUser);
			try {
			    vpsTicketRecordService.executePromotionMoney(vpsTicketRecord.getInfoKey(), currentUser);
			    model.addAttribute("errMsg", "操作成功");
            } catch (Exception e) {
                if (e instanceof BusinessException) {
                    model.addAttribute("errMsg", "操作成功" + e.getMessage());
                } else {
                    model.addAttribute("errMsg", "操作成功, 推广激励发放失败");
                    e.printStackTrace();
                }
            }
		} catch (BusinessException ex) {
			model.addAttribute("errMsg",ex.getMessage());
			ex.printStackTrace();
		}catch (Exception ex) {
			model.addAttribute("errMsg", "编辑失败");
            log.error("编辑小票失败", ex);
			ex.printStackTrace();
		}
		return "forward:showTicketList.do"; 
	}

    
    /**
     * 送审小票
     */
	@ResponseBody
    @RequestMapping("/doRemind")
    public String deRemind(HttpSession session, String infoKey) {
	    Map<String, Object> resultMap = new HashMap<>();
        try {
            vpsTicketRecordService.sendTicketAudit(infoKey, false);
            resultMap.put("errMsg", "送审成功");
        }catch (Exception ex) {
            log.error("送审失败", ex);
            resultMap.put("errMsg", "送审失败");
        }
        return JSON.toJSONString(resultMap); 
    }
	
}
