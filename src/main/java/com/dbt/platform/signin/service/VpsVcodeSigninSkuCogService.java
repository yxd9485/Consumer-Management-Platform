package com.dbt.platform.signin.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.signin.bean.VpsVcodeSigninSkuCog;
import com.dbt.platform.signin.dao.IVpsVcodeSigninSkuCogDao;

/**
 * 签到活动配置表Service
 */
@Service
public class VpsVcodeSigninSkuCogService extends BaseService<VpsVcodeSigninSkuCog> {

	@Autowired
	private IVpsVcodeSigninSkuCogDao signinSkuCogDao;
	

    /**
     * 批量插入配置
     * @param map signinSkuCogLst
     */
    public void batchWrite(List<VpsVcodeSigninSkuCog> signinSkuCogLst) {
        
        Map<String, Object> map = new HashMap<>();
        map.put("signinSkuCogLst", signinSkuCogLst);

        Date currDate = DateUtil.getNow();
        int index = 0;
        for (VpsVcodeSigninSkuCog item : signinSkuCogLst) {
            index++;
            item.setCreateTime(DateUtil.getDateTime(DateUtil.add(
                    currDate, index, Calendar.SECOND), DateUtil.DEFAULT_DATETIME_FORMAT));
        }
        signinSkuCogDao.batchWrite(map);
    }
    
    /**
     * 更新配置
     * 
     * @param signinSkuCog
     */
    public void update(VpsVcodeSigninSkuCog signinSkuCog) {
        signinSkuCogDao.update(signinSkuCog);
    }
    
    /**
     * 获取签到活动对应的SKU限制配置
     * 
     * @param activityKey
     * @return
     */
    public List<VpsVcodeSigninSkuCog> queryByActivitykey(String activityKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("activityKey", activityKey);
        return signinSkuCogDao.queryByActivitykey(map);
    }
    
    /**
     * 物理删除签到活动对应的SKU限制配置
     * 
     * @param activityKey
     * @return
     */
    public void deleteByActivitykey(String activityKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("activityKey", activityKey);
        signinSkuCogDao.deleteByActivitykey(map);
    }

}
