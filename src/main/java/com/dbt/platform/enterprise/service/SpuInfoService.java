package com.dbt.platform.enterprise.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.bean.SpuInfo;
import com.dbt.platform.enterprise.dao.ISkuInfoDao;
import com.dbt.platform.enterprise.dao.ISpuInfoDao;

@Service
public class SpuInfoService extends BaseService<SpuInfo> {

	@Autowired
	private ISpuInfoDao iSpuInfoDao;
	@Autowired
	private ISkuInfoDao iSkuInfoDao;
    
    /**
     * 根据企业获取spu列表
     */
    public List<SpuInfo> queryForLst(SpuInfo queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return iSpuInfoDao.queryForLst(map);
    }

    /**
     * 根据企业获取spu数量
     */
    public int queryForCount(SpuInfo queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return iSpuInfoDao.queryForCount(map);
    }
    
    /**
     * 根据企业获取spu列表
     * @param companyKey 企业KEY</br> 
     * @return List<SpuInfo> </br>
     */
    public List<SpuInfo> loadSpuListByCompany(String companyKey) {
        return iSpuInfoDao.loadSpuListByCompany(companyKey);
    }

    /**
     * 根据企业获取spu数量
     * @param companyKey 企业KEY</br> 
     * @return int </br>
     */
    public int loadSpuListByCompanyCount(String companyKey) {
        Integer count = iSpuInfoDao.loadSpuListByCompanyCount(companyKey);
        return null == count ? 0 : count.intValue();
    }

	/**
	 * 根据spu主键查询spu信息
	 * @param </br> 
	 * @return SpuInfo </br>
	 */
	public SpuInfo findById(String spuKey) {
		return iSpuInfoDao.findById(spuKey);
	}

	/**
	 * 添加spu信息
	 * @param SpuInfo </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void insertSpuInfo(SpuInfo spuInfo) throws Exception {
        
        // 校验名称是否重复
        if ("1".equals(checkBussionName("spuInfo", "spu_info_key", 
                    null, "spu_name", spuInfo.getSpuName()))) {
            throw new BusinessException("SPU名称已存在");
        }
        
        // 新增SPU
		String spuInfoKey = UUIDTools.getInstance().getUUID();
		spuInfo.setSpuInfoKey(spuInfoKey);
		iSpuInfoDao.create(spuInfo);
		
		// 更新SKU中SPU_INFO_KEY
		List<String> skuKeyList = Arrays.asList(spuInfo.getSkuKeyGroup().split(","));
		Map<String, Object> map = new HashMap<>();
		map.put("spuInfoKey", spuInfoKey);
		map.put("skuKeyList", skuKeyList);
		iSkuInfoDao.updateSpuInfo(map);
		
		for (String skuKey : skuKeyList) {
			SkuInfo sku = iSkuInfoDao.findById(skuKey);
			if(null != sku){
				// 删除SKU缓存
				CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + skuKey.replace("-", ""));
				// 删除原SPU缓存（分组概念），等分组规则做好后可以删除以下代码
				if (StringUtils.isNotBlank(sku.getSpuKey())) {
		            CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + sku.getSpuKey());
		        }
			}
		}
		logService.saveLog("spuInfo", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(spuInfo), "创建SPU" + spuInfo.getSpuName());
	}

	/**
	 * 编辑spu信息
	 * @param spuInfo </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void updateSpuInfo(SpuInfo spuInfo) throws Exception {
        
        // 校验名称是否重复
        if ("1".equals(checkBussionName("spuInfo", "spu_info_key", 
        		spuInfo.getSpuInfoKey(), "spu_name", spuInfo.getSpuName()))) {
            throw new BusinessException("SPU名称已存在");
        }
        
		// 修改spu
        iSpuInfoDao.update(spuInfo);
        
        String spuInfoKey = spuInfo.getSpuInfoKey();
		
        // 清空原来SKU对应的spu_info_key字段
        List<SkuInfo> oldSkuList = iSkuInfoDao.queryForListBySpuKey(spuInfoKey);
        iSkuInfoDao.clearSpuInfoKey(spuInfoKey);
        
        // 更新SKU中SPU_INFO_KEY
 		List<String> skuKeyList = Arrays.asList(spuInfo.getSkuKeyGroup().split(","));
 		Map<String, Object> map = new HashMap<>();
 		map.put("spuInfoKey", spuInfoKey);
 		map.put("skuKeyList", skuKeyList);
 		iSkuInfoDao.updateSpuInfo(map);
 		
 		for (String skuKey : skuKeyList) {
 			SkuInfo sku = iSkuInfoDao.findById(skuKey);
 			if(null != sku){
 				// 删除SKU缓存
 				CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + skuKey.replace("-", ""));
 				// 删除原SPU缓存（分组概念），等分组规则做好后可以删除以下代码
 				if (StringUtils.isNotBlank(sku.getSpuKey())) {
 		            CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + sku.getSpuKey());
 		        }
 			}
 		}
 		
 		// 删除原来SKU的缓存KEY
 		if(CollectionUtils.isNotEmpty(oldSkuList)){
 			for (SkuInfo skuInfo : oldSkuList) {
 				// 删除SKU缓存
 				CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + skuInfo.getSkuKey().replace("-", ""));
 				// 删除原SPU缓存（分组概念），等分组规则做好后可以删除以下代码
 				if (StringUtils.isNotBlank(skuInfo.getSpuKey())) {
 		            CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + skuInfo.getSpuKey());
 		        }
 			}
        }
        logService.saveLog("spuInfo", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(spuInfo), "修改SPU" + spuInfo.getSpuName());
	}

	/**
	 * 删除Spu信息
	 * @param spuKey </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void deleteSpuInfo(String spuInfoKey) throws Exception {
		SpuInfo spuInfo = iSpuInfoDao.findById(spuInfoKey);
		if(null != spuInfo){
			if(CollectionUtils.isNotEmpty(spuInfo.getSkuList())){
				// 清空SKU中的SPU关联
				iSkuInfoDao.clearSpuInfoKey(spuInfoKey);
				
				// 清空缓存
				for (SkuInfo skuInfo : spuInfo.getSkuList()) {
	 				// 删除SKU缓存
	 				CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + skuInfo.getSkuKey().replace("-", ""));
	 				// 删除原SPU缓存（分组概念），等分组规则做好后可以删除以下代码
	 				if (StringUtils.isNotBlank(skuInfo.getSpuKey())) {
	 		            CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + skuInfo.getSpuKey());
	 		        }
	 			}
			}
			
			// 删除SPU
			iSpuInfoDao.deleteById(spuInfoKey);
	        logService.saveLog("spuInfo", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(spuInfo), "删除SPU" + spuInfo.getSpuName());
		}
	}
}
