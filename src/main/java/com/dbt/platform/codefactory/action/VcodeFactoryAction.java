package com.dbt.platform.codefactory.action;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;
import com.dbt.platform.codefactory.bean.VpsWinery;
import com.dbt.platform.codefactory.service.VpsVcodeFactoryService;
import com.dbt.platform.codefactory.service.VpsVcodeWineryService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/vcodeFactory")
public class VcodeFactoryAction extends BaseAction {

    @Autowired
    private VpsVcodeFactoryService vpsVcodeFactoryService;
    @Autowired
    private VpsVcodeWineryService vpsVcodeWineryService;
    
    /**
     * 分页查询工厂列表
     * @return
     */
    @RequestMapping("/showVcodeFactoryList")
    public String showVcodeFactoryList(HttpSession session, 
    		String queryParam, String pageParam, Model model) {
        try {
        	DbContextHolder.setDBType(null);
        	List<ServerInfo> projectServerLst = vpsVcodeFactoryService.queryProvinceLst();
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageOrderInfo = new PageOrderInfo(pageParam);
            VpsVcodeFactory queryBean = new VpsVcodeFactory(queryParam);
            
            List<VpsVcodeFactory> result = vpsVcodeFactoryService.queryForLst(queryBean, pageOrderInfo);
            List<VpsWinery> wineryLst = vpsVcodeWineryService.queryForLst(null, null);
            int countResult = vpsVcodeFactoryService.queryForCount(queryBean);
            model.addAttribute("resultList", result);
            model.addAttribute("showCount", countResult);
            model.addAttribute("wineryLst", wineryLst);
            model.addAttribute("projectServerLst", projectServerLst);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("startIndex", pageOrderInfo.getStartCount());
            model.addAttribute("countPerPage", pageOrderInfo.getPagePerCount());
            model.addAttribute("currentPage", pageOrderInfo.getCurrentPage());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "vcode/factory/showVcodeFactoryList";
    }
    
    
    /**
	 * 跳转工厂新增页面
	 */
	@RequestMapping("/showVcodeFactoryAdd")
	public String showVcodeFactoryAdd(HttpSession session, Model model){
		DbContextHolder.setDBType(null);
		List<ServerInfo> projectServerLst = vpsVcodeFactoryService.queryProvinceLst();
		List<VpsWinery> wineryLst = vpsVcodeWineryService.queryForLst(null, null);
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projectServerLst", projectServerLst);
        model.addAttribute("wineryLst", wineryLst);
		return "vcode/factory/showVcodeFactoryAdd";
	}
	
	/**
	 * 跳转工厂编辑页面
	 */
	@RequestMapping("/showVcodeFactoryEdit")
	public String showVcodeFactoryEdit(HttpSession session, String Key, Model model){
		DbContextHolder.setDBType(null);
		List<ServerInfo> projectServerLst = vpsVcodeFactoryService.queryProvinceLst();
		List<VpsWinery> wineryLst = vpsVcodeWineryService.queryForLst(null, null);
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projectServerLst", projectServerLst);
        model.addAttribute("wineryLst", wineryLst);
        model.addAttribute("vpsVcodeFactory", vpsVcodeFactoryService.findById(Key));
		return "vcode/factory/showVcodeFactoryEdit";
	}
	
	
	/**
	 * 添加工厂信息
	 */
	@RequestMapping("/doVcodeFactoryAdd")
	public String doVcodeFactoryAdd(HttpSession session, VpsVcodeFactory info,Model model){
		try{
			DbContextHolder.setDBType(null);
		    SysUserBasis currentUser = this.getUserBasis(session);
		    info.fillFields(currentUser.getUserKey());
		    vpsVcodeFactoryService.insertVcodeFactory(info);
			model.addAttribute("errMsg", "新增成功");
		} catch (BusinessException ex) {
		    model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
		} catch (Exception ex) {
			model.addAttribute("errMsg", "新增失败");
			log.error("新增失败", ex);
		}
		return "forward:showVcodeFactoryList.do";
	}
	
	
	
	
	/**
	 * 编辑工厂信息
	 */
	@RequestMapping("/doVcodeFactoryEdit")
	public String doVcodeFactoryEdit(HttpSession session, VpsVcodeFactory info, Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			DbContextHolder.setDBType(null);
			info.fillUpdateFields(currentUser.getUserKey());
			vpsVcodeFactoryService.updateVcodeFactory(info);
			model.addAttribute("errMsg", "编辑成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "编辑失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("SKU编辑失败", ex);
        }
        return "forward:showVcodeFactoryList.do"; 
	} 
	
	
	/**
	 * 删除工厂信息
	 */
	@RequestMapping("/doVcodeFactoryDelete")
	public String doVcodeFactoryDelete(HttpSession session, String Key,Model model){
		try{
			SysUserBasis currentUser = this.getUserBasis(session);
			DbContextHolder.setDBType(null);
			vpsVcodeFactoryService.deleteFactorynfo(Key,currentUser);
		    model.addAttribute("errMsg", "删除成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "删除失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("SKU删除失败", ex);
        }
        return "forward:showVcodeFactoryList.do";  
	} 
    

 
}
