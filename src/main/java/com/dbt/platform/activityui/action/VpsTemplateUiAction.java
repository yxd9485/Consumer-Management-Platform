package com.dbt.platform.activityui.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.base.action.BaseAction;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activityui.bean.AuthDTO;
import com.dbt.platform.activityui.bean.VpsAuthInfo;
import com.dbt.platform.activityui.bean.VpsTemplateUi;
import com.dbt.platform.activityui.service.VpsTemplateUiService;
import com.dbt.platform.system.bean.SysUserBasis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 码源订单Action
 *
 * @author hanshimeng
 */
@Controller
@RequestMapping("/templateUi")
public class VpsTemplateUiAction extends BaseAction {

    @Autowired
    private VpsTemplateUiService templateUiService;
    private List<String> backInfos = new ArrayList<>();
    /**
     * 时间格式
     */
    private final String formatter = "yyyy-MM-dd HH:mm:ss";

    /**
     * 模板UI列表
     *
     * @param session
     * @param pageParam
     * @param model
     * @return
     */
    @RequestMapping("showTemplateUiList")
    public String showTemplateUiList(HttpSession session, String queryParam, String pageParam, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            PageOrderInfo pageInfo = new PageOrderInfo(pageParam);
            VpsTemplateUi queryBean = new VpsTemplateUi(queryParam);

            List<VpsTemplateUi> resultList = templateUiService.queryForLst(queryBean, pageInfo);
            int countResult = templateUiService.queryForCount(queryBean);


            model.addAttribute("currentUser", currentUser);
            model.addAttribute("resultList", resultList);
            model.addAttribute("showCount", countResult);
            model.addAttribute("startIndex", pageInfo.getStartCount());
            model.addAttribute("countPerPage", pageInfo.getPagePerCount());
            model.addAttribute("currentPage", pageInfo.getCurrentPage());
            model.addAttribute("orderCol", pageInfo.getOrderCol());
            model.addAttribute("orderType", pageInfo.getOrderType());
            model.addAttribute("queryParam", queryParam);
            model.addAttribute("pageParam", pageParam);
            model.addAttribute("projectServerName", DbContextHolder.getDBType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "vcode/templateui/showTemplateUiList";
    }

    /**
     * 跳转新增页面
     */
    @RequestMapping("showTemplateUiAdd")
    public String showTemplateUiAdd(HttpSession session, Model model) {
        VpsTemplateUi vpsTemplateUi = new VpsTemplateUi();
        String picInfo = DatadicUtil.getDataDicValue(
                DatadicKey.dataDicCategory.FILTER_ACT_DEFAULTUI,
                DatadicKey.filterUI.FILTER_ACTUI_DEFAULTPARAM);
        try {
            vpsTemplateUi = JSON.parseObject(picInfo, VpsTemplateUi.class);
        } catch (Exception ex) {
            log.error(ex);
        }
        model.addAttribute("vpsTemplateUi", vpsTemplateUi);
        return "vcode/templateui/showTemplateUiAdd";
    }

    /**
     * 跳转新增页面
     */
    @RequestMapping("showTemplateUiEdit")
    public String showTemplateUiEdit(HttpSession session, Model model, String templateKey) {
//		try{
//			SysUserBasis currentUser = this.getUserBasis(session);
//			VpsTemplateUi vpsTemplateUi  = templateUiService.getTemplateUi(templateKey);
//			String property = vpsTemplateUi.getTemplateProperty();
//			TemplateProperty templateProperty = JSONObject.parseObject(property,TemplateProperty.class);
//			// 获取数据字典基础模板
//			model.addAttribute("vpsTemplateUi", vpsTemplateUi);
//			model.addAttribute("templateKey", templateKey);
//			model.addAttribute("backgroundPicUrl", templateProperty.getBackgroundPic().getPicUrl());
//			model.addAttribute("logoPicUrl", templateProperty.getLogoPic().getPicUrl());
//			model.addAttribute("openRedpacketPicUrl", templateProperty.getOpenRedpacketPic().getPicUrl());
//			model.addAttribute("sloganPicUrl", templateProperty.getSloganPic().getPicUrl());
//		} catch (Exception ex) {
//			log.error(ex);
//		}
        return "vcode/templateui/showTemplateUiEdit";
    }

    /**
     * 跳转新增页面
     */
    @RequestMapping("showAuthAdd")
    public String showAuthAdd(HttpSession session, String templateKey, Model model) {
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            model.addAttribute("templateKey", templateKey);
            log.info("1111111111111111111111111111111111111111111111111111???????????????" + templateKey);
            // 获取数据字典基础模板

        } catch (Exception ex) {
            log.error(ex);
        }
        return "vcode/templateui/showAuthAdd";
    }

