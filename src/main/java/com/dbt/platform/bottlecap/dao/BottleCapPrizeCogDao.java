package com.dbt.platform.bottlecap.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.bottlecap.bean.VpsVcodeBottlecapPrizeCogInfo;

import java.util.List;
import java.util.Map;

public interface BottleCapPrizeCogDao extends IBaseDao<VpsVcodeBottlecapPrizeCogInfo> {
    void createBottleCapPrize(List<VpsVcodeBottlecapPrizeCogInfo> bottlecapPrizeCogInfoList);

    List<VpsVcodeBottlecapPrizeCogInfo> queryPrizeCogByActivityKey(String activityKey);

    void deletePrizeCogByActivityKey(String activityKey);

    VpsVcodeBottlecapPrizeCogInfo queryPrizeCogByPrizeKey(String infoKey);

    void updatePrizeLaunchAmount(Map<String, Object> resultMap);
}
