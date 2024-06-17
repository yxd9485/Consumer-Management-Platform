package com.dbt.platform.activate.action;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.RedisApiUtil;
import com.dbt.framework.util.WechatUtil;
import com.dbt.platform.activate.bean.VpsVcodeBatchActivateCog;
import com.dbt.platform.activate.service.VpsVcodeBatchActivateCogService;
import com.dbt.platform.appuser.bean.VpsConsumerCheckUserInfo;
import com.dbt.platform.system.bean.SysUserBasis;

@Controller
@RequestMapping("/batchActivate")
public class VcodeBatchActivateCogAction extends BaseAction {

    @Autowired
    private VpsVcodeBatchActivateCogService activateCogService;
   
    /**
     * 分页查询激活人员列表
     * @return
     */
    @RequestMapping("/showActivateCogList")
    public String showActivateCogList(HttpSession session, 
    		VpsVcodeBatchActivateCog queryBean, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageOrderInfo = new PageOrderInfo(pageParam);
            model = activateCogService.queryForList(queryBean, pageOrderInfo, model);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("startIndex", pageOrderInfo.getStartCount());
            model.addAttribute("countPerPage", pageOrderInfo.getPagePerCount());
            model.addAttribute("currentPage", pageOrderInfo.getCurrentPage());
            model.addAttribute("queryBean", queryBean);
            model.addAttribute("pageParam", pageParam);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "vcode/activate/showBatchActivateCogList";
    }
    
    /**
     * 跳转到修改界面
     */
    @RequestMapping("/showActivateCogEdit")
    public String showActivateCogEdit(HttpSession session, String isCheck, String infoKey, Model model) {
    	VpsVcodeBatchActivateCog activateCog = activateCogService.findById(infoKey, model);
    	model.addAttribute("activateCog", activateCog);
        model.addAttribute("isCheck", isCheck);
        return "vcode/activate/showBatchActivateCogEdit";
    }
    
    /**
     * 修改激活人员
     */
    @RequestMapping("/doActivateCogEdit")
    public String doActivateCogEdit(HttpSession session, VpsVcodeBatchActivateCog activateCog, 
    		String isCheck, String oldUserPrivilege, String oldUserStatus, Model model) {
        try {
        	SysUserBasis currentUser = this.getUserBasis(session);
            activateCog.fillFields(currentUser.getUserKey());
            model = activateCogService.updateBatchActivateCog(
            		activateCog, isCheck, oldUserPrivilege, oldUserStatus, model);
        } catch (Exception e) {
        	if("1".equals(isCheck)){
        		model.addAttribute("errMsg", "审核失败");
        	}else{
        		model.addAttribute("errMsg", "更新失败");
        	}
        }
        return showActivateCogList(session, new VpsVcodeBatchActivateCog(), null, model);
    }
    
    /**
     * 驳回激活人员
     */
    @RequestMapping("/doActivateCogReject")
    public String doActivateCogReject(HttpSession session, String infoKey, Model model) {
        try {
        	SysUserBasis currentUser = this.getUserBasis(session);
        	VpsVcodeBatchActivateCog activateCog = activateCogService.findById(infoKey, model);
        	activateCog.setUserStatus(Constant.ACTIVATE_USER_STATUS.USER_STATUS_3);
            activateCog.fillFields(currentUser.getUserKey());
            model = activateCogService.rejectBatchActivateCog(activateCog, model);
        } catch (Exception e) {
        	model.addAttribute("errMsg", "驳回失败");
        }
        return showActivateCogList(session, new VpsVcodeBatchActivateCog(), null, model);
    }
    
    /**
     * 跳转批次激活二维码页面
     */
    @RequestMapping("/showActivateCogQrcode")
    public String showActivateCogQrcode(HttpSession session, Model model) {
    	JSONObject jsonObject = null;
    	String batchActivateQrcodeKey = CacheKey.cacheKey.company.KEY_COMPANY_BATCH_ACTIVATE_QRCODE;
    	jsonObject = (JSONObject) RedisApiUtil.getInstance().getObject(true, batchActivateQrcodeKey);
    	if(null == jsonObject || StringUtils.isBlank(jsonObject.getString("ticket"))){
    		jsonObject = WechatUtil.CreateQrcode(
        			null, Constant.CREATE_QRCODE_ACTION_NAME.QR_LIMIT_STR_SCENE, "1", DbContextHolder.getDBType());
    		if(StringUtils.isNotBlank(jsonObject.getString("ticket"))){
    			RedisApiUtil.getInstance().setObject(true, batchActivateQrcodeKey, jsonObject);
    		}
    	}
    	if(null != jsonObject && StringUtils.isNotBlank(jsonObject.getString("ticket"))){
    		// 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
    		String ticket = jsonObject.getString("ticket");
    		// 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）
    		String expireSeconds = jsonObject.getString("expire_seconds");
    		// 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
    		String qrcodeUrl = jsonObject.getString("url");
    		model.addAttribute("ticket", ticket);
    		model.addAttribute("expireSeconds", expireSeconds);
    		model.addAttribute("qrcodeUrl", qrcodeUrl);
    	}
        return "vcode/activate/showBatchActivateCogQrcode";
    } 
    
    /**
     * 导出查询结果
     * 
     * @return
     */
    @RequestMapping("/exportActivateCogList")
    public void exportPrizeList(HttpSession session, HttpServletResponse response,
    		VpsVcodeBatchActivateCog queryBean, String pageParam, Model model) {
        try {
            // 导出
        	activateCogService.exportActivateCogList(queryBean, response);
        } catch (Exception e) {
        	model.addAttribute("errMsg", "导出失败");
        }
    }
}
