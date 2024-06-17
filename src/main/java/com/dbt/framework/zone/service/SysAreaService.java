package com.dbt.framework.zone.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.datadic.util.DatadicKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.zone.bean.SysAreaM;
import com.dbt.framework.zone.dao.ISysAreaDao;

/**
 * 文件名: SysAreaService.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. <br>
 * 描述: 区域码表<br>
 * 修改人: FuZhongYue<br>
 * 修改时间：2014-03-17 15:40:45<br>
 * 修改内容：新增<br>
 */
@Service("sysAreaService")
public class SysAreaService {

	@Autowired
	private ISysAreaDao iSysAreaDao;

	
	/**
	 * 根据父编号查找数据
	 * 
	 * @param parentId
	 * @return
	 */
	public List<SysAreaM> findByParentId(String parentId) {
		return iSysAreaDao.findByParentId(parentId);
	}
	
    /**
     * 根据父编号查找数据
     * 
     * @param parentId
     * @return
     */
    public List<SysAreaM> findByParentIdForProject(String parentId, boolean isProjectArea) {
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", parentId);
        if (isProjectArea) {
            String projectArea = DatadicUtil.getDataDicValue(
            		DatadicKey.dataDicCategory.FILTER_COMPANY_INFO, 
					DatadicKey.filterCompanyInfo.PROJECT_AREA);
            if (StringUtils.isNotBlank(projectArea)) {
                map.put("projectArea", projectArea.split(","));
            }
        }
        return iSysAreaDao.findByParentIdForProject(map);
    }

	/**
	 * 根据当前编号查找数据
	 * 
	 * @param zoneId
	 * @return
	 */
	public SysAreaM findById(String zoneId) {
		return iSysAreaDao.findById(zoneId);
	}

	public String findZoneByCurId(String curId, boolean isProjectArea) {
		Map<String, Object> areaMap = new HashMap<String, Object>();
		// 状态正常
		areaMap.put("state", 0);

		try {
//			List<SysAreaM> areaList = new ArrayList<SysAreaM>();
//			SysAreaM curArea = iSysAreaDao.findById(curId);
//			areaList.add(curArea);
//			recursionArea(areaList, curArea);
//
//			List<SysAreaM> provinceList, cityList, districtList;
//
//			// 省
//			// provinceList = iSysAreaDao.findByParentId("0");
//			provinceList = findByParentIdForProject("0", isProjectArea);
//			areaMap.put("curprovince", areaList.get(areaList.size() - 1));
//			areaMap.put("province", provinceList);
//
//			if (areaList.size() == 2) {
//				// 市
//			    // cityList = iSysAreaDao.findByParentId(areaList.get(1).getAreaCode());
//				cityList = findByParentIdForProject(areaList.get(1).getAreaCode(), isProjectArea);
//				areaMap.put("curcity", areaList.get(0));
//				areaMap.put("city", cityList);
//			}
//
//			if (areaList.size() == 3) {
//				// 市
//				// cityList = iSysAreaDao.findByParentId(areaList.get(2).getAreaCode());
//                cityList = findByParentIdForProject(areaList.get(2).getAreaCode(), isProjectArea);
//				areaMap.put("curcity", areaList.get(1));
//				areaMap.put("city", cityList);
//
//				// 县(区)
//				// districtList = iSysAreaDao.findByParentId(areaList.get(1).getAreaCode());
//				districtList = findByParentIdForProject(areaList.get(1).getAreaCode(), isProjectArea);
//				areaMap.put("curdistrict", areaList.get(0));
//				areaMap.put("district", districtList);
//			}
			
			// 省列表
			areaMap.put("province", findByParentIdForProject("0", isProjectArea));
			if (StringUtils.isNotBlank(curId)) {
			    // 省级
			    if ("0000".equals(curId.substring(2))) {
			        areaMap.put("curprovince", findById(curId));
			        areaMap.put("city", findByParentIdForProject(curId, isProjectArea));
			        
			        // 县
			    } else if (!"00".equals(curId.substring(4))) {
			        areaMap.put("curprovince", findById(curId.substring(0, 2) + "0000"));
			        
			        areaMap.put("curcity", findById(curId.substring(0, 4) + "00"));
			        areaMap.put("city", findByParentIdForProject(curId.substring(0, 2) + "0000", isProjectArea));
			        
			        areaMap.put("curdistrict", findById(curId));
			        areaMap.put("district", findByParentIdForProject(curId.substring(0, 4) + "00", isProjectArea));
			        
			        // 市   
			    } else {
                    areaMap.put("curprovince", findById(curId.substring(0, 2) + "0000"));
                    
                    areaMap.put("curcity", findById(curId));
                    areaMap.put("city", findByParentIdForProject(curId.substring(0, 2) + "0000", isProjectArea));
                    
                    areaMap.put("district", findByParentIdForProject(curId.substring(0, 4) + "00", isProjectArea));
			    }
			}

		} catch (Exception e) {
			// 状态异常
			areaMap.put("state", 1);
			e.printStackTrace();
		}

		return JSON.toJSONString(areaMap);
	}

