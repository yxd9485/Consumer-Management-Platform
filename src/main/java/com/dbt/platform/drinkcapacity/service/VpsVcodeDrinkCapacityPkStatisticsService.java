package com.dbt.platform.drinkcapacity.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.platform.drinkcapacity.bean.VpsVcodeDrinkCapacityPkRecord;
import com.dbt.platform.drinkcapacity.bean.VpsVcodeDrinkCapacityPkStatistics;
import com.dbt.platform.drinkcapacity.dao.IVpsVcodeDrinkCapacityPkStatisticsDao;

/**
 * 用户酒量PK1V1情况Service
 */
@Service
public class VpsVcodeDrinkCapacityPkStatisticsService 
                extends BaseService<VpsVcodeDrinkCapacityPkStatistics> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private IVpsVcodeDrinkCapacityPkStatisticsDao pkStatisticsDao;
    
    /**
     * 获取用户的比赛情况
     * 
     * @param userKey
     * @return
     */
    public VpsVcodeDrinkCapacityPkStatistics findByUserKey(String userKey) {
        return pkStatisticsDao.findByUserKey(userKey);
    }
    
    /**
     * 更新用户酒量PK1V1情况
     * 
     * @param pkStatistics
     */
    public void updatePkStatistics(VpsVcodeDrinkCapacityPkRecord pkRecord) {
        
        Map<String, Object> map = new HashMap<>();
        map.put("lastPkTime", pkRecord.getBeginTime());
        if (Constant.PkStatus.status_1.equals(pkRecord.getPkStatus())) {
            
            // userKeyA
            map.put("userKey", pkRecord.getUserKeyA());
            map.put("lastPkResult", pkRecord.getScanNumA()>pkRecord.getScanNumB() ? "胜" : "负");
            map.put("winNum", pkRecord.getScanNumA()>pkRecord.getScanNumB() ? 1 : 0);
            map.put("loseNum", pkRecord.getScanNumA()>pkRecord.getScanNumB() ? 0 : 1);
            pkStatisticsDao.updatePkResult(map);
            
            // userKeyB
            map.put("userKey", pkRecord.getUserKeyB());
            map.put("lastPkResult", pkRecord.getScanNumB()>pkRecord.getScanNumA() ? "胜" : "负");
            map.put("winNum", pkRecord.getScanNumB()>pkRecord.getScanNumA() ? 1 : 0);
            map.put("loseNum", pkRecord.getScanNumB()>pkRecord.getScanNumA() ? 0 : 1);
            pkStatisticsDao.updatePkResult(map);
            
        } else {
            if (Constant.PkStatus.status_2.equals(pkRecord.getPkStatus())) {
                map.put("lastPkResult", "平");
                map.put("drawNum", 1);
            } else if (Constant.PkStatus.status_3.equals(pkRecord.getPkStatus())) {
                map.put("lastPkResult", "流局");
                map.put("unpkNum", 1);
            }
            
            // 更新userKeyA
            map.put("userKey", pkRecord.getUserKeyA());
            pkStatisticsDao.updatePkResult(map);
            // 更新userKeyB
            map.put("userKey", pkRecord.getUserKeyB());
            pkStatisticsDao.updatePkResult(map);
        }
        try {
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.drinkCapacityPk
                    .DRINKCAPACITY_PK_STATISTICS + "_" + pkRecord.getUserKeyA());
            CacheUtilNew.removeByKey(CacheUtilNew.cacheKey.drinkCapacityPk
                    .DRINKCAPACITY_PK_STATISTICS + "_" + pkRecord.getUserKeyB());
        } catch (Exception e) {
            log.error("刷新用户酒量PK1V1情况缓存失败", e);
        }
    }
}
