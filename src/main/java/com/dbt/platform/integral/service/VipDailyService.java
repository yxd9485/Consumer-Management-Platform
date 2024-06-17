package com.dbt.platform.integral.service;

import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.integral.bean.VpsVipDailyTaskCog;
import com.dbt.platform.integral.dao.VipDailyDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VipDailyService {
    @Autowired
    private VipDailyDao vipDailyDao;

    public List<VpsVipDailyTaskCog> queryForLst(VpsVipDailyTaskCog queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return vipDailyDao.queryForLst(map);
    }

    public int queryForCount(VpsVipDailyTaskCog queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return vipDailyDao.queryForCount(map);
    }

    public String createVipDailyTask(SysUserBasis currentUser, VpsVipDailyTaskCog vpsVipDailyTaskCog) {
        // 查询是否已有此类型的任务
        int count = vipDailyDao.queryCountForTaskType(vpsVipDailyTaskCog.getTaskType());
        if (count != 0) {
            return "添加失败!此类型已有记录";
        }
        vpsVipDailyTaskCog.setCreateTime(DateUtil.getDateTime());
        vpsVipDailyTaskCog.setCreateUser(currentUser.getUserName());
        vpsVipDailyTaskCog.setUpdateTime(DateUtil.getDateTime());
        vpsVipDailyTaskCog.setUpdateUser(currentUser.getUserName());
        vipDailyDao.createVipDailyTask(vpsVipDailyTaskCog);
        return "添加成功";
    }

    public VpsVipDailyTaskCog findById(String infoKey) {
        return vipDailyDao.findById(infoKey);
    }

    public String updateVipDailyTask(SysUserBasis currentUser, VpsVipDailyTaskCog vpsVipDailyTaskCog) {
        vpsVipDailyTaskCog.setUpdateTime(DateUtil.getDateTime());
        vpsVipDailyTaskCog.setUpdateUser(currentUser.getUserName());
        vipDailyDao.updateVipDailyTask(vpsVipDailyTaskCog);
        return "修改成功";
    }
}
