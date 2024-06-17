package com.dbt.platform.taoeasteregg.service;

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
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.system.bean.SysUserBasis;
import com.dbt.platform.taoeasteregg.bean.VpsVcodeTaoEasterEggCog;
import com.dbt.platform.taoeasteregg.dao.IVpsVcodeTaoEasterEggCogDao;

/**
 * 淘彩蛋Service
 */
@Service
public class VpsVcodeTaoEasterEggCogService extends BaseService<VpsVcodeTaoEasterEggCog> {

	@Autowired
	private IVpsVcodeTaoEasterEggCogDao easterEggCogDao;
	@Autowired
	private VcodeActivityService activityService;
	
	public List<VpsVcodeTaoEasterEggCog> queryForLst(VpsVcodeTaoEasterEggCog queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return easterEggCogDao.queryForLst(map);
	}
	
	public int queryForCount(VpsVcodeTaoEasterEggCog queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return easterEggCogDao.queryForCount(map);
	}

	public VpsVcodeTaoEasterEggCog findById(String id) {
		return easterEggCogDao.findById(id);
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("vpsVcodeTaoEasterEggCog", "info_key", infoKey, "tao_name", bussionName);
	}
	
	/**
	 * 配置活动冲突校验
	 * 
	 * @param infoKey  彩蛋规则主键
	 * @param vcodeActivityKeys    关联的活动主键
	 * @param startDate    规则开始日期
	 * @param endDate      规则结束日期
	 */
	public void validActivityConflict(String infoKey, String vcodeActivityKeys, String startDate, String endDate) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("infoKey", infoKey);
	    map.put("activityKeyAry", vcodeActivityKeys.split(","));
	    map.put("startDate", startDate);
	    map.put("endDate", endDate);
	    List<VpsVcodeTaoEasterEggCog> easterEggCogLst = easterEggCogDao.queryForConflictCheck(map);
	    if (CollectionUtils.isNotEmpty(easterEggCogLst)) {
	        StringBuffer buffer = new StringBuffer();
	        List<VcodeActivityCog> vcodeActivityCogLst = activityService.queryByActivityKey(vcodeActivityKeys.split(","));
	        buffer.append(startDate + "至" + endDate + "期间与以下活动冲突\n</br>");
	        for (VcodeActivityCog activityCog : vcodeActivityCogLst) {
	            for (VpsVcodeTaoEasterEggCog easterEggCog : easterEggCogLst) {
	                if (easterEggCog.getVcodeActivityKeys().indexOf(activityCog.getVcodeActivityKey()) != -1) {
	                    buffer.append("活动：").append(activityCog.getVcodeActivityName());
	                    buffer.append("，彩蛋规则：").append(easterEggCog.getTaoName()).append("\n</br>");
	                    break;
	                }
	            }
            }
	        throw new BusinessException(buffer.toString());
	    }
	}

	public void create(VpsVcodeTaoEasterEggCog easterEggCog) {
	    // 配置活动冲突校验
	    validActivityConflict(null, easterEggCog.getVcodeActivityKeys(), easterEggCog.getStartDate(), easterEggCog.getEndDate());
	    
		// 生成编号
		easterEggCog.setTaoNo(getBussionNo("vpsVcodeTaoEasterEggCog", "tao_no", Constant.OrderNoType.type_CD));
		easterEggCog.setLadderRule(easterEggCog.getLadderRule().replace("，", ","));
		easterEggCogDao.create(easterEggCog);
		
		logService.saveLog("taoEasterEggCog", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(easterEggCog), "新增淘彩蛋规则");
	}

	public void update(VpsVcodeTaoEasterEggCog easterEggCog) throws Exception {
        // 配置活动冲突校验
        validActivityConflict(easterEggCog.getInfoKey(), easterEggCog
                .getVcodeActivityKeys(), easterEggCog.getStartDate(), easterEggCog.getEndDate());
        easterEggCog.setLadderRule(easterEggCog.getLadderRule().replace("，", ","));
        
		easterEggCogDao.update(easterEggCog);
        logService.saveLog("taoEasterEggCog", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(easterEggCog), "修改淘彩蛋规则");
	}

	public void delete(String infoKey, SysUserBasis currentUser) throws Exception {
	    VpsVcodeTaoEasterEggCog easterEggCog = findById(infoKey);
		if(null != easterEggCog){
			easterEggCog.fillFields(currentUser.getUserKey());
			easterEggCog.setDeleteFlag("1");
			easterEggCogDao.update(easterEggCog);
			
			// 删除缓存
			String key = CacheUtilNew.cacheKey.vodeActivityKey
					.KEY_TAO_EASTEREGG_COG + Constant.DBTSPLIT + DateUtil.getDate();
			CacheUtilNew.removeGroupByKey(key);
		}
        logService.saveLog("taoEasterEggCog", Constant.OPERATION_LOG_TYPE.TYPE_3, JSON.toJSONString(easterEggCog), "删除淘彩蛋规则");
	}

	public VpsVcodeTaoEasterEggCog findByActivityKey(String activityKey) {
		return easterEggCogDao.findByActivityKey(activityKey);
	}
}
