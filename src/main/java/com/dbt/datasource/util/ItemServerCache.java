package com.dbt.datasource.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.util.BeanFactoryUtil;
import com.dbt.framework.util.RedisApiUtil;
/**
 * 各项目配置缓存
 * @author zhaohongtao
 *
 * 2020年1月15日
 */
public class ItemServerCache {
	public synchronized static void loadCache() {
		loadItem();
	}
	/**
	 * 加载ServerInfo
	 */
	private static void loadItem() {
		Map<String,ServerInfo> itemCache = new HashMap<String,ServerInfo>();
		ServerInfoService serverInfoService=(ServerInfoService)BeanFactoryUtil.getbeanFromWebContext("serverInfoService");
		List<ServerInfo> list=serverInfoService.getAllServer();
		for (ServerInfo serverInfo : list) {
			if(StringUtils.isNotBlank(serverInfo.getProjectServerName())){
				itemCache.put(serverInfo.getProjectServerName(), serverInfo);
			}
		}
		
		// 加载数据源到缓存
		RedisApiUtil.getInstance().setObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO, itemCache);
		// 删除未更新的数据源标识
		RedisApiUtil.getInstance().del(false, CacheKey.cacheKey.KEY_UPDATE_PROJECT_SERVER_INFO_LIST);
	}
}
