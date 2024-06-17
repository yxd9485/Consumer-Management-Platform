package com.dbt.platform.activate.action;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.activity.service.VcodeQrcodeBatchInfoService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.service.SkuInfoService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/searchBatchActivate")
public class SearchBatchActivateAction extends BaseAction {

    @Autowired
    private VcodeQrcodeBatchInfoService batchInfoService;
    @Autowired
    private SkuInfoService skuInfoService;
   
    /**
     * 分页查询激活批次列表
     * @return
     */
    @RequestMapping("/showSearchActivateBatchList")
    public String showSearchActivateBatchList(HttpSession session, 
    		String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeQrcodeBatchInfo queryBean = new VcodeQrcodeBatchInfo(queryParam, Constant.QrcodeBatchType.type_4);
            
            // 激活批次列表
            List<VcodeQrcodeBatchInfo> batchInfoList = 
            		batchInfoService.queryActivateBatchList(queryBean, pageInfo);
            int count = batchInfoService.queryActivateBatchCount(queryBean);
            
            // 该企业下的skuList
            List<SkuInfo> skuList = skuInfoService.loadSkuListByCompany(currentUser.getCompanyKey());
            
            // 获取该企业下的工厂（使用原生jedis查询，因为key的结尾没有拼serverName）
            String factoryKey = CacheKey.cacheKey.company.KEY_COMPANY_FACTORY_INFO;
            String factoryNames = (String) RedisApiUtil.getInstance().eval("return redis.call('hget', KEYS[1], ARGV[1])", 1, factoryKey, DbContextHolder.getDBType());
    		
            
            model.addAttribute("showCount", count);
            model.addAttribute("skuList", skuList);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("batchInfoList", batchInfoList);
            model.addAttribute("factoryNameList", Arrays.asList(
            			StringUtils.defaultIfBlank(factoryNames,"").split(",")));
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/activate/showSearchActivateBatchList";
    }
    
    /**
     * 导出激活批次列表
     * @return
     */
    @RequestMapping("/exportActivateBatchList")
    public void exportActivateBatchList(HttpSession session, HttpServletResponse response,
    						String queryParam, String pageParam, Model model) {
        try {
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VcodeQrcodeBatchInfo queryBean = new VcodeQrcodeBatchInfo(queryParam, Constant.QrcodeBatchType.type_4);
            
        	// 导出
        	String fileName = URLEncoder.encode("激活批次导出列表", "UTF-8") + DateUtil.getDate() + ".xls";
        	response.reset();
        	response.setCharacterEncoding("GBK");
        	response.setContentType("application/msexcel;charset=UTF-8");
    		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
            batchInfoService.exportActivateBatchList(queryBean, pageInfo, response);
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
