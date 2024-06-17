package com.dbt.framework.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AntiSqlInjectionIterceptor implements HandlerInterceptor {

	//配置不拦截的资源
	public String[] allowUrls;

	public void setAllowUrls(String[] allowUrls) {
		this.allowUrls = allowUrls;
	}
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
		
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
		if(null != allowUrls && allowUrls.length >= 1){
			for (String url : allowUrls) {
				if(requestUrl.contains(url)){
					return true;
				}
			}
		}
		
		//获取页面所有提交的参数
		Enumeration<String> params = request.getParameterNames();
		while(params.hasMoreElements()){
			String paramName = params.nextElement();
			String value = request.getParameter(paramName);
			value = StringEscapeUtils.escapeSql(value);
			value = escapeHtml(value);
			request.setAttribute(paramName, value);
		}
		
		return true;
	}
	
	private String escapeHtml(String value) {
		value = value.replaceAll("\"", "\\\"");
		value = value.replaceAll("\'", "\\\'");
        //value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "（").replaceAll("\\)", "）");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
                "\"\"");
        value = value.replaceAll("script", "");
        // 加入强制去掉前后空格的设置
        String vals = value.trim();
        return vals;
    }
	
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
	}

}
