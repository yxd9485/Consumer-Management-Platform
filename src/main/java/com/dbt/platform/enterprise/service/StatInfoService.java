package com.dbt.platform.enterprise.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.service.ServerInfoService;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.enterprise.bean.StatInfo;
import com.dbt.platform.enterprise.bean.StatisticsSku;
import com.dbt.platform.enterprise.dao.ISkuDao;
import com.dbt.platform.enterprise.dao.IStatInfoDao;

/**
 * @author RoyFu 
 * @createTime 2016年4月21日 下午5:50:28
 * @description 
 */
@Service
public class StatInfoService extends BaseService<StatInfo> {

	@Autowired
	private IStatInfoDao iStatInfoDao;
    @Autowired
    private ServerInfoService serverInfoService;
    @Autowired
    private ISkuDao skuDao;
    
    /**
     * 根据企业获取sku列表
     */
    public List<StatInfo> queryForLst(StatInfo queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return iStatInfoDao.queryForLst(map);
    }

    /**
     * 根据企业获取sku数量
     */
    public int queryForCount(StatInfo queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return iStatInfoDao.queryForCount(map);
    }

	/**
	 * 根据sku主键查询sku信息
	 * @param </br> 
	 * @return SkuInfo </br>
	 */
	public StatInfo findById(String statInfoKey) {
		return iStatInfoDao.findById(statInfoKey);
	}

	public void insertStatInfo(StatInfo statInfo) {
		iStatInfoDao.create(statInfo);
	}

	public void updateStatInfo(StatInfo statInfo) {
		iStatInfoDao.update(statInfo);
	}

	public void deleteStatInfo(String statInfoKey) {
		iStatInfoDao.deleteById(statInfoKey);
	}
	
	/**
	 * 获取统计分组有效的省区
	 * @return
	 */
	public List<ServerInfo> queryServerInfo() {
        List<ServerInfo> allServer = serverInfoService.getAllServer();
        List<ServerInfo> serverInfoList = new ArrayList<>();
        List<String> invalidServerLst = Arrays.asList(StringUtils.defaultString(DatadicUtil.getDataDicValue(DatadicKey
                .dataDicCategory.DATA_CONSTANT_CONFIG, DatadicKey.dataConstantConfig.UNINCLUDEDSERVER)).split(","));
        for (ServerInfo item : allServer) {
            if (invalidServerLst.contains(item.getProjectServerName())) continue;
            serverInfoList.add(item);
        }
        
        return serverInfoList;
	}
	
	/**
	 * 获取SKU参与过的统计分组
	 * @return
	 */
	public Map<String, String> queryStatNameForSku() {
	    Map<String, String> resultMap = new HashMap<>();
	    List<StatisticsSku> statSkuLst = skuDao.queryStatName();
	    for (StatisticsSku item : statSkuLst) {
	        resultMap.put(item.getProjectServerName() + ":" + item.getSkuKey(), item.getStatName());
        }
	    return resultMap;
	}
}