    /**
     * 跳转授权编辑
     */
    @RequestMapping("showAuthEdit")
    public String showAuthEdit(HttpSession session, Model model, String templateKey) {
        try {
            List<VpsAuthInfo> authInfoList = templateUiService.getAuthInfo(templateKey);
            AuthDTO auth = new AuthDTO();
            for (VpsAuthInfo a : authInfoList) {
                if (a.getAuthType().equals("1")) {
                    auth.setLocTemplateProperty(a.getTemplateProperty());
                    auth.setLocIsDefault(a.getIsDefault());
                    auth.setLocAuthStatus(a.getAuthStatus());
                    auth.setLocAuthPage(a.getAuthPage());
                    auth.setLocAuthCondition(a.getAuthCondition());
                }
                if (a.getAuthType().equals("2")) {
                    auth.setTelTemplateProperty(a.getTemplateProperty());
                    auth.setTelIsDefault(a.getIsDefault());
                    auth.setTelAuthStatus(a.getAuthStatus());
                    auth.setTelAuthPage(a.getAuthPage());
                    auth.setTelAuthCondition(a.getAuthCondition());
                }
                if (a.getAuthType().equals("3")) {
                    auth.setAppAuthStatus(a.getAuthStatus());
                    auth.setAppTemplateProperty(a.getTemplateProperty());
                }
            }
            // 获取数据字典基础模板
            model.addAttribute("auth", auth);
            model.addAttribute("templateKey", templateKey);
        } catch (Exception ex) {
            log.error(ex);
        }
        return "vcode/templateui/showAuthEdit";
    }

    @ResponseBody
    @RequestMapping("/doAuthAdd")
    public String doAuthAdd(HttpSession session, AuthDTO authdto, Model model) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsAuthInfo tel = new VpsAuthInfo();
            tel.setAuthCondition(authdto.getTelAuthCondition());
            tel.setAuthType("2");
            tel.setTemplateKey(authdto.getTemplateKey());
            tel.setAuthKey(UUID.randomUUID().toString());
            tel.setAuthPage(authdto.getTelAuthPage());
            tel.setAuthStatus(authdto.getTelAuthStatus());
            tel.setIsDefault(authdto.getTelIsDefault());
            tel.setTemplateProperty(authdto.getTelTemplateProperty());
            VpsAuthInfo loc = new VpsAuthInfo();
            loc.setAuthCondition(authdto.getLocAuthCondition());
            loc.setTemplateKey(authdto.getTemplateKey());
            loc.setAuthKey(UUID.randomUUID().toString());
            loc.setAuthPage(authdto.getLocAuthPage());
            loc.setAuthStatus(authdto.getLocAuthStatus());
            loc.setAuthType("1");
            loc.setIsDefault(authdto.getLocIsDefault());
            loc.setTemplateProperty(authdto.getLocTemplateProperty());
            VpsAuthInfo app = new VpsAuthInfo();
            app.setTemplateKey(authdto.getTemplateKey());
            app.setAuthKey(UUID.randomUUID().toString());
            app.setAuthStatus(authdto.getAppAuthStatus());
            app.setTemplateProperty(authdto.getAppTemplateProperty());
            app.setAuthType("3");
            tel.setCreateTime(DateUtil.getDate(formatter));
            tel.setCreateUser(currentUser.getUserName());
            loc.setCreateTime(DateUtil.getDate(formatter));
            loc.setCreateUser(currentUser.getUserName());
            app.setCreateTime(DateUtil.getDate(formatter));
            app.setCreateUser(currentUser.getUserName());
            List<VpsAuthInfo> authList = new ArrayList<>();
            authList.add(tel);
            authList.add(loc);
            authList.add(app);
            templateUiService.addAuthInfo(authList);
            errMsg = "添加成功";
        } catch (Exception ex) {
            errMsg = "添加失败";
            log.error(ex.getMessage(), ex);
        }
        Map<String, Object> map = new HashMap<>();
