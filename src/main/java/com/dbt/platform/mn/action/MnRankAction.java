
package com.dbt.platform.mn.action;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.service.SysAreaService;
import com.dbt.platform.bottlecap.bean.VpsVcodeBottlecapActivityCogInfo;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.mn.bean.*;
import com.dbt.platform.mn.dto.RankReportAllExcel;
import com.dbt.platform.mn.dto.TerminalRankActivityCogVO;
import com.dbt.platform.mn.service.*;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.vpointsshop.dto.DownloadData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 蒙牛排行榜
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/mnRank")
public class MnRankAction extends BaseAction{

	@Autowired
	private ITerminalRankActivityCogService terminalRankActivityCogService;
	@Autowired
	private IRankReportAllService rankReportAllService;
	@Autowired
	private ITerminalRankActivityAreaCogService terminalRankActivityAreaCogService;

	@Autowired
	private SkuInfoService skuInfoService;
	@Autowired
	private IMainAreaInfoMnService mainAreaInfoMnService;
	@Autowired
	private MnDepartmentService mnDepartmentService;
	
	/**
    * 	查询排行榜活动
    */
	@RequestMapping("/showRankList")
	public String queryListByMap(HttpSession session, String queryParam, String pageParam, Model model) {

		try {
			SysUserBasis currentUser = this.getUserBasis(session);
			TerminalRankActivityCogVO queryBean = new TerminalRankActivityCogVO(queryParam);
			queryParam = StringUtils.defaultIfBlank(queryParam, "");
			PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
			IPage<TerminalRankActivityCogVO> page = terminalRankActivityCogService.queryPageVo(pageInfo, queryBean);
			model.addAttribute("currentUser", currentUser);
			model.addAttribute("resultList", page.getRecords());
			model.addAttribute("showCount", page.getTotal());
			model.addAttribute("startIndex", pageInfo.getStartCount());
			model.addAttribute("countPerPage", pageInfo.getPagePerCount());
			model.addAttribute("currentPage", pageInfo.getCurrentPage());
			model.addAttribute("params", queryBean);
			model.addAttribute("pageParams", pageParam);
			model.addAttribute("queryParam", queryParam);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "mn/rank/showRankList";
	}

	/**
    * 	排行榜活动新增页面
    */
	@RequestMapping("/showRankAdd")
	public String showRankAdd(HttpSession session, Model model) {
		SysUserBasis currentUser = this.getUserBasis(session);
		// 该企业下的skuList
		List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
		initArea(model);
		model.addAttribute("skuList", skuList);
		return "mn/rank/showRankAdd";
	}
	/**
    * 	排行榜活动修改页面
    */
	@RequestMapping("/showRankEdit")
	public String showRankEdit(HttpSession session,String infoKey,String tabFlag, Model model) {
		SysUserBasis currentUser = this.getUserBasis(session);
		// 该企业下的skuList
		TerminalRankActivityCogVO activityCog =  terminalRankActivityCogService.showRankEdit(infoKey);
		model.addAttribute("isBegin", !"0".equals(activityCog.getIsBegin()));
		List<TerminalRankActivityAreaCog> areaList = terminalRankActivityAreaCogService.list(new QueryWrapper<TerminalRankActivityAreaCog>().eq(TerminalRankActivityAreaCog.TERMINAL_RANK_INFO_KEY, infoKey));
		List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
		initArea(model,areaList);
		model.addAttribute("skuList", skuList);
		model.addAttribute("tabFlag", tabFlag);
		model.addAttribute("activityCog", activityCog);

		return "mn/rank/showRankEdit";
	}

	/**
	 * 	查询排行榜活动
	 */
	@RequestMapping("/rankSave")
	public String rankSave(HttpSession session,TerminalRankActivityCogVO cogVO, Model model) {
		SysUserBasis currentUser = this.getUserBasis(session);
		try{
			cogVO.setCreateUser(currentUser.getUserName());
			cogVO.setUpdateUser(currentUser.getUserName());
			terminalRankActivityCogService.saveRank(cogVO);
			model.addAttribute("errMsg", "新增成功");
		}catch (Exception e){
			e.printStackTrace();
			model.addAttribute("errMsg", "新增成功");
		}
		return "forward:showRankList.do";
	}
	/**
	 * 	查询排行榜活动
	 */
	@RequestMapping("/rankUpdate")
	public String rankUpdate(HttpSession session,TerminalRankActivityCogVO cogVO, Model model) {
		SysUserBasis currentUser = this.getUserBasis(session);
		try{
			cogVO.setCreateUser(currentUser.getUserName());
			cogVO.setUpdateUser(currentUser.getUserName());
			terminalRankActivityCogService.saveUpdate(cogVO);
			model.addAttribute("errMsg", "修改成功");
		}catch (Exception e){
			e.printStackTrace();
			model.addAttribute("errMsg", "修改失败");
		}
		return "forward:showRankList.do";
	}
	/**
	 * 	删除
	 */
	@RequestMapping("/deleteRank")
	public String deleteRank(HttpSession session,String infoKey, Model model) {
		SysUserBasis currentUser = this.getUserBasis(session);
		try{
			terminalRankActivityCogService.removeById(infoKey);
			model.addAttribute("errMsg", "删除成功");
		}catch (Exception e){
			e.printStackTrace();
		}
		return "forward:showRankList.do";
	}

	/**
	 * 	活动名称校验
	 * @return true 重复  false 不重复
	 */
	@RequestMapping("/checkActivityName")
	@ResponseBody
	public boolean checkActivityName(HttpSession session,String infoKey,String activityName) {
		try{
			if (StringUtils.isNotEmpty(infoKey)) {
				QueryWrapper<TerminalRankActivityCog> queryWrapper = new QueryWrapper<TerminalRankActivityCog>();
				queryWrapper.eq(TerminalRankActivityCog.ACTIVITY_NAME, activityName);
				queryWrapper.ne(TerminalRankActivityCog.INFO_KEY, infoKey);
				List<TerminalRankActivityCog> list = terminalRankActivityCogService.list(queryWrapper);
				return list.size() >0;
			}else{
				QueryWrapper<TerminalRankActivityCog> queryWrapper = new QueryWrapper<TerminalRankActivityCog>();
				queryWrapper.eq(TerminalRankActivityCog.ACTIVITY_NAME, activityName);
				List<TerminalRankActivityCog> list = terminalRankActivityCogService.list(queryWrapper);
				return list.size() >0;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 	导出
	 */
	@RequestMapping("/rankExport")
	public void rankExport(HttpSession session, String infoKey, HttpServletResponse response) {
		SysUserBasis currentUser = this.getUserBasis(session);
		try{
			// 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
			try {
				TerminalRankActivityCog byId = terminalRankActivityCogService.getById(infoKey);
				if(byId!=null){
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setCharacterEncoding("utf-8");
					// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
					String fileName = URLEncoder.encode(byId.getActivityName(), "UTF-8").replaceAll("\\+", "%20");
					response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
					// 这里需要设置不关闭流
					QueryWrapper<RankReportAll> rankReportAllQueryWrapper = new QueryWrapper<>();
					rankReportAllQueryWrapper.eq(RankReportAll.ACTIVITYKEY, infoKey);
					rankReportAllQueryWrapper.orderByAsc(RankReportAll.RANKNUM);
					List<RankReportAll> list = rankReportAllService.list(rankReportAllQueryWrapper);
					List<RankReportAllExcel> collect = list.stream().map(rank -> {
						return new RankReportAllExcel(rank);
					}).collect(Collectors.toList());
					EasyExcel.write(response.getOutputStream(), RankReportAllExcel.class).autoCloseStream(Boolean.FALSE).sheet("数据")
							.doWrite(collect);
				}
			} catch (Exception e) {
				// 重置response
				response.reset();
				response.setContentType("application/json");
				response.setCharacterEncoding("utf-8");
				Map<String, String> map = MapUtils.newHashMap();
				map.put("status", "failure");
				map.put("message", "下载文件失败" + e.getMessage());
				response.getWriter().println(JSON.toJSONString(map));
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public void initArea( Model model){
		initArea( model, new ArrayList<>());
	}
	public void initArea(Model model,List<TerminalRankActivityAreaCog> areaCogList){
		boolean boo = false;
		Object isBegin = model.getAttribute("isBegin");
		if(isBegin!=null){
			boo = (boolean) isBegin;
		}
		// 返回配置的省级区域
			List<MainAreaInfoMn> areaLst = mainAreaInfoMnService.list();
			if(CollectionUtils.isNotEmpty(areaLst)){
				// 山东省-济南市-平阴县;山东省-青岛市；河南省；
				JSONObject json = null;
				List<JSONObject> areaList = new ArrayList<>();
				for (MainAreaInfoMn item : areaLst) {
					json = new JSONObject();
					json.put("id", item.getId());
					json.put("pId", item.getPid());
					json.put("name", item.getName());
					json.put("checked", false);
					if(boo){
						json.put("chkDisabled", true);
					}
					String id = String.valueOf(item.getId());
					for (TerminalRankActivityAreaCog areaCog : areaCogList) {
						if (id.equals(areaCog.getCity())
						|| id.equals(areaCog.getProvince())
						|| id.equals(areaCog.getCounty())) {
							json.put("checked", true);
							json.put("open", true);
							break;
						}
					}
					areaList.add(json);
				}
				model.addAttribute("areaJson", JSON.toJSONString(areaList));
			}

			List<MnDepartmentEntity> mnDepartmentEntities = mnDepartmentService.queryListByMap(-1, 0);
			if(CollectionUtils.isNotEmpty(mnDepartmentEntities)){
				//
				JSONObject json = null;
				List<JSONObject> areaList = new ArrayList<>();
				for (MnDepartmentEntity item : mnDepartmentEntities) {
					if(item.getLevel()<4){
						continue;
					}
					json = new JSONObject();
					json.put("id", item.getId());
					json.put("pId", item.getPid());
					json.put("name", item.getDep_name());
					json.put("checked", false);

					if(boo){
						json.put("chkDisabled", true);
					}
					String id = String.valueOf(item.getId());
					for (TerminalRankActivityAreaCog areaCog : areaCogList) {
						if (id.equals(areaCog.getLevelCode2())
								|| id.equals(areaCog.getLevelCode3())
								|| id.equals(areaCog.getLevelCode4())
								|| id.equals(areaCog.getLevelCode5())) {
							json.put("checked", true);
							json.put("open", true);
							break;
						}
					}
					areaList.add(json);
				}
				model.addAttribute("mnDepartmentJson", JSON.toJSONString(areaList));
			}
		if(CollectionUtils.isNotEmpty(mnDepartmentEntities)){
			// 山东省-济南市-平阴县;山东省-青岛市；河南省；
			JSONObject json = null;
			List<JSONObject> areaList = new ArrayList<>();
			for (MnDepartmentEntity item : mnDepartmentEntities) {
				if(item.getLevel()<4 || item.getLevel()>5 ){
					continue;
				}
				String id = String.valueOf(item.getId());
				json = new JSONObject();
				json.put("id", id);
				json.put("pId", item.getPid());
				json.put("name", item.getDep_name());
				json.put("checked", false);

				if(boo){
					json.put("chkDisabled", true);
				}
				for (TerminalRankActivityAreaCog areaCog : areaCogList) {
					if (id.equals(areaCog.getLevelCode2())
							|| id.equals(areaCog.getLevelCode3())
							|| id.equals(areaCog.getLevelCode4())
							|| id.equals(areaCog.getLevelCode5())) {
						json.put("checked", true);
						json.put("open", true);
						break;
					}
				}
				areaList.add(json);
			}
			model.addAttribute("mnDepartmentJsonLeave4_5", JSON.toJSONString(areaList));
		}

	}

}
