package com.dbt.platform.bottlecap.service;

import com.dbt.framework.base.bean.Constant;
import com.dbt.framework.base.service.BaseService;
import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.DateUtil;
import com.dbt.framework.util.UUIDTools;
import com.dbt.platform.bottlecap.bean.VpsVcodeBottlecapActivityCogInfo;
import com.dbt.platform.bottlecap.bean.VpsVcodeBottlecapPrizeCogInfo;
import com.dbt.platform.bottlecap.dao.BottleCapDao;
import com.dbt.platform.bottlecap.dao.BottleCapPrizeCogDao;
import com.dbt.platform.system.bean.SysUserBasis;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BottleCapService {

    @Autowired
    private BottleCapDao bottleCapDao;
    @Autowired
    private BottleCapPrizeCogDao bottleCapPrizeCogDao;

    public List<VpsVcodeBottlecapActivityCogInfo> queryForList(VpsVcodeBottlecapActivityCogInfo queryBean, PageOrderInfo pageInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        map.put("pageInfo", pageInfo);
        return bottleCapDao.queryForList(map);
    }

    public int queryForListCount(VpsVcodeBottlecapActivityCogInfo queryBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryBean", queryBean);
        return bottleCapDao.queryForListCount(map);
    }

    public String addBottleCapActivity(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo, SysUserBasis currentUser) {
        // 校验配置时间段是否已有活动
        boolean repeatFlag = checkRepeatDate(vpsVcodeBottlecapActivityCogInfo.getStartDate(), vpsVcodeBottlecapActivityCogInfo.getEndDate(), null);
        if (!repeatFlag){
            return "当前选择时间已有活动,请重新选择";
        }

        // 新增积盖活动配置
        String activityKey = UUIDTools.getInstance().getUUID();

        // 校验规则配置格式
        String checkResult = checkRuleFormat(vpsVcodeBottlecapActivityCogInfo);
        if (checkResult != null) {
            return checkResult;
        }

        vpsVcodeBottlecapActivityCogInfo.setActivityKey(activityKey);
        vpsVcodeBottlecapActivityCogInfo.setCreateUser(currentUser.getUserName());
        vpsVcodeBottlecapActivityCogInfo.setUpdateUser(currentUser.getUserName());
        bottleCapDao.create(vpsVcodeBottlecapActivityCogInfo);

        // 集盖奖品配置修改
        updateBottleCapPrizeCog(vpsVcodeBottlecapActivityCogInfo, currentUser, activityKey);
        return "新增成功";
    }

    private boolean checkRepeatDate(String startDate, String endDate, String activityKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("activityKey", activityKey);
        int count = bottleCapDao.queryOverlapActivity(map);
        return count == 0;
    }

    public Boolean checkActivityRepeatDate(String startDate, String endDate, String activityKey) {
        return checkRepeatDate(startDate,endDate,activityKey);
    }

    private String checkRuleFormat(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo) {
        if (vpsVcodeBottlecapActivityCogInfo.getLadder().length == 0) {
            return "规则配置不能为空!";
        }
        int ruleCogNum = vpsVcodeBottlecapActivityCogInfo.getLadder().length;
        for (int i = 0; i < ruleCogNum; i++) {
            if (Integer.parseInt(vpsVcodeBottlecapActivityCogInfo.getLaunchAmount()[i]) == 0) {
                return "投放个数不能为0!";
            }
            if ("0".equals(vpsVcodeBottlecapActivityCogInfo.getBoottlecapPrizeType()[i])) {
                if (StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getMaxMoney()[i]) || StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getMinMoney()[i])) {
                    return "奖励类型为现金时配置金额区间不能为空!";
                }
                if (Double.parseDouble(vpsVcodeBottlecapActivityCogInfo.getMaxMoney()[i]) <= 0) {
                    return "金额范围不能为0!";
                }
                if (Double.parseDouble(vpsVcodeBottlecapActivityCogInfo.getMaxMoney()[i]) < Double.parseDouble(vpsVcodeBottlecapActivityCogInfo.getMinMoney()[i])) {
                    return "配置金额最大值不能小于最小值!";
                }
            }
            if ("1".equals(vpsVcodeBottlecapActivityCogInfo.getBoottlecapPrizeType()[i])) {
                if (StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getMaxVpoints()[i]) || StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getMinVpoints()[i])) {
                    return "奖励类型为积分时配置积分区间不能为空!";
                }
                if (Integer.parseInt(vpsVcodeBottlecapActivityCogInfo.getMaxVpoints()[i]) <= 0) {
                    return "积分范围不能为0!";
                }
                if (Integer.parseInt(vpsVcodeBottlecapActivityCogInfo.getMaxVpoints()[i]) < Integer.parseInt(vpsVcodeBottlecapActivityCogInfo.getMinVpoints()[i])) {
                    return "配置积分最大值不能小于最小值!";
                }
            }
            if ("2".equals(vpsVcodeBottlecapActivityCogInfo.getBoottlecapPrizeType()[i])) {
                if (StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getBigPrizeType()[i])) {
                    return "奖励类型为实物奖时实物奖配置不能为空";
                }
                if (StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getReceiveEndTime()[i])) {
                    return "奖励类型为实物奖时领取截止时间不能为空";
                }
            }
            if ("1".equals(vpsVcodeBottlecapActivityCogInfo.getActivityType())) {
                if (StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getLimitExchange()[i]) ||
                        Integer.parseInt(vpsVcodeBottlecapActivityCogInfo.getLimitExchange()[i]) <= 0) {
                    return "活动类型为累计时限制单人兑换次数不能为0!";
                }
            }
        }
        return null;
    }

    /**
     * @param vpsVcodeBottlecapActivityCogInfo
     * @param currentUser
     * @param activityKey
     */
    private void updateBottleCapPrizeCog(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo, SysUserBasis currentUser, String activityKey) {
        List<VpsVcodeBottlecapPrizeCogInfo> bottlecapPrizeCogInfoList = new ArrayList<>();
        int ruleCogNum = vpsVcodeBottlecapActivityCogInfo.getLadder().length;
        for (int i = 0; i < ruleCogNum; i++) {
            VpsVcodeBottlecapPrizeCogInfo vpsVcodeBottlecapPrizeCogInfo = new VpsVcodeBottlecapPrizeCogInfo();
            vpsVcodeBottlecapPrizeCogInfo.setInfoKey(UUIDTools.getInstance().getUUID());
            vpsVcodeBottlecapPrizeCogInfo.setBoottlecapActivityKey(activityKey);
            vpsVcodeBottlecapPrizeCogInfo.setLadder(vpsVcodeBottlecapActivityCogInfo.getLadder()[i]);
            vpsVcodeBottlecapPrizeCogInfo.setBoottlecapPrizeType(vpsVcodeBottlecapActivityCogInfo.getBoottlecapPrizeType()[i]);
            vpsVcodeBottlecapPrizeCogInfo.setConsumeBoottlecap(Integer.valueOf(vpsVcodeBottlecapActivityCogInfo.getConsumeBoottlecap()[i]));
            vpsVcodeBottlecapPrizeCogInfo.setMinMoney(Double.valueOf(vpsVcodeBottlecapActivityCogInfo.getMinMoney()[i]));
            vpsVcodeBottlecapPrizeCogInfo.setMaxMoney(Double.valueOf(vpsVcodeBottlecapActivityCogInfo.getMaxMoney()[i]));
            vpsVcodeBottlecapPrizeCogInfo.setMinVpoints(Integer.valueOf(vpsVcodeBottlecapActivityCogInfo.getMinVpoints()[i]));
            vpsVcodeBottlecapPrizeCogInfo.setMaxVpoints(Integer.valueOf(vpsVcodeBottlecapActivityCogInfo.getMaxVpoints()[i]));
            if ("2".equals(vpsVcodeBottlecapActivityCogInfo.getBoottlecapPrizeType()[i])) {
                vpsVcodeBottlecapPrizeCogInfo.setReceiveEndTime(vpsVcodeBottlecapActivityCogInfo.getReceiveEndTime()[i]);
            }
            if (vpsVcodeBottlecapActivityCogInfo.getBigPrizeType().length > 0) {
                vpsVcodeBottlecapPrizeCogInfo.setBigPrizeType(vpsVcodeBottlecapActivityCogInfo.getBigPrizeType()[i]);
            }
            vpsVcodeBottlecapPrizeCogInfo.setLaunchAmount(Integer.valueOf(vpsVcodeBottlecapActivityCogInfo.getLaunchAmount()[i]));
            if ("1".equals(vpsVcodeBottlecapActivityCogInfo.getActivityType())) {
                vpsVcodeBottlecapPrizeCogInfo.setLimitExchange(Integer.valueOf(vpsVcodeBottlecapActivityCogInfo.getLimitExchange()[i]));
            }
            vpsVcodeBottlecapPrizeCogInfo.setDeleteFlag("0");
            vpsVcodeBottlecapPrizeCogInfo.setCreateUser(currentUser.getUserName());
            vpsVcodeBottlecapPrizeCogInfo.setCreateTime(DateUtil.getDateTime());
            vpsVcodeBottlecapPrizeCogInfo.setUpdateUser(currentUser.getUserName());
            vpsVcodeBottlecapPrizeCogInfo.setUpdateTime(DateUtil.getDateTime());
            bottlecapPrizeCogInfoList.add(vpsVcodeBottlecapPrizeCogInfo);
        }
        bottleCapPrizeCogDao.createBottleCapPrize(bottlecapPrizeCogInfoList);

    }

    public VpsVcodeBottlecapActivityCogInfo queryActivityCogByKey(String activityKey) {
        return bottleCapDao.queryActivityCogByKey(activityKey);
    }

    public List<VpsVcodeBottlecapPrizeCogInfo> queryPrizeCogByActivityKey(String activityKey) {
        return bottleCapPrizeCogDao.queryPrizeCogByActivityKey(activityKey);
    }

    public String doBottleCapActivityEdit(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo, SysUserBasis currentUser) {

        if (StringUtils.isBlank(vpsVcodeBottlecapActivityCogInfo.getStartDate())) {
            vpsVcodeBottlecapActivityCogInfo.setStartDate(vpsVcodeBottlecapActivityCogInfo.getNotEditStartDate());
        }

        // 根据活动时间判断修改类型
        int updateFlag = checkUpdateFlag(vpsVcodeBottlecapActivityCogInfo);

        String msg = null;
        if (updateFlag == 0) {
            // 未开始 未开始时活动可以全部修改
            String checkResult = checkRuleFormat(vpsVcodeBottlecapActivityCogInfo);
            if (checkResult != null) {
                return checkResult;
            }

            msg = notStartedActivityUpdate(vpsVcodeBottlecapActivityCogInfo, currentUser);
        }  else if (updateFlag == 1) {
            // 已上线活动
            msg = inProgressActivityUpdate(vpsVcodeBottlecapActivityCogInfo, currentUser);
        }

        return msg;
    }

    private String inProgressActivityUpdate(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo, SysUserBasis currentUser) {
        // 验证奖品投放数量
        if (vpsVcodeBottlecapActivityCogInfo.getLadder().length == 0) {
            return "规则配置不能为空!";
        }
        int ruleCogNum = vpsVcodeBottlecapActivityCogInfo.getLadder().length;
        for (int i = 0; i < ruleCogNum; i++) {
            VpsVcodeBottlecapPrizeCogInfo vpsVcodeBottlecapPrizeCogInfo = bottleCapPrizeCogDao.queryPrizeCogByPrizeKey(vpsVcodeBottlecapActivityCogInfo.getPrizeInfoKey()[i]);
            if (vpsVcodeBottlecapPrizeCogInfo.getReceiveAmount() > Integer.parseInt(vpsVcodeBottlecapActivityCogInfo.getLaunchAmount()[i])) {
                return "投放个数不能小于已投放个数!";
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("infoKey", vpsVcodeBottlecapActivityCogInfo.getPrizeInfoKey()[i]);
            resultMap.put("launchAmount", vpsVcodeBottlecapActivityCogInfo.getLaunchAmount()[i]);
            resultMap.put("updateUser", currentUser.getUserName());
            bottleCapPrizeCogDao.updatePrizeLaunchAmount(resultMap);
        }

        VpsVcodeBottlecapActivityCogInfo bottlecapActivityCogInfo = new VpsVcodeBottlecapActivityCogInfo();
        bottlecapActivityCogInfo.setActivityKey(vpsVcodeBottlecapActivityCogInfo.getActivityKey());
        bottlecapActivityCogInfo.setEndDate(vpsVcodeBottlecapActivityCogInfo.getEndDate());
        bottlecapActivityCogInfo.setActivitySku(vpsVcodeBottlecapActivityCogInfo.getActivitySku());
        bottlecapActivityCogInfo.setBannerPic(vpsVcodeBottlecapActivityCogInfo.getBannerPic());
        bottlecapActivityCogInfo.setRoleDescribe(vpsVcodeBottlecapActivityCogInfo.getRoleDescribe());
        bottlecapActivityCogInfo.setRemarks(vpsVcodeBottlecapActivityCogInfo.getRemarks());
        bottlecapActivityCogInfo.setUpdateUser(currentUser.getUserName());
        bottleCapDao.update(bottlecapActivityCogInfo);
        return "修改成功";
    }

    private String notStartedActivityUpdate(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo, SysUserBasis currentUser) {
        // 新增积盖活动配置
        vpsVcodeBottlecapActivityCogInfo.setUpdateUser(currentUser.getUserName());
        bottleCapDao.update(vpsVcodeBottlecapActivityCogInfo);

        if (vpsVcodeBottlecapActivityCogInfo.getLadder().length == 0) {
            return "规则配置不能为空!";
        }

        // 清除原有规则配置
        deleteBottleCapPrizeCogByKey(vpsVcodeBottlecapActivityCogInfo.getActivityKey());
        // 积盖奖品配置修改
        updateBottleCapPrizeCog(vpsVcodeBottlecapActivityCogInfo, currentUser, vpsVcodeBottlecapActivityCogInfo.getActivityKey());

        return "修改成功";
    }

    private void deleteBottleCapPrizeCogByKey(String activityKey) {
        bottleCapPrizeCogDao.deletePrizeCogByActivityKey(activityKey);
    }

    private int checkUpdateFlag(VpsVcodeBottlecapActivityCogInfo vpsVcodeBottlecapActivityCogInfo) {
        int updateFlag;
        if (DateUtil.getDate().compareTo(vpsVcodeBottlecapActivityCogInfo.getStartDate()) >= 0
                && DateUtil.getDate().compareTo(vpsVcodeBottlecapActivityCogInfo.getEndDate()) <= 0) {
            updateFlag = 1;
        } else if (DateUtil.getDate().compareTo(vpsVcodeBottlecapActivityCogInfo.getEndDate()) > 0) {
            updateFlag = 2;
        } else {
            updateFlag = 0;
        }
        return updateFlag;
    }
}
