package com.dbt.seckill.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.platform.activity.bean.VcodeActivityHotAreaCog;
import com.dbt.platform.activity.service.VcodeActivityHotAreaCogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.IdWorker;
import com.dbt.framework.util.MathUtil;
import com.dbt.framework.util.QrCodeUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.activity.bean.VcodeActivityMoneyImport;
import com.dbt.platform.activity.util.VcodeActivityMoneyExcel;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.seckill.bean.SeckillActivityBean;
import com.dbt.seckill.bean.SeckillActivityQuery;
import com.dbt.seckill.bean.SeckillCogMoney;
import com.dbt.seckill.service.SeckillService;

@Controller
@RequestMapping("/seckill")
public class SeckillAction extends BaseAction{
	@Autowired
	private SeckillService secService;
	@Autowired
	private VcodeActivityHotAreaCogService hotAreaCogService;
	/**
	 * 查询秒杀活动
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @param session
	 * @return
	 */
	@RequestMapping("/getSeckillList")
	public String getSeckillList(Model model, String pageParam,String queryParam){
		PageOrderInfo info = new PageOrderInfo(pageParam);
		SeckillActivityQuery bean=new SeckillActivityQuery(queryParam);
		int countAll=0;
		List<SeckillActivityBean> secList=null;
		try {
			secList = secService.getSeckillList(info,bean);
			countAll = secService.getSeckillCount(info, bean);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
		model.addAttribute("secList", secList);
		model.addAttribute("showCount", countAll);
		model.addAttribute("startIndex", info.getStartCount());
		model.addAttribute("countPerPage", info.getPagePerCount());
		model.addAttribute("currentPage", info.getCurrentPage());
        model.addAttribute("orderCol", info.getOrderCol());
        model.addAttribute("orderType", info.getOrderType());
        model.addAttribute("queryParam", queryParam);
		return "seckill/showSeckillList";
	}
	@RequestMapping("/addSeckil")
	public String addSeckil(Model model,HttpSession session, 
			@RequestParam("batchFile") MultipartFile batchFile, SeckillActivityBean bean){
		String result = "addSuccess";
		try {
			SysUserBasis user=getUserBasis(session);
			String userKey=user.getUserKey();
			bean.fillFields(userKey);
			String ruleKey=UUIDTools.getInstance().getUUID();
			String seckillId=IdWorker.getCommonId("MS");
			bean.setSeckillId(seckillId);
			bean.setSeckillRuleKey(ruleKey);
			List<VcodeActivityMoneyImport> list=checkFile(batchFile);
			if(list==null||list.size()==0){
				result = "addFalse";
				model.addAttribute("refresh", result);
				return "forward:/seckill/getSeckillList.do";
			}
			result=secService.addSeckill(bean);
			result=secService.addCogMoney(list,ruleKey);
		} catch (Exception e) {
			result = "addFalse";
			e.printStackTrace();
		}
		model.addAttribute("refresh", result);
		return "forward:/seckill/getSeckillList.do";
	}
	/**
	 * 名称重复验证
	 * @param seckillName
	 * @return
	 */
	@RequestMapping("/checkName")
	@ResponseBody
	public String checkName(String seckillName,String seckillId) {
		try {
			int countAll = secService.checkName(seckillName,seckillId);
			if(countAll>0) {
				return "FAIL";
			}
			return "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FAIL";
	}
	@RequestMapping("/changeStatus")
	public String changeStatus(Model model,String seckillId,String pageParam,String queryParam) {
		PageOrderInfo info = new PageOrderInfo(null);
		SeckillActivityQuery bean=new SeckillActivityQuery();
		bean.setSeckillId(seckillId);
		try {
			secService.changeStatus(info,bean);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
		model.addAttribute("pageParam",pageParam);
		model.addAttribute("queryParam",queryParam);
		return "forward:/seckill/getSeckillList.do";
	}
	/**
	 * 详情
	 * @param model
	 * @param pageParam
	 * @param queryParam
	 * @return
	 */
	@RequestMapping("/getSeckillDetail")
	public String getSeckillDetail(Model model,String seckillId,HttpSession session){
		PageOrderInfo info = new PageOrderInfo(null);
		SeckillActivityQuery bean=new SeckillActivityQuery();
		bean.setSeckillId(seckillId);
		try {
			List<VcodeActivityHotAreaCog> hotAreaList = null;
			hotAreaList = hotAreaCogService.findHotAreaListByAreaCode("000000");
			model.addAttribute("hotAreaList", hotAreaList);

			model.addAttribute("projectServerName", DbContextHolder.getDBType());
			secService.getSeckillDetail(info, bean, model);
		} catch (Exception e) {
			model.addAttribute("errorMsg","查询异常");
			e.printStackTrace();
		}
				
		return "seckill/editSeckill";
	}
	/**
	 * 更新批次
	 * @param model
	 * @param session
	 * @param batchFile
	 * @param batch
	 * @return
	 */
	@RequestMapping("/updateSeckill")
	public String updateSeckill(Model model,HttpSession session,
			@RequestParam("batchFile") MultipartFile batchFile,SeckillActivityBean bean){
		PageOrderInfo info = new PageOrderInfo(null);
		SeckillActivityQuery queryBean=new SeckillActivityQuery();
		queryBean.setSeckillId(bean.getSeckillId());
		SeckillActivityBean oldBean = secService.getSeckillList(info,queryBean).get(0);
		
		String oldRuleKey=oldBean.getSeckillRuleKey();
		bean.setUpdateTime(DateUtil.getDateTime());
		String result="addSuccess";
		try {
			List<VcodeActivityMoneyImport> list=checkFile(batchFile);
			if(list!=null&&list.size()>0){
				String newRuleKey=UUIDTools.getInstance().getUUID();
				bean.setSeckillRuleKey(newRuleKey);
				result=secService.addCogMoney(list,newRuleKey);
			}
			result=secService.updateSeckill(bean);
			if(list!=null&&list.size()>0) {
				secService.delCogMoney(oldRuleKey);
				secService.deleCogMoneyCache(oldBean);
			}
		} catch (Exception e) {
			result = "addFalse";
			e.printStackTrace();
		}
		model.addAttribute("refresh", result);
		return "forward:/seckill/getSeckillList.do";
	}
	/**
	 * 新增跳转
	 * @return
	 */
	@RequestMapping("/seckillAdd")
	public String seckillAdd(Model model,HttpSession session){
		// 获取该区域下的热区
		List<VcodeActivityHotAreaCog> hotAreaList = null;
		hotAreaList = hotAreaCogService.findHotAreaListByAreaCode("000000");
		model.addAttribute("hotAreaList", hotAreaList);
		model.addAttribute("projectServerName", DbContextHolder.getDBType());
		return "seckill/addSeckill";
	}
	/**
	 * 删除批次
	 * @param goodsId
	 * @return
	 */
//	@RequestMapping("/delSeckill")
//	public String delSeckill(String seckillId,Model model){
//		String result = "deleteSuccess";
//		try {
//			result=secService.delSeckill(seckillId);
//		} catch (Exception e) {
//			result = "deleteFalse";
//			e.printStackTrace();
//		}
//		model.addAttribute("refresh", result);
//		return "forward:/seckill/getSeckillList.do";
//	}
	@RequestMapping("/beforeCheckFile")
	@ResponseBody
	public String beforeCheckFile(@RequestParam("file") MultipartFile batchFile,HttpServletRequest request) {
		if(!batchFile.isEmpty()) {
			try {
				List<VcodeActivityMoneyImport> excelList = VcodeActivityMoneyExcel.importExcel(batchFile, SeckillService.entities);
				if(excelList.isEmpty()) {
					return "error:文件内容为空";
				}
				double allMoney=0;
				int allCount=0;
				int allRangeVal=0;
				List<SeckillCogMoney> moneyList=new ArrayList<SeckillCogMoney>();
				for (VcodeActivityMoneyImport seckillCogMoney : excelList) {
					long rangeVal=seckillCogMoney.getRangeVal();
					allRangeVal+=rangeVal;
				}
				for (VcodeActivityMoneyImport seckillCogMoney : excelList) {
					SeckillCogMoney money=new SeckillCogMoney();
					long rangeVal=seckillCogMoney.getRangeVal();
					allCount+=seckillCogMoney.getAmounts();
					allMoney+=Double.valueOf(seckillCogMoney.getMoney())*seckillCogMoney.getAmounts();
					double per = 0;
					if(rangeVal==0){
						per=0.0;
					}else{
						 per = (double)rangeVal / allRangeVal * 100;
					}
					String percentage = String.valueOf(MathUtil.round(per, 4));
					money.setPrizeType(seckillCogMoney.getPrizeType());
					money.setVcodeMoney(Double.valueOf(seckillCogMoney.getMoney()));
					money.setCogamounts(seckillCogMoney.getAmounts().intValue());
					money.setRangeVal(seckillCogMoney.getRangeVal().intValue());
					money.setPercent(percentage+"%");
					moneyList.add(money);
				}
				SeckillCogMoney moneyAll=new SeckillCogMoney();
				moneyAll.setPrizeType("--");
				moneyAll.setVcodeMoney(Double.valueOf(String.format("%.2f",allMoney)));
				moneyAll.setCogamounts(allCount);
				moneyAll.setRangeVal(allRangeVal);
				moneyAll.setPercent("100%");
				moneyList.add(moneyAll);
				return JSON.toJSONString(moneyList);
			} catch (BusinessException e) {
			    log.error(e);
			    return "error:" + e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				return "error:解析出错";
			}
		}else {
			return "error:文件为空";
		}
	}
	/**
	 * excel解析
	 * @param batchFile
	 * @return
	 * @throws IOException
	 */
	public List<VcodeActivityMoneyImport> checkFile(MultipartFile batchFile) throws IOException{
		if(!batchFile.isEmpty()) {
			List<VcodeActivityMoneyImport> excelList;
			try {
				excelList = VcodeActivityMoneyExcel.importExcel(batchFile, SeckillService.entities);
				return excelList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	@RequestMapping("/checkImg")
	@ResponseBody
	public String uploadVcodeImg(String seckillId,HttpServletRequest request,HttpServletResponse response) {
		PageOrderInfo info = new PageOrderInfo(null);
		SeckillActivityQuery queryBean=new SeckillActivityQuery();
		queryBean.setSeckillId(seckillId);
		String path="";
		String filePath="";
		try {
			SeckillActivityBean bean = secService.getSeckillList(info,queryBean).get(0);
//			filePath = Constant.filePath+"upload/";
			filePath="/data/upload/";
			filePath=filePath+bean.getSeckillVcode()+".png";
			path="/upload/"+bean.getSeckillVcode()+".png";
			File file =new File(filePath);
			if(!file.exists()) {
				QrCodeUtil.encodeQRCodeImage(bean.getSeckillUrl(), null, filePath, 283, 283, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
}
