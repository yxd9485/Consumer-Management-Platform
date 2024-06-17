package com.dbt.platform.promotion.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;
import com.dbt.platform.activity.bean.VcodeActivityVpointsCog;
import com.dbt.platform.activity.service.VcodeActivityRebateRuleCogService;
import com.dbt.platform.promotion.bean.VpsVcodeBindPromotionCog;
import com.dbt.platform.promotion.dao.IVpsVcodeBindPromotionCogDao;
import com.dbt.platform.signin.bean.VpsVcodeSigninSkuCog;
import com.dbt.platform.signin.service.VpsVcodeSigninSkuCogService;

/**
 * 签到活动配置表Service
 */
@Service
public class VpsVcodeBindPromotionCogService extends BaseService<VpsVcodeBindPromotionCog> {

	@Autowired
	private IVpsVcodeBindPromotionCogDao promotionCogDao;
	@Autowired
	private VcodeActivityRebateRuleCogService rebateRuleCogService;
	@Autowired
	private VpsVcodeSigninSkuCogService signinSkuCogService;

	/**
	 * 获取活动
	 * 
	 * @param activityKey
	 * @return
	 */
	public VpsVcodeBindPromotionCog findActivityByKey(String activityKey) {
		return promotionCogDao.findById(activityKey);
	}