	private List<SysAreaM> recursionArea(List<SysAreaM> areaList,
			SysAreaM curArea) {
		if ("0".equals(curArea.getParentCode())) {
			return areaList;
		} else {
			curArea = iSysAreaDao.findById(curArea.getParentCode());
			areaList.add(curArea);
			recursionArea(areaList, curArea);
		}
		return areaList;
	}

	/**
	 * 根据当前区域名查找数据
	 * 
	 * @param zoneId
	 * @return
	 */
	public SysAreaM findSubChildCode(String areaname) {
		return iSysAreaDao.findSubChildCode(areaname);
	}

	/**
	 * 查询所有区域名称为“全部”的list
	 * 
	 * @return List<String> 区域id list
	 */
	public List<String> queryAllAreaNameIsAll() {
		return iSysAreaDao.queryAllAreaNameIsAll();
	}

	/**
	 * 根据MAP查询code
	 * 
	 * @param zoneId
	 * @return
	 */
	public SysAreaM findCodeByMap(Map<String, Object> map) {
		return iSysAreaDao.findCodeByMap(map);
	}
	
	/**
	 * 根据多个区域码信息得到区域对象信息
	 * 
	 * @param areaCodeList
	 * @return
	 */
	public List<SysAreaM> queryByAreaCode(List<String> areaCodeList) {
	    return iSysAreaDao.queryAreamByAreaCodes(areaCodeList);
	}
	
	/**
	 * 根据多个区域码信息得到区域对象信息
	 * 
	 * @param areaCodeList
	 * @return
	 */
	public List<SysAreaM> queryAllByAreaCode(List<String> areaCodeList) {
		return iSysAreaDao.queryAllAreamByAreaCodes(areaCodeList);
	}
	
	public void setCachedArea() throws Exception {
		Map<String, SysAreaM> areaMap = new HashMap<String, SysAreaM>();
		List<SysAreaM> areaList = iSysAreaDao.findRegionList();
		for (SysAreaM area : areaList) {
			areaMap.put(area.getAreaCode(), area);
		}
		CacheUtilNew.setCacheValue("region-info", areaMap);
	}
	

    
    /**
     * 依据区域code获取本身及上级名称
     * @param areaCode
     * @return
     */
    public String getAreaNameByCode(String areaCode) {
        if (StringUtils.isEmpty(areaCode)) return "";
        
        String areaName = "";
        // 全部
        if ("000000".equals(areaCode)) {
            areaName = "全部";
            
        } else if ("000001".equals(areaCode)) {
            areaName = "省外";
            
        } else {
            String finalName = findById(areaCode).getAreaName();
            
            // 最后2位不为00，则为县
            if (!"00".equals(areaCode.substring(4, 6))) {
                SysAreaM areaM = findById(areaCode.substring(0, 4) + "00");
                areaName = areaM.getAreaName() + " - " + finalName;
                areaM = findById(areaCode.substring(0, 2) + "0000");
                areaName = areaM.getAreaName() + " - " + areaName;
                
            // 市
            } else if (!"00".equals(areaCode.substring(2, 4))) {
                SysAreaM areaM = findById(areaCode.substring(0, 2) + "0000");
                areaName = areaM.getAreaName() + " - " + finalName;
                
            // 省
            } else {
                areaName = finalName;
            }
        }
        return areaName;
    }

	public List<SysAreaM> findRegionList() {
		return iSysAreaDao.findRegionList();
	}

	public List<SysAreaM> queryAllList() {
		return iSysAreaDao.queryAllList();
	}
}
