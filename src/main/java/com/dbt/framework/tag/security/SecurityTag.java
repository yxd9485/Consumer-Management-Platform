package com.dbt.framework.tag.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.securityauth.SecurityContext;
import com.dbt.framework.util.UserThreadLocalUtil;

/**
 * 文件名：SecurityTag.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: 按钮安全控制标签<br>
 * 修改人: 谷长鹏<br>
 * 修改时间：2014-06-19 10:37:26<br>
 * 修改内容：新增<br>
 */
public class SecurityTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	/** 安全编码 */
	private String securityCode;

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public int doStartTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		// 得到session中用户相关信息
		SecurityContext securityContext = (SecurityContext) session
				.getAttribute(Constant.USER_SESSION + UserThreadLocalUtil.get("vjfSessionId"));
		List<String> permissList = securityContext.getPermissionList();
		if (permissList == null || permissList.isEmpty()) {
			permissList = new ArrayList<String>();
		}
		if (permissList.contains(this.securityCode)) {
			return 1;
		}
		return 0;
	}

}