	/**
	 * 活动列表
	 * 
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 */
	public List<VpsVcodeBindPromotionCog> findVcodeActivityList(
	        VpsVcodeBindPromotionCog queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		List<VpsVcodeBindPromotionCog> activityList = promotionCogDao.loadActivityList(queryMap);
        if(null != activityList && !activityList.isEmpty()){
            
            String currDate = DateUtil.getDate();
            String cacheStr = CacheKey.cacheKey.vcode.KEY_VCODE_BIND_PROMOTION_COG
                        + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT);
            Object cacheObj = CacheUtilNew.getCacheValue(cacheStr);
            List<VpsVcodeBindPromotionCog> signinCogLst = null;
            if(null != cacheObj){
                signinCogLst = JSON.parseArray(cacheObj.toString(), VpsVcodeBindPromotionCog.class);
            }
            
            if (CollectionUtils.isNotEmpty(signinCogLst)) {
                for (VpsVcodeBindPromotionCog item : activityList) {
                    for (VpsVcodeBindPromotionCog cacheItem : signinCogLst) {
                        if (item.getActivityKey().equals(cacheItem.getActivityKey())) {
                            if(!item.getStartDate().equals(cacheItem.getStartDate())
                                    || !item.getEndDate().equals(cacheItem.getEndDate())){
                                if (currDate.compareTo(cacheItem.getEndDate()) > 0) {
                                    item.setIsBegin("缓存异常(已结束)");
                                    
                                } else if (currDate.compareTo(cacheItem.getStartDate()) < 0){
                                    item.setIsBegin("缓存异常(未开始)");
                                    
                                } else {
                                    item.setIsBegin("缓存异常(进行中)");
                                }
                            }
                            continue;
                        }
                    }
                }
            }
        }
		return activityList;
	}

	/**
	 * 活动列表条数
	 * 
	 * @param queryBean
	 * @return
	 */
	public int countVcodeActivityList(VpsVcodeBindPromotionCog queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return promotionCogDao.countActivityList(queryMap);
	}

	/**
	 * 创建活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws Exception 
	 */
	public void writeActivityCog(VpsVcodeBindPromotionCog activityCog, 
	                                String currentUserKey, Model model) throws Exception {
	    
	    // 校验当前促销SKU是否有正在进行的活动
	    List<VpsVcodeBindPromotionCog> promotionCogLst = 
	                        queryByPromotionSkuForActivityDate(activityCog);
	    if (CollectionUtils.isNotEmpty(promotionCogLst)) {
	        model.addAttribute("errMsg", "同一促销SKU同一时间内只能配置一个有效活动");
	        return;
	    }
	    
        // 主键
        activityCog.setActivityKey(UUID.randomUUID().toString());
        // 生成编号
	    activityCog.setActivityNo(getBussionNo("promotion", "activity_no", Constant.OrderNoType.type_KB));
        activityCog.setMoneyConfigFlag("0");
        activityCog.fillFields(currentUserKey);
        promotionCogDao.create(activityCog);
	        
        // SKU限制关系类型：0与、1或、2包含
        VpsVcodeSigninSkuCog signinSkuCogItem = null;
        List<VpsVcodeSigninSkuCog> signinSkuCogLst = new ArrayList<>();
        if ("2".equals(activityCog.getSkuRelationType())) {
            signinSkuCogItem = new VpsVcodeSigninSkuCog();
            signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
            signinSkuCogItem.setActivityKey(activityCog.getActivityKey());
            signinSkuCogItem.setSkuKey(StringUtils.join(activityCog.getSkuKey(), ","));
            signinSkuCogItem.setSignType(activityCog.getSignType()[0]);
            signinSkuCogItem.setSignOperator(activityCog.getSignOperator()[0]);
            signinSkuCogItem.setSignNum(activityCog.getSignNum()[0]);
            signinSkuCogItem.setContinueFlag("0");
            if (ArrayUtils.isNotEmpty(activityCog.getContinueFlag())) {
                signinSkuCogItem.setContinueFlag("1");
            }
            signinSkuCogItem.fillFields(currentUserKey);
            signinSkuCogLst.add(signinSkuCogItem);
           
        } else {
            List<String> continueFlagLst = new ArrayList<>();
            if (activityCog.getContinueFlag() != null) {
                continueFlagLst = Arrays.asList(activityCog.getContinueFlag());
            }
            for (int i = 0; i < activityCog.getSkuKey().length; i++) {
                signinSkuCogItem = new VpsVcodeSigninSkuCog();
                signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
                signinSkuCogItem.setActivityKey(activityCog.getActivityKey());
                signinSkuCogItem.setSkuKey(activityCog.getSkuKey()[i]);
                signinSkuCogItem.setSignType(activityCog.getSignType()[i]);
                signinSkuCogItem.setSignOperator(activityCog.getSignOperator()[i]);
                signinSkuCogItem.setSignNum(activityCog.getSignNum()[i]);
                signinSkuCogItem.setContinueFlag("0");
                if (continueFlagLst.contains(String.valueOf(i))) {
                    signinSkuCogItem.setContinueFlag("1");
                }
                signinSkuCogItem.fillFields(currentUserKey);
                signinSkuCogLst.add(signinSkuCogItem);
            }
        }
        signinSkuCogService.batchWrite(signinSkuCogLst);
        
        logService.saveLog("promotion", Constant.OPERATION_LOG_TYPE.TYPE_1, JSON.toJSONString(activityCog), "创建" + activityCog.getActivityName());

        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_VCODE_BIND_PROMOTION_COG 
                    + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
        model.addAttribute("errMsg", "添加成功");
	}

	/**
	 * 更新活动
	 * 
	 * @param activityCog
	 * @param currentUserKey
	 * @throws IOException 
	 */
	public void updateActivityCog(VpsVcodeBindPromotionCog activityCog, String currentUserKey, Model model) throws Exception {

//        // 校验当前促销SKU是否有正在进行的活动
//	    String promotionSkuKey = activityCog.getPromotionSkuKey();
//        List<VpsVcodeBindPromotionCog> promotionCogLst = queryByPromotionSkuForActivityDate(activityCog);
//        if (CollectionUtils.isNotEmpty(promotionCogLst) && (promotionCogLst.size() > 1 
//                || !promotionCogLst.get(0).getActivityKey().equals(activityCog.getActivityKey()))) {
//            model.addAttribute("errMsg", "同一促销SKU同一时间内只能配置一个有效活动");
//            return;
//        }
        activityCog.setUpdateTime(DateUtil.getDateTime());
        activityCog.setUpdateUser(currentUserKey);
        promotionCogDao.update(activityCog);
        
        // 删除之前的配置，再持久化最新配置
        signinSkuCogService.deleteByActivitykey(activityCog.getActivityKey());
        
        // SKU限制关系类型：0与、1或、2包含
        VpsVcodeSigninSkuCog signinSkuCogItem = null;
        List<VpsVcodeSigninSkuCog> signinSkuCogLst = new ArrayList<>();
        if ("2".equals(activityCog.getSkuRelationType())) {
            signinSkuCogItem = new VpsVcodeSigninSkuCog();
            signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
            signinSkuCogItem.setActivityKey(activityCog.getActivityKey());
            signinSkuCogItem.setSkuKey(StringUtils.join(activityCog.getSkuKey(), ","));
            signinSkuCogItem.setSignType(activityCog.getSignType()[0]);
            signinSkuCogItem.setSignOperator(activityCog.getSignOperator()[0]);
            signinSkuCogItem.setSignNum(activityCog.getSignNum()[0]);
            signinSkuCogItem.setContinueFlag("0");
            if (ArrayUtils.isNotEmpty(activityCog.getContinueFlag())) {
                signinSkuCogItem.setContinueFlag("1");
            }
            signinSkuCogItem.fillFields(currentUserKey);
            signinSkuCogLst.add(signinSkuCogItem);
            
        } else {
            List<String> continueFlagLst = new ArrayList<>();
            if (activityCog.getContinueFlag() != null) {
                continueFlagLst = Arrays.asList(activityCog.getContinueFlag());
            }
            for (int i = 0; i < activityCog.getSkuKey().length; i++) {
                signinSkuCogItem = new VpsVcodeSigninSkuCog();
                signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
                signinSkuCogItem.setActivityKey(activityCog.getActivityKey());
                signinSkuCogItem.setSkuKey(activityCog.getSkuKey()[i]);
                signinSkuCogItem.setSignType(activityCog.getSignType()[i]);
                signinSkuCogItem.setSignOperator(activityCog.getSignOperator()[i]);
                signinSkuCogItem.setSignNum(activityCog.getSignNum()[i]);
                signinSkuCogItem.setContinueFlag("0");
                if (continueFlagLst.contains(String.valueOf(i))) {
                    signinSkuCogItem.setContinueFlag("1");
                }
                signinSkuCogItem.fillFields(currentUserKey);
                signinSkuCogLst.add(signinSkuCogItem);
            }
        }
        signinSkuCogService.batchWrite(signinSkuCogLst);
        
        logService.saveLog("promotion", Constant.OPERATION_LOG_TYPE.TYPE_2, JSON.toJSONString(activityCog), "修改" + activityCog.getActivityName());

        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_VCODE_BIND_PROMOTION_COG 
                    + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
        model.addAttribute("errMsg", "修改成功");
	}
	
	/**
	 * 获取促销SKU当前有效活动
	 */
	public List<VpsVcodeBindPromotionCog> queryByPromotionSkuForActivityDate(VpsVcodeBindPromotionCog promotionCog) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("promotionSkuKey", promotionCog.getPromotionSkuKey());
	    map.put("startDate", promotionCog.getStartDate());
	    map.put("endDate", promotionCog.getEndDate());
	    return promotionCogDao.queryByPromotionSkuForActivityDate(map);
	}
	
    /**
     * 修改活动的活动配置添加状态
     * 
     * @param vcodeActivityCog
     */
    public void changeStatus(String activityKey) {
        VpsVcodeBindPromotionCog promotionCog = findActivityByKey(activityKey);
        promotionCog.setMoneyConfigFlag("1");
        promotionCogDao.update(promotionCog);
    }
    
	/**
	 * 转换成扫码活动对应
	 * 
	 * @param promotionCog
	 * @return
	 */
	public VcodeActivityCog transformForActivityCog(VpsVcodeBindPromotionCog promotionCog) {
	    VcodeActivityCog activityCog = new VcodeActivityCog();
	    activityCog.setVcodeActivityKey(promotionCog.getActivityKey());
	    activityCog.setVcodeActivityName(promotionCog.getActivityName());
	    activityCog.setStartDate(promotionCog.getStartDate());
	    activityCog.setEndDate(promotionCog.getEndDate());
	    if (StringUtils.isNotBlank(promotionCog.getDoubtRuleType())) {
	        activityCog.setDoubtRuleType(promotionCog.getDoubtRuleType());
	    }
        if (StringUtils.isNotBlank(promotionCog.getDoubtRuleCoe())) {
            activityCog.setDoubtRuleCoe(Integer.valueOf(promotionCog.getDoubtRuleCoe()));
        }
        activityCog.setDoubtRuleRangeMin(promotionCog.getDoubtRuleRangeMin());
        activityCog.setDoubtRuleRangeMax(promotionCog.getDoubtRuleRangeMax());
        activityCog.setBlacklistFlag("1");
	    activityCog.setActivityType(Constant.activityType.activity_type5);
	    return activityCog;
	}

	public VpsVcodeBindPromotionCog findById(String id){
       return  promotionCogDao.findById(id);
    }
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("promotion", "activity_key", infoKey, "activity_name", bussionName);
	}
}
