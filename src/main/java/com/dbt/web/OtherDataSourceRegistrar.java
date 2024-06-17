package com.dbt.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.util.RedisApiUtil;
import com.google.common.collect.Maps;
import com.vjifen.server.base.datasource.database.IDDSDataBase;
import com.vjifen.server.base.datasource.database.VDataSourceProperties;

@Component
public class OtherDataSourceRegistrar implements IDDSDataBase {
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";

    @Autowired
    private VjifenManageConfig vjifenManageConfig;
    @Autowired
    private ServerInfoService serverInfoService;

    @Override
    public List<VDataSourceProperties> getProperties() {
        List<ServerInfo> infos = new ArrayList<ServerInfo>();
        if (StringUtils.isNotBlank(vjifenManageConfig.getSingleProjectServerName())) {
            infos.add(serverInfoService.findByProjectServerName(vjifenManageConfig.getSingleProjectServerName()));
        } else {
            infos = serverInfoService.getAllServer();
        }

        Map<String, ServerInfo> cache = Maps.newHashMap();
        Map<String, ServerInfo> cacheForItemValue = Maps.newHashMap();

        List<VDataSourceProperties> res = infos.stream().filter(e -> StringUtils.isNotBlank(e.getProjectServerName())).map(e -> {
            VDataSourceProperties p = new VDataSourceProperties();
            cache.put(e.getProjectServerName(), e);
            cacheForItemValue.put(e.getItemValue(), e);
            p.setKey(e.getProjectServerName());
            p.setDriverClassName(DRIVER);
            p.setUrl(e.getServerJdbc());
            p.setUsername(e.getServerU());
            p.setPassword(e.getServerP());
            return p;
        }).collect(Collectors.toList());

        //serverInfo更新缓存
        if (StringUtils.isBlank(vjifenManageConfig.getSingleProjectServerName())) {
            RedisApiUtil.getInstance().setObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO, cache);
            RedisApiUtil.getInstance().setObject(false, CacheKey.cacheKey.KEY_PROJECT_SERVER_INFO_FOR_ITEM_VALUE, cacheForItemValue);
        }
        return res;
    }
}
