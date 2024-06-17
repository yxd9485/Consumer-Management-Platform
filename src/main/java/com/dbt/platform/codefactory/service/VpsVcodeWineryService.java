package com.dbt.platform.codefactory.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;
import com.dbt.platform.codefactory.bean.VpsWinery;
import com.dbt.platform.codefactory.dao.IVpsVcodeFactoryDao;
import com.dbt.platform.codefactory.dao.IVpsVcodeWineryDao;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * Service
 * @author 
 *
 */
@Service("vpsVcodeWineryService")	
public class VpsVcodeWineryService extends BaseService<VpsWinery>{

	@Autowired
	private IVpsVcodeWineryDao iVpsVcodeWineryDao;
	@Autowired
	private VpsVcodeFactoryService vpsVcodeFactoryService;
	@Autowired
	private IVpsVcodeFactoryDao iVpsVcodeFactoryDao;
	

	public List<VpsWinery> queryForLst(VpsWinery queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return iVpsVcodeWineryDao.queryForLst(map);
	}

	public int queryForCount(VpsWinery queryBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		return iVpsVcodeWineryDao.queryForCount(map);
	}
	


	public VpsWinery findById(String key) {
		return iVpsVcodeWineryDao.findById(key);
	}

	public void insertInfo(VpsWinery info) {
		// 校验名称是否重复
		if (iVpsVcodeWineryDao.checkShort(info) > 0) {
			throw new BusinessException("赋码厂缩写已存在");
		}
		iVpsVcodeWineryDao.create(info);
		logService.saveLog("vcodeWinery", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(info),
				"创建酒厂" + info.getWineryName());

	}

	public void updateInfo(VpsWinery info) {
		// 校验名称是否重复
		if (iVpsVcodeWineryDao.checkShort(info) > 0) {
			throw new BusinessException("赋码厂缩写已存在");
		}	
		VpsWinery old = findById(String.valueOf(info.getId()));
		
		// 修改赋码厂对应的酒厂
		VpsVcodeFactory queryBean = new VpsVcodeFactory();
		queryBean.setServerWinery(String.valueOf(info.getId()));
		List<VpsVcodeFactory> result = vpsVcodeFactoryService.queryForLst(queryBean, null);
		if (!CollectionUtils.isEmpty(result)) {
			for (VpsVcodeFactory vpsVcodeFactory : result) {
				String winName = "";
				for (String str : vpsVcodeFactory.getServerWineryName().split(",")) {
					if (!old.getWineryName().equals(str)) {
						winName += str + ",";
					}
				}
				winName += info.getWineryName();
				vpsVcodeFactory.setServerWineryName(winName);
				iVpsVcodeFactoryDao.update(vpsVcodeFactory);
			}
		}
		iVpsVcodeWineryDao.update(info);
		logService.saveLog("vcodeWinery", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(info),
				"修改酒厂" + info.getWineryName());
	}

	public void deleteInfo(String key, SysUserBasis currentUser) {
		VpsWinery vpsWinery = findById(key);
		vpsWinery.setDeleteFlag("1");
		vpsWinery.fillUpdateFields(currentUser.getUserKey());
		iVpsVcodeWineryDao.update(vpsWinery);
		// 删除赋码厂对应的酒厂
		VpsVcodeFactory queryBean = new VpsVcodeFactory();
		queryBean.setServerWinery(key);
		List<VpsVcodeFactory> result = vpsVcodeFactoryService.queryForLst(queryBean, null);
		if (!CollectionUtils.isEmpty(result)) {
			for (VpsVcodeFactory vpsVcodeFactory : result) {
				String winId = "", winName = "";
				for (String str : vpsVcodeFactory.getServerWinery().split(",")) {
					if (!String.valueOf(vpsWinery.getId()).equals(str)) {
						winId += str + ",";
					}
				}
				for (String str : vpsVcodeFactory.getServerWineryName().split(",")) {
					if (!vpsWinery.getWineryName().equals(str)) {
						winName += str + ",";
					}
				}
				vpsVcodeFactory.setServerWinery(winId.contains(",") ? winId.substring(0, winId.length() - 1) : winId);
				vpsVcodeFactory.setServerWineryName(winName.contains(",") ? winName.substring(0, winName.length() - 1) : winName);
				iVpsVcodeFactoryDao.update(vpsVcodeFactory);
			}
		}
		logService.saveLog("vcodeWinery", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(vpsWinery),
				"修改酒厂" + vpsWinery.getWineryName());
	}

	public List<VpsWinery> findWineryByFactoryLst(String wineryName) {
		return iVpsVcodeWineryDao.findWineryByFactoryLst(wineryName);
	}

public static void main(String[] args) {
	String a = "aaa,aa,sas,";
	System.out.println(a.contains(","));
}
	
   
}
