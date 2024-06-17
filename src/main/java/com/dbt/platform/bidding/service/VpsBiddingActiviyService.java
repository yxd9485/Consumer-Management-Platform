package com.dbt.platform.bidding.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.bidding.bean.VpsBiddingActiviy;
import com.dbt.platform.bidding.dao.IVpsBiddingActiviyDao;
import com.dbt.platform.doubleprize.bean.VpsVcodeDoublePrizeCog;

/**
  * 竞价活动配置表Service
 */
@Service
public class VpsBiddingActiviyService extends BaseService<VpsBiddingActiviy> {

	@Autowired
	private IVpsBiddingActiviyDao biddingActiviyDao;

	/**
	 * 获取活动
	 * 
	 * @param activityKey
	 * @return
	 */
	public VpsBiddingActiviy findActivityByKey(String activityKey) {
		return biddingActiviyDao.findById(activityKey);
	}

	/**
	 * 活动列表
	 * 
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 */
	public List<VpsBiddingActiviy> queryVcodeActivityList(
			VpsBiddingActiviy queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		return biddingActiviyDao.loadActivityList(queryMap);
	}

	/**
	 * 活动列表条数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int countVcodeActivityList(VpsBiddingActiviy queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return biddingActiviyDao.countActivityList(queryMap);
	}

	/**
	 * 创建活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws Exception 
	 */
	public void writeActivityCog(VpsBiddingActiviy activityCog, 
	                                String currentUserKey, Model model) throws Exception {
	    // 初始化活动主键
	    activityCog.setActivityKey(UUID.randomUUID().toString());
	    // 生成编号
	    activityCog.setActivityNo(getBussionNo("biddingActivity", "activity_no", Constant.OrderNoType.type_JJ));
	    
	    // 操作人信息
	    activityCog.fillFields(currentUserKey);
	    
	    if(StringUtils.isBlank(activityCog.getIsDedicated())) {
	    	activityCog.setIsDedicated("0");
	    }
	    
	    // 持久化当前活动
        biddingActiviyDao.create(activityCog);
	    
	    logService.saveLog("biddingActivity", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(activityCog), "创建竞价活动");
	}


	/**
	 * 更新活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws IOException 
	 */
	public void updateActivityCog(VpsBiddingActiviy activityCog, String currentUserKey, Model model) throws Exception {
		if(StringUtils.isBlank(activityCog.getIsDedicated())) {
	    	activityCog.setIsDedicated("0");
	    }
        activityCog.fillFields(currentUserKey);
        biddingActiviyDao.update(activityCog);
	    
        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.biddingActivity
                .KEY_BIDDING_ACTIVITY_COG + Constant.DBTSPLIT + activityCog.getActivityKey());
        
        logService.saveLog("biddingActivity", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(activityCog), "修改竞价活动");
	}
	
	
    /**
     * 修改活动的活动配置添加状态
     * 
     * @param vcodeActivityCog
     */
    public void changeStatus(String activityKey) {
    	VpsBiddingActiviy activityCog = findActivityByKey(activityKey);
        biddingActiviyDao.update(activityCog);
    }
    
    /**
	 *  根据活动ID获取活动信息
	 */
    public VpsBiddingActiviy findById(String id){
		return biddingActiviyDao.findById(id);
	}
    
    /**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("biddingActivity", "activity_key", infoKey, "activity_name", bussionName);
	}

	/**
	 * 删除活动
	 * @param activityKey
	 * @throws Exception 
	 */
	public void delete(String id) throws Exception {
		biddingActiviyDao.deleteById(id);
		// 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.biddingActivity
                .KEY_BIDDING_ACTIVITY_COG + Constant.DBTSPLIT + id);
	}

	/**
	 * 检验月擂台是否重复
	 * @param infoKey
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String checkMonthBidding(String infoKey, String startDate, String endDate) {
		boolean isRepetition  = false;
		List<VpsBiddingActiviy> biddingList = this.queryValidBiddingList("2");
		if(CollectionUtils.isNotEmpty(biddingList)) {
			for (VpsBiddingActiviy item : biddingList) {
				if(StringUtils.isNotBlank(infoKey) && infoKey.equals(item.getActivityKey())) continue;
				if(startDate.compareTo(item.getEndDate()) > 0 || endDate.compareTo(item.getStartDate()) < 0) {
					continue;
				}else {
					isRepetition = true;
					break;
				}
			}
		}
		if(isRepetition) {
			return "1";
		}
		return "0";
	}
	
	/**
	 * 查询有效竞价活动
	 * @param activityType
	 * @return
	 */
	private List<VpsBiddingActiviy> queryValidBiddingList(String activityType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("activityType", activityType);
		return biddingActiviyDao.queryValidBiddingList(map);
	}
	
}
