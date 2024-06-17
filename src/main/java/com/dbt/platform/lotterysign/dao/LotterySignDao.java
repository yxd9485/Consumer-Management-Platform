package com.dbt.platform.lotterysign.dao;

import com.dbt.platform.lotterysign.bean.VpsLotterySignCog;

import java.util.List;
import java.util.Map;

public interface LotterySignDao {
    List<VpsLotterySignCog> queryForList(Map<String, Object> map);

    int queryForCount(Map<String, Object> map);

    int queryOverlapActivity(Map<String, Object> map);

    void doLotterySignAdd(VpsLotterySignCog vpsLotterySignCog);

    VpsLotterySignCog findById(String infoKey);

    void doLotterySignEdit(VpsLotterySignCog vpsLotterySignCog);

    void doLotterySignDel(Map<String, Object> map);
}
