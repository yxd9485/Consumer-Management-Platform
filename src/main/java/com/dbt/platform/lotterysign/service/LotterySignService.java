package com.dbt.platform.lotterysign.service;

import com.dbt.framework.base.exception.BusinessException;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.platform.lotterysign.bean.VpsLotterySignCog;
import com.dbt.platform.lotterysign.dao.LotterySignDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LotterySignService {
    @Autowired
    private LotterySignDao lotterySignDao;

    public List<VpsLotterySignCog> queryForList(VpsLotterySignCog queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return lotterySignDao.queryForList(map);
    }

    public int queryForCount(VpsLotterySignCog queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return lotterySignDao.queryForCount(map);
    }

    public String doLotterySignAdd(VpsLotterySignCog vpsLotterySignCog, SysUserBasis currentUser) {
        // 查询时间段是否已有活动
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", vpsLotterySignCog.getStartDate());
        map.put("endDate", vpsLotterySignCog.getEndDate());
        map.put("infoKey", "");
        int count = lotterySignDao.queryOverlapActivity(map);
        if (count != 0) {
            return "添加失败!所选时间段已有活动!";
        }
        // 验证活动结束时间是否为每月最后一天或每周最后一天
        if (vpsLotterySignCog.getCycleType().equals("1")) {
            String endOfMonth = DateUtil.getDateTime(DateUtil.getMonthLastDay(DateUtil
                    .getDateFromDay(vpsLotterySignCog.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT)), DateUtil.DEFAULT_DATE_FORMAT);
            if (!endOfMonth.equals(vpsLotterySignCog.getEndDate())) {
                return "添加失败!周期类型为月时,活动结束时间只能是当月最后一天!";
            }
        } else {
            if (7 != DateUtil.getDayOfWeek(DateUtil.getDateFromDay(vpsLotterySignCog.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT))) {
                throw new BusinessException("周期类型为自然周时活动的结束日期必需为周日");
            }
        }

        vpsLotterySignCog.setCreateTime(DateUtil.getDateTime());
        vpsLotterySignCog.setUpdateTime(DateUtil.getDateTime());
        vpsLotterySignCog.setCreateUser(currentUser.getUserName());
        vpsLotterySignCog.setUpdateUser(currentUser.getUserName());
        lotterySignDao.doLotterySignAdd(vpsLotterySignCog);
        return "新增成功";
    }

    public VpsLotterySignCog findById(String infoKey) {
        return lotterySignDao.findById(infoKey);
    }

    public String doLotterySignEdit(VpsLotterySignCog vpsLotterySignCog, SysUserBasis currentUser) {
        if (!vpsLotterySignCog.getStatus().equals("0")) {
            vpsLotterySignCog.setStartDate(null);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", StringUtils.isNotBlank(vpsLotterySignCog.getStartDate())?vpsLotterySignCog.getStartDate():vpsLotterySignCog.getNoEditStartDate());
        map.put("endDate", vpsLotterySignCog.getEndDate());
        map.put("infoKey", vpsLotterySignCog.getInfoKey());
        int count = lotterySignDao.queryOverlapActivity(map);
        if (count != 0) {
            return "修改失败!所选时间段已有活动!";
        }

        // 验证活动结束时间是否为每月最后一天或每周最后一天
        if (vpsLotterySignCog.getCycleType().equals("1")) {
            String endOfMonth = DateUtil.getDateTime(DateUtil.getMonthLastDay(DateUtil
                    .getDateFromDay(vpsLotterySignCog.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT)), DateUtil.DEFAULT_DATE_FORMAT);
            if (!endOfMonth.equals(vpsLotterySignCog.getEndDate())) {
                return "修改失败!周期类型为月时,活动结束时间只能是当月最后一天!";
            }
        } else {
            if (7 != DateUtil.getDayOfWeek(DateUtil.getDateFromDay(vpsLotterySignCog.getEndDate(), DateUtil.DEFAULT_DATE_FORMAT))) {
                throw new BusinessException("周期类型为自然周时活动的结束日期必需为周日");
            }
        }

        vpsLotterySignCog.setUpdateTime(DateUtil.getDateTime());
        vpsLotterySignCog.setUpdateUser(currentUser.getUserName());
        lotterySignDao.doLotterySignEdit(vpsLotterySignCog);
        return "修改成功";
    }

    public String doLotterySignDel(String infoKey, SysUserBasis currentUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("infoKey", infoKey);
        map.put("updateUser", currentUser.getUserName());
        lotterySignDao.doLotterySignDel(map);
        return "删除成功";
    }
}
