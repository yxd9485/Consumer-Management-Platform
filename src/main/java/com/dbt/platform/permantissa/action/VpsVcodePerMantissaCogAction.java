package com.dbt.platform.permantissa.action;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dbt.datasource.util.DbContextHolder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeActivityMoneyImport;
import com.dbt.platform.activity.service.VcodeActivityVpointsCogService;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.permantissa.bean.VpsVcodePerMantissaCog;
import com.dbt.platform.permantissa.service.VpsVcodePerMantissaCogService;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 逢尾数规则Action
 */
@Controller
@RequestMapping("/perMantissaCog")
public class VpsVcodePerMantissaCogAction extends BaseAction{
    
    private Logger log = Logger.getLogger(VpsVcodePerMantissaCogAction.class);

	@Autowired
	private VpsVcodePerMantissaCogService perMantissaCogService;
	@Autowired
	private VcodeActivityVpointsCogService vpointsCogService;
	@Autowired
	private SkuInfoService skuInfoService;
	
	/**
	 * 获取列表
	 */
	@RequestMapping("/showPerMantissaCogList")
	public String showPerMantissaCogList(HttpSession session, 
	            String tabsFlag, String queryParam, String pageParam, Model model) {
		try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsVcodePerMantissaCog queryBean = new VpsVcodePerMantissaCog(queryParam);
            SysUserBasis currentUser = this.getUserBasis(session);
            queryBean.setCompanyKey(currentUser.getCompanyKey());
            queryBean.setTabsFlag(StringUtils.isBlank(tabsFlag) ? "1" : tabsFlag);
            
			List<VpsVcodePerMantissaCog> resultList = perMantissaCogService.queryForList(queryBean, pageInfo);
			int countResult = perMantissaCogService.queryForCount(queryBean);
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
            model.addAttribute("tabsFlag", queryBean.getTabsFlag());
			model.addAttribute("projectServerName", DbContextHolder.getDBType());
		} catch (Exception ex) {
		    log.error(ex.getMessage(), ex);
		}
		return "vcode/permantissa/showPerMantissaCogList";
	}
	
	/**
	 * 跳转新增页面
	 */
	@RequestMapping("/showPerMantissaCogAdd")
	public String showPerMantissaCogAdd(HttpSession session, Model model) throws Exception {
	    SysUserBasis userBasis = this.getUserBasis(session);
	    model.addAttribute("skuLst", skuInfoService.loadSkuListByCompany(userBasis.getCompanyKey()));
	    model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(true, true, true, false, true, false, null));
		model.addAttribute("projectServerName", DbContextHolder.getDBType());
	    
        return "vcode/permantissa/showPerMantissaCogAdd";
	}
	
	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("/showPerMantissaCogEdit")
	public String showPerMantissaCogEdit(HttpSession session, String infoKey, Model model) throws Exception {
        SysUserBasis userBasis = this.getUserBasis(session);
        List<String> couponNoLst = vpointsCogService.queryCouponNoByRebateRuleKey(infoKey);
        model.addAttribute("perMantissaCog", perMantissaCogService.findById(infoKey));
        model.addAttribute("skuLst", skuInfoService.loadSkuListByCompany(userBasis.getCompanyKey()));
        model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(true, true, true, false, true, false, couponNoLst));
        model.addAttribute("vpointsCogLst", vpointsCogService.queryVpointsCogByrebateRuleKey(infoKey));
		model.addAttribute("projectServerName", DbContextHolder.getDBType());
	    return "vcode/permantissa/showPerMantissaCogEdit";
	}
	
	/**
	 * 跳转查看页面
	 */
	@RequestMapping("/showPerMantissaCogView")
	public String showPerhundredCogView(HttpSession session, String infoKey, Model model) throws Exception {
        SysUserBasis userBasis = this.getUserBasis(session);
        List<String> couponNoLst = vpointsCogService.queryCouponNoByRebateRuleKey(infoKey);
        model.addAttribute("perMantissaCog", perMantissaCogService.findById(infoKey));
        model.addAttribute("skuLst", skuInfoService.loadSkuListByCompany(userBasis.getCompanyKey()));
        model.addAttribute("prizeTypeMap", vpointsCogService.queryAllPrizeType(true, true, true, false, true, false, couponNoLst));
        model.addAttribute("vpointsCogLst", vpointsCogService.queryVpointsCogByrebateRuleKey(infoKey));
        
        return "vcode/permantissa/showPerMantissaCogView";
	}
	
	/**
	 * 添加记录
	 */
	@RequestMapping("/doPerMantissaCogAdd")
	public String doPerMantissaCogAdd(HttpSession session, 
	        @RequestParam("prizeFile")MultipartFile prizeFlile, VpsVcodePerMantissaCog perMantissaCog, Model model){
		try{
			SysUserBasis userBasis = this.getUserBasis(session);
			perMantissaCogService.addPerMantissaCog(perMantissaCog, userBasis, prizeFlile);
			model.addAttribute("errMsg", "添加成功");
		} catch (Exception ex) {
		    if (ex instanceof BusinessException) {
		        model.addAttribute("errMsg", "添加失败," + ex.getMessage());
		    } else {
		        model.addAttribute("errMsg", "添加失败");
		    }
			log.error(ex.getMessage(), ex);
		}
		return "forward:showPerMantissaCogList.do";
	}
	
	/**
	 * 修改记录
	 */
	@RequestMapping("/doPerMantissaCogEdit")
	public String doPerMantissaCogEdit(HttpSession session, 
	        @RequestParam("prizeFile")MultipartFile prizeFlile, VpsVcodePerMantissaCog perMantissaCog, Model model){
		try {
			SysUserBasis userBasis = this.getUserBasis(session);
			perMantissaCogService.editPerMantissaCog(perMantissaCog, userBasis, prizeFlile);
			
			// 逢百规则当天有效标志
			String redisKey = CacheUtilNew.cacheKey.dotRedpacketCog
			            .DOT_RULE_IS_VALID + Constant.DBTSPLIT + perMantissaCog.getInfoKey();
			RedisApiUtil.getInstance().del(true, redisKey);
	        RedisApiUtil.getInstance().del(true, redisKey + Constant.DBTSPLIT + DateUtil.getDate());
	        // 删除规则缓存
            CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_PERMANTISSA_COG + Constant.DBTSPLIT + DateUtil.getDateTime(DateUtil.DEFAULT_DATE_FORMAT_SHT));
            // 删除规则对应的配置项
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.vodeActivityKey.KEY_VCODE_ACTIVITY_VPOINTS_COG_LIST
                    + Constant.DBTSPLIT + perMantissaCog.getInfoKey() + Constant.DBTSPLIT + perMantissaCog.getInfoKey());
	        
			model.addAttribute("errMsg", "修改成功");
		} catch (Exception ex) {
            if (ex instanceof BusinessException) {
                model.addAttribute("errMsg", "修改失败," + ex.getMessage());
            } else {
                model.addAttribute("errMsg", "修改失败");
                try {
                    CacheUtilNew.removeAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.error(ex.getMessage(), ex);
		}
		return "forward:showPerMantissaCogList.do";
	}
	
	/**
	 * 刪除记录
	 */
	@RequestMapping("/doPerMantissaCogDelete")
	public String doPerMantissaCogDelete(HttpSession session, String infoKey, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			perMantissaCogService.deletePerMantissaCog(infoKey, currentUser.getUserKey());
			
			// 删除逢百规则过期标志缓存
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog.DOT_RULE_IS_VALID + Constant.DBTSPLIT + infoKey);
	        // 删除统计数据缓存
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog
	        		.DOT_RULE_TOTAL_PRIZE + Constant.DBTSPLIT + infoKey + "bottle");
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog
	        		.DOT_RULE_TOTAL_PRIZE + Constant.DBTSPLIT + infoKey + "money");
	        RedisApiUtil.getInstance().del(true, CacheUtilNew.cacheKey.dotRedpacketCog
	        		.DOT_RULE_TOTAL_PRIZE + Constant.DBTSPLIT + infoKey + "vpoint");
	        // 删除规则缓存
            CacheUtilNew.removeGroupByKey(CacheUtilNew.cacheKey.vodeActivityKey
                    .KEY_VCODE_PERMANTISSA_COG + Constant.DBTSPLIT + DateUtil.getDateTime(DateUtil.DEFAULT_DATE_FORMAT_SHT));
	        
			model.addAttribute("errMsg", "删除成功");
		} catch (Exception ex) {
            if (ex instanceof BusinessException) {
                model.addAttribute("errMsg", "删除失败," + ex.getMessage());
            } else {
                model.addAttribute("errMsg", "删除失败");
                try {
                    CacheUtilNew.removeAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            log.error(ex.getMessage(), ex);
			
		}
		return "forward:showPerMantissaCogList.do"; 
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkBussionName")
	public String checkBussionName(String infoKey, String bussionName){
		return perMantissaCogService.checkBussionName(infoKey, bussionName);
	}
	
	/**
	 * 解析奖项配置项Excel
	 */
	@ResponseBody
	@RequestMapping("/checkPrizeCogExcel")
	public String checkPrizeCogExcel(HttpServletRequest request, MultipartFile prizeFile) {
	    Map<String, Object> resultMap = new HashMap<>();
	    try {
	        Map<String, String> prizeTypeMap = vpointsCogService.queryAllPrizeType(true, true, true, false, true, true, null);
	        List<VcodeActivityMoneyImport> excelLst = perMantissaCogService.initVpointsCog(prizeFile);
	        for (VcodeActivityMoneyImport item : excelLst) {
	            item.setPrizeType(prizeTypeMap.get(item.getPrizeType()));
            }
	        resultMap.put("excelLst", excelLst);
        } catch (Exception ex) {
            if (ex instanceof BusinessException) {
                resultMap.put("errMsg", "校验失败," + ex.getMessage());
            } else {
                resultMap.put("errMsg", "解析失败");
            }
        }
	    return JSON.toJSONString(resultMap);
	}
}
