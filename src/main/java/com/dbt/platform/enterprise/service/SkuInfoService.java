package com.dbt.platform.enterprise.service;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.datadic.util.DatadicUtil;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.StringUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.enterprise.bean.StatisticsSku;
import com.dbt.platform.enterprise.dao.ISkuDao;
import com.dbt.platform.enterprise.dao.ISkuInfoDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;


/**
 * @author RoyFu 
 * @createTime 2016年4月21日 下午5:50:28
 * @description 
 */
@Service
public class SkuInfoService extends BaseService<SkuInfo> {

	@Autowired
	private ISkuInfoDao iSkuInfoDao;
	@Autowired
	private ISkuDao iSkuDao;
	@Autowired
	private VcodeActivityService vcodeActivityService;
    
    /**
     * 根据企业获取sku列表
     */
    public List<SkuInfo> queryForLst(SkuInfo queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return iSkuInfoDao.queryForLst(map);
    }

	/**
	 * 区分不同账户sku
	 * @param currentUser
	 * @param model
	 */
	public void initSkuAccount(SysUserBasis currentUser, Model model) {
		if (ObjectUtils.isEmpty(currentUser)) {
			return;
		}
		String projectServerName = currentUser.getProjectServerName();
		switch (projectServerName) {
			case "zhongLWX":
				model.addAttribute("jhSwitchFlag", DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory.FILTER_SWITCH_SETTING,DatadicUtil.dataDic.filterSwitchSetting.SWITCH_JH_SWEEP));
				break;
			case "zuijiu":
				model.addAttribute("zjSwitchFlag", DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory.FILTER_SWITCH_SETTING,DatadicUtil.dataDic.filterSwitchSetting.SWITCH_ZJ_SWEEP));
				break;
			default:
				model.addAttribute("mztSwitchFlag", DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory
						.FILTER_SWITCH_SETTING, DatadicUtil.dataDic.filterSwitchSetting.SWITCH_MZT_SWEEP));
				model.addAttribute("acMztSwitchFlag", DatadicUtil.getDataDicValue(DatadicUtil.dataDicCategory
						.FILTER_SWITCH_SETTING, DatadicUtil.dataDic.filterSwitchSetting.SWITCH_ACMZT_SWEEP));
				break;
		}
	}

    /**
     * 根据企业获取sku数量
     */
    public int queryForCount(SkuInfo queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return iSkuInfoDao.queryForCount(map);
    }
    
    /**
     * 根据企业获取sku列表
     * @param companyKey 企业KEY</br> 
     * @return List<SkuInfo> </br>
     */
    public List<SkuInfo> loadSkuListByCompany(String companyKey) {
        return iSkuInfoDao.loadSkuListByCompany(companyKey);
    }

    /**
     * 根据企业获取sku数量
     * @param companyKey 企业KEY</br> 
     * @return int </br>
     */
    public int loadSkuListByCompanyCount(String companyKey) {
        Integer count = iSkuInfoDao.loadSkuListByCompanyCount(companyKey);
        return null == count ? 0 : count.intValue();
    }

	/**
	 * 根据sku主键查询sku信息
	 * @param </br> 
	 * @return SkuInfo </br>
	 */
	public SkuInfo findById(String skuKey) {
		return iSkuInfoDao.findById(skuKey);
	}
	
	/**
	 * 获取与高印同步的SKU信息
	 * @return
	 */
	public List<SkuInfo> queryForCPIS() {
	    List<SkuInfo> queryLst = iSkuInfoDao.queryForCPIS();
	    List<SkuInfo> skuLst = new ArrayList<SkuInfo>();
	    SkuInfo sku = null;
	    if (!CollectionUtils.isEmpty(queryLst)) {
	        for (SkuInfo item : queryLst) {
                sku = new SkuInfo();
                sku.setSku_code(item.getSkuKey());
                sku.setSku_name(item.getSkuName());
                sku.setShort_name(item.getShortName());
                sku.setSku_brand(item.getSkuBrand());
                sku.setBar_code(item.getCommodityCode());
                sku.setSpec_desc(item.getSpecificationDesc());
                sku.setUpdate_time(item.getUpdateTime());
                skuLst.add(sku);
            }
	    }
        return skuLst;
	}

	/**
	 * 添加Sku信息
	 * @param skuInfo </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void insertSkuInfo(SkuInfo skuInfo) throws Exception {
        
        // 校验名称是否重复(SKU名称、SKU简称、item名称、码智联SKU主键)
        if ("1".equals(checkBussionName("skuInfo", "SKU_KEY", 
                    null, "sku_name", skuInfo.getSkuName()))) {
            throw new BusinessException("SKU名称已存在");
        }
        if ("1".equals(checkBussionName("skuInfo", "SKU_KEY",
                null, "SHORT_NAME", skuInfo.getShortName()))) {
            throw new BusinessException("SKU简称已存在");
        }

//        if ("1".equals(checkBussionName("skuInfo", "SKU_KEY",
//                null, "ITEM_NAME", skuInfo.getItemName()))) {
//            throw new BusinessException("item名称已存在");
//        }

        if (StringUtils.isNotBlank(skuInfo.getAcMztSku())) {
            if ("1".equals(checkBussionName("skuInfo", "SKU_KEY",
                    null, "AC_MZT_SKU", skuInfo.getAcMztSku()))) {
                throw new BusinessException("码智联SKU主键已存在");
            }
        }


        
		int num = iSkuInfoDao.findMaxSkuKeyNum() + 1;
		String skuKey = skuInfo.getCompanyKey() + "-" + StringUtil.lPad(String.valueOf(num), "0", 3);
		skuInfo.setSkuKey(skuKey);
		skuInfo.setBrandKey("00001");
		skuInfo.setCategoryKey("1031");
        if (StringUtils.isNotBlank(skuInfo.getMztSku())) {
			skuInfo.setMztSku(skuInfo.getMztSku().replace("，", ",").replace(" ", ""));
		}
        if (StringUtils.isNotBlank(skuInfo.getAcMztSku())) {
			skuInfo.setAcMztSku(skuInfo.getAcMztSku().replace("，", ",").replace(" ", ""));
		}
        if (StringUtils.isNotBlank(skuInfo.getJhSku())) {
			skuInfo.setJhSku(skuInfo.getJhSku().replace("，", ",").replace(" ", ""));
		}
		if (StringUtils.isNotBlank(skuInfo.getZjSku())) {
			skuInfo.setZjSku(skuInfo.getZjSku().replace("，", ",").replace(" ", ""));
		}
        if (StringUtils.isNotBlank(skuInfo.getVolume())) {
            skuInfo.setVolume(skuInfo.getVolume().replace(",", ""));
        }else{
			skuInfo.setVolume(null);
		}


		iSkuInfoDao.create(skuInfo);
		
		if (StringUtils.isNotBlank(skuInfo.getSpuKey())) {
	        CacheUtilNew.removeByKey("vpsSkuInfo" + "_" + skuInfo.getSpuKey());
			CacheUtilNew.removeGroupByKey("vpsSkuInfos_");

		}
        
        // 码中台SKU缓存
        if (StringUtils.isNotBlank(skuInfo.getMztSku())) {
            CacheUtilNew.removeGroupByKey(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + skuInfo.getMztSku());
        }
		// 嘉华SKU缓存
		if (StringUtils.isNotBlank(skuInfo.getJhSku())) {
			CacheUtilNew.removeGroupByKey(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + skuInfo.getJhSku());
		}
		// 最酒SKU缓存
		if (StringUtils.isNotBlank(skuInfo.getZjSku())) {
			CacheUtilNew.removeGroupByKey(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + skuInfo.getZjSku());
		}


		logService.saveLog("skuInfo", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(skuInfo), "创建SKU" + skuInfo.getSkuName());
	}

	/**
	 * 编辑Sku信息
	 * @param skuInfo </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void updateSkuInfo(SkuInfo skuInfo) throws Exception {
	    
	    SkuInfo oldSkuInfo = iSkuInfoDao.findById(skuInfo.getSkuKey());
        
        // 校验名称是否重复
        if ("1".equals(checkBussionName("skuInfo", "SKU_KEY", 
                    skuInfo.getSkuKey(), "sku_name", skuInfo.getSkuName()))) {
            throw new BusinessException("SKU名称已存在");
        }
        if ("1".equals(checkBussionName("skuInfo", "SKU_KEY",
                skuInfo.getSkuKey(), "SHORT_NAME", skuInfo.getShortName()))) {
            throw new BusinessException("SKU简称已存在");
        }

//        if ("1".equals(checkBussionName("skuInfo", "SKU_KEY",
//                skuInfo.getSkuKey(), "ITEM_NAME", skuInfo.getItemName()))) {
//            throw new BusinessException("item名称已存在");
//        }

        if (StringUtils.isNotBlank(skuInfo.getAcMztSku())) {
            if ("1".equals(checkBussionName("skuInfo", "SKU_KEY",
                    skuInfo.getSkuKey(), "AC_MZT_SKU", skuInfo.getAcMztSku()))) {
                throw new BusinessException("码智联SKU主键已存在");
            }
        }


		// 修改sku
        if (StringUtils.isNotBlank(skuInfo.getMztSku())) skuInfo.setMztSku(skuInfo.getMztSku().replace("，", ",").replace(" ", ""));
        if (StringUtils.isNotBlank(skuInfo.getAcMztSku())) skuInfo.setAcMztSku(skuInfo.getAcMztSku().replace("，", ",").replace(" ", ""));
        if (StringUtils.isNotBlank(skuInfo.getJhSku())) skuInfo.setJhSku(skuInfo.getJhSku().replace("，", ",").replace(" ", ""));
		if (StringUtils.isNotBlank(skuInfo.getZjSku())) skuInfo.setZjSku(skuInfo.getZjSku().replace("，", ",").replace(" ", ""));
        if (StringUtils.isNotBlank(skuInfo.getVolume())) {
			skuInfo.setVolume(skuInfo.getVolume().replace(",", ""));
		}else{
			skuInfo.setVolume(null);
		}

		iSkuInfoDao.update(skuInfo);
		
		// 查询该sku下有没有活动，活动存在则更新活动缓存
		String skuKey = skuInfo.getSkuKey();
		List<VcodeActivityCog> activityCogList = vcodeActivityService.queryActivityListBySkuKey(skuKey);
		if(null != activityCogList || !CollectionUtils.isEmpty(activityCogList)){
			// 删除活动缓存
			for (VcodeActivityCog vcodeActivityCog : activityCogList) {
			    CacheUtilNew.addKeyModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW
			            + Constant.DBTSPLIT + vcodeActivityCog.getVcodeActivityKey());
			}
		}
		
		String oldSpuKey = oldSkuInfo.getSpuKey();
		if (StringUtils.isNotBlank(oldSpuKey)) {
            CacheUtilNew.addKeyModelVal("vpsSkuInfo" + "_" + oldSpuKey);
		}
		if(StringUtils.isNotBlank(skuInfo.getSpuKey())) {
            CacheUtilNew.addKeyModelVal("vpsSkuInfo" + "_" + skuInfo.getSpuKey());
		}
		
		CacheUtilNew.addKeyModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_SINGIN_COG
		        + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
		CacheUtilNew.addKeyModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_BIND_PROMOTION_COG
		        + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
		CacheUtilNew.addKeyModelVal("vpsSkuInfo" + "_" + skuKey.replace("-", ""));
		CacheUtilNew.addGroupModelVal("vpsSkuInfos_");
        
        // 清除码中台相关缓存
		this.clearZtSkuCache(oldSkuInfo);
        this.clearZtSkuCache(skuInfo);
        logService.saveLog("skuInfo", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(skuInfo), "修改SKU" + skuInfo.getSkuName());
	}

	/**
	 * 删除Sku信息
	 * @param skuKey </br> 
	 * @return void </br>
	 * @throws Exception 
	 */
	public void deleteSkuInfo(String skuKey) throws Exception {
		SkuInfo skuInfo = iSkuInfoDao.findById(skuKey);
		iSkuInfoDao.deleteById(skuKey);
        if (StringUtils.isNotBlank(skuInfo.getSpuKey())) {
            CacheUtilNew.addKeyModelVal("vpsSkuInfo" + "_" + skuInfo.getSpuKey());
        }
        CacheUtilNew.addKeyModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_SINGIN_COG
                + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
        CacheUtilNew.addKeyModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_BIND_PROMOTION_COG
                + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
        CacheUtilNew.addKeyModelVal("vpsSkuInfo" + "_" + skuKey.replace("-", ""));
		CacheUtilNew.addGroupModelVal("vpsSkuInfos_");
        // 清除码中台相关缓存
        this.clearZtSkuCache(skuInfo);
	}

	/**
	 * 清空码中台相关缓存
	 * @param ztSkuKey 码中台SKU主键集合，以半角逗号分隔
	 */
	public void clearZtSkuCache(SkuInfo skuInfo) {
	    if (skuInfo == null) return;
	    String skuKeyStr = "";
	    if (StringUtils.isNotBlank(skuInfo.getMztSku())) skuKeyStr = skuKeyStr + "," + skuInfo.getMztSku();
	    if (StringUtils.isNotBlank(skuInfo.getAcMztSku())) skuKeyStr = skuKeyStr + "," + skuInfo.getAcMztSku();
	    if (StringUtils.isNotBlank(skuInfo.getJhSku())) skuKeyStr = skuKeyStr + "," + skuInfo.getJhSku();
		if (StringUtils.isNotBlank(skuInfo.getZjSku())) skuKeyStr = skuKeyStr + "," + skuInfo.getZjSku();
		List<String> ztSkuKeyLst = Arrays.asList(skuKeyStr.split(","));
	    for (String skuKey : ztSkuKeyLst) {
	        if (StringUtils.isBlank(skuKey)) continue;
            CacheUtilNew.addKeyModelVal(CacheKey.cacheKey.vcode.KEY_VCODE_ACTIVITY_COG_NEW + Constant.DBTSPLIT + skuKey);
        }
	}

	/**
	 * 根据spukey查询sku数量
	 * @param spuInfoKey
	 * @return
	 */
	public int queryForCountBySpuKey(String spuInfoKey) {
		return iSkuInfoDao.queryForCountBySpuKey(spuInfoKey);
	}

	/**
	 * 查询SKU
	 * @param isExistSpuKey	是否存在spuKey
	 * @param spuInfoKey	spu主键
	 * @return
	 */
	public List<SkuInfo> queryListByMap(boolean isExistSpuKey, String spuInfoKey) {
		Map<String, Object> map = new HashMap<>();
		map.put("spuInfoKey", spuInfoKey);
		map.put("isExistSpuKey", isExistSpuKey);
		return iSkuInfoDao.queryListByMap(map);
	}

    public void insertStatSku(StatisticsSku sku) {
		iSkuDao.create(sku);
    }

	public List<StatisticsSku> queryForListByStatKey(String statInfoKey) {
		return iSkuDao.queryForListByStatKey(statInfoKey);
	}

	public void deleteStatSkuInfo(String statInfoKey) {
		iSkuDao.deleteStatSkuInfo(statInfoKey);
	}
}
