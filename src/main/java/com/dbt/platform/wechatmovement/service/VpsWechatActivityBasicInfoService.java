package com.dbt.platform.wechatmovement.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtil;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.wechatmovement.bean.VpsWechatActivityBasicInfo;
import com.dbt.platform.wechatmovement.dao.IVpsWechatActivityBasicInfoDao;

@Service
public class VpsWechatActivityBasicInfoService extends BaseService<VpsWechatActivityBasicInfo> {

    @Autowired
    private IVpsWechatActivityBasicInfoDao wechatActivityBasicInfodao;
    @Autowired
	private VpsWechatMovementPeriodsService wechatMovementPeriodsService;

    /**
     * 获取记录List
     * @param pageInfo
     * @return
     */
	public List<VpsWechatActivityBasicInfo> queryPopssActivityInfoList(PageOrderInfo pageInfo) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("pageInfo", pageInfo);
		return wechatActivityBasicInfodao.queryPopssActivityInfoList(queryMap);
	}

	/**
	 * 获取记录Count
	 * @param pageInfo
	 * @return
	 */
	public int queryPopssActivityInfoCount(PageOrderInfo pageInfo) {
		return wechatActivityBasicInfodao.queryPopssActivityInfoCount();
	}
	
    /**
     * 查询微信运动配置信息
     *
     * @return
     * @throws Exception
     */
    public VpsWechatActivityBasicInfo findWechatActivityBasicInfo() {
        VpsWechatActivityBasicInfo basicInfo =wechatActivityBasicInfodao.findWechatActivityBasicInfo();
        String [] basicInfoCashLmite=  basicInfo.getExtractLimit().split(";");
          if(basicInfoCashLmite.length>1) {
              basicInfo.setFirstCash(basicInfoCashLmite[0].replace("1-",""));
              basicInfo.setSecondCash(basicInfoCashLmite[1].replace("2-",""));
          }
        return basicInfo;
    }

    /**
     * @author zzy
     * @createTime 2018/6/26 15:41
     * @description 保存或更新Popss活动基本信息
     */
    public void savePopssActivityBaseInfo(VpsWechatActivityBasicInfo 
    		popssActivityBaseInfo, String oldCurrencyPrice, String oldActivityMagnification,String oldBonusPool) throws Exception {
        StringBuffer sbf = new StringBuffer();
        if (null != popssActivityBaseInfo.getFirstCash() && !"".equals(popssActivityBaseInfo.getFirstCash())) {
            sbf.append("1-" + popssActivityBaseInfo.getFirstCash() + ";");
        }
        if (null != popssActivityBaseInfo.getSecondCash() && !"".equals(popssActivityBaseInfo.getSecondCash())) {
            sbf.append("2-" + popssActivityBaseInfo.getSecondCash());
        }
        popssActivityBaseInfo.setExtractLimit(sbf.toString());
        wechatActivityBasicInfodao.savePopssActivityBaseInfo(popssActivityBaseInfo);
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_WECHAT_ACTIVITY_BASIC_INFO);
        
        
        // 更新期表的虚拟数据倍数
        if(StringUtils.isNotBlank(popssActivityBaseInfo.getInfoKey())
        		&& StringUtils.isNotBlank(oldCurrencyPrice)
        		&& StringUtils.isNotBlank(oldActivityMagnification)
        		&& StringUtils.isNotBlank(oldBonusPool)
        		&& StringUtils.isNotBlank(popssActivityBaseInfo.getCurrencyPrice())
        		&& StringUtils.isNotBlank(popssActivityBaseInfo.getActivityMagnification())
        		&& StringUtils.isNotBlank(popssActivityBaseInfo.getBonusPool())
        		&& (!oldCurrencyPrice.equals(popssActivityBaseInfo.getCurrencyPrice())|| !oldBonusPool.equals(popssActivityBaseInfo.getBonusPool())
        				|| !oldActivityMagnification.equals(popssActivityBaseInfo.getActivityMagnification()))){
        	// 明天期数
        	String periodsKey = new DateTime().plusDays(1).toString("yyyyMMdd");
        	String currencyPrice = popssActivityBaseInfo.getCurrencyPrice();
        	String activityMagnification = popssActivityBaseInfo.getActivityMagnification();
        	String bonusPool= popssActivityBaseInfo.getBonusPool();
        	wechatMovementPeriodsService.updateActivityMagnification(periodsKey, currencyPrice, activityMagnification,bonusPool);
        }	
    }
}
