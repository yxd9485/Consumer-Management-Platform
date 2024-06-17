package com.dbt.platform.turntable.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VcodeActivityRebateRuleCog;

import java.util.List;

public interface TurntableRuleDao extends IBaseDao<VcodeActivityRebateRuleCog> {
    List<VcodeActivityRebateRuleCog> queryTurntableRuleByActivityKey(String activityKey);
}