//		map.put("templateKey", vpsTemplateUi.getTemplateKey());
        map.put("errMsg", errMsg);
        return JSON.toJSONString(map);
    }

    @ResponseBody
    @RequestMapping("doAuthEdit")
    public String doAuthEdit(HttpSession session, AuthDTO authdto) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            VpsAuthInfo tel = new VpsAuthInfo();
            tel.setAuthCondition(authdto.getTelAuthCondition());
            tel.setAuthType("2");
            tel.setTemplateKey(authdto.getTemplateKey());
            tel.setAuthKey(UUID.randomUUID().toString());
            tel.setAuthPage(authdto.getTelAuthPage());
            tel.setAuthStatus(authdto.getTelAuthStatus());
            tel.setIsDefault(authdto.getTelIsDefault());
            tel.setTemplateProperty(authdto.getTelTemplateProperty());
            VpsAuthInfo loc = new VpsAuthInfo();
            loc.setAuthCondition(authdto.getLocAuthCondition());
            loc.setTemplateKey(authdto.getTemplateKey());
            loc.setAuthKey(UUID.randomUUID().toString());
            loc.setAuthPage(authdto.getLocAuthPage());
            loc.setAuthStatus(authdto.getLocAuthStatus());
            loc.setAuthType("1");
            loc.setIsDefault(authdto.getLocIsDefault());
            loc.setTemplateProperty(authdto.getLocTemplateProperty());
            VpsAuthInfo app = new VpsAuthInfo();
            app.setTemplateKey(authdto.getTemplateKey());
            app.setAuthKey(UUID.randomUUID().toString());
            app.setAuthStatus(authdto.getAppAuthStatus());
            app.setTemplateProperty(authdto.getAppTemplateProperty());
            app.setAuthType("3");
            tel.setCreateTime(DateUtil.getDate(formatter));
            tel.setCreateUser(currentUser.getUserName());
            loc.setCreateTime(DateUtil.getDate(formatter));
            loc.setCreateUser(currentUser.getUserName());
            app.setCreateTime(DateUtil.getDate(formatter));
            app.setCreateUser(currentUser.getUserName());
            List<VpsAuthInfo> authList = new ArrayList<>();
            authList.add(tel);
            authList.add(loc);
            authList.add(app);
            templateUiService.updateAuth(authList);
            errMsg = "添加成功";
        } catch (Exception ex) {
            errMsg = "添加失败";
            log.error(ex.getMessage(), ex);
        }
        Map<String, Object> map = new HashMap<>();
