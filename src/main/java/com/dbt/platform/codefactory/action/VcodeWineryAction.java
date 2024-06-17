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
import com.dbt.platform.codefactory.bean.VpsWinery;
import com.dbt.platform.codefactory.service.VpsVcodeFactoryService;
import com.dbt.platform.codefactory.service.VpsVcodeWineryService;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/vcodeWinery")
public class VcodeWineryAction extends BaseAction {

    @Autowired
    private VpsVcodeWineryService vpsVcodeWineryService;
    @Autowired
    private VpsVcodeFactoryService vpsVcodeFactoryService;
   
   
    /**
     * 分页查询酒厂列表
     * @return
     */
    @RequestMapping("/showWineryList")
    public String showWineryList(HttpSession session, 
    		String queryParam, String pageParam, Model model) {
        try {
        	DbContextHolder.setDBType(null);
        	List<ServerInfo> projectServerLst = vpsVcodeFactoryService.queryProvinceLst();
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageOrderInfo = new PageOrderInfo(pageParam);
            VpsWinery queryBean = new VpsWinery(queryParam);
            
            List<VpsWinery> result = vpsVcodeWineryService.queryForLst(queryBean, pageOrderInfo);
            int countResult = vpsVcodeWineryService.queryForCount(queryBean);
            model.addAttribute("resultList", result);
            model.addAttribute("showCount", countResult);
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
        return "vcode/winery/showWineryList";
    }
    
    
    /**
	 * 跳转工厂新增页面
	 */
	@RequestMapping("/showWineryAdd")
	public String showWineryAdd(HttpSession session, Model model){
		DbContextHolder.setDBType(null);
		List<ServerInfo> projectServerLst = vpsVcodeFactoryService.queryProvinceLst();
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projectServerLst", projectServerLst);
		return "vcode/winery/showWineryAdd";
	}
	
	/**
	 * 跳转工厂编辑页面
	 */
	@RequestMapping("/showWineryEdit")
	public String showWineryEdit(HttpSession session, String Key, Model model){
		DbContextHolder.setDBType(null);
		List<ServerInfo> projectServerLst = vpsVcodeFactoryService.queryProvinceLst();
        SysUserBasis currentUser = this.getUserBasis(session);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("projectServerLst", projectServerLst);
        model.addAttribute("vpsWinery", vpsVcodeWineryService.findById(Key));
		return "vcode/winery/showWineryEdit";
	}
    
	
	/**
	 * 添加工厂信息
	 */
	@RequestMapping("/doWineryAdd")
	public String doWineryAdd(HttpSession session, VpsWinery info,Model model){
		try{
			DbContextHolder.setDBType(null);
		    SysUserBasis currentUser = this.getUserBasis(session);
		    info.fillFields(currentUser.getUserKey());
		    vpsVcodeWineryService.insertInfo(info);
			model.addAttribute("errMsg", "新增成功");
		} catch (BusinessException ex) {
		    model.addAttribute("errMsg", "新增失败，" + ex.getMessage());
		} catch (Exception ex) {
			model.addAttribute("errMsg", "新增失败");
			log.error("新增失败", ex);
		}
		return "forward:showWineryList.do";
	}
	
	
	
	
	/**
	 * 编辑工厂信息
	 */
	@RequestMapping("/doWineryEdit")
	public String doWineryEdit(HttpSession session, VpsWinery info, Model model){
		try{
			DbContextHolder.setDBType(null);
			SysUserBasis currentUser = this.getUserBasis(session);
			info.fillUpdateFields(currentUser.getUserKey());
			vpsVcodeWineryService.updateInfo(info);
			model.addAttribute("errMsg", "编辑成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "编辑失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "编辑失败");
            log.error("SKU编辑失败", ex);
        }
        return "forward:showWineryList.do"; 
	} 
	
	
	/**
	 * 删除工厂信息
	 */
	@RequestMapping("/doWineryDelete")
	public String doWineryDelete(HttpSession session, String Key,Model model){
		try{
			DbContextHolder.setDBType(null);
			SysUserBasis currentUser = this.getUserBasis(session);
			vpsVcodeWineryService.deleteInfo(Key,currentUser);
		    model.addAttribute("errMsg", "删除成功");
        } catch (BusinessException ex) {
            model.addAttribute("errMsg", "删除失败，" + ex.getMessage());
        } catch (Exception ex) {
            model.addAttribute("errMsg", "删除失败");
            log.error("SKU删除失败", ex);
        }
        return "forward:showWineryList.do";  
	} 

 
}
