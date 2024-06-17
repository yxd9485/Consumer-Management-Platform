package com.dbt.framework.base.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dbt.framework.base.action.reply.BaseDataResult;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.bean.Constant.ResultCode;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.securityauth.SecurityContext;
import com.dbt.framework.util.UUIDTools;
import com.dbt.framework.util.UserThreadLocalUtil;
import com.dbt.platform.system.bean.SysRole;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 文件名：BaseAction.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: action基础类，公共类<br>
 * 修改人: 谷长鹏<br>
 * 修改时间：2014-02-19 10:37:26<br>
 * 修改内容：新增<br>
 */
public class BaseAction {
	protected Log log = LogFactory.getLog(getClass());
    private final static int CACHESIZE = 2048;
	
	/**
	 * 得到uuid
	 * 
	 * @return
	 */
	protected String callUUID() {
		return UUIDTools.getInstance().getUUID();
	}
	/**
	 * 校验用户Context是否为空
	 * 
	 * @param securityContext
	 *            SecurityContext对象
	 * @return true：为空 false：不为空
	 */
	private boolean checkSecurityContextIsNull(SecurityContext securityContext) {
		if (securityContext == null) {
			return true;
		}
		return false;
	}
	/**
	 * 得到当前登录用户session中，相关内容信息(用户基本信息、角色、权限code信息)
	 * 
	 * @param session
	 *            session会话
	 * @return SecurityContext 用户Context
	 */
	protected SecurityContext getSecurityContext(HttpSession session) {
		/*
		 * if (null == session || session.isNew()) { session =
		 * request.getSession(true); }
		 */
        SecurityContext securityContext = (SecurityContext) session
                .getAttribute(Constant.USER_SESSION + UserThreadLocalUtil.get("vjfSessionId"));
		return securityContext;

	}
	/**
	 * 得到session中用户基本信息
	 * 
	 * @param session
	 *            session会话
	 * @return SysUserBasis 用户基本信息对象
	 */
	protected SysUserBasis getUserBasis(HttpSession session) {
		SysUserBasis sysUserBasis = null;
		SecurityContext securityContext = getSecurityContext(session);
		if (checkSecurityContextIsNull(securityContext)) {
			sysUserBasis=  new SysUserBasis();
		}else{
			sysUserBasis = securityContext.getSysUserBasis();
		}
		SysRole roleInfo = getUniqueRoleInfo(session);
		if(null != roleInfo){
			sysUserBasis.setRoleKey(roleInfo.getRoleKey());
		}
		return sysUserBasis;
	}
	/**
	 * 得到session中除菜单外，按钮、超链接等权限信息
	 * 
	 * @param session
	 *            Session会话
	 * @return List<String>
	 */
	protected List<String> getPermissionList(HttpSession session) {
		SecurityContext securityContext = getSecurityContext(session);
		if (checkSecurityContextIsNull(securityContext)) {
			return new ArrayList<String>();
		}
		return securityContext.getPermissionList();
	}
	/**
	 * 得到session中，当前用户所有角色信息
	 * 
	 * @param session
	 *            session会话
	 * @return List<SysRoleM>集合
	 */
	protected List<SysRole> getRoleList(HttpSession session) {
		SecurityContext securityContext = getSecurityContext(session);
		if (checkSecurityContextIsNull(securityContext)) {
			return new ArrayList<SysRole>();
		}
		return securityContext.getSysRoleList();
	}
	/**
	 * 得到唯一角色信息
	 * 
	 * @param session
	 *            session会话
	 * @return 角色信息
	 */
	protected SysRole getUniqueRoleInfo(HttpSession session) {
		return getRoleList(session).get(0);
	}
	
	/**
	 * 校验当前用户是否拥有指定角色授权
	 * 
	 * @param session
	 * @param roleId   角色ID
	 * @return
	 */
	protected boolean validRole(HttpSession session, String... roleId) { 
	    boolean validFlag = false;
	    List<SysRole> roleLst = getRoleList(session);
	    for (SysRole item : roleLst) {
	        for (String role : roleId) {
	            if (item.getRoleKey().equals(role)) {
	                validFlag = true;
	                break;
	            }
            }
	        if (validFlag) break;
        }
        return validFlag;
    }
	
	/**
	 * 初始化返回提示语
	 * @param ex
	 * @param model
	 * @param optDesc
	 */
	protected void initErrMsg(Exception ex, Model model, String optDesc) {
        if (ex instanceof BusinessException) {
            model.addAttribute("errMsg", StringUtils.isBlank(optDesc) ? ex.getMessage() : optDesc + "," + ex.getMessage());
        } else {
            model.addAttribute("errMsg", optDesc);
        }
    }

    protected void initBaseDataResult(BaseDataResult baseResult, Exception e, String errMsg) {
        if (e instanceof BusinessException) {
            BusinessException ex = (BusinessException)e;
            baseResult.initReslut(StringUtils.defaultIfBlank(ex.getErrCode(), ResultCode.FILURE), ex.getMessage());
        } else {
            log.error(e);
            baseResult.initReslut(ResultCode.FILURE, errMsg);
        }
    }
    
    protected JSONObject parseRequestToJsonstr(HttpServletRequest request)throws Exception{
        StringBuffer sbBuffer;
        JSONObject jsonObj;
        try {
            sbBuffer = new StringBuffer();
            InputStream inputStream = request.getInputStream();
            byte[] b = new byte[CACHESIZE];
            for (int n; (n = inputStream.read(b)) != -1;) {
                sbBuffer.append(new String(b, 0, n, "UTF-8"));
            }
            jsonObj=JSON.parseObject(sbBuffer.toString());
            return jsonObj;
        } catch (Exception e) {
            log.error("解析请求报文出错：", e);
            throw e;
        }
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T parseRequest(HttpServletRequest request, Class<T> cls)throws Exception{
        StringBuffer sbBuffer;
        T obj;
        try {
            sbBuffer = new StringBuffer();
            InputStream inputStream = request.getInputStream();
            byte[] b = new byte[CACHESIZE];
            for (int n; (n = inputStream.read(b)) != -1;) {
                sbBuffer.append(new String(b, 0, n, "UTF-8"));
            }
            obj = JSON.parseObject(sbBuffer.toString(), cls);
            return obj;
        } catch (Exception e) {
            log.error("解析请求报文出错：", e);
            throw e;
        }
    }
    
    protected String getContextUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        String protocol = request.getHeader("X-Forwarded-Scheme");
        if (StringUtils.isBlank(protocol)) {
            protocol = request.getScheme();
        }
        return protocol + url.substring(url.indexOf(":"), url.indexOf(request.getContextPath())) + request.getContextPath() + "/";
    }
}