//		map.put("templateKey", vpsTemplateUi.getTemplateKey());
        map.put("errMsg", errMsg);
        return JSON.toJSONString(map);
    }

    /**
     * 创建活动模板
     */
    @ResponseBody
    @RequestMapping("/doTemplateUiAdd")
    public String doTemplateUiAdd(HttpSession session, VpsTemplateUi vpsTemplateUi, Model model) {
        String errMsg = "";
        try {
            SysUserBasis currentUser = this.getUserBasis(session);
            vpsTemplateUi.setTemplateKey(UUID.randomUUID().toString());
            vpsTemplateUi.setCreateTime(DateUtil.getDate(formatter));
            vpsTemplateUi.setCreateUser(currentUser.getUserName());
            templateUiService.addTemplateUi(vpsTemplateUi);
            errMsg = "添加成功";
        } catch (Exception ex) {
            errMsg = "添加失败";
            log.error(ex.getMessage(), ex);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("templateKey", vpsTemplateUi.getTemplateKey());
        map.put("errMsg", errMsg);
        return JSON.toJSONString(map);
    }

    /**
     * 修改模板信息，用于页签切换保存
     */
	@ResponseBody
	@RequestMapping("doTemplateUiEdit")
	public String doTemplateUiEdit(HttpSession session, VpsTemplateUi
			vpsTemplateUi){
		String errMsg = "";
		try{
			//页面图片模型图片属性都是已经预置的判断是否为空，因为切换tab页点击下一步也会走更新
//            if(StringUtils.isNotBlank(vpsTemplateUi.getBackgroundPic())){
//                TemplateProperty templateProperty = getTemplateProperty(vpsTemplateUi);
//                vpsTemplateUi.setTemplateProperty(JSON.toJSONString(templateProperty));
//            }
			SysUserBasis currentUser = this.getUserBasis(session);
			vpsTemplateUi.setUpdateTime(DateUtil.getDate(formatter));
			vpsTemplateUi.setUpdateUser(currentUser.getUserName());
			templateUiService.updateTemplateUi(vpsTemplateUi);
			errMsg="添加成功";
		} catch (Exception ex) {
			errMsg="添加失败";
			log.error(ex);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("templateKey", vpsTemplateUi.getTemplateKey());
		map.put("errMsg", errMsg);
		return JSON.toJSONString(map);
	}
//
//	/**
//	 * 获取图片各个属性
//	 * @param vpsTemplateUi
//	 * @return
//	 */
//
//	private TemplateProperty getTemplateProperty(VpsTemplateUi vpsTemplateUi) {
//		TemplateProperty templateProperty = new TemplateProperty();
//		List<String> backs = Arrays.asList(vpsTemplateUi.getBackgroundPic().split("\\|"));
//		List<String> slogans = Arrays.asList(vpsTemplateUi.getSloganPic().split("\\|"));
//		List<String> openRedpackets = Arrays.asList(vpsTemplateUi.getOpenRedpacketPic().split("\\|"));
//		List<String> logos = Arrays.asList(vpsTemplateUi.getLogoPic().split("\\|"));
//		TemplateProperty.PicInfo backPicInfo = new TemplateProperty.PicInfo();
//		backPicInfo.setPicName(backs.get(0));
//		backPicInfo.setPicWidth(backs.get(1));
//		backPicInfo.setPicHeight(backs.get(2));
//		backPicInfo.setPicX(backs.get(3));
//		backPicInfo.setPicY(backs.get(4));
//		backPicInfo.setPicUrl(backs.get(5));
//		TemplateProperty.PicInfo slogansPicInfo = new TemplateProperty.PicInfo();
//		slogansPicInfo.setPicName(slogans.get(0));
//		slogansPicInfo.setPicWidth(slogans.get(1));
//		slogansPicInfo.setPicHeight(slogans.get(2));
//		slogansPicInfo.setPicX(slogans.get(3));
//		slogansPicInfo.setPicY(slogans.get(4));
//		slogansPicInfo.setPicUrl(slogans.get(5));
//		TemplateProperty.PicInfo openRedpacketsPicInfo = new TemplateProperty.PicInfo();
//		openRedpacketsPicInfo.setPicName(openRedpackets.get(0));
//		openRedpacketsPicInfo.setPicWidth(openRedpackets.get(1));
//		openRedpacketsPicInfo.setPicHeight(openRedpackets.get(2));
//		openRedpacketsPicInfo.setPicX(openRedpackets.get(3));
//		openRedpacketsPicInfo.setPicY(openRedpackets.get(4));
//		openRedpacketsPicInfo.setPicUrl(openRedpackets.get(5));
//		TemplateProperty.PicInfo logosPicInfo = new TemplateProperty.PicInfo();
//		logosPicInfo.setPicName(logos.get(0));
//		logosPicInfo.setPicWidth(logos.get(1));
//		logosPicInfo.setPicHeight(logos.get(2));
//		logosPicInfo.setPicX(logos.get(3));
//		logosPicInfo.setPicY(logos.get(4));
//		logosPicInfo.setPicUrl(logos.get(5));
//		templateProperty.setBackgroundPic(backPicInfo);
//		templateProperty.setLogoPic(slogansPicInfo);
//		templateProperty.setOpenRedpacketPic(openRedpacketsPicInfo);
//		templateProperty.setSloganPic(logosPicInfo);
//		return templateProperty;
//	}

}
