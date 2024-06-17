package com.dbt.platform.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.platform.system.bean.SysFunction;
import com.dbt.platform.system.bean.SysRole;
import com.dbt.platform.system.dao.ISysFunctionDao;

/**
 * @author RoyFu
 * @createTime 2016年4月21日 上午9:34:59
 * @description
 */
@Service
public class SysFunctionService extends BaseService<SysFunction> {

	@Autowired
	private ISysFunctionDao iSysFunctionDao;

	public List<SysFunction> getMenu(String userKey, String urlPrefix, List<SysRole> roleLst, String projectServerName) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userKey", userKey);
		map.put("projectServerName", projectServerName);
		if (StringUtils.isNotBlank(projectServerName)) {
		    List<String> roleKeyLst = new ArrayList<String>();
		    for (SysRole role : roleLst) {
                roleKeyLst.add(projectServerName + ":" + role.getRoleKey());
            }
	        map.put("roleKeyLst", roleKeyLst);
		}
		List<SysFunction> funcList = iSysFunctionDao.loadFunctionListByUserKey(map);
		Map<String, List<SysFunction>> funcMap = new HashMap<String, List<SysFunction>>();
		String url = null;
		List<SysFunction> list;
		for (SysFunction func : funcList) {
			url = func.getStartUrl();
			if (url != null && !url.trim().equals("") && !url.trim().equals("#")) {
				func.setStartUrl(urlPrefix + url);
                func.setUrl(url);
			} else {
				func.setStartUrl("#");
                func.setUrl("#");
			}
			list = funcMap.get(func.getParentKey());
			if (list == null) {
				list = new ArrayList<SysFunction>();
				funcMap.put(func.getParentKey(), list);
			}
			list.add(func);
		}
		List<SysFunction> rootList = funcMap.get("0");
		transferSubList(rootList, funcMap);
		return rootList;
	}

	private void transferSubList(List<SysFunction> funcList, Map<String, List<SysFunction>> funcMap) {
		if (null != funcList && !funcList.isEmpty()) {
			List<SysFunction> list;
			for (SysFunction func : funcList) {
				list = funcMap.get(func.getFunctionKey());
				if (list != null) {
					func.setSubFunctionList(list);
					transferSubList(list, funcMap);
				}
			}
		}
	}

	public String getMenuHtml(List<SysFunction> funcList) {
		StringBuffer sb = new StringBuffer("<ul id=\"nav\">");
		for (SysFunction func : funcList) {
			sb.append("<li><a href=\"" + func.getStartUrl() + "\""+" id="+"\"" +func.getUrl().replace("/","").replace(".","").split("\\?")[0]+"\" target=\"mainFrame\"><img src=\"../assets/img/leftmenu/"
					+ func.getMenuIcon() + ".png\" />" + func.getFunctionName() + "</a>");
			if (func.getSubFunctionList() != null && !func.getSubFunctionList().isEmpty()) {
				sb.append("<ul class=\"sub-menu\">");
				getSubMenuHtml(func.getSubFunctionList(), sb);
				sb.append("</ul>");
			}
			sb.append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}

	private String getSubMenuHtml(List<SysFunction> funcList, StringBuffer sb) {
		for (SysFunction func : funcList) {
			sb.append("<li><a href=\"" + func.getStartUrl()+ "\""+" id="+"\"" +func.getUrl().replace("/","").replace(".","").split("\\?")[0]
//			        + "\" target=\"mainFrame\"><i class=\"icon-angle-right\"></i>"
					+ "\" target=\"mainFrame\">"
					+ func.getFunctionName() + "</a>");

			if (func.getSubFunctionList() != null && !func.getSubFunctionList().isEmpty()) {
				sb.append("<ul class=\"sub-menu\">");
				getSubMenuHtml(func.getSubFunctionList(), sb);
				sb.append("</ul>");
			}
			sb.append("</li>");
		}
		return sb.toString();
	}

	/**
	 * 查询当前用户下所有的菜单按钮
	 *
	 * @param userKey 用户key
	 * @return
	 */
	public List<String> loadCurrentFunctionByUser(String userKey) {
		return iSysFunctionDao.loadCurrentFunctionByUser(userKey);
	}
}
