package com.dbt.platform.signin.service;

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
import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.cache.bean.CacheKey;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.signin.bean.VpsVcodeSigninCog;
import com.dbt.platform.signin.bean.VpsVcodeSigninSkuCog;
import com.dbt.platform.signin.dao.IVpsVcodeSigninCogDao;

/**
 * 签到活动配置表Service
 */
@Service
public class VpsVcodeSigninCogService extends BaseService<VpsVcodeSigninCog> {

	@Autowired
	private IVpsVcodeSigninCogDao signinCogDao;
	@Autowired
	private VpsVcodeSigninSkuCogService signinSkuCogService;

	/**
	 * 获取某个签到活动
	 * 
	 * @param activityKey
	 * @return
	 */
	public VpsVcodeSigninCog findActivityByKey(String activityKey) {
		return signinCogDao.findById(activityKey);
	}

	/**
	 * 活动列表
	 * 
	 * @param queryBean
	 * @param pageInfo
	 * @return
	 * @throws Exception 
	 */
	public List<VpsVcodeSigninCog> findVcodeActivityList(
	        VpsVcodeSigninCog queryBean, PageOrderInfo pageInfo) throws Exception {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		queryMap.put("pageInfo", pageInfo);
		List<VpsVcodeSigninCog> activityList = signinCogDao.loadActivityList(queryMap);
        if(null != activityList && !activityList.isEmpty()){
            
            String currDate = DateUtil.getDate();
            String cacheStr = CacheKey.cacheKey.vcode.KEY_VCODE_SINGIN_COG
                        + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT);
            Object cacheObj = CacheUtilNew.getCacheValue(cacheStr);
            List<VpsVcodeSigninCog> signinCogLst = null;
            if(null != cacheObj){
                signinCogLst = JSON.parseArray(cacheObj.toString(), VpsVcodeSigninCog.class);
            }
            
            if (CollectionUtils.isNotEmpty(signinCogLst)) {
                for (VpsVcodeSigninCog item : activityList) {
                    for (VpsVcodeSigninCog cacheItem : signinCogLst) {
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
	public int countVcodeActivityList(VpsVcodeSigninCog queryBean) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("queryBean", queryBean);
		return signinCogDao.countActivityList(queryMap);
	}

	/**
	 * 创建活动
	 * 
	 * @param signinCog
	 * @param currentUserKey
	 * @throws Exception 
	 */
	public void writeActivityCog(VpsVcodeSigninCog signinCog, String currentUserKey) throws Exception {
        
        // 校验签到的开始及结束日期的合法性
        validSignDate(signinCog);
	    
	    // 主键
		signinCog.setActivityKey(UUID.randomUUID().toString());
		// 生成编号
		signinCog.setActivityNo(getBussionNo("vcodeSignin", "activity_no", Constant.OrderNoType.type_QD));
		
		// 可疑用户限制
		if (!"1".equals(signinCog.getDoubtRebateFlag())) {
		    signinCog.setDoubtRebateFlag("0");
		}
        signinCog.setMoneyConfigFlag("0");
        signinCog.fillFields(currentUserKey);
        signinCogDao.create(signinCog);
		
		// SKU限制关系类型：0与、1或、2包含
		VpsVcodeSigninSkuCog signinSkuCogItem = null;
		List<VpsVcodeSigninSkuCog> signinSkuCogLst = new ArrayList<>();
		if ("2".equals(signinCog.getSkuRelationType())) {
		    signinSkuCogItem = new VpsVcodeSigninSkuCog();
		    signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
		    signinSkuCogItem.setActivityKey(signinCog.getActivityKey());
		    signinSkuCogItem.setSkuKey(StringUtils.join(signinCog.getSkuKey(), ","));
		    signinSkuCogItem.setSignType(signinCog.getSignType()[0]);
		    signinSkuCogItem.setSignOperator("3");
		    signinSkuCogItem.setSignNum(signinCog.getSignNum()[0]);
            signinSkuCogItem.setContinueFlag("0");
		    if (ArrayUtils.isNotEmpty(signinCog.getContinueFlag())) {
		        signinSkuCogItem.setContinueFlag("1");
		    }
		    signinSkuCogItem.fillFields(currentUserKey);
		    signinSkuCogLst.add(signinSkuCogItem);
		    
		} else {
            List<String> continueFlagLst = new ArrayList<>();
            if (signinCog.getContinueFlag() != null) {
                continueFlagLst = Arrays.asList(signinCog.getContinueFlag());
            }
		    for (int i = 0; i < signinCog.getSkuKey().length ; i++) {
		        signinSkuCogItem = new VpsVcodeSigninSkuCog();
	            signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
	            signinSkuCogItem.setActivityKey(signinCog.getActivityKey());
	            signinSkuCogItem.setSkuKey(signinCog.getSkuKey()[i]);
	            signinSkuCogItem.setSignType(signinCog.getSignType()[i]);
	            signinSkuCogItem.setSignOperator("3");
	            signinSkuCogItem.setSignNum(signinCog.getSignNum()[i]);
	            signinSkuCogItem.setContinueFlag("0");
	            if (continueFlagLst.contains(String.valueOf(i))) {
	                signinSkuCogItem.setContinueFlag("1");
	            }
	            signinSkuCogItem.fillFields(currentUserKey);
	            signinSkuCogLst.add(signinSkuCogItem);
            }
		}
		signinSkuCogService.batchWrite(signinSkuCogLst);
		logService.saveLog("vcodeSignin", Constant.OPERATION_LOG_TYPE.TYPE_1,
		                    JSON.toJSONString(signinCog), "创建" + signinCog.getActivityName());

        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_VCODE_SINGIN_COG 
                    + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
	}

	/**
	 * 更新活动
	 * 
	 * @param signinCog
	 * @param currentUserKey
	 * @throws IOException 
	 */
	public void updateActivityCog(VpsVcodeSigninCog signinCog, String currentUserKey, Model model) throws Exception {
	    
	    // 校验签到的开始及结束日期的合法性
	    validSignDate(signinCog);
       
	    // 保存活动
        if (!"1".equals(signinCog.getDoubtRebateFlag())) {
            signinCog.setDoubtRebateFlag("0");
        }
        signinCog.setUpdateTime(DateUtil.getDateTime());
        signinCog.setUpdateUser(currentUserKey);
        signinCogDao.update(signinCog);
        
        // 删除之前的配置，再持久化最新配置
        signinSkuCogService.deleteByActivitykey(signinCog.getActivityKey());
        
        // SKU限制关系类型：0与、1或、2包含
        VpsVcodeSigninSkuCog signinSkuCogItem = null;
        List<VpsVcodeSigninSkuCog> signinSkuCogLst = new ArrayList<>();
        if ("2".equals(signinCog.getSkuRelationType())) {
            signinSkuCogItem = new VpsVcodeSigninSkuCog();
            signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
            signinSkuCogItem.setActivityKey(signinCog.getActivityKey());
            signinSkuCogItem.setSkuKey(StringUtils.join(signinCog.getSkuKey(), ","));
            signinSkuCogItem.setSignType(signinCog.getSignType()[0]);
            signinSkuCogItem.setSignOperator("3");
            signinSkuCogItem.setSignNum(signinCog.getSignNum()[0]);
            signinSkuCogItem.setContinueFlag("0");
            if (ArrayUtils.isNotEmpty(signinCog.getContinueFlag())) {
                signinSkuCogItem.setContinueFlag("1");
            }
            signinSkuCogItem.fillFields(currentUserKey);
            signinSkuCogLst.add(signinSkuCogItem);
            
        } else {
            List<String> continueFlagLst = new ArrayList<>();
            if (signinCog.getContinueFlag() != null) {
                continueFlagLst = Arrays.asList(signinCog.getContinueFlag());
            }
            for (int i = 0; i < signinCog.getSkuKey().length; i++) {
                signinSkuCogItem = new VpsVcodeSigninSkuCog();
                signinSkuCogItem.setInfoKey(UUID.randomUUID().toString());
                signinSkuCogItem.setActivityKey(signinCog.getActivityKey());
                signinSkuCogItem.setSkuKey(signinCog.getSkuKey()[i]);
                signinSkuCogItem.setSignType(signinCog.getSignType()[i]);
                signinSkuCogItem.setSignOperator("3");
                signinSkuCogItem.setSignNum(signinCog.getSignNum()[i]);
                signinSkuCogItem.setContinueFlag("0");
                if (continueFlagLst.contains(String.valueOf(i))) {
                    signinSkuCogItem.setContinueFlag("1");
                }
                signinSkuCogItem.fillFields(currentUserKey);
                signinSkuCogLst.add(signinSkuCogItem);
            }
        }
        signinSkuCogService.batchWrite(signinSkuCogLst);
        logService.saveLog("vcodeSignin", Constant.OPERATION_LOG_TYPE.TYPE_2,
                            JSON.toJSONString(signinCog), "修改" + signinCog.getActivityName());

        // 删除活动缓存
        CacheUtilNew.removeByKey(CacheKey.cacheKey.vcode.KEY_VCODE_SINGIN_COG 
                    + Constant.DBTSPLIT + DateUtil.getDate(DateUtil.DEFAULT_DATE_FORMAT_SHT));
	}
	
	/**
	 * 校验签到的开始及结束日期的合法性
	 */
	public void validSignDate(VpsVcodeSigninCog signinCog) {
        if (Constant.periodType.PERIOD_TYPE0.equals(signinCog.getPeriodType())) {
            if (1 != DateUtil.getDayOfWeek(DateUtil
                    .getDateFromDay(signinCog.getStartDate(), DateUtil.DEFAULT_DATE_FORMAT))) {
                throw new BusinessException("周签到活动的开始日期必需为周一");
            }
            if (7 != DateUtil.getDayOfWeek(DateUtil
                    .getDateFromDay(signinCog.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT))) {
                throw new BusinessException("周签到活动的结束日期必需为周日");
            }
        } else if (Constant.periodType.PERIOD_TYPE1.equals(signinCog.getPeriodType())) {
            String endOfMonth = DateUtil.getDateTime(DateUtil.getMonthLastDay(DateUtil
                    .getDateFromDay(signinCog.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT)), DateUtil.DEFAULT_DATE_FORMAT);
            if (!"01".equals(signinCog.getStartDate().substring(8))
                        || !endOfMonth.equals(signinCog.getEndDate())) {
                throw new BusinessException("月签到活动的周期必需是完整的月");
            }
        }
	}
	
    /**
     * 修改活动的活动配置添加状态
     * 
     * @param vcodeActivityCog
     */
    public void changeStatus(String activityKey) {
        VpsVcodeSigninCog signinCog = findActivityByKey(activityKey);
        signinCog.setMoneyConfigFlag("1");
        signinCogDao.update(signinCog);
    }
    
	/**
	 * 转换成扫码活动对应
	 * 
	 * @param signinCog
	 * @return
	 */
	public VcodeActivityCog transformForActivityCog(VpsVcodeSigninCog signinCog) {
	    VcodeActivityCog activityCog = new VcodeActivityCog();
	    activityCog.setVcodeActivityKey(signinCog.getActivityKey());
	    activityCog.setVcodeActivityName(signinCog.getActivityName());
	    activityCog.setStartDate(signinCog.getStartDate());
	    activityCog.setEndDate(signinCog.getEndDate());
	    activityCog.setActivityType(Constant.activityType.activity_type4);
	    return activityCog;
	}

	public VpsVcodeSigninCog findById(String id){
		return 	signinCogDao.findById(id);
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("vcodeSignin", "activity_key", infoKey, "activity_name", bussionName);
	}
}
