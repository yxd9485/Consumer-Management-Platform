package com.dbt.platform.publish.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.platform.activity.bean.VcodeActivityCog;
import com.dbt.platform.activity.service.VcodeActivityService;
import com.dbt.platform.enterprise.bean.SkuInfo;
import com.dbt.platform.publish.bean.VpsActRule;
import com.dbt.platform.publish.dao.ActRuleDao;
/**
 * 
 * @ClassName: ActRuleService 
 * @Description: 发布系统活动规则Service
 * @author: bin.zhang
 * @date: 2019年12月20日 下午1:39:50
 */
@Service
public class ActRuleService {
	@Autowired
	private ActRuleDao actRuleDao;
    @Autowired
    private VcodeActivityService vcodeActivityService;

	public List<VpsActRule> queryForLst(VpsActRule queryBean, PageOrderInfo pageInfo) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    queryMap.put("pageInfo", pageInfo);
	    
	    return actRuleDao.queryForLst(queryMap);
	}

	public int queryForCount(VpsActRule queryBean) {
	    Map<String, Object> queryMap = new HashMap<String, Object>();
	    queryMap.put("queryBean", queryBean);
	    return actRuleDao.queryForCount(queryMap);
	}

	public void updateVpsActRule(VpsActRule vpsActRule) {
		actRuleDao.updateVpsActRule(vpsActRule);
		
	}

	public VpsActRule findById(String infoKey) {
		return actRuleDao.findById(infoKey);
	}

    public void addActRule(VpsActRule vpsActRule) {
		actRuleDao.addActRule(vpsActRule);
    }

	public List<String> findVcodeActivityKeys() {
		return actRuleDao.findVcodeActivityKeys();
	}
	
	/**
	 * 获取规则配置有效活动
	 * @return
	 */
	public List<VcodeActivityCog> queryValidActiviyCog(String vcodeActivityKey) {
        List<String> activityKeys = this.findVcodeActivityKeys();
        List<VcodeActivityCog> tempActivityCogLst = vcodeActivityService.findAllVcodeActivityList(Constant.activityType.activity_type0, vcodeActivityKey);
        List<VcodeActivityCog> activityCogLst = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tempActivityCogLst)) {
            for (VcodeActivityCog s : tempActivityCogLst) {
                if(!activityKeys.contains(s.getVcodeActivityKey())){
                    activityCogLst.add(s);
                }
            }
        }
        return activityCogLst;
	}
}
