package com.dbt.framework.base.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dbt.datasource.util.ItemServerCache;

public class StartUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static WebApplicationContext webApplicationContext = null;
	private static Log log = LogFactory.getLog(StartUpServlet.class);

	/**
	 * DBT FrameWork Start
	 */
	public void init() throws ServletException {
		log.info("DBT Framwork StartUpServlet staring");
		ServletContext application = getServletContext();
		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(application);
		ItemServerCache.loadCache();
	}
}
