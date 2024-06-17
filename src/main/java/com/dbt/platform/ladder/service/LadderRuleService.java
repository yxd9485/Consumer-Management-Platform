package com.dbt.platform.ladder.service;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.CacheUtilNew;
import com.dbt.framework.util.CheckUtil;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.dao.IVcodeActivityCogDao;
import com.dbt.platform.ladder.bean.LadderRuleCog;
import com.dbt.platform.ladder.dao.ILadderRuleDao;
import com.dbt.platform.system.bean.SysUserBasis;

/**
 * 阶梯规则Service
 * @author hanshimeng
 *
 */
@Service
public class LadderRuleService extends BaseService<LadderRuleCog> {

	@Autowired
	private ILadderRuleDao ladderRuleDao;
	@Autowired
	private IVcodeActivityCogDao activityCogDao;
	
	public List<LadderRuleCog> findLadderRuleList(LadderRuleCog queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return ladderRuleDao.queryForLst(map);
	}
	
	public int findLadderRuleCount(LadderRuleCog queryBean) {
		Map<String, Object> map = new HashMap<>();
		map.put("queryBean", queryBean);
		return ladderRuleDao.queryForCount(map);
	}

	public LadderRuleCog findById(String id) {
		return ladderRuleDao.findById(id);
	}
	
	/**
	 * 检验名称是否重复
	 * @param infoKey		主键（修改页面需传递）
	 * @param bussionName	名称
	 * @return
	 */
	public String checkBussionName(String infoKey, String bussionName) {
		return checkBussionName("ladderRuleCog", "info_key", infoKey, "rule_name", bussionName);
	}

	
	/**
	 * 检验活动是否可配置阶梯
	 * @param activityKeys
	 * @return
	 */
	public String checkActivityForLadder(String activityKeys, String ladderInfoKey,String startDate,String endDate) {
		String activityNames = "";
		
		LadderRuleCog rule = ladderRuleDao.findById(ladderInfoKey);
		Map<String, Object> map = new HashMap<>();
		map.put("activityKeys", Arrays.asList(activityKeys.split(",")));
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		List<VcodeActivityCog> activityList = activityCogDao.queryActivityForLadder(map);
		if(CollectionUtils.isNotEmpty(activityList)){
			for (VcodeActivityCog item : activityList) {
				boolean flag = false;
//				if (DateUtil.isInTheRound(item.getStartDate(), startDate, endDate,DateUtil.DEFAULT_DATE_FORMAT)
//					||	DateUtil.isInTheRound(item.getEndDate(), startDate,endDate,DateUtil.DEFAULT_DATE_FORMAT)) {
//					flag = true;
//				}
				// as>be || ae<bs
				if(startDate.compareTo(item.getEndDate()) > 0 || endDate.compareTo(item.getStartDate()) < 0) {
					flag = false;
				}else {
					flag = true;
				}
				if(flag && (CheckUtil.isEmpty(rule) || !rule.getInfoKey().equals(item.getLadderInfoKey()))){
					activityNames += item.getVcodeActivityName() + ",";
				}
			}
		}
		return StringUtils.isNotBlank(activityNames) ? activityNames.substring(0, activityNames.length() -1) : "1";
	}

	public String create(LadderRuleCog ladderRuleCog) throws Exception {
		ladderRuleCog.setInfoKey(UUID.randomUUID().toString());
		// 生成编号
		ladderRuleCog.setRuleNo(
				getBussionNo("ladderRuleCog", "rule_no", Constant.OrderNoType.type_JT));
		if(Constant.ladderRuleFalg.flag_2.equals(ladderRuleCog.getRuleFlag())){
			ladderRuleCog.setLadderStartTime(null);
			ladderRuleCog.setLadderEndTime(null);
		}
		ladderRuleDao.create(ladderRuleCog);
        
        // 删除缓存
        String key = CacheUtilNew.cacheKey.vodeActivityKey
                .KEY_LADDER_RULE_COG + Constant.DBTSPLIT + DateUtil.getDate();
        CacheUtilNew.removeGroupByKey(key);
		return ladderRuleCog.getInfoKey();
	}

	public void update(LadderRuleCog ladderRuleCog) throws Exception {
		if(Constant.ladderRuleFalg.flag_2.equals(ladderRuleCog.getRuleFlag())){
			ladderRuleCog.setLadderStartTime(null);
			ladderRuleCog.setLadderEndTime(null);
		}
		ladderRuleDao.update(ladderRuleCog);
        
        // 删除缓存
        String key = CacheUtilNew.cacheKey.vodeActivityKey
                .KEY_LADDER_RULE_COG + Constant.DBTSPLIT + DateUtil.getDate();
        CacheUtilNew.removeGroupByKey(key);
	}

	public void delete(String infoKey, SysUserBasis currentUser) throws Exception {
		LadderRuleCog ladderRuleCog = findById(infoKey);
		if(null != ladderRuleCog){
			ladderRuleCog.fillFields(currentUser.getUserKey());
			ladderRuleCog.setDeleteFlag("1");
			ladderRuleDao.update(ladderRuleCog);
			
			// 删除缓存
			String key = CacheUtilNew.cacheKey.vodeActivityKey
					.KEY_LADDER_RULE_COG + Constant.DBTSPLIT + DateUtil.getDate();
			CacheUtilNew.removeGroupByKey(key);
		}
	}

	/**
	 * 根据活动KEY查询阶梯规则
	 * @param infoKey
	 * @return
	 */
	public LadderRuleCog findByActivityKey(String activityKey) {
		return ladderRuleDao.findByActivityKey(activityKey);
	}
}
