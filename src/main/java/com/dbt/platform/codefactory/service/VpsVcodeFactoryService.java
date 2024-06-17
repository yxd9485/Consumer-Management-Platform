package com.dbt.platform.codefactory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.datadic.bean.ServerInfo;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;
import com.dbt.platform.codefactory.dao.IVpsVcodeFactoryDao;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * Service
 * @author 
 *
 */
@Service("vpsVcodeFactoryService")	
public class VpsVcodeFactoryService extends BaseService<VpsVcodeFactory>{

	@Autowired
	private IVpsVcodeFactoryDao iVpsVcodeFactoryDao;
	

	public List<VpsVcodeFactory> queryForLst(VpsVcodeFactory queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return iVpsVcodeFactoryDao.queryForLst(map);
	}

	public int queryForCount(VpsVcodeFactory queryBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		return iVpsVcodeFactoryDao.queryForCount(map);
	}
	
	/**
	 * 查询省区
	 * @return
	 */
	public List<ServerInfo> queryProvinceLst() {
		List<ServerInfo> serverLst = new ArrayList<>();
		ServerInfo info = null;
		String projectName = DatadicUtil.getDataDicValue(DatadicKey.dataDicCategory.COMPANY_INFO,
				DatadicKey.companyInfo.PROJECT_SERVER_NAME);
		if (StringUtils.isNotBlank(projectName)) {
			String[] strArry = projectName.split(",");
			for (String str : strArry) {
				info = new ServerInfo();
				String[] serNameArray = str.split(":");
				info.setServerName(serNameArray[0]);
				info.setProjectServerName(serNameArray[1]);
				serverLst.add(info);
			}
		}
		return serverLst;
	}

	public VpsVcodeFactory findById(String id) {
		return iVpsVcodeFactoryDao.findById(id);
	}

	/**
	 * 新增
	 * @param info
	 */
	public void insertVcodeFactory(VpsVcodeFactory info) {
		// 校验名称是否重复
		if (iVpsVcodeFactoryDao.checkShort(info) > 0) {
			throw new BusinessException("赋码厂缩写已存在");
		}	
		iVpsVcodeFactoryDao.create(info);
		logService.saveLog("vcodeFactory", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(info),"创建赋码厂" + info.getFactoryName());

	}

	/**
	 * 修改
	 * @param info
	 */
	public void updateVcodeFactory(VpsVcodeFactory info) {
		// 校验名称是否重复
		if (iVpsVcodeFactoryDao.checkShort(info) > 0) {
			throw new BusinessException("赋码厂缩写已存在");
		}
		iVpsVcodeFactoryDao.update(info);
		logService.saveLog("vcodeFactory", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(info), "修改赋码厂" + info.getFactoryName());
	}

	
	public void deleteFactorynfo(String key, SysUserBasis currentUser) {
		VpsVcodeFactory factory = findById(key);
		factory.setDeleteFlag("1");
		factory.fillUpdateFields(currentUser.getUserKey());
		iVpsVcodeFactoryDao.update(factory);		
	}


     public static void main(String[] args) {
    	long  count = Long.parseLong("00000123");
		System.out.println(String.format("%08d", count));
	}

	public VpsVcodeFactory findByFactoryName(String qrcodeManufacture) {
		return iVpsVcodeFactoryDao.findByFactoryName(qrcodeManufacture);
	}   
}
